package com.ztiany.loadmore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            setupFragment(DemoFragment.newInstance(1, false, View.VISIBLE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mune_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_grid:
                setupFragment(DemoFragment.newInstance(2, false, View.VISIBLE));
                break;
            case R.id.menu_staggered:
                setupFragment(DemoFragment.newInstance(3, true, View.VISIBLE));
                break;
            case R.id.menu_linear:
                setupFragment(DemoFragment.newInstance(1, false, View.VISIBLE));
                break;
            case R.id.menu_linear_auto_invisible:
                setupFragment(DemoFragment.newInstance(1, true, View.INVISIBLE));
                break;
            case R.id.menu_linear_auto_gone:
                setupFragment(DemoFragment.newInstance(1, false, View.GONE));
                break;
        }
        return true;
    }

    private void setupFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.act_frag_container, fragment)
                .commit();
    }

}
