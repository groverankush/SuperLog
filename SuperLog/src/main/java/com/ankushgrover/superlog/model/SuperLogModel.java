package com.ankushgrover.superlog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ankushgrover.superlog.SuperLog;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class SuperLogModel implements Parcelable {

    private String tag;
    private String message;
    private String timestamp;
    private int type;


    public SuperLogModel(){

    }

    public SuperLogModel(String tag, String message, int type) {
        this.tag = tag;
        this.message = message;
        this.type = type;
    }

    public SuperLogModel(String tag, String message, String timestamp, int type) {
        this.tag = tag;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    protected SuperLogModel(Parcel in) {
        tag = in.readString();
        message = in.readString();
        timestamp = in.readString();
        type = in.readInt();
    }

    public static final Creator<SuperLogModel> CREATOR = new Creator<SuperLogModel>() {
        @Override
        public SuperLogModel createFromParcel(Parcel in) {
            return new SuperLogModel(in);
        }

        @Override
        public SuperLogModel[] newArray(int size) {
            return new SuperLogModel[size];
        }
    };

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tag);
        parcel.writeString(message);
        parcel.writeString(timestamp);
        parcel.writeInt(type);
    }
}
