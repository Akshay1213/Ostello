package com.xoxytech.ostello;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by akshay on 20/7/17.
 */

public class CitySuggetions implements SearchSuggestion {

    private String city;
    private boolean IsHistory = false;

    public CitySuggetions(String city) {//constructor
        this.city = city;
    }
    public CitySuggetions(Parcel source) {//constructor
        this.city = source.readString();
        this.IsHistory = source.readInt() != 0;
    }


    public String getBody() {
        return city;
    }

    public boolean isHistory() {//setter
        return IsHistory;
    }

    public void setHistory(boolean history) {//getter
        IsHistory = history;
    }
    public static final Parcelable.Creator<CitySuggetions> CREATOR=new Parcelable.ClassLoaderCreator<CitySuggetions>() {
        @Override
        public CitySuggetions createFromParcel(Parcel source, ClassLoader loader) {
            return null;
        }

        @Override
        public CitySuggetions createFromParcel(Parcel source) {
            return new CitySuggetions(source);
        }

        @Override
        public CitySuggetions[] newArray(int size) {
            return new CitySuggetions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeInt(IsHistory ? 1 : 0);

    }
}
