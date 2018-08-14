package donnews.ru.donnews.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import donnews.ru.donnews.Models.CommentItem;
import donnews.ru.donnews.R;

/**
 * Created by antonnikitin on 30.05.17.
 */

public class FullCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<CommentItem> mCommentItems;

    public FullCommentsAdapter(Context context, ArrayList<CommentItem> commentItems) {
        this.mContext = context;
        this.mCommentItems = commentItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentItem commentItem = mCommentItems.get(position);
        Typeface typefaceLead = Typeface.createFromAsset(mContext.getAssets(), "PT_Sans-Caption-Web-Regular.ttf");
        Typeface typefaceTitle = Typeface.createFromAsset(mContext.getAssets(), "PT_Sans-Web-Bold.ttf");
        ((ViewHolder) holder).mAuthorNameTextView.setTypeface(typefaceLead);
        ((ViewHolder) holder).mAuthorNameTextView.setText(commentItem.getAuthor_name());
        ((ViewHolder) holder).mCommentTextView.setTypeface(typefaceLead);
        ((ViewHolder) holder).mCommentTextView.setText(commentItem.getText());
    }

    @Override
    public int getItemCount() {
        return mCommentItems.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.author_name)
        TextView mAuthorNameTextView;
        @Bind(R.id.text_comments) TextView mCommentTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
