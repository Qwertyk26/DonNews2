package donnews.ru.donnews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Adapters.NewsAdapter;
import donnews.ru.donnews.Helpers.RecyclerItemClickListener;
import donnews.ru.donnews.Models.NewsItem;
import donnews.ru.donnews.Models.RequestResponse;
import donnews.ru.donnews.Network.LoadMoreInterface;
import donnews.ru.donnews.Network.NetworkClient;
import donnews.ru.donnews.Network.SearchInterface;
import donnews.ru.donnews.Presenters.LoadMorePresenter;
import donnews.ru.donnews.Presenters.SearchPresenter;
import rx.Observable;

/**
 * Created by antonnikitin on 11.04.17.
 */

public class SearchActivity extends AppCompatActivity implements SearchInterface, LoadMoreInterface {
    @Inject
    NetworkClient mNetworkClient;
    SearchPresenter mSearchPresenter;
    LoadMorePresenter mLoadMorePresenter;
    @Bind(R.id.searchView)
    SearchView mSearchView;
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    private NewsAdapter mNewsAdapter;
    ArrayList<NewsItem> mNewsList;
    int offset = 0;
    @Bind(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ((AppApplication) getApplicationContext()).getApiComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mSearchView.setIconified(false);
        mSearchView.setQueryHint("Поиск в Donnews");
        mSearchPresenter = new SearchPresenter(this);
        mSearchPresenter.onCreate();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mProgressBar.setVisibility(View.VISIBLE);
                mSearchPresenter.searchQuery();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new NewsAdapter(this, mRecyclerView, 5);
        mRecyclerView.setAdapter(mNewsAdapter);
        mLoadMorePresenter = new LoadMorePresenter(this);
        mLoadMorePresenter.onCreate();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public void onCompleted() {
        mProgressBar.animate().alpha(0);
    }

    @Override
    public void onError(String message) {
        mProgressBar.animate().alpha(0);
        Toast.makeText(this, "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearch(RequestResponse news) {
        mNewsList = new ArrayList<>();
        for (int i = 0; i < news.getResult().size(); i++) {
            mNewsList.add(news.getResult().get(i));
        }
        mNewsAdapter.addItems(mNewsList, false, false, false, false);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(SearchActivity.this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent mIntent = new Intent(SearchActivity.this, DetailNewsActivity.class);
                mIntent.putExtra("news", mNewsList);
                mIntent.putExtra("position", position);
                mIntent.putExtra("offset", offset);
                mIntent.putExtra("category", "mix");
                startActivity(mIntent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        mNewsAdapter.setOnLoadMoreListener(new NewsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                offset+=11;
                mNewsList.add(null);
                mNewsAdapter.notifyItemInserted(mNewsList.size() - 1);
                mLoadMorePresenter.loadMore();
            }
        });
        adView.setBlockId("R-M-240930-2");
        adView.setAdSize(AdSize.BANNER_320x50);
        final AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdEventListener(new AdEventListener() {
            @Override
            public void onAdFailedToLoad(AdRequestError adRequestError) {
                Log.d("Ads error", adRequestError.getDescription());
            }

            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {

            }
        });
        adView.loadAd(adRequest);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSearchPresenter.onDestroy();
    }
    @Override
    public Observable<RequestResponse> search() {
        return mNetworkClient.search(mSearchView.getQuery().toString(), offset, 10);
    }

    @Override
    public void onCompletedLoadMore() {

    }

    @Override
    public void onErrorLoadMore(String message) {
        Toast.makeText(this, "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore(RequestResponse news) {
        mNewsList.remove(mNewsList.size() - 1);
        mNewsAdapter.notifyItemRemoved(mNewsList.size());
        for (int i = 0; i < news.getResult().size(); i++) {
            mNewsList.add(news.getResult().get(i));
            mNewsAdapter.notifyItemInserted(mNewsList.size());
        }
        mNewsAdapter.setLoaded();
    }

    @Override
    public Observable<RequestResponse> loadMore() {
        return mNetworkClient.search(mSearchView.getQuery().toString(), offset, 10);
    }
}
