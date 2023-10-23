package com.example.todoapptwo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapptwo.MainActivity;
import com.example.todoapptwo.Model.todo_model;
import com.example.todoapptwo.R;
import com.example.todoapptwo.addNewTask;
import com.example.todoapptwo.utils.database;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<todo_model> mList;
    private MainActivity activity;
    private database myDB;

    public ToDoAdapter(database myDB, MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final todo_model item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    myDB.updateStatus(item.getId(), 1);
                }else
                    myDB.updateStatus(item.getId(), 0);
            }
        });

    }

    public boolean toBoolean(int num){
        return num!=0;
    }

    public Context getContext(){
        return activity;
    }

    public void setTasks(List<todo_model> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        todo_model item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editTask(int position){
        todo_model item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        addNewTask task = new addNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
