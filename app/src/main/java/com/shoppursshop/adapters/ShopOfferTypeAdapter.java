package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.settings.ComboProductOfferActivity;
import com.shoppursshop.activities.settings.FreeProductOfferActivity;
import com.shoppursshop.activities.settings.ProductPriceOfferActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 20-04-2019.
 */

public class ShopOfferTypeAdapter extends RecyclerView.Adapter<ShopOfferTypeAdapter.MyViewHolder> {
    private List<String> myItemList = new ArrayList<>();
    private Context context;

    public ShopOfferTypeAdapter(Context context, List<String> myItemList) {
        super();
        this.context = context;
        this.myItemList = myItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.simple_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        {
            final String itemName = myItemList.get(position);
            myViewHolder.text_offerName.setText(itemName);
            myViewHolder.image_selected.setVisibility(View.GONE);
            myViewHolder.image_arrow.setVisibility(View.VISIBLE);

           /* RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(true);

            Glide.with(context)
                    .load(item.getProductLocalImage())
                    .apply(requestOptions)
                    .into(myViewHolder.imageView);*/
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
        private TextView text_offerName;
        private ImageView image_selected, image_arrow;
        private View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            image_selected = itemView.findViewById(R.id.image_selected);
            image_arrow = itemView.findViewById(R.id.image_arrow);
            text_offerName=itemView.findViewById(R.id.text_name);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == rootView){
                String item = myItemList.get(getAdapterPosition());
                Intent intent = null;
                if(item.equals("Free Product Offer")){
                    intent = new Intent(context, FreeProductOfferActivity.class);
                }else if(item.equals("Combo Product Offer")){
                    intent = new Intent(context, ComboProductOfferActivity.class);
                }else if(item.equals("Product Price Offer")){
                    intent = new Intent(context, ProductPriceOfferActivity.class);
                }
                intent.putExtra("myoffer",item);
                intent.putExtra("flag","shoppurs_offer");
                context.startActivity(intent);
            }
        }
    }
}