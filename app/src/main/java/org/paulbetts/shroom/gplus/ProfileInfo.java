package org.paulbetts.shroom.gplus;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class ProfileInfo {

    @Expose
    private String kind;
    @Expose
    private String etag;
    @Expose
    private String occupation;
    @Expose
    private String gender;
    @Expose
    private List<Email> emails = new ArrayList<Email>();
    @Expose
    private List<Url> urls = new ArrayList<Url>();
    @Expose
    private String objectType;
    @Expose
    private String id;
    @Expose
    private String displayName;
    @Expose
    private Name name;
    @Expose
    private String tagline;
    @Expose
    private String aboutMe;
    @Expose
    private String url;
    @Expose
    private Image image;
    @Expose
    private List<Organization> organizations = new ArrayList<Organization>();
    @Expose
    private List<PlacesLived> placesLived = new ArrayList<PlacesLived>();
    @Expose
    private Boolean isPlusUser;
    @Expose
    private Integer circledByCount;
    @Expose
    private Boolean verified;

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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public List<PlacesLived> getPlacesLived() {
        return placesLived;
    }

    public void setPlacesLived(List<PlacesLived> placesLived) {
        this.placesLived = placesLived;
    }

    public Boolean getIsPlusUser() {
        return isPlusUser;
    }

    public void setIsPlusUser(Boolean isPlusUser) {
        this.isPlusUser = isPlusUser;
    }

    public Integer getCircledByCount() {
        return circledByCount;
    }

    public void setCircledByCount(Integer circledByCount) {
        this.circledByCount = circledByCount;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

}
