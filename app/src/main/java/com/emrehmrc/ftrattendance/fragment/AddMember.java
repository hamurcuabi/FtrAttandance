package com.emrehmrc.ftrattendance.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.emrehmrc.ftrattendance.DialogListener;
import com.emrehmrc.ftrattendance.R;
import com.emrehmrc.ftrattendance.model.Member;

import java.util.Date;

public class AddMember extends DialogFragment implements View.OnClickListener {

    private static DialogListener dialogListener;
    private EditText edtName;
    private ImageButton btnAdd;


    public AddMember() {
    }

    public static AddMember newInstance(DialogListener listener) {

        Bundle args = new Bundle();
        AddMember fragment = new AddMember();
        dialogListener = listener;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_member, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        btnAdd.setOnClickListener(this);

    }

    private void init(View view) {
        edtName = view.findViewById(R.id.edtName);
        btnAdd = view.findViewById(R.id.btnAdd);

    }

    @Override
    public void onClick(View v) {

        if (isEmpty()) {
            //add to firebase database
            Date date = new Date();
            String name = edtName.getText().toString().trim();
            dialogListener.onClosed(name);
            dismiss();

        }
    }

    private boolean isEmpty() {
        boolean isValid;
        if (edtName.getText().toString().trim().isEmpty()) {
            edtName.setError("Boş Alan");
            isValid = false;
        }else isValid = true;
        return isValid;
    }
}
