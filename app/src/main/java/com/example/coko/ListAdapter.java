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

        //리스트에서 받아오기
        String place_num = arrayList.get(position).getPlace_id();
        final String name = arrayList.get(position).getName();
        String lat = arrayList.get(position).getLatitude();

        String likes = "likes";
        String place = "place";
        final String addlikes = likes.concat(place_num);
        final String addplace = place.concat(place_num);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("찜목록 삭제")
                        .setMessage(name+"을(를) 찜목록에서 정말로 삭제하시겠습니까?")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                arrayList.remove(position); // 찜목록 화면에서 삭제

                                database.getReference().child("Likes").child(addlikes).removeValue(); // 디비 Likes 항목에서 삭제
                                database.getReference().child("Place").child(addplace).child("likeox").setValue(false); // 디비 Place에서 false로 변환

                                notifyItemChanged(position);
                                notifyItemRangeRemoved(0, arrayList.size());
                            }
                        });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView pic;
        TextView name;
        TextView location;

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
