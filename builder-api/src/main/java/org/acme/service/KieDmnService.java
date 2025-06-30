package org.acme.service;

import io.grpc.Context;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.acme.model.Screener;
import org.acme.repository.utils.StorageUtils;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.dmn.api.core.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import org.drools.compiler.kie.builder.impl.InternalKieModule;

@ApplicationScoped
public class KieDmnService implements DmnService {

//    public List<Map<String, Object>> evaluateDecision(InputStream inputStream, Map<String, Object> inputs) throws IOException {
//        String dmnXml = convertStreamToString(inputStream);
//        KieSession kieSession = initializeKieSession(dmnXml);
//        DMNRuntime dmnRuntime = kieSession.getKieRuntime(DMNRuntime.class);
//
//        try {
//            DmnParser dmnParser = new DmnParser(dmnXml);
//            String name = dmnParser.getName();
//            String nameSpace = dmnParser.getNameSpace();
//            DMNModel dmnModel = dmnRuntime.getModel(nameSpace, name);
//
//            DMNContext context = dmnRuntime.newContext();
//            for (String key : inputs.keySet()) {
//                context.set(key, inputs.get(key));
//
//            }
//            DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, context);
//           List<Map<String, Object>> results = new ArrayList<>();
//            for (DMNDecisionResult r :  dmnResult.getDecisionResults()){
//                Map<String, Object> result = new HashMap<>();
//                result.put(r.getDecisionName(), r.getResult());
//                results.add(result);
//            }
//
//            results.add(getErrorMessages(dmnResult));
//
//            kieSession.dispose();
//            return results;
//        }
//        catch (Exception e){
//            return new ArrayList<>();
//        } finally{
//            if (kieSession != null) {
//                kieSession.dispose();
//            }
//        }
//    }

//    public Map<String, Object> evaluateDecision(Screener screener, Map<String, Object> inputs) throws IOException {
//
//        String filePath = StorageUtils.getPublishedCompiledDmnModelPath(screener.getId());
//        Optional<byte[]> dmnDataOpt = StorageUtils.getFileBytesFromStorage(filePath);
//
//
//        if (dmnDataOpt.isEmpty()){
//            throw new NotFoundException();
//        }
//
//        byte[] dmnModuleData = dmnDataOpt.get();
//
//        KieSession kieSession = initializeKieSession(screener.getId(), dmnModuleData);
//        DMNRuntime dmnRuntime = kieSession.getKieRuntime(DMNRuntime.class);
//
//        try {
//            DMNModel dmnModel = dmnRuntime.getModel(screener.getPublishedDmnNameSpace(), screener.getPublishedDmnName());
//
//            DMNContext context = dmnRuntime.newContext();
//            for (String key : inputs.keySet()) {
//                context.set(key, inputs.get(key));
//            }
//
//            DMNResult dmnResult = dmnRuntime.evaluateAll(dmnModel, context);
//
//            Map<String, Object> response = new LinkedHashMap<>();
//            response.put("inputs", inputs);
//
//
//            List<Map<String, Object>> decisions = new ArrayList<>();
//            for (DMNDecisionResult decisionResult :  dmnResult.getDecisionResults()) {
//                Map<String, Object> decisionDetail = new LinkedHashMap<>();
//                decisionDetail.put("result", decisionResult.getResult());
//                decisionDetail.put("status", decisionResult.getEvaluationStatus().toString());
//
//
//                if (!decisionResult.getMessages().isEmpty()) {
//                    decisionDetail.put("messages", decisionResult.getMessages().stream()
//                            .map(DMNMessage::getMessage).collect(Collectors.toList()));
//                }
//            }
//
//
//
//            kieSession.dispose();
//            return response;
//        }
//        catch (Exception e){
//            return new HashMap<>();
//        } finally{
//            if (kieSession != null) {
//                kieSession.dispose();
//            }
//        }
//    }
//
//    private static Map<String, Object> getErrorMessages(DMNResult dmnResult) {
//        List<String> dmnMessages = dmnResult.getMessages().stream().map(m -> m.getText()).toList();
//        Map<String,Object> messages = new HashMap<>();
//        messages.put("Error Messages", dmnMessages);
//        return messages;
//    }
//
//    private KieSession initializeKieSession(String dmnXml) throws IOException {
//        KieSession kieSession;
//        KieServices kieServices = KieServices.Factory.get();
//        KieFileSystem kfs = kieServices.newKieFileSystem();
//        ReleaseId myReleaseId = kieServices.newReleaseId("org.myorg", "my-dmn-module", "1.0.0");
//        kfs.write("src/main/resources/model.dmn", dmnXml);
//        kfs.generateAndWritePomXML(myReleaseId);
//        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs);
//        kieBuilder.buildAll();
//        KieContainer kieContainer = kieServices.newKieContainer(myReleaseId);
//        kieSession = kieContainer.newKieSession();
//        return kieSession;
//    }
//
//    public String convertStreamToString(InputStream inputStream) throws IOException {
//        StringBuilder textBuilder = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                textBuilder.append(line).append("\n");
//            }
//        }
//        return textBuilder.toString();
//    }



    public Map<String, Object> evaluateDecision(Screener screener, Map<String, Object> inputs) throws IOException {

        String filePath = StorageUtils.getWorkingCompiledDmnModelPath(screener.getId());
        Optional<byte[]> dmnDataOpt = StorageUtils.getFileBytesFromStorage(filePath);


        if (dmnDataOpt.isEmpty()){
            throw new NotFoundException();
        }

        byte[] dmnModuleData = dmnDataOpt.get();

        KieSession kieSession = initializeKieSession(screener.getId(), dmnModuleData);
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

    private KieSession initializeKieSession(String screenerId, byte[] moduleBytes) throws IOException {
        KieServices kieServices = KieServices.Factory.get();
        Resource jarResource = kieServices.getResources().newByteArrayResource(moduleBytes);


        System.out.println("Loaded JAR size: " + moduleBytes.length);
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
        String filPath = StorageUtils.getPublishedCompiledDmnModelPath(screenerId);
        StorageUtils.writeBytesToStorage(filPath, serializedModel, "application/java-archive");
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
        String filPath = StorageUtils.getWorkingCompiledDmnModelPath(screenerId);
        StorageUtils.writeBytesToStorage(filPath, serializedModel, "application/java-archive");
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

    private static Optional<String> getWorkingDmnXml(String screenerId) {
        String filePath = StorageUtils.getScreenerWorkingDmnModelPath(screenerId);
        Optional<String> dmnXml = StorageUtils.getStringFromStorage(filePath);
        if (dmnXml.isEmpty()){
            throw new RuntimeException("working DMN file not found");
        }
        return dmnXml;
    }
}
