package com.shoppursshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ShopOfferItem;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 20-04-2019.
 */

public class ShopOfferListAdapter extends RecyclerView.Adapter<ShopOfferListAdapter.MyViewHolder> {
    private List<ShopOfferItem> myItemList = new ArrayList<>();
    private Context context;
    private int colorTheme;

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
                    .load(item.getProductLocalImage())
                    .apply(requestOptions)
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

            rootView.setOnClickListener(this);
            iv_three_dot.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == rootView){
                ShopOfferItem item = (ShopOfferItem) myItemList.get(getAdapterPosition());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("myoffer",item);
                intent.putExtra("flag","shoppurs_offer");
               // context.startActivity(intent);
            }else if(view == iv_three_dot){
                ShopOfferItem item = (ShopOfferItem) myItemList.get(getAdapterPosition());

                PopupMenu popupMenu = new PopupMenu(view.getContext(), iv_three_dot);
                ((Activity)context).getMenuInflater().inflate(R.menu.offer_list_popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Edit")){
                            /*Intent intent = new Intent(context, AddProductActivity.class);
                            intent.putExtra("flag","editProduct");
                            intent.putExtra("type","editProduct");
                            intent.putExtra("product",item);
                            ((Activity) context).startActivityForResult(intent,2);*/
                            DialogAndToast.showToast("Edit Clicked", context);
                        }else if(item.getItemId() == R.id.action_remove){
                            DialogAndToast.showToast("Remove Clicked", context);
                            //myItemClickListener.onItemClicked(getAdapterPosition());
                            //itemList.remove(getAdapterPosition());
                            // notifyItemRemoved(getAdapterPosition());
                        }else{
                            DialogAndToast.showToast("Disable Clicked", context);
                        }

                        return false;
                    }
                });
            }
        }
    }
}