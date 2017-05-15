/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.io.FileDeleter;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;
import com.mmxlabs.scenario.service.util.ResourceHelper;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

public class DirScanScenarioService extends AbstractScenarioService {

	private static final Logger log = LoggerFactory.getLogger(DirScanScenarioService.class);

	/**
	 * Root of the directory tree
	 */
	private File dataPath;

	/**
	 * Name of this service
	 */
	private String serviceName;

	private final Map<String, WeakReference<Container>> folderMap = new HashMap<String, WeakReference<Container>>();
	private final Map<String, WeakReference<ScenarioInstance>> scenarioMap = new HashMap<String, WeakReference<ScenarioInstance>>();
	private final Map<Container, Path> modelToFilesystemMap = new WeakHashMap<Container, Path>();

	private Thread watchThread;
	private volatile boolean watchThreadRunning;

	/**
	 * A {@link ReadWriteLock} to co-ordinate access between file system watcher and manipulation code. In this case the "write" lock is the watcher and the read locks belong to the manipulation code.
	 * Multiple Filesystem change operations are permitted (this may be a flawed assumption) but the filesystem watch code runs exclusively.
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

	private final EContentAdapter serviceModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {

			super.notifyChanged(notification);

			// Process changes and replicate back on FileSystem
			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getContainer_Name()) {

				final Container c = (Container) notification.getNotifier();

				final Path path = modelToFilesystemMap.get(c);
				if (path != null) {
					// Append .lingo if it is a scenario
					final String ext = c instanceof ScenarioInstance ? ".lingo" : "";
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

	public DirScanScenarioService(final String name) throws IOException {
		super(name);
	}

	@Override
	public ScenarioInstance insert(final Container container, final EObject rootObject) throws IOException {
		throw new UnsupportedOperationException();

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
				FileDeleter.delete(f, LicenseFeatures.isPermitted("features:secure-delete"));
			} catch (final Exception e) {
				log.error("Error deleting: " + f.getName(), e);
			}
		} else {
			f.delete();
		}
	}

	@Override
	protected ScenarioService initServiceModel() {

		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("Shared folder scenario service");
		serviceModel.setLocal(false);
		serviceModel.eAdapters().add(serviceModelAdapter);

		return serviceModel;
	}

	@Override
	public URI resolveURI(final String uri) {
		return URI.createURI(uri);
	}

	private Manifest loadManifest(final File scenario) {
		final URI fileURI = URI.createFileURI(scenario.toString());

		final URI manifestURI = URI.createURI("archive:" + fileURI.toString() + "!/MANIFEST.xmi");
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

		if (dataPath.exists()) {
			lock.writeLock().lock();

			try {
				try {
					scanDirectory(getServiceModel(), dataPath.toPath());
				} catch (final Exception e) {
					log.error(e.getMessage(), e);
				}
			} finally {
				lock.writeLock().unlock();
				pokeWatchThread();
			}
		}

		watchThreadRunning = true;
		watchThread = new Thread("DirScan Watcher") {
			@Override
			public void run() {

				while (watchThreadRunning) {
					lock.writeLock().lock();
					try {
						scanDirectory(getServiceModel(), dataPath.toPath());
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
			};
		};
		watchThread.start();
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
		getServiceModel().getElements().clear();

		// Empty maps
		folderMap.clear();
		scenarioMap.clear();

	}

	private void scanDirectory(final Container root, final Path dataPath) throws IOException {

		final Set<String> newFolders = new HashSet<>();
		final Set<String> newFiles = new HashSet<>();

		if (Files.exists(dataPath)) {
			// register directory and sub-directories
			Files.walkFileTree(dataPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
					log.debug("preVisitDirectory: " + dir.normalize());
					addFolder(dir);
					newFolders.add(dir.normalize().toString());
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
					log.debug("visitFile: " + file.normalize());
					try {
						addFile(file);
						newFiles.add(file.normalize().toString());
					} catch (final Exception e) {
						log.debug(e.getMessage(), e);
					}
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

	}

	protected void removeFolder(final Path dir) {
		log.debug("removeFolder: " + dir.normalize());

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

	protected void addFolder(final Path dir) throws IOException {
		log.debug("addFolder: " + dir.normalize());

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
		log.debug("addFile: " + file.normalize());

		// Filter based on file extension
		if (!(file.toString().endsWith(".lingo") || file.toString().endsWith(".scenario"))) {
			return;
		}

		final String pathKey = file.normalize().toString();
		if (!scenarioMap.containsKey(pathKey)) {

			final File f = file.toFile();
			final Manifest manifest = loadManifest(f);
			if (manifest != null) {
				final ScenarioInstance scenarioInstance = ScenarioServiceFactory.eINSTANCE.createScenarioInstance();
				scenarioInstance.setReadonly(true);
				scenarioInstance.setUuid(manifest.getUUID());

				final URI fileURI = URI.createFileURI(f.getAbsolutePath());
				scenarioInstance.setRootObjectURI("archive:" + fileURI.toString() + "!/rootObject.xmi");
				final String scenarioname = f.getName().replaceFirst("\\.lingo$", "").replace("\\.scenario$", "");
				scenarioInstance.setName(scenarioname);
				scenarioInstance.setVersionContext(manifest.getVersionContext());
				scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

				scenarioInstance.setClientVersionContext(manifest.getClientVersionContext());
				scenarioInstance.setClientScenarioVersion(manifest.getClientScenarioVersion());

				final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
				scenarioInstance.setMetadata(meta);
				meta.setContentType(manifest.getScenarioType());

				final String string = file.getParent().normalize().toString();
				final WeakReference<Container> weakReference = folderMap.get(string);
				if (weakReference != null) {
					weakReference.get().getElements().add(scenarioInstance);

					scenarioMap.put(pathKey, new WeakReference<ScenarioInstance>(scenarioInstance));
					modelToFilesystemMap.put(scenarioInstance, file);
				}
			}
		}
	}

	protected void removeFile(final Path file) {

		if (file == null) {
			return;
		}
		log.debug("removeFile: " + file.normalize());
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
		}

	}

	/**
	 * File system changes mean this is no longer in the tree. Fire various notifications
	 * 
	 * @param c
	 */
	private void detachSubTree(final Container c) {
		log.debug("detachSubTree: " + c.getName());

		for (final Container cc : c.getElements()) {
			detachSubTree(cc);
		}
		if (c instanceof ScenarioInstance) {

			final ScenarioInstance scenarioInstance = (ScenarioInstance) c;

			final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {

					final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					final IEditorReference[] editorReferences = activePage.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
					activePage.closeEditors(editorReferences, false);
				}
			});
		}
	}

	@Override
	public ScenarioInstance duplicate(final ScenarioInstance original, final Container destination) throws IOException {
		log.debug("Duplicating " + original.getUuid() + " into " + destination);
		final IScenarioService originalService = original.getScenarioService();

		final StringBuilder sb = new StringBuilder();
		{
			Container c = destination;
			while (c != null && !(c instanceof ScenarioService)) {
				sb.insert(0, File.separator + c.getName());
				c = c.getParent();
			}

		}

		lock.readLock().lock();
		try {

			final ResourceSet instanceResourceSet = createResourceSet();
			final File target = new File(dataPath.toString() + sb.toString() + File.separator + original.getName() + ".lingo");
			if (target.exists()) {
				final boolean[] response = new boolean[1];
				final Display display = PlatformUI.getWorkbench().getDisplay();
				display.syncExec(new Runnable() {

					@Override
					public void run() {
						response[0] = MessageDialog.openQuestion(display.getActiveShell(), "Target exists - overwrite?",
								String.format("File \"%s\" already exists. Do you want to overwrite?", target.getAbsoluteFile()));

					}
				});
				if (!response[0]) {
					return null;
				}
			}
			final URI scenarioURI = URI.createFileURI(target.getAbsolutePath());

			final URI destURI = URI.createURI("archive:" + scenarioURI.toString() + "!/rootObject.xmi");
			assert destURI != null;
			if (original.getInstance() == null) {
				// Not loaded, copy raw data
				final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
				final String rootObjectURI = original.getRootObjectURI();
				assert rootObjectURI != null;
				final URI sourceURI = originalService.resolveURI(rootObjectURI);
				copyURIData(uc, sourceURI, destURI);
			} else {
				// Already loaded? Just use the same instance.
				final EObject rootObject = EcoreUtil.copy(original.getInstance());
				final Resource instanceResource = instanceResourceSet.createResource(destURI);
				instanceResource.getContents().add(rootObject);
				ResourceHelper.saveResource(instanceResource);
			}

			final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
			manifest.setScenarioType(original.getMetadata().getContentType());
			manifest.setUUID(original.getUuid());
			manifest.setScenarioVersion(original.getScenarioVersion());
			manifest.setVersionContext(original.getVersionContext());

			manifest.setClientScenarioVersion(original.getClientScenarioVersion());
			manifest.setClientVersionContext(original.getClientVersionContext());
			// client version!

			final URI manifestURI = URI.createURI("archive:" + scenarioURI.toString() + "!/MANIFEST.xmi");
			final Resource manifestResource = instanceResourceSet.createResource(manifestURI);

			manifestResource.getContents().add(manifest);

			manifest.getModelURIs().add("rootObject.xmi");
			manifestResource.save(null);
		} finally {
			lock.readLock().unlock();
			pokeWatchThread();
		}
		return null;
	}

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {
		log.debug("moveInto -> " + destination.getName());

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
						// Files.move(path, destLoc);
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
		log.debug("moveFileTree: " + source.normalize() + " -> " + dest.normalize());

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
	public EObject load(final ScenarioInstance instance) throws IOException {

		// Make read-only...
		instance.setReadonly(true);

		if (instance.getInstance() == null) {

			try {
				final EObject eObj = super.load(instance);

				if (eObj != null) {
					// Forces editor lock to disallow users from editing the scenario.
					// TODO: This is not a very clean way to do it!
					final ScenarioLock lock = instance.getLock(ScenarioLock.EDITORS);
					lock.claim();
				}

				// TODO: This should be linked to a listener...
				{
					// Mark resources as read-only
					final EditingDomain domain = (EditingDomain) instance.getAdapters().get(EditingDomain.class);
					if (domain instanceof AdapterFactoryEditingDomain) {
						final AdapterFactoryEditingDomain adapterFactoryEditingDomain = (AdapterFactoryEditingDomain) domain;
						final Map<Resource, Boolean> resourceToReadOnlyMap = adapterFactoryEditingDomain.getResourceToReadOnlyMap();
						for (final Resource r : domain.getResourceSet().getResources()) {
							resourceToReadOnlyMap.put(r, Boolean.TRUE);
						}
					}
				}

				return eObj;
			} catch (final Exception e) {
				throw new DirScanException(getName(), e);
			}
		} else {
			return instance.getInstance();
		}
	}

	@Override
	public void makeFolder(final Container parent, final String name) {
		if (parent instanceof ScenarioInstance) {
			return;
		}

		log.debug("makeFolder: " + parent.getName() + " ::  " + name);

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

	/**
	 * Override the normal method as we register the {@link DirScanScenarioService} is a different way. Thus we can avoid keeping a service tracker active.
	 */
	@Override
	public IScenarioCipherProvider getScenarioCipherProvider() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(DirScanScenarioService.class).getBundleContext();
		final ServiceReference<IScenarioCipherProvider> serviceReference = bundleContext.getServiceReference(IScenarioCipherProvider.class);
		if (serviceReference != null) {
			return bundleContext.getService(serviceReference);
		}
		return null;
	}

}
