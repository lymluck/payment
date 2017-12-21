package com.smartstudy.paysdk.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 2017/12/18.
 */

public class AliPayBean implements Parcelable {

    /**
     * prepayInfo : service=mobile.securitypay.pay&partner=xxx
     * serial : 2265f360dca911e78fb6dd55b8364031
     */

    private String prepayInfo;
    private String serial;

    public void setPrepayInfo(String prepayInfo) {
        this.prepayInfo = prepayInfo;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getPrepayInfo() {
        return prepayInfo;
    }

    public String getSerial() {
        return serial;
    }

    @Override
    public String toString() {
        return "AliPayBean{" +
                "prepayInfo='" + prepayInfo + '\'' +
                ", serial='" + serial + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prepayInfo);
        dest.writeString(this.serial);
    }

    public AliPayBean() {
    }

    protected AliPayBean(Parcel in) {
        this.prepayInfo = in.readString();
        this.serial = in.readString();
    }

    public static final Parcelable.Creator<AliPayBean> CREATOR = new Parcelable.Creator<AliPayBean>() {
        @Override
        public AliPayBean createFromParcel(Parcel source) {
            return new AliPayBean(source);
        }

        @Override
        public AliPayBean[] newArray(int size) {
            return new AliPayBean[size];
        }
    };
}
