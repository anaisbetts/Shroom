package org.paulbetts.shroom.gdrive;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class DriveItem {

    @Expose
    private String kind;
    @Expose
    private String id;
    @Expose
    private String etag;
    @Expose
    private String selfLink;
    @Expose
    private String webContentLink;
    @Expose
    private String alternateLink;
    @Expose
    private String iconLink;
    @Expose
    private String title;
    @Expose
    private String mimeType;
    @Expose
    private Labels labels;
    @Expose
    private String createdDate;
    @Expose
    private String modifiedDate;
    @Expose
    private String modifiedByMeDate;
    @Expose
    private String version;
    @Expose
    private List<Parent> parents = new ArrayList<Parent>();
    @Expose
    private String downloadUrl;
    @Expose
    private UserPermission userPermission;
    @Expose
    private String originalFilename;
    @Expose
    private String fileExtension;
    @Expose
    private String md5Checksum;
    @Expose
    private String fileSize;
    @Expose
    private String quotaBytesUsed;
    @Expose
    private List<String> ownerNames = new ArrayList<String>();
    @Expose
    private List<Owner> owners = new ArrayList<Owner>();
    @Expose
    private String lastModifyingUserName;
    @Expose
    private LastModifyingUser lastModifyingUser;
    @Expose
    private Boolean editable;
    @Expose
    private Boolean copyable;
    @Expose
    private Boolean writersCanShare;
    @Expose
    private Boolean shared;
    @Expose
    private Boolean appDataContents;
    @Expose
    private String headRevisionId;

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

    public String getWebContentLink() {
        return webContentLink;
    }

    public void setWebContentLink(String webContentLink) {
        this.webContentLink = webContentLink;
    }

    public String getAlternateLink() {
        return alternateLink;
    }

    public void setAlternateLink(String alternateLink) {
        this.alternateLink = alternateLink;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedByMeDate() {
        return modifiedByMeDate;
    }

    public void setModifiedByMeDate(String modifiedByMeDate) {
        this.modifiedByMeDate = modifiedByMeDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public UserPermission getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermission userPermission) {
        this.userPermission = userPermission;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getMd5Checksum() {
        return md5Checksum;
    }

    public void setMd5Checksum(String md5Checksum) {
        this.md5Checksum = md5Checksum;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getQuotaBytesUsed() {
        return quotaBytesUsed;
    }

    public void setQuotaBytesUsed(String quotaBytesUsed) {
        this.quotaBytesUsed = quotaBytesUsed;
    }

    public List<String> getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(List<String> ownerNames) {
        this.ownerNames = ownerNames;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public String getLastModifyingUserName() {
        return lastModifyingUserName;
    }

    public void setLastModifyingUserName(String lastModifyingUserName) {
        this.lastModifyingUserName = lastModifyingUserName;
    }

    public LastModifyingUser getLastModifyingUser() {
        return lastModifyingUser;
    }

    public void setLastModifyingUser(LastModifyingUser lastModifyingUser) {
        this.lastModifyingUser = lastModifyingUser;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getCopyable() {
        return copyable;
    }

    public void setCopyable(Boolean copyable) {
        this.copyable = copyable;
    }

    public Boolean getWritersCanShare() {
        return writersCanShare;
    }

    public void setWritersCanShare(Boolean writersCanShare) {
        this.writersCanShare = writersCanShare;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getAppDataContents() {
        return appDataContents;
    }

    public void setAppDataContents(Boolean appDataContents) {
        this.appDataContents = appDataContents;
    }

    public String getHeadRevisionId() {
        return headRevisionId;
    }

    public void setHeadRevisionId(String headRevisionId) {
        this.headRevisionId = headRevisionId;
    }

}
