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
        if (doesAttributeExistOfType(map, FieldNames.SCREENER_NAME, String.class)){
            screener.setScreenerName((String) map.get(FieldNames.SCREENER_NAME));
        }
        if (doesAttributeExistOfType(map, FieldNames.IS_PUBLISHED, Boolean.class)){
            screener.setIsPublished((Boolean) map.get(FieldNames.IS_PUBLISHED));
        }

        return screener;
    }

    public static Map<String, Object> fromScreener(Screener screener){
        Map<String, Object> data = new HashMap<>();
        if (screener.getScreenerName() != null){
            data.put(FieldNames.SCREENER_NAME, screener.getScreenerName());
        }
        if (screener.isPublished() != null){
            data.put(FieldNames.IS_PUBLISHED, screener.isPublished());
        }
        if (screener.getOrganizationName() != null){
            data.put(FieldNames.ORGANIZATION_NAME, screener.getOrganizationName());
        }
        return data;
    }

    private static boolean doesAttributeExistOfType(Map<String, Object> map, String attributeName, Class<?> expectedType) {
        return map.containsKey(attributeName) && expectedType.isInstance(map.get(attributeName));
    }
}
