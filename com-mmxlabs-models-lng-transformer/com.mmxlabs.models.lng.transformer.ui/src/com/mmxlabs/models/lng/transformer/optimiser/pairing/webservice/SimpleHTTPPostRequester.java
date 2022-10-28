/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing.webservice;

import java.io.IOException;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
 
/**
 *
 */
public class SimpleHTTPPostRequester {
	private static final boolean DEBUG = false;
	private ObjectMapper mapper = new ObjectMapper();


    /**
     * Performs HTTP post.
     * @param url http resource to post
     * @return string response
     * @throws IOException 
     */
    protected String post(String url, Map<String, Object> map) throws IOException {
    	String jsonRequest = null;
    	try {
			jsonRequest = mapper.writeValueAsString(map);

		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			throw new IOException();
		}

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpPost httpPost = new HttpPost(url);
			String responseBody = null;

			StringEntity stringEntity = new StringEntity(jsonRequest);
			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Content-type", "application/json");
			return httpClient.execute(httpPost, response -> response.getEntity().toString());
		}
	}
}