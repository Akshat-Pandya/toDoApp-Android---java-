package com.example.todoapp;

public class CurrentTask {
   private static String taskid,title,description,date,time;

    public static  String getTaskid() {
        return taskid;
    }

    public static void setTaskid(String taskid) {
        CurrentTask.taskid = taskid;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        CurrentTask.title = title;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        CurrentTask.description = description;
    }

    public static String getDate() {
        return date;
    }

    public static  void setDate(String date) {
        CurrentTask.date = date;
    }

    public  static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        CurrentTask.time = time;
    }
}
