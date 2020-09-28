package com.seawindsolution.podphotographer.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ronak Gopani on 16/10/19 at 5:06 PM.
 */
public class Booking_data {

    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("OrderNo")
    @Expose
    private String OrderNo;
    @SerializedName("CustomerId")
    @Expose
    private String CustomerId;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("Phone1")
    @Expose
    private String Phone1;
    @SerializedName("Phone2")
    @Expose
    private String Phone2;
    @SerializedName("ShootingAddress")
    @Expose
    private String ShootingAddress;
    @SerializedName("ShootingFlatNo")
    @Expose
    private String ShootingFlatNo;
    @SerializedName("ShootingStreet1")
    @Expose
    private String ShootingStreet1;
    @SerializedName("ShootingStreet2")
    @Expose
    private String ShootingStreet2;
    @SerializedName("ShootingArea")
    @Expose
    private String ShootingArea;
    @SerializedName("ShootingPin")
    @Expose
    private String ShootingPin;
    @SerializedName("ShootingCity")
    @Expose
    private String ShootingCity;
    @SerializedName("ShootingState")
    @Expose
    private String ShootingState;
    @SerializedName("ShootingCountry")
    @Expose
    private String ShootingCountry;
    @SerializedName("ShootingLat")
    @Expose
    private String ShootingLat;
    @SerializedName("ShootingLng")
    @Expose
    private String ShootingLng;
    @SerializedName("ShootingDate")
    @Expose
    private String ShootingDate;
    @SerializedName("ShootingStartTime")
    @Expose
    private String ShootingStartTime;
    @SerializedName("ShootingeEndTime")
    @Expose
    private String ShootingeEndTime;
    @SerializedName("ShootingHours")
    @Expose
    private String ShootingHours;
    @SerializedName("ProductId")
    @Expose
    private String ProductId;
    @SerializedName("ProductTitle")
    @Expose
    private String ProductTitle;
    @SerializedName("ProductPrice")
    @Expose
    private String ProductPrice;
    @SerializedName("Transportation")
    @Expose
    private String Transportation;
    @SerializedName("SubTotal")
    @Expose
    private String SubTotal;
    @SerializedName("Total")
    @Expose
    private String Total;
    @SerializedName("PaymentMethod")
    @Expose
    private String PaymentMethod;
    @SerializedName("PaymentStatus")
    @Expose
    private String PaymentStatus;
    @SerializedName("EntDt")
    @Expose
    private String EntDt;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("DisplayFlg")
    @Expose
    private String DisplayFlg;

    @SerializedName("ExtHours")
    @Expose
    private String ExtHours;
    @SerializedName("ExtEndTime")
    @Expose
    private String ExtEndTime;
    @SerializedName("ExtReqFlg")
    @Expose
    private String ExtReqFlg;

    @SerializedName("ExtStatus")
    @Expose
    private String ExtStatus;
    @SerializedName("ExtAvlFlg")
    @Expose
    private String ExtAvlFlg;

    @SerializedName("CheckFlg")
    @Expose
    private String CheckFlg;
    @SerializedName("ExtId")
    @Expose
    private String ExtId;
    @SerializedName("PhotographerId")
    @Expose
    private String PhotographerId;
    @SerializedName("ExtOrderId")
    @Expose
    private String ExtOrderId;

    @SerializedName("ShootingMeetPoint")
    @Expose
    private String Shootingmeetpoint;

    @SerializedName("DisplayCheckList")
    @Expose
    private String DisplayCheckList;

    public String getCheckFlg() {
        return CheckFlg;
    }

    public void setCheckFlg(String checkFlg) {
        CheckFlg = checkFlg;
    }

    public String getExtOrderId() {
        return ExtOrderId;
    }

    public void setExtOrderId(String extOrderId) {
        ExtOrderId = extOrderId;
    }

    public String getPhotographerId() {
        return PhotographerId;
    }

    public void setPhotographerId(String photographerId) {
        PhotographerId = photographerId;
    }

    public String getExtId() {
        return ExtId;
    }

    public void setExtId(String extId) {
        ExtId = extId;
    }

    public String getExtAvlFlg() {
        return ExtAvlFlg;
    }

    public void setExtAvlFlg(String extAvlFlg) {
        ExtAvlFlg = extAvlFlg;
    }

