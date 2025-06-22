package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.dmn.api.core.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@ApplicationScoped
public class KieDmnService implements DmnService {
    public List<Map<String, Object>> evaluateDecision(InputStream inputStream, Map<String, Object> inputs) throws IOException {
        String dmnXml = convertStreamToString(inputStream);
        KieSession kieSession = initializeKieSession(dmnXml);
        DMNRuntime dmnRuntime = kieSession.getKieRuntime(DMNRuntime.class);

        try {
            DmnParser dmnParser = new DmnParser(dmnXml);
            String name = dmnParser.getName();
            String nameSpace = dmnParser.getNameSpace();
            DMNModel dmnModel = dmnRuntime.getModel(nameSpace, name);

            DMNContext context = dmnRuntime.newContext();
            for (String key : inputs.keySet()) {
                context.set(key, inputs.get(key));

            }
            DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, context);
            List<Map<String, Object>> results = new ArrayList<>();
            for (DMNDecisionResult r :  dmnResult.getDecisionResults()){
                Map<String, Object> result = new HashMap<>();
                result.put(r.getDecisionName(), r.getResult());
                results.add(result);

            }
            kieSession.dispose();
            return results;
        }
        catch (Exception e){
            return new ArrayList<>();
        } finally{
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
    }

    private KieSession initializeKieSession(String dmnXml) throws IOException {
        KieSession kieSession;
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
        ReleaseId myReleaseId = kieServices.newReleaseId("org.myorg", "my-dmn-module", "1.0.0");
        kfs.write("src/main/resources/model.dmn", dmnXml);
        kfs.generateAndWritePomXML(myReleaseId);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
        kieBuilder.buildAll();
        KieContainer kieContainer = kieServices.newKieContainer(myReleaseId);
        kieSession = kieContainer.newKieSession();
        return kieSession;
    }

    public String convertStreamToString(InputStream inputStream) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append("\n");
            }
        }
        return textBuilder.toString();
    }
}
