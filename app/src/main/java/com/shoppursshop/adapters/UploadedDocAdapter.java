package com.shoppursshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemTypeClickListener;
import com.shoppursshop.models.MyKyc;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadedDocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MyKyc> itemList;
    private Context context;
    private int prePosition = -1;
    private MyItemTypeClickListener myItemClickListener;

    public void setMyItemClickListener(MyItemTypeClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public UploadedDocAdapter(Context context, List<MyKyc> itemList) {
        this.itemList = itemList;
        this.context=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textHeader,textDesc,tv_pdf;
        private CircleImageView image;
        private ImageView imageViewClose;

        public MyViewHolder(View itemView) {
            super(itemView);
            textHeader=itemView.findViewById(R.id.text_header);
            tv_pdf=itemView.findViewById(R.id.tv_pdf);
            textDesc=itemView.findViewById(R.id.text_desc);
            image=itemView.findViewById(R.id.image_pic);
            imageViewClose=itemView.findViewById(R.id.image_close);
            imageViewClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageViewClose){
                myItemClickListener.onItemClicked(getAdapterPosition(),2);
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
                View v10 = inflater.inflate(R.layout.document_item_layout, parent, false);
                viewHolder = new MyViewHolder(v10);
                break;
            default:
                View v = inflater.inflate(R.layout.document_item_layout, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyKyc item = (MyKyc) itemList.get(position);
            MyViewHolder myViewHolder = (MyViewHolder)holder;
            myViewHolder.textHeader.setText(item.getKycDocType());
            myViewHolder.tv_pdf.setVisibility(View.GONE);
            myViewHolder.image.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.signature(new ObjectKey(item.getSignature()));
            Glide.with(context)
                    .load(item.getKycDocPic())
                    .apply(requestOptions)
                    .into(myViewHolder.image);

        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
