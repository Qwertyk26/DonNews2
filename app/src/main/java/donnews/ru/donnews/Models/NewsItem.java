package donnews.ru.donnews.Models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by antonnikitin on 27.03.17.
 */

public class NewsItem implements Serializable, Comparable<NewsItem>{

    @Expose private String category;
    @Expose private String lead;
    @Expose private String title;
    @Expose private String text_full;
    @Expose private String image_full;
    @Expose private String author_name;
    @Expose private String alias;
    @Expose private int comments_count;
    @Expose private String post;
    @Expose public ArrayList<CommentItem> comments;
    @Expose private int image_width;
    @Expose private int image_height;
    @Expose private String date;
    @Expose private String expert_post;
    @Expose private String expert_name;

    public String getExpert_post() {
        return expert_post;
    }

    public void setExpert_post(String expert_post) {
        this.expert_post = expert_post;
    }

    public String getExpert_name() {
        return expert_name;
    }

    public void setExpert_name(String expert_name) {
        this.expert_name = expert_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Expose private  String category_name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text_full;
    }

    public void setText(String text) {
        this.text_full = text;
    }

    public String getImage() {
        return image_full;
    }

    public void setImage(String image) {
        this.image_full = image;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int compareTo(@NonNull NewsItem newsItem) {
        return getDate().compareTo(newsItem.getDate());
    }
}
