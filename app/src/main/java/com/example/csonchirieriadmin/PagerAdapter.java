package com.example.csonchirieriadmin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    int tabsNr;
    public PagerAdapter(FragmentManager fm, int tabsNr){
        super(fm);
        this.tabsNr = tabsNr;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new InchirieriFragment();
            case 1:
                return new StatisticiFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return this.tabsNr;
    }
}
