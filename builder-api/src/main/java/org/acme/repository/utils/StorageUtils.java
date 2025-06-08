package org.acme.repository.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.CopyWriter;
import com.google.firebase.cloud.StorageClient;
import io.quarkus.logging.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

    public static void updatePublishedScreenerArtifacts(String screenerId) throws Exception {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();
            Blob workingFormBlob = bucket.get(getScreenerWorkingFormSchemaPath(screenerId));
            Blob workingDmnBlob = bucket.get(getScreenerWorkingDmnModelPath(screenerId));
            CopyWriter formCopyWriter = workingFormBlob.copyTo(BlobId.of(bucket.getName(), getScreenerPublishedFormSchemaPath(screenerId)));
            CopyWriter dmnCopyWriter = workingDmnBlob.copyTo(BlobId.of(bucket.getName(), getScreenerPublishedDmnModelPath(screenerId)));
            formCopyWriter.getResult();
            dmnCopyWriter.getResult();
            Log.info("Working artifacts copied to published artifact paths for screener: " + screenerId);
        } catch (Exception e){
            Log.error("Error updating published artifacts in cloud storage");
            throw new Exception("Error updating published artifacts in cloud storage");
        }
    }
}
