package com.shoppursshop.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyColor;
import com.shoppursshop.models.MyUnitMeasure;
import com.shoppursshop.models.ProductColor;
import com.shoppursshop.models.ProductSize;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.utilities.Utility;

import java.util.List;

public class SpecificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;

    private MyItemTypeClickListener myItemTypeClickListener;

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public SpecificationAdapter(Context context, List<Object> itemList, String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;
    }

    public class MyUnitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;
        private ImageView imageDelete;
        private RelativeLayout container;
        public MyUnitViewHolder(View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            textHeader=itemView.findViewById(R.id.text_name);
            imageDelete=itemView.findViewById(R.id.image_delete);
            imageDelete.setOnClickListener(this);
            if(type.equals("unitDetail") || type.equals("sizeDetail") || type.equals("colorDetail")){
                imageDelete.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            if(type.equals("unit")){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),1);
            }else{
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
            }

        }
    }

    public class MyColorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;
        private ImageView imageColor,imageDelete;
        private RelativeLayout container;
        public MyColorViewHolder(View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.container);
            textHeader=itemView.findViewById(R.id.text_name);
            imageColor=itemView.findViewById(R.id.image_color);
            imageDelete=itemView.findViewById(R.id.image_delete);
            imageDelete.setOnClickListener(this);
            if(type.equals("unitDetail") || type.equals("sizeDetail") || type.equals("colorDetail")){
                imageDelete.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
             myItemTypeClickListener.onItemClicked(getAdapterPosition(),3);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.unit_item_layout, parent, false);
                viewHolder = new MyUnitViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.color_item_layout, parent, false);
                viewHolder = new MyColorViewHolder(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.unit_item_layout, parent, false);
                viewHolder = new MyUnitViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("unit") || type.equals("size") || type.equals("unitDetail") || type.equals("sizeDetail")){
            return 0;
        }else{
            return 1;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyUnitViewHolder){
            MyUnitViewHolder myViewHolder = (MyUnitViewHolder)holder;
            if(type.equals("unit")){
                ProductUnit item = (ProductUnit)itemList.get(position);
                if(item.getStatus().equals("D")){
                    myViewHolder.container.setVisibility(View.GONE);
                }else{
                    myViewHolder.container.setVisibility(View.VISIBLE);
                }
                myViewHolder.textHeader.setText(item.getUnitValue()+" "+item.getUnitName());
            }else if(type.equals("size")){
                ProductSize item = (ProductSize)itemList.get(position);
                if(item.getStatus().equals("D")){
                    myViewHolder.container.setVisibility(View.GONE);
                }else{
                    myViewHolder.container.setVisibility(View.VISIBLE);
                }
                myViewHolder.textHeader.setText(item.getSize());
            }

        }else if(holder instanceof MyColorViewHolder){
            MyColorViewHolder myViewHolder = (MyColorViewHolder)holder;
            ProductColor item = (ProductColor)itemList.get(position);
            if(item.getStatus().equals("D")){
                myViewHolder.container.setVisibility(View.GONE);
            }else{
                myViewHolder.container.setVisibility(View.VISIBLE);
            }
            myViewHolder.textHeader.setText(item.getColorName());
            Utility.setColorFilter(myViewHolder.imageColor.getBackground(),Integer.parseInt(item.getColorValue()));
        }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
