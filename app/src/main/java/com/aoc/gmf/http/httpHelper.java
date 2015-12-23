package com.aoc.gmf.http;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.aoc.gmf.MyEvent.EventSourceObject;

import org.json.JSONObject;

/**
 * Created by giser on 2015/5/18.
 */
public class httpHelper {
    //webservice
    public EventSourceObject GetResEvent = new EventSourceObject();
    public static String  errorTip  = "error";

    public void XmlHttp(RequestQueue mRequestQueue, String url, final String nameSpace) {
        StringRequest jr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String res = response.toString();
                String[] strs = res.split(nameSpace + "\">"); //parkingwebservice
                String[] strs2 = strs[1].split("</");
                String result = strs2[0];

                //�����¼�
                GetResEvent.setString(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GetResEvent.setString(errorTip);
            }
        });
        mRequestQueue.add(jr);
        mRequestQueue.start();
    }

    public void StringHttp(RequestQueue mRequestQueue, String url) {
        StringRequest jr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String res = response.toString();
                GetResEvent.setString(res);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GetResEvent.setString(errorTip+error.getMessage());

            }
        });
        mRequestQueue.add(jr);
        mRequestQueue.start();
    }

    public void JsonHttp(RequestQueue mRequestQueue, String url) {

        JsonObjectRequest jr = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                GetResEvent.setString(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //GetResEvent.setString(errorTip);

                GetResEvent.setString(errorTip);
            }
        });
        mRequestQueue.add(jr);
        mRequestQueue.start();
    }
}
