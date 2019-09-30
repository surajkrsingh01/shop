package com.shoppursshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.shoppursshop.R;
import com.shoppursshop.activities.settings.ChatActivity;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.ChatUser;
import com.shoppursshop.utilities.Utility;

import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ChatUser> itemList;
    private Context context;
    private int counter;

    public ChatUserAdapter(Context context, List<ChatUser> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName,textLastMessage,textInitials;
        public RelativeLayout viewForeground;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            viewForeground=itemView.findViewById(R.id.rl_foreground);
            textName=itemView.findViewById(R.id.text_name);
            textLastMessage=itemView.findViewById(R.id.text_last_message);
            textInitials=itemView.findViewById(R.id.tv_initial);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ChatUser item = itemList.get(getAdapterPosition());
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("messageTo",item.getUserCode());
            intent.putExtra("messageToName",item.getUserName());
            intent.putExtra("messageToMobile",item.getUserMobile());
            intent.putExtra("messageToPic",item.getUserPic());
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
                View v0 = inflater.inflate(R.layout.chat_user_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            default:
                View v = inflater.inflate(R.layout.chat_user_item_layout, parent, false);
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
            ChatUser myUser = itemList.get(position);
            myViewHolder.textName.setText(myUser.getUserName());
            myViewHolder.textLastMessage.setText(myUser.getLastMessage());
            String initials = "";
            if(myUser.getUserName().contains(" ")){
                String[] name = myUser.getUserName().split(" ");
                initials = name[0].substring(0,1);
            }else{
                initials = myUser.getUserName().substring(0,1);
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
