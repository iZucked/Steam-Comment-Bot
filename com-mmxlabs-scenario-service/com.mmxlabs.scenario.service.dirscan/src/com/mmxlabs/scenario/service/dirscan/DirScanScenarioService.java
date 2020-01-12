/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.model.util.ResourceHelper;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

public class DirScanScenarioService extends AbstractScenarioService {

	private static final String LINGO_FILE_EXTENSION = ".lingo";

	private static final Logger log = LoggerFactory.getLogger(DirScanScenarioService.class);

	/**
	 * Root of the directory tree
	 */
	private File dataPath;

	/**
	 * Name of this service
	 */
	private String serviceName;

	private final Map<String, WeakReference<Container>> folderMap = new HashMap<>();
	private final Map<String, WeakReference<ScenarioInstance>> scenarioMap = new HashMap<>();
	private final Map<Container, Path> modelToFilesystemMap = new WeakHashMap<>();

	private Thread watchThread;
	private volatile boolean watchThreadRunning;

	/**
	 * A {@link ReadWriteLock} to co-ordinate access between file system watcher and manipulation code. In this case the "write" lock is the watcher and the read locks belong to the manipulation code.
	 * Multiple Filesystem change operations are permitted (this may be a flawed assumption) but the filesystem watch code runs exclusively.
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

	private final @NonNull EContentAdapter serviceModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {

			super.notifyChanged(notification);

			// Process changes and replicate back on FileSystem
			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getContainer_Name()) {

				final Container c = (Container) notification.getNotifier();

				final Path path = modelToFilesystemMap.get(c);
				if (path != null) {
					// Append .lingo if it is a scenario
					final String ext = c instanceof ScenarioInstance ? LINGO_FILE_EXTENSION : "";
					final Path newName = path.getParent().resolve(notification.getNewStringValue() + ext);
					lock.readLock().lock();
					try {
						// Remove from tree as file system watcher will re-add
						if (c instanceof ScenarioInstance) {
							removeFile(path);
						} else {
							removeFolder(path);
						}

						path.toFile().renameTo(newName.toFile());
					} finally {
						lock.readLock().unlock();
						pokeWatchThread();
					}
				}
			}
		}
	};

	public DirScanScenarioService(final @NonNull String name) throws IOException {
		super(name);
	}

	@Override
	public ScenarioInstance copyInto(Container parent, ScenarioModelRecord tmpRecord, String name, @NonNull IProgressMonitor progressMonitor) throws Exception {
		try {
			progressMonitor.beginTask("Copy", 1);
			final String uuid = EcoreUtil.generateUUID();
			final StringBuilder sb = new StringBuilder();
			{
				Container c = parent;
				while (c != null && !(c instanceof ScenarioService)) {
					sb.insert(0, File.separator + c.getName());
					c = c.getParent();
				}

			}
			//
			lock.readLock().lock();
			try {
				final File target = new File(dataPath.toString() + sb.toString() + File.separator + name + LINGO_FILE_EXTENSION);
				URI archiveURI = URI.createFileURI(target.getAbsolutePath());

				if (target.exists()) {
					final boolean[] response = new boolean[1];
					final Display display = PlatformUI.getWorkbench().getDisplay();
					display.syncExec(() -> response[0] = MessageDialog.openQuestion(display.getActiveShell(), "Target exists - overwrite?",
							String.format("File \"%s\" already exists. Do you want to overwrite?", target.getAbsoluteFile())));
					if (!response[0]) {
						return null;
					}
				}
				tmpRecord.saveCopyTo(uuid, archiveURI);

				log.debug("Inserting scenario into {}", parent);

				// Create new model nodes
				final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
				final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();

				// Create a new UUID
				newInstance.setUuid(uuid);
				newInstance.setMetadata(metadata);

				final URI scenarioURI = URI.createFileURI(target.getAbsolutePath());

				final URI destURI = URI.createURI("archive:" + scenarioURI.toString() + "!/rootObject.xmi");
				assert destURI != null;
			} finally {
				lock.readLock().unlock();
				pokeWatchThread();
			}

			progressMonitor.worked(1);

			return null;
		} finally {
			progressMonitor.done();
		}
	}

	@Override
	public ScenarioInstance copyInto(Container parent, IScenarioDataProvider scenarioDataProvider, String name, @NonNull IProgressMonitor progressMonitor) throws Exception {
		try {
			progressMonitor.beginTask("Copy", 1);
			final String uuid = EcoreUtil.generateUUID();
			final StringBuilder sb = new StringBuilder();
			{
				Container c = parent;
				while (c != null && !(c instanceof ScenarioService)) {
					sb.insert(0, File.separator + c.getName());
					c = c.getParent();
				}

			}
			//
			lock.readLock().lock();
			try {
				final File target = new File(dataPath.toString() + sb.toString() + File.separator + name + LINGO_FILE_EXTENSION);
				URI archiveURI = URI.createFileURI(target.getAbsolutePath());

				if (target.exists()) {
					final boolean[] response = new boolean[1];
					final Display display = PlatformUI.getWorkbench().getDisplay();
					display.syncExec(() -> response[0] = MessageDialog.openQuestion(display.getActiveShell(), "Target exists - overwrite?",
							String.format("File \"%s\" already exists. Do you want to overwrite?", target.getAbsoluteFile())));
					if (!response[0]) {
						return null;
					}
				}
				{
					final EObject rootObject = EcoreUtil.copy(scenarioDataProvider.getScenario());
					ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, scenarioCipherProvider -> {
						final Map<String, EObject> extraDataObjects = ScenarioStorageUtil.createCopyOfExtraData(scenarioDataProvider);
						ScenarioStorageUtil.storeToURI(uuid, rootObject, extraDataObjects, scenarioDataProvider.getManifest(), archiveURI, scenarioCipherProvider);
					});
				}

				log.debug("Inserting scenario into {}", parent);

				// Create new model nodes
				final ScenarioInstance newInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
				final Metadata metadata = ScenarioServiceFactory.eINSTANCE.createMetadata();

				// Create a new UUID
				newInstance.setUuid(uuid);
				newInstance.setMetadata(metadata);

				final URI scenarioURI = URI.createFileURI(target.getAbsolutePath());

				final URI destURI = URI.createURI("archive:" + scenarioURI.toString() + "!/rootObject.xmi");
				assert destURI != null;
			} finally {
				lock.readLock().unlock();
				pokeWatchThread();
			}
			progressMonitor.worked(1);

			return null;

		} finally {
			progressMonitor.done();
		}
	}

	@Override
	public void delete(final Container container) {

		lock.readLock().lock();
		try {
			final Path destPath = modelToFilesystemMap.get(container);
			if (destPath == null) {
				log.error("Destination is not known to scenario service.", new RuntimeException());
				return;
			}

			final Path path = modelToFilesystemMap.get(container);
			if (path != null) {
				delete(path.toFile());
			}
		} finally {
			lock.readLock().unlock();
			pokeWatchThread();
		}
	}

	private void delete(final File f) {

		if (f.isDirectory()) {
			for (final File sub : f.listFiles()) {
				delete(sub);
			}
		}
		if (f.isFile()) {
			try {
				FileDeleter.delete(f, LicenseFeatures.isPermitted(FileDeleter.LICENSE_FEATURE__SECURE_DELETE));
			} catch (final Exception e) {
				log.error("Error deleting: " + f.getName(), e);
			}
		} else {
			try {
				Files.delete(f.toPath());
			} catch (final Exception e) {
				log.error("Error deleting: " + f.getName(), e);
			}
		}
	}

	@Override
	protected ScenarioService initServiceModel() {

		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("Shared folder scenario service");
		serviceModel.setLocal(false);
		serviceModel.setServiceID(getSerivceID());
		serviceModel.setOffline(true);
		serviceModel.eAdapters().add(serviceModelAdapter);

		return serviceModel;
	}

	@Override
	public URI resolveURI(final String uri) {
		return URI.createURI(uri);
	}

	private Manifest loadManifest(final File scenario) {
		final URI fileURI = URI.createFileURI(scenario.toString());

		final URI manifestURI = ScenarioStorageUtil.createArtifactURI(fileURI, ScenarioStorageUtil.PATH_MANIFEST_OBJECT);

		assert manifestURI != null;
		final ResourceSet resourceSet = ResourceHelper.createResourceSet(getScenarioCipherProvider());
		assert resourceSet != null;

		try {
			final Resource resource = ResourceHelper.loadResource(resourceSet, manifestURI);
			if (resource.getContents().size() == 1) {
				final EObject top = resource.getContents().get(0);
				if (top instanceof Manifest) {
					return (Manifest) top;
				}
			}
		} catch (final Exception e) {
			// Unable to parse file for some reason
			log.debug("Unable to find manifest for " + scenario, e);
		}
		return null;
	}

	public void start(final Dictionary<?, ?> d) throws IOException {

		final String path = d.get("path").toString();
		serviceName = d.get("serviceName").toString();

		// Convert to absolute path
		dataPath = new File(path).getAbsoluteFile();
		watchThreadRunning = true;

		if (dataPath.exists()) {
			lock.writeLock().lock();

			try {
				// Perform initial scan in workspace job so we can at least see a progress bar somewhere
				final WorkspaceJob job = new WorkspaceJob("Initial scan") {

					@Override
					public IStatus runInWorkspace(final IProgressMonitor monitor) throws CoreException {
						// Perform initial scan
						try {
							scanDirectory(dataPath.toPath(), monitor);
						} catch (final Exception e) {
							log.error(e.getMessage(), e);
						}
						watchThread.start();

						// Initial model load
						new Thread(() -> {
							getServiceModel();
							getServiceModel().setOffline(false);
							setReady();
						}).start();
						return Status.OK_STATUS;
					}
				};
				// Mark as system so it stays in the background
				job.setSystem(true);
				// Show progress in UI (maybe)
				job.setProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY, Boolean.TRUE);
				job.schedule();
			} finally {
				lock.writeLock().unlock();
				pokeWatchThread();
			}
		}

		watchThread = new Thread("DirScan Watcher") {

			@Override
			public void run() {

				while (watchThreadRunning) {
					lock.writeLock().lock();
					try {
						scanDirectory(dataPath.toPath(), new NullProgressMonitor());
					} catch (final Exception e) {
						log.error(e.getMessage(), e);
					} finally {
						lock.writeLock().unlock();
					}
					try {
						Thread.sleep(20000);
					} catch (final InterruptedException e) {
						// ignore
					}
				}
			}
		};

	}

	private void pokeWatchThread() {
		if (watchThread != null) {
			watchThread.interrupt();
		}
	}

	public void stop() {

		// Terminate watch thread
		watchThreadRunning = false;
		// Remove Model elements
		getServiceModel().setOffline(true);
		getServiceModel().getElements().clear();

		// Empty maps
		folderMap.clear();
		scenarioMap.clear();

	}

	private void scanDirectory(final Path dataPath, final IProgressMonitor monitor) throws IOException {

		monitor.beginTask("Scanning dir " + dataPath, IProgressMonitor.UNKNOWN);
		try {
			final Set<String> newFolders = new HashSet<>();
			final Set<String> newFiles = new HashSet<>();

			if (Files.exists(dataPath)) {
				// register directory and sub-directories
				Files.walkFileTree(dataPath, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
						if (!watchThreadRunning) {
							return FileVisitResult.TERMINATE;
						}
						log.debug("preVisitDirectory: {}", dir.normalize());
						monitor.subTask("Scanning " + dir.normalize());
						addFolder(dir);
						newFolders.add(dir.normalize().toString());
						monitor.worked(1);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
						if (!watchThreadRunning) {
							return FileVisitResult.TERMINATE;
						}
						monitor.subTask("Scanning " + file.normalize());
						log.debug("visitFile: {}", file.normalize());
						try {
							addFile(file);
							newFiles.add(file.normalize().toString());
						} catch (final Exception e) {
							log.debug(e.getMessage(), e);
						}
						monitor.worked(1);
						return super.visitFile(file, attrs);
					}
				});
			}
			{
				final Set<String> currentFiles = new HashSet<>(scenarioMap.keySet());
				currentFiles.removeAll(newFiles);
				for (final String file : currentFiles) {
					final WeakReference<ScenarioInstance> ref = scenarioMap.remove(file);
					if (ref != null) {
						final ScenarioInstance scenarioInstance = ref.get();
						if (scenarioInstance != null) {
							removeFile(scenarioInstance);
						}
					}
				}
			}
			{
				final Set<String> currentFolders = new HashSet<>(folderMap.keySet());
				currentFolders.removeAll(newFolders);
				for (final String file : currentFolders) {
					final WeakReference<Container> ref = folderMap.remove(file);
					if (ref != null) {
						final Container container = ref.get();
						if (container != null) {
							removeFolder(container);
						}
					}
				}
			}
		} finally {
			monitor.done();
		}
	}

	protected void removeFolder(final Path dir) {
		log.debug("removeFolder: {}", dir.normalize());

		final WeakReference<Container> remove = folderMap.remove(dir.normalize().toString());
		if (remove == null) {
			return;
		}
		final Container c = remove.get();
		removeFolder(c);
	}

	protected void removeFolder(final Container c) {
		if (c != null) {
			detachSubTree(c);
			final EObject container = c.eContainer();
			if (container instanceof Container) {
				((Container) container).getElements().remove(c);
			}
		}
	}

	protected void addFolder(final Path dir) {
		log.debug("addFolder: {}", dir.normalize());

		final String pathKey = dir.normalize().toString();
		if (!folderMap.containsKey(pathKey)) {
			// top entry will have no parent....
			if (!dir.equals(dataPath.toPath())) {

				final String string = dir.getParent().normalize().toString();
				// Add folder to parent
				final Container parentFolder = folderMap.get(string).get();
				final EList<Container> elements = parentFolder.getElements();
				final String folderName = dir.toFile().getName();

				// This handles the case where a folder has been added to the model before it appeared on filesystem
				Container folder = null;
				for (final Container c : elements) {
					if (c.getName().equals(folderName)) {
						folder = c;
						break;
					}
				}
				if (folder == null) {
					folder = ScenarioServiceFactory.eINSTANCE.createFolder();
					elements.add(folder);
					folder.setName(folderName);
				}
				// Store in map
				folderMap.put(pathKey, new WeakReference<Container>(folder));
				modelToFilesystemMap.put(folder, dir);
			} else {
				folderMap.put(dataPath.toPath().normalize().toString(), new WeakReference<Container>(getServiceModel()));
			}
		}
	}

	protected void addFile(final Path file) {
		log.debug("addFile: {}", file.normalize());

		// Filter based on file extension
		if (!(file.toString().endsWith(LINGO_FILE_EXTENSION))) {
			return;
		}

		final String pathKey = file.normalize().toString();
		if (!scenarioMap.containsKey(pathKey)) {

			final File f = file.toFile();
			URI archiveURI = URI.createFileURI(f.getAbsolutePath());
			final Manifest manifest = loadManifest(f);
			if (manifest != null) {
				final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
				scenarioInstance.setReadonly(true);
				scenarioInstance.setUuid(manifest.getUUID());

				scenarioInstance.setRootObjectURI(archiveURI.toString());

				final String scenarioname = f.getName().replaceFirst("\\.lingo$", "").replace("\\.scenario$", "");
				scenarioInstance.setName(scenarioname);
				scenarioInstance.setVersionContext(manifest.getVersionContext());
				scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

				scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
				scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

				final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
				scenarioInstance.setMetadata(meta);
				meta.setContentType(manifest.getScenarioType());
				meta.setCreated(new Date(f.lastModified()));

				final String string = file.getParent().normalize().toString();
				final WeakReference<Container> weakReference = folderMap.get(string);
				if (weakReference != null) {
					weakReference.get().getElements().add(scenarioInstance);

					scenarioMap.put(pathKey, new WeakReference<ScenarioInstance>(scenarioInstance));
					modelToFilesystemMap.put(scenarioInstance, file);
				}
				ScenarioModelRecord modelRecord = ScenarioStorageUtil.loadInstanceFromURI(archiveURI, true, false, false, getScenarioCipherProvider());
				if (modelRecord != null) {
					modelRecord.setName(scenarioInstance.getName());
					modelRecord.setScenarioInstance(scenarioInstance);
					SSDataManager.Instance.register(scenarioInstance, modelRecord);
					scenarioInstance.setRootObjectURI(archiveURI.toString());
				}
			}
		}
	}

	protected void removeFile(final Path file) {

		if (file == null) {
			return;
		}
		log.debug("removeFile: {}", file.normalize());
		final Path normal = file.normalize();
		if (normal == null) {
			return;
		}
		final WeakReference<ScenarioInstance> scenarioRef = scenarioMap.remove(normal.toString());
		if (scenarioRef == null) {
			return;
		}
		final ScenarioInstance c = scenarioRef.get();
		removeFile(c);
	}

	protected void removeFile(final ScenarioInstance c) {

		if (c != null) {
			detachSubTree(c);
			final EObject container = c.eContainer();
			if (container instanceof Container) {
				((Container) container).getElements().remove(c);
			}

			SSDataManager.Instance.releaseModelRecord(c);
		}

	}

	/**
	 * File system changes mean this is no longer in the tree. Fire various notifications
	 * 
	 * @param c
	 */
	private void detachSubTree(final Container c) {
		log.debug("detachSubTree: {}", c.getName());

		for (final Container cc : c.getElements()) {
			detachSubTree(cc);
		}
		if (c instanceof ScenarioInstance) {

			final ScenarioInstance scenarioInstance = (ScenarioInstance) c;

			final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
			Display.getDefault().asyncExec(() -> {
				final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				final IEditorReference[] editorReferences = activePage.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
				activePage.closeEditors(editorReferences, false);
			});
		}
	}

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {
		log.debug("moveInto -> {}", destination.getName());

		lock.readLock().lock();
		try {
			final Path destPath = modelToFilesystemMap.get(destination);
			if (destPath == null) {
				log.error("Destination is not known to scenario service.", new RuntimeException());
				return;
			}
			if (destination instanceof ScenarioInstance) {
				log.error("Destination is a scenario - cannot move into.", new RuntimeException());
				return;
			}

			// Loop over targets and move (rename)
			for (final Container c : elements) {

				if (c.getParent() == destination) {
					continue;
				}

				final Path path = modelToFilesystemMap.get(c);
				if (path != null) {
					try {
						// Generate the new target filename
						final Path destLoc = destPath.resolve(path.getFileName());

						moveFileTree(path, destLoc);
						// Remove from tree as file system watcher will re-add.
						// We do this after the move as errors will leave the file in place (TODO: What about folder moves?)
						if (c instanceof ScenarioInstance) {
							removeFile(path);
						} else {
							removeFolder(path);
						}
					} catch (final IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		} finally {
			lock.readLock().unlock();
			pokeWatchThread();
		}
	}

	private void moveFileTree(final Path source, final Path dest) throws IOException {
		log.debug("moveFileTree: {} -> {}", source.normalize(), dest.normalize());

		// Maps the old file tree to the new file tree
		final Map<String, Path> oldToNewMap = new HashMap<>();
		oldToNewMap.put(source.getParent().toString(), dest.getParent());

		// Create a file walker op to move each file individually from one folder to another
		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {

				// Make Folder
				final Path target = oldToNewMap.get(dir.getParent().toString());
				if (target == null) {
					// Error!
					return FileVisitResult.SKIP_SUBTREE;
				}

				final Path resolve = target.resolve(dir.getFileName());
				oldToNewMap.put(dir.toString(), resolve);

				resolve.toFile().mkdirs();

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

				removeFile(file);
				final Path target = oldToNewMap.get(file.getParent().toString());
				if (target == null) {
					// Error!
					return FileVisitResult.CONTINUE;
				}

				final Path resolve = target.resolve(file.getFileName());

				// File.move often appears to tell java the file has moved, but causes delete to fail due to the file still being present in the folder....
				// Files.move(file, resolve, StandardCopyOption.ATOMIC_MOVE);

				// ... so instead we copy then delete
				Files.copy(file, resolve);
				Files.delete(file);

				return super.visitFile(file, attrs);
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {

				removeFolder(dir);

				Files.delete(dir);

				return super.postVisitDirectory(dir, exc);
			}
		});
	}

	@Override
	public void makeFolder(final Container parent, final String name) {
		if (parent instanceof ScenarioInstance) {
			return;
		}

		log.debug("makeFolder: {} :: {}", parent.getName(), name);

		lock.readLock().lock();
		try {
			final Path path;
			if (parent == getServiceModel()) {
				path = dataPath.toPath();
			} else {
				path = modelToFilesystemMap.get(parent);
			}

			if (path == null) {
				return;
			}
			final Path newFolderPath = path.resolve(name);
			newFolderPath.toFile().mkdirs();
		} finally {
			lock.readLock().unlock();
			pokeWatchThread();
		}
	}

	@Override
	public String getSerivceID() {
		return "dir-scan-" + serviceName;
	}
}
