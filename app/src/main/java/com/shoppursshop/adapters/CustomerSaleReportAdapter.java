package com.shoppursshop.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.CustomerSaleReport;
import com.shoppursshop.utilities.Utility;

import java.util.List;

public class CustomerSaleReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CustomerSaleReport> itemList;
    private Context context;
    private String type;
    private int coloTheme;

    public void setColoTheme(int coloTheme) {
        this.coloTheme = coloTheme;
    }

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public CustomerSaleReportAdapter(Context context, List<CustomerSaleReport> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName,textMobile,textAmount;
        private Button btnInvoice;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.text_name);
            textMobile=itemView.findViewById(R.id.text_mobile);
            textAmount=itemView.findViewById(R.id.text_amount);
            btnInvoice=itemView.findViewById(R.id.btn_invoice);
            btnInvoice.setOnClickListener(this);

            Utility.setColorFilter(btnInvoice.getBackground(),coloTheme);
        }

        @Override
        public void onClick(View view) {
            myItemClickListener.onItemClicked(getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.customer_sale_order_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.customer_sale_order_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHomeHeaderViewHolder){
            MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
            CustomerSaleReport item = itemList.get(position);
            myViewHolder.textName.setText(item.getName());
            myViewHolder.textMobile.setText(item.getMobile());
            myViewHolder.textAmount.setText(Utility.numberFormat(item.getAmount()));

        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
