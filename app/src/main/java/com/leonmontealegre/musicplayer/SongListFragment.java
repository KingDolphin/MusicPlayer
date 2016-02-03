package com.leonmontealegre.musicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class SongListFragment extends MusicListFragment {

    public static final String TAG = "SongListFragment";

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

//            final ImageView backgroundImage = (ImageView)parent.getRootView().findViewById(R.id.backgroundImage);
            //backgroundImage.setMinimumWidth(2560);
            //((ScrollView)parent.getRootView().findViewById(R.id.scrollView)).getLayoutParams().width = backgroundImage.getHeight() * backgroundImage.getHeight() / backgroundImage.getWidth();
           // ((ScrollView)parent.getRootView().findViewById(R.id.scrollView)).setMinimumHeight(backgroundImage.getHeight() * backgroundImage.getHeight() / backgroundImage.getWidth());
            //backgroundImage.getLayoutParams().width = backgroundImage.getHeight() * backgroundImage.getHeight() / backgroundImage.getWidth();
            //backgroundImage.setX(-125);
//            backgroundImage.buildDrawingCache();
//            Bitmap map = backgroundImage.getDrawingCache();
//            int height = backgroundImage.getHeight();
//            int width = height * map.getWidth() / map.getHeight();
//            backgroundImage.setImageBitmap(Bitmap.createBitmap(map, 0, 0, width, height));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongList.play(position);
//                    TranslateAnimation animation = new TranslateAnimation(-backgroundImage.getWidth()/2-225.0f, -125.0f, 0.0f, 0.0f);
//                    animation.setDuration(SongList.getSongs().get(position).getDuration());
//                    backgroundImage.startAnimation(animation);
                }
            });
            return convertView;
        }
    }

}