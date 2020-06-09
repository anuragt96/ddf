package com.example.hp.sjdf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Setusername extends AppCompatActivity {
    Button setbutton;
    EditText e1;
    FirebaseAuth mAuth;
    TextView tv;
    private DocumentReference mUser;
    private FirebaseUser mCurrentUser;
    private ProgressBar mProgressbar;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setusername);
        //tv=(TextView)findViewById(R.id.textView4);
        mAuth = FirebaseAuth.getInstance();
        setbutton=(Button)findViewById(R.id.set_username_button);
        e1=(EditText)findViewById(R.id.username_edittext);
        mProgressbar=(ProgressBar)findViewById(R.id.progressBar2);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_user_id=mCurrentUser.getUid();
        mUser= FirebaseFirestore.getInstance().collection("users").document(current_user_id);

        mFirestore = FirebaseFirestore.getInstance();

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null)
        {
            //final String username = acct.getDisplayName();
            //      String personGivenName = acct.getGivenName();
            //       String personFamilyName = acct.getFamilyName();
            final String personEmail = acct.getEmail();
            //     String personId = acct.getId();
            //     Uri personPhoto = acct.getPhotoUrl();


            setbutton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    mProgressbar.setVisibility(View.VISIBLE);
                    final String username = e1.getText().toString();
                    //    String phoneno = et2.getText().toString();
                    mAuth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult getTokenResult) {
                            String token_id=getTokenResult.getToken();



                            Map<String, String> userMap = new HashMap<>();
                            userMap.put("Name", username);
                            userMap.put("Email", personEmail);
                            userMap.put("image", "default");
                            userMap.put("thumb_image", "default");
                            userMap.put("token_id",token_id );
                            String current_user_id = mCurrentUser.getUid();
                            mFirestore.collection("users").document(current_user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(Setusername.this, "Your Name Is Saved",Toast.LENGTH_SHORT).show();
                                    mProgressbar.setVisibility(View.INVISIBLE);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(Setusername.this, "Data entry failed", Toast.LENGTH_SHORT).show();
                                    mProgressbar.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    });


                }
            });
        }

    }
}
