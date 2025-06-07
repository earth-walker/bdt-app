package org.acme.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.constants.CollectionNames;
import org.acme.constants.FieldNames;
import org.acme.mapper.ScreenerMapper;
import org.acme.model.Screener;

import org.acme.repository.utils.FirebaseUtils;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ScreenerRepositoryImpl implements ScreenerRepository {

    @Override
    public List<Screener> getScreeners(String userId) {

        List<Map<String, Object>> screenersMaps = FirebaseUtils.getFirestoreDocsByField(
                CollectionNames.SCREENER_COLLECTION,
                FieldNames.OWNER_ID,
                userId);

        List<Screener> screeners = screenersMaps.stream().map(ScreenerMapper::fromMap).toList();

        return screeners;
    }

    @Override
    public Optional<Screener> getScreener(String screenerId){
        Optional<Map<String, Object>> dataOpt = FirebaseUtils.getFirestoreDocById(CollectionNames.SCREENER_COLLECTION, screenerId);
        if (dataOpt.isEmpty()){
            return Optional.empty();
        }
        Map<String, Object> data = dataOpt.get();
        Screener screener = ScreenerMapper.fromMap(data);

        if (doesAttributeExistAndOfType(data, FieldNames.WORKING_FORM_PATH, String.class)){
            Map<String, Object>  formSchema = getFormSchemaFromStorage((String) data.get(FieldNames.WORKING_FORM_PATH));
            screener.setFormSchema(formSchema);
        }

        if (doesAttributeExistAndOfType(data, FieldNames.WORKING_DMN_PATH, String.class)){
            Optional<String> dmnModel = FirebaseUtils.getFileAsStringFromStorage((String) data.get(FieldNames.WORKING_DMN_PATH));
            dmnModel.ifPresent(screener::setDmnModel);
        }

        return Optional.of(screener);
    }

    private static Map<String, Object> getFormSchemaFromStorage(String filePath) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(filePath);

            if (blob == null || !blob.exists()) {
                throw new RuntimeException("File not found in Firebase Storage");
            }

            byte[] content = blob.getContent();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> formSchema = mapper.readValue(new ByteArrayInputStream(content), new TypeReference<Map<String, Object>>() {
            });

            return formSchema;

        } catch (Exception e){
            Log.error("Error fetching form model from firebase storage: ", e);
            return new HashMap<>();
        }
    }

    private Boolean doesAttributeExistAndOfType(Map<String, Object> map, String key, Class<?> expectedClass){
        return map.containsKey(key) && expectedClass.isInstance(map.get(key));
    }
}

