package com.shoppursshop.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shoppursshop.activities.ProductDetailActivity;
import com.shoppursshop.activities.ShoppursProductListActivity;
import com.shoppursshop.database.DbHelper;
import com.shoppursshop.fragments.DescBottomFragment;
import com.shoppursshop.interfaces.MyImageClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyProduct;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.models.ProductUnit;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suraj kumar singh on 20-04-2019.
 */

public class ShopProductListAdapter extends RecyclerView.Adapter<ShopProductListAdapter.MyViewHolder> {
    private List<MyProductItem> myProductsList = new ArrayList<>();
    private Context context;
    private boolean isDarkTheme;

    private MyItemTypeClickListener myItemTypeClickListener;
    private MyImageClickListener myImageClickListener;

    public void setMyImageClickListener(MyImageClickListener myImageClickListener) {
        this.myImageClickListener = myImageClickListener;
    }

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }


    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public ShopProductListAdapter(Context context, List<MyProductItem> myProducts) {
        super();
        this.context = context;
        this.myProductsList = myProducts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_list_item_layout_1, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        {
            final MyProductItem item = (MyProductItem) myProductsList.get(position);

           /* if(dbHelper.checkProdExistInShopCart(item.getId())){
                myViewHolder.btnAddCart.setVisibility(View.GONE);
                myViewHolder.linear_plus_minus.setVisibility(View.VISIBLE);
                myViewHolder.tv_cartCount.setText(String.valueOf(dbHelper.getProductQuantity(item.getId())));
                item.setFreeProductPosition(dbHelper.getFreeProductPosition(item.getId()));
                item.setOfferCounter(dbHelper.getOfferCounter(item.getId()));
                item.setQty(Integer.parseInt(myViewHolder.tv_cartCount.getText().toString()));
                item.setTotalAmount(dbHelper.getTotalAmount(item.getId()));
                item.setProdSp(dbHelper.getProductSellingPrice(item.getId()));
            }else {
                item.setQty(0);
                item.setTotalAmount(0);
                myViewHolder.tv_cartCount.setText(String.valueOf(0));
                myViewHolder.linear_plus_minus.setVisibility(View.GONE);
                myViewHolder.btnAddCart.setVisibility(View.VISIBLE);
            }*/

            myViewHolder.textbarcode.setText(item.getProdBarCode());
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
        private TextView textName,textMrp, textSp, textOffPer, textStatus, textbarcode, tv_cartCount;
        private ImageView imageView, image_minus, image_plus;
        private View rootView;
        private Button btnAddCart;
        private LinearLayout linear_plus_minus;
        private Spinner spinnerUnit;
        private RelativeLayout relative_unit;
        private TextView text_offer;

        public MyViewHolder(View itemView) {
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
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),3);
            }else if(view == btnAddCart){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
            }else if(view == image_plus){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),2);
            }else if(view == image_minus){
                myItemTypeClickListener.onItemClicked(getAdapterPosition(),1);
            }else if(view == rootView){
                MyProductItem item = (MyProductItem) myProductsList.get(getAdapterPosition());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("flag","shoppurs_product");
                intent.putExtra("myProduct",item);
                context.startActivity(intent);
            }
        }
    }
}