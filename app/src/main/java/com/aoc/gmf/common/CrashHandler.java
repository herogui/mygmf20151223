package com.aoc.gmf.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.session.PlaybackState;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

/**
 * Created by giser on 2015/8/26.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler
{
    private static CrashHandler instance;

    public static final String TAG = "CrashHandler";

    private static final String VERSION_NAME = "versionName";

    private static final String VERSION_CODE = "versionCode";

    private static final String STACK_TRACE = "STACK_TRACE";

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;

    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    private Properties deviceCrashInfo = new Properties();

    private CrashHandler()
    {
    }

    public static synchronized CrashHandler getInstance()
    {
        if (instance == null)
        {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context ctx)
    {
        this.mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        if (mDefaultHandler != null && !handlerException(ex))
        {
// 如果用户没有进行异常处理就让系统默认的处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else
        {
            exitApp();
        }
    }

    private boolean handlerException(Throwable ex)
    {
        if (ex == null)
        {
            return true;
        }
        final String msg = ex.toString();
        new Thread() {
            @Override
            public void run()
            {
                Looper.prepare();
                //Toast toast = Toast.makeText(mContext, "程序出错啦！" + msg, Toast.LENGTH_SHORT);
                new AlertDialog.Builder(mContext)
                        .setTitle("错误提示")
                        .setMessage(msg)
                      //  .setMessage("app出现异常了！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exitApp();
                            }
                        })
                        .show();
                //toast.setGravity(Gravity.CENTER, 0, 0);
                //toast.show();
                Looper.loop();
            }
        }.start();
// 收集设备信息
        collectDeviceInfo(mContext);
// 保存错误
        saveCrashInfoToFiles(ex);
// 发送错误报告到服务器
        sendCrashReportsToServer(mContext);
        return true;
    }

    private void collectDeviceInfo(Context ctx)
    {
        try
        {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null)
            {
                deviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set"
                        : pi.versionName);
                deviceCrashInfo.put(VERSION_CODE, pi.versionCode);
            }
        } catch (Exception e)
        {
            Log.e(TAG, "error occured when collecting crash info" + e);
        }

// 使用反射来收集设备信息.在Build类中包含各种设备信息,
// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
// 具体信息请参考后面的截图
        Field[] fields = PlaybackState.Builder.class.getDeclaredFields();
        for (Field field : fields)
        {
            try
            {
                field.setAccessible(true);
                deviceCrashInfo.put(field.getName(), null);
            } catch (Exception e)
            {
                Log.e(TAG, "error occured when collecting device info" + e);
            }
        }
    }

    private String saveCrashInfoToFiles(Throwable ex)
    {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null)
        {
            ex.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        deviceCrashInfo.put("EXCEPTION", ex.getLocalizedMessage());
        deviceCrashInfo.put(STACK_TRACE, result);

        try
        {
            long time = System.currentTimeMillis();
            String filename = "crash-" + time + CRASH_REPORTER_EXTENSION;
            FileOutputStream trace = mContext.openFileOutput(filename, Context.MODE_PRIVATE);

            deviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
        } catch (Exception e)
        {
            Log.e(TAG, "error occured when save crashinfo to files" + e);
        }
        return null;
    }

    private void sendCrashReportsToServer(Context ctx)
    {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0)
        {
            TreeSet<String> sortedFiles = new TreeSet<String>();
            sortedFiles.addAll(Arrays.asList(crFiles));
            for (String fileName : sortedFiles)
            {
                File cr = new File(ctx.getFilesDir(), fileName);
                postReport(cr);
                cr.delete();// 删除已发送的报告
            }
        }
    }

    private void postReport(File file)
    {
// TODO 发送错误报告到服务器
    }

    private String[] getCrashReportFiles(Context ctx)
    {
        File filesDir = ctx.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    private void exitApp()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
