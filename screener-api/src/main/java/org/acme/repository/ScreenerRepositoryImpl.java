package org.acme.repository;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.constants.CollectionNames;
import org.acme.mapper.ScreenerMapper;
import org.acme.model.Screener;

import org.acme.repository.utils.FirestoreUtils;
import org.acme.repository.utils.StorageUtils;

import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ScreenerRepositoryImpl implements ScreenerRepository {

    @Override
    public Optional<Screener> getScreener(String screenerId) {
        Log.info("here");
        Optional<Map<String, Object>> dataOpt = FirestoreUtils.getFirestoreDocById(CollectionNames.SCREENER_COLLECTION, screenerId);

        Log.info("here2");
        if (dataOpt.isEmpty()){
            return Optional.empty();
        }

        Log.info("here3");
        Map<String, Object> data = dataOpt.get();
        Screener screener = ScreenerMapper.fromMap(data);

        String formPath = StorageUtils.getScreenerPublishedFormSchemaPath(screenerId);
        Map<String, Object>  formSchema = StorageUtils.getFormSchemaFromStorage(formPath);
        screener.setFormSchema(formSchema);

        return Optional.of(screener);
    }
}

