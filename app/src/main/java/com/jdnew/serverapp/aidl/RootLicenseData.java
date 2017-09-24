package com.jdnew.serverapp.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * description :
 * author : JDNew
 * on : 2017/9/24.
 */

public class RootLicenseData implements Parcelable {

    private String license;
    private byte[] rsaPublicKey;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public byte[] getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(byte[] rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public RootLicenseData(String license , byte[] rsaPublicKey){
        this.license = license;
        this.rsaPublicKey = rsaPublicKey;
    }

    protected RootLicenseData(Parcel in) {
        license = in.readString();
        rsaPublicKey = in.createByteArray();
    }

    public static final Creator<RootLicenseData> CREATOR = new Creator<RootLicenseData>() {
        @Override
        public RootLicenseData createFromParcel(Parcel in) {
            return new RootLicenseData(in);
        }

        @Override
        public RootLicenseData[] newArray(int size) {
            return new RootLicenseData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(license);
        parcel.writeByteArray(rsaPublicKey);
    }
}
