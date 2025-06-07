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
import org.acme.model.Screener;

import org.acme.repository.utils.FirebaseUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ScreenerRepositoryImpl implements ScreenerRepository {

    @Override
    public Optional<Screener> getScreenerByName(String name) {

        Optional<Map<String, Object>> dataOptional = FirebaseUtils.getFirestoreDocByUniqueField(
                CollectionNames.SCREENER_COLLECTION,
                FieldNames.SCREENER_NAME,
                name);

        if (dataOptional.isEmpty()){
            return Optional.empty();
        }

        Map<String, Object> data = dataOptional.get();

        Optional<Boolean> isPublishedOptional = FirebaseUtils.getOptionalField(data, FieldNames.IS_PUBLISHED, Boolean.class);
        Optional<String> formPathOptional = FirebaseUtils.getOptionalField(data, FieldNames.FORM_PATH, String.class);

        Screener screener = new Screener();

        isPublishedOptional.ifPresent(screener::setPublished);

        if (formPathOptional.isPresent()){
            String formPath = formPathOptional.get();
            Map<String, Object> formModel = getFormSchemaFromStorage(formPath);
            screener.setFormModel(formModel);
        }
        return Optional.of(screener);
    }

    @Override
    public Optional<InputStream> getScreenerDmnModelByName(String name) {

        Optional<Map<String, Object>> dataOptional = FirebaseUtils.getFirestoreDocByUniqueField(
                CollectionNames.SCREENER_COLLECTION,
                FieldNames.SCREENER_NAME,
                name);

        if (dataOptional.isEmpty()){
            return Optional.empty();
        }

        Map<String, Object> data = dataOptional.get();

        Optional<Boolean> isPublishedOptional = FirebaseUtils.getOptionalField(data, FieldNames.IS_PUBLISHED, Boolean.class);
        Optional<String> dmnPathOptional = FirebaseUtils.getOptionalField(data, FieldNames.DMN_PATH, String.class);

        if (dmnPathOptional.isEmpty() || isPublishedOptional.isEmpty() || !isPublishedOptional.get()) {
            return Optional.empty();
        }

        String dmnPath = dmnPathOptional.get();

        return FirebaseUtils.getFileFromStorage(dmnPath);
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
            Map<String, Object> formModel = mapper.readValue(new ByteArrayInputStream(content), new TypeReference<Map<String, Object>>() {
            });

            return formModel;

        } catch (Exception e){
            Log.error("Error fetching form model from firebase storage: ", e);
            return new HashMap<>();
        }
    }
}

