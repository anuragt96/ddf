package com.example.hp.sjdf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private CardView profilecard,allusercard,notificationcard,resultcard,logoutcard,extracard;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        profilecard = (CardView)findViewById(R.id.profile_card);
        allusercard = (CardView)findViewById(R.id.allusers_card);
        notificationcard = (CardView)findViewById(R.id.notification_card);
        resultcard = (CardView)findViewById(R.id.resultgeneration_card);
        logoutcard = (CardView)findViewById(R.id.logout_card);
        extracard = (CardView)findViewById(R.id.extra_card);

        profilecard.setOnClickListener(this);
        allusercard.setOnClickListener(this);
        notificationcard.setOnClickListener(this);
        resultcard.setOnClickListener(this);
        logoutcard.setOnClickListener(this);
        extracard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent i;

        switch (v.getId())
        {
            case R.id.profile_card:             i = new Intent(this,HomeActivity.class); startActivity(i); break;

            case R.id.allusers_card:            i = new Intent(this,Contact_list_Activity.class); startActivity(i); break;

            case R.id.notification_card:        i = new Intent(this,MainNotification.class); startActivity(i); break;

            case R.id.resultgeneration_card:
                mAuth.getInstance().signOut();
                i = new Intent(this,LginActivity.class); startActivity(i); break;

            case R.id.logout_card:              i = new Intent(this,ShowVote.class); startActivity(i); break;

            case R.id.extra_card:               i = new Intent(this,ShowEvent.class); startActivity(i); break;

            default: break;

        }
    }
}