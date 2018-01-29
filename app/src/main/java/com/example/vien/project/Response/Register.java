package com.example.vien.project.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vien on 26/01/2018.
 */

public class Register {


   @SerializedName("status") // definisiin sama dengan API di web
   @Expose //eksekusi nya
    private boolean status;



    @SerializedName("msg") // definisiin sama dengan API di web
    @Expose //eksekusi nya
    private String msg;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
