package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.inject.Named;

import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBViaCUpdateService;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCAdapter;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.Constants;

public class Util {

	public static boolean PERFORM_UPSTREAM_QUERIES = false;

	private static Map<String, String> initMap() {
		Map<String, String> m = new HashMap<>();
		m.put("GB", "United Kingdom");
		m.put("US", "United States");
		m.put("EG", "Egypt");
		m.put("MX", "Mexico");
		m.put("ID", "Indonesia");
		m.put("DZ", "Algeria");
		m.put("JO", "Jordan");
		m.put("AU", "Australia");
		m.put("CN", "China");
		m.put("PH", "Philippines");
		m.put("MY", "Malaysia");
		m.put("NG", "Nigeria");
		m.put("BD", "Bangladesh");
		m.put("ZA", "South Africa");
		m.put("TZ", "Tanzania");
		m.put("AE", "United Arab Emirates");
		m.put("TL", "East Timor");
		m.put("ES", "Spain");
		m.put("NL", "Netherlands");
		m.put("MQ", "Martinique");
		m.put("GI", "Gibraltar");
		m.put("JP", "Japan");
		m.put("PR", "Puerto Rico");
		m.put("IN", "India");
		m.put("CA", "Canada");
		m.put("KR", "South Korea");
		m.put("MA", "Morocco");
		m.put("TW", "Taiwan");
		m.put("PK", "Pakistan");
		m.put("IQ", "Iraq");
		m.put("SI", "Slovenia");
		m.put("CM", "Cameroon");
		m.put("CY", "Cyprus");
		m.put("IE", "Ireland");
		m.put("BN", "Brunei Darussalam");
		m.put("TH", "Thailand");
		m.put("LY", "Libya");
		m.put("CL", "Chile");
		m.put("KE", "Kenya");
		m.put("KW", "Kuwait");
		m.put("UY", "Uruguay");
		m.put("RU", "Russian Federation");
		m.put("NZ", "New Zealand");
		m.put("TT", "Trinidad and Tobago");
		m.put("GP", "Guadeloupe");
		m.put("JM", "Jamaica");
		m.put("PG", "Papua New Guinea");
		m.put("IT", "Italy");
		m.put("HR", "Croatia");
		m.put("BR", "Brazil");
		m.put("DE", "Germany");
		m.put("LB", "Lebanon");
		m.put("PL", "Poland");
		m.put("QA", "Qatar");
		m.put("EE", "Estonia");
		m.put("MZ", "Mozambique");
		m.put("SV", "El Salvador");
		m.put("GR", "Greece");
		m.put("IR", "Iran");
		m.put("BH", "Bahrain");
		m.put("PA", "Panama");
		m.put("OM", "Oman");
		m.put("FR", "France");
		m.put("MT", "Malta");
		m.put("TR", "Turkey");
		m.put("UA", "Ukraine");
		m.put("SG", "Singapore");
		m.put("PE", "Peru");
		m.put("DO", "Dominican Republic");
		m.put("NO", "Norway");
		m.put("YE", "Yemen");
		m.put("AO", "Angola");
		m.put("IL", "Israel");
		m.put("UM", "United States Minor Outlying Islands");
		m.put("PT", "Portugal");
		m.put("AR", "Argentina");
		m.put("BE", "Belgium");
		m.put("VN", "Vietnam");
		m.put("GQ", "Equatorial Guinea");
		m.put("CO", "Colombia");
		m.put("LT", "Lithuania");
		return m;
	}

	public static final Map<String, String> COUNTRY_CODE_MAP = initMap();

	private static TypeReference<List<AtoBviaCLookupRecord>> DISTANCE_TYPE = new TypeReference<List<AtoBviaCLookupRecord>>() {

	};

	public static void withService(Consumer<AtoBViaCUpdateService> consumer) {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		try {
			Injector injector = createInjector(executor);
			AtoBViaCUpdateService service = injector.getInstance(AtoBViaCUpdateService.class);
			consumer.accept(service);
		} finally {
			executor.shutdownNow();
		}

	}

	public static Injector createInjector(ExecutorService executor) {
		return Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {

				File f = new File("apikey.txt");
				if (f.exists()) {
					try {
						String key = Files.asCharSource(f, StandardCharsets.UTF_8).read();
						bind(String.class).annotatedWith(Names.named(Constants.API_KEY)).toInstance(key.trim());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				} else {
					bind(String.class).annotatedWith(Names.named(Constants.API_KEY)).toInstance("demo");
				}
				bind(String.class).annotatedWith(Names.named(Constants.API_URL)).toInstance("https://api.atobviac.com/v1/");
				bind(Boolean.class).annotatedWith(Names.named(Constants.API_ENABLED)).toInstance(PERFORM_UPSTREAM_QUERIES);

				bind(AtoBViaCUpdateService.class);
				bind(AtoBviaCAdapter.class);

				bind(ExecutorService.class).toInstance(executor);

			}

			@Provides
			@Singleton
			private CloseableHttpClient createHttpClient(@Named(Constants.API_URL) String url) throws URISyntaxException {
				HttpHost host = URIUtils.extractHost(new URI(url));
				return HttpClientUtil.createBasicHttpClient(host, false).build();

			}
		});
	}

	/**
	 * 
	 * Taken from https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @param unit
	 * @return
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344; // KM
		} else if (unit == 'N') { // Nautical  miles
			dist = dist * 0.8684;
		}
		// else 'M' miles
		return (dist);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}
