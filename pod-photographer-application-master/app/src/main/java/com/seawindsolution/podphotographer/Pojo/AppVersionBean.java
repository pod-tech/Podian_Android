
package com.seawindsolution.podphotographer.Pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class AppVersionBean implements Serializable {

    @SerializedName("IsSuccess")
    private Boolean isSuccess;
    @SerializedName("Message")
    private String message;
    @SerializedName("ResponseData")
    private List<ResponseDatum> responseData;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResponseDatum> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseDatum> responseData) {
        this.responseData = responseData;
    }

    public class ResponseDatum implements Serializable{

        @SerializedName("app_name")
        private String appName;
        private String entDt;
        @SerializedName("Id")
        private String id;
        private String vandroid;
        private String versionDescription;
        private String viphone;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getEntDt() {
            return entDt;
        }

        public void setEntDt(String entDt) {
            this.entDt = entDt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVandroid() {
            return vandroid;
        }

        public void setVandroid(String vandroid) {
            this.vandroid = vandroid;
        }

        public String getVersionDescription() {
            return versionDescription;
        }

        public void setVersionDescription(String versionDescription) {
            this.versionDescription = versionDescription;
        }

        public String getViphone() {
            return viphone;
        }

        public void setViphone(String viphone) {
            this.viphone = viphone;
        }
    }

}
