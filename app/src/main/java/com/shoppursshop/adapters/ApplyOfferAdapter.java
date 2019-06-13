package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.settings.FreeProductOfferActivity;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.utilities.DialogAndToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ApplyOfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> myItemList = new ArrayList<>();
    private Context context;
    private int colorTheme;

    private MyItemTypeClickListener myItemTypeClickListener;


    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public ApplyOfferAdapter(Context context, List<Object> myItemList) {
        super();
        this.context = context;
        this.myItemList = myItemList;
    }

    public void setColorTheme(int colorTheme){
        this.colorTheme = colorTheme;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_shop_offers, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        if (myItemList != null) {
            return myItemList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView text_prodName,text_offerName,textOfferName;
        private ImageView imageView, iv_three_dot;
        private View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            text_prodName=itemView.findViewById(R.id.text_prodName);
            text_offerName=itemView.findViewById(R.id.text_offerName);
            imageView=itemView.findViewById(R.id.image_view);
            iv_three_dot = itemView.findViewById(R.id.image_menu);

            rootView.setOnClickListener(this);
            iv_three_dot.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == rootView){

            }
        }
    }
}
