package com.shoppursshop.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.PaymentSchemeAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;

import java.util.List;

public class ComboOfferAdapter extends RecyclerView.Adapter<ComboOfferAdapter.MyComboViewHolder> {
    private Context context;
    private List<MyProductItem> myProductItems;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public ComboOfferAdapter(Context context, List<MyProductItem> myProductItems){
        this.context = context;
        this.myProductItems = myProductItems;
    }

    @NonNull
    @Override
    public MyComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyComboViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.combo_offer_item, parent, false);
        viewHolder = new MyComboViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyComboViewHolder holder, int position) {
        MyProductItem productItem = myProductItems.get(position);
        holder.edit_prodName.setText(productItem.getProdName());
        if(productItem.getProdSp() > 0f){
            holder.edit_quantity.setText("1");
            holder.edit_price.setText(String.format("%.00f",productItem.getProdSp()));
        }

    }

    @Override
    public int getItemCount() {
        return myProductItems.size();
    }

    public class MyComboViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText edit_prodName,edit_quantity,edit_price;

        public MyComboViewHolder(View itemView) {
            super(itemView);
            edit_prodName = itemView.findViewById(R.id.edit_name);
            edit_quantity = itemView.findViewById(R.id.edit_quantity);
            edit_price = itemView.findViewById(R.id.edit_price);
            edit_prodName.setOnClickListener(this);
           // final MyProductItem item = myProductItems.get(getAdapterPosition());
            edit_quantity.addTextChangedListener(new TextWatcher() {
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
                        item.setQty(Integer.parseInt(editable.toString()));
                    }

                }
            });

            edit_price.addTextChangedListener(new TextWatcher() {
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
                        item.setProdSp(Float.parseFloat(editable.toString()));
                    }

                }
            });
        }

        @Override
        public void onClick(View v) {
           myItemClickListener.onItemClicked(getAdapterPosition());
        }
    }
}
