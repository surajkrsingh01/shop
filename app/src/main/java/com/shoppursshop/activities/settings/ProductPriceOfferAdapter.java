package com.shoppursshop.activities.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    private float sp;

    public float getSp() {
        return sp;
    }

    public void setSp(float sp) {
        this.sp = sp;
    }

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
        if(productItem.getQty() > 0){
            holder.edit_price.setText(String.format("%.02f",sp));
            if(productItem.getOfferPrice() > 0f){
                holder.editTextOfferPrice.setText(String.format("%.02f",productItem.getOfferPrice()));
            }else{
                holder.editTextOfferPrice.setText("");
            }
            holder.editTextQty.setText(""+productItem.getQty());
        }
    }

    @Override
    public int getItemCount() {
        return myProductItems.size();
    }

    public class MyProductPriceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private EditText edit_price,editTextQty,editTextOfferPrice;

        public MyProductPriceViewHolder(View itemView) {
            super(itemView);
            edit_price = itemView.findViewById(R.id.edit_price);
            editTextQty = itemView.findViewById(R.id.edit_quantity);
            editTextOfferPrice = itemView.findViewById(R.id.edit_offer_price);

            editTextOfferPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!TextUtils.isEmpty(editable.toString())){
                        MyProductItem item = myProductItems.get(getAdapterPosition());
                        item.setOfferPrice(Float.parseFloat(editable.toString()));
                       // notifyItemChanged(getAdapterPosition());
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
