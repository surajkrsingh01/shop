package com.shoppursshop.activities.settings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.adapters.PaymentSchemeAdapter;
import com.shoppursshop.models.MyProductItem;

import java.util.List;

public class ComboOfferAdapter extends RecyclerView.Adapter<ComboOfferAdapter.MyComboViewHolder> {
    private Context context;
    private List<MyProductItem> myProductItems;

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
    }

    @Override
    public int getItemCount() {
        return myProductItems.size();
    }

    public class MyComboViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EditText edit_prodName;

        public MyComboViewHolder(View itemView) {
            super(itemView);
            edit_prodName = itemView.findViewById(R.id.edit_name);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
