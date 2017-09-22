package com.mmxlabs.lngdataserver.repository.csv.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import org.junit.Test;
import com.mmxlabs.common.csv.CSVReader;
import com.mmxlabs.lngdataserver.distances.CsvDistancesImporter;
import com.mmxlabs.lngdataserver.distances.DefaultDistanceProvider;
import com.mmxlabs.lngdataserver.distances.DistanceRepository;
import com.mmxlabs.lngdataserver.distances.IDistanceProvider;
import com.mmxlabs.lngdataserver.distances.Via;
import com.mmxlabs.models.util.importer.impl.DefaultImportContext;

public class DefaultDistanceProviderTests {

	private boolean initialized = false;
	private final DistanceRepository repo = new DistanceRepository();

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
		assertNotNull(dp);
	}

	@Test
	public void getDistanceTest() throws Exception {
		int dist = getDistanceProvider("1488467163%newest").getDistance("barrow island", "soyo", Via.Direct);
		assertEquals(6952, dist);
	}

	// @Test(expected = PortNotFoundException.class)
	public void getNonExistingPort() throws Exception {

		assertEquals(Integer.MAX_VALUE, getDistanceProvider("1488467163%newest").getDistance("Soyo", "Barrow", Via.Direct));
	}

	@Test
	public void testIdentityRoute() throws Exception {

		int dist = getDistanceProvider("1488467163%newest").getDistance("pecem", "pecem", Via.Direct);
		assertEquals(0, dist);
	}
}
