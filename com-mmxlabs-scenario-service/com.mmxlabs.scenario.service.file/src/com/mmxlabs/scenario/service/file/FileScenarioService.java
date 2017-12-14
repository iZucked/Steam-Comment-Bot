/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants2;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.file.internal.Activator;
import com.mmxlabs.scenario.service.file.internal.FileScenarioServiceBackup;
import com.mmxlabs.scenario.service.file.preferences.PreferenceConstants;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ResourceHelper;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class FileScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(FileScenarioService.class);

	private static final String PROPERTY_MODEL = "com.mmxlabs.scenario.service.file.model";

	private final ResourceSet resourceSet = new ResourceSetImpl();
	private Resource resource;

	private final Map<Object, Object> options;

	private URI storeURI;

	private final Semaphore backupLock = new Semaphore(1);

	private Job createArchiveJob;

	private Job moveJob;

	public FileScenarioService() {
		super("My Scenarios");
		options = new HashMap<Object, Object>();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
	}

	public void start(final ComponentContext context) throws InterruptedException {

		final Dictionary<?, ?> d = context.getProperties();

		final Object value = d.get(PROPERTY_MODEL);

		if (value == null) {
			throw new RuntimeException("FileScenarioService: No model URI property set");
		}
		final String modelURIString = value.toString();

		final IPath workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		storeURI = URI.createFileURI(workspaceLocation + modelURIString);

		// Initial model load
		new Thread(() -> {
			// Trigger workspace backup before the full initialisation
			try {
				backupWorkspace();
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getServiceModel();
			setReady();
		}).start();
	}

	/**
	 * Creates a complete backup of the file scenario service data. First we create a local archive as the rest of the application will block to avoid data modification issues. Then we copy to the
	 * remote location after releasing the lock.
	 */
	private void backupWorkspace() throws InterruptedException {

		if (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_ENABLED_KEY)) {

			// First job is blocking - creating the zip file itself.
			// The job will release the lock
			backupLock.acquire();
			// Create a schedule rule to avoid jobs being run concurrently
			final ISchedulingRule rule = new ISchedulingRule() {

				@Override
				public boolean isConflicting(final ISchedulingRule rule) {
					return rule == this;
				}

				@Override
				public boolean contains(final ISchedulingRule rule) {
					return rule == this;
				}
			};

			// Get the zip file name
			final String targetName = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_NAME_KEY);

			// Use the URI to resolve to the scenario-service directory (or what ever is
			// currently defined in the service properties).
			final URI resolveURI = resolveURI(targetName);
			final File localArchive = new File(resolveURI.toFileString());

			createArchiveJob = new Job("Archive scenario data") {

				@Override
				public IStatus run(final IProgressMonitor monitor) {
					monitor.beginTask("Archive scenario data", IProgressMonitor.UNKNOWN);
					try {
						new FileScenarioServiceBackup().backup(localArchive, localArchive.getParentFile());
					} catch (final Exception e) {
						log.error("Error archiving scenario data: " + e.getMessage(), e);
						return Status.CANCEL_STATUS;
					} finally {
						// Release the lock
						backupLock.release();
					}
					createArchiveJob = null;
					return Status.OK_STATUS;
				}
			};
			createArchiveJob.setProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY, Boolean.TRUE);
			createArchiveJob.setSystem(true);
			createArchiveJob.setUser(true);
			createArchiveJob.setPriority(Job.SHORT);
			createArchiveJob.setRule(rule);
			createArchiveJob.schedule(0);

			// Second job to copy archive to remote mapped location then "secure" delete the
			// archive
			final String dest = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_PATH_KEY);
			final File destinationLocation = new File(dest);
			if (destinationLocation.exists() && destinationLocation.isDirectory()) {

				moveJob = new Job("Storing scenario data backup in " + destinationLocation.toString()) {

					@Override
					public IStatus run(final IProgressMonitor monitor) {
						monitor.beginTask("Storing scenario data backup in " + destinationLocation.toString(), 10);
						try {
							final File destFile = new File(destinationLocation + "/" + targetName);
							moveLocalArchive(localArchive, destFile, new SubProgressMonitor(monitor, 10));
						} catch (final IOException e) {
							log.error("Error moving archive to remote " + e.getMessage(), e);
						} finally {
							monitor.done();
						}
						moveJob = null;
						return Status.OK_STATUS;
					}
				};
				moveJob.setProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY, Boolean.TRUE);

				moveJob.setSystem(false);
				moveJob.setUser(true);
				moveJob.setPriority(Job.SHORT);
				moveJob.setRule(rule);
				moveJob.schedule(0);
			} else {
				log.error("Error moving backup archive, remote directory does not exist " + dest);
			}
		}
	}

	/**
	 * Move the source archive to the destination file and delete the source
	 * 
	 * @param source
	 * @param dest
	 * @param monitor
	 * @throws IOException
	 */
	private void moveLocalArchive(final File source, final File dest, final IProgressMonitor monitor) throws IOException {

		monitor.beginTask("Move archive to destination", 4);
		try {
			Files.copy(source, dest);
			monitor.worked(3);
			FileDeleter.delete(source, LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE));
			monitor.worked(1);
		} finally {
			monitor.done();
		}
	}

	public void stop(final ComponentContext context) {
		// save current state
		save();

		// Cancel existing jobs
		{
			// Pin references
			Job j = createArchiveJob;
			if (j != null) {
				j.cancel();
				createArchiveJob = null;
			}
			j = moveJob;
			if (j != null) {
				j.cancel();
				moveJob = null;
			}
		}
		if (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_ENABLED_KEY)) {

			// Run the backup
			try {
				final ProgressMonitorDialog p = new ProgressMonitorDialog(null);

				final IRunnableWithProgress runnable = (monitor) -> {
					monitor.beginTask("Backup scenario data", 20);
					try {

						monitor.subTask("Create archive");
						// Get the zip file name
						final String targetName = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_NAME_KEY);

						// Use the URI to resolve to the scenario-service directory (or what ever is
						// currently defined in the service properties).
						final URI resolveURI = resolveURI(targetName);
						final File localArchive = new File(resolveURI.toFileString());

						new FileScenarioServiceBackup().backup(localArchive, localArchive.getParentFile());
						monitor.worked(10);
						monitor.subTask("Move archive to destination");
						// Second job to copy archive to remote mapped location then "secure" delete the
						// archive
						final String dest = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_PATH_KEY);
						final File destinationLocation = new File(dest);
						if (destinationLocation.exists() && destinationLocation.isDirectory()) {
							final File destFile = new File(destinationLocation + File.pathSeparator + targetName);
							moveLocalArchive(localArchive, destFile, new SubProgressMonitor(monitor, 10));
						}
					} catch (final Exception e) {
						log.error("Error performing backup: " + e.getMessage(), e);
					} finally {
						monitor.done();
					}
				};

				p.setOpenOnRun(true);
				p.run(true, false, runnable);
			} catch (final InvocationTargetException | InterruptedException e) {
				log.error("Error performing backup: " + e.getMessage(), e);
			}
		}
	}

	public void save() {
		if (resource != null) {
			try {
				synchronized (resource) {
					resource.save(options);
				}
			} catch (final Throwable e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void delete(final Container container) {
		final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(container);

		if (scenarioService != null && scenarioService != this) {
			throw new IllegalArgumentException("Cannot delete a container instance that belongs to another scenario service");
		}
		{
			// Recursively delete contents
			while (container.getElements().isEmpty() == false) {
				// assert false; // Caller should do this
				delete(container.getElements().get(0));
			}
		}

		// Remove container ref from it's parent
		final EObject parent = container.eContainer();
		if (parent != null) {
			final EStructuralFeature containment = container.eContainingFeature();
			if (containment != null) {
				if (containment.isMany()) {
					@SuppressWarnings("unchecked")
					final EList<EObject> value = (EList<EObject>) parent.eGet(containment);
					while (value.remove(container))
						;
				} else {
					parent.eSet(containment, null);
				}
			}
		}

		// destroy models, if there are any
		if (container instanceof ScenarioInstance) {
			final ScenarioInstance instance = (ScenarioInstance) container;

			if (scenarioService != null) {
				fireEvent(ScenarioServiceEvent.PRE_DELETE, instance);
			}

			@NonNull
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);

			System.gc();
			System.gc();

			if (modelRecord != null) {
				@Nullable
				final ModelReference ref = modelRecord.aquireReferenceIfLoaded("FileScenarioService:1");
				if (ref != null) {
					ref.close();
					modelRecord.dumpReferences();
				}
				SSDataManager.Instance.remove(instance);
				modelRecord.setScenarioInstance(null);
			}
			// TODO: Move to "trash?"
			final File mainFile = new File(resolveURI(instance.getUuid() + ".lingo").toFileString());
			final File backupFile = new File(resolveURI(instance.getUuid() + ".lingo.backup").toFileString());
			if (mainFile.exists()) {
				try {
					FileDeleter.delete(mainFile, LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE));
				} catch (final IOException e) {
					log.error("Whilst deleting instance " + instance.getName() + ", IO exception deleting scenario " + mainFile, e);
				}
			}
			if (backupFile.exists()) {
				try {
					FileDeleter.delete(backupFile, LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE));
				} catch (final IOException e) {
					log.error("Whilst deleting instance " + instance.getName() + ", IO exception deleting scenario backup " + backupFile, e);
				}
			}

		}
	}

	@Override
	public ScenarioInstance copyInto(Container parent, ScenarioModelRecord sourceRecord, String name) throws Exception {

		// Create a new UUID
		final String uuid = EcoreUtil.generateUUID();
		URI archiveURI = resolveURI(String.format("./%s.lingo", uuid));

		sourceRecord.saveCopyTo(uuid, archiveURI);

		// Create new model nodes
		final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();

		newInstance.setName(name);
		newInstance.setUuid(uuid);
		newInstance.setScenarioVersion(sourceRecord.getManifest().getScenarioVersion());
		newInstance.setVersionContext(sourceRecord.getManifest().getVersionContext());
		newInstance.setClientScenarioVersion(sourceRecord.getManifest().getClientScenarioVersion());
		newInstance.setClientVersionContext(sourceRecord.getManifest().getClientVersionContext());

		newInstance.setRootObjectURI(archiveURI.toString());

		final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
		metadata.setContentType(sourceRecord.getManifest().getScenarioType());
		// Update last modified date
		metadata.setLastModified(new Date());

		newInstance.setMetadata(metadata);

		ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, false, true, getScenarioCipherProvider());

		modelRecord.setScenarioInstance(newInstance);
		modelRecord.setName(newInstance.getName());
		SSDataManager.Instance.register(newInstance, modelRecord);
		newInstance.setManifest(modelRecord.getManifest());

		// Finally add to node in the service model.
		parent.getElements().add(newInstance);

		return newInstance;
	}

	@Override
	public ScenarioInstance copyInto(Container parent, IScenarioDataProvider scenarioDataProvider, String name) throws Exception {

		// Create a new UUID
		final String uuid = EcoreUtil.generateUUID();
		URI archiveURI = resolveURI(String.format("./%s.lingo", uuid));
		{
			// try (ModelReference ref = aquireReference("ModelRecord:saveAsCopy")) {
			final EObject rootObject = EcoreUtil.copy(scenarioDataProvider.getScenario());
			ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
				final Map<String, EObject> extraDataObjects = ScenarioStorageUtil.createCopyOfExtraData(scenarioDataProvider);
				ScenarioStorageUtil.storeToURI(uuid, rootObject, extraDataObjects, scenarioDataProvider.getManifest(), archiveURI, scenarioCipherProvider);
			});
		}

		// Create new model nodes
		final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();

		newInstance.setName(name);
		newInstance.setUuid(uuid);
		newInstance.setScenarioVersion(scenarioDataProvider.getManifest().getScenarioVersion());
		newInstance.setVersionContext(scenarioDataProvider.getManifest().getVersionContext());
		newInstance.setClientScenarioVersion(scenarioDataProvider.getManifest().getClientScenarioVersion());
		newInstance.setClientVersionContext(scenarioDataProvider.getManifest().getClientVersionContext());

		newInstance.setRootObjectURI(archiveURI.toString());

		final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();
		metadata.setContentType(scenarioDataProvider.getManifest().getScenarioType());
		// Update last modified date
		metadata.setLastModified(new Date());

		newInstance.setMetadata(metadata);

		ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, false, true, getScenarioCipherProvider());

		modelRecord.setScenarioInstance(newInstance);
		modelRecord.setName(newInstance.getName());
		SSDataManager.Instance.register(newInstance, modelRecord);
		newInstance.setManifest(modelRecord.getManifest());

		// Finally add to node in the service model.
		parent.getElements().add(newInstance);

		return newInstance;
	}

	final EContentAdapter saveAdapter = new EContentAdapter() {

		@Override
		public void notifyChanged(final Notification notification) {
			super.notifyChanged(notification);
			if (notification.isTouch() == false) {
				if (notification.getFeature() instanceof EStructuralFeature && ((EStructuralFeature) notification.getFeature()).isTransient())
					return;
				save();
			}
		}

		@Override
		protected void addAdapter(final Notifier notifier) {
			if (!(notifier instanceof ModelReference)) {
				notifier.eAdapters().add(this);
			}
		}

	};

	@Override
	protected synchronized ScenarioService initServiceModel() {
		// Attempt to get backup lock - this should block until the backup is created.
		backupLock.acquireUninterruptibly();
		// Unlock as we do not really need it
		backupLock.release();
		synchronized (this) {
			// HACk
			while (!PlatformUI.isWorkbenchRunning()) {
				try {
					this.wait(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		boolean attemptBackup = true;
		boolean mainFileExists = false;
		boolean backupFileExists = false;
		resource = resourceSet.createResource(storeURI);
		if (storeURI.isFile() && !new File(storeURI.toFileString()).exists()) {
			mainFileExists = false;
		} else {
			// Assume it exists
			mainFileExists = true;
		}
		boolean resourceExisted = false;
		final URI backupURI = URI.createURI(storeURI.toString() + ".backup");
		try {
			resource.load(options);
			resourceExisted = true;
		} catch (final IOException ex) {
			// // Load failure, try to restore backup

			// Do not create a new backup in case we need to manually restore
			attemptBackup = false;
			// "Unload" the resource -- this clears the loaded flag
			resource.unload();
			// Change to the backup URI
			if (backupURI.isFile() && !new File(backupURI.toFileString()).exists()) {
				backupFileExists = false;
				if (mainFileExists) {
					log.error("Error reading main scenario service models.", ex);
				}
			} else {
				// Assume it exists
				backupFileExists = true;

				resource.setURI(backupURI);

				try {
					resource.load(options);
					resourceExisted = true;
					log.warn("Scenario service model restored from backup.");
				} catch (final IOException ex2) {
					if (mainFileExists && backupFileExists) {
						log.error("Error reading both main and backup scenario service models.", ex2);
					} else if (!mainFileExists && backupFileExists) {
						log.error("Error reading backup scenario service models.", ex2);
					} else if (mainFileExists && !backupFileExists) {
						log.error("Error reading main scenario service models.", ex2);
					}
				} finally {
					// Restore original URI for saves later on
					resource.setURI(storeURI);
				}
			}
		}

		if (resource.getContents().isEmpty()) {
			resource.getContents().add(ScenarioServiceFactory.eINSTANCE.createScenarioService());
			if (resourceExisted) {
				// consider loading backup?
			}
		} else if (attemptBackup) {
			// back-up resource
			log.debug("Backing up " + storeURI);
			try (final InputStream inputStream = resourceSet.getURIConverter().createInputStream(storeURI)) {
				try (final OutputStream outputStream = resourceSet.getURIConverter().createOutputStream(backupURI)) {
					int x;
					while ((x = inputStream.read()) != -1) {
						outputStream.write(x);
					}
				} catch (final IOException e) {

				}
			} catch (final IOException e) {

			}
		}

		final ScenarioService result = (ScenarioService) resource.getContents().get(0);

		result.setServiceID(getSerivceID());
		result.setDescription("File scenario service with store " + storeURI);
		result.setLocal(true);

		// modify any old scenarios to fix wrong pointing
		makeRelativeURIs(result);
		recoverLostScenarios(result);

		result.eAdapters().add(saveAdapter);

		result.setSupportsForking(true);
		result.setSupportsImport(true);

		result.eAllContents().forEachRemaining(e -> {
			if (e instanceof ScenarioInstance) {
				ScenarioInstance scenarioInstance = (ScenarioInstance) e;
				URI archiveURI = resolveURI(scenarioInstance.getUuid() + ".lingo");
				if (!new File(archiveURI.toFileString()).exists()) {
					// Maybe old style?
					URI oldURI = resolveURI(scenarioInstance.getUuid() + ".xmi");
					File oldScenarioFile = new File(oldURI.toFileString());
					if (oldScenarioFile.exists()) {
						Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
						manifest.setScenarioType(scenarioInstance.getMetadata().getContentType());
						manifest.setVersionContext(scenarioInstance.getVersionContext());
						manifest.setScenarioVersion(scenarioInstance.getScenarioVersion());
						manifest.setClientVersionContext(scenarioInstance.getClientVersionContext());
						manifest.setClientScenarioVersion(scenarioInstance.getClientScenarioVersion());
						try {
							ScenarioStorageUtil.storeToURI(scenarioInstance.getUuid(), oldURI, Collections.emptyMap(), manifest, archiveURI, getScenarioCipherProvider());

							// Delete the old file.
							FileDeleter.delete(oldScenarioFile, true);

							// Remove instances backup
							{
								URI backupScenarioURI = resolveURI("instances/" + scenarioInstance.getUuid() + ".xmi");
								File backupScenarioFile = new File(backupScenarioURI.toFileString());
								if (backupScenarioFile.exists()) {
									FileDeleter.delete(backupScenarioFile, true);
								}
							}

						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
				try {
					ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, false, true, getScenarioCipherProvider());
					if (modelRecord != null) {
						modelRecord.setName(scenarioInstance.getName());
						modelRecord.setScenarioInstance(scenarioInstance);
						SSDataManager.Instance.register(scenarioInstance, modelRecord);
						scenarioInstance.setRootObjectURI(archiveURI.toString());
						scenarioInstance.setManifest(modelRecord.getManifest());
					}
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
		});

		return result;
	}

	/**
	 * Simple method to recover any lost scenarios (those which are in the instances/ dir, but not in the scenario service)
	 * 
	 * @param result
	 */
	private void recoverLostScenarios(final ScenarioService result) {

		if (true) {
			// This does nothing in the new .lingo model
			return;
		}

		// gather up all UUIDs
		final HashSet<String> allUUIDs = new HashSet<String>();
		final TreeIterator<EObject> iterator = result.eAllContents();
		while (iterator.hasNext()) {
			final EObject o = iterator.next();
			if (o instanceof ScenarioInstance) {
				allUUIDs.add(((ScenarioInstance) o).getUuid());
			}
		}
		final Folder lostAndFound = ScenarioServiceFactory.eINSTANCE.createFolder();
		lostAndFound.setName("Lost & Found");
		// now look for all instances in the spare dir which don't have UUIDs
		final File f = new File(resolveURI("instances/").toFileString());
		if (f.exists() && f.isDirectory()) {
			final HashMap<String, ScenarioInstance> recoveredInstances = new HashMap<String, ScenarioInstance>();
			final HashSet<String> recoveredSubInstances = new HashSet<String>();
			for (final File instanceFile : f.listFiles()) {
				if (instanceFile == null) {
					continue;
				}
				if (instanceFile.getName().endsWith(".xmi")) {
					final String instanceUUID = instanceFile.getName().substring(0, instanceFile.getName().length() - 4);
					if (!allUUIDs.contains(instanceUUID)) {
						log.warn("Recovering instance " + instanceUUID);
						// recover the instance in f, if possible
						try {
							final Resource resource = ResourceHelper.loadResource(resourceSet, URI.createFileURI(instanceFile.getAbsolutePath()));
							final EObject o = resource.getContents().get(0);
							if (o instanceof ScenarioInstance) {
								final ScenarioInstance theInstance = (ScenarioInstance) o;
								final TreeIterator<EObject> contentIterator = o.eAllContents();
								while (contentIterator.hasNext()) {
									final EObject sub = contentIterator.next();
									if (sub instanceof ScenarioInstance) {
										final ScenarioInstance subInstance = (ScenarioInstance) sub;
										if (recoveredInstances.containsKey(subInstance.getUuid())) {
											recoveredInstances.remove(subInstance.getUuid());
										}
										recoveredSubInstances.add(subInstance.getUuid());
									}
								}
								if (!recoveredSubInstances.contains(theInstance.getUuid())) {
									recoveredInstances.put(theInstance.getUuid(), theInstance);
								}

							}
							resourceSet.getResources().remove(resource);
						} catch (final Throwable th) {

						}
					}
				}
			}
			lostAndFound.getElements().addAll(recoveredInstances.values());
		}
		if (lostAndFound.getElements().size() > 0) {
			result.getElements().add(lostAndFound);
		}
	}

	private void makeRelativeURIs(final Container container) {
		if (container instanceof ScenarioInstance) {
			final ScenarioInstance instance = (ScenarioInstance) container;

			{
				final String uriString = instance.getRootObjectURI();
				final URI uri = URI.createURI(uriString);
				if (uri.isRelative() == false) {
					final URI derezzed = uri.deresolve(storeURI);
					if (derezzed.isRelative()) {
						instance.setRootObjectURI(derezzed.toString());
					}
				}
			}
		}
		for (final Container c : container.getElements()) {
			makeRelativeURIs(c);
		}
	}

	@Override
	public URI resolveURI(final String uriString) {
		final URI uri = URI.createURI(uriString);
		if (uri.isRelative()) {
			return uri.resolve(storeURI);
		} else {
			return uri;
		}
	}

	@Override
	public String getSerivceID() {
		return "file-scenario-service";
	}

	@Override
	public void makeFolder(final Container parent, final String name) {
		if (parent instanceof ScenarioInstance) {
			return;
		}

		executeAdd(parent, () -> {
			final Folder f = ScenarioServiceFactory.eINSTANCE.createFolder();
			f.setName(name);
			return f;
		});
	}

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {
		destination.getElements().addAll(elements);
	}
}
