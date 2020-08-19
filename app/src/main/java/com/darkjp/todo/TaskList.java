package com.darkjp.todo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TaskList implements Parcelable {
    private String id;
    private String title;
    private String creator;
    private ArrayList<User> participants;
    private ArrayList<Task> tasksList;


    public TaskList() {
    }

    public TaskList(String id, String title) {
        this.id = id;
        this.title = title;
        this.creator = "";
        this.participants = new ArrayList<>();
        this.tasksList = new ArrayList<>();
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

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public ArrayList<Task> getTasksList() {
        return tasksList;
    }

    public void setTasksList(ArrayList<Task> tasksList) {
        this.tasksList = tasksList;
    }

    @Override
    public String toString() {
        return "TaskList{" +
                "id='" + id +'\'' +
                "title='" + title + '\'' +
                ", author='" + creator + '\'' +
                ", tasks=" + tasksList +
                ", participants=" + participants +
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
