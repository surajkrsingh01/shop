package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shoppursshop.R;
import com.shoppursshop.activities.ProductDetailActivity;
import com.shoppursshop.activities.settings.AddPaymentDevice;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 20-04-2019.
 */

public class ShoppursProductAdapter extends RecyclerView.Adapter<ShoppursProductAdapter.MyViewHolder> {
    private List<MyProductItem> myProductsList = new ArrayList<>();
    private Context context;
    private int colorTheme,counter;

    private MyImageClickListener myImageClickListener;


    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public ShoppursProductAdapter(Context context, List<MyProductItem> myProducts) {
        super();
        this.context = context;
        this.myProductsList = myProducts;
    }

    public void setColorTheme(int colorTheme){
        this.colorTheme = colorTheme;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_payment_device, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        {
            final MyProductItem item = (MyProductItem) myProductsList.get(position);
            myViewHolder.textbarcode.setText(item.getProdCode());
            myViewHolder.textName.setText(item.getProdName());

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

            myViewHolder.btnAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myViewHolder.linear_plus_minus.setVisibility(View.VISIBLE);
                    myViewHolder.btnAddCart.setVisibility(View.GONE);
                    item.setQty(1);
                    float subtotal = item.getProdMrp() * item.getQty();
                    item.setTotalAmount(subtotal);
                    DialogAndToast.showToast("Add to Cart ", context);
                    ((AddPaymentDevice)context).updateUi();
                }
            });

            myViewHolder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(myViewHolder.tv_cartCount.getText().toString());
                    if(count==1){
                        myViewHolder.linear_plus_minus.setVisibility(View.GONE);
                        myViewHolder.btnAddCart.setVisibility(View.VISIBLE);
                        item.setQty(0);
                        float subtotal = item.getProdMrp() * item.getQty();
                        item.setTotalAmount(subtotal);
                        ((AddPaymentDevice)context).updateUi();
                    }else {
                        myViewHolder.tv_cartCount.setText(String.valueOf(count-1));
                        item.setQty(count-1);
                        float subtotal = item.getProdMrp() * item.getQty();
                        item.setTotalAmount(subtotal);
                        ((AddPaymentDevice)context).updateUi();
                    }
                }
            });
            myViewHolder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = Integer.parseInt(myViewHolder.tv_cartCount.getText().toString());
                    myViewHolder.tv_cartCount.setText(String.valueOf(count+1));
                    item.setQty(count+1);
                    float subtotal = item.getProdMrp() * item.getQty();
                    item.setTotalAmount(subtotal);
                    ((AddPaymentDevice)context).updateUi();
                }
            });

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
        private TextView textName,textSp,textMrp,textOffPer, textbarcode, tv_cartCount,tvInitials;
        private ImageView imageView;
        private View rootView;
        private Button btnAddCart, btn_plus, btn_minus;
        private LinearLayout linear_plus_minus;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textbarcode = itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textSp=itemView.findViewById(R.id.text_sp);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            tvInitials=itemView.findViewById(R.id.tvInitial);
            imageView=itemView.findViewById(R.id.image_view);
            linear_plus_minus = itemView.findViewById(R.id.linear_plus_minus);
            btnAddCart = itemView.findViewById(R.id.btn_addCart);
            ((GradientDrawable)btnAddCart.getBackground()).setColor(colorTheme);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            ((GradientDrawable)btn_plus.getBackground()).setColor(colorTheme);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            ((GradientDrawable)btn_minus.getBackground()).setColor(colorTheme);
            tv_cartCount = itemView.findViewById(R.id.tv_cartCount);
            rootView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == rootView){
                MyProductItem item = (MyProductItem) myProductsList.get(getAdapterPosition());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("myProduct",item);
                intent.putExtra("flag","shoppurs_product");
                context.startActivity(intent);
            }else if(v == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),1,imageView);
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