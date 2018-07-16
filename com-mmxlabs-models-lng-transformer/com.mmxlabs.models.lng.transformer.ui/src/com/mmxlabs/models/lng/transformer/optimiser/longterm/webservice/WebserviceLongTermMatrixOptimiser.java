/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.longterm.webservice;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.models.lng.transformer.optimiser.longterm.ILongTermMatrixOptimiser;

public class WebserviceLongTermMatrixOptimiser implements ILongTermMatrixOptimiser {
	public static final String OPTIMISER_URL = "http://localhost:8080/api/optimise";
	ObjectMapper mapper = new ObjectMapper();

	@Override
	public boolean[][] findOptimalPairings(long[][] values, boolean[] optionalLoads, boolean[] optionalDischarges, boolean[][] valid, List<Map<String, List<Integer>>> maxDischargeSlotsConstraints,
			List<Map<String, List<Integer>>> minDischargeSlotsConstraints, List<Map<String, List<Integer>>> maxLoadSlotsConstraints, List<Map<String, List<Integer>>> minLoadSlotsConstraints) {
		SimpleHTTPPostRequester requester = new SimpleHTTPPostRequester();
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("pnl", values);
		map.put("optionalLoads", optionalLoads);
		map.put("optionalDischarges", (optionalDischarges));
		map.put("restricted", (valid));
		map.put("maxDischargeSlots", (maxDischargeSlotsConstraints));
		map.put("minDischargeSlots", (minDischargeSlotsConstraints));
		map.put("maxLoadSlots", (maxLoadSlotsConstraints));
		map.put("minLoadSlots", (minLoadSlotsConstraints));

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
