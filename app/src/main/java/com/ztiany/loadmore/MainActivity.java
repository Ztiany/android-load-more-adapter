package com.ztiany.loadmore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            setupFragment(BaseDemoFragment.newInstance(1, true, false));
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
                setupFragment(BaseDemoFragment.newInstance(2, true, false));
                break;
            case R.id.menu_staggered:
                setupFragment(BaseDemoFragment.newInstance(3, true, true));
                break;
            case R.id.menu_linear:
                setupFragment(BaseDemoFragment.newInstance(1, true, false));
                break;
            case R.id.menu_grid_no_load_more:
                setupFragment(BaseDemoFragment.newInstance(2, false, false));
                break;
            case R.id.menu_staggered_no_load_more:
                setupFragment(BaseDemoFragment.newInstance(3, false, true));
                break;
            case R.id.menu_linear_no_load_more:
                setupFragment(BaseDemoFragment.newInstance(1, false, false));
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
