package de.refugeehackathon.transit;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawer_navigation)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        navigateTo(R.id.navigation_map);
    }

    private void navigateTo(@IdRes int navId) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, getFragmentForNavId(navId)).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        final ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        navigateTo(item.getItemId());
        return false;
    }

    private Fragment getFragmentForNavId(@IdRes int id) {
        switch (id) {
            case R.id.navigation_about:
                return new AboutFragment();

            case R.id.navigation_faq:
                return new FAQFragment();

            default:
            case R.id.navigation_map:
                return new MapFragment();
        }
    }
}
