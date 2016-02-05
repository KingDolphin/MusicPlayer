package com.leonmontealegre.musicplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public abstract class MusicListFragment extends ListFragment {

    private static final String TAG = "MusicListFragment";


    protected float angle = -(float)Math.PI / 24;

    protected Matrix matrix = new Matrix();
    protected float value = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, parent, savedInstanceState);

        getActivity().setTitle(R.string.app_title);
        ArrayAdapter adapter = getArrayAdapter();
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        ListView listView = (ListView)rootView.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return rootView;
    }

    protected abstract ArrayAdapter getArrayAdapter();

    protected abstract void onSearch(String keyword);

    public abstract void onSongFinish(Song s);

    protected void animate(final ImageView backgroundImage) {
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
