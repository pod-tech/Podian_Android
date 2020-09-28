package com.seawindsolution.podphotographer.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ronak Gopani on 16/10/19 at 5:06 PM.
 */
public class Chat_adapter_p {

    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Sender")
    @Expose
    private String Sender;
    @SerializedName("Receiver")
    @Expose
    private String Receiver;
    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("EntDt")
    @Expose
    private String EntDt;
    @SerializedName("uType")
    @Expose
    private String uType;
    @SerializedName("isLocation")
    @Expose
    private String isLocation;

    public String getIsLocation() {
        return isLocation;
    }

    public void setIsLocation(String isLocation) {
        this.isLocation = isLocation;
    }
    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getEntDt() {
        return EntDt;
    }

    public void setEntDt(String entDt) {
        EntDt = entDt;
    }
}
