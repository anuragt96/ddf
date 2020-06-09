package com.example.hp.sjdf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static android.R.string.yes;

public class MainVoting extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView votetitle,tv,tv1;
    RadioButton rb;
    RadioGroup rg;
    Button btn;
    //private CollectionReference mUser;
    private DocumentReference mUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mref;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_voting);
        votetitle=(TextView)findViewById(R.id.votingtitle);
        tv=(TextView)findViewById(R.id.textView15);
        tv1=(TextView)findViewById(R.id.textView161);
        rg=(RadioGroup) findViewById(R.id.radio);
        btn=(Button)findViewById(R.id.votebutton);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        final String uname = mAuth.getCurrentUser().getDisplayName();
        //String kuchho=tv.getText().toString();








        final Intent intent=getIntent();
        final String vote=intent.getStringExtra("vote");
        final String sendersname=intent.getStringExtra("sendersname");
        votetitle.setText("Title: "+vote);
        mUser = FirebaseFirestore.getInstance().collection("users").document(sendersname);
//        mUser.collection("vote_notification").document(vote).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if (task.isSuccessful()){
//                    DocumentSnapshot documentSnapshot=task.getResult();
//                    if (documentSnapshot != null){
//                        final String id=documentSnapshot.getString("Senders_Id");
//
//                        another(id);
//
//                       // tUser.collection("users").document(id).collection("Vote").document(vote).collection("yes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//                    }  else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                }
//
//
//            }
//        });

//

        mUser.collection("Vote").document(vote).collection("see_votes").document("yes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists() && documentSnapshot != null){
                        final int money=documentSnapshot.getLong("total").intValue();
                        String mo=String.valueOf(money);
                        //        Toast.makeText(MainVoting.this, mo, Toast.LENGTH_SHORT).show();
                        tv.setText(mo);


                    }
                }
            }
        });

        mUser.collection("Vote").document(vote).collection("see_votes").document("no").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists() && documentSnapshot != null){
                        final int money1=documentSnapshot.getLong("total").intValue();
                        String mo1=String.valueOf(money1);
                        //    Toast.makeText(MainVoting.this, mo1, Toast.LENGTH_SHORT).show();
                        tv1.setText(mo1);
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radiobuttonid=rg.getCheckedRadioButtonId();
                rb=(RadioButton)findViewById(radiobuttonid);
                //Toast.makeText(MainVoting.this, rb.getText(), Toast.LENGTH_SHORT).show();





                if (rb.getText().toString().equals("Yes")){

                    int username=Integer.parseInt(tv.getText().toString());
                    int i=username+1;
                    Long manish=Long.valueOf(i);
                    mFirestore.collection("users").document(sendersname).collection("Vote").document(vote).collection("see_votes")
                            .document("yes").update("total", manish)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainVoting.this, "Your Vote Is Submitted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainVoting.this, "Oops..! Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //Toast.makeText(MainVoting.this, "yes", Toast.LENGTH_SHORT).show();
                } else if (rb.getText().toString().equals("No"))
                {


                    //Toast.makeText(MainVoting.this, "no", Toast.LENGTH_SHORT).show();
                    int username=Integer.parseInt(tv1.getText().toString());
                    int ii=username+1;
                    Long manish1=Long.valueOf(ii);

                    mFirestore.collection("users").document(sendersname).collection("Vote").document(vote).collection("see_votes")
                            .document("no").update("total", manish1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainVoting.this, "Your Vote Is Submitted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainVoting.this, "Oops..! Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }













            }
        });




    }

//    private void another(String id) {
//
//        Intent intent=getIntent();
//
//        final String vote=intent.getStringExtra("vote");
//
//        tUser.collection("users").document(id).collection("Vote").document(vote).collection("yes").whereEqualTo("total", 0).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (DocumentSnapshot document: task.getResult()){
//                        String yest=document.getData().toString();
//
//                        Toast.makeText(MainVoting.this, yest, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//
//
//    }


}
