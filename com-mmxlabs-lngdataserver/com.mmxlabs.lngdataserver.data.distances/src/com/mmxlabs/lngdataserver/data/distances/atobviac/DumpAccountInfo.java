package com.mmxlabs.lngdataserver.data.distances.atobviac;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.apache.http.HttpHost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.common.http.HttpClientUtil;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBViaCUpdateService;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCAdapter;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCLookupRecord;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.AtoBviaCPort;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.Constants;
import com.mmxlabs.lngdataserver.data.distances.atobviac.impl.LocalDataStore;
import com.mmxlabs.lngdataserver.data.distances.atobviac.model.PortDistanceVersion;

public class DumpAccountInfo {

	public static void main(String[] args) {
		List<String> newPorts = Lists.newArrayList("NL001");

		String existingDataSet = "2023b";
		String newDataSet = "2023d";

		Util.withService(service -> {
			try {
				Map<String, String> accountDetails = service.getAccountDetails();
				for (Map.Entry<String, String> e : accountDetails.entrySet()) {
					System.out.printf("%s: %s %n", e.getKey(), e.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
