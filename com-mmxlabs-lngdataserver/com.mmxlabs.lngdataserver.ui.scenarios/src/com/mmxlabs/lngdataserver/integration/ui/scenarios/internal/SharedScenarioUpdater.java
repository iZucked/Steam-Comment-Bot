package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.emf.common.util.URI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SharedWorkspaceServiceClient;
import com.mmxlabs.lngdataserver.server.IUpstreamDetailChangedListener;
import com.mmxlabs.lngdataserver.server.UpstreamUrlProvider;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.ui.ScenarioServiceModelUtils;

public class SharedScenarioUpdater {

	private final SharedWorkspaceServiceClient client;

	private final ExecutorService taskExecutor;

	private final Map<String, ScenarioInstance> mapping = new ConcurrentHashMap<>();
	private final Map<String, Container> pathMap = new ConcurrentHashMap<>();
	private final ScenarioService modelRoot;
	private final File basePath;
	private Instant lastModified = Instant.EPOCH;
	private IUpstreamDetailChangedListener detailChangedListener = new IUpstreamDetailChangedListener() {

		@Override
		public void changed() {
			// Reset to trigger refresh
			lastModified = Instant.EPOCH;

		}
	};

	public SharedScenarioUpdater(final ScenarioService modelRoot, final File basePath, final SharedWorkspaceServiceClient client) {
		this.modelRoot = modelRoot;
		this.basePath = basePath;
		this.client = client;
		taskExecutor = Executors.newSingleThreadExecutor();
		UpstreamUrlProvider.INSTANCE.registerDetailsChangedLister(detailChangedListener);
	}

	public void dispose() {
		UpstreamUrlProvider.INSTANCE.deregisterDetailsChangedLister(detailChangedListener);
		taskExecutor.shutdownNow();
	}

	public void update(final List<Pair<String, String>> scenarios) {

		final Set<String> existingUUIDS = new HashSet<>();
		recursiveUUIDCollector(modelRoot, existingUUIDS);

		if (scenarios != null) {
			for (final Pair<String, String> scenario : scenarios) {
				final String uuid = scenario.getFirst();
				final String path = scenario.getSecond();

				final Container parent = getContainer(path);
				final String name = path.contains("/") ? path.substring(path.lastIndexOf("/") + 1) : path;

				taskExecutor.execute(new AddScenarioTask(parent, name, uuid));
				existingUUIDS.remove(uuid);
			}
		}

		// Clean up deleted items
		for (final String uuid : existingUUIDS) {
			final ScenarioInstance scenarioInstance = mapping.get(uuid);
			if (scenarioInstance != null) {
				ScenarioServiceModelUtils.closeReferences(scenarioInstance);
				final Container parent = scenarioInstance.getParent();
				if (parent != null) {
					RunnerHelper.asyncExec(() -> parent.getElements().remove(scenarioInstance));
				}
				SSDataManager.Instance.releaseModelRecord(scenarioInstance);
				mapping.remove(uuid);

				// TODO: Move .lingo file into delete queue
			}
		}
	}

