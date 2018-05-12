package com.omnicrola.justsimplesignal.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.security.AndroidPermission;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.omnicrola.justsimplesignal.R;
import com.omnicrola.justsimplesignal.activities.fragments.CellSignalFragment;
import com.omnicrola.justsimplesignal.data.CellSignal;
import com.omnicrola.justsimplesignal.events.cellular.CellSignalUpdateEvent;
import com.omnicrola.justsimplesignal.services.signal.cellular.CellSignalInfoService;
import com.omnicrola.justsimplesignal.tools.PermissionsWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CellSignalFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    transitionToCellSignalFragment();

                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsWrapper permissionsWrapper = new PermissionsWrapper(getApplicationContext());
        permissionsWrapper.request(this, AndroidPermission.ACCESS_COARSE_LOCATION);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        startService(new Intent(this, CellSignalInfoService.class));
        Log.d(getClass().getSimpleName(), "Starting CellSignalInfoService");

        transitionToCellSignalFragment();
    }

    private void transitionToCellSignalFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment cellSignalFragment = CellSignalFragment.newInstance();
        fragmentTransaction.replace(R.id.main_container, cellSignalFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
