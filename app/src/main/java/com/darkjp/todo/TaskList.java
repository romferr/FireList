package com.darkjp.todo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskList implements Parcelable {
    private String id;
    private String title;
    private String creator;
    private HashMap<String, Boolean> participant;
    private ArrayList<Task> task;


    public TaskList() {
    }

    public TaskList(String id, String title) {
        this.id = id;
        this.title = title;
        this.creator = "";
        this.participant = new HashMap<>();
        this.task = new ArrayList<>();
    }

    protected TaskList(Parcel in) {
        id = in.readString();
        title = in.readString();
        creator = in.readString();
    }

    public static final Creator<TaskList> CREATOR = new Creator<TaskList>() {
        @Override
        public TaskList createFromParcel(Parcel in) {
            return new TaskList(in);
        }

        @Override
        public TaskList[] newArray(int size) {
            return new TaskList[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public HashMap<String, Boolean> getParticipant() {
        return participant;
    }

    public void setParticipant(HashMap<String, Boolean> participant) {
        this.participant = participant;
    }

    public ArrayList<Task> getTask() {
        return task;
    }

    public void setTask(ArrayList<Task> task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", author='" + creator + '\'' +
                ", tasks=" + task +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(creator);
    }
}
