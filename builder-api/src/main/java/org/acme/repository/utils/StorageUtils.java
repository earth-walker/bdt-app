package org.acme.repository.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import io.quarkus.logging.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class StorageUtils {

    public static void writeStringToStorage(String filePath, String content, String contentType){
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            InputStream inputSteam = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            Blob blob = bucket.create(filePath, inputSteam, contentType);
            Log.info("Uploaded to GCS: " + blob.getName());
        } catch (Exception e){
            Log.error("Error writing file to GCS: " + e.getMessage());
        }
    }

    public static void writeBytesToStorage(String filePath, byte[] content, String contentType){
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            InputStream inputSteam = new ByteArrayInputStream(content);
            Blob blob = bucket.create(filePath, inputSteam, contentType);
            Log.info("Uploaded to GCS: " + blob.getName());
        } catch (Exception e){
            Log.error("Error writing file to GCS: " + e.getMessage());
        }
    }

    public static void writeJsonToStorage(String filePath, JsonNode json){
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputSteam = new ByteArrayInputStream(mapper.writeValueAsBytes(json));
            Blob blob = bucket.create(filePath, inputSteam, "application/json");
            Log.info("Uploaded to GCS: " + blob.getName());
        } catch (Exception e){
            Log.error("Error writing file to GCS: " + e.getMessage());
        }
    }

    public static Optional<InputStream> getFileInputStreamFromStorage(String filePath) {
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


    public static Optional<String> getStringFromStorage(String filePath) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(filePath);

            if (blob == null || !blob.exists()) {
                return Optional.empty();
            }

            // Get content and convert to UTF-8 String
            byte[] data = blob.getContent(Blob.BlobSourceOption.generationMatch());
            String content = new String(data, StandardCharsets.UTF_8);

            return Optional.of(content);

        } catch (Exception e) {
            Log.error("Error fetching XML file from Firebase Storage: ", e);
            return Optional.empty();
        }
    }

    public static Optional<byte[]> getFileBytesFromStorage(String filePath) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(filePath);

            if (blob == null || !blob.exists()) {
                return Optional.empty();
            }

            byte[] data = blob.getContent();

            return Optional.of(data);

        } catch (Exception e){
            Log.error("Error fetching file from firebase storage: ", e);
            return Optional.empty();
        }
    }
    public static String getScreenerWorkingDmnModelPath(String screenerId){
        return "dmn/working/" + screenerId + ".dmn";
    }

    public static String getScreenerWorkingFormSchemaPath(String screenerId){
        return "form/working/" + screenerId + ".json";
    }

    public static String getScreenerPublishedDmnModelPath(String screenerId){
        return "dmn/published/" + screenerId + ".dmn";
    }

    public static String getScreenerPublishedFormSchemaPath(String screenerId){
        return "form/published/" + screenerId + ".json";
    }

    public static String getPublishedCompiledDmnModelPath(String screenerId){
        return "compiled_dmn_models/published/" + screenerId + "/kiebase.ser";
    }

    public static String getWorkingCompiledDmnModelPath(String screenerId){
        return "compiled_dmn_models/working/" + screenerId + "/kiebase.ser";
    }
    public static Map<String, Object> getFormSchemaFromStorage(String filePath) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.get(filePath);

            if (blob == null || !blob.exists()) {
               return null;
            }

            byte[] content = blob.getContent();


            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> formSchema = mapper.readValue(new ByteArrayInputStream(content), new TypeReference<Map<String, Object>>() {
            });

            return formSchema;

        } catch (Exception e){
            Log.error("Error fetching form model from firebase storage: ", e);
            return null;
        }
    }

    public static void updatePublishedFormSchemaArtifact(String screenerId) throws Exception {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob workingFormBlob = bucket.get(getScreenerWorkingFormSchemaPath(screenerId));
//            Blob workingDmnBlob = bucket.get(getScreenerWorkingDmnModelPath(screenerId));
            CopyWriter formCopyWriter = workingFormBlob.copyTo(BlobId.of(bucket.getName(), getScreenerPublishedFormSchemaPath(screenerId)));
//            CopyWriter dmnCopyWriter = workingDmnBlob.copyTo(BlobId.of(bucket.getName(), getScreenerPublishedDmnModelPath(screenerId)));
            formCopyWriter.getResult();
//            dmnCopyWriter.getResult();
            Log.info("Working form schema copied to published artifact path for screener: " + screenerId);
        } catch (Exception e){
            Log.error("Error updating published form schema in cloud storage");
            throw new Exception("Error updating published form schema in cloud storage");
        }
    }
}
