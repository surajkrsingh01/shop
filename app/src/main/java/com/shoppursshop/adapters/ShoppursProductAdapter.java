package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.ProductDetailActivity;
import com.shoppursshop.activities.settings.AddPaymentDevice;
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
    private int colorTheme;

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
            myViewHolder.textMrp.setText("MRP: Rs"+ Utility.numberFormat(item.getProdMrp()));

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

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(true);

            Glide.with(context)
                    .load(item.getProdImage1())
                    .apply(requestOptions)
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
        private TextView textName,textMrp, textbarcode, tv_cartCount;
        private ImageView imageView;
        private View rootView;
        private Button btnAddCart, btn_plus, btn_minus;
        private LinearLayout linear_plus_minus;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textbarcode = itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textMrp=itemView.findViewById(R.id.text_mrp);
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
        }

        @Override
        public void onClick(View v) {
            if(v == rootView){
                MyProductItem item = (MyProductItem) myProductsList.get(getAdapterPosition());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("myProduct",item);
                intent.putExtra("flag","shoppurs_product");
                context.startActivity(intent);
            }
        }
    }
}