package com.example.flickerapp;


import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FLICKER_QUERY = "FLICKER_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";
    static final String PHOTO_TAG_MODE ="PHOTO_TAG_MODE";
    static final String PHOTO_LANG = "PHOTO_LANG";

    void activateToolbar(boolean enableHome){
        Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null){
            Log.d(TAG, "activateToolbar: inside null statement");
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if(toolbar != null){
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(enableHome);
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
