package com.example.todoapptwo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoapptwo.Model.todo_model;
import com.example.todoapptwo.utils.database;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class addNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "addNewTask";

    private EditText mEditText;
    private Button mSaveButton;

    private database myDB;


    public static addNewTask newInatancw(){
        return new addNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.add_new_task , container , false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.edittext);
        mSaveButton = view.findViewById(R.id.button_save);

        myDB = new database(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if (task.length() > 0){
                mSaveButton.setEnabled(false);
            }
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")){
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                }else{
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text = mEditText.getText().toString();

                if (finalIsUpdate){
                    myDB.updateTask(bundle.getInt("id"), text);
                }else{
                    todo_model item = new todo_model();
                    item.setTask(text);
                    item.setStatus(0);
                    myDB.insertTask(item);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).OnDialogClose(dialog);
        }
    }
}
