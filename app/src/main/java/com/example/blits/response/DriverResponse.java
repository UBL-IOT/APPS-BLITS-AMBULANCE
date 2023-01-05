package com.example.blits.response;

import com.example.blits.model.DriverModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriverResponse {
    @SerializedName("code")
    private String mRc;

    @SerializedName("message")
    private String mRm;

    @SerializedName("status")
    private Boolean mStatus;

    @SerializedName("data")
    private List<DriverModel> data;

    public Boolean getmStatus() {
        return mStatus;
    }

    public void setmStatus(Boolean mStatus) {
        this.mStatus = mStatus;
    }

    public List<DriverModel> getData() {
        return data;
    }

    public void setData(List<DriverModel> data) {
        this.data = data;
    }

    public String getmRc() {
        return mRc;
    }

    public void setmRc(String mRc) {
        this.mRc = mRc;
    }

    public String getmRm() {
        return mRm;
    }

    public void setmRm(String mRm) {
        this.mRm = mRm;
    }
}
