package com.example.todoapp;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseUtility {
    private static String taskid;
    public static String getUserId(){
         FirebaseAuth auth=FirebaseAuth.getInstance();
         String s=auth.getCurrentUser().getUid();
        return s;
    }

    public static void setTaskid(String taskid) {
        FirebaseUtility.taskid = taskid;
    }
}

