package com.mumu.realmadrid.model;

/**
 * Created by tsingsun on 15/8/18.
 */
public class LocationModel {

    private String dataProvide="";//数据提供者
    private String city="";
    private String lat="";//纬度
    private String lng="";//经度
    private String province="";//省份

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getDataProvide() {
        return dataProvide;
    }

    public void setDataProvide(String dataProvide) {
        this.dataProvide = dataProvide;
    }
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


}
