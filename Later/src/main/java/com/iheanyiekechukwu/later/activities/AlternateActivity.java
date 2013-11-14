package com.iheanyiekechukwu.later.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.adapters.MainFragmentStatePagerAdapter;
import com.iheanyiekechukwu.later.adapters.MainTabPagerAdapter;


public class AlternateActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */


    private ViewPager mViewPager;
    private ActionBar actionBar;
    private MainTabPagerAdapter mAdapter;
    private MainFragmentStatePagerAdapter mStateAdapter;
    private PagerTitleStrip tabStrip;
    private String[] tabs = {"Trash", "Pending", "Sent"};
    private int[] vpColors = { android.R.color.holo_red_dark,
                               android.R.color.holo_blue_dark,
                               android.R.color.holo_green_dark};

    private int[] vpHighlightColors = { android.R.color.holo_red_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate);

        // Set up the action bar.


        actionBar = getActionBar();
        actionBar.setTitle("Later");




        tabStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);

        //tabStrip.setDrawFullUnderline(true);
        //tabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.white));

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mStateAdapter  = new MainFragmentStatePagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);


        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
                changeStripBackground(position);
                //Toast.makeText(getBaseContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

            }
        });

        mViewPager.setAdapter(mStateAdapter);



        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected (ActionBar.Tab tab, FragmentTransaction ft) {
                //mViewPager.setBackgroundColor(vpColors[tab.getPosition()]);
                mViewPager.setCurrentItem(tab.getPosition());
                changeStripBackground(tab.getPosition());

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

            }
        };

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.

        for (String tName : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tName).setTabListener(tabListener));
        }

        mViewPager.setCurrentItem(1);
    }


    private void changeStripBackground(int position) {

        //tabStrip.setTabIndicatorColor(vpColors[position]);
        //actionBar.setBackgroundDrawble(new ColorDrawable(getResources().getColor(vpHighlightColors[position])));
        actionBar.setBackgroundDrawable(new ColorDrawable(vpHighlightColors[position]));
        tabStrip.setBackgroundColor(getResources().getColor(vpColors[position]));
//        tabStrip.notify();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alternate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_compose:

                startComposeActivity();
                Toast.makeText(this, "Clicked Compose!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startComposeActivity() {
        /**
         * Method for creating a new ComposeActivity.
         *
         */

        Intent intent = new Intent(this, ComposeActivity.class);
        //Intent intent = new Intent(this, AlternateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        changeStripBackground(tab.getPosition());
        Log.d("Testing", String.valueOf(tab.getPosition()));

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


}
