package com.shoppursshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyUser;
import com.shoppursshop.utilities.Utility;

import java.util.List;

public class MyUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MyUser> itemList;
    private Context context;
    private String type;
    private int counter;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public MyUserAdapter(Context context, List<MyUser> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName,textMobile,textInitials;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textName=itemView.findViewById(R.id.text_name);
            textMobile=itemView.findViewById(R.id.text_mobile);
            textInitials=itemView.findViewById(R.id.tv_initial);
        }

        @Override
        public void onClick(View view) {
          //  myItemClickListener.onItemClicked(getAdapterPosition());
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.shop_user_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.shop_user_item_layout, parent, false);
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
            MyUser myUser = itemList.get(position);
            myViewHolder.textName.setText(myUser.getUsername());
            myViewHolder.textMobile.setText(myUser.getMobile());
            String initials = "";
            if(myUser.getUsername().contains(" ")){
                String[] name = myUser.getUsername().split(" ");
                initials = name[0].substring(0,1);
            }else{
                initials = myUser.getUsername().substring(0,1);
            }

            myViewHolder.textInitials.setText(initials);
            Utility.setColorFilter(myViewHolder.textInitials.getBackground(),getTvColor(counter));

            counter++;
            if(counter == 12){
                counter = 0;
            }
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private int getTvColor(int position){
        if(position >= 12){
            position = 0;
        }

        int[] tvColor={context.getResources().getColor(R.color.light_blue500),
                context.getResources().getColor(R.color.yellow500),context.getResources().getColor(R.color.green500),
                context.getResources().getColor(R.color.orange500),context.getResources().getColor(R.color.red_500),
                context.getResources().getColor(R.color.teal_500),context.getResources().getColor(R.color.cyan500),
                context.getResources().getColor(R.color.deep_orange500),context.getResources().getColor(R.color.blue500),
                context.getResources().getColor(R.color.purple500),context.getResources().getColor(R.color.amber500),
                context.getResources().getColor(R.color.light_green500)};

        return tvColor[position];
    }
}
