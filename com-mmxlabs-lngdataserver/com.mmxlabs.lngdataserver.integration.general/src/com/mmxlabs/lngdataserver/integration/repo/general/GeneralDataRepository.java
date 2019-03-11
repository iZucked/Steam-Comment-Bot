/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.repo.general;

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
import com.mmxlabs.lngdataserver.commons.http.WrappedProgressMonitor;

public class GeneralDataRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralDataRepository.class);

	public static final GeneralDataRepository INSTANCE = new GeneralDataRepository();

	private File dataFolder;

	private DataServiceClient client;

	private GeneralDataUpdater updater;
	private final ConcurrentMap<TypeRecord, ImmutableList<GeneralDataRecord>> dataMap;
	private final ConcurrentMap<TypeRecord, ConcurrentLinkedQueue<IUpdateListener>> listeners = new ConcurrentHashMap<>();

	private final List<TypeRecord> initialTypes = new LinkedList<>();

	private GeneralDataRepository() {
		dataMap = new MapMaker() //
				.concurrencyLevel(4) //
				.makeMap();

		start();
	}

	public ImmutableList<GeneralDataRecord> getRecords(TypeRecord record) {
		return dataMap.getOrDefault(record, ImmutableList.of());
	}

	private void update(TypeRecord typeRecord, final GeneralDataRecord record) {

		dataMap.compute(typeRecord, (k, v) -> {
			if (v == null) {
				return ImmutableList.of(record);
			} else {
				if (v.contains(record)) {
					return v;
				}
				return new ImmutableList.Builder<GeneralDataRecord>() //
						.addAll(v) //
						.add(record) //
						.build();
			}
		});

		fireListeners(typeRecord);
	}

	private void fireListeners(TypeRecord type) {
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

	public void uploadData(final TypeRecord typeRecord, final String json, @Nullable final IProgressMonitor progressMonitor) throws Exception {
		try {

			try {
				updater.pause();
				client.upload(typeRecord, json, WrappedProgressMonitor.wrapMonitor(progressMonitor));
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

	public void delete(final TypeRecord typeRecord, final String uuid) throws IOException {

		client.deleteData(typeRecord, uuid);
		updater.refreshType(typeRecord);
	}

	public void start() {

		final Bundle bundle = FrameworkUtil.getBundle(GeneralDataRecord.class);
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

					client = new DataServiceClient();
					synchronized (initialTypes) {
						GeneralDataRepository.this.updater = new GeneralDataUpdater(dataFolder, client, initialTypes, GeneralDataRepository.this::update);
					}
					updater.start();
				}
				this.close();
				return workspace;
			}
		}.open();
	}

	public void stop() {
		if (updater != null) {
			updater.stop();
		}
	}

	public void registerType(final @NonNull TypeRecord type) {
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

	public void addListener(TypeRecord typeRecord, IUpdateListener l) {
		listeners.computeIfAbsent(typeRecord, k -> new ConcurrentLinkedQueue<>()).add(l);
	}

	public void removeListener(TypeRecord typeRecord, IUpdateListener l) {
		listeners.get(typeRecord).remove(l);
	}
}
