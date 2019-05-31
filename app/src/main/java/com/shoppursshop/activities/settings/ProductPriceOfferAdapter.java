package com.shoppursshop.activities.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shoppursshop.R;
import com.shoppursshop.models.MyProductItem;

import java.util.List;

public class ProductPriceOfferAdapter extends RecyclerView.Adapter<ProductPriceOfferAdapter.MyProductPriceViewHolder> {
    private Context context;
    private List<MyProductItem> myProductItems;

    public ProductPriceOfferAdapter(Context context, List<MyProductItem> myProductItems){
        this.context = context;
        this.myProductItems = myProductItems;
    }

    @NonNull
    @Override
    public MyProductPriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyProductPriceViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_price_offer_item, parent, false);
        viewHolder = new MyProductPriceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductPriceViewHolder holder, int position) {
        MyProductItem productItem = myProductItems.get(position);
        holder.edit_offer_price.setText(productItem.getProdName());
    }

    @Override
    public int getItemCount() {
        return myProductItems.size();
    }

    public class MyProductPriceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText edit_offer_price;

        public MyProductPriceViewHolder(View itemView) {
            super(itemView);
            edit_offer_price = itemView.findViewById(R.id.edit_offer_price);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
