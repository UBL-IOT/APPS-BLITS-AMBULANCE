package com.example.blits.response;

import com.example.blits.model.DriverModel;
import com.example.blits.model.PesananModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PesananResponse {
    @SerializedName("code")
    private String mRc;

    @SerializedName("message")
    private String mRm;

    @SerializedName("status")
    private Boolean mStatus;

    @SerializedName("data")
    private List<PesananModel> data;

    public List<PesananModel> getData() {
        return data;
    }

    public void setData(List<PesananModel> data) {
        this.data = data;
    }

    public Boolean getmStatus() {
        return mStatus;
    }

    public void setmStatus(Boolean mStatus) {
        this.mStatus = mStatus;
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
