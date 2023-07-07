package com.example.flickerapp;

import android.icu.util.ICUUncheckedIOException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

enum JsonParsingStatus {UNABLE_TO_PARSE_DUE_TO_DOWNLOAD_ERROR, OK, DOWNLOADED_SUCCESSFULLLY_BUT_NOT_PARSED, IDLE}

public class GetFlickerJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OndownloadComplete {
    private static final String TAG = "GetFlickerJsonData";
    private List<Photo> mPhotoList;
    private String mBaseUrl;
    private String mlang;
    private boolean mMatchall;
    private OndataAvailable mcallback;
    private boolean runOnSameThread = false;
    private JsonParsingStatus jsonParsingStatus;

    interface OndataAvailable {
        void ondataAvailable(List<Photo> data, JsonParsingStatus status);
    }

    public GetFlickerJsonData(OndataAvailable mcallback, String mBaseUrl, String mlang, boolean mMatchall) {
        this.mBaseUrl = mBaseUrl;
        this.mlang = mlang;
        this.mMatchall = mMatchall;
        this.mcallback = mcallback;
        this.jsonParsingStatus = JsonParsingStatus.IDLE;
    }

    public void executeInTheSameThread(String searchCriteria) {
        Log.d(TAG, "executeInTheSameThread: start");
        runOnSameThread = true;
        String destinationUri = CreateUri(searchCriteria, mlang, mMatchall);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "executeInTheSameThread: ends");
    }

    private String CreateUri(String searchCriteria, String lang, boolean matchall) {
        Log.d(TAG, "CreateUri: starts");
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchall ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: started");
        if (mcallback != null) {
            mcallback.ondataAvailable(photos, jsonParsingStatus);
        }
    }

    @Override
    protected List<Photo> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: started ");
        String destinationUri = CreateUri(strings[0], mlang, mMatchall);
        GetRawData getRawData = new GetRawData(this);
//        Log.d(TAG, "doInBackground:  uri created is ::::::::::::"+destinationUri);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;
    }

    @Override
    public void ondownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "ondownloadComplete: starts");

        if (status == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();
            jsonParsingStatus = JsonParsingStatus.OK;
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    if(title.equalsIgnoreCase("")){
//                        Log.d(TAG, "ondownloadComplete: INSIDE MY CONDITIONS............");
                        title = "Title is not Available for this Photo";
                    }
                    String author = jsonPhoto.getString("author");
                    String author_id = jsonPhoto.getString("author_id");
                    String tag = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String IMageUrl = jsonMedia.getString("m");
                    String link = IMageUrl.replaceFirst("_m.", "_b.");
                    Photo currFeed = new Photo(title, author, author_id, link, IMageUrl, tag);
                    mPhotoList.add(currFeed);
//                    Log.d(TAG, "ondownloadComplete: photo parameter" + currFeed.toString());
                }

            } catch (JSONException jsone) {
                Log.e(TAG, "ondownloadComplete: exception due to jsone" + jsone.getMessage());
                jsonParsingStatus = JsonParsingStatus.DOWNLOADED_SUCCESSFULLLY_BUT_NOT_PARSED;
            }
        }
        if (status != DownloadStatus.OK) {
            jsonParsingStatus = JsonParsingStatus.UNABLE_TO_PARSE_DUE_TO_DOWNLOAD_ERROR;
        }
        if (runOnSameThread && mcallback != null) {

            mcallback.ondataAvailable(mPhotoList, jsonParsingStatus);
        }
        Log.d(TAG, "ondownloadComplete: ends");
    }
}
