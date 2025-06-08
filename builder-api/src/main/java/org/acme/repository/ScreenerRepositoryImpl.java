package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.constants.CollectionNames;
import org.acme.constants.FieldNames;
import org.acme.mapper.ScreenerMapper;
import org.acme.model.Screener;

import org.acme.repository.utils.FirestoreUtils;
import org.acme.repository.utils.StorageUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class ScreenerRepositoryImpl implements ScreenerRepository {

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

        if (doesAttributeExistAndOfType(data, FieldNames.WORKING_FORM_PATH, String.class)){
            Map<String, Object>  formSchema = StorageUtils.getFormSchemaFromStorage((String) data.get(FieldNames.WORKING_FORM_PATH));
            screener.setFormSchema(formSchema);
        }

        if (doesAttributeExistAndOfType(data, FieldNames.WORKING_DMN_PATH, String.class)){
            Optional<String> dmnModel = FirestoreUtils.getFileAsStringFromStorage((String) data.get(FieldNames.WORKING_DMN_PATH));
            dmnModel.ifPresent(screener::setDmnModel);
        }

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
}

