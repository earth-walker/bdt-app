package org.acme.model;


import java.util.Map;

public class Screener {
    private String id;
    private Map<String, Object> formSchema;
    private String dmnModel;
    private boolean isPublished;
    private String author;
    private String ownerId;
    private String screenerName;
    private String lastPublishDate;

    public Screener(Map<String, Object> model, boolean isPublished){
        this.formSchema = model;
        this.isPublished = isPublished;
    }

    public Screener(){
    }

    public Map<String, Object> getFormSchema() {
        return formSchema;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean isPublished){
        this.isPublished = isPublished;
    }

    public void setFormSchema(Map<String, Object> formSchema) {
        this.formSchema = formSchema;
    }

    public void setDmnModel(String dmnModel){
        this.dmnModel = dmnModel;
    }

    public void setOwnerId(String ownerId){
        this.ownerId = ownerId;
    }

    public String getOwnerId(){
        return this.ownerId;
    }

    public void setScreenerName(String screenerName){
        this.screenerName = screenerName;
    }

    public String getScreenerName(){
        return this.screenerName;
    }

    public void setLastPublishDate(String lastPublishDate){
        this.lastPublishDate = lastPublishDate;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public String getDmnModel(){
        return this.dmnModel;
    }
}
