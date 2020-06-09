package com.example.hp.sjdf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainNotification extends AppCompatActivity
{

    Button btn,btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notification);

        btn= (Button)findViewById(R.id.button6);
        btn1= (Button)findViewById(R.id.button7);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent event = new Intent(getApplicationContext(),Notification.class);
                startActivity(event);
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Vote = new Intent(getApplicationContext(),VoteNotification.class);
                startActivity(Vote);
            }
        });
    }
}
