package org.paulbetts.shroom.gplus;
import com.google.gson.annotations.Expose;

public class Name {

    @Expose
    private String familyName;
    @Expose
    private String givenName;

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
}
