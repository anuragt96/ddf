package com.example.hp.sjdf;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import static android.R.attr.button;

public class ShowEvent extends AppCompatActivity
{
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


    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        btn1=(Button)findViewById(R.id.evenbutton);
        recyclerView = (RecyclerView) findViewById(R.id.evenList);

        firestoreDB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadNotesList();

        firestoreListener = mUser.collection("Event")
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

        btn1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent allevent=new Intent(getApplicationContext(),CreateEvent.class);
                startActivity(allevent);
            }
        });

    }

    private void loadNotesList() {

        com.google.firebase.firestore.Query query = mUser.collection("Event");

        FirestoreRecyclerOptions<Note> response = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Note, NoteRecyclerViewAdapter.ViewHolder>(response) {
            @Override
            protected void onBindViewHolder(final NoteRecyclerViewAdapter.ViewHolder holder, int position, Note model)
            {
                final Note note = notesList.get(position);

                holder.event.setText(note.getEventName());
                holder.event.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final Dialog d=new Dialog(ShowEvent.this);
                        d.setContentView(R.layout.custom1);
                        d.setCancelable(false);
                        Button b1=(Button)d.findViewById(R.id.positive1);
                        Button b2=(Button)d.findViewById(R.id.negative1);
                        TextView tv=(TextView)d.findViewById(R.id.txtclose1);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.dismiss();
                            }
                        });


                       b1.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {

                               mUser.collection("Event").document(note.getEventName())
                                       .delete()
                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                               Toast.makeText(ShowEvent.this, note.getEventName()+" Event Is Deleted", Toast.LENGTH_SHORT).show();
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               //Log.w(TAG, "Error deleting document", e);
                                           }
                                       });



                               mUser.collection("Event").document(note.getEventName()).collection("Added_Users") .get()
                                       .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (DocumentSnapshot document : task.getResult()) {
                                                       //Log.d(TAG, document.getId() + " => " + document.getData());

                                                       final String id=document.getString("Id");

                                                       firestoreDB.collection("users").document(id).collection("notification")
                                                               .document(note.getEventName()).delete()
                                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void aVoid) {

                                                                   }
                                                               })
                                                               .addOnFailureListener(new OnFailureListener() {
                                                                   @Override
                                                                   public void onFailure(@NonNull Exception e) {

                                                                   }
                                                               });




                                                   }
                                               } else {
                                                   Log.d(TAG, "Error getting documents: ", task.getException());
                                               }
                                           }
                                       });
                               d.dismiss();

                           }
                       });
                        b2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                d.cancel();
                            }
                        });

                        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        d.show();







                        return true;
                    }
                });
                holder.event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                                Intent chat=new Intent(getApplicationContext(),ChatActivity.class);
                                chat.putExtra("event",note.getEventName());
                                startActivity(chat);


                            }
                        });










            }

            @Override
            public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.event_layout, parent, false);

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
