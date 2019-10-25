package com.shoppursshop.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
    private int coloTheme;
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
                    if (item.getStatus().equals("0"))
                        myViewHolder.text_return_status.setText("Cancelled");
                    else myViewHolder.text_return_status.setText("Returned");
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
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Glide.with(context)
                    .load(item.getProdImage1())
                    .apply(requestOptions)
                    .error(R.drawable.ic_photo_black_192dp)
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
        private TextView textName,textMrp, textSp, textOffPer, text_return_status;
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
}