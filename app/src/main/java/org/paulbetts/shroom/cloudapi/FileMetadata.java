package org.paulbetts.shroom.cloudapi;

/**
 * Created by paul on 8/14/14.
 */
public interface FileMetadata {
    String getParentId();
    String getId();
    String getName();
    long getSize();
}
