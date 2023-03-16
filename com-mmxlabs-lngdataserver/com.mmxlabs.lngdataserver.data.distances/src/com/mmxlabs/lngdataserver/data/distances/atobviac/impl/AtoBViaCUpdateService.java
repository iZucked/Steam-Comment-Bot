/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.inject.Named;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;

/**
 * @author robert.erdin@minimaxlabs.com on 18/07/17.
 */
public class AtoBViaCUpdateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AtoBViaCUpdateService.class);

	private int completedRequests = 0; // Counter for Current # of Completed Queries
	// private List<AtoBviaCLookupRecord> errorRecordList = new ArrayList<>();
	// private Map<String, Boolean> errorRecords = new HashMap<>();

	// @Autowired
	// private Datastore datastore;

	LocalDataStore localDatastore;

	@Inject(optional = true)
	@Named(Constants.API_ENABLED)
	private boolean distanceLookupEnabled = false;

	@Inject
	private AtoBviaCAdapter atoBviaCAdapter;

	public Future<Void> batchRefresh(final List<Pair<String, String>> distanceLines) throws Exception {
		distanceLookupEnabled = true;

		if (!distanceLookupEnabled) {
			return CompletableFuture.completedFuture(null);
		}

		final String version = atoBviaCAdapter.getCurrentDataVersion();

		if (version == null || "UNKNOWN".contentEquals(version)) {
			// Error!
			throw new RuntimeException("Unable to get latest distance version");
		}

		final List<CompletableFuture<AtoBviaCLookupRecord>> futures = new LinkedList<>();
		for (final Pair<String, String> request : distanceLines) {
			futures.add(createLookup(version, request, true));
			// futures.add(createLookup(version, request, false));
		}
		// Wait for all futures to complete
		for (final CompletableFuture<?> f : futures) {
			f.join();
			completedRequests++;
		}
		final CompletableFuture<Void> multiFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
		return multiFuture;
	}
	//
	// public Future<Void> refreshTimeouts() throws Exception {
	// final List<Pair<String, String>> queries = new LinkedList<>();
	//
	// // Find existing stuff and filter requests.
	// final Query<AtoBviaCLookupRecord> q = datastore.createQuery(AtoBviaCLookupRecord.class) //
	// .filter("errorCode", "timeout") // check error codes, some will need version update
	// ;
	// final Iterator<AtoBviaCLookupRecord> itr = q.iterator();
	// while (itr.hasNext()) {
	// final AtoBviaCLookupRecord next = itr.next();
	// queries.add(Pair.of(next.getFrom(), next.getTo()));
	// }
	//
	// return batchRefresh(queries);
	// }
	//
	// public Future<Void> refreshErrorDistances() throws Exception {
	// final List<Pair<String, String>> queries = new LinkedList<>();
	//
	// // Find existing stuff and filter requests.
	// final Query<AtoBviaCLookupRecord> q = datastore.createQuery(AtoBviaCLookupRecord.class) //
	// .filter("errorCode !=", null) // check error codes, some will need version update
	// ;
	// final Iterator<AtoBviaCLookupRecord> itr = q.iterator();
	// while (itr.hasNext()) {
	// final AtoBviaCLookupRecord next = itr.next();
	// queries.add(Pair.of(next.getFrom(), next.getTo()));
	// }
	//
	// return batchRefresh(queries);
	// }
	//
	// public List<Pair<String, String>> generateMissingDistanceLines(final List<Pair<String, String>> distanceLines) {
	//
	// // Find all existing valid distances ...
	// final Query<AtoBviaCLookupRecord> q = datastore.createQuery(AtoBviaCLookupRecord.class) //
	// .filter("distance >", 0.0);
	//
	// // ... and remove them from the full lookup list
	// q.forEach(r -> distanceLines.remove(Pair.of(r.getFrom(), r.getTo())));
	//
	// // Push through a set to remove any potential duplicates
	// final List<Pair<String, String>> copy = new LinkedList<>(new LinkedHashSet<>(distanceLines));
	//
	//// if (true) {
	//// System.out.printf("%d missing distances%n", copy.size());
	//// copy.forEach(p -> System.out.printf("Missing %s -> %s\n", p.getKey(), p.getRight()));
	//// }
	// return copy;
	// }
	// public List<Pair<String, String>> generateTimeoutDistanceLines(final List<Pair<String, String>> distanceLines) {
	//
	// // Find all existing valid distances ...
	// final Query<AtoBviaCLookupRecord> q = datastore.createQuery(AtoBviaCLookupRecord.class) //
	// .filter("errorCode", "timeout");
	//
	// final List<Pair<String, String>> copy = new LinkedList<>();
	// // ... and remove them from the full lookup list
	// q.forEach(r -> copy.add(Pair.of(r.getFrom(), r.getTo())));
	//
	// copy.retainAll(distanceLines);
	//
	// // Push through a set to remove any potential duplicates
	//
	//// if (true) {
	//// System.out.printf("%d missing distances%n", copy.size());
	//// copy.forEach(p -> System.out.printf("Missing %s -> %s\n", p.getKey(), p.getRight()));
	//// }
	// return copy;
	// }

	private AtoBviaCLookupRecord parseResponse(final String version, final Pair<String, String> request, final HttpResponse res, Throwable ex) {
		// Version more strictly correct from response headers? (if present). Could happen if new version published during update?

		// Unwrap nested exceptions
		// while (ex != null && !(ex instanceof HttpClientErrorException)) {
		// ex = ex.getCause();
		// }
		// if (ex instanceof Exception) {
		//// final HttpClientErrorException e = (HttpClientErrorException) ex;
		// LOGGER.warn("Response for {} x {} not numeric", request.getFirst(), request.getSecond());
		//// LOGGER.warn("Upstream API returned: {} {}", e.getRawStatusCode(), e.getResponseBodyAsString());
		//// LOGGER.warn("x-abc-error: {}", e.getResponseHeaders().get("x-abc-error"));
		//// LOGGER.warn("x-abc-errorcode: {}", e.getResponseHeaders().get("x-abc-errorcode"));
		//// final AtoBviaCLookupRecord r = AtoBviaCLookupRecord.upstreamError(version, request.getFirst(), request.getSecond(), e.getResponseHeaders().get("x-abc-errorcode").toString());
		// return r;
		// }
		if (res == null) {
			LOGGER.warn("Could not get response for {} > {}", request.getFirst(), request.getSecond());
			final AtoBviaCLookupRecord r = AtoBviaCLookupRecord.fail(version, request.getFirst(), request.getSecond());
			return r;
		} else {
			if (res.getStatusLine().getStatusCode() == 200) {
				try {
					final String responseBody = EntityUtils.toString(res.getEntity());
					final AtoBviaCLookupRecord r = AtoBviaCLookupRecord.distance(version, request.getFirst(), request.getSecond(), Double.parseDouble(responseBody));
					// System.out.printf("GOT Distance %s -> %s = %f\n", r.getFrom(), r.getTo(), r.getDistance());
					return r;
				} catch (Exception e) {
					LOGGER.warn("Failed to parse distance response for {} > {} : {}", request.getFirst(), request.getSecond(), res);
					final AtoBviaCLookupRecord r = AtoBviaCLookupRecord.fail(version, request.getFirst(), request.getSecond());
					return r;
				}
			} else {
				LOGGER.warn("Failed to parse distance response for {} > {} : {}", request.getFirst(), request.getSecond(), res);
				final AtoBviaCLookupRecord r = AtoBviaCLookupRecord.upstreamError(version, request.getFirst(), request.getSecond(), res.getStatusLine().getReasonPhrase());
				return r;
			}
		}
	}

	private CompletableFuture<AtoBviaCLookupRecord> createLookup(final String version, final Pair<String, String> request, final boolean antiPiracy) {
		return atoBviaCAdapter.getDistance(request.getFirst(), request.getSecond(), antiPiracy).handle((res, ex) -> {
			final AtoBviaCLookupRecord r = parseResponse(version, request, res, ex);
			r.setAntiPiracy(antiPiracy); // ParseResponse doesn't set antiPiracy
			if (r.getErrorCode() == null) {
				// Successful lookup, save it
				localDatastore.store(r);
			} else {
				// There was an error condition. Do not save it if there is a valid result already stored.
				// This avoids loosing distances should e.g. connection time out or key expire

				String id = AtoBviaCLookupRecord.updateIdFor(request.getFirst(), request.getSecond(), r.getAntiPiracy());

				AtoBviaCLookupRecord existing = localDatastore.get(id);
				if (existing == null) {
					localDatastore.store(r);
				} else {
					localDatastore.cache(r);
				}

				// final Query<AtoBviaCLookupRecord> q = datastore.createQuery(AtoBviaCLookupRecord.class) //
				// .filter("version", version) //
				// .filter("from", request.getLeft()) //
				// .filter("to", request.getRight()) //
				// .filter("errorCode", null) //
				// .filter("antiPiracy", r.getAntiPiracy()) //
				// ;
				// errorRecordList.add(r);
				// if (q.count() == 0) {
				// datastore.save(r);
				// errorRecords.put(r.getFrom() + "__" + r.getTo() + "__" + r.getAntiPiracy() + " " + r.getErrorCode(), true);
				// } else {
				// errorRecords.put(r.getFrom() + "__" + r.getTo() + "__" + r.getAntiPiracy() + " " + r.getErrorCode(), false);
				// }
			}
			return r;
		});
	}

	// public AtoBviaCLookupRecord getRecord(final String from, final String to) {
	// final Query<AtoBviaCLookupRecord> q = datastore.createQuery(AtoBviaCLookupRecord.class) //
	// .filter("from", from) //
	// .filter("to", to) //
	// ;
	// return q.get();
	// }

	// public Query<AtoBviaCLookupRecord> getAllRecordsQuery() {
	// return datastore.createQuery(AtoBviaCLookupRecord.class);
	// }
	//
	// public void putRecord(final AtoBviaCLookupRecord record) {
	// datastore.save(record);
	// }

	// public void saveToFile(final String string) {
	// try (FileWriter fw = new FileWriter(string)) {
	// new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(fw, datastore.createQuery(AtoBviaCLookupRecord.class).asList());
	// } catch (final Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// public void loadAndReplaceFromFile(final String string) {
	// try (FileReader fw = new FileReader(string)) {
	// final List<AtoBviaCLookupRecord> l = new ObjectMapper().readValue(fw, new TypeReference<List<AtoBviaCLookupRecord>>() {
	// });
	//
	// datastore.delete(datastore.createQuery(AtoBviaCLookupRecord.class));
	// datastore.save(l);
	//
	// } catch (final Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// public void replaceFrom(final List<AtoBviaCLookupRecord> l) {
	// datastore.delete(datastore.createQuery(AtoBviaCLookupRecord.class));
	// datastore.save(l);
	// }
	//
	// public ImmutableList<AtoBviaCLookupRecord> getRecords() {
	// return ImmutableList.copyOf(datastore.createQuery(AtoBviaCLookupRecord.class));
	// }

	public enum UpdateMode {
		REPLACE_ALL, REPLACE_MISSING
	}

	// public void setManualDistance(final String from, final String to, final double distance) {
	// final AtoBviaCLookupRecord record = AtoBviaCLookupRecord.distance("manual", from, to, distance);
	// datastore.save(record);
	// }

	// public int getRefreshCount() {
	// return completedRequests;
	// }
	//
	// public void resetCounts() {
	// completedRequests = 0;
	// errorRecords.clear();
	// errorRecordList.clear();
	// }
	//
	// public int getErrorCount() {
	// return errorRecords.size();
	// }
	//
	// public Map<String, Boolean> getErrors() {
	// return errorRecords;
	// }
	//
	// public List<AtoBviaCLookupRecord> getErrorList() {
	// return errorRecordList;
	// }

	public Map<String, String> getAccountDetails() throws ClientProtocolException, IOException {
		final String[] response = atoBviaCAdapter.getAccountDetails(atoBviaCAdapter.getAccountRequestString()).split(",");
		final String[] account = new String[3];
		for (int i = 0; i < response.length; i++) {
			final String[] field = response[i].split(":");
			account[i] = field[1];
		}
		final String remainingDistances = account[0].substring(1, account[0].length() - 1);
		final String licenseExpiry = account[1].substring(1, account[1].length() - 1);
		final String version = account[2].substring(1, account[2].length() - 2);
		final Map<String, String> accountDetails = new HashMap<>();
		accountDetails.put("remainingDistances", remainingDistances);
		accountDetails.put("licenseExpiry", licenseExpiry);
		accountDetails.put("version", version);
		return accountDetails;
	}

	public void setDataStore(LocalDataStore localDataStore2) {
		this.localDatastore = localDataStore2;
	}

	public boolean isDistanceLookupEnabled() {
		return distanceLookupEnabled;
	}

	public void setDistanceLookupEnabled(boolean distanceLookupEnabled) {
		this.distanceLookupEnabled = distanceLookupEnabled;
	}

	public AtoBviaCAdapter getAtoBviaCAdapter() {
		return atoBviaCAdapter;
	}

	public void setAtoBviaCAdapter(AtoBviaCAdapter atoBviaCAdapter) {
		this.atoBviaCAdapter = atoBviaCAdapter;
	}
}