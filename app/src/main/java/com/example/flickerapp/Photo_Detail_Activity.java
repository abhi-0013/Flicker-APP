package com.example.flickerapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;


import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

public class Photo_Detail_Activity extends BaseActivity {
    private static final String TAG = "Photo_Detail_Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        activateToolbar(true);
        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null){
            Log.d(TAG, "onCreate: ");
            Resources resources = getResources();
            TextView photoTitle = (TextView) findViewById(R.id.photo_title_);
            photoTitle.setText(resources.getString(R.string.Photo_Title_text,photo.getTitle()));

            TextView photoTag = (TextView) findViewById(R.id.photo_tag);
            photoTag.setText(resources.getString(R.string.Photo_Tags_text,photo.getTag()));

            TextView photoAuthor = (TextView) findViewById(R.id.phot_author);
            photoAuthor.setText(resources.getString(R.string.Photo_Author_text,photo.getAuthor()));
            ImageView imageView = (ImageView) findViewById(R.id.photo_image);

            Picasso.get().load(photo.getLink())
                    .error(R.drawable.broken_thumbnails)
                    .placeholder(R.drawable.thumbnail)
                    .into(imageView);
        }
    }

}