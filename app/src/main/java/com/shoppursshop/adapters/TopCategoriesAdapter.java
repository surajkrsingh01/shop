package com.shoppursshop.adapters;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.activities.ShoppursProductListActivity;
import com.shoppursshop.models.Category;
import com.shoppursshop.models.SubCategory;
import com.shoppursshop.utilities.DialogAndToast;

import java.util.List;

/**
 * Created by suraj kumar singh on 18-04-2019.
 */

public class TopCategoriesAdapter extends RecyclerView.Adapter<TopCategoriesAdapter.MyViewHolder> {
    private Context context;
    private List<SubCategory> categoryList;
    private int selectedIndex;

    public TopCategoriesAdapter(Context context, List<SubCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_categories, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
        {
            SubCategory item = categoryList.get(position);
            myViewHolder.tvCategoryName.setText(item.getName());
            Log.d("TopCategoiesAdapter", item.isSelected()+"");
            if(item.isSelected()){
                selectedIndex = position;
                myViewHolder.relative_category.setBackground(ContextCompat.getDrawable(context,R.drawable.accent_solid_small_round_corner_background));
                myViewHolder.tvCategoryName.setTextColor(context.getResources().getColor(R.color.white));
               // myViewHolder.iv_options.setVisibility(View.VISIBLE);

            }else{
                myViewHolder.relative_category.setBackground(ContextCompat.getDrawable(context,R.drawable.white_solid_round_corner_background));
                myViewHolder.tvCategoryName.setTextColor(context.getResources().getColor(R.color.primary_text_color));
                //myViewHolder.iv_options.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (categoryList != null) {
            return categoryList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         private TextView tvCategoryName;
         private RelativeLayout relative_category;


        public MyViewHolder(View itemView) {
            super(itemView);
            relative_category = itemView.findViewById(R.id.relative_category);
            tvCategoryName = itemView.findViewById(R.id.tv_Category);
            relative_category.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == relative_category){
                if(selectedIndex == getAdapterPosition()){

                }else {
                    categoryList.get(selectedIndex).setSelected(false);
                    SubCategory item = categoryList.get(getAdapterPosition());
                    item.setSelected(true);
                    selectedIndex = getAdapterPosition();
                   // DialogAndToast.showToast(item.getName(), context);
                    ((ShoppursProductListActivity) (context)).getProducts(String.valueOf(item.getId()));
                    notifyDataSetChanged();
                }
            }
        }
    }
}
