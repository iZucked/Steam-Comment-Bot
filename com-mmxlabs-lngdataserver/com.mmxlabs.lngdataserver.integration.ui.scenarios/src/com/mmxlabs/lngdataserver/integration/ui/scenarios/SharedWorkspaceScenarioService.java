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
import java.util.function.Supplier;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.commons.http.IProgressListener;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.api.SharedWorkspacePathUtils;
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
	public ScenarioInstance copyInto(final Container parent, final ScenarioModelRecord tmpRecord, final String name, @Nullable final IProgressMonitor progressMonitor) throws Exception {
		if (serviceModel.isOffline()) {
			return null;
		}

		try {
			if (progressMonitor != null) {
				// progressMonitor.beginTask("Copy", 1000);
			}
			final String path = SharedWorkspacePathUtils.getPathFor(parent, name);
			final File f = ScenarioStorageUtil.storeToTemporaryFile(tmpRecord);
			try {
				updater.pause();
				final String uuid = client.uploadScenario(f, path, wrapMonitor(progressMonitor));
				if (uuid != null) {
					final Path target = Paths.get(baseCaseFolder.getAbsolutePath(), String.format("%s.lingo", uuid));
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
			} catch (final IOException e) {
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
	public ScenarioInstance copyInto(final Container parent, final IScenarioDataProvider scenarioDataProvider, final String name, @Nullable final IProgressMonitor progressMonitor) throws Exception {
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
		// Note: while this is recursive, we do assume a child first deletion set of calls as defined in DeleteScenarioCommandHandler
		final List<String> uuidsToDelete = new LinkedList<>();
		recursiveDelete(container, uuidsToDelete);
		for (final String uuid : uuidsToDelete) {

			try {
				client.deleteScenario(uuid);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (container instanceof Container) {
			updater.removePath(SharedWorkspacePathUtils.getPathFor(container));
			RunnerHelper.asyncExec(() -> container.getParent().getElements().remove(container));
		}
		try {
			updater.refresh();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void recursiveDelete(final Container parent, final List<String> uuids) {
		if (parent instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) parent;
			uuids.add(scenarioInstance.getExternalID());
		}
		for (final Container c : parent.getElements()) {
			recursiveDelete(c, uuids);
		}

	}

	@Override
	public void fireEvent(final ScenarioServiceEvent event, final ScenarioInstance scenarioInstance) {
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
		final File workspaceLocationFile = workspaceLocation.toFile();

		baseCaseFolder = new File(workspaceLocationFile.getAbsolutePath() + File.separator + "scenarios" + File.separator + "team");
		if (!baseCaseFolder.exists()) {
			baseCaseFolder.mkdirs();
		}

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
		final String basePath = SharedWorkspacePathUtils.getPathFor(destination);
		try {
			for (final Container c : elements) {
				if (isThisScenarioService(c)) {
					recursiveMoveInto(c, basePath);
				}
			}
		} catch (final Exception e) {
			RunnerHelper.asyncExec(display -> MessageDialog.openError(display.getActiveShell(), "Error moving scenarios",
					"Unable to rename one or more scenarios due to name conflict. See error log for more details"));
			log.error("Error moving scenarios " + e.getMessage(), e);
		}
		try {
			updater.refresh();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isThisScenarioService(final Container c) {
		Container parent = c;
		while (parent != null) {
			if (parent instanceof ScenarioService) {
				return parent == serviceModel;
			}
			parent = parent.getParent();
		}
		return false;
	}

	private void recursiveMoveInto(final Container c, final String basePath) throws IOException {
		if (c instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) c;
			client.rename(scenarioInstance.getExternalID(), SharedWorkspacePathUtils.addChildSegment(basePath, scenarioInstance.getName()));
		}

		for (final Container child : c.getElements()) {
			recursiveMoveInto(child, SharedWorkspacePathUtils.addChildSegment(basePath, c.getName()));
		}

	}

	@Override
	public void makeFolder(final Container parent, final String name) {
		updater.createFolderFromUI(parent, name, true);
		return;
	}

	@Override
	public String getSerivceID() {
		return "team-workspace-" + serviceName;
	}

	private ScenarioInstance constructInstance(final File f) {
		final URI archiveURI = URI.createFileURI(f.getAbsolutePath());
		final Manifest manifest = ScenarioStorageUtil.loadManifest(f, getScenarioCipherProvider());
		if (manifest != null) {
			final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
			scenarioInstance.setReadonly(true);
			scenarioInstance.setUuid(manifest.getUUID());

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

			final ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, true, false, false, getScenarioCipherProvider());
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

	private final EContentAdapter serviceModelAdapter = new EContentAdapter() {

		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {
			super.notifyChanged(notification);

			if (notification.isTouch()) {
				return;
			}

			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getContainer_Name()) {
				if (notification.getNewStringValue().contains("/")) {
					// Do not allow forward slashed in names - used as separator in service backend
					// Note: This will trigger another notification to hit the else statement
					RunnerHelper.asyncExec(() -> ((Container) notification.getNotifier()).setName(notification.getNewStringValue().replaceAll("/", "_")));
				} else {
					if (renameWalk((Container) notification.getNotifier(), notification.getOldStringValue(), notification.getNewStringValue())) {
						RunnerHelper.asyncExec(display -> MessageDialog.openError(display.getActiveShell(), "Error renaming scenarios",
								"Unable to rename one or more scenarios due to name conflict. See error log for more details"));
					}
				}
			}
		}

	};

	protected boolean renameWalk(final @NonNull Container container, final @NonNull String oldName, final @NonNull String newName) {
		if (container instanceof ScenarioService) {
			return false;
		}

		boolean error = false;
		final Container parent = container.getParent();
		final String basePath = SharedWorkspacePathUtils.getPathFor(parent);

		if (container instanceof ScenarioInstance) {
			final ScenarioInstance scenarioInstance = (ScenarioInstance) container;
			try {
				client.rename(scenarioInstance.getExternalID(), SharedWorkspacePathUtils.addChildSegment(basePath, newName));
			} catch (final IOException e) {
				log.error("Error renaming scenario " + e.getMessage(), e);
				error = true;
			}
		}

		error |= renameChildren(container, SharedWorkspacePathUtils.addChildSegment(basePath, oldName), SharedWorkspacePathUtils.addChildSegment(basePath, newName));
		return error;
	}

	private boolean renameChildren(final @NonNull Container container, final @NonNull String oldBasePath, final @NonNull String newBasePath) {

		boolean error = false;
		for (final Container c : container.getElements()) {
			if (c instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) c;
				try {
					client.rename(scenarioInstance.getExternalID(), SharedWorkspacePathUtils.addChildSegment(newBasePath, c.getName()));
				} catch (final IOException e) {
					log.error("Error renaming scenario " + e.getMessage(), e);
					error = true;
				}
			}
			error |= renameChildren(c, SharedWorkspacePathUtils.addChildSegment(oldBasePath, c.getName()), SharedWorkspacePathUtils.addChildSegment(newBasePath, c.getName()));

		}
		return error;
	}

	private IProgressListener wrapMonitor(final IProgressMonitor monitor) {
		if (monitor == null) {
			return null;
		}

		return new IProgressListener() {
			boolean firstCall = true;

			@Override
			public void update(final long bytesRead, final long contentLength, final boolean done) {
				if (firstCall) {
					int total = (int) (contentLength / 1000L);
					if (total == 0) {
						total = 1;
					}
					monitor.beginTask("Transfer", total);
					firstCall = false;
				}
				final int worked = (int) (bytesRead / 1000L);
				monitor.worked(worked);
			}
		};
	}

	@Override
	public <U extends Container> @NonNull U executeAdd(@NonNull final Container viewInstance, @NonNull final Supplier<@NonNull U> factory) {
		final @NonNull U child = factory.get();
		if (child instanceof Folder) {
			return (U) updater.createFolderFromUI(viewInstance, child.getName(), true);
		} else {
			RunnerHelper.syncExec(() -> {
				viewInstance.getElements().add(child);
			});
		}
		return child;
	}
}
