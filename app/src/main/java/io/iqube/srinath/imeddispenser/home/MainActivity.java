package io.iqube.srinath.imeddispenser.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import io.iqube.srinath.imeddispenser.models.User;
import io.iqube.srinath.imeddispenser.prescription.PrescriptionListFragment;
import io.iqube.srinath.imeddispenser.R;
import io.iqube.srinath.imeddispenser.schedule.ScheduleListFragment;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.prescription) {
            PrescriptionListFragment fragment = PrescriptionListFragment.newInstance(1);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//            startActivity(new Intent(MainActivity.this,PrescriptionListActivity.class));
            // Handle the camera action
        } else if (id == R.id.schedule) {
            ScheduleListFragment fragment = new ScheduleListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

        } else if (id == R.id.doctor) {

        } else if (id == R.id.profile) {

        } else if (id == R.id.connect_to_dispenser) {

        } else if (id == R.id.logout) {
            realm.beginTransaction();
            realm.delete(User.class);
            realm.commitTransaction();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
