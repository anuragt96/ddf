package com.example.hp.sjdf;

import android.accounts.Account;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
{
    private final static String TAG = HomeActivity.class.getSimpleName();

    private GoogleSignInClient mGoogleSignInClient;
    ImageButton btn1,btn2;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private GoogleApiClient mGoogleApiClient;
    private static final int GALLERY_PICK = 1;
    private FirebaseUser mCurrentUser;
    private DocumentReference mUser;
   // private ProgressBar mProgressBar;
    private Button SignOut;
    private CircleImageView circleImageView;
    private TextView tv,tv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn1=(ImageButton)findViewById(R.id.pencil);
        btn2=(ImageButton)findViewById(R.id.mcam);
        circleImageView = (CircleImageView)findViewById(R.id.picid);
        tv=(TextView)findViewById(R.id.textView);
        tv1 = (TextView)findViewById(R.id.textview_display);
        //mProgressBar = (ProgressBar)findViewById(R.id.progressBar3);
        SignOut =(Button)findViewById(R.id.logout_button);

        mAuth = FirebaseAuth.getInstance();
        //String current_user_id = mCurrentUser.getUid();
        String current_user_id = mAuth.getCurrentUser().getUid();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);
        SignOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                mAuth.signOut();
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                       // updateUI(null);
//                Intent signout=new Intent(getApplicationContext(),LginActivity.class);
//                startActivity(signout);
//                    }
//                });
                mAuth.getInstance().signOut();
                Intent signout=new Intent(getApplicationContext(),LginActivity.class);
                startActivity(signout);
            }

        });

        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent u = new Intent(getApplicationContext(),Setusername.class);
                startActivity(u);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cam = new Intent(getApplicationContext(),UploadActivity.class);
                startActivity(cam);
            }
        });


        mUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists() && documentSnapshot !=null)
                    {
                        DocumentSnapshot doc = task.getResult();
//                        StringBuilder fields = new StringBuilder("");
//                        //      StringBuilder name = new StringBuilder("");
//                        //     StringBuilder image = new StringBuilder("");
//                        fields.append("").append(doc.get("Email"));
//                        //       image.append(" ").append(doc.get("image"));
//                        //     name.append(" ").append(doc.get("Name"));
//                        //  fields.append("\nEmail: ").append(doc.get("Email"));
//                        //  fields.append("\nPhone: ").append(doc.get("Phone"));
//                        tv.setText(fields.toString());
//                           tv1.setText(name.toString());
                        //           Picasso.with(getActivity()).load(String.valueOf(image)).into(circleImageView);
                        String username = doc.getString("Name");
                        tv1.setText(username);
                        String email = doc.getString("Email");
                        tv.setText(email);
                        String image = doc.getString("image");
                        Glide.with(getApplicationContext()).load(image).into(circleImageView);

//                        mProgressBar.setVisibility(View.VISIBLE);
//                        Picasso.with(getApplicationContext()).load(String.valueOf(image)).fit().error(R.drawable.defaultimage)
//                                .into(circleImageView,new com.squareup.picasso.Callback()
//                                {
//                                    @Override
//                                    public void onSuccess()
//                                    {
//                                        if (mProgressBar != null)
//                                        {
//                                            mProgressBar.setVisibility(View.GONE);
//                                            Toast.makeText(HomeActivity.this, "Profile Photo Updated", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError()
//                                    {
//                                        mProgressBar.setVisibility(View.INVISIBLE);
//                                        Toast.makeText(HomeActivity.this, "Profile Photo couldn't Be Updated", Toast.LENGTH_SHORT).show();
//                                    }
//                                });

                    }
                }
            }
        });


    }

}
