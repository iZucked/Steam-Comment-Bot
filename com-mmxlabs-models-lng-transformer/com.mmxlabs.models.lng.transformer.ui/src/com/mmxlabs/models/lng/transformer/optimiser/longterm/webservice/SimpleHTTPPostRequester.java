/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.longterm.webservice;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
 
/**
 *
 */
public class SimpleHTTPPostRequester {
	ObjectMapper mapper = new ObjectMapper();

    public SimpleHTTPPostRequester() {        
    }
 
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

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = null;
        try {
            StringEntity stringEntity = new StringEntity(jsonRequest);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "application/json");
            responseBody = httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            throw e;
        }
        return responseBody;
    }}
