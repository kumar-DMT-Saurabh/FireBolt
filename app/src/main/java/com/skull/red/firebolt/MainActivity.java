package com.skull.red.firebolt;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.annotation.IntDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.MissingFormatArgumentException;

public class MainActivity extends AppCompatActivity {


    private String TAG =MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<Repository> repoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repoList=new ArrayList<Repository>();
        lv=(ListView)findViewById(R.id.list);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    onLongClick( lv,parent, view,  position, id);
                return false;
            }
        });

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"JSON data is being downloaded",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh=new HttpHandler();
            String url ="https://api.github.com/users/xing/repos";
//            String url ="http://api.androidhive.info/contacts/";
            String jsonStr =sh.makeServiceCall(url);

            Log.e(TAG,"Response from URL: "+jsonStr);
            if(jsonStr !=null){
                try{

                    JSONArray jsonArray= new JSONArray(jsonStr);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject infoObj= jsonArray.getJSONObject(i);
                        String name=infoObj.getString("name");
                        String desc=infoObj.getString("description");
                        String htmlUrl=infoObj.getString("html_url");
                        boolean fork=infoObj.getBoolean("fork");

                        JSONObject ownerObj= infoObj.getJSONObject("owner");
                        String login=ownerObj.getString("login");
                        String ownerUrl=ownerObj.getString("html_url");

                        Repository repo=new Repository();
                        repo.setOwnerUrl(ownerUrl);
                        repo.setUrl(url);
                        repo.setFork(fork);
                        repo.setName(name);
                        repo.setLogin(login);
                        repo.setDescription(desc);
                        repoList.add(repo);
                    }

                }
                catch (final Exception e)
                {
                    Log.e(TAG,"JSON parsing error "+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error:  main activity",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            MyCustomAdapter adapter=new MyCustomAdapter(MainActivity.this,repoList);

            lv.setAdapter(adapter);
        }
    }

    private void onLongClick(ListView lv, AdapterView<?> parent, View view, int position, long id)
    {
        final Repository repo=(Repository) lv.getItemAtPosition(position);
        repo.isFork();

        AlertDialog.Builder alertDialogBuilder =    new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(repo.getUrl()+ "  "+repo.getOwnerUrl());
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Owner Url", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri =Uri.parse(repo.getOwnerUrl());
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("User Url", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri =Uri.parse(repo.getUrl());
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
