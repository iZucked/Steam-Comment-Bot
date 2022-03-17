/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.generic;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.mmxlabs.hub.IUpstreamDetailChangedListener;
import com.mmxlabs.hub.UpstreamUrlProvider;
import com.mmxlabs.hub.common.http.WrappedProgressMonitor;
import com.mmxlabs.rcp.common.locking.WellKnownTriggers;

public class GenericDataRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericDataRepository.class);

	public static final GenericDataRepository INSTANCE = new GenericDataRepository();

	private File dataFolder;

	private GenericDataServiceClient client;

	private GenericDataUpdater updater;
	private final ConcurrentMap<String, ImmutableList<GenericDataRecord>> dataMap;
	private final ConcurrentMap<String, ConcurrentLinkedQueue<IUpdateListener>> listeners = new ConcurrentHashMap<>();

	private final List<String> initialTypes = new LinkedList<>();

	private final IUpstreamDetailChangedListener upstreamDetailsChangedListener;

	private GenericDataRepository() {
		dataMap = new MapMaker() //
				.concurrencyLevel(4) //
				.makeMap();

		upstreamDetailsChangedListener = dataMap::clear;

		start();
	}

	public ImmutableList<GenericDataRecord> getRecords(final String type) {
		return dataMap.getOrDefault(type, ImmutableList.of());
	}

	private void update(final GenericDataRecord record) {
		dataMap.compute(record.getType(), (k, v) -> {
			if (v == null) {
				return ImmutableList.of(record);
			} else {
				if (v.contains(record)) {
					return v;
				}
				return new ImmutableList.Builder<GenericDataRecord>() //
						.addAll(v) //
						.add(record) //
						.build();
			}
		});

		fireListeners(record.getType());
	}

	private void fireListeners(String type) {
		try {
			ConcurrentLinkedQueue<IUpdateListener> typeListeners = listeners.get(type);
			if (typeListeners != null) {
				typeListeners.forEach(IUpdateListener::updated);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	public @Nullable File getDataFile(final String type, final String uuid) {
		return new File(String.format("%s/%s/%s.data", dataFolder.getAbsoluteFile(), type, uuid));
	}

	public void uploadData(final String type, final String uuid, final String contentType, final File dataFile, @Nullable final IProgressMonitor progressMonitor) throws Exception {
		try {

			try {
				updater.pause();
				client.upload(type, uuid, contentType, dataFile, WrappedProgressMonitor.wrapMonitor(progressMonitor));
			} finally {
				updater.resume();
			}
			try {
				updater.refresh();
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (progressMonitor != null) {
				progressMonitor.done();
			}
		}
	}

	public void delete(final String type, final String uuid) throws IOException {

		client.deleteData(type, uuid);
		try {
			updater.refresh();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {

		final Bundle bundle = FrameworkUtil.getBundle(GenericDataRecord.class);
		final BundleContext bundleContext = bundle.getBundleContext();

		new ServiceTracker<IWorkspace, IWorkspace>(bundleContext, IWorkspace.class, null) {
			private boolean started = false;

			@Override
			public IWorkspace addingService(final ServiceReference<IWorkspace> reference) {
				final IWorkspace workspace = super.addingService(reference);

				if (!started) {
					started = true;
					final IPath workspaceLocation = workspace.getRoot().getLocation();
					final File workspaceLocationFile = workspaceLocation.toFile();

					dataFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "refdata");
					if (!dataFolder.exists()) {
						dataFolder.mkdirs();
					}

					client = new GenericDataServiceClient();
					synchronized (initialTypes) {
						GenericDataRepository.this.updater = new GenericDataUpdater(dataFolder, client, initialTypes, GenericDataRepository.this::update);
					}
					WellKnownTriggers.WORKSPACE_DATA_ENCRYPTION_CHECK.delayUntilTriggered(() -> updater.start());
				}
				this.close();
				return workspace;
			}
		}.open();
	}

	public void stop() {

		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(upstreamDetailsChangedListener);
		if (updater != null) {
			updater.stop();
		}
		dataMap.clear();
	}

	public void registerType(final @NonNull String type) {
		synchronized (initialTypes) {
			if (updater == null) {
				initialTypes.add(type);
			} else {
				updater.registerType(type);
			}
		}
	}

	public interface IUpdateListener {
		void updated();
	}

	public void addListener(String modelType, IUpdateListener l) {
		listeners.computeIfAbsent(modelType, k -> new ConcurrentLinkedQueue<>()).add(l);
	}

	public void removeListener(String modelType, IUpdateListener l) {
		listeners.get(modelType).remove(l);
	}
}
