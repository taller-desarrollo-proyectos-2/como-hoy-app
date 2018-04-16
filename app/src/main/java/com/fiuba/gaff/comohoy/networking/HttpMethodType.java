package com.fiuba.gaff.comohoy.networking;

public enum HttpMethodType {
    GET(0),
    POST(1);

    private final int value;
    private HttpMethodType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static HttpMethodType fromInteger(int x) {
        switch(x) {
            case 0:
                return HttpMethodType.GET;
            case 1:
                return HttpMethodType.POST;
        }
        return HttpMethodType.GET;
    }
}
