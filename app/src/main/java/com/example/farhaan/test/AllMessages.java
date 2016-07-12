package com.example.farhaan.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap.Config;

/**
 * Created by Farhaan on 26-05-2016.
 */
public class AllMessages extends Activity implements AdapterView.OnItemClickListener{

    String name, url;
    //String baseUrl = "http://192.168.2.11/getData.php?sender=";
    final List<String> myContacts = new ArrayList<String>();
    RequestQueue requestQueue;
    ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    String friend;
    int j;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inbox);
        listView = (ListView) findViewById(R.id.myMessages);
        //Bundle bundle = getIntent().getExtras();
        //name = bundle.getString("stuff");
        //myContacts.add("anyone");
        Values.receiver = "";
        url = Values.baseUrl + "getData.php?sender=" + Values.sender;
        System.out.println(url);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray details = response.getJSONArray("result");
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject detail = details.getJSONObject(i);
                        //System.out.println("i = " + i);
                        if(detail.getString("sender").equals(Values.sender)){
                            friend = detail.getString("receiver");
                            //System.out.println("if " + friend);
                        } else {
                            friend = detail.getString("sender");
                            //System.out.println("else " + friend);
                        }
                        //System.out.println("myContacts size = " + myContacts.size());
                        for(j = 0;j < myContacts.size();j++){
                            if(friend.equals(myContacts.get(j)))
                                break;
                        }
                        //System.out.println("j = " + j);
                        if (j == myContacts.size()) {
                            myContacts.add(friend);
                            System.out.println(myContacts.get(j));
                        }
                    }
                    arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, myContacts);
                    listView.setAdapter(arrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }
        });
        requestQueue.add(jsonObjectRequest);
        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myContacts);
        listView.setAdapter(arrayAdapter);*/
        listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        try {
            String output = myContacts.get(pos).toString().trim();
            System.out.println(output);
            Values.receiver = output;
            Intent intent = new Intent(AllMessages.this, CreateMessage.class);
            /*Bundle bundle = new Bundle();
            bundle.putString("contactName",output);
            bundle.putString("stuff",name);
            intent.putExtras(bundle);*/
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToAdd(View view){
        Intent intent = new Intent(AllMessages.this, AddContact.class);
        startActivity(intent);
    }

    public void Logout(View view) {
        Values.sender = "";
        Intent intent2 =  new Intent(AllMessages.this, MainActivity.class);
        startActivity(intent2);
    }

}
