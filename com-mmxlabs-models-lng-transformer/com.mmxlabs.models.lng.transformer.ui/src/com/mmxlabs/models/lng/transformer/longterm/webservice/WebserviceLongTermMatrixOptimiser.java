/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.transformer.longterm.ILongTermMatrixOptimiser;

public class WebserviceLongTermMatrixOptimiser implements ILongTermMatrixOptimiser {
	public static final String OPTIMISER_URL = "http://localhost:8080/api/optimise";
	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public boolean[][] findOptimalPairings(long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid) {
		SimpleHTTPPostRequester requester = new SimpleHTTPPostRequester();
		Map<String, Object> map = new HashMap<>();
		map.put("pnl", values);
		map.put("optionalLoads", optionalLoads);
		map.put("optionalDischarges", (optionalDischarges));
		map.put("restricted", (valid));
		
		boolean[][] pairings = null;

		try {
			String response = requester.post(OPTIMISER_URL, map);
			PairingsWebServiceResult parsedResult = mapper.readValue(response, PairingsWebServiceResult.class);
			if (parsedResult.isOptimised()) {
				pairings = parsedResult.getPairings();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return pairings;
	}

}
