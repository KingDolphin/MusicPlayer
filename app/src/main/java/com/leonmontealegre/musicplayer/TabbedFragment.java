package com.leonmontealegre.musicplayer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

public class TabbedFragment extends Fragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    private ViewPager mViewPager;
    private TabHost mTabHost;
    private View mRootView;

    private MusicListFragmentPagerAdapter pagerAdapter;

    public MusicListFragment currentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
        mViewPager = (ViewPager)mRootView.findViewById(R.id.view_pager);

        // Adds fragments to list, only SongListFragments right now since we didn't have time for others
        List<MusicListFragment> listFragments = new ArrayList<MusicListFragment>();
        listFragments.add(new SongListFragment());
        listFragments.add(new SongListFragment());
        listFragments.add(new SongListFragment());

        pagerAdapter = new MusicListFragmentPagerAdapter(getFragmentManager(), listFragments);
        currentFragment = listFragments.get(0);

        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(this);

        mTabHost = (TabHost)mRootView.findViewById(R.id.tabHost);
        mTabHost.setup();

        String[] tabNames = getResources().getStringArray(R.array.tab_names);
        for(int i = 0; i < tabNames.length; i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = mTabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getActivity().getApplicationContext()));
            mTabHost.addTab(tabSpec);
        }
        mTabHost.setOnTabChangedListener(this);

        return mRootView;
    }

    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(selectedItem);

        currentFragment = pagerAdapter.getItem(selectedItem);

        //allow for the horizontal scrolling
        HorizontalScrollView hScrollView = (HorizontalScrollView)mRootView.findViewById(R.id.h_scroll_view);
        View tabView = mTabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft() - (hScrollView.getWidth() - tabView.getWidth()) / 2;
        hScrollView.smoothScrollTo(scrollPos, 0);
    }

    @Override
    public void onPageSelected(int selectedItem) {
        mTabHost.setCurrentTab(selectedItem);
    }

    private class MusicListFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<MusicListFragment> listFragments;

        public MusicListFragmentPagerAdapter(FragmentManager fm, List<MusicListFragment> listFragments) {
            super(fm);
            this.listFragments = listFragments;
        }

        @Override
        public MusicListFragment getItem(int position) {
            return listFragments.get(position);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {}

    @Override
    public void onPageScrollStateChanged(int i) {}

    private class FakeContent implements TabHost.TabContentFactory {

        Context context;

        public FakeContent(Context mContext) {
            context = mContext;
        }

        @Override
        public View createTabContent(String tag) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }


}
