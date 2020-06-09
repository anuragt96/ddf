package com.example.hp.sjdf;

import android.content.Intent;
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
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import java.util.List;

public class SeeTagUsers extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private NoteRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;

    private DocumentReference mUser;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;
    private List<Note> notesList;


    //TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_tag_users);
        //tv=(TextView)findViewById(R.id.textView7);
        Intent intent=getIntent();
        final String event1=intent.getStringExtra("event1");
       // tv.setText(event1);
        recyclerView = (RecyclerView) findViewById(R.id.rvseeusers);


        firestoreDB = FirebaseFirestore.getInstance();
        //e1=(EditText)findViewById(R.id.title);
        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadNotesList();
        firestoreListener = mUser.collection("Event").document(event1).collection("Added_Users")
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
        final String event1=intent.getStringExtra("event1");

        com.google.firebase.firestore.Query query = mUser.collection("Event").document(event1).collection("Added_Users");

        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Note, NoteRecyclerViewAdapter.ViewHolder>(response) {
            @Override
            protected void onBindViewHolder(NoteRecyclerViewAdapter.ViewHolder holder, int position, Note model)
            {
                final Note note = notesList.get(position);

                holder.title.setText(note.getName());

//                String name=getSnapshots().getSnapshot(position).getId();

            }

            @Override
            public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tag_users, parent, false);

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
