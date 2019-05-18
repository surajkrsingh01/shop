package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyProductItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MyProductItem> itemList;
    private Context context;

    private MyItemTypeClickListener myItemTypeClickListener;

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public CartAdapter(Context context, List<MyProductItem> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textBarCode,textName,textMrp,textCounter;
        private ImageView imageView,imageViewMinus,imageViewAdd;

        public MyViewHolder(View itemView) {
            super(itemView);
            textBarCode=itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textCounter=itemView.findViewById(R.id.tv_counter);
            imageViewMinus=itemView.findViewById(R.id.image_minus);
            imageViewAdd=itemView.findViewById(R.id.image_add);
            imageView=itemView.findViewById(R.id.image_view);

            imageViewAdd.setOnClickListener(this);
            imageViewMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageViewAdd){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
            }else{
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),1);
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v8 = inflater.inflate(R.layout.cart_item_layout, parent, false);
        viewHolder = new MyViewHolder(v8);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyProductItem item = (MyProductItem) itemList.get(position);
        MyViewHolder myViewHolder = (MyViewHolder)holder;

        myViewHolder.textBarCode.setText(item.getProdBarCode());
        myViewHolder.textName.setText(item.getProdName());
        //myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getMrp()));
        myViewHolder.textMrp.setText("MRP: Rs"+item.getProdMrp());
        myViewHolder.textCounter.setText(""+item.getQty());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);

        Glide.with(context)
                .load(item.getProdImage1())
                .apply(requestOptions)
                .into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
