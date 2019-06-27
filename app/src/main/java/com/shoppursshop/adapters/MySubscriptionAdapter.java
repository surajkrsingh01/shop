package com.shoppursshop.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.models.UserLicense;
import com.shoppursshop.utilities.Utility;

import java.util.List;

public class MySubscriptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<UserLicense> itemList;
    private Context context;

    public MySubscriptionAdapter(Context context, List<UserLicense> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textType, textNumOfUsers,textAmount,textExpiryDate;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textType=itemView.findViewById(R.id.tv_license_type);
            textAmount = itemView.findViewById(R.id.text_amount);
            textNumOfUsers = itemView.findViewById(R.id.tv_no_users);
            textExpiryDate = itemView.findViewById(R.id.tv_license_expiry_date);
        }

        @Override
        public void onClick(View view) {

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v0 = inflater.inflate(R.layout.my_subscription_item_layout, parent, false);
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
            UserLicense item = itemList.get(position);
            myViewHolder.textType.setText(item.getLicenseType());
            myViewHolder.textNumOfUsers.setText(""+item.getNumOfUsers());
            if(item.getLicenseType().equals("Free")){
                myViewHolder.textExpiryDate.setText("NA");
                myViewHolder.textAmount.setText("0.0");
            }else{
                myViewHolder.textExpiryDate.setText(Utility.parseDate(item.getExpiryDate(),
                        "yyyy-MM-dd HH:mm:ss","dd MMM yyyy"));
                myViewHolder.textAmount.setText(Utility.numberFormat(item.getAmount()) +" ("+item.getScheme()+")");
            }

        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
