package com.example.blits.model;

public class ModelUser {

    String _id, guid , fullname, username, email, password, no_telpon, alamat, role, created_at,foto_ktp,foto_selfie;

    public ModelUser() {

    }

    public ModelUser(String _id, String fullname, String username, String email, String password, String no_telpon, String alamat, String role, String created_at) {
        this._id = _id;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.no_telpon = no_telpon;
        this.alamat = alamat;
        this.role = role;
        this.created_at = created_at;
        this.guid = guid ;
        this.foto_ktp = foto_ktp ;
        this.foto_selfie = foto_selfie ;
    }

    public String getFoto_ktp() {
        return foto_ktp;
    }

    public void setFoto_ktp(String foto_ktp) {
        this.foto_ktp = foto_ktp;
    }

    public String getFoto_selfie() {
        return foto_selfie;
    }

    public void setFoto_selfie(String foto_selfie) {
        this.foto_selfie = foto_selfie;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNo_telpon() {
        return no_telpon;
    }

    public void setNo_telpon(String no_telpon) {
        this.no_telpon = no_telpon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
