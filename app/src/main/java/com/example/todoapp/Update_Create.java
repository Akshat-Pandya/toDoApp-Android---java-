package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Update_Create extends AppCompatActivity {

    private EditText editTitle,editDescription;
    private Button btnDeleteTask,btnUpdateTask;
    private String key;
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private String date,time,title,description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_create);
        getSupportActionBar().hide();
        initializeView();
        db=FirebaseDatabase.getInstance();
        ref=db.getReference("Tasks").child(FirebaseUtility.getUserId());

        updateUI();
        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 date=getSelectedDate();
                 time=getSelectedTime();
                 title=editTitle.getText().toString();
                 description=editDescription.getText().toString();
                 if(!title.isEmpty()&&!description.isEmpty())
                 {
                     if(CurrentTask.getTaskid()==null)
                     key=ref.push().getKey();
                     else
                         key=CurrentTask.getTaskid();
                     TaskTemplate taskTemplate=new TaskTemplate(title,description,date,time);
                     taskTemplate.setKey(key);
                     ref.child(key).setValue(taskTemplate).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(Update_Create.this, "Task Updated Successfully", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                                else{
                                    Toast.makeText(Update_Create.this, "Could not update task", Toast.LENGTH_SHORT).show();
                                }
                         }
                     });

                 }
                 else{
                     Toast.makeText(Update_Create.this, "Title and Description of task cannot be vacant", Toast.LENGTH_SHORT).show();
                 }
            }
        });
        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CurrentTask.getTaskid()==null)
                {
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(Update_Create.this);
                    dialog.setCancelable(false);
                    dialog.setTitle("Want to Delete Task ?");
                    dialog.setIcon(R.drawable.baseline_delete_24);
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteTask();
                        }
                    });
                    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Task not deleted
                        }
                    });
                    dialog.show();

                }
            }
        });
    }

    private void deleteTask() {
        ref=db.getReference("Tasks").child(FirebaseUtility.getUserId());
        ref.child(CurrentTask.getTaskid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Update_Create.this, "Task deleted successfully - ", Toast.LENGTH_SHORT).show();
                        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(Update_Create.this, "Please try later - ", Toast.LENGTH_SHORT).show();
                    }
            }
        });


    }

    private void updateUI() {
        if(CurrentTask.getDate()!=null&&CurrentTask.getDescription()!=null&&CurrentTask.getTaskid()!=null&&CurrentTask.getTime()!=null&& CurrentTask.getTitle()!=null )
        {
            editTitle.setText(CurrentTask.getTitle());
            editDescription.setText(CurrentTask.getDescription());
            String dateString = CurrentTask.getDate(); // Replace with your actual date string
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd-M-yyyy", Locale.getDefault());
            try {
                Date date = dateFormat.parse(dateString);

                Calendar calendar = Calendar.getInstance();
                if (date != null) {
                    calendar.setTime(date);

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    // Set the DatePicker components
                    datePicker.init(year, month, day, null);
                }
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            String timeString = CurrentTask.getTime(); // Replace with your actual time string
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

            try {
                Date date = timeFormat.parse(timeString);

                Calendar calendar = Calendar.getInstance();
                if (date != null) {
                    calendar.setTime(date);

                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    int amPm = calendar.get(Calendar.AM_PM);

                    // Set AM/PM
                    timePicker.setIs24HourView(false); // Enable 12-hour format
                    timePicker.setHour(hour);
                    timePicker.setMinute(minute);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }
    }

    private String getSelectedTime() {
        // Get the selected hour and minute
        int hour = timePicker.getCurrentHour();  // For API 23 and below, use getHour()
        int minute = timePicker.getCurrentMinute();  // For API 23 and below, use getMinute()

// Format the hour in 12-hour format
        int formattedHour = hour % 12;
        if (formattedHour == 0) {
            formattedHour = 12; // 0 should be displayed as 12 in 12-hour format
        }

// Determine whether it's AM or PM
        String amPm;
        if (hour < 12) {
            amPm = "AM";
        } else {
            amPm = "PM";
        }
        return formattedHour+":"+minute+" "+amPm;
    }

    private String getSelectedDate() {
        int day=datePicker.getDayOfMonth();
        int month=datePicker.getMonth()+1;
        int year=datePicker.getYear();
        // Create a Calendar instance and set the selected date
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int day_of_week=calendar.get(Calendar.DAY_OF_WEEK);
        String dow="";
        switch(day_of_week)
        {
            case 1:
                dow="Sun";
                break;
            case 2:
                dow="Mon";
                break;
            case 3:
                dow="Tue";
                break;
            case 4:
                dow="Wed";
                break;
            case 5:
                dow="Thur";
                break;
            case 6:
                dow="Fri";
                break;
            case 7:
                dow="Sat";
                break;

        }
        String s=dow+", "+day+"-"+month+"-"+year;
        return s;
    }

    private void initializeView() {
        datePicker=findViewById(R.id.datePicker);
        timePicker=findViewById(R.id.timePicker);
        editTitle=findViewById(R.id.editTextTitle);
        editDescription=findViewById(R.id.editTextDescription);
        btnUpdateTask=findViewById(R.id.btnUpdate);
        btnDeleteTask=findViewById(R.id.btnDelete);

    }


}