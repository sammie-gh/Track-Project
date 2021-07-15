package com.gh.sammie.trackproject.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.trackproject.ItemClickListener;
import com.gh.sammie.trackproject.R;

public class GoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtBooktName, txt_status, txt_date, txt_location, txt_terminal, item_price, txt_days, txt_pnty, txt_TTcost,txtDescription;
    public ImageView bookImageView;


    private ItemClickListener itemClickListener;


    public GoodsViewHolder(View itemView) {
        super(itemView);

        txtBooktName = itemView.findViewById(R.id.item_title);
        bookImageView = itemView.findViewById(R.id.book_image);
        txtDescription = itemView.findViewById(R.id.txtDescription);
        item_price = itemView.findViewById(R.id.item_price);
        txt_status = itemView.findViewById(R.id.txt_status);
        txt_days = itemView.findViewById(R.id.txt_days);
        txt_date = itemView.findViewById(R.id.txt_date);
        txt_pnty = itemView.findViewById(R.id.txt_pnty);
        txt_TTcost = itemView.findViewById(R.id.txt_TTcost);
        txt_terminal = itemView.findViewById(R.id.txt_terminal);
        txt_location = itemView.findViewById(R.id.txt_location);
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
