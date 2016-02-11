package com.leonmontealegre.musicplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    // The ControlBarFragment instance
    public static ControlBarFragment controlBar;

    // The TabbedFragment instance
    public static TabbedFragment tabbedFragment;

    // Angle to translate the image across
    protected final float angle = -(float)Math.PI / 24;

    // Matrix for translating the background image
    protected Matrix matrix = new Matrix();

    // Stupid variable to stop a dumb bug with android where it waits a really long time to start
    // the animation when it shouldn't
    protected float value = -1;

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

        while (cursor.moveToNext()) { // Loads songs from files on the system
            String dataPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)); //is a path
            if (!new File(dataPath).exists())
                continue;

            String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            if (duration == 0)
                continue;

            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            Bitmap bitmap = null;
            try { // tries to load album art, since most songs don't have album art in them
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
            } catch (Exception e) {
                Log.d(TAG, "Song does not have album art.");
            }

            SongList.add(new Song(artist, bitmap, album, title, dataPath, albumId, duration, albumArtUri));
            Log.d(TAG, "SONG : " + title + " : " + duration);
        }
        SongList.sort(); // Sorts all the songs in alphabetical order

        controlBar = new ControlBarFragment(); // Loads the control bar with play/pause/fastforward/rewind
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentControlBar, controlBar).commit();

        tabbedFragment = new TabbedFragment(); // Loads the fragment that has all the tabs
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, tabbedFragment).commit();

        // Loads the search bar
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentSearchBar, new SearchBarFragment()).commit();


        final ImageView backgroundImage = (ImageView)findViewById(R.id.backgroundImage);
        backgroundImage.setImageBitmap(blur(((BitmapDrawable) backgroundImage.getDrawable()).getBitmap())); // Blurs the background image
        animate(backgroundImage); // Animates the background
    }

    protected void animate(final ImageView backgroundImage) {
        backgroundImage.setScaleType(ImageView.ScaleType.MATRIX);

        final float scaleFactor = (float) backgroundImage.getHeight() / (float) backgroundImage.getDrawable().getIntrinsicHeight() * 1.25f;
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate(-value * (float) Math.cos(angle), value * (float) Math.sin(angle)); // Translates the matrix
        backgroundImage.setImageMatrix(matrix);

        ValueAnimator animator; // Creates an animator that goes from 0->100 or 100->0
        if (value == 0)
            animator = ValueAnimator.ofFloat(0, 100);
        else
            animator = ValueAnimator.ofFloat(value, 0);

        // Updater that translates the image in the direction
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

    public Bitmap blur(Bitmap bitmap) {
        Bitmap blurredMap = bitmap.copy(bitmap.getConfig(), true);

        RenderScript rs = RenderScript.create(this);
        final Allocation input = Allocation.createFromBitmap(rs, blurredMap);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(5f);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurredMap);

        return blurredMap;
    }

}
