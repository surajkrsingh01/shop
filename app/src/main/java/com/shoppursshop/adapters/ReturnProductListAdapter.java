package com.shoppursshop.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.SaleReturn;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 20-04-2019.
 */

public class ReturnProductListAdapter extends RecyclerView.Adapter<ReturnProductListAdapter.MyViewHolder> {
    private List<SaleReturn> itemList = new ArrayList<>();
    private Context context;
    private int coloTheme,counter;
    private String shopCode;
    private boolean isDarkTheme;
    private Typeface typeface;

    public void setColoTheme(int coloTheme) {
        this.coloTheme = coloTheme;
    }

    private MyImageClickListener myImageClickListener;
    private MyItemTypeClickListener myItemTypeClickListener;

    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public ReturnProductListAdapter(Context context, List<SaleReturn> myProducts) {
        super();
        this.context = context;
        this.itemList = myProducts;
    }

    public void setTypeFace(Typeface typeFace){
        this.typeface = typeFace;
    }

    public void setShopCode(String code){
        this.shopCode = code;
    }

    @Override
    public ReturnProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.return_product_list_item_layout, parent, false);
        return new ReturnProductListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReturnProductListAdapter.MyViewHolder myViewHolder, final int position) {
        {
            final SaleReturn item =  itemList.get(position);
            myViewHolder.textName.setText(item.getProdName());
            myViewHolder.textSp.setText(Utility.numberFormat(Double.valueOf(item.getProdSp())));
            myViewHolder.textMrp.setText(Utility.numberFormat(Double.valueOf(item.getProdMrp())));
            myViewHolder.textMrp.setPaintFlags(myViewHolder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (item.getStatus().equals("1")) {
                    myViewHolder.relative_return_product.setVisibility(View.VISIBLE);
                    myViewHolder.text_return_status.setVisibility(View.GONE);
                } else {
                    myViewHolder.relative_return_product.setVisibility(View.GONE);
                    myViewHolder.text_return_status.setVisibility(View.VISIBLE);
                    if (item.getStatus().equals("0"))
                        myViewHolder.text_return_status.setText("Cancelled");
                    else if (item.getStatus().equals("2"))
                        myViewHolder.text_return_status.setText("Pending");
                    else if (item.getStatus().equals("3"))
                        myViewHolder.text_return_status.setText("Returned");
                }

            float diff = Float.valueOf(item.getProdMrp()) - Float.valueOf(item.getProdSp());
            if(diff > 0f){
                float percentage = diff * 100 /Float.valueOf(item.getProdMrp());
                myViewHolder.textOffPer.setText(String.format("%.02f",percentage)+"% off");
                myViewHolder.textMrp.setVisibility(View.VISIBLE);
                myViewHolder.textOffPer.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.textMrp.setVisibility(View.GONE);
                myViewHolder.textOffPer.setVisibility(View.GONE);
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
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textName,textMrp, textSp, textOffPer, text_return_status,tvInitials;
        private ImageView imageView;
        private View rootView;
        private RelativeLayout relative_return_product;
        private Button btn_return;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textName=itemView.findViewById(R.id.text_name);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textSp=itemView.findViewById(R.id.text_sp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            tvInitials=itemView.findViewById(R.id.tvInitial);
            imageView=itemView.findViewById(R.id.image_view);
            text_return_status = itemView.findViewById(R.id.text_return_status);
            relative_return_product = itemView.findViewById(R.id.relative_return_product);
            btn_return = itemView.findViewById(R.id.btn_return);
            btn_return.setOnClickListener(this);
            Utility.setColorFilter(btn_return.getBackground(),coloTheme);
            rootView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SaleReturn item = (SaleReturn) itemList.get(getAdapterPosition());
            if(v == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),0,imageView);
              //  ((ReturnProductsActivity)context).showLargeImageDialog(item, imageView);
            }else if(v == rootView){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),1);
             //   ((ReturnProductsActivity)context).showProductDetails(item);
            }else if(v == btn_return){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
             //   ((ReturnProductsActivity)context).acceptRequest(item);
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