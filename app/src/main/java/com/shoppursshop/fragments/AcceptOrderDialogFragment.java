package com.shoppursshop.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemClickListener;

public class AcceptOrderDialogFragment extends BottomSheetDialogFragment {

    private MyItemClickListener myItemClickListener;
    private String totalAmount,ordPayStatus,ordDeliveryMode;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setOrdPayStatus(String ordPayStatus) {
        this.ordPayStatus = ordPayStatus;
    }

    public void setOrdDeliveryMode(String ordDeliveryMode) {
        this.ordDeliveryMode = ordDeliveryMode;
    }

    public static AcceptOrderDialogFragment getInstance() {
        return new AcceptOrderDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.accept_order_dialog_fragment, container, false);
        Button btnAcceptOrder = view.findViewById(R.id.btn_accept_order);
        Button btnBack = view.findViewById(R.id.btn_back);
        EditText editDeliveryBy = view.findViewById(R.id.edit_delivery_by);
        TextView textViewAmount = view.findViewById(R.id.text_total_amount);
        TextView textViewPayStatus = view.findViewById(R.id.text_status);
        TextView textViewDeliveryMode = view.findViewById(R.id.text_delivery_mode);

        textViewAmount.setText(totalAmount);
        textViewPayStatus.setText(ordPayStatus);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myItemClickListener.onItemClicked(0);
            }
        });

        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myItemClickListener.onItemClicked(1);
            }
        });

        return view;
    }

}
