package com.example.flickerapp;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;


public class SearchActivity extends BaseActivity {
    private static final String TAG = "SearchActivity";
    private SearchView msearchView;
    private String tag = "ANY";
    private boolean isTagSelected = false;
    private boolean isLangSelected = false;
    private String lang = "en-us";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        activateToolbar(true);
        Button tg_any = (Button) findViewById(R.id.Tag_forANY);
        Button tg_all = (Button) findViewById(R.id.Tag_forALL);
        Button but_english = (Button) findViewById(R.id.button_english);
        Button but_german = (Button) findViewById(R.id.button_german);
        Button but_CHinese = (Button) findViewById(R.id.Button_chinese);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button)view;
                String res = b.getText().toString();
                Log.d(TAG, "onClick: starts with "+ res);
                if("ANY".equalsIgnoreCase(res)){
                    if(isTagSelected){
                        tg_all.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                    }
                    b.setBackgroundColor(getResources().getColor(R.color.DARK_PRIMARY_COLOR));
                    tag = res;
                    isTagSelected = true;
                }
                else if("ALL".equalsIgnoreCase(res)){
                    if(isTagSelected){
                        tg_any.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                    }
                    b.setBackgroundColor(getResources().getColor(R.color.DARK_PRIMARY_COLOR));
                    isTagSelected = true;
                    tag = res;

                }else if("ENGLISH".equalsIgnoreCase(res)){
                    if(isLangSelected)
                    {
                        but_german.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                        but_CHinese.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                    }
                    b.setBackgroundColor(getResources().getColor(R.color.DARK_PRIMARY_COLOR));
                    lang = "en-us";
                    isLangSelected = true;
                }else if("GERMAN".equalsIgnoreCase(res)){
                    if(isLangSelected)
                    {
                        but_english.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                        but_CHinese.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                    }
                    b.setBackgroundColor(getResources().getColor(R.color.DARK_PRIMARY_COLOR));
                    lang = "de-de";
                    isLangSelected = true;
                }else if("CHINESE".equalsIgnoreCase(res)){
                    if(isLangSelected)
                    {
                        but_german.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                        but_english.setBackgroundColor(getResources().getColor(R.color.PRIMARY_COLOR));
                    }
                    b.setBackgroundColor(getResources().getColor(R.color.DARK_PRIMARY_COLOR));
                    lang = "zh-hk";
                    isLangSelected = true;
                }

            }
        };

        tg_all.setOnClickListener(listener);
        tg_any.setOnClickListener(listener);
        but_english.setOnClickListener(listener);
        but_CHinese.setOnClickListener(listener);
        but_german.setOnClickListener(listener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        msearchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        msearchView.setSearchableInfo(searchableInfo);
        Log.d(TAG, "onCreateOptionsMenu: "+getComponentName().toString());
        Log.d(TAG, "onCreateOptionsMenu: hint is"+ msearchView.getQueryHint());
        Log.d(TAG, "onCreateOptionsMenu: searchable info is "+ searchableInfo.toString());

        msearchView.setIconified(false);

        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "onQueryTextSubmit: starts");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sharedPreferences.edit().putString(FLICKER_QUERY,s).apply();
                sharedPreferences.edit().putString(PHOTO_TAG_MODE,tag).apply();
                sharedPreferences.edit().putString(PHOTO_LANG,lang).apply();
                msearchView.clearFocus();
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        msearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return true;
            }
        });
        return true;
    }
}