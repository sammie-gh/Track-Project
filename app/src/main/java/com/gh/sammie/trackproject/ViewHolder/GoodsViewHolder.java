package com.gh.sammie.trackproject.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.trackproject.ItemClickListener;
import com.gh.sammie.trackproject.R;

public class GoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtBooktName, itemPrice;
    public ImageView bookImageView;

    private ItemClickListener itemClickListener;


    public GoodsViewHolder(View itemView) {
        super(itemView);

        txtBooktName = itemView.findViewById(R.id.item_title);
        bookImageView = itemView.findViewById(R.id.book_image);
        itemPrice = itemView.findViewById(R.id.item_price);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}
