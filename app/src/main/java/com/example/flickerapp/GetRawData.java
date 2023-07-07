package com.example.flickerapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

public class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private DownloadStatus mdownloadStatus;
    private OndownloadComplete mcallback;

    interface OndownloadComplete {
        void ondownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(OndownloadComplete callback) {
        this.mdownloadStatus = DownloadStatus.IDLE;
        this.mcallback = callback;
    }

    public void runInSameThread(String destinationUrl) {
        Log.d(TAG, "runInSameThread: starts");
        if (mcallback != null) {
            mcallback.ondownloadComplete(doInBackground(destinationUrl), mdownloadStatus);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: started  and the parameter are " + s);
        if (mcallback != null) {
            mcallback.ondownloadComplete(s, mdownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: start");
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        if (strings == null) {
            return null;
        }
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: response code is : " + response);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            //now A string builder object to store the downloaded raw data
            StringBuilder result = new StringBuilder();
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }
            mdownloadStatus = DownloadStatus.OK;
            Log.d(TAG, "doInBackground: ends");
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: malFormed exception" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IOexceptions" + e.getMessage());
            e.printStackTrace();
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground:  security exception" + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: reader.close io exception is executed here" + e.getMessage());
                }
            }
        }
        mdownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