    public String getExtStatus() {
        return ExtStatus;
    }

    public void setExtStatus(String extStatus) {
        ExtStatus = extStatus;
    }

    public String getExtReqFlg() {
        return ExtReqFlg;
    }

    public void setExtReqFlg(String extReqFlg) {
        ExtReqFlg = extReqFlg;
    }

    public String getExtHours() {
        return ExtHours;
    }

    public void setExtHours(String extHours) {
        ExtHours = extHours;
    }

    public String getExtEndTime() {
        return ExtEndTime;
    }

    public void setExtEndTime(String extEndTime) {
        ExtEndTime = extEndTime;
    }

    public String getDisplayFlg() {
        return DisplayFlg;
    }

    public void setDisplayFlg(String displayFlg) {
        DisplayFlg = displayFlg;
    }

    public String getShootingLat() {
        return ShootingLat;
    }

    public void setShootingLat(String shootingLat) {
        ShootingLat = shootingLat;
    }

    public String getShootingLng() {
        return ShootingLng;
    }

    public void setShootingLng(String shootingLng) {
        ShootingLng = shootingLng;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        Phone1 = phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public String getShootingAddress() {
        return ShootingAddress;
    }

    public void setShootingAddress(String shootingAddress) {
        ShootingAddress = shootingAddress;
    }

    public String getShootingFlatNo() {
        return ShootingFlatNo;
    }

    public void setShootingFlatNo(String shootingFlatNo) {
        ShootingFlatNo = shootingFlatNo;
    }

    public String getShootingStreet1() {
        return ShootingStreet1;
    }

    public void setShootingStreet1(String shootingStreet1) {
        ShootingStreet1 = shootingStreet1;
    }

    public String getShootingStreet2() {
        return ShootingStreet2;
    }

    public void setShootingStreet2(String shootingStreet2) {
        ShootingStreet2 = shootingStreet2;
    }

    public String getShootingArea() {
        return ShootingArea;
    }

    public void setShootingArea(String shootingArea) {
        ShootingArea = shootingArea;
    }

    public String getShootingPin() {
        return ShootingPin;
    }

    public void setShootingPin(String shootingPin) {
        ShootingPin = shootingPin;
    }

    public String getShootingCity() {
        return ShootingCity;
    }

    public void setShootingCity(String shootingCity) {
        ShootingCity = shootingCity;
    }

    public String getShootingState() {
        return ShootingState;
    }

    public void setShootingState(String shootingState) {
        ShootingState = shootingState;
    }

    public String getShootingCountry() {
        return ShootingCountry;
    }

    public void setShootingCountry(String shootingCountry) {
        ShootingCountry = shootingCountry;
    }

    public String getShootingDate() {
        return ShootingDate;
    }

    public void setShootingDate(String shootingDate) {
        ShootingDate = shootingDate;
    }

    public String getShootingStartTime() {
        return ShootingStartTime;
    }

    public void setShootingStartTime(String shootingStartTime) {
        ShootingStartTime = shootingStartTime;
    }

    public String getShootingeEndTime() {
        return ShootingeEndTime;
    }

    public void setShootingeEndTime(String shootingeEndTime) {
        ShootingeEndTime = shootingeEndTime;
    }

    public String getShootingHours() {
        return ShootingHours;
    }

    public void setShootingHours(String shootingHours) {
        ShootingHours = shootingHours;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductTitle() {
        return ProductTitle;
    }

    public void setProductTitle(String productTitle) {
        ProductTitle = productTitle;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getTransportation() {
        return Transportation;
    }

    public void setTransportation(String transportation) {
        Transportation = transportation;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(String subTotal) {
        SubTotal = subTotal;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getEntDt() {
        return EntDt;
    }

    public void setEntDt(String entDt) {
        EntDt = entDt;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getShootingmeetpoint() {
        return Shootingmeetpoint;
    }

    public void setShootingmeetpoint(String shootingmeetpoint) {
        if (shootingmeetpoint == null){
            shootingmeetpoint = "";
        }
        Shootingmeetpoint = shootingmeetpoint;
    }

    public String getDisplayCheckList() {
        return DisplayCheckList;
    }

    public void setDisplayCheckList(String displayCheckList) {
        DisplayCheckList = displayCheckList;
    }
}
