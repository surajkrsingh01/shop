package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.shoppursshop.models.MySimpleItem;

import java.util.List;

public class SimpleItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;

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
        if(type.equals("simpleList") || type.equals("levelSimpleList")){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHomeHeaderViewHolder){
            MySimpleItem item = (MySimpleItem) itemList.get(position);
            MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getName());
            if(item.isSelected()){
               myViewHolder.imageViewSelected.setVisibility(View.VISIBLE);
               myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.grey200));
            }else{
                myViewHolder.imageViewSelected.setVisibility(View.GONE);
                myViewHolder.relativeLayoutContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
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
            SimpleItemAdapter myItemAdapter = new SimpleItemAdapter(context,item.getItemList(),"levelSimpleList");
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
