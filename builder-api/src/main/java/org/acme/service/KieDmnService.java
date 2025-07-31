package org.acme.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.acme.model.domain.Screener;
import org.acme.persistence.StorageService;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.dmn.api.core.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import org.drools.compiler.kie.builder.impl.InternalKieModule;

@ApplicationScoped
public class KieDmnService implements DmnService {

    @Inject
    private StorageService storageService;

    public Map<String, Object> evaluateDecision(Screener screener, Map<String, Object> inputs) throws IOException {

        String filePath = storageService.getWorkingCompiledDmnModelPath(screener.getId());
        Optional<byte[]> dmnDataOpt = storageService.getFileBytesFromStorage(filePath);


        if (dmnDataOpt.isEmpty()){
            throw new NotFoundException();
        }

        byte[] dmnModuleData = dmnDataOpt.get();

        KieSession kieSession = initializeKieSession(dmnModuleData);
        DMNRuntime dmnRuntime = kieSession.getKieRuntime(DMNRuntime.class);

        try {
            DMNModel dmnModel = dmnRuntime.getModel(screener.getWorkingDmnNameSpace(), screener.getWorkingDmnName());

            DMNContext context = dmnRuntime.newContext();
            for (String key : inputs.keySet()) {
                context.set(key, inputs.get(key));
            }

            DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, context);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("inputs", inputs);


            List<Map<String, Object>> decisions = new ArrayList<>();
            for (DMNDecisionResult decisionResult :  dmnResult.getDecisionResults()) {
                Map<String, Object> decisionDetail = new LinkedHashMap<>();
                decisionDetail.put("decisionName", decisionResult.getDecisionName());
                decisionDetail.put("result", decisionResult.getResult());
                decisionDetail.put("status", decisionResult.getEvaluationStatus().toString());

                decisions.add(decisionDetail);
            }
            response.put("decisions", decisions);

            if (!dmnResult.getMessages().isEmpty()) {
                response.put("messages", dmnResult.getMessages().stream()
                        .map(DMNMessage::getMessage).collect(Collectors.toList()));
            }

            kieSession.dispose();
            return response;
        }
        catch (Exception e){
            return new HashMap<>();
        } finally{
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
    }

    private KieSession initializeKieSession(byte[] moduleBytes) throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        Resource jarResource = kieServices.getResources().newByteArrayResource(moduleBytes);
        KieModule kieModule = kieServices.getRepository().addKieModule(jarResource);

        ReleaseId releaseId = kieModule.getReleaseId();
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        return kieContainer.newKieSession();
    }

    public String compilePublishedDmnModel(String screenerId) throws Exception {
        Optional<String> optDmnXml = getWorkingDmnXml(screenerId);
        if (optDmnXml.isEmpty()) {
            throw new Exception("Working Dmn not found for screener: " + screenerId);
        }
        String dmnXml = optDmnXml.get();
        byte[] serializedModel = compileDmnModel(dmnXml, new HashMap<>(), screenerId);
        String filPath = storageService.getPublishedCompiledDmnModelPath(screenerId);
        storageService.writeBytesToStorage(filPath, serializedModel, "application/java-archive");
        Log.info("Saved compiled published dmn for model " + screenerId + " to storage.");
        return dmnXml;
    }

    public String compileWorkingDmnModel(String screenerId) throws Exception {
        Optional<String> optDmnXml = getWorkingDmnXml(screenerId);
        if (optDmnXml.isEmpty()) {
            throw new Exception("Working Dmn not found for screener: " + screenerId);
        }
        String dmnXml = optDmnXml.get();
        byte[] serializedModel = compileDmnModel(dmnXml, new HashMap<>(), screenerId);
        String filPath = storageService.getWorkingCompiledDmnModelPath(screenerId);
        storageService.writeBytesToStorage(filPath, serializedModel, "application/java-archive");
        Log.info("Saved compiled working dmn for model " + screenerId + " to storage.");
        return dmnXml;
    }

    private byte[] compileDmnModel(String dmnXml, Map<String, String> importedModels, String modelId) throws IOException {
        Log.info("Compiling and saving DMN model: " + modelId);

        KieServices kieServices = KieServices.Factory.get();
        // 1. Compile the DMN XML into a KieBase
        KieFileSystem kfs = kieServices.newKieFileSystem();
        // Use a unique ReleaseId for each compilation if you plan to update models
        // For production, consider versioning the ReleaseId carefully.
        ReleaseId releaseId = kieServices.newReleaseId("user-model", modelId, "1.0.0");
        kfs.write("src/main/resources/model.dmn", dmnXml);


        String kmoduleXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kmodule xmlns=\"http://www.drools.org/xsd/kmodule\">\n" +
                "  <kbase name=\"dmn-kbase\" default=\"true\">\n" +
                "    <ksession name=\"dmn-ksession\" default=\"true\"/>\n" +
                "  </kbase>\n" +
                "</kmodule>";

        kfs.write("src/main/resources/META-INF/kmodule.xml", kmoduleXml);

        // Write all imported DMN models
        for (Map.Entry<String, String> entry : importedModels.entrySet()) {
            // Ensure the path starts with "src/main/resources/" to be picked up by KieBuilder
            String resourcePath = entry.getKey();
            if (!resourcePath.startsWith("src/main/resources/")) {
                resourcePath = "src/main/resources/" + resourcePath;
            }
            kfs.write(resourcePath, entry.getValue());
            Log.info("Added imported DMN model to KieFileSystem: " + resourcePath);
        }
        kfs.generateAndWritePomXML(releaseId);

        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
        kieBuilder.buildAll();
        Results results = kieBuilder.getResults();

        if (results.hasMessages(Message.Level.ERROR)) {
            Log.error("DMN Compilation errors for model " + modelId + ":");
            for (Message message : results.getMessages(Message.Level.ERROR)) {
                Log.error(message.getText());
            }
            throw new IllegalStateException("DMN Model compilation failed for model: " + modelId);
        }

        InternalKieModule kieModule = (InternalKieModule) kieBuilder.getKieModule();
        byte[] kieModuleBytes = kieModule.getBytes();

        Log.info("Serialized kieModule for model " + modelId);
        return kieModuleBytes;
    }

    private Optional<String> getWorkingDmnXml(String screenerId) {
        String filePath = storageService.getScreenerWorkingDmnModelPath(screenerId);
        Optional<String> dmnXml = storageService.getStringFromStorage(filePath);
        if (dmnXml.isEmpty()){
            throw new RuntimeException("working DMN file not found");
        }
        return dmnXml;
    }
}
