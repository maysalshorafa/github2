package com.pos.leaders.leaderspossystem.SettingsTab;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Win8.1 on 3/25/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                GeneralSettings tab1 = new GeneralSettings();
                return tab1;
            case 1:
                PosSettings tab2 = new PosSettings();
                return tab2;
            case 2:
                BOPOSVersionSettings tab3 = new BOPOSVersionSettings();
                return tab3;
            case 3:
                NewTab tab4 = new NewTab();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}