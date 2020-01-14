package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MyProductItem> itemList;
    private Context context;
    private boolean isDarkTheme;
    private int counter;

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    private MyItemTypeClickListener myItemTypeClickListener;
    private MyImageClickListener myImageClickListener;

    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public CartAdapter(Context context, List<MyProductItem> itemList) {
        this.itemList = itemList;
        this.context=context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textBarCode,textName,textMrp,textSp,textOffPer,textCounter,textOffer,
                textAdditionDiscount,tvInitials;
        private ImageView imageView,imageViewMinus,imageViewAdd;
        private RelativeLayout rlOffer;
        private RelativeLayout relativeLayoutUnit;
        private Spinner spinnerUnit;
        private View viewSeparator;

        public MyViewHolder(View itemView) {
            super(itemView);
            //textBarCode=itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textSp=itemView.findViewById(R.id.text_sp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            textCounter=itemView.findViewById(R.id.tv_counter);
            tvInitials=itemView.findViewById(R.id.tvInitial);
            imageViewMinus=itemView.findViewById(R.id.image_minus);
            imageViewAdd=itemView.findViewById(R.id.image_add);
            imageView=itemView.findViewById(R.id.image_view);
            viewSeparator=itemView.findViewById(R.id.view_separator);
            textOffer=itemView.findViewById(R.id.text_offer);
            textAdditionDiscount=itemView.findViewById(R.id.text_additional_discount);
            rlOffer=itemView.findViewById(R.id.relative_offer);
            relativeLayoutUnit=itemView.findViewById(R.id.relative_unit);
            spinnerUnit=itemView.findViewById(R.id.spinner_unit);

             textMrp.setPaintFlags(textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            imageViewAdd.setOnClickListener(this);
            imageViewMinus.setOnClickListener(this);
            imageView.setOnClickListener(this);
            rlOffer.setOnClickListener(this);
            textAdditionDiscount.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageViewAdd){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
            }else if(view == rlOffer){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),3);
            }else if(view == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),1,imageView);
            }else if(view == textAdditionDiscount){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),4);
            }else{
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),1);
            }
        }
    }

    public class MyFreeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textName,textFreeItem,textOffer;
        private RelativeLayout relativeLayoutUnit;
        private Spinner spinnerUnit;
        private ImageView imageView;

        public MyFreeViewHolder(View itemView) {
            super(itemView);
            //textBarCode=itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            imageView=itemView.findViewById(R.id.image_view);
            textFreeItem=itemView.findViewById(R.id.text_free_item);
            textOffer=itemView.findViewById(R.id.text_offer);
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

        switch (viewType){
            case 0:
                View v0 = inflater.inflate(R.layout.cart_item_layout, parent, false);
                viewHolder = new MyViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.cart_free_item_layout, parent, false);
                viewHolder = new MyFreeViewHolder(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.cart_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        MyProductItem item = itemList.get(position);
        if(item.getProdSp() == 0f){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyProductItem item = (MyProductItem) itemList.get(position);
        if(holder instanceof MyViewHolder){
            final MyViewHolder myViewHolder = (MyViewHolder)holder;
            //  myViewHolder.textBarCode.setText(item.getProdBarCode());
            myViewHolder.textName.setText(item.getProdName());
            //myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getMrp()));
            myViewHolder.textSp.setText(Utility.numberFormat(item.getProdSp()));
            myViewHolder.textMrp.setText(Utility.numberFormat(item.getProdMrp()));
            myViewHolder.textCounter.setText(""+item.getQty());

            float diff = item.getProdMrp() - item.getProdSp();
            if(diff > 0f){
                float percentage = diff * 100 /item.getProdMrp();
                myViewHolder.textOffPer.setText(String.format("%.02f",percentage)+"% off");
                myViewHolder.textMrp.setVisibility(View.VISIBLE);
                myViewHolder.textOffPer.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.textMrp.setVisibility(View.GONE);
                myViewHolder.textOffPer.setVisibility(View.GONE);
            }

            if(position == itemList.size() -1){
                myViewHolder.viewSeparator.setVisibility(View.GONE);
            }else{
                myViewHolder.viewSeparator.setVisibility(View.VISIBLE);
            }

            if(item.getProductOffer() != null){
                if(item.getProductOffer() instanceof ProductComboOffer){
                    ProductComboOffer productComboOffer = (ProductComboOffer)item.getProductOffer();
                    myViewHolder.textOffer.setText(productComboOffer.getOfferName());
                }else if(item.getProductOffer() instanceof ProductDiscountOffer){
                    ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)item.getProductOffer();
                    if(item.getOfferCounter() > 0){
                        myViewHolder.textOffer.setText(item.getOfferCounter()+" free item - offer on "+item.getProdName());
                    }else{
                        myViewHolder.textOffer.setText(productDiscountOffer.getOfferName());
                    }
                }

                myViewHolder.rlOffer.setVisibility(View.VISIBLE);

            }else{
                myViewHolder.rlOffer.setVisibility(View.GONE);
            }

            if(item.getProductUnitList() != null && item.getProductUnitList().size() > 0){
                myViewHolder.relativeLayoutUnit.setVisibility(View.VISIBLE);
                ;
                List<String> unitList = new ArrayList<>();
                for(ProductUnit unit : item.getProductUnitList()){
                    unitList.add(unit.getUnitValue()+" "+unit.getUnitName());
                }
                ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(context, R.layout.simple_dropdown_list_item, unitList){
                    @Override
                    public boolean isEnabled(int position){
                        return true;
                    }
                    @Override
                    public View getView(int position, View convertView,
                                        ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(isDarkTheme){
                            tv.setTextColor(context.getResources().getColor(R.color.white));
                        }else{
                            tv.setTextColor(context.getResources().getColor(R.color.primary_text_color));
                        }
                        return view;
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        if(isDarkTheme){
                            view.setBackgroundColor(context.getResources().getColor(R.color.dark_color));
                        }else{
                            view.setBackgroundColor(context.getResources().getColor(R.color.white));
                        }
                        TextView tv = (TextView) view;
                        if(isDarkTheme){
                            tv.setTextColor(context.getResources().getColor(R.color.white));
                        }else{
                            tv.setTextColor(context.getResources().getColor(R.color.primary_text_color));
                        }
                        tv.setPadding(20,20,20,20);
                        return view;
                    }
                };

                myViewHolder.spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ProductUnit unit = item.getProductUnitList().get(i);
                        item.setUnit(unit.getUnitValue()+" "+unit.getUnitName());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                myViewHolder.spinnerUnit.setAdapter(unitAdapter);

            }else{
                myViewHolder.relativeLayoutUnit.setVisibility(View.GONE);
            }

            myViewHolder.tvInitials.setText(Utility.getInitials(item.getProdName()));

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            requestOptions.centerCrop();
            requestOptions.skipMemoryCache(false);

            Glide.with(context)
                    .load(item.getProdImage1())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            myViewHolder.tvInitials.setVisibility(View.VISIBLE);
                            myViewHolder.imageView.setVisibility(View.GONE);
                            //  myViewHolder.textInitial.setBackgroundColor(getTvColor(counter));
                            Utility.setColorFilter(myViewHolder.tvInitials.getBackground(),getTvColor(counter));

                            counter++;
                            if(counter == 12){
                                counter = 0;
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //on load success
                            return false;
                        }
                    })
                    .into(myViewHolder.imageView);

        }else if(holder instanceof MyFreeViewHolder){
            MyFreeViewHolder myViewHolder = (MyFreeViewHolder)holder;
            //  myViewHolder.textBarCode.setText(item.getProdBarCode());
            myViewHolder.textName.setText(item.getProdName());
            if(item.getQty() > 1){
                myViewHolder.textFreeItem.setText(item.getQty()+" free items!");
            }else{
                myViewHolder.textFreeItem.setText("1 free item!");
            }

            myViewHolder.textOffer.setText("offer applied");
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
