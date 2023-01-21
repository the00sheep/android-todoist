package com.bawp.todoister.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Task> taskList;

    private final OnTodoClickListener todoClickListener;

    public RecyclerViewAdapter(List<Task> taskList, OnTodoClickListener onTodoClickListener) {
        this.taskList = taskList;
        this.todoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false); //gets todo_row widgets
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        //date converter
        String formatted = Utils.fomratDate(task.getDueDate());

        //color code by priority
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[] {-android.R.attr.state_enabled},
                new int[] {android.R.attr.state_enabled}
        },
                new int[]{
                        Color.LTGRAY, //disabled
                        Utils.priorityColor(task)
                });


        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);

        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip todayChip;

        OnTodoClickListener onTodoClickListener;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //setting up widgets
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);

            //setting up onTodoClickListener (override bellow)
            this.onTodoClickListener = todoClickListener;

            //it is connected to our own click listener
            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //get current task based on adapter position
            Task currTask = taskList.get(getAdapterPosition());

            int id = view.getId();

            if (id == R.id.todo_row_layout) {

                onTodoClickListener.onTodoClick(currTask);
            }
            //mark for deletion
            else if(id == R.id.todo_radio_button){

                onTodoClickListener.onTodoRadioButtonClick(currTask);
            }

        }
    }
}
