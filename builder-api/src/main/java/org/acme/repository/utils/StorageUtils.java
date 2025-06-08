package org.acme.repository.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import io.quarkus.logging.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StorageUtils {

    public static void writeStringToStorage(String filePath, String content, String contentType){
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            InputStream inputSteam = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            Blob blob = bucket.create(filePath, inputSteam, "application/json");
            Log.info("Uploaded to GCS: " + blob.getName());
        } catch (Exception e){
            Log.error("Error writing file to GCS: " + e.getMessage());
        }
    }

    public static void writeStringToStorage(String filePath, String content){
        writeStringToStorage(filePath, content, "text/plain");
    }

    public static Optional<InputStream> getFileFromStorage(String filePath) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(filePath);

            if (blob == null || !blob.exists()) {
                return Optional.empty();
            }

            byte[] data = blob.getContent();

            return Optional.of(new ByteArrayInputStream(data));

        } catch (Exception e){
            Log.error("Error fetching file from firebase storage: ", e);
            return Optional.empty();
        }
    }

    public static String getScreenerWorkingDmnModelPath(String screenerId){
        return "dmn/working-" + screenerId + ".dmn";
    }

    public static String getScreenerWorkingFormSchemaPath(String screenerId){
        return "form/working-" + screenerId + ".json";
    }
    public static String getScreenerPublishedDmnModelPath(String screenerId){
        return "dmn/published-" + screenerId + ".dmn";
    }

    public static String getScreenerPublishedFormSchemaPath(String screenerId){
        return "dmn/published-" + screenerId + ".dmn";
    }

    public static Map<String, Object> getFormSchemaFromStorage(String filePath) {
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
}
