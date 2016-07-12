package com.example.farhaan.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farhaan on 02-06-2016.
 */
public class ContactsList extends Activity implements AdapterView.OnItemClickListener {

    private ListView lv;
    List<String> mylist = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        lv = (ListView) findViewById(R.id.myListView);
        //List<String> mylist = new ArrayList<String>();
        DatabaseHandler newdb = new DatabaseHandler(this);
        mylist = newdb.getAllContacts();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(this);
    }

    /*public void goToAdd(View view) {
        Intent intent = new Intent(ContactsList.this, AddContact.class);
        startActivity(intent);
    }*/

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        try {
            String output = mylist.get(pos).toString().trim();
            Intent intent1 = new Intent(ContactsList.this, CreateMessage.class);
            Values.receiver = output;
            //Bundle bundle = new Bundle();
            //bundle.putString("contactName", output);
            //bundle.putString("stuff", Values.sender);
            //intent1.putExtras(bundle);
            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void goToInbox(View view) {
        Intent intent2 = new Intent(ContactsList.this, AllMessages.class);
        //String output = Values.sender;
        //Bundle bundle = new Bundle();
        //bundle.putString("stuff", output);
        //intent1.putExtras(bundle);
        startActivity(intent2);
    }*/
}
