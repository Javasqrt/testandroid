package com.anton.testtagsoft;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyTableRowAdapter extends RecyclerView.Adapter<MyTableRowAdapter.MyRecyclerViewHolder> implements ClickItemListener {
    private  ArrayList<TableRowView> arrayList;
    private ClickItemListener clickItemListener;
    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row_view,parent,false);
        return new MyRecyclerViewHolder(view,clickItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {
        TableRowView tableRowView = arrayList.get(position);
        holder.name1.setText(tableRowView.getName());
        holder.imageView.setImageBitmap(tableRowView.getImage());
        holder.name = tableRowView.getName();
        holder.image = tableRowView.getImage();
        holder.status = tableRowView.getStatus();
        holder.gender = tableRowView.getGender();
        holder.species = tableRowView.getSpecies();






    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void onItemClickListener(ClickItemListener clickItemListener){
        this.clickItemListener = clickItemListener;
    }

    @Override
    public void onItemClick(int position, String name, Bitmap image, String status, String species, String gender) {

    }

    public void getItemByName(ArrayList<TableRowView> arrayListSearch) {
        arrayList = arrayListSearch;
        notifyDataSetChanged();
    }

    public static class MyRecyclerViewHolder extends RecyclerView.ViewHolder{
            TextView name1;
           ImageView imageView;
        public String name, status,species, gender;
        public Bitmap image;
        public MyRecyclerViewHolder(@NonNull View itemView, ClickItemListener clickListener) {
            super(itemView);
            name1 = itemView.findViewById(R.id.txt_name);
          imageView = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            clickListener.onItemClick(position,name, image, status,species, gender);
                        }
                    }
                }
            });
        }

    }
    public MyTableRowAdapter(ArrayList<TableRowView> arrayList){
        this.arrayList = arrayList;
    }


}
