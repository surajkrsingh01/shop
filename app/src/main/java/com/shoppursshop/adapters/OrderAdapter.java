package com.shoppursshop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemTouchListener;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyItem;
import com.shoppursshop.models.OrderItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;

    private MyItemTouchListener myItemTouchListener;

    public void setMyItemTouchListener(MyItemTouchListener myItemTouchListener) {
        this.myItemTouchListener = myItemTouchListener;
    }

    private ConstraintSet constraintSet = new ConstraintSet();

    public OrderAdapter(Context context, List<Object> itemList,String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;


        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_date_range);
            textDesc=itemView.findViewById(R.id.text_desc);
        }

        @Override
        public void onClick(View view) {
        }
    }


    public class MyListType1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textCustName,textAmount,textDeliveryType;
        private ImageView imageView;
        private View rootView;

        public MyListType1ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textCustName=itemView.findViewById(R.id.text_customer_name);
            textAmount=itemView.findViewById(R.id.text_amount);
            textDeliveryType=itemView.findViewById(R.id.text_delivery_type);
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

    public class MyListType2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textHeader,textAmount,textStatus,textDateTime;
        private RatingBar ratingBar;
        private Button btnReorder;
        private View rootView;

        public MyListType2ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textHeader=itemView.findViewById(R.id.text_header);
            textAmount=itemView.findViewById(R.id.text_sub_header);
            textStatus=itemView.findViewById(R.id.text_status);
            textDateTime=itemView.findViewById(R.id.text_date_time);
            btnReorder=itemView.findViewById(R.id.btn_reorder);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            rootView.setOnTouchListener(this);
            btnReorder.setOnClickListener(this);
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
                View v0 = inflater.inflate(R.layout.home_list_header_item_layout, parent, false);
                viewHolder = new OrderAdapter.MyHomeHeaderViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.order_list_item_layout, parent, false);
                viewHolder = new OrderAdapter.MyListType1ViewHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.order_list_item_type_2_layout, parent, false);
                viewHolder = new OrderAdapter.MyListType2ViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new OrderAdapter.MyViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("orderList")){
            Object item = itemList.get(position);
            if(item instanceof HomeListItem){
                return 0;
            }else{
                OrderItem orderItem = (OrderItem)item;
                if(orderItem.getType() == 1){
                    return 1;
                }else{
                    return 2;
                }

            }
        }else if(type.equals("countries")){
            return 3;
        } else{
            return 4;
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


            HomeListItem item = (HomeListItem) itemList.get(position);
            MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());
            myViewHolder.textDesc.setText(item.getDesc());

        }else if(holder instanceof MyListType1ViewHolder){
            OrderItem item = (OrderItem) itemList.get(position);
            MyListType1ViewHolder myViewHolder = (MyListType1ViewHolder)holder;

            myViewHolder.textCustName.setText(item.getCustomerName()+", "+item.getMobile());
            myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getAmount()));
            myViewHolder.textDeliveryType.setText(item.getDeliveryType());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(true);

            Glide.with(context)
                    .load(item.getLocalImage())
                    .apply(requestOptions)
                    .into(myViewHolder.imageView);


        }else if(holder instanceof MyListType2ViewHolder){
            OrderItem item = (OrderItem) itemList.get(position);
            MyListType2ViewHolder myViewHolder = (MyListType2ViewHolder)holder;

            myViewHolder.textHeader.setText("Order id - "+item.getId());
            myViewHolder.textStatus.setText("("+item.getStatus()+")");
            myViewHolder.textAmount.setText("Order Total amount - Rs "+String.format("%.02f",item.getAmount()));
            myViewHolder.textDateTime.setText(item.getDateTime());
            myViewHolder.ratingBar.setRating(item.getRating());

            if(item.getStatus().equals("Delivered")){
                myViewHolder.textStatus.setTextColor(context.getResources().getColor(R.color.green600));
            }else{
                myViewHolder.textStatus.setTextColor(context.getResources().getColor(R.color.red_600));
            }
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
