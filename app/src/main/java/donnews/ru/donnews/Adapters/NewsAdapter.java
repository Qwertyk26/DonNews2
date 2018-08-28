package donnews.ru.donnews.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Helpers.Helper;
import donnews.ru.donnews.Models.NewsItem;
import donnews.ru.donnews.R;
import static donnews.ru.donnews.Helpers.Helper.getTimeAgo;

/**
 * Created by antonnikitin on 27.03.17.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NewsItem> mNewsList;
    private Context mContext;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_AUTHOR_COLUMN = 3;
    private static final int TYPE_STORIES = 4;
    private static final int TYPE_SPECPROECTS = 5;
    private static final int TYPE_INTERVIEW = 6;
    private final int VIEW_TYPE_LOADING = 2;
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private Handler handler;
    private boolean mIsInterView;
    private boolean mIsAuthorColumn;
    private boolean mIsStoriesColumn;
    private boolean mIsSpecProjectsColumn;
    private int screenWidth;
    private float newHeight;
    private int spaceBetweenAds;

    public NewsAdapter(Context context, RecyclerView mRecyclerView,  int spaceBetweenAds) {
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
        this.spaceBetweenAds = spaceBetweenAds;
    }
    public void addItems(ArrayList<NewsItem> newsList, boolean isInterView,boolean isAuthorColumn, boolean isStoriesColumn, boolean isSpecProjectsColumn) {
        mNewsList = newsList;
        mIsInterView = isInterView;
        mIsAuthorColumn = isAuthorColumn;
        mIsStoriesColumn = isStoriesColumn;
        mIsSpecProjectsColumn = isSpecProjectsColumn;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }  else if (mNewsList.get(position) == null) {
            return VIEW_TYPE_LOADING;
        } else if (mNewsList.get(position).getCategory().contains("authorcolumn")) {
            return TYPE_AUTHOR_COLUMN;
        } else if (mNewsList.get(position).getCategory().contains("stories")) {
            return TYPE_STORIES;
        } else if (mNewsList.get(position).getCategory().contains("specproects")) {
            return TYPE_SPECPROECTS;
        } else if (mNewsList.get(position).getCategory().contains("interview")) {
            return TYPE_INTERVIEW;
        }
        return TYPE_ITEM;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            if (mIsStoriesColumn) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stories, parent, false);
                return new StoriesViewHolder(v);
            } else if (mIsInterView)  {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interview, parent, false);
                return new InterviewViewHolder(v);
            } else if (mIsAuthorColumn) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author_news, parent, false);
                return new AuthorViewHolder(v);
            } else if (mIsSpecProjectsColumn) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specproects, parent, false);
                return new SpecProjectsViewHolder(v);
            } else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_item_news, parent, false);
                return new ViewHolder(v);
            }
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_item, parent, false);
            return new ProgressViewHolder(v);
        } else if (viewType == TYPE_AUTHOR_COLUMN) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author_news, parent, false);
            return new AuthorViewHolder(v);
        } else if (viewType == TYPE_STORIES) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stories, parent, false);
            return new StoriesViewHolder(v);
        } else if (viewType == TYPE_SPECPROECTS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specproects, parent, false);
            return new SpecProjectsViewHolder(v);
        } else if (viewType == TYPE_INTERVIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interview, parent, false);
            return new InterviewViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
            return new OtherHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NewsItem mNewsItem = mNewsList.get(position);
        Typeface typefaceLead = Typeface.createFromAsset(mContext.getAssets(), "PT_Sans-Caption-Web-Regular.ttf");
        Typeface typefaceTitle = Typeface.createFromAsset(mContext.getAssets(), "PT_Sans-Web-Bold.ttf");
        Typeface serifRegular = Typeface.createFromAsset(mContext.getAssets(), "PT_Serif-Web-Regular.ttf");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date convertedDate = new Date();
        if(holder instanceof ViewHolder) {
            ((ViewHolder) holder).mCommentsImageView.setColorFilter(Color.parseColor("#FFFFFF"));
            ((ViewHolder) holder).mTitleTextView.setTypeface(typefaceTitle);
            ((ViewHolder) holder).mTitleTextView.setText(mNewsItem.getTitle());
            if (mNewsItem.getComments_count() == 0) {
                ((ViewHolder) holder).mCommentsImageView.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).mCommentsCountTextView.setText("");
            } else {
                ((ViewHolder) holder).mCommentsImageView.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).mCommentsCountTextView.setTypeface(typefaceLead);
                ((ViewHolder) holder).mCommentsCountTextView.setText(String.valueOf(mNewsItem.getComments_count()));
            }
            screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            newHeight = (screenWidth * mNewsItem.getImage_height()) / mNewsItem.getImage_width();
            ((ViewHolder) holder).mCoverImage.getLayoutParams().width = screenWidth;
            ((ViewHolder) holder).mCoverImage.getLayoutParams().height = (int) newHeight;
            Picasso.with(mContext).load(mNewsItem.getImage()).into(((ViewHolder) holder).mCoverImage);
            try {
                convertedDate = dateFormat.parse(mNewsItem.getDate());
                ((ViewHolder) holder).mDateTextView.setTypeface(typefaceLead);
                ((ViewHolder) holder).mDateTextView.setText(getTimeAgo(convertedDate.getTime(), mContext));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (holder instanceof OtherHolder) {
            ((OtherHolder) holder).mLeadTextView.setTypeface(typefaceLead);
            ((OtherHolder) holder).mLeadTextView.setText(mNewsItem.getLead());
            ((OtherHolder) holder).mTitleTextView.setTypeface(serifRegular);
            ((OtherHolder) holder).mTitleTextView.setText(mNewsItem.getTitle());
            ((OtherHolder) holder).mCommentsImageView.setColorFilter(Color.parseColor("#7e99c5"));
            try {
                convertedDate = dateFormat.parse(mNewsItem.getDate());
                ((OtherHolder) holder).mDateTextView.setTypeface(typefaceLead);
                ((OtherHolder) holder).mDateTextView.setText(Helper.getTimeAgo(convertedDate.getTime(), mContext));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (mNewsItem.getComments_count() == 0) {
                ((OtherHolder) holder).mCommentsLayout.setVisibility(View.GONE);
            } else {
                ((OtherHolder) holder).mCommentsImageView.setVisibility(View.VISIBLE);
                ((OtherHolder) holder).mCommentsCountTextView.setText(String.valueOf(mNewsItem.getComments_count()));
                ((OtherHolder) holder).mCommentsCountTextView.setTypeface(typefaceLead);
            }
        } else if (holder instanceof AuthorViewHolder) {
            screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            newHeight = (screenWidth * mNewsItem.getImage_height()) / mNewsItem.getImage_width();
            ((AuthorViewHolder) holder).mCoverImage.getLayoutParams().width = screenWidth / 3;
            ((AuthorViewHolder) holder).mCoverImage.getLayoutParams().height = ((int) newHeight) / 3;
            Picasso.with(mContext).load(mNewsItem.getImage()).into(((AuthorViewHolder) holder).mCoverImage);
            ((AuthorViewHolder) holder).mTitleTextView.setTypeface(typefaceTitle);
            ((AuthorViewHolder) holder).mTitleTextView.setText(mNewsItem.getTitle());
            ((AuthorViewHolder) holder).mAuthorTextView.setText(mNewsItem.getAuthor_name());
            ((AuthorViewHolder) holder).mAuthorTextView.setTypeface(typefaceTitle);
        } else if (holder instanceof StoriesViewHolder) {
            screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            newHeight = (screenWidth * mNewsItem.getImage_height()) / mNewsItem.getImage_width();
            ((StoriesViewHolder) holder).mCoverImage.getLayoutParams().width = screenWidth;
            ((StoriesViewHolder) holder).mCoverImage.getLayoutParams().height = (int) newHeight;
            Picasso.with(mContext).load(mNewsItem.getImage()).into(((StoriesViewHolder) holder).mCoverImage);
            ((StoriesViewHolder) holder).mTitleTextView.setTypeface(typefaceTitle);
            ((StoriesViewHolder) holder).mTitleTextView.setText(mNewsItem.getTitle());
            ((StoriesViewHolder) holder).mLeadTextView.setTypeface(typefaceLead);
            ((StoriesViewHolder) holder).mLeadTextView.setText(mNewsItem.getLead());
            ((StoriesViewHolder) holder).mCategoryTitle.setTypeface(typefaceLead);
            if (mNewsItem.getComments_count() == 0) {
                ((StoriesViewHolder) holder).mCommentsLayout.setVisibility(View.GONE);
            } else {
                ((StoriesViewHolder) holder).mCommentsCountTextView.setText(String.valueOf(mNewsItem.getComments_count()));
                ((StoriesViewHolder) holder).mCommentsCountTextView.setTypeface(typefaceLead);
            }
            ((StoriesViewHolder) holder).mCommentsImageView.setColorFilter(Color.parseColor("#000000"));
        } else if (holder instanceof SpecProjectsViewHolder) {
            ((SpecProjectsViewHolder) holder).mTitleTextView.setTypeface(typefaceTitle);
            ((SpecProjectsViewHolder) holder).mTitleTextView.setText(mNewsItem.getTitle());
            ((SpecProjectsViewHolder) holder).mLeadTextView.setTypeface(typefaceLead);
            ((SpecProjectsViewHolder) holder).mLeadTextView.setText(mNewsItem.getLead());
            screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            newHeight = (screenWidth * mNewsItem.getImage_height()) / mNewsItem.getImage_width();
            ((SpecProjectsViewHolder) holder).mCoverImage.getLayoutParams().width = screenWidth;
            ((SpecProjectsViewHolder) holder).mCoverImage.getLayoutParams().height = (int) newHeight;
            Picasso.with(mContext).load(mNewsItem.getImage()).into(((SpecProjectsViewHolder) holder).mCoverImage);
            ((SpecProjectsViewHolder) holder).mCommentsImageView.setColorFilter(Color.parseColor("#FFFFFF"));
            ((SpecProjectsViewHolder) holder).mCommentsCountTextView.setTypeface(typefaceLead);
            ((SpecProjectsViewHolder) holder).mCategoryTitle.setTypeface(typefaceLead);
            if (mNewsItem.getComments_count() == 0) {
                ((SpecProjectsViewHolder) holder).mCommentsCountTextView.setText("");
            } else {
                ((SpecProjectsViewHolder) holder).mCommentsCountTextView.setText(String.valueOf(mNewsItem.getComments_count()));
                ((SpecProjectsViewHolder) holder).mCommentsCountTextView.setTypeface(typefaceLead);
            }
            try {
                convertedDate = dateFormat.parse(mNewsItem.getDate());
                ((SpecProjectsViewHolder) holder).mDateTextView.setTypeface(typefaceLead);
                ((SpecProjectsViewHolder) holder).mDateTextView.setText(getTimeAgo(convertedDate.getTime(), mContext));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (holder instanceof InterviewViewHolder) {
            ((InterviewViewHolder) holder).mTitleTextView.setTypeface(typefaceTitle);
            ((InterviewViewHolder) holder).mCategoryTitle.setTypeface(typefaceTitle);
            ((InterviewViewHolder) holder).mTitleTextView.setText(mNewsItem.getTitle());
            ((InterviewViewHolder) holder).mAuthorTextView.setText(String.format("%s ,", mNewsItem.getExpert_name()));
            ((InterviewViewHolder) holder).mAuthorPostTextView.setText(mNewsItem.getExpert_post());
            ((InterviewViewHolder) holder).mAuthorPostTextView.setTypeface(typefaceTitle);
            ((InterviewViewHolder) holder).mAuthorTextView.setTypeface(typefaceTitle);
            ((InterviewViewHolder) holder).mCategoryTitle.setText(mNewsItem.getCategory_name());
            ((InterviewViewHolder) holder).mCategoryTitle.setTypeface(typefaceLead);
            screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            newHeight = (screenWidth * mNewsItem.getImage_height()) / mNewsItem.getImage_width();
            ((InterviewViewHolder) holder).mCoverImage.getLayoutParams().width = screenWidth;
            ((InterviewViewHolder) holder).mCoverImage.getLayoutParams().height = (int) newHeight;
            Picasso.with(mContext).load(mNewsItem.getImage()).into(((InterviewViewHolder) holder).mCoverImage);
            ((InterviewViewHolder) holder).mCommentsImageView.setColorFilter(Color.parseColor("#FFFFFF"));
            ((InterviewViewHolder) holder).mCommentsCountTextView.setTypeface(typefaceLead);
            if (mNewsItem.getComments_count() == 0) {
                ((InterviewViewHolder) holder).mCommentsCountTextView.setText("");
            } else {
                ((InterviewViewHolder) holder).mCommentsCountTextView.setText(String.valueOf(mNewsItem.getComments_count()));
                ((InterviewViewHolder) holder).mCommentsCountTextView.setTypeface(typefaceLead);
            }
        } else {
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList == null ? 0 : mNewsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView mTitleTextView;
        @Bind(R.id.coverImage)
        ImageView mCoverImage;
        @Bind(R.id.comments_layout)
        RelativeLayout mCommentsLayout;
        @Bind(R.id.comments_count)
        TextView mCommentsCountTextView;
        @Bind(R.id.commentsImageView) ImageView mCommentsImageView;
        @Bind(R.id.date) TextView mDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class OtherHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lead)
        TextView mLeadTextView;
        @Bind(R.id.title) TextView mTitleTextView;
        @Bind(R.id.comments_layout)
        RelativeLayout mCommentsLayout;
        @Bind(R.id.comments_count)
        TextView mCommentsCountTextView;
        @Bind(R.id.commentsImageView) ImageView mCommentsImageView;
        @Bind(R.id.date) TextView mDateTextView;

        OtherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.progressBar)
        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
    class StoriesViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lead)
        TextView mLeadTextView;
        @Bind(R.id.title) TextView mTitleTextView;
        @Bind(R.id.coverImage)
        ImageView mCoverImage;
        @Bind(R.id.comments_layout)
        RelativeLayout mCommentsLayout;
        @Bind(R.id.comments_count)
        TextView mCommentsCountTextView;
        @Bind(R.id.commentsImageView) ImageView mCommentsImageView;
        @Bind(R.id.category_title) TextView mCategoryTitle;

        StoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class SpecProjectsViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lead)
        TextView mLeadTextView;
        @Bind(R.id.title) TextView mTitleTextView;
        @Bind(R.id.coverImage)
        ImageView mCoverImage;
        @Bind(R.id.comments_layout)
        RelativeLayout mCommentsLayout;
        @Bind(R.id.comments_count)
        TextView mCommentsCountTextView;
        @Bind(R.id.commentsImageView) ImageView mCommentsImageView;
        @Bind(R.id.category_title) TextView mCategoryTitle;
        @Bind(R.id.date) TextView mDateTextView;

        SpecProjectsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class AuthorViewHolder extends  RecyclerView.ViewHolder {
        @Bind(R.id.author_name)
        TextView mAuthorTextView;
        @Bind(R.id.title) TextView mTitleTextView;
        @Bind(R.id.coverImage)
        ImageView mCoverImage;

        AuthorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    class InterviewViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.expert_name)
        TextView mAuthorTextView;
        @Bind(R.id.expert_post)
        TextView mAuthorPostTextView;
        @Bind(R.id.title) TextView mTitleTextView;
        @Bind(R.id.coverImage)
        ImageView mCoverImage;
        @Bind(R.id.category_title) TextView mCategoryTitle;
        @Bind(R.id.comments_count)
        TextView mCommentsCountTextView;
        @Bind(R.id.commentsImageView) ImageView mCommentsImageView;

        InterviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public void setLoaded() {
        loading = false;
    }

}
