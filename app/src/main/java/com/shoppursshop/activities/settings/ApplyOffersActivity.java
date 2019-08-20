package com.shoppursshop.activities.settings;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.BaseActivity;
import com.shoppursshop.adapters.ApplyOfferAdapter;
import com.shoppursshop.adapters.CouponOfferAdapter;
import com.shoppursshop.adapters.OfferDescAdapter;
import com.shoppursshop.interfaces.MyItemClickListener;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyProductItem;
import com.shoppursshop.models.ProductComboDetails;
import com.shoppursshop.models.ProductComboOffer;
import com.shoppursshop.models.ProductDiscountOffer;
import com.shoppursshop.utilities.ConnectionDetector;
import com.shoppursshop.utilities.DialogAndToast;
import com.shoppursshop.utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class ApplyOffersActivity extends BaseActivity implements MyItemTypeClickListener {

    private List<Object> itemList;
    private RecyclerView recyclerView;
    private ApplyOfferAdapter myItemAdapter;
    private TextView textViewError;

    private RelativeLayout rlOfferDesc;
    private String flag;
    private int position;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        rlOfferDesc = findViewById(R.id.rl_offer_desc);
        itemList = new ArrayList<>();
        List<ProductComboOffer> comboOffer = dbHelper.getProductComboOffer();
        Log.i(TAG,"combo offer  "+comboOffer.size());
        List<ProductComboOffer> priceOffer = dbHelper.getProductPriceOffer();
        List<Object> freeOffer = dbHelper.getProductFreeOffer();
        MyProductItem myProductItem = null;
        ProductDiscountOffer productDiscountOffer = null;

        for(ProductComboOffer productComboOffer : priceOffer){
            myProductItem = dbHelper.getProductDetails(productComboOffer.getProdId());
            myProductItem.setProductOffer(productComboOffer);
            myProductItem.setOfferId(""+productComboOffer.getId());
            myProductItem.setOfferType("price");
            itemList.add(myProductItem);
        }

        for(Object ob : freeOffer){
            productDiscountOffer = (ProductDiscountOffer)ob;
            myProductItem = dbHelper.getProductDetails(productDiscountOffer.getProdBuyId());
            myProductItem.setProductOffer(productDiscountOffer);
            myProductItem.setOfferId(""+productDiscountOffer.getId());
            myProductItem.setOfferType("free");
            itemList.add(myProductItem);
        }

        String comboName = "";
        String comboIds = "";
        float totAmt = 0f;
        float totMrp = 0f;
        float totSgst =0f;
        float totCgst = 0f;
        MyProductItem comboProductItem = null;
        int comboId = 1;
        for(ProductComboOffer productComboOffer : comboOffer){
            myProductItem = new MyProductItem();
            for(ProductComboDetails productComboDetails : productComboOffer.getProductComboOfferDetails()){
                comboProductItem = dbHelper.getProductDetails(productComboDetails.getPcodProdId());
                totAmt = totAmt + (productComboDetails.getPcodPrice() * productComboDetails.getPcodProdQty());
                totMrp = totMrp + (comboProductItem.getProdMrp() * productComboDetails.getPcodProdQty());
                totCgst = totCgst + (productComboDetails.getPcodPrice() *
                        comboProductItem.getProdCgst() * productComboDetails.getPcodProdQty()/100);
                totSgst = totSgst + (productComboDetails.getPcodPrice() *
                        comboProductItem.getProdSgst() * productComboDetails.getPcodProdQty()/100);
                if(TextUtils.isEmpty(comboName)){
                    comboName = comboProductItem.getProdName();
                    comboIds = ""+comboProductItem.getProdId();
                }else{
                    comboName = comboName +"+"+comboProductItem.getProdName();
                    comboIds = comboIds+"-"+comboProductItem.getProdId();
                }

            }
            Log.i(TAG,"cgst "+totCgst);
            Log.i(TAG,"sgst "+totSgst);
            Log.i(TAG,"totAmt "+totAmt);
            Log.i(TAG,"totMrp "+totMrp);
            myProductItem.setProdId(-comboId);
            myProductItem.setProdName(comboName);
            myProductItem.setComboProductIds(comboIds);
            myProductItem.setProdSp(totAmt);
           // myProductItem.setTotalAmount(totAmt);
            myProductItem.setProdMrp(totMrp);
            myProductItem.setProdSgst(totSgst);
            myProductItem.setProdCgst(totCgst);
            myProductItem.setIsBarCodeAvailable("N");
            itemList.add(myProductItem);
            comboId++;
        }


        List<MyProductItem> cartList =  dbHelper.getCartProducts();
        MyProductItem myProductItem1 = null;
        for(MyProductItem cartProduct : cartList){
            if(cartProduct.getProdSp() != 0f){
                counter++;
                //setOffer(ob);
                for(Object ob1 : itemList){
                    myProductItem1 = (MyProductItem)ob1;
                    if(cartProduct.getProdId() == myProductItem1.getProdId()){
                        myProductItem1.setQty(cartProduct.getQty());
                        myProductItem1.setTotalAmount(cartProduct.getTotalAmount());
                        myProductItem1.setFreeProductPosition(cartProduct.getFreeProductPosition());
                        myProductItem1.setOfferCounter(cartProduct.getOfferCounter());
                    }
                }
            }
        }

        textViewError = findViewById(R.id.text_error);
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ApplyOfferAdapter(this,itemList);
        myItemAdapter.setMyItemTypeClickListener(this);
        myItemAdapter.setColorTheme(colorTheme);
        recyclerView.setAdapter(myItemAdapter);

    }

    @Override
    public void onItemClicked(int position,int type) {
        this.position = position;
        MyProductItem item = (MyProductItem)itemList.get(position);
        String offerName = "";
        if(type == 3){
            List<String> offerDescList = new ArrayList<>();
            Object ob = item.getProductOffer();
            if(ob instanceof ProductDiscountOffer){

                ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)ob;
                offerName = productDiscountOffer.getOfferName();

                offerDescList.add("Buy "+productDiscountOffer.getProdBuyQty()+" Get "+productDiscountOffer.getProdFreeQty());
                offerDescList.add("Offer valid till "+Utility.parseDate(productDiscountOffer.getEndDate(),"yyyy-MM-dd",
                        "EEE dd MMMM, yyyy")+" 23:59 PM");

            }else if(ob instanceof ProductComboOffer){
                ProductComboOffer productComboOffer = (ProductComboOffer)ob;
                offerName = productComboOffer.getOfferName();
                float totOfferAmt = 0f;
                for(ProductComboDetails productComboDetails : productComboOffer.getProductComboOfferDetails()){
                    totOfferAmt =  productComboDetails.getPcodPrice();
                    offerDescList.add("Buy "+productComboDetails.getPcodProdQty()+" at Rs "+
                            Utility.numberFormat(totOfferAmt));
                }

                offerDescList.add("Offer valid till "+Utility.parseDate(productComboOffer.getEndDate(),"yyyy-MM-dd",
                        "EEE dd MMMM, yyyy")+" 23:59 PM");
            }

            rlOfferDesc.setVisibility(View.VISIBLE);
            ImageView iv_clear = findViewById(R.id.iv_clear);
            //Utility.setColorFilter(iv_clear.getDrawable(),colorTheme);
            TextView tvOfferName = findViewById(R.id.text_offer_name);
            findViewById(R.id.relative_footer_action).setBackgroundColor(colorTheme);
            TextView tv = findViewById(R.id.text_action);
            tv.setText("OKAY! GOT IT");

            tvOfferName.setText(offerName);

            iv_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlOfferDesc.setVisibility(View.GONE);
                }
            });
            findViewById(R.id.relative_footer_action).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlOfferDesc.setVisibility(View.GONE);
                }
            });


            RecyclerView recyclerViewOfferDesc=findViewById(R.id.recycler_view_offer_desc);
            recyclerViewOfferDesc.setHasFixedSize(true);
            final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
            recyclerViewOfferDesc.setLayoutManager(layoutManager);
            recyclerViewOfferDesc.setItemAnimator(new DefaultItemAnimator());
            OfferDescAdapter offerDescAdapter =new OfferDescAdapter(this,offerDescList);
            recyclerViewOfferDesc.setAdapter(offerDescAdapter);
            recyclerViewOfferDesc.setNestedScrollingEnabled(false);
        }else if(type == 1){
            if(item.getQty() > 0){
                if(item.getQty() == 1){
                    counter--;
                    dbHelper.removeProductFromCart(item.getProdBarCode());
                    dbHelper.removeProductFromCart(item.getProdId());
                    if(item.getProductOffer() instanceof ProductDiscountOffer){
                        ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)item.getProductOffer();

                        if(productDiscountOffer.getProdBuyId() != productDiscountOffer.getProdFreeId())
                        dbHelper.removeFreeProductFromCart(productDiscountOffer.getProdFreeId());
                    }
                    item.setQty(0);
                    myItemAdapter.notifyItemChanged(position);
                }else{
                    int qty = item.getQty() - 1;
                    float netSellingPrice = getOfferAmount(item,type);
                    // item.setQty(qty);
                    qty = item.getQty();
                    Log.i(TAG,"netSellingPrice "+netSellingPrice);
                    float amount = item.getTotalAmount() - netSellingPrice;
                    Log.i(TAG,"tot amount "+amount);
                    item.setTotalAmount(amount);
                    dbHelper.updateCartData(item,qty,amount);
                    myItemAdapter.notifyItemChanged(position);
                }
            }

        }else if(type == 2){
            if((item.getComboProductIds() == null || item.getComboProductIds().equals("null"))
                    && item.getQty() == item.getProdQoh()){
                DialogAndToast.showDialog("There are no more stocks",this);
            }else{
                int qty = item.getQty() + 1;
                item.setQty(qty);
                if(qty == 1){
                    counter++;
                    item.setFreeProductPosition(counter);
                    dbHelper.addProductToCart(item);
                }
                float netSellingPrice = getOfferAmount(item,type);
                Log.i(TAG,"netSellingPrice "+netSellingPrice);
                float amount = item.getTotalAmount() + netSellingPrice;
                Log.i(TAG,"tot amount "+amount);
                item.setTotalAmount(amount);
                qty = item.getQty();
                Log.i(TAG,"qty "+qty);

                dbHelper.updateCartData(item,qty,amount);
                myItemAdapter.notifyItemChanged(position);
            }
        }
    }


    private float getOfferAmount(MyProductItem item,int type){
        float amount = 0f;
        int qty = item.getQty();
        if(item.getProductOffer() != null && item.getProductOffer() instanceof ProductComboOffer){
            ProductComboOffer productComboOffer = (ProductComboOffer)item.getProductOffer();
            if(qty >= 1){
                int maxSize = productComboOffer.getProductComboOfferDetails().size();
                int mod = qty % maxSize;
                Log.i(TAG,"mod "+mod);
                if(mod == 0){
                    mod = maxSize;
                }
                amount = getOfferPrice(mod,item.getProdSp(),productComboOffer.getProductComboOfferDetails());
            }else{
                amount = item.getProdSp();
            }

               if(type == 1)
                item.setQty(item.getQty() - 1);

        }else if(item.getProductOffer() != null && item.getProductOffer() instanceof ProductDiscountOffer){

            ProductDiscountOffer productDiscountOffer = (ProductDiscountOffer)item.getProductOffer();
            amount = item.getProdSp();
            if(type == 1){
                if(productDiscountOffer.getProdBuyId() == productDiscountOffer.getProdFreeId()){
                    Log.i(TAG,"item qty "+item.getQty()+" offer buy qty"+productDiscountOffer.getProdBuyQty());
                    Log.i(TAG,"minus mode "+(item.getQty() - item.getOfferCounter()-1)% productDiscountOffer.getProdBuyQty());
                    if((item.getQty() - item.getOfferCounter() -1)% productDiscountOffer.getProdBuyQty() ==
                            (productDiscountOffer.getProdBuyQty()-1)){
                        item.setQty(item.getQty() - 2);
                        item.setOfferCounter(item.getOfferCounter() - 1);
                        dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());

                    }else{
                        item.setQty(item.getQty() - 1);
                    }
                }else{
                    item.setQty(item.getQty() - 1);
                    Log.i(TAG,"minus mode "+item.getQty() % productDiscountOffer.getProdBuyQty());
                    if(item.getQty() % productDiscountOffer.getProdBuyQty() == (productDiscountOffer.getProdBuyQty()-1)){
                        item.setOfferCounter(item.getOfferCounter() - 1);
                        if(item.getOfferCounter() == 0){
                            dbHelper.removeFreeProductFromCart(productDiscountOffer.getProdFreeId());
                        }else{
                            dbHelper.updateFreeCartData(productDiscountOffer.getProdFreeId(),item.getOfferCounter(),0f);
                            dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
                        }
                    }

                }
            }else if(type == 2){
                if(productDiscountOffer.getProdBuyId() == productDiscountOffer.getProdFreeId()){
                    Log.i(TAG,"Same product");
                    Log.i(TAG,"item qty "+item.getQty()+" offer buy qty"+productDiscountOffer.getProdBuyQty());
                    Log.i(TAG,"plus mode "+(item.getQty() - item.getOfferCounter())% productDiscountOffer.getProdBuyQty());
                    if((item.getQty() - item.getOfferCounter())% productDiscountOffer.getProdBuyQty() == 0){
                        item.setQty(item.getQty() + 1);
                        item.setOfferCounter(item.getOfferCounter() + 1);
                        dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
                    }else{

                    }
                }else{
                    Log.i(TAG,"Different product");
                    if(item.getQty() % productDiscountOffer.getProdBuyQty() == 0){
                        item.setOfferCounter(item.getOfferCounter() + 1);
                        MyProductItem item1 = null;
                        if(item.getOfferCounter() == 1){
                            item1 = dbHelper.getProductDetails(productDiscountOffer.getProdFreeId());
                            item1.setProdSp(0f);
                            item1.setQty(1);
                            item1.setFreeProductPosition(item.getFreeProductPosition());
                            dbHelper.addProductToCart(item1);
                            dbHelper.updateFreePositionCartData(item.getFreeProductPosition(),item.getProdId());
                            dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
                            Log.i(TAG,"Different product added to cart");
                        }else{
                          //  item1 = itemList.get(item.getFreeProductPosition());
                         //   item1.setQty(item.getOfferCounter());
                            dbHelper.updateFreeCartData(productDiscountOffer.getProdFreeId(),item.getOfferCounter(),0f);
                            dbHelper.updateOfferCounterCartData(item.getOfferCounter(),item.getProdId());
                            Log.i(TAG,"Different product updated in cart");
                        }
                        //  myItemAdapter.notifyDataSetChanged();
                    }

                }

            }else{
                amount = item.getProdSp();
            }

        }else{
            if(type == 1)
                item.setQty(item.getQty() - 1);
            amount = item.getProdSp();
        }

        return amount;
    }

    private float getOfferPrice(int qty,float sp,List<ProductComboDetails> productComboDetailsList){
        float amount = 0f;
        int i = -1;
        for(ProductComboDetails productComboDetails:productComboDetailsList){
            if(productComboDetails.getPcodProdQty() == qty){
                amount = productComboDetails.getPcodPrice();
                if(qty != 1){
                    amount = amount - productComboDetailsList.get(i).getPcodPrice();
                }
                Log.i(TAG,"offer price "+amount);
                break;
            }else{
                amount = sp;
            }
            i++;
        }
        Log.i(TAG,"final selling price "+amount);
        return amount;
    }

    private void setOffer(MyProductItem item){
        List<ProductComboOffer> comboOfferList = dbHelper.getProductPriceOffer(""+item.getProdId());
        List<ProductDiscountOffer> productDiscountOfferList = dbHelper.getProductFreeOffer(""+item.getProdId());
        Log.i(TAG,"comboOfferList size "+comboOfferList.size());
        Log.i(TAG,"productDiscountOfferList size "+productDiscountOfferList.size());

        if(comboOfferList.size() > 0){
            item.setProductOffer(comboOfferList.get(0));
        }else if(productDiscountOfferList.size() > 0){
            item.setProductOffer(productDiscountOfferList.get(0));
        }
    }

}
