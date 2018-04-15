package com.fiuba.gaff.comohoy.networking;

import java.util.List;
import java.util.Map;

/**
 * Wrapper class that serves as a union of a result value and an exception. When the download
 * task has completed, either the result value or exception can be a non-null value.
 * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
 */
public class NetworkResult {
    public String mResultValue;
    public Map<String, List<String>> mHeaders;
    public Exception mException;
    public String mUrl;

    public NetworkResult(String resultValue, String url) {
        mResultValue = resultValue;
        mUrl = url;
    }

    public NetworkResult(Exception exception) {
        mException = exception;
    }

    @Override
    public String toString() {
        if (mResultValue != null) {
            return mResultValue;
        } else if (mException != null) {
            return mException.getMessage();
        }
        return "";
    }
}