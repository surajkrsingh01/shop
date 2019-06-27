package com.shoppursshop.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shoppursshop.R;
import com.shoppursshop.interfaces.MyItemClickListener;

public class CancelDialogFragment extends BottomSheetDialogFragment {

    private MyItemClickListener myItemClickListener;
    private String totalAmount;

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public static CancelDialogFragment getInstance() {
        return new CancelDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.cancel_dialog_fragment, container, false);
        Button btnCancelOrder = view.findViewById(R.id.btn_cancel_order);
        Button btnBack = view.findViewById(R.id.btn_back);
        EditText editTextReason = view.findViewById(R.id.edit_reason);
        TextView textViewAmount = view.findViewById(R.id.text_total_amount);

        textViewAmount.setText(totalAmount);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myItemClickListener.onItemClicked(2);
            }
        });

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              myItemClickListener.onItemClicked(3);
            }
        });

        return view;
    }


}
