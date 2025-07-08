package org.acme.persistence;

import org.acme.model.DmnModel;

import java.util.List;

public interface DmnModelRepository {

    public List<DmnModel> getAllDmnModels();
}