	private void recursiveUUIDCollector(final Container parent, final Collection<String> uuids) {
		if (parent instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) parent;
			uuids.add(scenarioInstance.getExternalID());
		}
		for (final Container c : parent.getElements()) {
			recursiveUUIDCollector(c, uuids);
		}

	}

	private Container getContainer(final String path) {
		final String[] segments = path.split("/");
		Container parent = modelRoot;

		final StringBuilder pathBuilder = new StringBuilder();
		for (int i = 0; i < segments.length - 1; ++i) {
			if (i > 0) {
				pathBuilder.append("/");
			}
			pathBuilder.append(segments[i]);

			final String segmentPath = pathBuilder.toString();
			if (pathMap.containsKey(segmentPath)) {
				parent = pathMap.get(segmentPath);
			} else {
				final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
				f.setName(segments[i]);
				taskExecutor.execute(new AddFolderTask(parent, f));
				pathMap.put(segmentPath, f);
				parent = f;
			}
		}
		return parent;
	}

	private class AddFolderTask implements Runnable {
		private final Container parent;
		private final Folder f;

		public AddFolderTask(final Container parent, final Folder f) {
			this.parent = parent;
			this.f = f;
		}

		@Override
		public void run() {
			RunnerHelper.syncExecDisplayOptional(() -> parent.getElements().add(f));
		}
	}

	private class AddScenarioTask implements Runnable {
		private final Container parent;
		private final String uuid;
		private final String name;

		public AddScenarioTask(final Container parent, final String name, final String uuid) {
			this.parent = parent;
			this.name = name;
			this.uuid = uuid;
		}

		@Override
		public void run() {
			final File f = new File(String.format("%s%s%s.lingo", basePath, File.separator, uuid));
			if (!f.exists()) {
				try {
					if (!downloadScenario(uuid, f)) {
						// Something went wrong - reset lastModified to trigger another refresh
						lastModified = Instant.EPOCH;
						// Failed!
						return;
					}
				} catch (final IOException e) {
					// Something went wrong - reset lastModified to trigger another refresh
					lastModified = Instant.EPOCH;
					e.printStackTrace();
					return;
				}
			}

			final ScenarioInstance instance = mapping.computeIfAbsent(uuid, u -> loadScenarioFrom(f, u, name));

			RunnerHelper.syncExecDisplayOptional(() -> {
				instance.setName(name);
				parent.getElements().add(instance);
			});
		}
	}

	private boolean downloadScenario(final String uuid, final File f) throws IOException {
		return client.downloadTo(uuid, f);
	}

	protected ScenarioInstance loadScenarioFrom(final File f, final String uuid, final String scenarioname) {
		final URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f);
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(true);
			scenarioInstance.setUuid(manifest.getUUID());
			scenarioInstance.setExternalID(uuid);

			scenarioInstance.setRootObjectURI(archiveURI.toString());

			scenarioInstance.setName(scenarioname);
			scenarioInstance.setVersionContext(manifest.getVersionContext());
			scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

			scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
			scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

			final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
			scenarioInstance.setMetadata(meta);
			meta.setContentType(manifest.getScenarioType());
			// Probably better pass in from service
			ServiceHelper.withOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
				final ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, true, false, false, scenarioCipherProvider);
				if (modelRecord != null) {
					modelRecord.setName(scenarioInstance.getName());
					modelRecord.setScenarioInstance(scenarioInstance);
					SSDataManager.Instance.register(scenarioInstance, modelRecord);
					scenarioInstance.setRootObjectURI(archiveURI.toString());
				}
			});
			return scenarioInstance;
		}
		return null;
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void start() {

		final Thread t = new Thread() {
			@Override
			public void run() {

				while (true) {
					try {
						refresh();

					} catch (final IOException e1) {
						e1.printStackTrace();
					}

					try {
						Thread.sleep(10_000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		};
		t.start();

	}

	/**
	 * UI created folder, so register with the local map to avoid recreating it later
	 * 
	 * @param f
	 */
	public void registerFolder(final Folder f) {

		final StringBuilder sb = new StringBuilder();
		Container parent = f;
		boolean first = true;
		while (parent != null && !(parent instanceof ScenarioService)) {
			if (!first) {
				sb.insert(0, "/");
			}

			sb.insert(0, parent.getName());
			parent = parent.getParent();
			first = false;
		}
		pathMap.put(sb.toString(), f);
	}

	public void updatePath(String oldPath, String newPath) {

		Container value = pathMap.get(oldPath);
		if (value != null) {
			pathMap.put(newPath, value);
		}
	}

	public void refresh() throws IOException {
		final Instant m = client.getLastModified();
		if (m != null) {
			if (m.isAfter(lastModified)) {
				final Pair<List<Pair<String, String>>, Instant> scenariosPair = client.getScenarios();
				if (scenariosPair != null) {
					update(scenariosPair.getFirst());
					lastModified = scenariosPair.getSecond();
				}
			}
		}
	};

}
