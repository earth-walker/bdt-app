package org.acme.service;
import org.acme.model.domain.Screener;
import java.io.IOException;
import java.util.Map;

public interface DmnService {
    public Map<String, Object> evaluateDecision(Screener screener, Map<String, Object> inputs) throws IOException;
    public String compilePublishedDmnModel(String screenerId) throws Exception;
    public String compileWorkingDmnModel(String screenerId) throws Exception;
}
