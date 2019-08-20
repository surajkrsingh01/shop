package com.shoppursshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.shoppursshop.activities.AddProductActivity;
import com.shoppursshop.activities.ProductDetailActivity;
import com.shoppursshop.activities.settings.AddPaymentDevice;
import com.shoppursshop.activities.settings.ComboProductOfferActivity;
import com.shoppursshop.activities.settings.CreateCouponOfferActivity;
import com.shoppursshop.activities.settings.FreeProductOfferActivity;
import com.shoppursshop.activities.settings.ProductPriceOfferActivity;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ShopOfferItem;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 20-04-2019.
 */

public class ShopOfferListAdapter extends RecyclerView.Adapter<ShopOfferListAdapter.MyViewHolder> {
    private List<ShopOfferItem> myItemList = new ArrayList<>();
    private Context context;
    private int colorTheme;

    private MyItemTypeClickListener myItemTypeClickListener;
    private MyImageClickListener myImageClickListener;

    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public ShopOfferListAdapter(Context context, List<ShopOfferItem> myItemList) {
        super();
        this.context = context;
        this.myItemList = myItemList;
    }

    public void setColorTheme(int colorTheme){
        this.colorTheme = colorTheme;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_shop_offers, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        {
            final ShopOfferItem item = (ShopOfferItem) myItemList.get(position);
            myViewHolder.text_prodName.setText(item.getProductName());
            myViewHolder.text_offerName.setText(item.getOfferName());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(true);

            Glide.with(context)
                    .load(item.getProductImage())
                    .apply(requestOptions)
                    .error(R.drawable.ic_photo_black_192dp)
                    .into(myViewHolder.imageView);
        }
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
            imageView.setOnClickListener(this);
            rootView.setOnClickListener(this);
            iv_three_dot.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == rootView){
                ShopOfferItem item = (ShopOfferItem) myItemList.get(getAdapterPosition());
                Intent intent = null;
                if(item.getOfferType().equals("free")){
                    intent = new Intent(context, FreeProductOfferActivity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("data",(Serializable) item.getProductObject());
                }else if(item.getOfferType().equals("combo")){
                    intent = new Intent(context, ComboProductOfferActivity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("data",(Serializable) item.getProductObject());
                }else if(item.getOfferType().equals("price")){
                    intent = new Intent(context, ProductPriceOfferActivity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("data",(Serializable) item.getProductObject());
                }else if(item.getOfferType().equals("coupon")){
                    intent = new Intent(context, CreateCouponOfferActivity.class);
                    intent.putExtra("flag","edit");
                    intent.putExtra("data",(Serializable) item.getProductObject());
                }
                context.startActivity(intent);
            }else if(view == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),1,imageView);
            }else if(view == iv_three_dot){
                final ShopOfferItem shopOfferItem = (ShopOfferItem) myItemList.get(getAdapterPosition());

                PopupMenu popupMenu = new PopupMenu(view.getContext(), iv_three_dot);
                ((Activity)context).getMenuInflater().inflate(R.menu.offer_list_popup_menu, popupMenu.getMenu());
                if(shopOfferItem.getOfferStatus().equals("2")){
                    popupMenu.getMenu().getItem(1).setVisible(false);
                    popupMenu.getMenu().getItem(2).setVisible(true);
                    Log.i("Adapter","Enable button is showing...");
                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Edit")){
                            Intent intent = null;
                            if(shopOfferItem.getOfferType().equals("free")){
                                intent = new Intent(context, FreeProductOfferActivity.class);
                                intent.putExtra("flag","edit");
                                intent.putExtra("data",(Serializable) shopOfferItem.getProductObject());
                            }else if(shopOfferItem.getOfferType().equals("combo")){
                                intent = new Intent(context, ComboProductOfferActivity.class);
                                intent.putExtra("flag","edit");
                                intent.putExtra("data",(Serializable) shopOfferItem.getProductObject());
                            }else if(shopOfferItem.getOfferType().equals("price")){
                                intent = new Intent(context, ProductPriceOfferActivity.class);
                                intent.putExtra("flag","edit");
                                intent.putExtra("data",(Serializable) shopOfferItem.getProductObject());
                            }
                            context.startActivity(intent);
                        }else if(item.getItemId() == R.id.action_remove){
                            DialogAndToast.showToast("Remove Clicked", context);
                            myItemTypeClickListener.onItemClicked(getAdapterPosition(),1);
                        }else if(item.getItemId() == R.id.action_disable){
                            myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
                            //DialogAndToast.showToast("Disable Clicked", context);
                        }else{
                            Intent intent = null;
                            if(shopOfferItem.getOfferType().equals("free")){
                                intent = new Intent(context, FreeProductOfferActivity.class);
                                intent.putExtra("flag","edit");
                                intent.putExtra("data",(Serializable) shopOfferItem.getProductObject());
                            }else if(shopOfferItem.getOfferType().equals("combo")){
                                intent = new Intent(context, ComboProductOfferActivity.class);
                                intent.putExtra("flag","edit");
                                intent.putExtra("data",(Serializable) shopOfferItem.getProductObject());
                            }else if(shopOfferItem.getOfferType().equals("price")){
                                intent = new Intent(context, ProductPriceOfferActivity.class);
                                intent.putExtra("flag","edit");
                                intent.putExtra("data",(Serializable) shopOfferItem.getProductObject());
                            }
                            context.startActivity(intent);
                        }

                        return false;
                    }
                });
            }
        }
    }
}