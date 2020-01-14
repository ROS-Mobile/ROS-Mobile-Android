package com.schneewittchen.rosandroid;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.schneewittchen.rosandroid.ui.main.MainFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

        if(fragment instanceof OnBackPressedListener) {
            OnBackPressedListener listener = (OnBackPressedListener)fragment;

            if (listener.onBackPressed()){
                return;
            }
        }

        super.onBackPressed();

    }
}
