package com.vpaliy.loginconcept;

/**
 * Created by slytherin on 22/10/18.
 */

public class Places {
    private Integer mid;
    private Integer mtype;
    private Double mrating;
    private String mname;

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getMid() {

        return mid;
    }

    public Places(Integer mtype, Double mrating, String mname, Integer mid) {
        this.mtype = mtype;
        this.mrating = mrating;
        this.mname = mname;
        this.mid=mid;

    }

    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    public void setMrating(Double mrating) {
        this.mrating = mrating;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public Integer getMtype() {

        return mtype;
    }

    public Double getMrating() {
        return mrating;
    }

    public String getMname() {
        return mname;
    }

    public Places() {
    }
}
