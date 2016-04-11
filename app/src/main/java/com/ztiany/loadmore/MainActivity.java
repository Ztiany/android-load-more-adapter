package com.ztiany.loadmore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ztiany.loadmore.loadmore.GridRecyclerViewFragment;
import com.ztiany.loadmore.loadmore.LinearRecyclerViewFragment;
import com.ztiany.loadmore.loadmore.StaggeredGridRecyclerViewFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            setupFragment(LinearRecyclerViewFragment.class);
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
                setupFragment(GridRecyclerViewFragment.class);
                break;
            case R.id.menu_staggered:
                setupFragment(StaggeredGridRecyclerViewFragment.class);
                break;
            case R.id.menu_linear:
                setupFragment(LinearRecyclerViewFragment.class);
                break;
        }

        return true;
    }

    private void setupFragment(Class clazz) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.act_frag_container, Fragment.instantiate(this, clazz.getName()))
                .commit();
    }
}
