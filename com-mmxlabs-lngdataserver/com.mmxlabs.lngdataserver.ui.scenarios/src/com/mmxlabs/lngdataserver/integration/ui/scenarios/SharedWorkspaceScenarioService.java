/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SharedWorkspaceServiceClient;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.internal.SharedScenarioUpdater;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class SharedWorkspaceScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(SharedWorkspaceScenarioService.class);

	public SharedWorkspaceScenarioService() {
		super("Team");
	}

	/**
	 * Root of the directory tree
	 */
	private File dataPath;

	/**
	 * Name of this service
	 */
	private String serviceName;

	private File baseCaseFolder;

	private SharedWorkspaceServiceClient client;

	private SharedScenarioUpdater updater;

	@Override
	public ScenarioInstance copyInto(Container parent, ScenarioModelRecord tmpRecord, String name, @Nullable IProgressMonitor progressMonitor) throws Exception {
		if (serviceModel.isOffline()) {
			return null;
		}

		try {
			if (progressMonitor != null) {
				// progressMonitor.beginTask("Copy", 1000);
			}
			StringBuilder path = new StringBuilder();
			Container p = parent;
			path.append(name);
			while (p != null && !(p instanceof ScenarioService)) {
				path.insert(0, p.getName() + "/");
				p = p.getParent();
			}

			File f = ScenarioStorageUtil.storeToTemporaryFile(tmpRecord);
			try {
				updater.pause();
				String uuid = client.uploadScenario(f, path.toString(), wrapMonitor(progressMonitor));
				if (uuid != null) {
					Path target = Paths.get(baseCaseFolder.getAbsolutePath(), String.format("%s.lingo", uuid));
					Files.copy(f.toPath(), target);
				}
			} finally {
				updater.resume();
				if (f != null) {
					f.delete();
				}
			}
			try {
				updater.refresh();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		} finally {
			if (progressMonitor != null) {
				progressMonitor.done();
			}
		}
	}

	@Override
	public ScenarioInstance copyInto(Container parent, IScenarioDataProvider scenarioDataProvider, String name, @Nullable IProgressMonitor progressMonitor) throws Exception {
		try {
			if (progressMonitor != null) {
				progressMonitor.beginTask("Copy", 1);
			}
			return null;
		} finally {
			if (progressMonitor != null) {
				progressMonitor.done();
			}
		}
	}

	@Override
	public void delete(final Container container) {
		if (serviceModel.isOffline()) {
			return;
		}

		List<String> uuidsToDelete = new LinkedList<>();
		recursiveDelete(container, uuidsToDelete);
		for (String uuid : uuidsToDelete) {
			try {
				client.deleteScenario(uuid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (container instanceof Folder) {
			RunnerHelper.asyncExec(() -> container.getParent().getElements().remove(container));
		}
		try {
			updater.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void recursiveDelete(Container parent, List<String> uuids) {
		if (parent instanceof ScenarioInstance) {
			ScenarioInstance scenarioInstance = (ScenarioInstance) parent;
			uuids.add(scenarioInstance.getExternalID());
		}
		for (Container c : parent.getElements()) {
			recursiveDelete(c, uuids);
		}

	}

	@Override
	public void fireEvent(ScenarioServiceEvent event, ScenarioInstance scenarioInstance) {
		super.fireEvent(event, scenarioInstance);
	}

	@Override
	protected ScenarioService initServiceModel() {
		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("Team workspace");
		serviceModel.setLocal(false);
		serviceModel.setOffline(true);
		serviceModel.setServiceID(getSerivceID());
		serviceModel.eAdapters().add(serviceModelAdapter);

		return serviceModel;
	}

	@Override
	public URI resolveURI(final String uri) {
		return URI.createURI(uri);
	}

	public void start() throws IOException {

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		File workspaceLocationFile = workspaceLocation.toFile();

		baseCaseFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "scenarios" + File.separator + "team");
		if (!baseCaseFolder.exists()) {
			baseCaseFolder.mkdirs();
		}

		// storeURI = URI.createFileURI(baseCaseFolder.getCanonicalPath());

		client = new SharedWorkspaceServiceClient();

		// Initial model load
		new Thread(() -> {

			getServiceModel();
			setReady();
			this.updater = new SharedScenarioUpdater(serviceModel, baseCaseFolder, client);
			updater.start();
		}).start();
	}

	public void stop() {
		if (updater != null) {
			updater.stop();
		}
	}

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {
		if (serviceModel.isOffline()) {
			return;
		}
		String basePath = "";
		final StringBuilder sb = new StringBuilder();
		Container parent = destination;
		boolean first = true;
		while (parent != null && !(parent instanceof ScenarioService)) {
			if (!first) {
				sb.insert(0, "/");
			}

			sb.insert(0, parent.getName());
			parent = parent.getParent();
			first = false;
		}
		if (sb.length() > 0) {
			sb.append("/");
		}
		basePath = sb.toString();

		for (Container c : elements) {
			if (isThisScenarioService(c)) {
				recursiveMoveInto(c, basePath);
			}
		}

		try {
			updater.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return;
	}

	private boolean isThisScenarioService(Container c) {
		Container parent = c;
		while (parent != null) {
			if (parent instanceof ScenarioService) {
				return parent == serviceModel;
			}
			parent = parent.getParent();
		}
		return false;
	}

	private void recursiveMoveInto(Container c, String basePath) {
		if (c instanceof ScenarioInstance) {
			ScenarioInstance scenarioInstance = (ScenarioInstance) c;
			try {
				client.rename(scenarioInstance.getExternalID(), basePath + scenarioInstance.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// client.uploadScenario(file, path);
		}

		for (Container child : c.getElements()) {
			recursiveMoveInto(child, basePath + c.getName() + "/");
		}

	}

	@Override
	public void makeFolder(final Container parent, final String name) {

		RunnerHelper.asyncExec(() -> {
			Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
			f.setName(name);
			parent.getElements().add(f);
			updater.registerFolder(f);
		});
		return;
	}

	@Override
	public String getSerivceID() {
		return "team-workspace-" + serviceName;
	}

	private ScenarioInstance constructInstance(File f) {
		URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f, getScenarioCipherProvider());
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(true);
			scenarioInstance.setUuid(manifest.getUUID());

			final URI fileURI = URI.createFileURI(f.getAbsolutePath());
			scenarioInstance.setRootObjectURI(archiveURI.toString());

			final String scenarioname = f.getName().replaceFirst("\\.lingo$", "");
			scenarioInstance.setName(scenarioname);
			scenarioInstance.setVersionContext(manifest.getVersionContext());
			scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

			scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
			scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

			final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
			scenarioInstance.setMetadata(meta);
			meta.setContentType(manifest.getScenarioType());

			ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, true, false, false, getScenarioCipherProvider());
			if (modelRecord != null) {
				modelRecord.setName(scenarioInstance.getName());
				modelRecord.setScenarioInstance(scenarioInstance);
				SSDataManager.Instance.register(scenarioInstance, modelRecord);
				scenarioInstance.setRootObjectURI(archiveURI.toString());
			}

			return scenarioInstance;
		}
		return null;
	}

	private EContentAdapter serviceModelAdapter = new EContentAdapter() {

		@Override
		public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {
			super.notifyChanged(notification);

			if (notification.isTouch()) {
				return;
			}

			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getContainer_Name()) {
				if (notification.getNewStringValue().contains("/")) {
					// Do not allow forward slashed in names - used as separator in service backend
					RunnerHelper.asyncExec(() -> ((Container) notification.getNotifier()).setName(notification.getNewStringValue().replaceAll("/", "_")));
				} else {
					renameWalk((Container) notification.getNotifier(), notification.getOldStringValue(), notification.getNewStringValue());
				}
			}
		}

	};

	protected void renameWalk(Container container, String oldName, String newName) {
		if (container instanceof ScenarioService) {
			return;
		}

		String basePath = "";
		final StringBuilder sb = new StringBuilder();
		Container parent = container.getParent();
		boolean first = true;
		while (parent != null && !(parent instanceof ScenarioService)) {
			if (!first) {
				sb.insert(0, "/");
			}

			sb.insert(0, parent.getName());
			parent = parent.getParent();
			first = false;
		}
		if (sb.length() > 0) {
			sb.append("/");
		}
		basePath = sb.toString();
		if (container instanceof ScenarioInstance) {
			ScenarioInstance scenarioInstance = (ScenarioInstance) container;
			try {
				client.rename(scenarioInstance.getExternalID(), basePath + newName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		updater.updatePath(basePath + oldName, basePath + newName);
		renameChildren(container, basePath + oldName, basePath + newName);
	}

	private void renameChildren(Container container, String oldBasePath, String newBasePath) {

		for (Container c : container.getElements()) {
			if (c instanceof ScenarioInstance) {
				ScenarioInstance scenarioInstance = (ScenarioInstance) c;
				try {
					client.rename(scenarioInstance.getExternalID(), newBasePath + "/" + c.getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			renameChildren(c, oldBasePath + "/" + c.getName(), newBasePath + "/" + c.getName());

		}

	}

	private IProgressListener wrapMonitor(IProgressMonitor monitor) {
		if (monitor == null) {
			return null;
		}

		return new IProgressListener() {
			boolean firstCall = true;

			@Override
			public void update(long bytesRead, long contentLength, boolean done) {
				if (firstCall) {
					int total = (int) (contentLength / 1000L);
					if (total == 0) {
						total = 1;
					}
					monitor.beginTask("Transfer", total);
					firstCall = false;
				}
				int worked = (int) (bytesRead / 1000L);
				monitor.worked(worked);
			}
		};
	}
}
