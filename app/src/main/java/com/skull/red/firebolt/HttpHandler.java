package com.skull.red.firebolt;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Created by Kumar Saurabh on 1/14/2017.
 */

public class HttpHandler {


    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler()
    {
    }

    public String makeServiceCall(String reqUrl)
    {
        String response=null;

        try{
            URL url=new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");

            //read the response
            InputStream in= new BufferedInputStream(conn.getInputStream());
            response=convertStreamToString(in);
        }
        catch (Exception e)
        {
            Log.e(TAG,"Exception inhttp handler ");
        }
        return response;
    }

    private String convertStreamToString(InputStream is)
    {
        BufferedReader reader =new BufferedReader(new InputStreamReader(is));
        StringBuilder sb= new StringBuilder();

        String line;

        try
        {
            while ((line =reader.readLine())!=null)
            {
                sb.append(line).append('\n');
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  sb.toString();
    }
}
