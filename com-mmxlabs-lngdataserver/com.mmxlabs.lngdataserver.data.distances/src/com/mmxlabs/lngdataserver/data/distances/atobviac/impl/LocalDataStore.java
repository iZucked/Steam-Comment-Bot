/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
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

	// Just record the entry in the cache file, but not in the final records.
	public void saveAll() {
		try {
			final JsonFactory jsonFactory = new JsonFactory();
			jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
			final ObjectMapper m = new ObjectMapper(jsonFactory);

			if (logStream != null) {
				getRecords().forEach(r -> {
					try {
						m.writeValue(logStream, r);
						logStream.write("\n".getBytes());

					} catch (StreamWriteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DatabindException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				logStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearTimeouts() {
		var itr = records.entrySet().iterator();

		while (itr.hasNext()) {
			var e = itr.next();
			if ("timeout".equals(e.getValue().getErrorCode())) {
				itr.remove();
			}
		}
	}

	public void clearBadGateway() {
		var itr = records.entrySet().iterator();

		while (itr.hasNext()) {
			var e = itr.next();
			if ("Bad Gateway".equals(e.getValue().getErrorCode())) {
				itr.remove();
			}
		}
	}

}
