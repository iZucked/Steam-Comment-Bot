/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.general;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.MapMaker;
import com.google.common.io.Files;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.commons.http.WrappedProgressMonitor;
import com.mmxlabs.lngdataserver.server.IUpstreamDetailChangedListener;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

public class GeneralDataUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralDataUpdater.class);

	private final DataServiceClient client;

	private final ExecutorService taskExecutor;

	private final File basePath;
	private final IUpstreamDetailChangedListener detailChangedListener = this::refresh;

	private final ConcurrentMap<TypeRecord, Thread> updateThreadMap;

	private final ReentrantLock updateLock = new ReentrantLock();

	private final List<TypeRecord> interestedTypes;

	private final BiConsumer<TypeRecord, GeneralDataRecord> readyCallback;

	public GeneralDataUpdater(final File basePath, final DataServiceClient client, final List<TypeRecord> interestedTypes, final BiConsumer<TypeRecord, GeneralDataRecord> readyCallback) {
		this.basePath = basePath;
		this.client = client;
		this.interestedTypes = interestedTypes;
		this.readyCallback = readyCallback;
		taskExecutor = Executors.newSingleThreadExecutor();
		updateThreadMap = new MapMaker() //
				.concurrencyLevel(4) //
				.makeMap();

		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(detailChangedListener);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(detailChangedListener);
		taskExecutor.shutdownNow();
	}

	public void update(final TypeRecord typeRecord, final List<GeneralDataRecord> records) {

		if (records != null) {
			for (final GeneralDataRecord record : records) {
				taskExecutor.execute(new DownloadTask(typeRecord, record));
			}
		}
	}

	private class DownloadTask implements Runnable {
		private final GeneralDataRecord record;
		private final TypeRecord typeRecord;

		public DownloadTask(final TypeRecord typeRecord, final GeneralDataRecord record) {
			this.typeRecord = typeRecord;
			this.record = record;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s/%s/%s.data", basePath, typeRecord.getType(), record.getUuid()));
			if (!f.exists()) {
				try {
					f.getParentFile().mkdirs();
					if (!downloadData(typeRecord, record, f)) {
						// Something went wrong - reset lastModified to trigger another refresh
						// lastModified = Instant.EPOCH;
						// Failed!
						return;
					}
				} catch (final Exception e) {
					// Something went wrong - reset lastModified to trigger another refresh
					// lastModified = Instant.EPOCH;
					e.printStackTrace();
					return;
				}
			}

			readyCallback.accept(typeRecord, record);
		}

		private boolean downloadData(final TypeRecord typeRecord, final GeneralDataRecord record, final File f) {
			final boolean[] ret = new boolean[1];
			final Job background = new Job("Downloading reference data") {

				@Override
				public IStatus run(final IProgressMonitor monitor) {
					try {
						updateLock.lock();
						client.downloadTo(typeRecord, record.getUuid(), f, WrappedProgressMonitor.wrapMonitor(monitor));
						ret[0] = true;
					} catch (final Exception e) {
						e.printStackTrace();
						// return Status.
					} finally {
						updateLock.unlock();
						if (monitor != null) {
							monitor.done();
						}
					}
					return Status.OK_STATUS;
				}
			};
			background.setSystem(false);
			background.setUser(true);

			background.schedule();
			try {
				background.join();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			return ret[0];
		}
	}

	public void stop() {
		listenForNewUpstreamVersions = false;

		final Iterator<Map.Entry<TypeRecord, Thread>> itr = updateThreadMap.entrySet().iterator();
		while (itr.hasNext()) {
			final Map.Entry<TypeRecord, Thread> e = itr.next();
			final Thread updateThread = e.getValue();
			itr.remove();
			if (updateThread != null) {
				updateThread.interrupt();
			}
		}
	}

	protected boolean listenForNewUpstreamVersions;

	public void start() {
		listenForNewUpstreamVersions = true;
		for (final TypeRecord typeRecord : interestedTypes) {
			readPersistedRecords(typeRecord);
			createTypedUpdateThread(typeRecord);
		}
	}

	private void createTypedUpdateThread(final TypeRecord typeRecord) {
		final Thread updateThread = new Thread(() -> {
			while (listenForNewUpstreamVersions) {
				final CompletableFuture<Boolean> newVersionFuture = client.notifyOnNewVersion(typeRecord);
				try {
					if (newVersionFuture == null) {
						return;
					}

					refreshType(typeRecord);
					final Boolean versionAvailable = newVersionFuture.get();
					if (versionAvailable == Boolean.TRUE) {
						refreshType(typeRecord);
					}
				} catch (final InterruptedException e) {
					if (!listenForNewUpstreamVersions) {
						return;
					}
				} catch (final ExecutionException e) {
					LOGGER.error(e.getMessage());
				}

				// make sure not everything is blocked in case of consecutive failure
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});
		updateThread.setName("DataHub Upstream listener: " + getClass().getName());
		updateThread.start();

		updateThreadMap.put(typeRecord, updateThread);
	}

	private void readPersistedRecords(final TypeRecord typeRecord) {
		final File f = new File(basePath.getAbsolutePath() + "/%s/records.json");
		if (f.exists()) {
			String json;
			try {
				json = Files.toString(f, Charsets.UTF_8);
				final List<GeneralDataRecord> records = client.parseRecordsJSONData(typeRecord, json);
				if (records != null) {
					update(typeRecord, records);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void refresh() {
		final boolean available = UpstreamUrlProvider.INSTANCE.isAvailable();
		if (available) {
			for (final TypeRecord typeRecord : interestedTypes) {
				refreshType(typeRecord);
			}
		}
	}

	public synchronized void refreshType(final TypeRecord typeRecord) {
		try {
			final Pair<String, Instant> recordsPair = client.getRecords(typeRecord);
			if (recordsPair != null) {
				final List<GeneralDataRecord> records = client.parseRecordsJSONData(typeRecord, recordsPair.getFirst());
				File parent = new File(basePath.getAbsolutePath() + "/" + typeRecord.getType());
				parent.mkdirs();
				update(typeRecord, records);
				Files.write(recordsPair.getFirst(), new File(parent.getAbsolutePath() + "/records.json"), Charsets.UTF_8);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		updateLock.lock();
	}

	public void resume() {
		updateLock.unlock();
	}

	public void registerType(final @NonNull TypeRecord typeRecord) {
		this.interestedTypes.add(typeRecord);
		readPersistedRecords(typeRecord);
		createTypedUpdateThread(typeRecord);
		refreshType(typeRecord);
		// lastModified = Instant.EPOCH;
	}
}
