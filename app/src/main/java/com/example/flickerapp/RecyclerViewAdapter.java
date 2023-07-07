package com.example.flickerapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private List<Photo> mPhotoList;
    private Context mcontext;

    public RecyclerViewAdapter(Context context, List<Photo> photos) {
        this.mPhotoList = photos;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public FlickrImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlickrImageViewHolder holder, int position) {

        if((mPhotoList) == null || (mPhotoList.size()==0)){
            holder.thumbnail.setImageResource(R.drawable.broken_thumbnails);
            holder.title.setText(R.string.Empty_string);
        }else{
            Photo photoitem = mPhotoList.get(position);
            Log.d(TAG, "onBindViewHolder: called for " + photoitem.getTitle() + "-->" + position);
            holder.title.setText(photoitem.getTitle());

            Picasso.get().load(photoitem.getImage())
                    .error(R.drawable.broken_thumbnails)
                    .placeholder(R.drawable.thumbnail)
                    .into(holder.thumbnail);

        }

    }

    @Override
    public int getItemCount() {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.size() : 1);
    }

    void loadNewData(List<Photo> newPhoto) {
        Log.d(TAG, "loadNewData: called");
        mPhotoList = newPhoto;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((mPhotoList != null) && (mPhotoList.size() != 0) ? mPhotoList.get(position) : null);
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.T_title);
        }
    }
}
