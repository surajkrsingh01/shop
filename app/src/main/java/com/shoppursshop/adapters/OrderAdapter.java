package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shoppursshop.R;
import com.shoppursshop.activities.OrderDetailActivity;
import com.shoppursshop.activities.settings.MyOrderDetailsActivity;
import com.shoppursshop.interfaces.MyItemTouchListener;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyItem;
import com.shoppursshop.models.OrderItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;
    private int counter;

    private SharedPreferences sharedPreferences;

    private MyItemTouchListener myItemTouchListener;

    public void setMyItemTouchListener(MyItemTouchListener myItemTouchListener) {
        this.myItemTouchListener = myItemTouchListener;
    }

    private ConstraintSet constraintSet = new ConstraintSet();

    public OrderAdapter(Context context, List<Object> itemList,String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;
        sharedPreferences = context.getSharedPreferences(Constants.MYPREFERENCEKEY,context.MODE_PRIVATE);
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

        private TextView textInitial,textCustName,textAmount,textDeliveryType,textViewStatus;
        private ImageView imageView;
        private View rootView;

        public MyListType1ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textCustName=itemView.findViewById(R.id.text_customer_name);
            textInitial=itemView.findViewById(R.id.tv_initial);
            textAmount=itemView.findViewById(R.id.text_amount);
            textDeliveryType=itemView.findViewById(R.id.text_delivery_type);
            textViewStatus=itemView.findViewById(R.id.text_status);
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
                    zoomAnimation(true,rootView);
                    //myItemTouchListener.onPressDown(getAdapterPosition());
                    break;
                // break;

                case MotionEvent.ACTION_UP:
                    zoomAnimation(false,rootView);
                    OrderItem item = (OrderItem) itemList.get(getAdapterPosition());
                    if(sharedPreferences.getString(Constants.SHOP_CODE,"").equals(item.getCustCode())){

                        Intent intent = new Intent(context, MyOrderDetailsActivity.class);
                        intent.putExtra("id",item.getId());
                        intent.putExtra("date",item.getDateTime());
                        intent.putExtra("totalAmount",item.getAmount());
                        intent.putExtra("status",item.getStatus());
                        intent.putExtra("ordPaymentStatus",item.getOrderPayStatus());
                        context.startActivity(intent);

                    }else{
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra("id",item.getId());
                        intent.putExtra("orderNumber",item.getOrderNumber());
                        intent.putExtra("custName",item.getCustomerName());
                        intent.putExtra("custCode",item.getCustCode());
                        intent.putExtra("date",item.getDateTime());
                        intent.putExtra("totalAmount",item.getAmount());
                        intent.putExtra("deliveryMode",item.getDeliveryType());
                        intent.putExtra("deliveryAddress",item.getDeliveryAddress());
                        intent.putExtra("status",item.getStatus());
                        intent.putExtra("ordPaymentStatus",item.getOrderPayStatus());
                        intent.putExtra("orderPosition",getAdapterPosition());

                        if(Utility.getTimeStamp("yyyy-MM-dd").equals(item.getDateTime().split(" ")[0]))
                            intent.putExtra("type","today");
                        else
                            intent.putExtra("type","pre");

                        context.startActivity(intent);
                    }

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
            textAmount=itemView.findViewById(R.id.text_amount);
            textStatus=itemView.findViewById(R.id.text_status);
            textDateTime=itemView.findViewById(R.id.text_date_time);
            ratingBar=itemView.findViewById(R.id.ratingBar);
            ratingBar.setVisibility(View.GONE);
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
            final MyListType1ViewHolder myViewHolder = (MyListType1ViewHolder)holder;

            myViewHolder.textCustName.setText(item.getCustomerName()+", "+item.getMobile());
            myViewHolder.textAmount.setText(Utility.numberFormat(item.getAmount()));
            myViewHolder.textDeliveryType.setText(item.getDeliveryType());
            myViewHolder.textViewStatus.setText(item.getStatus());

            if(item.getStatus() != null){
                myViewHolder.textViewStatus.setVisibility(View.VISIBLE);
                if(item.getStatus().equals("Accepted")|| item.getStatus().equals("Delivered")){
                    myViewHolder.textViewStatus.setTextColor(context.getResources().getColor(R.color.green500));
                }else{
                    myViewHolder.textViewStatus.setTextColor(context.getResources().getColor(R.color.red_500));
                }
            }else{
                myViewHolder.textViewStatus.setVisibility(View.GONE);
            }

            String initials = "";
            if(item.getCustomerName().contains(" ")){
                String[] name = item.getCustomerName().split(" ");
                initials = name[0].substring(0,1)+name[1].substring(0,1);
            }else{
                initials = item.getCustomerName().substring(0,2);
            }

            myViewHolder.textInitial.setText(initials);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Glide.with(context)
                    .load(item.getOrderImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            myViewHolder.textInitial.setVisibility(View.VISIBLE);
                            myViewHolder.imageView.setVisibility(View.GONE);
                            myViewHolder.textInitial.setBackgroundColor(getTvColor(counter));
                            counter++;
                            if(counter == 13){
                                counter = 0;
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            myViewHolder.textInitial.setVisibility(View.GONE);
                            myViewHolder.imageView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(myViewHolder.imageView);


        }else if(holder instanceof MyListType2ViewHolder){
            OrderItem item = (OrderItem) itemList.get(position);
            MyListType2ViewHolder myViewHolder = (MyListType2ViewHolder)holder;

            myViewHolder.textHeader.setText("Order No - "+item.getId());
            myViewHolder.textStatus.setText("("+item.getStatus()+")");
            myViewHolder.textAmount.setText(Utility.numberFormat(item.getAmount()));
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

    private int getTvColor(int position){
        int[] tvColor={context.getResources().getColor(R.color.light_blue500),
                context.getResources().getColor(R.color.yellow500),context.getResources().getColor(R.color.green500),
                context.getResources().getColor(R.color.orange500),context.getResources().getColor(R.color.red_500),
                context.getResources().getColor(R.color.teal_500),context.getResources().getColor(R.color.cyan500),
                context.getResources().getColor(R.color.deep_orange500),context.getResources().getColor(R.color.blue500),
                context.getResources().getColor(R.color.purple500),context.getResources().getColor(R.color.amber500),
                context.getResources().getColor(R.color.light_green500)};

        return tvColor[position];
    }
}
