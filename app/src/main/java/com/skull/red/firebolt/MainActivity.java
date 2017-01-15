package com.skull.red.firebolt;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private String TAG =MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String,String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList=new ArrayList<>();
        lv=(ListView)findViewById(R.id.list);

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
            String url ="http://api.androidhive.info/contacts/";
            String jsonStr =sh.makeServiceCall(url);

            Log.e(TAG,"Response from URL: "+jsonStr);
            if(jsonStr !=null){
                try{
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //Getting  JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");

                    //looping through all contactd
                    for (int i=0; i<contacts.length();i++) {
                        JSONObject c = contacts.optJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        //Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                }
                catch (final Exception e)
                {
                    Log.e(TAG,"JSON parsing error "+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
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
            ListAdapter adapter = new SimpleAdapter(MainActivity.this,contactList,
                    R.layout.list_item, new String[]{"email","mobile"},
                    new int[]{R.id.email,R.id.mobile});
            lv.setAdapter(adapter);
        }
    }
}
