/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.custom;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.google.common.base.Charsets;
import com.mmxlabs.common.Pair;
import com.mmxlabs.hub.DataHubServiceProvider;
import com.mmxlabs.hub.IDataHubStateChangeListener;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;

public class CustomReportDataUpdater {

	private final CustomReportDataServiceClient client;

	private final ExecutorService taskExecutor;

	private final File basePath;
	private Instant lastModified = Instant.EPOCH;
	private boolean purgeCache = false;
	private final IUpstreamDetailChangedListener purgeLocalRecords = () -> purgeCache = true;
	private final IDataHubStateChangeListener dataHubStateChangeListener = new IDataHubStateChangeListener() {
		
		@Override
		public void hubStateChanged(boolean online, boolean loggedin, boolean changedToOnlineAndLoggedIn) {
			purgeCache = changedToOnlineAndLoggedIn;
		}
		
		@Override
		public void hubPermissionsChanged() {
			
		}
	};

	private Thread updateThread;
	private final ReentrantLock updateLock = new ReentrantLock();

	private final Consumer<CustomReportDataRecord> readyCallback;
	private final ConcurrentMap<String, Instant> oldReports;

	public CustomReportDataUpdater(final File basePath, final CustomReportDataServiceClient client, final Consumer<CustomReportDataRecord> readyCallback) {
		this.basePath = basePath;
		this.client = client;
		this.readyCallback = readyCallback;
		taskExecutor = Executors.newSingleThreadExecutor();
		oldReports = new ConcurrentHashMap<String, Instant>();
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(purgeLocalRecords);
		DataHubServiceProvider.getInstance().addDataHubStateListener(dataHubStateChangeListener);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(purgeLocalRecords);
		DataHubServiceProvider.getInstance().removeDataHubStateListener(dataHubStateChangeListener);
		taskExecutor.shutdownNow();
	}

	public void update(final List<CustomReportDataRecord> records) {

		if (records != null) {
			for (final CustomReportDataRecord record : records) {
				taskExecutor.execute(new DownloadTask(record));
			}
		}
	}

	private class DownloadTask implements Runnable {
		private final CustomReportDataRecord record;

		public DownloadTask(final CustomReportDataRecord record) {
			this.record = record;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s/%s.json", basePath, record.getUuid()));
			final Instant creationDate = oldReports.get(record.getUuid());
			if (!Objects.equals(record.getCreationDate(), creationDate) && record.getCreationDate() != null) {
				try {
					f.getParentFile().mkdirs();
					if (!downloadData(record, f)) {
						// Something went wrong - reset lastModified to trigger another refresh
						lastModified = Instant.EPOCH;
						// Failed!
						return;
					} else {
						oldReports.put(record.getUuid(), record.getCreationDate());
					}
				} catch (final Exception e) {
					// Something went wrong - reset lastModified to trigger another refresh
					lastModified = Instant.EPOCH;
					e.printStackTrace();
					return;
				}
			}

			if (record.getCreationDate() != null) {
				readyCallback.accept(record);
			}
		}

		private boolean downloadData(final CustomReportDataRecord record, final File f) {
			final boolean[] ret = new boolean[1];
			final Job background = new Job("Downloading reference data") {

				@Override
				public IStatus run(final IProgressMonitor monitor) {
					try {
						ret[0] = client.downloadTo(record.getUuid(), f, WrappedProgressMonitor.wrapMonitor(monitor));
					} catch (final Exception e) {
						// return Status.
						e.printStackTrace();
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
		if (f.exists() && f.canRead()) {
			String json;
			try {
				json = Files.readString(f.toPath());
				final List<CustomReportDataRecord> records = CustomReportDataServiceClient.parseRecordsJSONData(json);
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

	public synchronized void refresh() throws IOException {
		if (purgeCache) {
			purgeLocalRecords();
		}
		final boolean available = DataHubServiceProvider.getInstance().isOnlineAndLoggedIn();

		if (available) {

			final Instant m = client.getLastModified();
			if (m != null && m.isAfter(lastModified)) {
				final Pair<String, Instant> recordsPair = client.getRecords();
				if (recordsPair != null) {
					final File recordsFile = new File(basePath.getAbsolutePath() + "/records.json");
					final File recordsFileBKP = new File(basePath.getAbsolutePath() + "/records.json.bkp");
					
					if (recordsFile.exists()) {
						if (recordsFileBKP.exists()) {
							recordsFileBKP.delete();
						}
						recordsFile.renameTo(recordsFileBKP);
					}
					final List<CustomReportDataRecord> records = CustomReportDataServiceClient.parseRecordsJSONData(recordsPair.getFirst());
					update(records);
					Files.writeString(recordsFile.toPath(), recordsPair.getFirst(), Charsets.UTF_8);
					lastModified = recordsPair.getSecond();
				}
			}
		}
	}
	
	private synchronized void purgeLocalRecords() {
		if (basePath.exists() && basePath.canRead() && basePath.canWrite() && basePath.isDirectory()) {
			for (final File f : this.basePath.listFiles()) {
				if (f.exists())
					f.delete();
			}
			oldReports.clear();
			purgeCache = false;
			lastModified = Instant.EPOCH;
		}
	}

	public void pause() {
		updateLock.lock();
	}

	public void resume() {
		updateLock.unlock();
	}
}
