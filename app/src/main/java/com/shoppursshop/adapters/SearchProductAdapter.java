package com.shoppursshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.shoppursshop.R;
import com.shoppursshop.activities.AddProductActivity;
import com.shoppursshop.activities.ProductDetailActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.models.MyProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 18-04-2019.
 */

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyViewHolder> {
    private List<MyProductItem> myProductsList;
    private Context context;
    private DbHelper dbHelper;
    private String flag;

    private MyItemClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public SearchProductAdapter(Context context, List<MyProductItem> myProducts) {
        super();
        this.context = context;
        this.myProductsList = myProducts;
        dbHelper = new DbHelper(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_list_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        {
            final MyProductItem item = (MyProductItem) myProductsList.get(position);
            myViewHolder.textBarCode.setText(item.getProdBarCode());
            myViewHolder.textName.setText(item.getProdName());
            //myViewHolder.textAmount.setText("Rs. "+String.format("%.02f",item.getMrp()));
            myViewHolder.textMrp.setText(""+item.getProdSp());

            if(flag.equals("searchCartProduct")){
                if(item.getIsBarCodeAvailable().equals("Y")){
                    if(item.getBarcodeList() != null && item.getBarcodeList().size() > 0){
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }else{
                        myViewHolder.textStatus.setText("Out of stock");
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }
                }else{
                    if(item.getProdQoh() > 0){
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }else{
                        myViewHolder.textStatus.setText("Out of stock");
                        myViewHolder.textStatus.setVisibility(View.GONE);
                    }
                }
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
        }
    }

    @Override
    public int getItemCount() {
        if (myProductsList != null) {
            return myProductsList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textBarCode,textName,textMrp,textStatus;
        private ImageView imageView,imageMenu;
        private Button btnAddToCart;
        private View rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textBarCode=itemView.findViewById(R.id.text_bar_code);
            textName=itemView.findViewById(R.id.text_name);
            textMrp=itemView.findViewById(R.id.text_mrp);
            textStatus=itemView.findViewById(R.id.text_status);
            btnAddToCart=itemView.findViewById(R.id.btn_add_to_cart);
            imageView=itemView.findViewById(R.id.image_view);
            imageMenu=itemView.findViewById(R.id.image_menu);
            imageMenu.setOnClickListener(this);

            if(flag.equals("order")){
                btnAddToCart.setVisibility(View.VISIBLE);
            }else{
                btnAddToCart.setVisibility(View.GONE);
            }
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == rootView){
                if(flag.equals("searchCartProduct")){
                    myItemClickListener.onItemClicked(getAdapterPosition());
                }else{
                    MyProductItem item = (MyProductItem) myProductsList.get(getAdapterPosition());
                    Intent intent = new Intent(context,ProductDetailActivity.class);
                    intent.putExtra("id",item.getProdId());
                    intent.putExtra("subCatName","");
                    intent.putExtra("flag","search");
                    context.startActivity(intent);
                }

            }else if(v == imageMenu){
                final MyProductItem myProductItem = (MyProductItem) myProductsList.get(getAdapterPosition());

                PopupMenu popupMenu = new PopupMenu(v.getContext(), imageMenu);
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
                            myProductsList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }

                        return false;
                    }
                });
            }
        }
    }
}
