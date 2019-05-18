package com.saul.arf.Menu;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.saul.arf.BuildConfig;
import com.saul.arf.R;

public class FragmentNavigationManager implements NavigationManager{
    private static FragmentNavigationManager mInstance;
    private FragmentManager mFragmentManager;
    private MenuActivity menuActivity;

    public  static FragmentNavigationManager getmInstance(MenuActivity menuActivity){
        if(mInstance==null)
            mInstance = new FragmentNavigationManager();
        mInstance.configure(menuActivity);
        return mInstance;
    }

    private void configure(MenuActivity menuActivity){
        this.menuActivity = menuActivity;
        mFragmentManager = menuActivity.getSupportFragmentManager();
    }

    @Override
    public void showFragment(String title){
        showFragment(ContentFragment.newInstance(title),false);
    }

    public void showFragment(Fragment contentFragment,boolean b) {
        FragmentManager fm = mFragmentManager;
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.container,contentFragment);
        ft.addToBackStack(null);
        if(b || !BuildConfig.DEBUG){
            ft.commitAllowingStateLoss();
        }else{
            ft.commit();
        }
        fm.executePendingTransactions();
    }
}
