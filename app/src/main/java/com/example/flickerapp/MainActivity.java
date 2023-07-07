package com.example.flickerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flickerapp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements GetFlickerJsonData.OndataAvailable,
                                                RecyclerItemClickListener.OnRecyclerClickListener{

    private static final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter mrecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        activateToolbar(false);

        Log.d(TAG, "onCreate: starts");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));
        // here i have initialized the Photo array in the Recycler View adapter thus when will call load new
        // data it will bring no objection
        mrecyclerViewAdapter = new RecyclerViewAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(mrecyclerViewAdapter);

     Log.d(TAG, "onCreate: ends");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String QueryResult = sharedPreferences.getString(FLICKER_QUERY,"");
        String tagMode = sharedPreferences.getString(PHOTO_TAG_MODE,"");
        String lang = sharedPreferences.getString(PHOTO_LANG,"");
        boolean matchall = false;
        if("ALL".equalsIgnoreCase(tagMode)){
            matchall = true;
        }
        if(QueryResult.length()>0){
            Log.d(TAG, "onResume: called with "+ lang + "---" + matchall);
            GetFlickerJsonData getFlickerJsonData = new GetFlickerJsonData(this, "https://www.flickr.com/services/feeds/photos_public.gne", lang, matchall);
//        getFlickerJsonData.executeInTheSameThread("cars,sports");
            getFlickerJsonData.execute(QueryResult);
            Log.d(TAG, "onResume: ends");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.activity_search){
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void ondataAvailable(List<Photo> data, JsonParsingStatus status) {
        if (status == JsonParsingStatus.OK) {
            mrecyclerViewAdapter.loadNewData(data);
        } else {
            Log.d(TAG, "ondataAvailable: download failed with status " + status);
        }
        Log.d(TAG, "ondataAvailable: ends");
    }

    @Override
    public void OnitemClick(View view, int Position) {
        Log.d(TAG, "OnitemClick: starts");
        Toast.makeText(MainActivity.this,"Normal Tap at postion"+ Position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemlongClick(View view, int Position) {
        Log.d(TAG, "OnItemlongClick: starts");
        Toast.makeText(MainActivity.this,"LONG PRESS Tap at postion"+ Position,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,Photo_Detail_Activity.class);
        intent.putExtra(PHOTO_TRANSFER,mrecyclerViewAdapter.getPhoto(Position));
        startActivity(intent);
    }
}