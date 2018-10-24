package com.example.simba.canopener;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.simba.canopener.Adapter.MainFragmentPagerAdapter;
import com.example.simba.canopener.fragments.earthquake.EarthquakeAfterFragment;
import com.example.simba.canopener.fragments.earthquake.EarthquakeBeforeFragment;
import com.example.simba.canopener.fragments.earthquake.EarthquakeDataFragment;
import com.example.simba.canopener.fragments.earthquake.EarthquakeExpectationsFragment;
import com.example.simba.canopener.fragments.earthquake.EarthquakePreparationFragment;
import com.example.simba.canopener.fragments.earthquake.EarthquakeVisualsFragment;

public class EarthquakeActivity extends AppCompatActivity {

    MainFragmentPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);

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
        mPagerAdapter.addFragment(new EarthquakeVisualsFragment(), getString(R.string.visuals));
        mPagerAdapter.addFragment(new EarthquakeExpectationsFragment(), getString(R.string.expectations));
        mPagerAdapter.addFragment(new EarthquakeBeforeFragment(), getString(R.string.before));
        mPagerAdapter.addFragment(new EarthquakePreparationFragment(), getString(R.string.during));
        mPagerAdapter.addFragment(new EarthquakeAfterFragment(), getString(R.string.after));
        mPagerAdapter.addFragment(new EarthquakeDataFragment(), getString(R.string.data));
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
