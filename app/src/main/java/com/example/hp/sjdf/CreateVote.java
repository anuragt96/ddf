package com.example.hp.sjdf;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateVote extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private NoteRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    private Button btn;
    private CheckBox cb;
    private EditText e1;


    private DocumentReference mUser;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;
    private List<Note> notesList;
    private List<Note> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vote);

        recyclerView = (RecyclerView) findViewById(R.id.rvEventList);
        btn=(Button)findViewById(R.id.button4);
        cb= (CheckBox)findViewById(R.id.checkBox);

        firestoreDB = FirebaseFirestore.getInstance();
        e1=(EditText)findViewById(R.id.title);
        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        final String uname = mAuth.getCurrentUser().getDisplayName();
        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadNotesList();

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String data = "";
                String data1 = "";
                String title = e1.getText().toString();
                for (int i = 0; i < notesList.size(); i++)
                {
                    Note singleuser = notesList.get(i);
                    if (singleuser.isselected())
                    {
                        data  = singleuser.getName().toString();
                        data1 = singleuser.getId().toString();
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("Id", data1);
                        userMap.put("Name", data);
                        mUser.collection("Vote").document(title).collection("Added_Users").add(userMap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });




                        Map<String, String> useMap = new HashMap<>();
                        //  userMap.put("Id", data1);
                        useMap.put("VoteName", title);
                        mUser.collection("Vote").document(title).set(useMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(CreateVote.this, "Your Voting Poll Is Created",Toast.LENGTH_SHORT).show();
                                //     mProgressbar.setVisibility(View.INVISIBLE);
                                    finish();
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(CreateVote.this, "Data entry failed", Toast.LENGTH_SHORT).show();
                                //   mProgressbar.setVisibility(View.INVISIBLE);
                            }
                        });
                        Map<String, String> usMap = new HashMap<>();
                        usMap.put("Senders_Name", uname);
                        usMap.put("Senders_Id", current_user_id);
                        usMap.put("Title",title);
                        firestoreDB.collection("users").document(data1).collection("vote_notification").document(title).set(usMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid)
                                    {


                                    }
                                }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {

                            }
                        });
//
//                        mUser.collection("Event").document(title).collection("Added_Users").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
//                        {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task)
//                            {
//                                if(task.isSuccessful())
//                                {
//                                    DocumentSnapshot documentSnapshot = task.getResult();
//                                    if(documentSnapshot.exists() && documentSnapshot !=null)
//                                    {
//                                        DocumentSnapshot doc = task.getResult();
//                                        String username = doc.getString("Name");
//
//                                        String email = doc.getString("Email");
//                                        tv.setText(email);
//                                        String image = doc.getString("image");
//
//
//                                    }
//                                }
//                            }
//                        });
                        final String finalData1 = data1;
                        mUser.collection("Vote").document(title).collection("Added_Users") .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                                final String username=document.getString("Name");
                                                final String id=document.getString("Id");
                                                String title = e1.getText().toString();


                                                Map<String, String> uMap = new HashMap<>();
                                                uMap.put("Id", id);
                                                uMap.put("Name", username);
                                                firestoreDB.collection("users").document(finalData1).collection("vote_notification")
                                                        .document(title).collection("Added_Users").add(uMap)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                //Toast.makeText(CreateVote.this, "Succees", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Toast.makeText(CreateVote.this, "Failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });






                    }
                }

                Map<String,Long > yesMap = new HashMap<>();
                yesMap.put("total", (long) 0);
                mUser.collection("Vote").document(title).collection("see_votes").document("yes").set(yesMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(CreateVote.this, "Successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(CreateVote.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });


                Map<String,Long > noMap = new HashMap<>();
                noMap.put("total", (long) 0);
                mUser.collection("Vote").document(title).collection("see_votes").document("no").set(noMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(CreateVote.this, "Successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(CreateVote.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });
        firestoreListener = mUser.collection("contact_list")
                .addSnapshotListener(new EventListener<QuerySnapshot>()
                {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e)
                    {
                        if (e != null)
                        {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        notesList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots)
                        {
                            Note note = doc.toObject(Note.class);
                            notesList.add(note);
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    private void loadNotesList()
    {

        com.google.firebase.firestore.Query query = mUser.collection("contact_list");

        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Note, NoteRecyclerViewAdapter.ViewHolder>(response)
        {
            @Override
            protected void onBindViewHolder(NoteRecyclerViewAdapter.ViewHolder holder, final int position, Note model)
            {
                final Note note = notesList.get(position);

                holder.title.setText(note.getName());
                holder.content.setText(note.getEmail());
                holder.chkSelected.setTag(notesList.get(position));
                holder.chkSelected.setChecked(notesList.get(position).isselected());
                holder.chkSelected.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        CheckBox cb = (CheckBox) v;
                        Note contact = (Note) cb.getTag();

                        contact.setIsselected(cb.isChecked());
                        notesList.get(position).setIsselected(cb.isChecked());

//                        Toast.makeText(v.getContext(),"Clicked on Checkbox: " + cb.getText() + " is "
//                                        + cb.isChecked(), Toast.LENGTH_LONG).show();
                    }
                });


            }

            @Override
            public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_layout, parent, false);

                return new NoteRecyclerViewAdapter.ViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e)
            {
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
