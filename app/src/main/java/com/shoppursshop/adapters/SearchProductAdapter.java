package com.shoppursshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shoppursshop.R;
import com.shoppursshop.activities.AddProductActivity;
import com.shoppursshop.activities.ProductDetailActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 18-04-2019.
 */

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyViewHolder> {
    private List<MyProductItem> myProductsList;
    private Context context;
    private DbHelper dbHelper;
    private String flag;
    private int counter;

    private MyItemClickListener myItemClickListener;
    private MyImageClickListener myImageClickListener;

    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public SearchProductAdapter(Context context, List<MyProductItem> myProducts) {
        super();
        this.context = context;
        this.myProductsList = myProducts;
        dbHelper = new DbHelper(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_list_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        {
            final MyProductItem item = (MyProductItem) myProductsList.get(position);
            myViewHolder.textBarCode.setText(item.getProdBarCode());
            myViewHolder.textName.setText(item.getProdName());
            //myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getMrp()));
            myViewHolder.textSp.setText(Utility.numberFormat(item.getProdSp()));
            myViewHolder.textMrp.setText(Utility.numberFormat(item.getProdMrp()));
            myViewHolder.textMrp.setPaintFlags(myViewHolder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            float diff = item.getProdMrp() - item.getProdSp();
            if(diff > 0f){
                float percentage = diff * 100 /item.getProdMrp();
                myViewHolder.textOffPer.setText(String.format("%.02f",percentage)+"% off");
                myViewHolder.textMrp.setVisibility(View.VISIBLE);
                myViewHolder.textOffPer.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.textMrp.setVisibility(View.GONE);
                myViewHolder.textOffPer.setVisibility(View.GONE);
            }

            if(flag.equals("searchCartProduct")){
                if(item.getIsBarCodeAvailable().equals("Y")){
                    if(item.getBarcodeList() != null && item.getBarcodeList().size() > 0){
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }else{
                        myViewHolder.textStatus.setText("Out of stock");
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }
                }else{
                    if(item.getProdQoh() > 0){
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }else{
                        myViewHolder.textStatus.setText("Out of stock");
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }
                }
            }

            String initials = "";
            if(item.getProdName().contains(" ")){
                String[] name = item.getProdName().split(" ");
                if(name[1].startsWith("(")){
                    initials = name[0].substring(0,1)+name[1].substring(1,2);
                }else{
                    initials = name[0].substring(0,1)+name[1].substring(0,1);
                }
            }else{
                initials = item.getProdName().substring(0,2);
            }

            myViewHolder.tvInitials.setText(initials);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Glide.with(context)
                    .load(item.getProdImage1())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            myViewHolder.tvInitials.setVisibility(View.VISIBLE);
                            myViewHolder.imageView.setVisibility(View.GONE);
                            //  myViewHolder.textInitial.setBackgroundColor(getTvColor(counter));
                            Utility.setColorFilter(myViewHolder.tvInitials.getBackground(),getTvColor(counter));

                            counter++;
                            if(counter == 12){
                                counter = 0;
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //on load success
                            return false;
                        }
                    })
                    .into(myViewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (myProductsList != null) {
            return myProductsList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textBarCode,textName,textSp,textMrp,textOffPer,textStatus,tvInitials;
        private ImageView imageView,imageMenu;
        private Button btnAddToCart;
        private View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textBarCode=itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textSp=itemView.findViewById(R.id.text_sp);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            tvInitials=itemView.findViewById(R.id.tvInitial);
            textStatus=itemView.findViewById(R.id.text_status);
            btnAddToCart=itemView.findViewById(R.id.btn_add_to_cart);
            imageView=itemView.findViewById(R.id.image_view);
            imageMenu=itemView.findViewById(R.id.image_menu);
            imageMenu.setOnClickListener(this);
            imageView.setOnClickListener(this);

            if(flag.equals("order")){
                btnAddToCart.setVisibility(View.VISIBLE);
            }else{
                btnAddToCart.setVisibility(View.GONE);
            }
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == rootView){
                if(flag.equals("searchCartProduct")){
                    myItemClickListener.onItemClicked(getAdapterPosition());
                }else{
                    myItemClickListener.onItemClicked(getAdapterPosition());
                }

            }else if(v == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),1,imageView);
            }else if(v == imageMenu){
                final MyProductItem myProductItem = (MyProductItem) myProductsList.get(getAdapterPosition());

                PopupMenu popupMenu = new PopupMenu(v.getContext(), imageMenu);
                ((Activity)context).getMenuInflater().inflate(R.menu.product_list_popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Edit")){
                            Intent intent = new Intent(context,AddProductActivity.class);
                            intent.putExtra("flag","editProduct");
                            intent.putExtra("type","editProduct");
                            intent.putExtra("product",myProductItem);
                            ((Activity) context).startActivityForResult(intent,2);
                        }else{
                            myProductsList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }

                        return false;
                    }
                });
            }
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
