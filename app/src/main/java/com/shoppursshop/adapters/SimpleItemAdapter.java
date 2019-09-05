package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyLevelItemClickListener;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.MySimpleItem;
import com.shoppursshop.utilities.Constants;
import com.shoppursshop.utilities.Utility;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SimpleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;
    boolean isDarkTheme;

    private MyItemClickListener myItemClickListener;
    private MyLevelItemClickListener myLevelItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setMyLevelItemClickListener(MyLevelItemClickListener myLevelItemClickListener) {
        this.myLevelItemClickListener = myLevelItemClickListener;
    }

    public SimpleItemAdapter(Context context, List<Object> itemList, String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;
        SharedPreferences sharedPreferences= context.getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        isDarkTheme = sharedPreferences.getBoolean(Constants.IS_DARK_THEME,false);
    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;
        private ImageView imageViewSelected;
        private RelativeLayout relativeLayoutContainer;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_name);
            imageViewSelected=itemView.findViewById(R.id.image_selected);
            relativeLayoutContainer = (RelativeLayout)itemView;

            if(!type.equals("simpleSyncList"))
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(type.equals("levelSimpleList")){
                MySimpleItem item = (MySimpleItem)itemList.get(getAdapterPosition());
                if(item.isSelected()){
                    item.setSelected(false);
                }else{
                    item.setSelected(true);
                }
                notifyItemChanged(getAdapterPosition());
               // myLevelItemClickListener.onLevelItemClicked(getAdapterPosition(),item.getPosition());
            }else if(type.equals("levelProductList")){
                MyProductItem item = (MyProductItem)itemList.get(getAdapterPosition());
                if(item.isSelected()){
                    item.setSelected(false);
                }else{
                    item.setSelected(true);
                }
                notifyItemChanged(getAdapterPosition());
                // myLevelItemClickListener.onLevelItemClicked(getAdapterPosition(),item.getPosition());
            }else{
                myItemClickListener.onItemClicked(getAdapterPosition());
            }

        }
    }

    public class MyHomeHeader1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;
        private Button btnSelectAll;
        private RecyclerView recyclerView;

        public MyHomeHeader1ViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
            btnSelectAll=itemView.findViewById(R.id.btn_see_all);
            recyclerView=itemView.findViewById(R.id.recycler_view);
            btnSelectAll.setText("Select All");
            //((GradientDrawable)btnSelectAll.getBackground()).setColor(colorTheme);
            btnSelectAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == btnSelectAll){
              myLevelItemClickListener.onLevelItemClicked(getAdapterPosition(),-1);
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.simple_list_item, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.header_list_item_type_1_layout, parent, false);
                viewHolder = new MyHomeHeader1ViewHolder(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.simple_list_item, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("simpleList") || type.equals("levelSimpleList")
                || type.equals("levelProductList") || type.equals("simpleSyncList")){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHomeHeaderViewHolder){
            if(type.equals("levelProductList")){
                MyProductItem item = (MyProductItem) itemList.get(position);
                MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
                myViewHolder.textHeader.setText(item.getProdName());
                if(item.isSelected()){
                    myViewHolder.imageViewSelected.setVisibility(View.VISIBLE);
                    if(isDarkTheme){
                        Utility.setColorFilter(myViewHolder.imageViewSelected.getDrawable(),
                                context.getResources().getColor(R.color.white));
                    }else{
                        Utility.setColorFilter(myViewHolder.imageViewSelected.getDrawable(),
                                context.getResources().getColor(R.color.primary_text_color));
                    }
                   // myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.grey200));
                  //  myViewHolder.textHeader.setTextColor(context.getResources().getColor(R.color.black));
                }else{
                    if(type.equals("simpleSyncList")){
                        myViewHolder.imageViewSelected.setBackgroundResource(R.drawable.ic_clear_red_24dp);
                    }else{
                        myViewHolder.imageViewSelected.setVisibility(View.GONE);
                    }


                    /*if(isDarkTheme) {
                        myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.dark_color));
                        myViewHolder.textHeader.setTextColor(context.getResources().getColor(R.color.white));
                    }
                    else{
                        myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
                        myViewHolder.textHeader.setTextColor(context.getResources().getColor(R.color.black));
                    }*/
                }
            }else{
                MySimpleItem item = (MySimpleItem) itemList.get(position);
                MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
                myViewHolder.textHeader.setText(item.getName());
                if(item.isSelected()){
                    myViewHolder.imageViewSelected.setVisibility(View.VISIBLE);
                    if(isDarkTheme){
                        Utility.setColorFilter(myViewHolder.imageViewSelected.getDrawable(),
                                context.getResources().getColor(R.color.white));
                    }else{
                        Utility.setColorFilter(myViewHolder.imageViewSelected.getDrawable(),
                                context.getResources().getColor(R.color.primary_text_color));
                    }
                   // myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.grey200));
                  //  myViewHolder.textHeader.setTextColor(context.getResources().getColor(R.color.black));
                }else{
                    myViewHolder.imageViewSelected.setVisibility(View.GONE);
                  /*  if(isDarkTheme) {
                        myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.dark_color));
                        myViewHolder.textHeader.setTextColor(context.getResources().getColor(R.color.white));
                    }
                    else{
                        myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
                        myViewHolder.textHeader.setTextColor(context.getResources().getColor(R.color.black));
                    }*/
                }
            }

        }else if(holder instanceof MyHomeHeader1ViewHolder){

            CatListItem item = (CatListItem) itemList.get(position);
            MyHomeHeader1ViewHolder myViewHolder = (MyHomeHeader1ViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());

            if(item.isSelectingAll()){
               myViewHolder.btnSelectAll.setText("Select All");
            }else{
                myViewHolder.btnSelectAll.setText("Unselect All");
            }

            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context);
            myViewHolder.recyclerView.setLayoutManager(layoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            String flag = "";
            if(type.equals("productList")){
                flag = "levelProductList";
            }else{
                flag = "levelSimpleList";
            }

            SimpleItemAdapter myItemAdapter = new SimpleItemAdapter(context,item.getItemList(),flag);
            myItemAdapter.setMyLevelItemClickListener(myLevelItemClickListener);
            myViewHolder.recyclerView.setAdapter(myItemAdapter);

            //  StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
            //   layoutParams.setFullSpan(true);
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
