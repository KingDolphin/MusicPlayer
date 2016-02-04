package com.leonmontealegre.musicplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class SongListFragment extends MusicListFragment {

    public static final String TAG = "SongListFragment";

    private float angle = -(float)Math.PI / 24;

    private Matrix matrix = new Matrix();
    private float value = -1;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, parent, savedInstanceState);

        final ImageView backgroundImage = (ImageView)parent.getRootView().findViewById(R.id.backgroundImage);
        animate(backgroundImage);

        return rootView;
    }

    @Override
    protected ArrayAdapter getArrayAdapter() {
        return new SongAdapter(SongList.getSongs());
    }

    @Override
    protected void onSearch(String keyword) {

    }

    @Override
    public void onSongFinish(Song s) {
        //Plays next song if it's not the last song
        if (s.index != SongList.getSongs().size()-1)
            SongList.play(s.index+1);
    }

    private class SongAdapter extends ArrayAdapter<Song> {
        public SongAdapter(List songs) {
            super(getActivity(), 0, songs);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
            }

            Song s = getItem(position);
            Log.d(TAG, "The song at position " + position + " is " + s.getTitle());

            Button button = (Button)convertView.findViewById(R.id.song_list_item_button);
            button.setText(s.getTitle());
            button.setSelected(true);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongList.play(position);
                }
            });
            return convertView;
        }
    }

    private void animate(final ImageView backgroundImage) {
        backgroundImage.setScaleType(ImageView.ScaleType.MATRIX);

        final float scaleFactor = (float) backgroundImage.getHeight() / (float) backgroundImage.getDrawable().getIntrinsicHeight() * 1.25f;
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate(-value * (float)Math.cos(angle), value * (float)Math.sin(angle));
        backgroundImage.setImageMatrix(matrix);

        ValueAnimator animator;
        if (value == 0)
            animator = ValueAnimator.ofFloat(0, 100);
        else
            animator = ValueAnimator.ofFloat(value, 0);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (float)animation.getAnimatedValue();
                matrix.reset();
                matrix.postScale(scaleFactor, scaleFactor);
                matrix.postTranslate(-value * (float)Math.cos(angle), value * (float)Math.sin(angle));
                backgroundImage.setImageMatrix(matrix);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animate(backgroundImage);
            }
        });
        animator.setDuration(value == -1 ? 10 : 50000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.start();
    }

}