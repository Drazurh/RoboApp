package com.robodoot.roboapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.robodoot.dr.RoboApp.PololuHandler;
import com.robodoot.dr.facetracktest.R;

public class MainActivity extends FragmentActivity implements
        NavigationDrawerCallbacks,
        ServoControlFragment.OnFragmentInteractionListener,
        CompTestFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    public PololuHandler pololu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        pololu = (PololuHandler)intent.getExtras().getSerializable("pololu");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);



    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch(position) {
            case 0:
                fragment = new ServoControlFragment();
                Toast.makeText(this, "Servo Control", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                fragment = new CompTestFragment();
                Toast.makeText(this, "Unit Testing", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                fragment = new HomeFragment();
                Toast.makeText(this, "Console", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                finish();
                break;
            case 4:
                Intent intent = new Intent("com.robodoot.dr.RoboApp.ColorTrackingActivity");
                startActivity(intent);
                return;
            default:
                break;

        }

        if (fragment == null) {
            Toast.makeText(this, "No associated fragment for the selected menu item.", Toast.LENGTH_SHORT).show();
            return;
        }

        transaction.replace(R.id.container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String stredittext = data.getStringExtra("edittextvalue");
                Bundle bndle = new Bundle();
                bndle.putString("txt", stredittext);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Log.d("TAG", "Creating New Component Test: " + stredittext);
                CompTestFragment cn = new CompTestFragment();
                cn.setArguments(bndle);
                transaction.replace(R.id.container, cn);
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    public void AddMessage(View view) {
        TextView disp = (TextView)findViewById(R.id.sendtext);
        CharSequence curr = disp.getText();
        CharSequence msg = ((EditText)findViewById(R.id.message)).getText();
        String newText = curr.toString() + "\n" + msg.toString();
        char[] newT = newText.toCharArray();
        disp.setText(newT, 0, newT.length);
        ((EditText)findViewById(R.id.message)).setText("");
        return;
    }
}
