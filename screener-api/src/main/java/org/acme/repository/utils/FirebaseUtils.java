package org.acme.repository.utils;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import io.quarkus.logging.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FirebaseUtils {

    // If unique field is not unique, this function returns the first of the matched results
    public static Optional<Map<String, Object>> getFirestoreDocByUniqueField(String collection, String field, String value) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> query = db.collection(collection)
                    .whereEqualTo(field, value)
                    .limit(1)
                    .get();
            List<QueryDocumentSnapshot> documents;
            documents = query.get().getDocuments();

            if (documents.isEmpty()) {
                return Optional.empty();
            }
            QueryDocumentSnapshot document = documents.getFirst();

            return Optional.of(document.getData());

        }catch(Exception e){
            Log.error("Error fetching document from firestore: ", e);
            return Optional.empty();
        }
    }

    public static <T> Optional<T> getOptionalField(Map<String, Object> doc, String fieldName, Class<T> clazz) {
        Object value = doc.get(fieldName);
        if (clazz.isInstance(value)) {
            return Optional.of((T) value);
        } else {
            return Optional.empty();
        }
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
}
