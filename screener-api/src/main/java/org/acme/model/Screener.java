package org.acme.model;


import java.util.HashMap;
import java.util.Map;

public class Screener {
    private Map<String, Object> formModel;
    private boolean isPublished;
    private String author;

    public Screener(Map<String, Object> model, boolean isPublished){
        this.formModel = model;
        this.isPublished = isPublished;
    }

    public Screener(){
        this.formModel = new HashMap<>();
        this.isPublished = false;
    }

    public Map<String, Object> getFormModel() {
        return formModel;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean isPublished){
        this.isPublished = isPublished;
    }

    public void setFormModel(Map<String, Object> formModel) {
        this.formModel = formModel;
    }
}
