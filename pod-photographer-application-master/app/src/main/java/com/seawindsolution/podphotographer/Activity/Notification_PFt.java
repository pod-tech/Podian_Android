package com.seawindsolution.podphotographer.Activity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ronak Gopani on 16/10/19 at 5:06 PM.
 */
public class Notification_PFt {

    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Title")
    @Expose
    private String Title;
    @SerializedName("CustomerId")
    @Expose
    private String CustomerId;
    @SerializedName("photographerId")
    @Expose
    private String photographerId;
    @SerializedName("generalId")
    @Expose
    private String generalId;
    @SerializedName("Type")
    @Expose
    private String Type;
    @SerializedName("EntDt")
    @Expose
    private String EntDt;

    public String getPhotographerId() {
        return photographerId;
    }

    public void setPhotographerId(String photographerId) {
        this.photographerId = photographerId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getGeneralId() {
        return generalId;
    }

    public void setGeneralId(String generalId) {
        this.generalId = generalId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getEntDt() {
        return EntDt;
    }

    public void setEntDt(String entDt) {
        EntDt = entDt;
    }
}
