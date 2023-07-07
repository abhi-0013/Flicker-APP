package com.example.flickerapp;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    interface OnRecyclerClickListener {
        void OnitemClick(View view, int Position);
        void OnItemlongClick(View view, int Position);
    }
    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    public RecyclerItemClickListener(Context context , final RecyclerView recyclerView , OnRecyclerClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts ");
                View Childview = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(Childview != null && mListener != null){
                    Log.d(TAG, "onSingleTapUp: calling Listener on tapUp");
                    mListener.OnitemClick(Childview, recyclerView.getChildAdapterPosition(Childview));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
                View Childview = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(Childview != null && mListener != null){
                    Log.d(TAG, "onLongPress: calling listener Onlongpress");
                    mListener.OnItemlongClick(Childview, recyclerView.getChildAdapterPosition(Childview));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mGestureDetector != null){
            boolean result= mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returns "+ result);
            return result;
        }else{
            Log.d(TAG, "onInterceptTouchEvent: returned false...");
            return false;
        }
    }
}
