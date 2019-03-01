package com.crescentek.cryptocurrency.model;

/**
 * Created by R.Android on 14-06-2018.
 */

public class BannerImage {


    private String bannerimageurl="";
    private String business_id="";
    private int imageId;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getBannerimageurl() {
        return bannerimageurl;
    }

    public void setBannerimageurl(String bannerimageurl) {
        this.bannerimageurl = bannerimageurl;
    }

}
