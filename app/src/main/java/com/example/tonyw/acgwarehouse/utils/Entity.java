package com.example.tonyw.acgwarehouse.utils;

import android.graphics.Bitmap;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.tonyw.acgwarehouse.R.id.videoThumb;

/**
 * Created by tonywu10 on 2016/12/2.
 */

public abstract class Entity {
    public static final int CATEGORY_ITEM_TYPE=0;
    public static final int VIDEO_ITEM_TYPE=1;
    public static final int COMIC_ITEM_TYPE=2;
    public static final int NEWS_ITEM_TYPE=3;
    public static final int DOWNLOADED_ITEM_TYPE=4;
    public static final int DOWNLOADING_ITEM_TYPE=5;
    public static final int COLLECT_ANIMATE_ITEM_TYPE=6;
    public static final int COLLECT_COMIC_ITEM_TYPE=7;
    public static final int COLLECT_NEWS_ITEM_TYPE=8;
    public static final int RESOURCE_ITEM_TYPE=9;
    public static final int SERIES_DETAIL_ITEM_TYPE=10;

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

    public Format format=new SimpleDateFormat("yyyy-MM-dd");

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

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
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

    public int getVideoThumb() {
        return videoThumb;
    }

    public String getVideoTitle() {
        return videoTitle;
    }


    public String getVideoThumbPath() {
        return videoThumbPath;
    }

    public void setVideoThumbPath(String videoThumbPath) {
        this.videoThumbPath = videoThumbPath;
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

    public int getComicThumb() {
        return comicThumb;
    }

    public String getComicTitle() {
        return comicTitle;
    }

    public int getNewEpisode() {
        return newEpisode;
    }

    public long getDownloadedSize() {
        return downloadedSize;
    }

    public int getDownloadedThumb() {
        return downloadedThumb;
    }

    public String getDownloadedTitle() {
        return downloadedTitle;
    }

    public String getDownloadedType() {
        return downloadedType;
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

    public int getDownloadingProgress() {
        return downloadingProgress;
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

    public long getDownloadingSize() {
        return downloadingSize;
    }

    public int getDownloadingThumb() {
        return downloadingThumb;
    }

    public String getDownloadingTitle() {
        return downloadingTitle;
    }

    public int getCollectNewEpisode() {
        return collectNewEpisode;
    }

    public String getCollectTitle() {
        return collectTitle;
    }

    public int getCollectThumb() {
        return collectThumb;
    }

    public boolean collectIsUpdate() {
        return collectIsUpdate;
    }

    public int getResourceEpisode() {
        return resourceEpisode;
    }

    public int getResourceThumb() {
        return resourceThumb;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public int getSeriesEpisode() {
        return seriesEpisode;
    }

    public abstract int getItemType();
}
