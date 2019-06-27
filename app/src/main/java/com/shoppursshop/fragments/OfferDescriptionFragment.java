package com.shoppursshop.fragments;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemTypeClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferDescriptionFragment extends BottomSheetDialogFragment {

    private String TAG = "BottomSearchFragment";

    private String flag;

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private MyItemTypeClickListener myItemTypeClickListener;

    private int colorTheme;

    public void setColorTheme(int colorTheme) {
        this.colorTheme = colorTheme;
    }

    public void setMyItemTypeClickListener(MyItemTypeClickListener myItemTypeClickListener) {
        this.myItemTypeClickListener = myItemTypeClickListener;
    }

    public OfferDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_offer_desciption, container, false);
        RelativeLayout relative_search = rootView.findViewById(R.id.relative_header);
        ((GradientDrawable)relative_search.getBackground()).setColor(colorTheme);
        ImageView iv_clear = rootView.findViewById(R.id.iv_clear);
        TextView tvOfferName = rootView.findViewById(R.id.text_offer_name);
        rootView.findViewById(R.id.relative_footer_action).setBackgroundColor(colorTheme);
        TextView tv = rootView.findViewById(R.id.text_action);
        tv.setText("OKAY! GOT IT");
        if(colorTheme == getResources().getColor(R.color.white)){
            tv.setTextColor(getResources().getColor(R.color.primary_text_color));
        }else{
            tv.setTextColor(getResources().getColor(R.color.white));
        }

        Bundle bundle = getArguments();
        String offerName = bundle.getString("offerName");

        tvOfferName.setText(offerName);

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferDescriptionFragment.this.dismiss();
            }
        });
        return rootView;
    }

}
