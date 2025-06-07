package org.acme.mapper;

import org.acme.constants.FieldNames;
import org.acme.model.Screener;

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
            screener.setPublished((Boolean) map.get(FieldNames.IS_PUBLISHED));
        }

        return screener;
    }

    private static boolean doesAttributeExistOfType(Map<String, Object> map, String attributeName, Class<?> expectedType) {
        return map.containsKey(attributeName) && expectedType.isInstance(map.get(attributeName));
    }
}
