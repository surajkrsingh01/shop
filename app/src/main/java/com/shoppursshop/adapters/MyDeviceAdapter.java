package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppursshop.R;
import com.shoppursshop.activities.settings.UserListActivity;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyDevice;
import com.shoppursshop.utilities.Utility;

import java.util.List;

public class MyDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MyDevice> itemList;
    private Context context;
    private int colorTheme;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setColorTheme(int colorTheme) {
        this.colorTheme = colorTheme;
    }

    public MyDeviceAdapter(Context context, List<MyDevice> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textModel,textSerNumber,text_user_name,text_user_mobile;
        private Button btn_allocate;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textModel=itemView.findViewById(R.id.tv_model_type);
            textSerNumber=itemView.findViewById(R.id.tv_serial_number);
            text_user_name=itemView.findViewById(R.id.text_user_name);
            text_user_mobile=itemView.findViewById(R.id.text_user_mobile);
            btn_allocate=itemView.findViewById(R.id.btn_allocate);
            Utility.setColorFilter(btn_allocate.getBackground(),colorTheme);
            btn_allocate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, UserListActivity.class);
            intent.putExtra("flag","allocateDevice");
            intent.putExtra("deviceId",itemList.get(getAdapterPosition()).getId());
            context.startActivity(intent);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.device_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.device_item_layout, parent, false);
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

            MyDevice item = itemList.get(position);

            myViewHolder.textModel.setText(item.getModel());
            myViewHolder.textSerNumber.setText(item.getSerialNumber());

            if(item.getAllottedUserId().equals("0")){
                myViewHolder.text_user_name.setText("Not allotted");
                myViewHolder.text_user_mobile.setVisibility(View.GONE);
                myViewHolder.btn_allocate.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.text_user_name.setText(item.getAllottedUserName());
                myViewHolder.text_user_mobile.setText(item.getAllottedUserMobile());
                myViewHolder.text_user_mobile.setVisibility(View.VISIBLE);
                myViewHolder.btn_allocate.setVisibility(View.GONE);
            }
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
