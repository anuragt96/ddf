package com.example.hp.sjdf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.StreamDownloadTask;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SeeVotes extends AppCompatActivity {

    TextView tv,tv1,tv2,tv3;
    ImageButton b1;
    private static final String TAG = "MainActivity";
    private DocumentReference mUser;
    private FirebaseFirestore mFirestore;
    private CollectionReference mref;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_votes);
        tv=(TextView)findViewById(R.id.textView9);
        tv1=(TextView)findViewById(R.id.tv121);
        tv2=(TextView)findViewById(R.id.tv122);
        b1=(ImageButton) findViewById(R.id.button8);
        progressBar= (ProgressBar) findViewById(R.id.progressBar3);
        //final GraphView graph = (GraphView) findViewById(R.id.graph);

        progressBar.setVisibility(View.GONE);
        Intent intent=getIntent();
        final String vote=intent.getStringExtra("vote");
        tv.setText(vote);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final String current_user_id = mAuth.getCurrentUser().getUid();
        final String uname = mAuth.getCurrentUser().getDisplayName();

//        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//        staticLabelsFormatter.setHorizontalLabels(new String[] {"Yes","No"});
//        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        mUser = FirebaseFirestore.getInstance().collection("users").document(current_user_id);

        mUser.collection("Vote").document(vote).collection("see_votes").document("yes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists() && documentSnapshot != null){
                        final int money=documentSnapshot.getLong("total").intValue();
                        String mo=String.valueOf(money);
                        //        Toast.makeText(MainVoting.this, mo, Toast.LENGTH_SHORT).show();

                        //tv1.setVisibility(View.VISIBLE);
                        tv1.setText(mo);



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
                        tv2.setText(mo1);

                    }
                }
            }
        });





        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                            Intent q=new Intent(getApplicationContext(),GraphActivity.class);
                            q.putExtra("yes",tv1.getText().toString());
                            q.putExtra("no",tv2.getText().toString());
                            startActivity(q);
            }
        });
    }

//int a=4;
//        int b=5;
//
////        int yesvote=Integer.parseInt(tv1.getText().toString());
////        int novote=Integer.parseInt(tv2.getText().toString());
////        long kk=Long.valueOf(yesvote);
////        long kkk=Long.valueOf(novote);
//
//        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[]
//        {
//                new DataPoint(1, a),
//                new DataPoint(2, b)
//        });
//        graph.addSeries(series);
//
//        // for color
//        series.setValueDependentColor(new ValueDependentColor<DataPoint>()
//        {
//            @Override
//            public int get(DataPoint data)
//            {
//                return Color.rgb((int) data.getX()* 255/4,(int) Math.abs(data.getY()*255/6),100);
//            }
//        });
//
//        series.setSpacing(50);
//
//        // draw values on top
//        series.setDrawValuesOnTop(true);
//        series.setValuesOnTopColor(Color.RED);
//
}
