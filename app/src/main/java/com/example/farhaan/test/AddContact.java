package com.example.farhaan.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farhaan on 02-06-2016.
 */
public class AddContact extends Activity implements AdapterView.OnItemClickListener{

    Button button;
    EditText nameText;
    private ListView lv;
    List<String> mylist = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nameText = (EditText) findViewById(R.id.enteredName);
        button = (Button) findViewById(R.id.addContact);
        lv = (ListView) findViewById(R.id.myListView);
        Values.receiver="";
        DatabaseHandler newdb1 = new DatabaseHandler(this);
        mylist = newdb1.getAllContacts();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewContact();
            }
        });
    }

    public void addNewContact() {
        String contact = nameText.getText().toString();
        DatabaseHandler newdb = new DatabaseHandler(this);
        newdb.addAnotherContact(contact);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        try {
            String output = mylist.get(pos).toString().trim();
            Intent intent1 = new Intent(AddContact.this, CreateMessage.class);
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

    public void goToInbox(View view) {
        Intent intent = new Intent(AddContact.this, AllMessages.class);
        //Bundle bundle = new Bundle();
        //bundle.putString("stuff", Values.sender);
        //intent.putExtras(bundle);
        startActivity(intent);
    }

    /*public void goToContacts(View view) {
        Intent intent1 = new Intent(AddContact.this, ContactsList.class);
        startActivity(intent1);
    }*/
}
