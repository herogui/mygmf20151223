package com.aoc.gmf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LonActivity extends Activity {

    ArrayList<CheckBox>  listchk;
    private Button login = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lon);

        listchk = new ArrayList<CheckBox>();
        listchk.add((CheckBox)findViewById(R.id.ck1));
        listchk.add((CheckBox)findViewById(R.id.ck2));
        listchk.add((CheckBox)findViewById(R.id.ck3));
        listchk.add((CheckBox)findViewById(R.id.ck4));
        listchk.add((CheckBox)findViewById(R.id.ck5));
        listchk.add((CheckBox)findViewById(R.id.ck6));
        listchk.add((CheckBox)findViewById(R.id.ck7));
        listchk.add((CheckBox)findViewById(R.id.ck8));

        login = (Button)findViewById(R.id.login2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num = 0;

                for(int i = 0;i<listchk.size();i++)
                {
                    if(listchk.get(i).isChecked()) {
                        num++;
                    }
                }


                if(num<1 )
                {
                    Toast.makeText(LonActivity.this,"请选择一个用户！", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(num>1)
                {
                    Toast.makeText(LonActivity.this,"一次只能选择一个用户！", Toast.LENGTH_SHORT).show();
                    return;
                }
                CheckBox checkBox = null;
                for(int i = 0;i<listchk.size();i++)
                {
                    if(listchk.get(i).isChecked()) {
                        checkBox = listchk.get(i);
                        break;
                    }
                }

                Intent intent = new Intent();
                intent.setClass(LonActivity.this, MainActivity.class);
                intent.putExtra("user",checkBox.getText());
                startActivity(intent);
            }
        });
    }

}
