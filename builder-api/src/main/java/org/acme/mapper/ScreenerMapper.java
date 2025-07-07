package org.acme.mapper;

import org.acme.constants.FieldNames;
import org.acme.model.Screener;

import java.util.HashMap;
import java.util.Map;

public class ScreenerMapper {

    public static Screener fromMap(Map<String, Object> map){
        Screener screener = new Screener();

        if (doesAttributeExistOfType(map, FieldNames.ID, String.class)){
            screener.setId((String) map.get(FieldNames.ID));
        }
        if (doesAttributeExistOfType(map, FieldNames.OWNER_ID, String.class)){
            screener.setOwnerId((String) map.get(FieldNames.OWNER_ID));
        }
        if (doesAttributeExistOfType(map, FieldNames.SCREENER_NAME, String.class)){
            screener.setScreenerName((String) map.get(FieldNames.SCREENER_NAME));
        }

        if (doesAttributeExistOfType(map, FieldNames.LAST_PUBLISHED_DATE, String.class)){
            screener.setLastPublishDate((String) map.get(FieldNames.LAST_PUBLISHED_DATE));
        }
        if (doesAttributeExistOfType(map, FieldNames.IS_PUBLISHED, Boolean.class)){
            screener.setIsPublished((Boolean) map.get(FieldNames.IS_PUBLISHED));
        }
        if (doesAttributeExistOfType(map, FieldNames.WORKING_DMN_NAME, String.class)){
            screener.setWorkingDmnName((String) map.get(FieldNames.WORKING_DMN_NAME));
        }
        if (doesAttributeExistOfType(map, FieldNames.WORKING_DMN_NAMESPACE, String.class)){
            screener.setWorkingDmnNameSpace((String) map.get(FieldNames.WORKING_DMN_NAMESPACE));
        }
        if (doesAttributeExistOfType(map, FieldNames.PUBLISHED_DMN_NAME, String.class)){
            screener.setPublishedDmnName((String) map.get(FieldNames.PUBLISHED_DMN_NAME));
        }
        if (doesAttributeExistOfType(map, FieldNames.PUBLISHED_DMN_NAMESPACE, String.class)){
            screener.setPublishedDmnNameSpace((String) map.get(FieldNames.PUBLISHED_DMN_NAMESPACE));
        }
        if (doesAttributeExistOfType(map, FieldNames.LAST_DMN_SAVE, String.class)){
            screener.setLastDmnSave((String) map.get(FieldNames.LAST_DMN_SAVE));
        }
        if (doesAttributeExistOfType(map, FieldNames.LAST_DMN_COMPILE, String.class)){
            screener.setLastDmnCompile((String) map.get(FieldNames.LAST_DMN_COMPILE));
        }

        return screener;
    }

    public static Map<String, Object> fromScreener(Screener screener){
        Map<String, Object> data = new HashMap<>();
        if (screener.getScreenerName() != null){
            data.put(FieldNames.SCREENER_NAME, screener.getScreenerName());
        }
        if (screener.getOwnerId() != null){
            data.put(FieldNames.OWNER_ID, screener.getOwnerId());
        }
        if (screener.isPublished() != null){
            data.put(FieldNames.IS_PUBLISHED, screener.isPublished());
        }
        if (screener.getOrganizationName() != null){
            data.put(FieldNames.ORGANIZATION_NAME, screener.getOrganizationName());
        }
        if (screener.getLastPublishDate() != null){
            data.put(FieldNames.LAST_PUBLISHED_DATE, screener.getLastPublishDate());
        }
        if (screener.getWorkingDmnName() !=null){
            data.put(FieldNames.WORKING_DMN_NAME, screener.getWorkingDmnName());
        }
        if (screener.getWorkingDmnNameSpace() !=null){
            data.put(FieldNames.WORKING_DMN_NAMESPACE, screener.getWorkingDmnNameSpace());
        }
        if (screener.getPublishedDmnName() !=null){
            data.put(FieldNames.PUBLISHED_DMN_NAME, screener.getPublishedDmnName());
        }
        if (screener.getPublishedDmnNameSpace() !=null){
            data.put(FieldNames.PUBLISHED_DMN_NAMESPACE, screener.getPublishedDmnNameSpace());
        }
        if (screener.getLastDmnSave()!=null){
            data.put(FieldNames.LAST_DMN_SAVE, screener.getLastDmnSave());
        }
        if (screener.getLastDmnCompile()!=null){
            data.put(FieldNames.LAST_DMN_COMPILE, screener.getLastDmnCompile());
        }

        return data;
    }

    private static boolean doesAttributeExistOfType(Map<String, Object> map, String attributeName, Class<?> expectedType) {
        return map.containsKey(attributeName) && expectedType.isInstance(map.get(attributeName));
    }
}
