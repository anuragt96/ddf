package com.example.hp.sjdf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp on 1/18/2018.
 */

public class NoteRecyclerViewAdapter extends RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>
{
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firestoreDB;
    private List<Note> notesList;
    private Context context;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new NoteRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position)
    {
        final int itemPosition = position;
        final Note note = notesList.get(itemPosition);


        holder.title.setText(note.getName());
        holder.content.setText(note.getEmail());
        holder.id.setText(note.getId());
        //holder.event.setText(note.getEvent());
        //Glide.with().asBitmap().load(notesList.get(position)).into(holder.ci);
        //      Picasso.with(context).load(String.valueOf(notesList.get(position))).into(holder.ci);

        //final String username = notesList.get(position).userId;
        //final String value = String.valueOf(getItemId(position));


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent()
//            }
//          });


 //        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent o = new Intent(v.getContext(), AddUserActivity.class);
//                //o.putExtra("q", value);
//                startActivity(o);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, content,id,event,titlel,sendername,senderbubblename,sendermessage;
        CircleImageView ci;
        LinearLayout linearLayout,linearLayout2;
        public CheckBox chkSelected;
        ImageView iv;


        public ViewHolder(View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            content = itemView.findViewById(R.id.tvContent);
            ci = itemView.findViewById(R.id.circleImageView);
            linearLayout = itemView.findViewById(R.id.single);
            linearLayout2 = itemView.findViewById(R.id.single2);
            chkSelected = itemView.findViewById(R.id.checkBox);
            id=itemView.findViewById(R.id.tvid);
            event=itemView.findViewById(R.id.tvEvent);
            titlel=itemView.findViewById(R.id.tvSenderId);
            sendername=itemView.findViewById(R.id.tvsendername);
            senderbubblename=itemView.findViewById(R.id.sendersname);
            sendermessage=itemView.findViewById(R.id.messagebhejo);
            iv=itemView.findViewById(R.id.imageView2);
        }
    }


}

