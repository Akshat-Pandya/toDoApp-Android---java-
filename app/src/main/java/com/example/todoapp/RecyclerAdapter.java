package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

   private ArrayList<TaskTemplate> dataList;
    private Context context;

    public RecyclerAdapter(ArrayList<TaskTemplate> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_task_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.time.setText(dataList.get(position).getTime());
        holder.date.setText(dataList.get(position).getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentTask.setTaskid(dataList.get(holder.getAdapterPosition()).getKey());
                CurrentTask.setTitle(dataList.get(holder.getAdapterPosition()).getTitle());
                CurrentTask.setDescription(dataList.get(holder.getAdapterPosition()).getDescription());
                CurrentTask.setTime(dataList.get(holder.getAdapterPosition()).getTime());
                CurrentTask.setDate(dataList.get(holder.getAdapterPosition()).getDate());
                Intent intent=new Intent(context,Update_Create.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataList!=null)
        return dataList.size();
        else
            return 0;
    }

    public void setData(ArrayList<TaskTemplate> dataList) {
        this.dataList=dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,date,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.textViewTitle);
            date=itemView.findViewById(R.id.textViewDate);
            time=itemView.findViewById(R.id.textViewTime);

        }
    }
}
