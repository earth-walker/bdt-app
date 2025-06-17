package org.acme.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface DmnService {
    public List<Map<String, Object>> evaluateDecision(InputStream inputStream, Map<String, Object> inputs) throws IOException;
}
