package com.shoppursshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.AddCustomerActivity;
import com.shoppursshop.activities.CustomerProfileActivity;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTouchListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyCustomer;
import com.shoppursshop.models.MyHeader;
import com.shoppursshop.models.MyItem;
import com.shoppursshop.utilities.Utility;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;

    private int counter;

    private MyItemTouchListener myItemTouchListener;
    private MyItemTypeClickListener myItemClickListener;

    public void setMyItemTouchListener(MyItemTouchListener myItemTouchListener) {
        this.myItemTouchListener = myItemTouchListener;
    }

    public MyItemTypeClickListener getMyItemClickListener() {
        return myItemClickListener;
    }

    public void setMyItemClickListener(MyItemTypeClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
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
        private Button btnAdd;

        public MyHomeHeader1ViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
            btnAdd=itemView.findViewById(R.id.btn_add);
            btnAdd.setOnClickListener(this);

            btnAdd.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View view) {
            if(view == btnAdd){
                Intent intent = new Intent(context,AddCustomerActivity.class);
                intent.putExtra("flag","manual");
                context.startActivity(intent);
            }
        }
    }


    public class MyCustomerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textInitial,textCustName,textAddress,textStateCity,textMobile,textEmail;
        private CircleImageView imageView;
        private ImageView imageMenu;
        private View rootView;

        public MyCustomerListViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textInitial=itemView.findViewById(R.id.tv_initial);
            textCustName=itemView.findViewById(R.id.text_customer_name);
            textMobile=itemView.findViewById(R.id.text_mobile);
            textAddress=itemView.findViewById(R.id.text_address);
            textStateCity=itemView.findViewById(R.id.text_state_city);
            imageView=itemView.findViewById(R.id.image_view);
            imageMenu=itemView.findViewById(R.id.image_menu);
            imageMenu.setOnClickListener(this);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageMenu){
                final MyCustomer customer = (MyCustomer)itemList.get(getAdapterPosition());
               /* if(customer.getIsFav().equals("Y")){
                    myItemClickListener.onItemClicked(getAdapterPosition(),1);
                }else{
                    myItemClickListener.onItemClicked(getAdapterPosition(),2);
                }*/

                PopupMenu popupMenu = new PopupMenu(view.getContext(), imageMenu);

                ((Activity)context).getMenuInflater().inflate(R.menu.customer_popup_menu, popupMenu.getMenu());

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle()+" position "+position, Toast.LENGTH_SHORT).show();
                        if(item.getTitle().equals("Call")){
                            if(customer.getIsFav().equals("Y")){
                                myItemClickListener.onItemClicked(getAdapterPosition(),1);
                            }else{
                                myItemClickListener.onItemClicked(getAdapterPosition(),3);
                            }
                            Log.i("Adapter","Call Customer"+customer.getName());
                        }else if(item.getTitle().equals("Message")){
                            if(customer.getIsFav().equals("Y")){
                                myItemClickListener.onItemClicked(getAdapterPosition(),2);
                            }else{
                                myItemClickListener.onItemClicked(getAdapterPosition(),4);
                            }
                            Log.i("Adapter","Message Customer"+customer.getName());
                        }else if(item.getTitle().equals("Location")){
                            if(customer.getIsFav().equals("Y")){
                                myItemClickListener.onItemClicked(getAdapterPosition(),5);
                            }else{
                                myItemClickListener.onItemClicked(getAdapterPosition(),6);
                            }
                            Log.i("Adapter","Location Customer"+customer.getName());
                        }
                        return true;
                    }
                });

            }
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
                    intent.putExtra("mobile",item.getMobile());
                    intent.putExtra("country",item.getCountry());
                    intent.putExtra("stateCity",item.getState()+", "+item.getCity());
                    intent.putExtra("locality",item.getLocality());
                    intent.putExtra("longitude",item.getLongitude());
                    intent.putExtra("latitude",item.getLatitude());
                    intent.putExtra("customerImage",item.getImage());
                    intent.putExtra("isFav",item.getIsFav());
                    intent.putExtra("custCode",item.getCode());
                    intent.putExtra("custId",item.getId());
                    intent.putExtra("ratings",item.getRatings());
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
        /*if(type.equals("customerList")){
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
        }*/

        return 2;
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
            myViewHolder.textCustName.setText(item.getName());
            myViewHolder.textMobile.setText(item.getMobile());
            if(TextUtils.isEmpty(item.getAddress()) || item.getAddress().equals("null")){
                myViewHolder.textAddress.setVisibility(View.GONE);
            }else{
                myViewHolder.textAddress.setVisibility(View.GONE);
                myViewHolder.textAddress.setText(item.getAddress());
            }

            if(TextUtils.isEmpty(item.getState()) || item.getState().equals("null")){
                myViewHolder.textStateCity.setVisibility(View.GONE);
            }else{
                myViewHolder.textStateCity.setVisibility(View.VISIBLE);
                myViewHolder.textStateCity.setText(item.getState()+", "+item.getCity());
            }

            String initials = "";
            if(item.getName().contains(" ")){
                String[] name = item.getName().split(" ");
                initials = name[0].substring(0,1);
            }else{
                initials = item.getName().substring(0,1);
            }

            myViewHolder.textInitial.setText(initials);

            if(item.getImage() != null && item.getImage().contains("http")){
                myViewHolder.textInitial.setVisibility(View.GONE);
                myViewHolder.imageView.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.textInitial.setVisibility(View.VISIBLE);
                myViewHolder.imageView.setVisibility(View.GONE);
              //  myViewHolder.textInitial.setBackgroundColor(getTvColor(counter));
                Utility.setColorFilter(myViewHolder.textInitial.getBackground(),getTvColor(counter));

                counter++;
                if(counter == 12){
                    counter = 0;
                }
            }

           // myViewHolder.textAddress.setText(item.getAddress());
          //  myViewHolder.textStateCity.setText(item.getState()+", "+item.getCity());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.dontTransform();
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            // requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Glide.with(context)
                    .load(item.getImage())
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

    private int getTvColor(int position){
        if(position >= 12){
            position = 0;
        }

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
