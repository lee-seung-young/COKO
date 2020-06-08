package com.example.coko;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context context;
    private ArrayList<LikesInfo> arrayList;
    private OnItemClickListener mListener;
    private FirebaseDatabase database;

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public ListAdapter(Context context, ArrayList<LikesInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

//    public ListAdapter(ArrayList<LikesInfo> arrayList){
//        this.arrayList = arrayList;
//    }
    @NonNull
    @Override
    public ListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        ListViewHolder holder = new ListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAdapter.ListViewHolder holder, final int position) {
        Picasso.get().load(arrayList.get(position).getPic()).into(holder.pic);
        holder.name.setText(arrayList.get(position).getName());
        holder.location.setText(arrayList.get(position).getLocation());

        database = FirebaseDatabase.getInstance();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("찜목록 삭제")
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                arrayList.remove(position);

                                Log.d("******", String.valueOf(position));
//                                int count =  position;
                                String addNum = "likes" + (position+1);
//                                database.getReference().child("Likes").child(addNum).removeValue();
                                database.getReference().child("Likes").child(addNum).removeValue();
//                                database.getReference().removeValue();
                                notifyItemChanged(position);

                            }
                        });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });}

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView pic;
        TextView name;
        TextView location;

//        LikesInfo info;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.pic = itemView.findViewById(R.id.likes_pic);
            this.name = itemView.findViewById(R.id.likes_name);
            this.location = itemView.findViewById(R.id.likes_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position  = getAdapterPosition();
                    mListener.onItemClick(position) ;
                }
            });

        }
    }

}
