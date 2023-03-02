package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LocalDataStore {

	private LinkedHashMap<String, AtoBviaCLookupRecord> records = new LinkedHashMap<>();

	private OutputStream logStream;

	public void loadFrom(List<AtoBviaCLookupRecord> source) {
		synchronized (records) {
			records.clear();
			source.forEach(r -> records.put(r.getKey(), r));
		}
	}

	public void setLogStream(OutputStream logStream) {
		this.logStream = logStream;
	}

	public void store(AtoBviaCLookupRecord r) {
		synchronized (records) {
			records.put(r.getKey(), r);
			cache(r);
		}
	}

	public AtoBviaCLookupRecord get(String id) {
		synchronized (records) {
			return records.get(id);
		}
	}

	public List<AtoBviaCLookupRecord> getRecords() {
		synchronized (records) {
			return new ArrayList<>(records.values());
		}
	}

	// Just record the entry in the cache file, but not in the final records.
	public void cache(AtoBviaCLookupRecord r) {
		synchronized (records) {
			try {
				final JsonFactory jsonFactory = new JsonFactory();
				jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
				final ObjectMapper m = new ObjectMapper(jsonFactory);

				if (logStream != null) {
					m.writeValue(logStream, r);
					logStream.write("\n".getBytes());
					logStream.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
