package org.acme.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.constants.CollectionNames;
import org.acme.constants.FieldNames;
import org.acme.mapper.ScreenerMapper;
import org.acme.model.Screener;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ScreenerRepositoryImpl implements ScreenerRepository {

    @Inject
    private StorageService storageService;

    @Override
    public List<Screener> getScreeners(String userId) {

        List<Map<String, Object>> screenersMaps = FirestoreUtils.getFirestoreDocsByField(
                CollectionNames.SCREENER_COLLECTION,
                FieldNames.OWNER_ID,
                userId);

        List<Screener> screeners = screenersMaps.stream().map(ScreenerMapper::fromMap).toList();

        return screeners;
    }

    @Override
    public Optional<Screener> getScreener(String screenerId){
        Optional<Map<String, Object>> dataOpt = FirestoreUtils.getFirestoreDocById(CollectionNames.SCREENER_COLLECTION, screenerId);
        if (dataOpt.isEmpty()){
            return Optional.empty();
        }
        Map<String, Object> data = dataOpt.get();
        Screener screener = ScreenerMapper.fromMap(data);

        String formPath = storageService.getScreenerWorkingFormSchemaPath(screenerId);
        Map<String, Object>  formSchema = storageService.getFormSchemaFromStorage(formPath);
        screener.setFormSchema(formSchema);

        String dmnPath = storageService.getScreenerWorkingDmnModelPath(screenerId);
        Optional<String> dmnModel = FirestoreUtils.getFileAsStringFromStorage(dmnPath);
        dmnModel.ifPresent(screener::setDmnModel);

        return Optional.of(screener);
    }

    @Override
    public Optional<Screener> getScreenerMetaDataOnly(String screenerId){
        Optional<Map<String, Object>> dataOpt = FirestoreUtils.getFirestoreDocById(CollectionNames.SCREENER_COLLECTION, screenerId);
        if (dataOpt.isEmpty()){
            return Optional.empty();
        }
        Map<String, Object> data = dataOpt.get();
        Screener screener = ScreenerMapper.fromMap(data);

        return Optional.of(screener);
    }


    private Boolean doesAttributeExistAndOfType(Map<String, Object> map, String key, Class<?> expectedClass){
        return map.containsKey(key) && expectedClass.isInstance(map.get(key));
    }


    @Override
    public String saveNewScreener(Screener screener) throws Exception{
        Map<String, Object> data = ScreenerMapper.fromScreener(screener);
        return FirestoreUtils.persistDocument(CollectionNames.SCREENER_COLLECTION, data);
    }

    @Override
    public void updateScreener(Screener screener) throws Exception {
        Map<String, Object> data = ScreenerMapper.fromScreener(screener);
        FirestoreUtils.updateDocument(CollectionNames.SCREENER_COLLECTION, data, screener.getId());
    }

    @Override
    public void deleteScreener(String screenerId) throws Exception {
        FirestoreUtils.deleteDocument(CollectionNames.SCREENER_COLLECTION, screenerId);
    }
}

