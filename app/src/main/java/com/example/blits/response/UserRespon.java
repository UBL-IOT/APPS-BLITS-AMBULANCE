package com.example.blits.response;

import com.example.blits.model.ModelUser;
import com.example.blits.model.PesananModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserRespon {
    @SerializedName("code")
    private String mRc;

    @SerializedName("message")
    private String mRm;

    @SerializedName("status")
    private Boolean mStatus;

    @SerializedName("user")
    private ModelUser user;


    public Boolean getmStatus() {
        return mStatus;
    }

    public void setmStatus(Boolean mStatus) {
        this.mStatus = mStatus;
    }

    public ModelUser getUser() {
        return user;
    }

    public void setUser(ModelUser user) {
        this.user = user;
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

    public Boolean getSuccess() {
        return mStatus;
    }

    public void setSuccess(Boolean mStatus) {
        this.mStatus = mStatus;
    }
}
