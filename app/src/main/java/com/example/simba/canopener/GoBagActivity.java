package com.example.simba.canopener;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.simba.canopener.Adapter.MainFragmentPagerAdapter;
import com.example.simba.canopener.fragments.gobag.BasicSupplyKitFragment;
import com.example.simba.canopener.fragments.gobag.GoBagFragment;
import com.example.simba.canopener.fragments.gobag.MaintainingYourKitFragment;

public class GoBagActivity extends AppCompatActivity {

    MainFragmentPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_bag);

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
        mPagerAdapter.addFragment(new BasicSupplyKitFragment(), getString(R.string.basic_items));
        mPagerAdapter.addFragment(new MaintainingYourKitFragment(), getString(R.string.kit_maintenance));
        mPagerAdapter.addFragment(new GoBagFragment(), getString(R.string.my_go_bag));
        //mPagerAdapter.addFragment(new VolcanoGoBagFragment(), getString(R.string.volcano));
        //mPagerAdapter.addFragment(new FloodGoBagFragment(), getString(R.string.flood));
        //mPagerAdapter.addFragment(new WildfireGoBagFragment(), getString(R.string.wildfire));
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
