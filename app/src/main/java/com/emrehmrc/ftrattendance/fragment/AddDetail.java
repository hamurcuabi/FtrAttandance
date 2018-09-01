package com.emrehmrc.ftrattendance.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.emrehmrc.ftrattendance.DialogListener;
import com.emrehmrc.ftrattendance.R;
import com.emrehmrc.ftrattendance.model.Detail;

public class AddDetail extends DialogFragment implements View.OnClickListener {

    private static DialogListener dialogListener;
    private static String date;
    private CheckBox cbA;
    private CheckBox cbB;
    private CheckBox cbC;
    private TextView txtDate;
    private ImageButton btnAdd;


    public AddDetail() {
    }

    public static AddDetail newInstance(DialogListener listener, String datecome) {

        Bundle args = new Bundle();
        AddDetail fragment = new AddDetail();
        dialogListener = listener;
        date = datecome;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_detail, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        btnAdd.setOnClickListener(this);

    }

    private void init(View view) {
        btnAdd = view.findViewById(R.id.btnAddDetail);
        txtDate = view.findViewById(R.id.txtShowDate);
        cbA = view.findViewById(R.id.checkBoxA);
        cbB = view.findViewById(R.id.checkBoxB);
        cbC = view.findViewById(R.id.checkBoxC);
        txtDate.setText(date);
        setCb();

    }

    private void setCb() {
        cbA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbB.setChecked(false);
                cbC.setChecked(false);
                setNullCb();
            }
        });
        cbB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbA.setChecked(false);
                cbC.setChecked(false);
                setNullCb();
            }
        });
        cbC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbB.setChecked(false);
                cbA.setChecked(false);
                setNullCb();
            }
        });

    }

    private void setNullCb() {
        if (cbB.isChecked() == false && cbA.isChecked() == false && cbC.isChecked() == false) {
            cbA.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        send();
    }


    private void send() {
        int state = 0;
        if (cbA.isChecked()) state = 1;
        else if (cbB.isChecked()) state = 2;
        else if (cbC.isChecked()) state = 3;
        Detail detail = new Detail(state);
        dialogListener.onClosedDetail(detail);
        dismiss();
    }
}


