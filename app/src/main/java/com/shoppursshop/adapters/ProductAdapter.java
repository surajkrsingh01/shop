package com.shoppursshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shoppursshop.R;
import com.shoppursshop.activities.product.AddProductActivity;
import com.shoppursshop.activities.product.ProductDetailActivity;
import com.shoppursshop.activities.product.ProductListActivity;
import com.shoppursshop.activities.ScannarActivity;
import com.shoppursshop.activities.categories.SubCatListActivity;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTouchListener;
import com.shoppursshop.models.Category;
import com.shoppursshop.models.FrequencyProduct;
import com.shoppursshop.models.HomeListItem;
import com.shoppursshop.models.MyHeader;
import com.shoppursshop.models.MyItem;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.SubCategory;
import com.shoppursshop.models.CatListItem;
import com.shoppursshop.utilities.Utility;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Object> itemList;
    private Context context;
    private String type,subCatName,flag;
    private int colorTheme,counter;

    private MyItemTouchListener myItemTouchListener;

    private MyItemClickListener myItemClickListener;

    private MyImageClickListener myImageClickListener;

    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public void setColorTheme(int colorTheme){
        this.colorTheme = colorTheme;
    }

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setMyItemTouchListener(MyItemTouchListener myItemTouchListener) {
        this.myItemTouchListener = myItemTouchListener;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private ConstraintSet constraintSet = new ConstraintSet();

    public ProductAdapter(Context context, List<Object> itemList,String type) {
        this.itemList = itemList;
        this.context=context;
        this.type = type;

    }

    public class MyHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;
        private RecyclerView recyclerView;

        public MyHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_date_range);
            textDesc=itemView.findViewById(R.id.text_desc);
            recyclerView=itemView.findViewById(R.id.recycler_view);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyHomeHeader1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;
        private Button btnSeeAll;
        private RecyclerView recyclerView;

        public MyHomeHeader1ViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
            btnSeeAll=itemView.findViewById(R.id.btn_see_all);
            ((GradientDrawable)btnSeeAll.getBackground()).setColor(colorTheme);
            recyclerView=itemView.findViewById(R.id.recycler_view);

            btnSeeAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == btnSeeAll){
                CatListItem myItem = (CatListItem) itemList.get(getAdapterPosition());
                Intent intent = new Intent(context,SubCatListActivity.class);
                intent.putExtra("catID",""+myItem.getId());
                intent.putExtra("catName",myItem.getTitle());
                intent.putExtra("flag",flag);
                context.startActivity(intent);
            }
        }
    }


    public class MyListType31ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textTitle;
        private ImageView imageView;
       // private ConstraintLayout constraintLayout;
        private View rootView;
        private long downTime,upTime;

        public MyListType31ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textTitle=itemView.findViewById(R.id.text_title);
            imageView=itemView.findViewById(R.id.image_view);
            //constraintLayout=itemView.findViewById(R.id.parentContsraint);
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
                    downTime = new Date().getTime();
                    //myItemTouchListener.onPressDown(getAdapterPosition());
                    break;
                // break;

                case MotionEvent.ACTION_UP:
                    // Log.i("Adapter","onPressUp");
                    zoomAnimation(false,rootView);
                    upTime = new Date().getTime();
                    long diff = upTime - downTime;
                    if(getAdapterPosition() == 0){

                    }else{
                        Category myItem = (Category) itemList.get(getAdapterPosition());
                        Intent intent = new Intent(context,SubCatListActivity.class);
                        intent.putExtra("catID",myItem.getId());
                        intent.putExtra("catName",myItem.getName());
                        intent.putExtra("flag",flag);
                        context.startActivity(intent);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i("Adapter","onPressCancel");
                    zoomAnimation(false,rootView);
                    break;
            }
            return true;
        }
    }

    public class MyListScanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textTitle;
        private ImageView imageView;
        private RelativeLayout relativeLayoutScan,relativeLayoutAddManually;
        // private ConstraintLayout constraintLayout;
        private View rootView;

        private CardView cardView;

        public MyListScanViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textTitle=itemView.findViewById(R.id.text_title);
            imageView=itemView.findViewById(R.id.image_view);
            relativeLayoutScan=itemView.findViewById(R.id.relative_scan);
            relativeLayoutAddManually=itemView.findViewById(R.id.relative_add_manually);
            //constraintLayout=itemView.findViewById(R.id.parentContsraint);
            rootView.setOnTouchListener(this);
            relativeLayoutScan.setOnClickListener(this);
            relativeLayoutAddManually.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == relativeLayoutAddManually){
                Intent intent = new Intent(context,AddProductActivity.class);
                intent.putExtra("flag","manual");
                intent.putExtra("type",flag);
                context.startActivity(intent);
            }else if(view == relativeLayoutScan){
                Intent intent = new Intent(context,ScannarActivity.class);
                intent.putExtra("flag","scan");
                intent.putExtra("type","searchProduct");
                context.startActivity(intent);
            }
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
                    SubCategory myItem = (SubCategory) itemList.get(getAdapterPosition());
                    Intent intent = new Intent(context,ProductListActivity.class);
                    intent.putExtra("subCatID",myItem.getId());
                    intent.putExtra("catId",myItem.getCatId());
                    intent.putExtra("subCatName",myItem.getName());
                    intent.putExtra("flag",flag);
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

    public class MySubHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc;


        public MySubHomeHeaderViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_date_range);
            textDesc=itemView.findViewById(R.id.text_desc);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public class MyProductListType1ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textBarCode,textName,textSp,textMrp,textOffPer,textStatus,tvInitials;
        private ImageView imageView,imageMenu;
        private Button btnAddToCart;
        private View rootView;

        public MyProductListType1ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textBarCode=itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textSp=itemView.findViewById(R.id.text_sp);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            textStatus=itemView.findViewById(R.id.text_status);
            tvInitials=itemView.findViewById(R.id.tvInitial);
            btnAddToCart=itemView.findViewById(R.id.btn_add_to_cart);
            imageView=itemView.findViewById(R.id.image_view);
            imageMenu=itemView.findViewById(R.id.image_menu);
            imageMenu.setOnClickListener(this);
            imageView.setOnClickListener(this);

            if(flag.equals("order")){
                btnAddToCart.setVisibility(View.VISIBLE);
            }else{
                btnAddToCart.setVisibility(View.GONE);
            }
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageMenu){
                final MyProductItem myProductItem = (MyProductItem) itemList.get(getAdapterPosition());

                PopupMenu popupMenu = new PopupMenu(view.getContext(), imageMenu);
                ((Activity)context).getMenuInflater().inflate(R.menu.product_list_popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Edit")){
                            Intent intent = new Intent(context,AddProductActivity.class);
                            intent.putExtra("flag","editProduct");
                            intent.putExtra("type","editProduct");
                            intent.putExtra("product",myProductItem);
                            ((Activity) context).startActivityForResult(intent,2);
                        }else{
                            myItemClickListener.onItemClicked(getAdapterPosition());
                            //itemList.remove(getAdapterPosition());
                           // notifyItemRemoved(getAdapterPosition());
                        }

                        return false;
                    }
                });
            }else if(view == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),1,imageView);
            }
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
                    if(!type.equals("orderProductList")){
                        MyProductItem item = (MyProductItem) itemList.get(getAdapterPosition());
                        Intent intent = new Intent(context,ProductDetailActivity.class);
                        intent.putExtra("id",item.getProdId());
                        intent.putExtra("MyProduct",item);
                        intent.putExtra("subCatName",subCatName);
                        intent.putExtra("flag",type);
                        context.startActivity(intent);
                    }

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

    public class MyOrderProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textName,textSp,textMrp,textOffPer,textQty,tvInitials;
        private ImageView imageView;
        private View rootView;

        public MyOrderProductListViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textName=itemView.findViewById(R.id.text_name);
            textSp=itemView.findViewById(R.id.text_sp);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            tvInitials=itemView.findViewById(R.id.tvInitial);
            textQty=itemView.findViewById(R.id.text_qty);
            imageView=itemView.findViewById(R.id.image_view);
            imageView.setOnClickListener(this);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),1,imageView);
            }

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
                    if(!type.equals("orderProductList")){
                        MyProductItem item = (MyProductItem) itemList.get(getAdapterPosition());
                        Intent intent = new Intent(context,ProductDetailActivity.class);
                        intent.putExtra("id",item.getProdId());
                        intent.putExtra("subCatName",subCatName);
                        intent.putExtra("flag",flag);
                        context.startActivity(intent);
                    }

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

    public class MyFrequencyOrderProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {

        private TextView textName,textSp,textMrp,textOffPer,textQty,
                textStartDate,textEndDate,textNextOrderDate,textStatus,tvInitials;
        private ImageView imageView;
        private View rootView;

        public MyFrequencyOrderProductListViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textName=itemView.findViewById(R.id.text_name);
            textSp=itemView.findViewById(R.id.text_sp);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textOffPer=itemView.findViewById(R.id.text_off_percentage);
            textQty=itemView.findViewById(R.id.text_qty);
            tvInitials=itemView.findViewById(R.id.tvInitial);
            textStartDate=itemView.findViewById(R.id.text_start_date);
            textEndDate=itemView.findViewById(R.id.text_end_date);
            textNextOrderDate=itemView.findViewById(R.id.text_next_order_date);
            textStatus=itemView.findViewById(R.id.text_status);
            imageView=itemView.findViewById(R.id.image_view);
            imageView.setOnClickListener(this);
            rootView.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view == imageView){
                myImageClickListener.onImageClicked(getAdapterPosition(),1,imageView);
            }

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
                    if(!type.equals("orderProductList")){
                        MyProductItem item = (MyProductItem) itemList.get(getAdapterPosition());
                        Intent intent = new Intent(context,ProductDetailActivity.class);
                        intent.putExtra("id",item.getProdId());
                        intent.putExtra("subCatName",subCatName);
                        intent.putExtra("flag",flag);
                        context.startActivity(intent);
                    }

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

    public class MyHomeHeader2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader;
        private Button buttonAdd;

        public MyHomeHeader2ViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_title);
            buttonAdd=itemView.findViewById(R.id.btn_add);
            buttonAdd.setText("Add Product");
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
                View v0 = inflater.inflate(R.layout.product_list_header_item_layout, parent, false);
                viewHolder = new MyHomeHeaderViewHolder(v0);
                break;
            case 1:
                View v1 = inflater.inflate(R.layout.list_item_type_3_1_layout, parent, false);
                viewHolder = new MyListType31ViewHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.header_list_item_type_1_layout, parent, false);
                viewHolder = new MyHomeHeader1ViewHolder(v2);
                break;
            case 3:
                View v3 = inflater.inflate(R.layout.list_item_type_3_layout, parent, false);
                viewHolder = new MyListType3ViewHolder(v3);
                break;
            case 4:
                View v4 = inflater.inflate(R.layout.list_item_scan_layout, parent, false);
                viewHolder = new MyListScanViewHolder(v4);
                break;
            case 5:
                View v5 = inflater.inflate(R.layout.home_list_header_item_layout, parent, false);
                viewHolder = new MySubHomeHeaderViewHolder(v5);
                break;
            case 6:
                View v6 = inflater.inflate(R.layout.list_item_type_3_layout, parent, false);
                viewHolder = new MyListType3ViewHolder(v6);
                break;
            case 7:
                View v7 = inflater.inflate(R.layout.home_list_header_item_layout, parent, false);
                viewHolder = new MySubHomeHeaderViewHolder(v7);
                break;
            case 8:
                View v8 = inflater.inflate(R.layout.product_list_item_layout, parent, false);
                viewHolder = new MyProductListType1ViewHolder(v8);
                break;
            case 9:
                View v9 = inflater.inflate(R.layout.header_item_type_2_layout, parent, false);
                viewHolder = new MyHomeHeader2ViewHolder(v9);
                break;
            case 10:
                View v10 = inflater.inflate(R.layout.order_product_list_item, parent, false);
                viewHolder = new MyOrderProductListViewHolder(v10);
                break;
            case 11:
                View v11 = inflater.inflate(R.layout.frequency_order_product_list, parent, false);
                viewHolder = new MyFrequencyOrderProductListViewHolder(v11);
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
        if(type.equals("catList")){
            Object item = itemList.get(position);
            if(item instanceof CatListItem){
                CatListItem myItem = (CatListItem) item;
                if(myItem.getType() == 0){
                    return 0;
                }else{
                    return 2;
                }

            }else if(item instanceof Category){
                if(position == 0){
                    return 4;
                }else{
                    return 1;
                }

            }else{
                return 3;
            }
        }else if(type.equals("subCatList")){
            Object item = itemList.get(position);
            if(position == 0){
                return 5;
            }else{
                return 6;
            }
        }else if(type.equals("productList") || type.equals("syncedProductList")){
            Object item = itemList.get(position);
           /* if(position == 0){
                return 7;
            }else if(position == 1){
                return 9;
            }else{
                return 8;
            }*/

            return 8;
        }else if(type.equals("orderProductList")){
            return 10;
        }else if(type.equals("frequencyOrderProductList")){
            return 11;
        } else{
            return 12;
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

            CatListItem item = (CatListItem) itemList.get(position);
            MyHomeHeaderViewHolder myViewHolder = (MyHomeHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());
            myViewHolder.textDesc.setText(item.getDesc());

            myViewHolder.recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
            myViewHolder.recyclerView.setLayoutManager(layoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            ProductAdapter myItemAdapter = new ProductAdapter(context,item.getItemList(),"catList");
            myItemAdapter.setFlag(flag);
            myViewHolder.recyclerView.setAdapter(myItemAdapter);

          //  StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
          //  layoutParams.setFullSpan(true);


        }else if(holder instanceof MyHomeHeader1ViewHolder){

            CatListItem item = (CatListItem) itemList.get(position);
            MyHomeHeader1ViewHolder myViewHolder = (MyHomeHeader1ViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());

            myViewHolder.recyclerView.setHasFixedSize(true);
            //RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL);
            myViewHolder.recyclerView.setLayoutManager(staggeredGridLayoutManager);
            myViewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            ProductAdapter myItemAdapter = new ProductAdapter(context,item.getItemList(),"catList");
            myItemAdapter.setFlag(flag);
            myViewHolder.recyclerView.setAdapter(myItemAdapter);

          //  StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
         //   layoutParams.setFullSpan(true);


        }else if(holder instanceof MyListType3ViewHolder){

            SubCategory item = (SubCategory) itemList.get(position);
            MyListType3ViewHolder myViewHolder = (MyListType3ViewHolder)holder;
            myViewHolder.textTitle.setText(item.getName());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.skipMemoryCache(false);
            requestOptions.dontTransform();
            requestOptions.disallowHardwareConfig();
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            // requestOptions.centerCrop();
            requestOptions.skipMemoryCache(true);

            String url = "";
            if(!item.getImage().contains("http")){
                url = context.getResources().getString(R.string.base_url)+"/"+item.getImage();
            }else{
                url = item.getImage();
            }

            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(myViewHolder.imageView);

            /*Picasso.with(context)
                    .load(url)
                    .centerCrop()
                    .into(myViewHolder.imageView);*/

           // Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);

            String ratio = String.format("%d:%d", (int)item.getWidth(),(int)item.getHeight());

            constraintSet.clone(myViewHolder.constraintLayout);
            constraintSet.setDimensionRatio(myViewHolder.imageView.getId(), ratio);
            constraintSet.applyTo(myViewHolder.constraintLayout);

        }else if(holder instanceof MyListType31ViewHolder){
            Category item = (Category) itemList.get(position);
            MyListType31ViewHolder myViewHolder = (MyListType31ViewHolder)holder;
            myViewHolder.textTitle.setText(item.getName());

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.skipMemoryCache(false);
            requestOptions.disallowHardwareConfig();
            //requestOptions.dontTransform();
            // requestOptions.override(Utility.dpToPx(150, context), Utility.dpToPx(150, context));
            // requestOptions.centerCrop();

            String url = "";
            if(!item.getImage().contains("http")){
                url = context.getResources().getString(R.string.base_url)+"/"+item.getImage();
            }else{
                url = item.getImage();
            }

            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(myViewHolder.imageView);

            /*Picasso.with(context)
                    .load(url)
                    .centerCrop()
                    .into(myViewHolder.imageView);*/

        }else if(holder instanceof MySubHomeHeaderViewHolder){

            HomeListItem item = (HomeListItem) itemList.get(position);
            MySubHomeHeaderViewHolder myViewHolder = (MySubHomeHeaderViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());
            myViewHolder.textDesc.setText(item.getDesc());

            if(!type.equals("productList") && !type.equals("syncedProductList")){
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams)myViewHolder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
            }


        }else if(holder instanceof MyProductListType1ViewHolder){
            MyProductItem item = (MyProductItem) itemList.get(position);
            final MyProductListType1ViewHolder myViewHolder = (MyProductListType1ViewHolder)holder;

            myViewHolder.textBarCode.setText(item.getProdBarCode());
            myViewHolder.textName.setText(item.getProdName());
            //myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getMrp()));
            myViewHolder.textSp.setText(Utility.numberFormat(item.getProdSp()));
            myViewHolder.textMrp.setText(Utility.numberFormat(item.getProdMrp()));
            myViewHolder.textMrp.setPaintFlags(myViewHolder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

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

            if(item.getIsBarCodeAvailable().equals("Y")){
                if(item.getBarcodeList() != null && item.getBarcodeList().size() > 0){
                    myViewHolder.textStatus.setVisibility(View.GONE);
                }else{
                    myViewHolder.textStatus.setText("Out of stock");
                    myViewHolder.textStatus.setVisibility(View.GONE);

                }
            }else {
                if (item.getProdQoh() == 0) {
                    myViewHolder.textStatus.setText("Out of stock");
                    myViewHolder.textStatus.setVisibility(View.GONE);
                } else {
                    myViewHolder.textStatus.setVisibility(View.GONE);
                }
            }
            Log.i("Adapter","name "+item.getProdName()+"name");
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


        }else if(holder instanceof MyOrderProductListViewHolder){
            MyProductItem item = (MyProductItem) itemList.get(position);
            final MyOrderProductListViewHolder myViewHolder = (MyOrderProductListViewHolder)holder;

            myViewHolder.textName.setText(item.getProdName());
            //myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getMrp()));
            myViewHolder.textSp.setText(Utility.numberFormat(item.getProdSp()));
            myViewHolder.textMrp.setText(Utility.numberFormat(item.getProdMrp()));
            myViewHolder.textMrp.setPaintFlags(myViewHolder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

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
            myViewHolder.textQty.setText("Qty: "+item.getQty());

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


        }else if(holder instanceof MyHomeHeader2ViewHolder){

            MyHeader item = (MyHeader) itemList.get(position);
            MyHomeHeader2ViewHolder myViewHolder = (MyHomeHeader2ViewHolder)holder;
            myViewHolder.textHeader.setText(item.getTitle());


        }else if(holder instanceof MyFrequencyOrderProductListViewHolder){

            FrequencyProduct item = (FrequencyProduct) itemList.get(position);
            final MyFrequencyOrderProductListViewHolder myViewHolder = (MyFrequencyOrderProductListViewHolder)holder;

            myViewHolder.textName.setText(item.getProdName());
            myViewHolder.textSp.setText(Utility.numberFormat(item.getProdSp()));
            myViewHolder.textMrp.setText(Utility.numberFormat(item.getProdMrp()));
            myViewHolder.textMrp.setPaintFlags(myViewHolder.textMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            myViewHolder.textQty.setText("Qty: "+item.getFrequencyQty());
            myViewHolder.textStartDate.setText("Start Date: "+Utility.parseDate(item.getFrequencyStartDate(),
                    "yyyy-MM-dd","dd MMM yyyy"));
            myViewHolder.textEndDate.setText("End Date: "+Utility.parseDate(item.getFrequencyEndDate(),
                    "yyyy-MM-dd","dd MMM yyyy"));
            myViewHolder.textNextOrderDate.setText("Next Order Date: "+Utility.parseDate(item.getNextOrderDate(),
                    "yyyy-MM-dd","dd MMM yyyy"));

            if(item.getStatus().equals("1")){
                myViewHolder.textStatus.setText("Active");
                myViewHolder.textStatus.setTextColor(context.getResources().getColor(R.color.green600));
                myViewHolder.textNextOrderDate.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.textStatus.setText("Inactive");
                myViewHolder.textStatus.setTextColor(context.getResources().getColor(R.color.red_500));
                myViewHolder.textNextOrderDate.setVisibility(View.GONE);
            }

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
