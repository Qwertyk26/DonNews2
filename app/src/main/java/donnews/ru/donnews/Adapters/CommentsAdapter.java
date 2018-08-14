package donnews.ru.donnews.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Constant;
import donnews.ru.donnews.Models.CommentItem;
import donnews.ru.donnews.Models.NewsItem;
import donnews.ru.donnews.R;

import static donnews.ru.donnews.Helpers.Helper.getTimeAgo;

/**
 * Created by antonnikitin on 01.04.17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<CommentItem> mCommentItems;
    private NewsItem mNewsItem;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private int screenWidth;
    private float newHeight;

    public CommentsAdapter(Context context) {
        mContext = context;
    }
    public void addComments(ArrayList<CommentItem> commentItems, NewsItem newsItem) {
        mCommentItems = commentItems;
        mNewsItem = newsItem;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_detail_header, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Typeface typefaceLead = Typeface.createFromAsset(mContext.getAssets(), "PT_Sans-Caption-Web-Regular.ttf");
        Typeface typefaceTitle = Typeface.createFromAsset(mContext.getAssets(), "PT_Sans-Web-Bold.ttf");
        Typeface serifRegular = Typeface.createFromAsset(mContext.getAssets(), "PT_Serif-Web-Regular.ttf");
        Typeface authorFont = Typeface.createFromAsset(mContext.getAssets(), "PT_Serif-Web-BoldItalic.ttf");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).mCategoryTextView.setTypeface(typefaceTitle);
            ((HeaderViewHolder) holder).mCategoryTextView.setText(mNewsItem.getCategory_name());
            ((HeaderViewHolder) holder).mTitleTextView.setTypeface(typefaceTitle);
            ((HeaderViewHolder) holder).mTitleTextView.setText(mNewsItem.getTitle());
            ((HeaderViewHolder) holder).mLeadTextView.setTypeface(typefaceLead);
            ((HeaderViewHolder) holder).mLeadTextView.setText(mNewsItem.getLead());
            screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            newHeight = (screenWidth * mNewsItem.getImage_height()) / mNewsItem.getImage_width();
            ((HeaderViewHolder) holder).mCoverImageView.getLayoutParams().width = screenWidth;
            ((HeaderViewHolder) holder).mCoverImageView.getLayoutParams().height = (int) newHeight;
            Picasso.with(mContext).load(mNewsItem.getImage()).into(((HeaderViewHolder) holder).mCoverImageView);
            String clearText = mNewsItem.getText().replace("{{voter}}", "");
            String clearText1 = clearText.replace("{", "");
            String clearText2 = clearText1.replace("}", "");
            SharedPreferences mSharedPreferences = mContext.getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE);
            String htmlData;
            if (mSharedPreferences.getInt("textSize", 0) == 0) {
                htmlData = "<html><head><link rel='stylesheet' type='text/css' href='style18.css' /></head><body>" + clearText2 + "</body></html>";
            } else {
                int textSize = mSharedPreferences.getInt("textSize", 0);
                htmlData = "<html><head><link rel='stylesheet' type='text/css' href='style" + textSize+ ".css' /></head><body>" + clearText2 + "</body></html>";
            }
            ((HeaderViewHolder) holder).mWebView.getSettings().setJavaScriptEnabled(true);
            ((HeaderViewHolder) holder).mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            ((HeaderViewHolder) holder).mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress == 100) {
                        ((HeaderViewHolder) holder).mProgressBar.animate().alpha(0);
                        if (!mNewsItem.getCategory().contains("interview")) {
                            ((HeaderViewHolder) holder).mAuthorTextView.setVisibility(View.VISIBLE);
                            ((HeaderViewHolder) holder).mExpertNameTextView.setVisibility(View.GONE);
                            ((HeaderViewHolder) holder).mExpertPostTextView.setVisibility(View.GONE);
                        }
                    }
                }
            });
            ((HeaderViewHolder) holder).mWebView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
            ((HeaderViewHolder) holder).mAuthorTextView.setTypeface(authorFont);
            if (!mNewsItem.getCategory().contains("interview")) {
                ((HeaderViewHolder) holder).mAuthorTextView.setText(mNewsItem.getAuthor_name());
            } else {
                ((HeaderViewHolder) holder).mExpertNameTextView.setText(mNewsItem.getExpert_name());
                ((HeaderViewHolder) holder).mExpertNameTextView.setTypeface(typefaceTitle);
                ((HeaderViewHolder) holder).mExpertPostTextView.setText(mNewsItem.getExpert_post());
                ((HeaderViewHolder) holder).mExpertPostTextView.setTypeface(typefaceTitle);
            }
            try {
                convertedDate = dateFormat.parse(mNewsItem.getDate());
                ((HeaderViewHolder) holder).mDateTextView.setTypeface(typefaceLead);
                ((HeaderViewHolder) holder).mDateTextView.setText(getTimeAgo(convertedDate.getTime(), mContext));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (holder instanceof ViewHolder) {
            CommentItem commentItem = mCommentItems.get(position - 1);
            ((ViewHolder) holder).mAuthorNameTextView.setTypeface(authorFont);
            ((ViewHolder) holder).mAuthorNameTextView.setText(commentItem.getAuthor_name());
            ((ViewHolder) holder).mCommentTextView.setTypeface(typefaceLead);
            ((ViewHolder) holder).mCommentTextView.setText(commentItem.getText());
            Date commentDate = new Date();
            try {
                commentDate = dateFormat.parse(commentItem.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((ViewHolder) holder).mDateText.setTypeface(typefaceLead);
            ((ViewHolder) holder).mDateText.setText(getTimeAgo(commentDate.getTime(), mContext));
        }
    }

    @Override
    public int getItemCount() {
        return mCommentItems.size() + 1;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.author_name)
        TextView mAuthorNameTextView;
        @Bind(R.id.text_comments) TextView mCommentTextView;
        @Bind(R.id.date) TextView mDateText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.category_title) TextView mCategoryTextView;
        @Bind(R.id.title) TextView mTitleTextView;
        @Bind(R.id.lead) TextView mLeadTextView;
        @Bind(R.id.webView)
        WebView mWebView;
        @Bind(R.id.coverImage)
        ImageView mCoverImageView;
        @Bind(R.id.author_name) TextView mAuthorTextView;
        @Bind(R.id.expert_name) TextView mExpertNameTextView;
        @Bind(R.id.expert_post) TextView mExpertPostTextView;
        @Bind(R.id.date) TextView mDateTextView;
        @Bind(R.id.progressBar)
        ProgressBar mProgressBar;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
