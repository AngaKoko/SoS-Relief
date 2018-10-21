package com.example.anga_.canopener;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.anga_.canopener.Adapter.MainFragmentPagerAdapter;
import com.example.anga_.canopener.fragments.VolcanoDataFragment;
import com.example.anga_.canopener.fragments.VolcanoPreparationFragment;
import com.example.anga_.canopener.fragments.VolcanoThreatsFragment;
import com.example.anga_.canopener.fragments.VolcanoVisualsFragment;

public class VolcanoActivity extends AppCompatActivity {

    MainFragmentPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volcano);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.tab_layout);
        //Reference MainFragmentPagerAdapter
        mPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());

        setUpViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager viewPager){
        mPagerAdapter.clearFragment();
        mPagerAdapter.addFragment(new VolcanoVisualsFragment(), getString(R.string.visuals));
        mPagerAdapter.addFragment(new VolcanoThreatsFragment(), getString(R.string.threats));
        mPagerAdapter.addFragment(new VolcanoPreparationFragment(), getString(R.string.safety_measures));
        mPagerAdapter.addFragment(new VolcanoDataFragment(), getString(R.string.data));
        viewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
