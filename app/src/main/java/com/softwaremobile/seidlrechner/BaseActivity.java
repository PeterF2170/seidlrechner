package com.softwaremobile.seidlrechner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class BaseActivity extends AppCompatActivity {
    FullscreenNav newFragment;
    boolean isLargeLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

    }

    public void showDialog(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        newFragment = new FullscreenNav();

        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }
    }

    public void dismissDialog(View view) {
        newFragment.dismiss();
    }

}