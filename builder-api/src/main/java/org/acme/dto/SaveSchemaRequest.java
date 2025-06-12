package org.acme.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class SaveSchemaRequest {
    public String screenerId;
    public JsonNode schema;
}
