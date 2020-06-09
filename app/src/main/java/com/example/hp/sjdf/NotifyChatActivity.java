package com.example.hp.sjdf;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifyChatActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private NoteRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;



    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;
    private List<Note> notesList;

    TextView tv;
    ImageButton btn,send_btn,btn1;
    private Toolbar mChatToolbar;
    EditText message;
    private DocumentReference mUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mref;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_chat);


        message=(EditText)findViewById(R.id.message_field);
        send_btn=(ImageButton)findViewById(R.id.send_message);
        btn1=(ImageButton)findViewById(R.id.attachement);
        mFirestore = FirebaseFirestore.getInstance();

        recyclerView=(RecyclerView)findViewById(R.id.notifychatrecycler);


        Intent intent=getIntent();
        final String event=intent.getStringExtra("event");
        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        final String uname = mAuth.getCurrentUser().getDisplayName();


        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);


        mChatToolbar=(Toolbar)findViewById(R.id.main1_page);
        setSupportActionBar(mChatToolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        //getSupportActionBar().setTitle(event);
        LayoutInflater inflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view=inflater.inflate(R.layout.chat_custom_layout,null);
        actionBar.setCustomView(action_bar_view);
        tv=(TextView)findViewById(R.id.textView6);
        tv.setText(event);
        btn=(ImageButton)findViewById(R.id.button5);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getApplicationContext(),NotifyFileUpload.class);
                i.putExtra("event1",event);
                startActivity(i);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent group=new Intent(getApplicationContext(),NotifySeeTagUsers.class);
                group.putExtra("event1",event);
                startActivity(group);
            }
        });
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String msg=message.getText().toString();
                message.setText("");

////apna
//                Map<String, String> uMap = new HashMap<>();
//                uMap.put("Name", uname);
//                uMap.put("Message", msg);
//                mUser.collection("notification").document(event).collection("message").add(uMap)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(NotifyChatActivity.this, "Succees", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(NotifyChatActivity.this, "Failure", Toast.LENGTH_SHORT).show();
//                    }
//                });
//tag walo ko msg

//                Long tslong = System.currentTimeMillis()/1000;
//                final String ts = tslong.toString();
                mUser.collection("notification").document(event).collection("Added_Users") .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());

                                        //final String username=document.getString("Name");
                                        final String id=document.getString("Id");
                                        //String title = e1.getText().toString();


                                        Map<String, String> ussMap = new HashMap<>();
                                        ussMap.put("Name", uname);
                                        ussMap.put("Message", msg);
                                        //ussMap.put("Time", ts);
                                        //uMap.put("Id", id);
                                        //uMap.put("Name", username);
                                        mFirestore.collection("users").document(id).collection("notification")
                                                .document(event).collection("message").add(ussMap)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //Toast.makeText(NotifyChatActivity.this, "Succees", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Toast.makeText(NotifyChatActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

//admin ko msg
                mUser.collection("notification").document(event).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                              //  Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());

                                final String id=document.getString("Senders_Id");
                                //String title = e1.getText().toString();


                                Map<String, String> ussMap = new HashMap<>();
                                ussMap.put("Name", uname);
                                ussMap.put("Message", msg);
                                //ussMap.put("Time", ts);
                                //uMap.put("Id", id);
                                //uMap.put("Name", username);
                                mFirestore.collection("users").document(id).collection("Event")
                                        .document(event).collection("message").add(ussMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                //Toast.makeText(NotifyChatActivity.this, "Succees", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Toast.makeText(NotifyChatActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    /*            Map<String, String> usMap = new HashMap<>();
                uMap.put("Name", uname);
                uMap.put("Message", msg);
                firestoreDB.collection("Event").document(event).collection("message").add(uMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                          //      Toast.makeText(ChatActivity.this, "Succees", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                 //       Toast.makeText(ChatActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                }); */


            }
        });



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadNotesList();

        firestoreListener = mUser.collection("notification").document(event).collection("message")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        notesList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            Note note = doc.toObject(Note.class);
                            notesList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    private void loadNotesList() {
        Intent intent=getIntent();
        String event=intent.getStringExtra("event");

        com.google.firebase.firestore.Query query = mUser.collection("notification").document(event).collection("message");

        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Note, NoteRecyclerViewAdapter.ViewHolder>(response) {
            @Override
            protected void onBindViewHolder(NoteRecyclerViewAdapter.ViewHolder holder, int position, Note model)
            {
                final Note note = notesList.get(position);

                holder.senderbubblename.setText(note.getName());
                holder.sendermessage.setText(note.getMessage());
                //Picasso.with(holder.iv.getContext()).load(note.getImage_msg()).into(holder.iv);
                Glide.with(getApplicationContext()).load(note.getImage_msg()).into(holder.iv);

            }

            @Override
            public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layoutforchatbubble, parent, false);

                return new NoteRecyclerViewAdapter.ViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        firestoreListener.remove();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
