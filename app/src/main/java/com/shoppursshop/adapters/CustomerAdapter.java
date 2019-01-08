package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.CustomerProfileActivity;
import com.shoppursshop.interfaces.MyItemTouchListener;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyHeader;
import com.shoppursshop.models.MyItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;

    private MyItemTouchListener myItemTouchListener;

    public void setMyItemTouchListener(MyItemTouchListener myItemTouchListener) {
        this.myItemTouchListener = myItemTouchListener;
    }

    private ConstraintSet constraintSet = new ConstraintSet();

    public CustomerAdapter(Context context, List<Object> itemList,String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
            textDesc=itemView.findViewById(R.id.text_desc);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyHomeHeader1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;

        public MyHomeHeader1ViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
        }

        @Override
        public void onClick(View view) {
        }
    }


    public class MyCustomerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textCustName,textAddress,textStateCity;
        private ImageView imageView;
        private View rootView;

        public MyCustomerListViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textCustName=itemView.findViewById(R.id.text_customer_name);
            textAddress=itemView.findViewById(R.id.text_address);
            textStateCity=itemView.findViewById(R.id.text_state_city);
            imageView=itemView.findViewById(R.id.image_view);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Log.i("Adapter","onPressDown");
                    zoomAnimation(true,rootView);
                    //myItemTouchListener.onPressDown(getAdapterPosition());
                    break;
                // break;

                case MotionEvent.ACTION_UP:
                    // Log.i("Adapter","onPressUp");
                    MyCustomer item = (MyCustomer)itemList.get(getAdapterPosition());
                    Intent intent = new Intent(context,CustomerProfileActivity.class);
                    intent.putExtra("name",item.getName());
                    intent.putExtra("address",item.getAddress());
                    intent.putExtra("stateCity",item.getState()+", "+item.getCity());
                    intent.putExtra("customerImage",item.getLocalImage());
                    context.startActivity(intent);
                    zoomAnimation(false,rootView);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i("Adapter","onPressCancel");
                    zoomAnimation(false,rootView);
                    break;
            }
            return true;
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;
        private CircleImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_header);
            textDesc=itemView.findViewById(R.id.text_desc);
            image=itemView.findViewById(R.id.image_pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.header_item_type_1_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.header_item_type_2_layout, parent, false);
                viewHolder = new MyHomeHeader1ViewHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.customer_list_item_layout, parent, false);
                viewHolder = new MyCustomerListViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("customerList")){
            Object item = itemList.get(position);
            if(item instanceof MyHeader){
                MyHeader myItem = (MyHeader) item;
                if(myItem.getType() == 0){
                    return 0;
                }else{
                    return 1;
                }

            }else if(item instanceof MyCustomer){
                return 2;
            }else{
                return 3;
            }
        }else if(type.equals("countries")){
            return 2;
        } else{
            return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyItem item = (MyItem) itemList.get(position);
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            myViewHolder.textHeader.setText(item.getHeader());
            myViewHolder.textDesc.setText(item.getDesc());
        }else if(holder instanceof MyHomeHeaderViewHolder){

            MyHeader item = (MyHeader) itemList.get(position);
            MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());
            myViewHolder.textDesc.setText(item.getDesc());


        }else if(holder instanceof MyHomeHeader1ViewHolder){

            MyHeader item = (MyHeader) itemList.get(position);
            MyHomeHeader1ViewHolder myViewHolder = (MyHomeHeader1ViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());


        }else if(holder instanceof MyCustomerListViewHolder){

            MyCustomer item = (MyCustomer) itemList.get(position);
            MyCustomerListViewHolder myViewHolder = (MyCustomerListViewHolder)holder;
            myViewHolder.textCustName.setText(item.getName()+", "+item.getMobile());
            myViewHolder.textAddress.setText(item.getAddress());
            myViewHolder.textStateCity.setText(item.getState()+", "+item.getCity());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.dontTransform();
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            // requestOptions.centerCrop();
            requestOptions.skipMemoryCache(true);

            Glide.with(context)
                    .load(item.getLocalImage())
                    .apply(requestOptions)
                    .into(myViewHolder.imageView);

        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void zoomAnimation(boolean zoomOut,View view){
        if(zoomOut){
            Animation aniSlide = AnimationUtils.loadAnimation(context,R.anim.zoom_out);
            aniSlide.setFillAfter(true);
            view.startAnimation(aniSlide);
        }else{
            Animation aniSlide = AnimationUtils.loadAnimation(context,R.anim.zoom_in);
            aniSlide.setFillAfter(true);
            view.startAnimation(aniSlide);
        }
    }
}
