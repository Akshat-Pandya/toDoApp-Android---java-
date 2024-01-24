package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerAdapter adapter;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private ArrayList<TaskTemplate> dataList=new ArrayList<>();
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=findViewById(R.id.fabAddTask);
        recycler=findViewById(R.id.recyclerViewTasks);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("Tasks");

        getSupportActionBar().hide();
        adapter=new RecyclerAdapter(dataList,this);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        ref.child(FirebaseUtility.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for(DataSnapshot temp:snapshot.getChildren())
                {
                    TaskTemplate taskTemplate=temp.getValue(TaskTemplate.class);
                    dataList.add(taskTemplate);
                }
                adapter.setData(dataList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentTask.setTaskid(null);
                CurrentTask.setDate(null);
                CurrentTask.setTime(null);
                CurrentTask.setDescription(null);
                CurrentTask.setTitle(null);
                Intent intent=new Intent(getApplicationContext(),Update_Create.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure you want to exit ? ");
        dialog.setIcon(R.drawable.baseline_exit_to_app_24);
        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dialog dismissed
            }
        });
        dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                FirebaseAuth.getInstance().signOut();
                MainActivity.super.onBackPressed();
            }
        });
        dialog.show();
    }
}