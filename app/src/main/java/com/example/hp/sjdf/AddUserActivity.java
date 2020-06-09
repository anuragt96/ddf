package com.example.hp.sjdf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {
    TextView tv,tv1,tv2;
    Button adduser;
    private DocumentReference mUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mref;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String TAG;
    private ImageView iv;
    ProgressBar mbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        tv=(TextView)findViewById(R.id.textView5);
        tv1=(TextView)findViewById(R.id.name_textview);
        tv2=(TextView)findViewById(R.id.email_textview);
        adduser = (Button)findViewById(R.id.button3);
        mbar = (ProgressBar)findViewById(R.id.progressBar4);
        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);
        mref = FirebaseFirestore.getInstance().collection("contact_list");
        iv=(ImageView)findViewById(R.id.imageView);

        Intent intent=getIntent();
        final String id=intent.getStringExtra("q");
        final String name=intent.getStringExtra("name");
        final String email=intent.getStringExtra("email");
        final String image=intent.getStringExtra("image");
        final String token_id=intent.getStringExtra("token");
        tv.setText(id);
        tv1.setText(name);
        tv2.setText(email);
        Glide.with(getApplicationContext()).load(image).into(iv);

        adduser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                com.google.firebase.firestore.Query query = mref.whereEqualTo("Id", id).whereEqualTo("Id", id);
//                if(equals(query))
//                {
//
//                }
                mbar.setVisibility(View.VISIBLE);
                Map<String, String> userMap = new HashMap<>();
                userMap.put("Name", name);
                userMap.put("Email", email);
                userMap.put("image", image);
                userMap.put("Id", id);
                userMap.put("token_id",token_id);
                mUser.collection("contact_list").add(userMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference)
                            {
                                mbar.setVisibility(View.INVISIBLE);

                                //   Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                Toast.makeText(AddUserActivity.this, "User Is Added To Your Contact List", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Log.w(TAG, "Error adding document", e);
                                mbar.setVisibility(View.INVISIBLE);

                                Toast.makeText(AddUserActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });

                adduser.setText("User Added");
                adduser.setVisibility(View.INVISIBLE);

            }
        });
    }


}
