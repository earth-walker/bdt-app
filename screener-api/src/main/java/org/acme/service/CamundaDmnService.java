package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class CamundaDmnService implements DmnService {
    public List<Map<String, Object>> evaluateDecision(InputStream inputStream, Map<String, Object> inputs) {
        // create a default DMN engine
        DmnEngine dmnEngine = DmnEngineConfiguration
                .createDefaultDmnEngineConfiguration()
                .buildEngine();

        // read a DMN model instance from a file
        DmnModelInstance dmnModelInstance = Dmn.readModelFromStream(inputStream);


        // parse the decisions
        List<DmnDecision> decisions = dmnEngine.parseDecisions(dmnModelInstance);

        DmnDecision decision = decisions.getFirst();
        VariableMap variables = Variables.createVariables();
        for (String key : inputs.keySet()) {
            variables = variables.putValue(key, inputs.get(key));

        }

        DmnDecisionResult results = dmnEngine.evaluateDecision(decision, variables);

        List<Map<String, Object>> result = results.getResultList();
        return result;
    }
}
