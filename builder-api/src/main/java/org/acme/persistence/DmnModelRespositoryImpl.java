package org.acme.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.constants.CollectionNames;
import org.acme.mapper.DmnModelMapper;
import org.acme.model.DmnModel;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DmnModelRespositoryImpl implements DmnModelRepository{
    @Override
    public List<DmnModel> getAllDmnModels() {
        List<Map<String, Object>> dmnMaps = FirestoreUtils.getAllDocsInCollection(
                CollectionNames.DMN_MODEL_COLLECTION);

        return dmnMaps.stream().map(DmnModelMapper::fromMap).toList();
    }
}
