package donnews.ru.donnews.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Adapters.NewsAdapter;
import donnews.ru.donnews.AppApplication;
import donnews.ru.donnews.DetailNewsActivity;
import donnews.ru.donnews.Helpers.RecyclerItemClickListener;
import donnews.ru.donnews.Models.NewsItem;
import donnews.ru.donnews.Models.RequestResponse;
import donnews.ru.donnews.Network.LoadMoreInterface;
import donnews.ru.donnews.Network.NetworkClient;
import donnews.ru.donnews.Network.NewsInterface;
import donnews.ru.donnews.Presenters.LoadMorePresenter;
import donnews.ru.donnews.Presenters.NewsMixPresenter;
import donnews.ru.donnews.R;
import rx.Observable;

/**
 * Created by antonnikitin on 27.03.17.
 */

public class MainNewsFragment extends Fragment implements NewsInterface, LoadMoreInterface {
    @Inject
    NetworkClient mNetworkClient;
    NewsMixPresenter mNewsPresenter;
    LoadMorePresenter mLoadMorePresenter;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    NewsAdapter mNewsAdapter;
    ArrayList<NewsItem> mNewsList;
    int offset = 0;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRefreshing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppApplication) getActivity().getApplicationContext()).getApiComponent().inject(this);
        mNewsPresenter = new NewsMixPresenter(this);
        mNewsPresenter.onCreate();
        mNewsPresenter.getMixNews();
        mLoadMorePresenter = new LoadMorePresenter(this);
        mLoadMorePresenter.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, rootView);
        mRecyclerView.setLayoutFrozen(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsAdapter = new NewsAdapter(getActivity(), mRecyclerView, 5);
        mRecyclerView.setAdapter(mNewsAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                isRefreshing = true;
                mNewsPresenter.getMixNews();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_red_light));
        return rootView;
    }

    @Override
    public void onCompleted() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressBar.animate().alpha(0);
    }

    @Override
    public void onError(String message) {
        mProgressBar.animate().alpha(0);
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        Toast.makeText(getActivity(), "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNews(RequestResponse news) {
        mNewsList = new ArrayList<>();
        Collections.sort(news.getResult(), new Comparator<NewsItem>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            @Override
            public int compare(NewsItem lhs, NewsItem rhs) {
                try {
                    return f.parse(rhs.getDate()).compareTo(f.parse(lhs.getDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        for (int i = 0; i < news.getResult().size(); i++) {
            mNewsList.add(news.getResult().get(i));
        }
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent mIntent = new Intent(getActivity(), DetailNewsActivity.class);
                mIntent.putExtra("news", mNewsList);
                mIntent.putExtra("position", position);
                mIntent.putExtra("offset", offset);
                mIntent.putExtra("category", "main");
                getActivity().startActivity(mIntent);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        mNewsAdapter.addItems(mNewsList, false, false, false, false);
        mNewsAdapter.setOnLoadMoreListener(new NewsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                offset+=11;
                mNewsList.add(null);
                mNewsAdapter.notifyItemInserted(mNewsList.size() - 1);
                mLoadMorePresenter.loadMore();
            }
        });
    }

    @Override
    public Observable<RequestResponse> getNews() {
        if (isRefreshing) {
            return mNetworkClient.getArticleList("main", 0, 10);
        } else {
            return mNetworkClient.getArticleList("main", offset, 10);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNewsPresenter.onDestroy();
    }

    @Override
    public void onCompletedLoadMore() {

    }

    @Override
    public void onErrorLoadMore(String message) {
        Toast.makeText(getActivity(), "Во время загрузки произошла ошибка", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore(RequestResponse news) {
        mNewsList.remove(mNewsList.size() - 1);
        mNewsAdapter.notifyItemRemoved(mNewsList.size());
        Collections.sort(news.getResult(), new Comparator<NewsItem>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            @Override
            public int compare(NewsItem lhs, NewsItem rhs) {
                try {
                    return f.parse(rhs.getDate()).compareTo(f.parse(lhs.getDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        for (int i = 0; i < news.getResult().size(); i++) {
            mNewsList.add(news.getResult().get(i));
            mNewsAdapter.notifyItemInserted(mNewsList.size());
        }
        mNewsAdapter.setLoaded();
    }

    @Override
    public Observable<RequestResponse> loadMore() {
        return mNetworkClient.getLoadMoreArticleList("main", offset, 10);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mNewsAdapter.notifyDataSetChanged();
    }
}
