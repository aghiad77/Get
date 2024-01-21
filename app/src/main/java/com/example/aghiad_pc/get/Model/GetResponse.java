package com.example.aghiad_pc.get.Model;


import com.google.gson.annotations.SerializedName;

public class GetResponse {


    @SerializedName("status")
    private String status;

    @SerializedName("value")
    private String value;

    @SerializedName("msg")
    private String msg;

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     * The msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     *
     * @param msg
     * The message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
