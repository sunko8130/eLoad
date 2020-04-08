package com.frontiertechnologypartners.mykyat_topup.model;

public class Language {

    private int imageLanguage;
    private String language;

    public Language(int imageLanguage, String language) {
        this.imageLanguage = imageLanguage;
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getImageLanguage() {
        return imageLanguage;
    }

    public void setImageLanguage(int imageLanguage) {
        this.imageLanguage = imageLanguage;
    }
}
