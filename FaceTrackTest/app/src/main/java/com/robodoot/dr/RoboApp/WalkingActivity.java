package com.robodoot.dr.RoboApp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.robodoot.dr.facetracktest.R;
import com.robodoot.roboapp.PololuVirtualCat;

public class WalkingActivity extends AppCompatActivity {

    public PololuVirtualCat p;
    public TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        final Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                walk();
            }
        });
        Intent i = getIntent();
        Activity parent = getParent();
        p = new PololuVirtualCat();
        tv = (TextView) findViewById(R.id.textView3);

    }

    @Override
    protected void onResume() {
        p.p.onResume(getIntent(), this);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_walking, menu);
        return true;
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

    public void walk() {
        p.stepForward();
        Log.d("WALKING", "WALKING");
        if(p.p.isOpen()) tv.setText("TRUE");
        else             tv.setText("FALSE");
    }
}
