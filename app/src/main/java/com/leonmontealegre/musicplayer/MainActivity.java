package com.leonmontealegre.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    public static ControlBarFragment controlBar;

    public static TabbedFragment tabbedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = { MediaStore.Audio.Media._ID,
                                       MediaStore.Audio.Media.ARTIST,
                                       MediaStore.Audio.Media.ALBUM,
                                       MediaStore.Audio.Media.TITLE,
                                       MediaStore.Audio.Media.DATA,
                                       MediaStore.Audio.Media.ALBUM_ID,
                                       MediaStore.Audio.Media.DURATION };
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        final Cursor cursor = context.getContentResolver().query(uri, cursor_cols, where, null, null);

        while (cursor.moveToNext()) {
            String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); //is a path
            if (!new File(dataPath).exists())
                continue;

            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
            } catch (Exception e) {
                Log.d(TAG, "Song does not have album art.");
            }

            SongList.add(new Song(artist, bitmap, album, title, dataPath, albumId, duration, albumArtUri));
            Log.d(TAG, "SONG : " + title + " : " + duration);
        }
        SongList.sort();

        controlBar = new ControlBarFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentControlBar, controlBar).commit();

        tabbedFragment = new TabbedFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, tabbedFragment).commit();

        getSupportFragmentManager().beginTransaction().add(R.id.fragmentSearchBar, new SearchBarFragment()).commit();
    }

}
