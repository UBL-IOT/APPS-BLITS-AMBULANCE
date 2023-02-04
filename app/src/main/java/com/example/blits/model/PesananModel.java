package com.example.blits.model;

import com.google.gson.annotations.SerializedName;

public class PesananModel {

    @SerializedName("guid")
    private String guid;

    @SerializedName("guid_user")
    private String guid_user;

    @SerializedName("kode_pesanan")
    private String kode_pesanan;

    @SerializedName("tujuan")
    private String tujuan;

    @SerializedName("titik_jemput")
    private String titik_jemput;

    @SerializedName("tanggal")
    private String tanggal;

    @SerializedName("status_pesanan")
    private int status_pesanan;

    @SerializedName("guid_driver")
    private String guid_driver;

    @SerializedName("tujuan_lat")
    private String tujuan_lat;

    @SerializedName("tujuan_long")
    private String tujuan_long;

    @SerializedName("titik_jemput_lat")
    private String titik_jemput_lat;

    @SerializedName("titik_jemput_long")
    private String titik_jemput_long;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("data_user")
    private ModelUser data_user;

    @SerializedName("data_driver")
    private DriverModel data_driver;

    @SerializedName("status_driver")
    private int status_driver;

    public int getStatus_driver() {
        return status_driver;
    }

    public void setStatus_driver(int status_driver) {
        this.status_driver = status_driver;
    }

    public String getKode_pesanan() {
        return kode_pesanan;
    }

    public void setKode_pesanan(String kode_pesanan) {
        this.kode_pesanan = kode_pesanan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getGuid_user() {
        return guid_user;
    }

    public void setGuid_user(String guid_user) {
        this.guid_user = guid_user;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public String getTitik_jemput() {
        return titik_jemput;
    }

    public void setTitik_jemput(String titik_jemput) {
        this.titik_jemput = titik_jemput;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getStatus_pesanan() {
        return status_pesanan;
    }

    public void setStatus_pesanan(int status_pesanan) {
        this.status_pesanan = status_pesanan;
    }

    public String getGuid_driver() {
        return guid_driver;
    }

    public void setGuid_driver(String guid_driver) {
        this.guid_driver = guid_driver;
    }

    public String getTujuan_lat() {
        return tujuan_lat;
    }

    public void setTujuan_lat(String tujuan_lat) {
        this.tujuan_lat = tujuan_lat;
    }

    public String getTujuan_long() {
        return tujuan_long;
    }

    public void setTujuan_long(String tujuan_long) {
        this.tujuan_long = tujuan_long;
    }

    public String getTitik_jemput_lat() {
        return titik_jemput_lat;
    }

    public void setTitik_jemput_lat(String titik_jemput_lat) {
        this.titik_jemput_lat = titik_jemput_lat;
    }

    public String getTitik_jemput_long() {
        return titik_jemput_long;
    }

    public void setTitik_jemput_long(String titik_jemput_long) {
        this.titik_jemput_long = titik_jemput_long;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public ModelUser getData_user() {
        return data_user;
    }

    public void setData_user(ModelUser data_user) {
        this.data_user = data_user;
    }

    public DriverModel getData_driver() {
        return data_driver;
    }

    public void setData_driver(DriverModel data_driver) {
        this.data_driver = data_driver;
    }
}
