package com.ztiany.loadmore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            setupFragment(DemoFragment.newInstance(1, false, false));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mune_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_grid:
                setupFragment(DemoFragment.newInstance(2, false, true));
                break;
            case R.id.menu_staggered:
                setupFragment(DemoFragment.newInstance(3, true, false));
                break;
            case R.id.menu_linear:
                setupFragment(DemoFragment.newInstance(1, false, false));
                break;
            case R.id.menu_linear_auto_hide:
                setupFragment(DemoFragment.newInstance(1, true, true));
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
