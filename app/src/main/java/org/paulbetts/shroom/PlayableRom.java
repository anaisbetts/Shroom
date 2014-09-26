package org.paulbetts.shroom;

import org.paulbetts.shroom.models.RomInfo;

import java.util.Date;

public class PlayableRom {
    public PlayableRom(RomInfo cloudRomInfo) {
        this.cloudRomInfo = cloudRomInfo;
        this.createdAt = new Date();
        this.lastPlayedAt = new Date(0);
        this.isStarred = false;
    }

    public PlayableRom(Date createdAt, Date lastPlayedAt, boolean isStarred, RomInfo cloudRomInfo) {
        this.createdAt = createdAt;
        this.lastPlayedAt = lastPlayedAt;
        this.isStarred = isStarred;
        this.cloudRomInfo = cloudRomInfo;
    }

    Long _id;
    public Long getId() { return _id; }

    Date createdAt;
    public Date getCreatedAt() {
        return createdAt;
    }

    Date lastPlayedAt;
    public Date getLastPlayedAt() {
        return lastPlayedAt;
    }

    boolean isStarred;
    public boolean isStarred() {
        return isStarred;
    }
    public void setStarred(boolean isStarred) { this.isStarred = isStarred; }

    RomInfo cloudRomInfo;
    public RomInfo getCloudRomInfo() { return cloudRomInfo; }
}
