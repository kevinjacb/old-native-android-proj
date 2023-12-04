package com.example.parse;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class RecycAdapter extends RecyclerView.Adapter<RecycAdapter.ViewHolder> {

    ArrayList<String> text;
    Context context;
    ArrayList<Bitmap> bitmap;

    public RecycAdapter(Context c, ArrayList<String> text, ArrayList<Bitmap> bitmap){
        init();
        this.text.addAll(text);
        this.context = c;
        this.bitmap.addAll(bitmap);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.data_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.txt.setText(text.get(position));
        holder.img.setImageBitmap(bitmap.get(position));
    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt;
        ImageView img;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.text);
            img = itemView.findViewById(R.id.image);

        }
    }
    public void init(){
        text = new ArrayList<>();
        bitmap = new ArrayList<>();
    }
}
