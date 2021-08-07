package com.gh.sammie.trackproject.ViewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gh.sammie.trackproject.ItemClickListener;
import com.gh.sammie.trackproject.R;

public class PaymentViewHolder extends RecyclerView.ViewHolder {

    public TextView txtBooktName, txt_status, item_price, txt_email, txtDescription;
    public ImageView bookImageView;


    private ItemClickListener itemClickListener;


    public PaymentViewHolder(View itemView) {
        super(itemView);

        txtBooktName = itemView.findViewById(R.id.item_title);
        txtDescription = itemView.findViewById(R.id.txtDescription);
        item_price = itemView.findViewById(R.id.item_price);
        txt_status = itemView.findViewById(R.id.txt_status);
        txt_email = itemView.findViewById(R.id.txt_user_email);


    }


}
