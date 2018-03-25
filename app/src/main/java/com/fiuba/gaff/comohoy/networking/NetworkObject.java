package com.fiuba.gaff.comohoy.networking;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class NetworkObject implements Parcelable {

    public static final Creator<NetworkObject> CREATOR = new Creator<NetworkObject>() {
        @Override
        public NetworkObject createFromParcel(Parcel in) {
            return new NetworkObject(in);
        }

        @Override
        public NetworkObject[] newArray(int size) {
            return new NetworkObject[size];
        }
    };

    private final String mUrl;
    private HttpMethodType mHttpMethod;
    private Map<String, String> mRequestProperties;
    private String mPostData;

    public NetworkObject(String URL, HttpMethodType httpMethod) {
        this.mUrl = URL;
        this.mHttpMethod = httpMethod;
    }

    public NetworkObject(String URL, HttpMethodType httpMethod, Map<String, String> requestProperties) {
        this(URL, httpMethod);
        this.mRequestProperties = requestProperties;
    }

    public NetworkObject(String URL, HttpMethodType httpMethod, String postData) {
        this(URL, httpMethod);
        this.mPostData = postData;
    }

    public NetworkObject(String URL, HttpMethodType httpMethod, Map<String, String> requestProperties, String postData) {
        this(URL, httpMethod, requestProperties);
        this.mPostData = postData;
    }

    public String getURL() {
        return mUrl;
    }

    public String getHttpMethod() {
        switch (mHttpMethod) {
            case GET:
                return "GET";
            case POST:
                return "POST";
        }

        return "N/A";
    }

    public String getPostData() {
        return mPostData;
    }

    public Map<String, String> GetRequestProperties() {
        return mRequestProperties;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeString(mPostData);
        dest.writeMap(mRequestProperties);
    }

    private NetworkObject(Parcel in) {
        mUrl = in.readString();
        mPostData = in.readString();
        in.readMap(mRequestProperties, String.class.getClassLoader());
    }
}
