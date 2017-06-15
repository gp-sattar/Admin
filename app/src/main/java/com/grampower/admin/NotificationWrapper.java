package com.grampower.admin;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samdroid on 12/6/17.
 */

public class NotificationWrapper implements Parcelable{
    String key,notification;
    Boolean isSelected;



    public NotificationWrapper(String key, String notification, Boolean isSelected) {
        this.key = key;
        this.notification = notification;
        this.isSelected = isSelected;
    }


    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(key);
        dest.writeString(notification);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public NotificationWrapper(Parcel in) {
        key = in.readString();
        notification= in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<NotificationWrapper> CREATOR = new Parcelable.Creator<NotificationWrapper>() {
        public NotificationWrapper createFromParcel(Parcel in) {
            return new NotificationWrapper(in);
        }

        public NotificationWrapper[] newArray(int size) {
            return new NotificationWrapper[size];
        }
    };

}
