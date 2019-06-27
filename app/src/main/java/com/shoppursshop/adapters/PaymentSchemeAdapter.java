package com.shoppursshop.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.utilities.Utility;

import java.util.List;

public class PaymentSchemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MyProductItem> itemList;
    private Context context;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public PaymentSchemeAdapter(Context context, List<MyProductItem> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader, textAmount;
        private ImageView imageViewSelected;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_name);
            textAmount = itemView.findViewById(R.id.text_amount);
            imageViewSelected=itemView.findViewById(R.id.image_selected);
            imageViewSelected.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemList.get(getAdapterPosition()).setSelected(true);
            myItemClickListener.onItemClicked(getAdapterPosition());
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v0 = inflater.inflate(R.layout.simple_payment_option_item, parent, false);
        viewHolder = new MyHomeHeaderViewHolder(v0);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHomeHeaderViewHolder){
            MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
            MyProductItem productItem = itemList.get(position);
            myViewHolder.textHeader.setText(productItem.getProdName());
            myViewHolder.textAmount.setText(Utility.numberFormat(productItem.getProdMrp()));
            if(productItem.isSelected()) {
                myViewHolder.imageViewSelected.setVisibility(View.VISIBLE);
            }
            else
                myViewHolder.imageViewSelected.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
