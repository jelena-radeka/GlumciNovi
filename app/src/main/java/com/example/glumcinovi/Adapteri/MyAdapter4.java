package com.example.glumcinovi.Adapteri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.glumcinovi.R;
import com.example.glumcinovi.model.FavoriteFIlmovi;
import com.example.glumcinovi.model.Filmovi;
import com.example.glumcinovi.net.Search;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.MyViewHolder>{

    private Context context;
    private List<FavoriteFIlmovi> searchItem;
    private OnItemClickListener listener;


    public MyAdapter4(Context context, List<FavoriteFIlmovi> searchItem, OnItemClickListener listener ) {
        this.context = context;
        this.searchItem = searchItem;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyAdapter4.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_prikaz4, parent, false);

        return new MyAdapter4.MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvTitle.setText(searchItem.get(position).getmNaziv());
        holder.tvYear.setText(searchItem.get(position).getmGodine());


    }

    @Override
    public int getItemCount() {
        return searchItem.size();
    }

    public FavoriteFIlmovi get(int position) {
        return searchItem.get(position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView tvTitle;
        private TextView tvYear;
        private TextView tvType;

        private ImageView ivMalaSlika;
        private OnItemClickListener vhListener;


        MyViewHolder(@NonNull View itemView, OnItemClickListener vhListener) {
            super(itemView);


            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvYear = itemView.findViewById(R.id.tvYear);

            this.vhListener = vhListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            vhListener.onItemClick(getAdapterPosition());
        }


    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}