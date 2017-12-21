package com.smartstudy.paysdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by louis on 2017/12/18.
 */

public class WXPayBean implements Parcelable {

    /**
     * tradeType : APP
     * appid : wx102809382098409
     * partnerid : 84092384
     * prepayid : wx201712098973a9287358c4493
     * package : Sign=WXPay
     * noncestr : adfoielk
     * timestamp : 1512801403
     * sign : CC9D33A1B924EE4044DB94B83DC2EE22
     * serial : 522f9d60dcab11e78fb6dd55b8364031
     * totalAmount : 10000
     * currentFee : 2000
     */

    private String tradeType;
    private String appid;
    private String partnerid;
    private String prepayid;
    @JSONField(name = "package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;
    private String serial;
    private double totalAmount;
    private double currentFee;

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setCurrentFee(double currentFee) {
        this.currentFee = currentFee;
    }

    public String getTradeType() {
        return tradeType;
    }

    public String getAppid() {
        return appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSign() {
        return sign;
    }

    public String getSerial() {
        return serial;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getCurrentFee() {
        return currentFee;
    }

    @Override
    public String toString() {
        return "WXPayBean{" +
                "tradeType='" + tradeType + '\'' +
                ", appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", packageX='" + packageX + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", serial='" + serial + '\'' +
                ", totalAmount=" + totalAmount +
                ", currentFee=" + currentFee +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tradeType);
        dest.writeString(this.appid);
        dest.writeString(this.partnerid);
        dest.writeString(this.prepayid);
        dest.writeString(this.packageX);
        dest.writeString(this.noncestr);
        dest.writeString(this.timestamp);
        dest.writeString(this.sign);
        dest.writeString(this.serial);
        dest.writeDouble(this.totalAmount);
        dest.writeDouble(this.currentFee);
    }

    public WXPayBean() {
    }

    protected WXPayBean(Parcel in) {
        this.tradeType = in.readString();
        this.appid = in.readString();
        this.partnerid = in.readString();
        this.prepayid = in.readString();
        this.packageX = in.readString();
        this.noncestr = in.readString();
        this.timestamp = in.readString();
        this.sign = in.readString();
        this.serial = in.readString();
        this.totalAmount = in.readInt();
        this.currentFee = in.readInt();
    }

    public static final Parcelable.Creator<WXPayBean> CREATOR = new Parcelable.Creator<WXPayBean>() {
        @Override
        public WXPayBean createFromParcel(Parcel source) {
            return new WXPayBean(source);
        }

        @Override
        public WXPayBean[] newArray(int size) {
            return new WXPayBean[size];
        }
    };
}
