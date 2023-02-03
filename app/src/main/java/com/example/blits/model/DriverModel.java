package com.example.blits.model;

import com.google.gson.annotations.SerializedName;

public class DriverModel {

    @SerializedName("guid")
    private String guid;

    @SerializedName("instansi")
    private String instansi;

    @SerializedName("no_plat")
    private String no_plat;

    @SerializedName("nama_driver")
    private String nama_driver;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("status_driver")
    private String status_driver;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("no_telpon")
    private String no_telpon;

    @SerializedName("data_user")
    private ModelUser user;

    public String getNo_telpon() {
        return no_telpon;
    }

    public void setNo_telpon(String no_telpon) {
        this.no_telpon = no_telpon;
    }

    public ModelUser getUser() {
        return user;
    }

    public void setUser(ModelUser user) {
        this.user = user;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getInstansi() {
        return instansi;
    }

    public void setInstansi(String instansi) {
        this.instansi = instansi;
    }

    public String getNo_plat() {
        return no_plat;
    }

    public void setNo_plat(String no_plat) {
        this.no_plat = no_plat;
    }

    public String getNama_driver() {
        return nama_driver;
    }

    public void setNama_driver(String nama_driver) {
        this.nama_driver = nama_driver;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getStatus_driver() {
        return status_driver;
    }

    public void setStatus_driver(String status_driver) {
        this.status_driver = status_driver;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
