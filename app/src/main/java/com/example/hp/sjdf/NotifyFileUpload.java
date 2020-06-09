package com.example.hp.sjdf;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NotifyFileUpload extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int FILE_SELECT_CODE =1 ;
    private Button selbtn,pausebtn,cancelbtn;
    private StorageReference mStorageRef;
    private TextView mfilenameLabel,msizelabel,mprogresslabel;
    private ProgressBar mbar;

    private DocumentReference mUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mref;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private StorageTask mstorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_file_upload);

        selbtn=(Button)findViewById(R.id.selectbtn);
        pausebtn=(Button)findViewById(R.id.pausebtn);
        cancelbtn=(Button)findViewById(R.id.cancelbtn);
        mfilenameLabel =(TextView)findViewById(R.id.textView11);
        mbar=(ProgressBar)findViewById(R.id.uploadprogressBar);
        msizelabel=(TextView)findViewById(R.id.textView10);
        mprogresslabel=(TextView)findViewById(R.id.textView8);
        mFirestore = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        final String uname = mAuth.getCurrentUser().getDisplayName();


        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);



        mStorageRef = FirebaseStorage.getInstance().getReference();

        selbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileSelector();
            }
        });

//        pausebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String text = pausebtn.getText().toString();
//                if(text.equals("PAUSE UPLOAD"))
//                {
//                    mstorage.pause();
//                    pausebtn.setText("RESUME UPLOAD");
//                }
//                else
//                {
//                    mstorage.resume();
//                    pausebtn.setText("PAUSE UPLOAD");
//                }
//            }
//        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mstorage.cancel();
                finish();
            }
        });


    }

    private void openFileSelector()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try{
            startActivityForResult(Intent.createChooser(intent,"Select Files To Upload"),FILE_SELECT_CODE);
        }catch(android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(this, "Please Install File Manager", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK)
        {
            Uri fileuri = data.getData();

            String uriString = fileuri.toString();
            File myFile = new File(uriString);
            //String path = myFile.getAbsolutePath();
            String Displayname= null;

            if(uriString.startsWith("content://"))
            {
                Cursor cursor = null;
                try {
                    cursor = NotifyFileUpload.this.getContentResolver().query(fileuri, null , null , null, null);
                    if(cursor != null && cursor.moveToFirst())
                    {
                        Displayname = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    }

                }finally {
                    cursor.close();
                }
            }
            else if(uriString.startsWith("file://"))
            {
                Displayname = myFile.getName();
            }

            mfilenameLabel.setText(Displayname);
            //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            StorageReference riversRef = mStorageRef.child("files/" + Displayname);

            mstorage = riversRef.putFile(fileuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            final String url = downloadUrl.toString();
                            final String uname = mAuth.getCurrentUser().getDisplayName();
                            Intent intent=getIntent();
                            final String event=intent.getStringExtra("event1");

                            Toast.makeText(NotifyFileUpload.this, "File Uploaded", Toast.LENGTH_SHORT).show();
//admin image send kr raha apne aap ko

                            final Map<String, String> uMap = new HashMap<>();
                            uMap.put("Name", uname);
                            uMap.put("Image_msg", url);

                            mUser.collection("notification").document(event).collection("message").add(uMap)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(NotifyFileUpload.this, "Succees", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(NotifyFileUpload.this, "Failure", Toast.LENGTH_SHORT).show();
                                }
                            });

                            //admin image send kr raha hai tag users walo ko

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
                                            ussMap.put("Image_msg", url);
                                            //ussMap.put("Message", msg);
                                            //ussMap.put("Time", ts);
                                            //uMap.put("Id", id);
                                            //uMap.put("Name", username);
                                            mFirestore.collection("users").document(id).collection("Event")
                                                    .document(event).collection("message").add(ussMap)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(NotifyFileUpload.this, "Succees", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(NotifyFileUpload.this, "Failure", Toast.LENGTH_SHORT).show();
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

                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(NotifyFileUpload.this, "Upload Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            mbar.setProgress((int)progress);

                            String progresstext = taskSnapshot.getBytesTransferred()/(1024 * 1024) + "/" +
                                    taskSnapshot.getTotalByteCount()/(1024 * 1024)
                                    + "mb";
                            msizelabel.setText(progresstext);
                        }
                    });
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}