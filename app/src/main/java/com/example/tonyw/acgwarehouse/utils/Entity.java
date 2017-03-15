package com.example.tonyw.acgwarehouse.utils;

import android.graphics.Bitmap;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class Entity {
    public static final int CATEGORY_ITEM_TYPE=0;
    protected static final int VIDEO_ITEM_TYPE=1;
    protected static final int COMIC_ITEM_TYPE=2;
    protected static final int NEWS_ITEM_TYPE=3;
    protected static final int DOWNLOADED_ITEM_TYPE=4;
    protected static final int DOWNLOADING_ITEM_TYPE=5;
    protected static final int COLLECT_ANIMATE_ITEM_TYPE=6;
    protected static final int COLLECT_COMIC_ITEM_TYPE=7;
    protected static final int COLLECT_NEWS_ITEM_TYPE=8;
    protected static final int RESOURCE_ITEM_TYPE=9;

    private String videoThumbPath;
    private Bitmap videoThumbBitmap;
    private String videoTitle;
    private String videoUrl;
    private String videoIntro;
    private int allVideoEpisode;
    private String categoryName;
    private int comicThumb;
    private String comicTitle;
    private int newEpisode;
    private String newsTitle;
    private Bitmap newsThumbBitmap;
    private String newsDate;
    private String newsSource;
    private String newsUrl;
    private int downloadedThumb;
    private String downloadedTitle;
    private long  downloadedSize;
    private String downloadedType;
    private int downloadingThumb;
    private String downloadingTitle;
    private int  downloadingProgress;
    private long downloadingSize;
    private int collectThumb;
    private String collectTitle;
    private int collectNewEpisode;
    private boolean collectIsUpdate;
    private int resourceThumb;
    private String resourceTitle;
    private int resourceEpisode;
    private int seriesEpisode;
    private String playUrl;

    private Format format=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);

    public Entity(String name)
    {
        categoryName=name;
    }

    public Entity(int episode,String url)
    {
        seriesEpisode=episode;
        playUrl=url;
    }

    public Entity(String path,String title)
    {
        videoThumbPath=path;
        videoTitle=title;
    }

    public Entity(Bitmap thumb,String title,String url,String intro,int allEpisode)
    {
        videoThumbBitmap=thumb;
        videoTitle=title;
        videoUrl=url;
        allVideoEpisode=allEpisode;
        videoIntro=intro;
    }

    public Entity(String title,Bitmap thumb,Date date,String source,String url)
    {
        newsTitle=title;
        newsThumbBitmap=thumb;
        newsDate=format.format(date);
        newsSource=source;
        newsUrl=url;
    }

    public Entity(String title,Bitmap thumb,Date date,String source)
    {
        newsTitle=title;
        newsThumbBitmap=thumb;
        newsDate=format.format(date);
        newsSource=source;
    }

    public Entity(int thumb,String title,int episode)
    {
        comicThumb=thumb;
        comicTitle=title;
        newEpisode=episode;
        resourceThumb=thumb;
        resourceTitle=title;
        resourceEpisode=episode;
    }

    public Entity(int thumb,String title,long size,String type)
    {
        downloadedThumb=thumb;
        downloadedTitle=title;
        downloadedSize=size;
        downloadedType=type;
    }

    public Entity(int thumb,String title,int progress,long size)
    {
        downloadingThumb=thumb;
        downloadingTitle=title;
        downloadingProgress=progress;
        downloadingSize=size;
    }

    public Entity(int thumb,String title,int episode,boolean isUpdate)
    {
        collectThumb=thumb;
        collectTitle=title;
        collectNewEpisode=episode;
        collectIsUpdate=isUpdate;
    }

    public Entity()
    {
    }

    public String getPlayUrl() {
        return playUrl;
    }


    public int getAllVideoEpisode() {
        return allVideoEpisode;
    }

    public void setAllVideoEpisode(int allVideoEpisode) {
        this.allVideoEpisode = allVideoEpisode;
    }

    public String getVideoIntro() {
        return videoIntro;
    }

    public void setVideoIntro(String videoIntro) {
        this.videoIntro = videoIntro;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public Bitmap getVideoThumbBitmap() {
        return videoThumbBitmap;
    }

    public void setVideoThumbBitmap(Bitmap videoThumbBitmap) {
        this.videoThumbBitmap = videoThumbBitmap;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public Bitmap getNewsThumbBitmap() {
        return newsThumbBitmap;
    }

    public void setNewsThumbBitmap(Bitmap newsThumbBitmap) {
        this.newsThumbBitmap = newsThumbBitmap;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public int getSeriesEpisode() {
        return seriesEpisode;
    }

    public abstract int getItemType();
}
