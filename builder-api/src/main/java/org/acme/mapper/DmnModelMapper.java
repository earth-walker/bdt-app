package org.acme.mapper;

import org.acme.constants.FieldNames;
import org.acme.model.DmnModel;
import java.util.HashMap;
import java.util.Map;

public class DmnModelMapper {

    public static DmnModel fromMap(Map<String, Object> map) {
        DmnModel dmnModel = new DmnModel();

        if (doesAttributeExistOfType(map, FieldNames.ID, String.class)) {
            dmnModel.setId((String) map.get(FieldNames.ID));
        }
        if (doesAttributeExistOfType(map, FieldNames.GROUP_ID, String.class)) {
            dmnModel.setGroupId((String) map.get(FieldNames.GROUP_ID));
        }
        if (doesAttributeExistOfType(map, FieldNames.ARTIFACT_ID, String.class)) {
            dmnModel.setArtifactId((String) map.get(FieldNames.ARTIFACT_ID));
        }
        if (doesAttributeExistOfType(map, FieldNames.STORAGE_LOCATION, String.class)) {
            dmnModel.setStorageLocation((String) map.get(FieldNames.STORAGE_LOCATION));
        }

        return dmnModel;
    }

    public static Map<String, Object> fromDmnModel(DmnModel model) {
        Map<String, Object> data = new HashMap<>();
        if (model.getGroupId() != null) {
            data.put(FieldNames.GROUP_ID, model.getGroupId());
        }
        if (model.getArtifactId() != null) {
            data.put(FieldNames.ARTIFACT_ID, model.getArtifactId());
        }
        if (model.getVersion() != null) {
            data.put(FieldNames.VERSION, model.getVersion());
        }
        return data;
    }

    private static boolean doesAttributeExistOfType(Map<String, Object> map, String attributeName, Class<?> expectedType) {
        return map.containsKey(attributeName) && expectedType.isInstance(map.get(attributeName));
    }
}
