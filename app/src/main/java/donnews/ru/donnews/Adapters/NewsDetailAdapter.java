package donnews.ru.donnews.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Models.NewsItem;
import donnews.ru.donnews.R;

/**
 * Created by antonnikitin on 13.04.17.
 */

public class NewsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NewsItem> mNewsItems;
    private Context mContext;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private Handler handler;
    private static final int TYPE_ITEM = 1;
    private final int VIEW_TYPE_LOADING = 2;

    public NewsDetailAdapter(Context context,ArrayList<NewsItem> newsItems, RecyclerView mRecyclerView) {
        this.mNewsItems = newsItems;
        mContext = context;
        this.handler = new Handler();
        if(mRecyclerView.getLayoutManager()instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    };
                    handler.post(runnable);
                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        return mNewsItems.get(position) !=null ? TYPE_ITEM: VIEW_TYPE_LOADING;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_load_more, parent, false);
            return new ProgressViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_detail, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            NewsItem newsItem = mNewsItems.get(position);
            CommentsAdapter mCommentsAdapter = new CommentsAdapter(mContext);
            ((ViewHolder) holder).mRecyclerView.setHasFixedSize(true);
            ((ViewHolder) holder).mRecyclerView.setNestedScrollingEnabled(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            ((ViewHolder) holder).mRecyclerView.setLayoutManager(layoutManager);
            ((ViewHolder) holder).mRecyclerView.setAdapter(mCommentsAdapter);
            mCommentsAdapter.addComments(newsItem.comments, newsItem);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mNewsItems == null ? 0 : mNewsItems.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class ProgressViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.progressBar)
        ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
    public void setLoaded(){
        loading = false;
    }
}
