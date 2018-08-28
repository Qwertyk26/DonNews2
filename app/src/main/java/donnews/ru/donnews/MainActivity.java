package donnews.ru.donnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Adapters.NewsAdapter;
import donnews.ru.donnews.Fragments.AuthorcolumnFragment;
import donnews.ru.donnews.Fragments.FullNewsFragment;
import donnews.ru.donnews.Fragments.InterviewFragment;
import donnews.ru.donnews.Fragments.MainNewsFragment;
import donnews.ru.donnews.Fragments.SpecproectsFragment;
import donnews.ru.donnews.Fragments.StoriesFragment;

public class MainActivity extends AppCompatActivity implements AdEventListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.adView)
    AdView mAdView;
    @Bind(R.id.adView_drawer)
    AdView adViewDrawer;
    public static Activity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        mViewPager.setOffscreenPageLimit(6);
        setupViewPager(mViewPager);
        ma = this;
        mTabLayout.setupWithViewPager(mViewPager);
        SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
        TextView headerText = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.username);
        FrameLayout headerLayout = (FrameLayout) mNavigationView.getHeaderView(0).findViewById(R.id.headerLayout);
        if (mSharedPreferences.getBoolean("auth", false) == false) {
            headerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.closeDrawer(Gravity.END);
                    Intent mIntent = new Intent(MainActivity.this, AuthActivity.class);
                    startActivity(mIntent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                }
            });
        } else {

            headerText.setText(mSharedPreferences.getString("username", ""));
        }
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent mIntent = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.advert:
                        mDrawerLayout.closeDrawer(Gravity.END);
                        mIntent = new Intent(MainActivity.this, AdvertActivity.class);
                        startActivity(mIntent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;
                    case R.id.about:
                        mDrawerLayout.closeDrawer(Gravity.END);
                        mIntent = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(mIntent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;
                    case R.id.leave_review:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=donnews.ru.donnews"));
                        startActivity(intent);
                        break;
                    case R.id.font_size:
                        mDrawerLayout.closeDrawer(Gravity.END);
                        mIntent = new Intent(MainActivity.this, FontSettingsActivity.class);
                        startActivity(mIntent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;
                }
                return true;
            }
        });
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mAdView.setBlockId("R-M-240930-2");
        adViewDrawer.setBlockId("R-M-240930-2");
        mAdView.setAdSize(AdSize.BANNER_320x50);
        adViewDrawer.setAdSize(AdSize.BANNER_320x50);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adViewDrawer.setAdEventListener(this);
        mAdView.setAdEventListener(this);
        mAdView.loadAd(adRequest);
        adViewDrawer.loadAd(adRequest);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainNewsFragment(), "Новости");
        adapter.addFragment(new StoriesFragment(), "Сюжеты");
        adapter.addFragment(new SpecproectsFragment(), "Спецпроекты");
        adapter.addFragment(new InterviewFragment(), "Интервью");
        adapter.addFragment(new AuthorcolumnFragment(), "Авторская колонка");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
            case R.id.search_action:
                Intent mIntent = new Intent(MainActivity.this ,SearchActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                break;
            case R.id.menu_action:
                mDrawerLayout.openDrawer(Gravity.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAdClosed() {

    }

    @Override
    public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
        Log.d("Ads error", adRequestError.toString());
    }

    @Override
    public void onAdLeftApplication() {

    }

    @Override
    public void onAdLoaded() {

    }

    @Override
    public void onAdOpened() {

    }
}
