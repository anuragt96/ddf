package com.example.hp.sjdf;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
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

public class AllUser extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText mSearchField;
    private ImageButton mSearchButton;
    private NoteRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;
    private List<Note> notesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        mSearchField = (EditText) findViewById(R.id.search_edittext);
        mSearchButton = (ImageButton) findViewById(R.id.mSearch_button);
        recyclerView = (RecyclerView) findViewById(R.id.rvNoteList);
        firestoreDB = FirebaseFirestore.getInstance();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//

        loadNotesList();

        firestoreListener = firestoreDB.collection("users")
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
//    private void kl(String searchtext) {
//        com.google.firebase.firestore.Query query = firestoreDB.collection("users").orderBy("Email").startAt(searchtext).endAt(searchtext + "\u8f8ff");
//    }

    private void loadNotesList() {

        com.google.firebase.firestore.Query query = firestoreDB.collection("users");
      //  com.google.firebase.firestore.Query query = firestoreDB.collection("users").orderBy("Email").startAt(searchtext).endAt(searchtext + "\u8f8ff");


        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Note, NoteRecyclerViewAdapter.ViewHolder>(response) {
            @Override
            protected void onBindViewHolder(NoteRecyclerViewAdapter.ViewHolder holder, int position, Note model)
            {
                final Note note = notesList.get(position);

                holder.title.setText(note.getName());
                holder.content.setText(note.getEmail());
                final String name=getSnapshots().getSnapshot(position).getId();
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent o = new Intent(getApplicationContext(), AddUserActivity.class);
                        o.putExtra("q", name);
                        o.putExtra("name",note.getName());
                        o.putExtra("email",note.getEmail());
                        o.putExtra("image",note.getImage());
                        o.putExtra("token",note.getToken_id());
                        startActivity(o);
                    }
                });
                Glide.with(getApplicationContext()).load(note.getImage()).into(holder.ci);
//                String name=getSnapshots().getSnapshot(position).getId();

            }

            @Override
            public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

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
