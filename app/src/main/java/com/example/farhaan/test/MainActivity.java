package com.example.farhaan.test;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;

/*import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;*/

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String contact = "Dehla";
        DatabaseHandler newdb = new DatabaseHandler(this);
        newdb.deleteAll();
        newdb.addAnotherContact(contact);
    }

    public void goToInbox(View view){
        Intent intent = new Intent(MainActivity.this, AllMessages.class);
        name = (EditText) findViewById(R.id.identification);
        Values.sender = name.getText().toString();
        Notifying();
        /*final PendingIntent pIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), intent, 0);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getBaseContext()).setSmallIcon(R.drawable.bubble_b)
                .setContentTitle("My notification")
                .setContentText("This is for test")
                .setAutoCancel(true);
        mBuilder.setContentIntent(pIntent);
        mNotificationManager.notify(0, mBuilder.build());*/
        startActivity(intent);
    }

    public void Notifying(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Values.baseUrl + "getCount.php?receiver=" + Values.sender;
                System.out.println(url);
                Intent intent3 = new Intent(MainActivity.this, AllMessages.class);
                final PendingIntent pIntent = PendingIntent.getActivity(MainActivity.this, (int) System.currentTimeMillis(), intent3, 0);
                final Intent intent4 = new Intent(MainActivity.this, CreateMessage.class);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                for(;;) {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                            try {
                                JSONArray details = response.getJSONArray("result");
                                JSONObject detail = details.getJSONObject(0);
                                int count = detail.getInt("id");
                                System.out.println(count);
                                if(count > Values.message_count){
                                    if(detail.getString("sender").equals(Values.receiver)){
                                        Values.update = true;
                                        Values.newMessage = detail.getString("message");
                                    } else {
                                        Values.update = false;
                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext());
                                        mBuilder.setSmallIcon(R.drawable.bubble_a);
                                        mBuilder.setContentTitle(detail.getString("sender"));
                                        mBuilder.setContentText(detail.getString("message"));
                                        mBuilder.setAutoCancel(true);
                                        mBuilder.setContentIntent(pIntent);
                                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        mNotificationManager.notify(0, mBuilder.build());
                                    }
                                }
                                Values.message_count = count;
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
                    final long NANOSEC_PER_SEC = 1000*1000*1000;
                    long startTime = System.nanoTime();
                    while ((System.nanoTime()-startTime)< 30*NANOSEC_PER_SEC){
                    }
                }
            }
        });
        thread.start();
    }

}
