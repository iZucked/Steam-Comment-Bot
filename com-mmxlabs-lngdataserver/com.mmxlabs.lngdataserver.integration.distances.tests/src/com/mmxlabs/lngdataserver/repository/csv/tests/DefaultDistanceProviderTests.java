/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.repository.csv.tests;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.lngdataserver.integration.distances.CsvDistancesImporter;
import com.mmxlabs.lngdataserver.integration.distances.DefaultDistanceProvider;
import com.mmxlabs.lngdataserver.integration.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.integration.distances.IDistanceProvider;
import com.mmxlabs.lngdataserver.integration.distances.Via;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

public class DefaultDistanceProviderTests {

	private boolean initialized = false;
	private final DistanceRepository repo = DistanceRepository.INSTANCE;

	public IDistanceProvider getDistanceProvider(String version) throws Exception {
		// Map<String, Map<String, Integer>> result = CsvDistancesImporter.importMatrix(reader, new DefaultImportContext(".".charAt(0)));

		InputStream directStream = getClass().getResourceAsStream(String.format("/distances/tests/%s%%DIRECT.csv", version));
		InputStream suezStream = getClass().getResourceAsStream(String.format("/distances/tests/%s%%SUEZ.csv", version));
		InputStream panamaStream = getClass().getResourceAsStream(String.format("/distances/tests/%s%%PANAMA.csv", version));

		Map<Via, Map<String, Map<String, Integer>>> m = new EnumMap<>(Via.class);
		m.put(Via.Direct, CsvDistancesImporter.importMatrix(new CSVReader(',', directStream), new DefaultImportContext('.')));
		m.put(Via.SuezCanal, CsvDistancesImporter.importMatrix(new CSVReader(',', suezStream), new DefaultImportContext('.')));
		m.put(Via.PanamaCanal, CsvDistancesImporter.importMatrix(new CSVReader(',', panamaStream), new DefaultImportContext('.')));
		// DefaultDistanceProvider("1488467163%newest")

		return new DefaultDistanceProvider(version, m);
	}

	@Test
	public void getDistanceProviderTest() throws Exception {
		IDistanceProvider dp = getDistanceProvider("1488467163%newest");
		Assertions.assertNotNull(dp);
	}

	@Test
	public void getDistanceTest() throws Exception {
		int dist = getDistanceProvider("1488467163%newest").getDistance("barrow island", "soyo", Via.Direct);
		Assertions.assertEquals(6952, dist);
	}

	// @Test(expected = PortNotFoundException.class)
	public void getNonExistingPort() throws Exception {

		Assertions.assertEquals(Integer.MAX_VALUE, getDistanceProvider("1488467163%newest").getDistance("Soyo", "Barrow", Via.Direct));
	}

	@Test
	public void testIdentityRoute() throws Exception {

		int dist = getDistanceProvider("1488467163%newest").getDistance("pecem", "pecem", Via.Direct);
		Assertions.	assertEquals(0, dist);
	}
}
