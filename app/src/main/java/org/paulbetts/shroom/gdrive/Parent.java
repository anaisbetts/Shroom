package org.paulbetts.shroom.gdrive;

import com.google.gson.annotations.Expose;

public class Parent {

    @Expose
    private String kind;
    @Expose
    private String id;
    @Expose
    private String selfLink;
    @Expose
    private String parentLink;
    @Expose
    private Boolean isRoot;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getParentLink() {
        return parentLink;
    }

    public void setParentLink(String parentLink) {
        this.parentLink = parentLink;
    }

    public Boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean isRoot) {
        this.isRoot = isRoot;
    }

}
