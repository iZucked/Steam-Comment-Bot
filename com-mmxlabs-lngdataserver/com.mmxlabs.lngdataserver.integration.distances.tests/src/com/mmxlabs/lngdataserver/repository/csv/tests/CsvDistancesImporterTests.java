/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.repository.csv.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.lngdataserver.integration.distances.CsvDistancesImporter;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

public class CsvDistancesImporterTests {

	@Test
	public void testImport() throws IOException {

		String subset = "from,arzew,bethioua,skikda,soyo,bahia blanca,escobar,barrow island,dampier,darwin,gladstone\r\n"
				+ "arzew,0,5.623,381.247,4073.118,5861.495,5594.44,10531.423,10564.979,11529.183,12603.058\r\n"
				+ "bethioua,5.623,0,380.204,4072.595,5860.972,5593.916,10530.9,10564.455,11528.66,12602.535\r\n"
				+ "skikda,384.11,383.068,0,4421.582,6209.958,5942.903,10879.887,10913.442,11877.647,12951.522\r\n"
				+ "soyo,4072.498,4071.975,4418.34,0,4496.004,4304.757,6952.37,6985.925,7950.13,9024.005\r\n"
				+ "bahia blanca,5860.864,5860.341,6206.707,4496.004,0,703.034,8827.532,8861.087,9154.006,7143.243\r\n"
				+ "escobar,5593.891,5593.368,5939.733,4304.839,702.414,0,8871.34,8904.895,9611.558,7600.794\r\n"
				+ "barrow island,10530.803,10530.28,10876.645,6952.37,8827.532,8871.37,0,96.867,1086.321,3167.246\r\n"
				+ "dampier,10564.358,10563.835,10910.2,6985.925,8861.087,8904.925,96.867,0,1043.955,3124.88\r\n"
				+ "darwin,11528.484,11527.961,11874.327,7950.051,9153.927,9612.099,1086.242,1046.404,0,2307.505\r\n"
				+ "gladstone,12598.923,12598.4,12944.766,9020.49,7142.676,7600.848,3167.646,3127.808,2307.985,0";

		InputStream stream = new ByteArrayInputStream(subset.getBytes("UTF-8"));
		CSVReader reader = new CSVReader(",".charAt(0), stream);

		Map<String, Map<String, Integer>> result = CsvDistancesImporter.importMatrix(reader, new DefaultImportContext(".".charAt(0)));
		Assertions.assertEquals(Integer.valueOf(5), result.get("arzew").get("bethioua"));
		Assertions.assertEquals(Integer.valueOf(12598), result.get("gladstone").get("arzew"));
		Assertions.assertEquals(Integer.valueOf(12603), result.get("arzew").get("gladstone"));
		Assertions.assertEquals(Integer.valueOf(0), result.get("gladstone").get("gladstone"));
	}
}
