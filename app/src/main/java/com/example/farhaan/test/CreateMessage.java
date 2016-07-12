package com.example.farhaan.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

/**
 * Created by Farhaan on 02-06-2016.
 */
public class CreateMessage extends Activity {

    ListView chat;
    EditText chatLine;
    Button send;
    String sender, receiver, message, receiveUrl, sendUrl;
    RequestQueue requestQueue;
    public ChatArrayAdapter chatArrayAdapter;
    private boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
        chat = (ListView) findViewById(R.id.MyChat);
        chatLine = (EditText) findViewById(R.id.NewMessage);
        send = (Button) findViewById(R.id.SendMessage);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        System.out.println("Values:sender = " + Values.sender);
        receiveUrl = Values.baseUrl + "getChat.php?sender=" + Values.sender + "&receiver=" + Values.receiver;
        System.out.println(receiveUrl);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(receiveUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONArray details = response.getJSONArray("result");
                    for (int i = 0; i < details.length(); i++) {
                        JSONObject detail = details.getJSONObject(i);
                        String chatMessage = detail.getString("message");
                        sender = detail.getString("sender");
                        System.out.println(sender);
                        System.out.println(chatMessage);
                        if (sender.equals(Values.sender)) {
                            side = true;
                        } else {
                            side = false;
                        }
                        System.out.println(side);
                        chatArrayAdapter.add(new ChatMessage(side, chatMessage));
                    }
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
        chat.setAdapter(chatArrayAdapter);
        update();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = chatLine.getText().toString();
                message = message.replace(" ", "%20");
                sendUrl = Values.baseUrl + "addMessage.php?sender=" + Values.sender + "&receiver=" + Values.receiver + "&message=" + message;
                System.out.println(sendUrl);
                StringRequest request = new StringRequest(sendUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(request);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    public void update() {
        new Thread() {
            public void run() {
                for (; ; ) {
                    if (Values.update) {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatArrayAdapter.add(new ChatMessage(false, Values.newMessage));
                                    chatArrayAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    final long NANOSEC_PER_SEC = 1000 * 1000 * 1000;
                    long startTime = System.nanoTime();
                    while ((System.nanoTime() - startTime) < 1 * 15 * NANOSEC_PER_SEC) {
                    }
                }
            }
        }.start();
    }
}
