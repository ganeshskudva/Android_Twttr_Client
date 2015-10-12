package com.codepath.apps.TwttrClient.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.TwttrClient.fragments.HomeTimeLineFragment;
import com.codepath.apps.TwttrClient.fragments.MentionsTimeLineFragment;

/**
 * Created by gkudva on 10/10/15.
 */
public class TweetFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "Mentions"};
    private Context context;

    public TweetFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
        {
            return new HomeTimeLineFragment();
        }
        else if (position == 1)
        {
            return new MentionsTimeLineFragment();
        }
        else
        {
            return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }




}
