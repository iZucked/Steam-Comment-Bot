/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.generic;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.commons.http.WrappedProgressMonitor;
import com.mmxlabs.lngdataserver.server.IUpstreamDetailChangedListener;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;

public class GenericDataUpdater {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericDataUpdater.class);

	private final GenericDataServiceClient client;

	private final ExecutorService taskExecutor;

	private final File basePath;
	private Instant lastModified = Instant.EPOCH;
	private final IUpstreamDetailChangedListener detailChangedListener = () -> lastModified = Instant.EPOCH; // Reset to trigger refresh

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	private final List<String> interestedTypes;

	private final Consumer<GenericDataRecord> readyCallback;

	public GenericDataUpdater(final File basePath, final GenericDataServiceClient client, final List<String> interestedTypes, final Consumer<GenericDataRecord> readyCallback) {
		this.basePath = basePath;
		this.client = client;
		this.readyCallback = readyCallback;
		taskExecutor = Executors.newSingleThreadExecutor();
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(detailChangedListener);
		this.interestedTypes = new LinkedList<>(interestedTypes);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(detailChangedListener);
		taskExecutor.shutdownNow();
	}

	public void update(final List<GenericDataRecord> records) {

		if (records != null) {
			for (final GenericDataRecord record : records) {
				taskExecutor.execute(new DownloadTask(record));
			}
		}
	}

	private class DownloadTask implements Runnable {
		private final GenericDataRecord record;

		public DownloadTask(final GenericDataRecord record) {
			this.record = record;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s/%s/%s.data", basePath, record.getType(), record.getUuid()));
			if (!f.exists()) {
				try {
					f.getParentFile().mkdirs();
					if (!downloadData(record, f)) {
						// Something went wrong - reset lastModified to trigger another refresh
						lastModified = Instant.EPOCH;
						// Failed!
						return;
					}
				} catch (final Exception e) {
					// Something went wrong - reset lastModified to trigger another refresh
					lastModified = Instant.EPOCH;
					e.printStackTrace();
					return;
				}
			}

			readyCallback.accept(record);
		}

		private boolean downloadData(final GenericDataRecord record, final File f) {
			final boolean[] ret = new boolean[1];
			final Job background = new Job("Downloading reference data") {

				@Override
				public IStatus run(final IProgressMonitor monitor) {
					try {
						ret[0] = client.downloadTo(record.getType(), record.getUuid(), f, WrappedProgressMonitor.wrapMonitor(monitor));
					} catch (final Exception e) {
						// return Status.
					} finally {
						if (monitor != null) {
							monitor.done();
						}
					}
					return Status.OK_STATUS;
				}
			};
			background.setSystem(false);
			background.setUser(true);
			// background.setPriority(Job.LONG);

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
		if (updateThread != null) {
			updateThread.interrupt();
			updateThread = null;
		}
	}

	public void start() {
		final File f = new File(basePath.getAbsolutePath() + "/records.json");
		if (f.exists()) {
			String json;
			try {
				json = Files.toString(f, Charsets.UTF_8);

				final List<GenericDataRecord> records = client.parseRecordsJSONData(json);
				if (records != null) {
					update(records);
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		updateThread = new Thread("GenericDataUpdaterThread") {
			@Override
			public void run() {

				while (true) {
					updateLock.lock();
					try {
						refresh();
					} catch (final IOException e1) {
						e1.printStackTrace();
					} finally {
						updateLock.unlock();
					}

					try {
						Thread.sleep(10_000);
					} catch (final InterruptedException e) {
						interrupt(); // preserve interruption status
						return;
					}
				}
			}

		};
		updateThread.start();
	}

	public void refresh() throws IOException {
		final boolean available = UpstreamUrlProvider.INSTANCE.isAvailable();

		if (available) {
			final Instant m = client.getLastModified();
			if (true || m != null && m.isAfter(lastModified)) {
				final Pair<String, Instant> recordsPair = client.getRecords(interestedTypes);
				if (recordsPair != null) {
					final List<GenericDataRecord> records = client.parseRecordsJSONData(recordsPair.getFirst());
					update(records);
					Files.write(recordsPair.getFirst(), new File(basePath.getAbsolutePath() + "/records.json"), Charsets.UTF_8);
					lastModified = recordsPair.getSecond();
				}
			}
		}
	}

	public void pause() {
		updateLock.lock();
	}

	public void resume() {
		updateLock.unlock();
	}

	public void registerType(final @NonNull String type) {
		this.interestedTypes.add(type);
		lastModified = Instant.EPOCH;
	}
}
