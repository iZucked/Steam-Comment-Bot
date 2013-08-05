/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.dirscan;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.util.AbstractScenarioService;

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

	/**
	 * The watch service instance
	 */
	private WatchService watcher;
	private final Map<WatchKey, Path> keys;

	private final Map<String, WeakReference<Container>> folderMap = new HashMap<String, WeakReference<Container>>();
	private final Map<String, WeakReference<ScenarioInstance>> scenarioMap = new HashMap<String, WeakReference<ScenarioInstance>>();
	private final Map<Container, Path> modelToFilesystemMap = new WeakHashMap<Container, Path>();

	private Thread watchThread;
	private volatile boolean watchThreadRunning;

	private final EContentAdapter serviceModelAdapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {

			super.notifyChanged(notification);

			// Process changes and replicate back on FileSystem
			if (notification.getFeature() == ScenarioServicePackage.eINSTANCE.getContainer_Name()) {

				final Container c = (Container) notification.getNotifier();

				final Path path = modelToFilesystemMap.get(c);
				if (path != null) {
					final Path newName = path.getParent().resolve(notification.getNewStringValue() + ".lingo");
					// Remove from tree as file system watcher will re-add
					if (c instanceof ScenarioInstance) {
						removeFile(path);
					} else {
						removeFolder(path);
					}

					path.toFile().renameTo(newName.toFile());
				}
			}
		}
	};

	public DirScanScenarioService(final String name) throws IOException {
		super(name);
		this.keys = new HashMap<WatchKey, Path>();

	}

	@Override
	public ScenarioInstance insert(final Container container, final EObject rootObject) throws IOException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void delete(final Container container) {

	}

	@Override
	protected ScenarioService initServiceModel() {

		final ScenarioService serviceModel = ScenarioServiceFactory.eINSTANCE.createScenarioService();
		serviceModel.setName(serviceName);
		serviceModel.setDescription("DirScan scenario service");
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
		final ResourceSetImpl resourceSet = new ResourceSetImpl();

		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

		final Resource resource = resourceSet.createResource(manifestURI);
		try {
			resource.load(null);
			if (resource.getContents().size() == 1) {
				final EObject top = resource.getContents().get(0);
				if (top instanceof Manifest) {
					return (Manifest) top;
				}
			}
		} catch (final Exception e) {
			// Unable to parse file for some reason
			log.error("Unable to find manifest for " + scenario, e);
		}
		return null;
	}

	public void start(final Dictionary<?, ?> d) throws IOException {

		// TODO Auto-generated method stub
		// final String scenarioServiceID = d.get("component.id").toString();
		final String path = d.get("path").toString();
		serviceName = d.get("serviceName").toString();

		dataPath = new File(path);

		this.watcher = FileSystems.getDefault().newWatchService();
		try {
			folderMap.put(dataPath.toPath().normalize().toString(), new WeakReference<Container>(getServiceModel()));
			recursiveAdd(getServiceModel(), dataPath.toPath());
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}

		watchThreadRunning = true;
		watchThread = new Thread("DirScan Watcher") {
			@Override
			public void run() {

				while (watchThreadRunning) {

					// wait for key to be signalled
					WatchKey key;
					try {
						// Yield every 2 seconds to check running state
						key = watcher.poll(2, TimeUnit.SECONDS);
					} catch (final ClosedWatchServiceException e) {
						break;
					} catch (final InterruptedException x) {
						return;
					}

					if (key == null) {
						continue;
					}

					final Path dir = keys.get(key);
					if (dir == null) {
						System.err.println("WatchKey not recognized!!");
						continue;
					}

					for (final WatchEvent<?> event : key.pollEvents()) {
						final Kind<?> kind = event.kind();

						// TBD - provide example of how OVERFLOW event is handled
						if (kind == OVERFLOW) {
							continue;
						}

						// Context for directory entry event is the file name of entry
						@SuppressWarnings("unchecked")
						final WatchEvent<Path> ev = (WatchEvent<Path>) (event);
						// Work out filename - if renamed or deleted, the old name will no longer exist on filesystem.
						final Path name = ev.context();
						final Path child = dir.resolve(name);

						// if directory is created, and watching recursively, then
						// register it and its sub-directories
						if (kind == ENTRY_CREATE) {
							try {
								recursiveAdd(folderMap.get(child.getParent().normalize().toString()).get(), child);
							} catch (final IOException e) {
								log.error(e.getMessage(), e);
							}
						}

						else if (kind == ENTRY_DELETE) {

							final String pathKey = child.normalize().toString();
							if (folderMap.containsKey(pathKey)) {
								removeFolder(child);
							} else if (scenarioMap.containsKey(pathKey)) {
								removeFile(child);
							}
						} else if (kind == ENTRY_MODIFY) {
							//
							if (Files.isRegularFile(child)) {
								addFile(child);
							}
						}
					}

					// reset key and remove from set if directory no longer accessible
					final boolean valid = key.reset();
					if (!valid) {
						keys.remove(key);

						// all directories are inaccessible
						if (keys.isEmpty()) {
							break;
						}
					}
				}
			};
		};
		watchThread.start();
	}

	public void stop() {

		// Terminate watch thread
		watchThreadRunning = false;
		// Close watch service - this will cause the watch#poll/take methods to throw exceptions
		try {
			watcher.close();
		} catch (final IOException e) {
			log.error(e.getMessage(), e);
		}
		// Cancel all keys
		for (final WatchKey key : keys.keySet()) {
			key.cancel();
		}
		// Remove Model elements
		getServiceModel().getElements().clear();

		// Empty maps
		keys.clear();
		folderMap.clear();
		scenarioMap.clear();

	}

	/**
	 * Register the given directory, and all its sub-directories, with the WatchService.
	 */

	private void recursiveAdd(final Container root, final Path dataPath) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(dataPath, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {

				addFolder(dir);

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

				addFile(file);

				return super.visitFile(file, attrs);
			}
		});
	}

	protected void removeFolder(final Path dir) {

		final Container c = folderMap.remove(dir.normalize().toString()).get();
		if (c != null) {
			detachSubTree(c);
			final EObject container = c.eContainer();
			if (container instanceof Container) {
				((Container) container).getElements().remove(c);
			}
		}
		for (final Map.Entry<WatchKey, Path> e : keys.entrySet()) {
			if (e.getValue().equals(dir)) {
				final WatchKey key = e.getKey();
				key.cancel();
				keys.remove(key);
				break;
			}
		}
	}

	protected void addFolder(final Path dir) throws IOException {
		final String pathKey = dir.normalize().toString();
		if (!folderMap.containsKey(pathKey)) {
			// top entry will have no parent....
			if (!dir.equals(dataPath.toPath())) {
				final Folder folder = ScenarioServiceFactory.eINSTANCE.createFolder();

				final String string = dir.getParent().normalize().toString();
				// Add folder to parent
				folderMap.get(string).get().getElements().add(folder);

				folder.setName(dir.toFile().getName());

				// Store in map
				folderMap.put(dir.normalize().toString(), new WeakReference<Container>(folder));
				modelToFilesystemMap.put(folder, dir);
			}
			final WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			keys.put(key, dir);
		}
	}

	protected void addFile(final Path file) {
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
				String scenarioname = f.getName().replaceFirst("\\.lingo$", "").replace("\\.scenario$", "");
				scenarioInstance.setName(scenarioname);
				scenarioInstance.setVersionContext(manifest.getVersionContext());
				scenarioInstance.setScenarioVersion(manifest.getScenarioVersion());

				final Metadata meta = ScenarioServiceFactory.eINSTANCE.createMetadata();
				scenarioInstance.setMetadata(meta);
				meta.setContentType(manifest.getScenarioType());

				final String string = file.getParent().normalize().toString();
				final WeakReference<Container> weakReference = folderMap.get(string);
				weakReference.get().getElements().add(scenarioInstance);

				scenarioMap.put(pathKey, new WeakReference<ScenarioInstance>(scenarioInstance));
				modelToFilesystemMap.put(scenarioInstance, file);
			}
		}
	}

	protected void removeFile(final Path file) {
		final ScenarioInstance c = scenarioMap.remove(file.normalize().toString()).get();

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

		for (final Container cc : c.getElements()) {
			detachSubTree(cc);
		}
		if (c instanceof ScenarioInstance) {

			final ScenarioInstance scenarioInstance = (ScenarioInstance) c;

			// TODO: Deselect from view
			// Activator.getDefault().getScenarioServiceSelectionProvider().deselect(scenarioInstance);

			final ScenarioServiceEditorInput editorInput = new ScenarioServiceEditorInput(scenarioInstance);
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					final IEditorReference[] editorReferences = activePage.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
					// TODO: Prompt to save?
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

		final ResourceSet instanceResourceSet = createResourceSet();
		final URI scenarioURI = URI.createFileURI(dataPath.toString() + sb.toString() + File.separator + original.getName() + ".lingo");

		final URI destURI = URI.createURI("archive:" + scenarioURI.toString() + "!/rootObject.xmi");
		if (original.getInstance() == null) {
			// Not loaded, copy raw data
			final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
			final URI sourceURI = originalService.resolveURI(original.getRootObjectURI());
			copyURIData(uc, sourceURI, destURI);
		} else {
			// Already loaded? Just use the same instance.
			final EObject rootObject = EcoreUtil.copy(original.getInstance());
			final Resource instanceResource = instanceResourceSet.createResource(destURI);
			instanceResource.getContents().add(rootObject);
			instanceResource.save(null);
		}

		final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
		manifest.setScenarioType(original.getMetadata().getContentType());
		manifest.setUUID(original.getUuid());
		manifest.setScenarioVersion(original.getScenarioVersion());
		manifest.setVersionContext(original.getVersionContext());
		final URI manifestURI = URI.createURI("archive:" + scenarioURI.toString() + "!/MANIFEST.xmi");
		final Resource manifestResource = instanceResourceSet.createResource(manifestURI);

		manifestResource.getContents().add(manifest);

		manifest.getModelURIs().add("rootObject.xmi");
		manifestResource.save(null);

		return null;
	}

	@Override
	public void moveInto(final List<Container> elements, final Container destination) {

		// TODO Auto-generated method stub

	}

	@Override
	public EObject load(final ScenarioInstance instance) throws IOException {

		if (instance.getInstance() == null) {

			final EObject object = super.load(instance);

			if (object != null) {
				final ScenarioLock lock = instance.getLock(ScenarioLock.EDITORS);
				lock.claim();
			}

			return object;
		} else {
			return instance.getInstance();
		}
	}
}
