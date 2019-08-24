package com.shoppursshop.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.BannerDetailsActivity;
import com.shoppursshop.activities.ProductDetailActivity;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTouchListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyItem;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type;
    private boolean isDarkTheme;

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }
    private MyItemTouchListener myItemTouchListener;
    private MyItemTypeClickListener myItemTypeClickListener;
    private MyImageClickListener myImageClickListener;

    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public void setMyItemTouchListener(MyItemTouchListener myItemTouchListener) {
        this.myItemTouchListener = myItemTouchListener;
    }

    private ConstraintSet constraintSet = new ConstraintSet();

    public MyItemAdapter(Context context, List<Object> itemList,String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;


        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_date_range);
            textDesc=itemView.findViewById(R.id.text_desc);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyListType1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textTitle,textName;
        private ImageView imageView;
        private CircleImageView image;
        private View rootView;


        public MyListType1ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textTitle=itemView.findViewById(R.id.text_title);
            textName=itemView.findViewById(R.id.text_name);
            imageView=itemView.findViewById(R.id.image_view);
            //itemView.setOnClickListener(this);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Log.i("Adapter","onPressDown");
                    zoomAnimation(true,rootView);
                    //myItemTouchListener.onPressDown(getAdapterPosition());
                   // return true;
                 break;

                case MotionEvent.ACTION_UP:
                   // Log.i("Adapter","onPressUp");
                    zoomAnimation(false,rootView);
                    HomeListItem item = (HomeListItem) itemList.get(getAdapterPosition());
                    Intent intent = new Intent(context, BannerDetailsActivity.class);
                    intent.putExtra("desc",item.getDesc());
                    context.startActivity(intent);
                    break;
                case MotionEvent.ACTION_CANCEL:
                     Log.i("Adapter","onPressCancel");
                    zoomAnimation(false,rootView);
                    break;
            }
            return true;
        }
    }

    public class MyListType2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textTitle,textDesc,textCat;
        private ImageView imageView;
        private View rootView;

        public MyListType2ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textTitle=itemView.findViewById(R.id.text_title);
            textDesc=itemView.findViewById(R.id.text_desc);
            textCat=itemView.findViewById(R.id.text_category);
            imageView=itemView.findViewById(R.id.image_view);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Log.i("Adapter","onPressDown");
                    zoomAnimation(true,rootView);
                    //myItemTouchListener.onPressDown(getAdapterPosition());
                    break;
                // break;

                case MotionEvent.ACTION_UP:
                    // Log.i("Adapter","onPressUp");
                    zoomAnimation(false,rootView);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i("Adapter","onPressCancel");
                    zoomAnimation(false,rootView);
                    break;
            }
            return true;
        }
    }

    public class MyListType3ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textTitle;
        private ImageView imageView;
        private ConstraintLayout constraintLayout;
        private View rootView;

        public MyListType3ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textTitle=itemView.findViewById(R.id.text_title);
            imageView=itemView.findViewById(R.id.image_view);
            constraintLayout=itemView.findViewById(R.id.parentContsraint);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Log.i("Adapter","onPressDown");
                    zoomAnimation(true,rootView);
                    //myItemTouchListener.onPressDown(getAdapterPosition());
                    break;
                // break;

                case MotionEvent.ACTION_UP:
                    // Log.i("Adapter","onPressUp");
                    zoomAnimation(false,rootView);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i("Adapter","onPressCancel");
                    zoomAnimation(false,rootView);
                    break;
            }
            return true;
        }
    }

    public class BannerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RecyclerView recyclerView;


        public BannerListViewHolder(View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.recycler_view);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textName,textMrp, textSp, textOffPer, textStatus, textbarcode, tv_cartCount;
        private ImageView imageView,image_minus, image_plus;
        private View rootView;
        private Button btnAddCart;
        private LinearLayout linear_plus_minus;
        private Spinner spinnerUnit;
        private RelativeLayout relative_unit;
        private TextView text_offer;

        public MyProductViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            textbarcode = itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textSp=itemView.findViewById(R.id.text_sp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            textStatus=itemView.findViewById(R.id.text_status);
            imageView=itemView.findViewById(R.id.image_view);
            btnAddCart = itemView.findViewById(R.id.btn_addCart);
            linear_plus_minus = itemView.findViewById(R.id.linear_plus_minus);
            image_plus = itemView.findViewById(R.id.image_plus);
            image_minus = itemView.findViewById(R.id.image_minus);
            tv_cartCount = itemView.findViewById(R.id.tv_cartCount);
            spinnerUnit = itemView.findViewById(R.id.spinner_unit);
            relative_unit = itemView.findViewById(R.id.relative_unit);
            text_offer = itemView.findViewById(R.id.text_offer);

            rootView.setOnClickListener(this);
            text_offer.setOnClickListener(this);
            imageView.setOnClickListener(this);
            image_plus.setOnClickListener(this);
            image_minus.setOnClickListener(this);
            btnAddCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),0,imageView);
            }else if(view==text_offer){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),1);
            }else if(view == btnAddCart){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
            }else if(view == image_plus){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),3);
            }else if(view == image_minus){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),4);
            }else if(view == rootView){
                MyProductItem item = (MyProductItem) itemList.get(getAdapterPosition());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("flag","shoppurs_product");
                intent.putExtra("myProduct",item);
                context.startActivity(intent);
            }
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;
        private CircleImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_header);
            textDesc=itemView.findViewById(R.id.text_desc);
            image=itemView.findViewById(R.id.image_pic);
            itemView.setOnClickListener(this);
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
                View v0 = inflater.inflate(R.layout.home_list_header_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.list_item_type_1_layout, parent, false);
                viewHolder = new MyListType1ViewHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.list_item_type_2_layout, parent, false);
                viewHolder = new MyListType2ViewHolder(v2);
                break;
            case 3:
                View v3 = inflater.inflate(R.layout.list_item_type_3_layout, parent, false);
                viewHolder = new MyListType3ViewHolder(v3);
                break;
            case 4:
                View v4 = inflater.inflate(R.layout.product_list_item_layout_1, parent, false);
                viewHolder = new MyProductViewHolder(v4);
                break;
            case 5:
                View v5 = inflater.inflate(R.layout.banner_horizontal_item_list, parent, false);
                viewHolder = new BannerListViewHolder(v5);
                break;
            default:
                View v = inflater.inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(type.equals("homeList")){
            if(itemList.get(position) instanceof MyProductItem){
                return 4;
            }else{
                HomeListItem item = (HomeListItem)itemList.get(position);
                if(item.getType() == 0){
                    return 0;
                }else if(item.getType() == 1){
                    return 1;
                }else if(item.getType() == 2){
                    return 2;
                }else if(item.getType() == 3){
                    return 3;
                }else if(item.getType() == 4){
                    return 4;
                }else if(item.getType() == 5){
                    return 5;
                }else{
                    return 10;
                }
            }

        }else if(type.equals("countries")){
            return 2;
        } else{
            return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          if(holder instanceof MyViewHolder){
              MyItem item = (MyItem) itemList.get(position);
              MyViewHolder myViewHolder = (MyViewHolder)holder;
              myViewHolder.textHeader.setText(item.getHeader());
              myViewHolder.textDesc.setText(item.getDesc());
          }else if(holder instanceof MyHomeHeaderViewHolder){


              HomeListItem item = (HomeListItem) itemList.get(position);
              MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
              myViewHolder.textHeader.setText(item.getTitle());
              myViewHolder.textDesc.setText(item.getDesc());

              StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
              layoutParams.setFullSpan(true);

          }else if(holder instanceof MyListType1ViewHolder){
              HomeListItem item = (HomeListItem) itemList.get(position);
              MyListType1ViewHolder myViewHolder = (MyListType1ViewHolder)holder;

              RequestOptions requestOptions = new RequestOptions();
              requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
              requestOptions.override(Utility.dpToPx(300, context), Utility.dpToPx(400, context));
              requestOptions.centerCrop();
              requestOptions.skipMemoryCache(false);

              Glide.with(context)
                      .load(item.getImage())
                      .apply(requestOptions)
                      .into(myViewHolder.imageView);

              //StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
              //layoutParams.setFullSpan(true);


          }else if(holder instanceof MyListType2ViewHolder){
              HomeListItem item = (HomeListItem) itemList.get(position);
              MyListType2ViewHolder myViewHolder = (MyListType2ViewHolder)holder;

              myViewHolder.textCat.setText(item.getCategory());
              myViewHolder.textTitle.setText(item.getTitle());
              myViewHolder.textDesc.setText(item.getDesc());

              RequestOptions requestOptions = new RequestOptions();
              requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
              // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
              requestOptions.centerCrop();
              requestOptions.skipMemoryCache(true);

              Glide.with(context)
                      .load(item.getLocalImage())
                      .apply(requestOptions)
                      .into(myViewHolder.imageView);

              StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
              layoutParams.setFullSpan(true);


          }else if(holder instanceof MyListType3ViewHolder){
              HomeListItem item = (HomeListItem) itemList.get(position);
              MyListType3ViewHolder myViewHolder = (MyListType3ViewHolder)holder;
              myViewHolder.textTitle.setText(item.getTitle());

              RequestOptions requestOptions = new RequestOptions();
              requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
              requestOptions.dontTransform();
              // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
             // requestOptions.centerCrop();
              requestOptions.skipMemoryCache(false);

              Glide.with(context)
                      .load(item.getImage())
                      .apply(requestOptions)
                      .into(myViewHolder.imageView);

              String ratio = String.format("%d:%d", (int)item.getWidth(),(int)item.getHeight());

              constraintSet.clone(myViewHolder.constraintLayout);
              constraintSet.setDimensionRatio(myViewHolder.imageView.getId(), ratio);
              constraintSet.applyTo(myViewHolder.constraintLayout);
          }else if(holder instanceof BannerListViewHolder){
              HomeListItem item = (HomeListItem) itemList.get(position);
              BannerListViewHolder myViewHolder = (BannerListViewHolder)holder;
              myViewHolder.recyclerView.setHasFixedSize(true);
              RecyclerView.LayoutManager layoutManagerHomeMenu=new LinearLayoutManager(context,
                      LinearLayoutManager.HORIZONTAL,false);
              myViewHolder.recyclerView.setLayoutManager(layoutManagerHomeMenu);
              myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
              MyItemAdapter myItemAdapter=new MyItemAdapter(context,item.getItemList(),"homeList");
              myViewHolder.recyclerView.setAdapter(myItemAdapter);

              StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
              layoutParams.setFullSpan(true);
          }else if(holder instanceof MyProductViewHolder){
              final MyProductItem item = (MyProductItem) itemList.get(position);
              final MyProductViewHolder myViewHolder = (MyProductViewHolder)holder;

              // myViewHolder.code.setText(item.getBarCode());
              myViewHolder.textName.setText(item.getProdName());
              //myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getMrp()));
              myViewHolder.textSp.setText(Utility.numberFormat(Double.valueOf(item.getProdSp())));
              myViewHolder.textMrp.setText(Utility.numberFormat(Double.valueOf(item.getProdMrp())));
              myViewHolder.textMrp.setPaintFlags(myViewHolder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


              float diff = Float.valueOf(item.getProdMrp()) - Float.valueOf(item.getProdSp());
              if(diff > 0f){
                  float percentage = diff * 100 /Float.valueOf(item.getProdMrp());
                  myViewHolder.textOffPer.setText(String.format("%.02f",percentage)+"% off");
                  myViewHolder.textMrp.setVisibility(View.VISIBLE);
                  myViewHolder.textOffPer.setVisibility(View.VISIBLE);
              }else{
                  myViewHolder.textMrp.setVisibility(View.GONE);
                  myViewHolder.textOffPer.setVisibility(View.GONE);
              }

              if(item.getQty() > 0){
                  myViewHolder.btnAddCart.setVisibility(View.GONE);
                  myViewHolder.linear_plus_minus.setVisibility(View.VISIBLE);
                  myViewHolder.tv_cartCount.setText(""+item.getQty());
              }else{
                  myViewHolder.btnAddCart.setVisibility(View.VISIBLE);
                  myViewHolder.linear_plus_minus.setVisibility(View.GONE);
              }

              Object ob = item.getProductOffer();

              if(ob instanceof ProductDiscountOffer){
                  ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)ob;
                  if(item.getOfferCounter() > 0){
                      myViewHolder.text_offer.setText(item.getOfferCounter()+" free item - offer on "+item.getProdName());
                  }else{
                      myViewHolder.text_offer.setText(productDiscountOffer.getOfferName());
                  }
              }else if(ob instanceof ProductComboOffer){
                  ProductComboOffer productComboOffer = (ProductComboOffer)ob;
                  myViewHolder.text_offer.setText(productComboOffer.getOfferName());
              }


              if(item.getProductUnitList() != null && item.getProductUnitList().size() > 0){
                  myViewHolder.relative_unit.setVisibility(View.VISIBLE);
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
                  myViewHolder.relative_unit.setVisibility(View.GONE);
              }


              RequestOptions requestOptions = new RequestOptions();
              requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
              // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
              requestOptions.centerCrop();
              requestOptions.skipMemoryCache(false);

              Glide.with(context)
                      .load(item.getProdImage1())
                      .apply(requestOptions)
                      .error(R.drawable.ic_photo_black_192dp)
                      .into(myViewHolder.imageView);

              StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
              layoutParams.setFullSpan(true);
          }
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void zoomAnimation(boolean zoomOut,View view){
        if(zoomOut){
            Animation aniSlide = AnimationUtils.loadAnimation(context,R.anim.zoom_out);
            aniSlide.setFillAfter(true);
            view.startAnimation(aniSlide);
        }else{
            Animation aniSlide = AnimationUtils.loadAnimation(context,R.anim.zoom_in);
            aniSlide.setFillAfter(true);
            view.startAnimation(aniSlide);
        }
    }

}
