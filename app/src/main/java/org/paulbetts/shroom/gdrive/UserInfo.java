package org.paulbetts.shroom.gdrive;

import com.google.gson.annotations.Expose;

public class UserInfo {
    @Expose
    private String kind;
    @Expose
    private String etag;
    @Expose
    private String selfLink;
    @Expose
    private String name;
    @Expose
    private User user;
    @Expose
    private String quotaBytesTotal;
    @Expose
    private String quotaBytesUsed;
    @Expose
    private String quotaBytesUsedAggregate;
    @Expose
    private String quotaBytesUsedInTrash;
    @Expose
    private String quotaType;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuotaBytesTotal() {
        return quotaBytesTotal;
    }

    public void setQuotaBytesTotal(String quotaBytesTotal) {
        this.quotaBytesTotal = quotaBytesTotal;
    }

    public String getQuotaBytesUsed() {
        return quotaBytesUsed;
    }

    public void setQuotaBytesUsed(String quotaBytesUsed) {
        this.quotaBytesUsed = quotaBytesUsed;
    }

    public String getQuotaBytesUsedAggregate() {
        return quotaBytesUsedAggregate;
    }

    public void setQuotaBytesUsedAggregate(String quotaBytesUsedAggregate) {
        this.quotaBytesUsedAggregate = quotaBytesUsedAggregate;
    }

    public String getQuotaBytesUsedInTrash() {
        return quotaBytesUsedInTrash;
    }

    public void setQuotaBytesUsedInTrash(String quotaBytesUsedInTrash) {
        this.quotaBytesUsedInTrash = quotaBytesUsedInTrash;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }
}
