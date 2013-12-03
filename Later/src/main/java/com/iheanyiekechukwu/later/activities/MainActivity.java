package com.iheanyiekechukwu.later.activities;

//import android.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.iheanyiekechukwu.later.R;
import com.iheanyiekechukwu.later.adapters.MessageAdapter;
import com.iheanyiekechukwu.later.fragments.NavigationDrawerFragment;
import com.iheanyiekechukwu.later.models.MessageModel;

import java.util.ArrayList;

;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ListView mListView;
    private Context context;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        this.context = getBaseContext();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                mTitle = "Pending Messages";
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                mTitle = "Deleted Messages";
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                mTitle = "Sent Messages";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

        //Intent intent = new Intent(this, ComposeActivity.class);
        Intent intent = new Intent(this, AlternateActivity.class);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Context context;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            args.putString()
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            this.context = getActivity().getBaseContext();


            ArrayList<MessageModel> testModelList = new ArrayList<MessageModel>();

           /* MessageModel test1 = new MessageModel(Constants.MESSAGE_TYPE.MESSAGE, "Testing Recipient", "Hey, this is my test message." +
                    " Pretty cool, huh?", "Tomorrow at 8:00PM");

            MessageModel test2 = new MessageModel(Constants.MESSAGE_TYPE.MESSAGE, "Testing Recipient", "Hey, this is my test message." +
                    " Pretty cool, huh?", "Tomorrow at 8:00PM");

            MessageModel test3 = new MessageModel(Constants.MESSAGE_TYPE.FACEBOOK, "Testing Recipient", "Hey, this is my test message." +
                    " Pretty cool, huh?", "Tomorrow at 8:00PM");

            MessageModel test4 = new MessageModel(Constants.MESSAGE_TYPE.TWITTER, "Testing Recipient", "Just testing of a Twitter post.", "Tomorrow at 8:00 PM");

            testModelList.add(test1);
            testModelList.add(test4);
            testModelList.add(test3);

            MessageModel[] testArray = {test1, test4, test3};*/

//              SwipeListView listView = (SwipeListView) rootView.findViewById(R.id.messageListView);

            ListView listView = (ListView) rootView.findViewById(R.id.messageListView);

           // SwipeMessageAdapter testMessageAdapter = new SwipeMessageAdapter(this.context);

            MessageAdapter testMessageAdapter = new MessageAdapter(this.context);

            //testMessageAdapter.addAll(testModelList);

            listView.setAdapter(testMessageAdapter);
            //swipeView.setAdapter(MessageAdapter);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
