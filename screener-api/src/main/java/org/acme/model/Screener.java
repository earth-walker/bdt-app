package org.acme.model;


import java.util.HashMap;
import java.util.Map;

public class Screener {
    private String id;
    private Map<String, Object> formSchema;
    private Boolean isPublished;
    private String organizationName;
    private String screenerName;

    public Screener(Map<String, Object> model, boolean isPublished){
        this.formSchema = model;
        this.isPublished = isPublished;
    }

    public Screener(){
        this.formSchema = new HashMap<>();
        this.isPublished = false;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public String getScreenerName(){
        return this.screenerName;
    }

    public void setScreenerName(String screenerName){
        this.screenerName = screenerName;
    }

    public Map<String, Object> getFormSchema() {
        return formSchema;
    }

    public Boolean isPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished){
        this.isPublished = isPublished;
    }

    public void setFormSchema(Map<String, Object> formSchema) {
        this.formSchema = formSchema;
    }

    public String getOrganizationName(){
        return this.organizationName;
    }

    public void setOrganizationName(){
        this.organizationName = organizationName;
    }
}
