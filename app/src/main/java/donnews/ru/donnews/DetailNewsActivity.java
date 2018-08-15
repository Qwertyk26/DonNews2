package donnews.ru.donnews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.yandex.mobile.ads.AdEventListener;
import com.yandex.mobile.ads.AdRequest;
import com.yandex.mobile.ads.AdRequestError;
import com.yandex.mobile.ads.AdSize;
import com.yandex.mobile.ads.AdView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Adapters.NewsDetailAdapter;
import donnews.ru.donnews.Helpers.Helper;
import donnews.ru.donnews.Models.NewsItem;
import donnews.ru.donnews.Models.RequestResponse;
import donnews.ru.donnews.Models.SignUpRespones;
import donnews.ru.donnews.Network.LoadMoreInterface;
import donnews.ru.donnews.Network.NetworkClient;
import donnews.ru.donnews.Network.SendCommentInterface;
import donnews.ru.donnews.Presenters.LoadMorePresenter;
import donnews.ru.donnews.Presenters.SendCommentPresenter;
import rx.Observable;

/**
 * Created by antonnikitin on 27.03.17.
 */

public class DetailNewsActivity extends AppCompatActivity implements LoadMoreInterface, SendCommentInterface {
    @Inject
    NetworkClient mNetworkClient;
    LoadMorePresenter mLoadMorePresenter;
    SendCommentPresenter mSendCommentPresenter;
    @Bind(R.id.recyclerView)
    RecyclerViewPager mRecyclerView;
    @Bind(R.id.toolBar)
    Toolbar mToolbar;
    ArrayList<NewsItem> mNewsList;
    @Bind(R.id.share_btn)
    FloatingActionButton mShareBtn;
    int position;
    int offset;
    NewsDetailAdapter mNewsDetailAdapter;
    private String category;
    @Bind(R.id.comment_text)
    EditText mCommentText;
    @Bind(R.id.sendBtn)
    Button sendBtn;
    Helper mHelper;
    ProgressDialog mProgressBar;
    @Bind(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ((AppApplication) getApplicationContext()).getApiComponent().inject(this);
        ButterKnife.bind(this);
        mNewsList = (ArrayList<NewsItem>) getIntent().getSerializableExtra("news");
        position = getIntent().getIntExtra("position", 0);
        offset = getIntent().getIntExtra("offset", 0);
        category = getIntent().getStringExtra("category");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mProgressBar = new ProgressDialog(this);
        mHelper = new Helper();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.scrollToPosition(position);
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(mRecyclerView);
        mNewsDetailAdapter = new NewsDetailAdapter(this, mNewsList, mRecyclerView);
        mRecyclerView.setAdapter(mNewsDetailAdapter);
        mLoadMorePresenter = new LoadMorePresenter(this);
        mSendCommentPresenter = new SendCommentPresenter(this);
        mLoadMorePresenter.onCreate();
        mNewsDetailAdapter.setOnLoadMoreListener(new NewsDetailAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                offset+=11;
                mNewsList.add(null);
                mNewsDetailAdapter.notifyItemInserted(mNewsList.size() - 1);
                mLoadMorePresenter.loadMore();
            }
        });
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mNewsList.get(mRecyclerView.getCurrentPosition()).getTitle() + "\n" + "http://www.donnews.ru/" + mNewsList.get(mRecyclerView.getCurrentPosition()).getAlias());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Поделиться с помощью"));
            }
        });
        mCommentText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
                    if (mSharedPreferences.getBoolean("auth", false) == false) {
                        Intent mIntent = new Intent(DetailNewsActivity.this, AuthActivity.class);
                        startActivity(mIntent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        mCommentText.setText("");
                    }
                }
            }
        });
        mCommentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCommentText.getText().length() == 0) {
                    sendBtn.setEnabled(false);
                } else {
                    sendBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.show();
                mProgressBar.setMessage("Подождите...");
                mSendCommentPresenter.addComment();
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
    public void onCompletedLoadMore() {

    }

    @Override
    public void onErrorLoadMore(String message) {
        Toast.makeText(DetailNewsActivity.this, "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore(RequestResponse news) {
        mNewsList.remove(mNewsList.size() - 1);
        mNewsDetailAdapter.notifyItemRemoved(mNewsList.size());
        for (int i = 0; i < news.getResult().size(); i++) {
            mNewsList.add(news.getResult().get(i));
            mNewsDetailAdapter.notifyItemInserted(mNewsList.size());
        }
        mNewsDetailAdapter.setLoaded();
    }

    @Override
    public Observable<RequestResponse> loadMore() {
        if (category.contains("mix")) {
            return mNetworkClient.getLoadMoreMixNews(offset, 10);
        } else {
            return mNetworkClient.getLoadMoreArticleList(category, offset, 10);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoadMorePresenter.onDestroy();
    }

    @Override
    public void onCompleted() {
        mProgressBar.dismiss();
    }

    @Override
    public void onError(String message) {
        mProgressBar.dismiss();
        mHelper.showToast(this, "Во время загузки произошла ошибка");
    }

    @Override
    public void onSendComment(SignUpRespones result) {

    }

    @Override
    public Observable<SignUpRespones> addComment() {
        SharedPreferences mSharedPreferences = getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
        String dn_token = mSharedPreferences.getString("dn_token", "dn_token");
        return mNetworkClient.addComment(mNewsList.get(mRecyclerView.getCurrentPosition()).getAlias(), mCommentText.getText().toString(), dn_token);
    }
}
