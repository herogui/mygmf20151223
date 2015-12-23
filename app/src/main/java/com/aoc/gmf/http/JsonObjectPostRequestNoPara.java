package com.aoc.gmf.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Acker on 2014/12/18.
 */

public class JsonObjectPostRequestNoPara extends Request<JSONObject> {
    private Response.Listener<JSONObject> mListener;
    public String cookieFromResponse="";
    private String mHeader;
    private Map<String, String> sendHeader=new HashMap<String, String>(1);
    public JsonObjectPostRequestNoPara(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            mHeader = response.headers.toString();

            Pattern pattern=Pattern.compile("Set-Cookie.*?;");
            Matcher m=pattern.matcher(mHeader);
            if(m.find()){
                cookieFromResponse =m.group();
            }

            JSONObject jsonObject = new JSONObject(jsonString);
            if(!cookieFromResponse.equals("")) {
                cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
                jsonObject.put("Cookie", cookieFromResponse);
            }
            else  jsonObject.put("Cookie", "");

            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return sendHeader;
    }

    public void setSendCookie(String cookie){
        sendHeader.put("Cookie",cookie);
    }
}