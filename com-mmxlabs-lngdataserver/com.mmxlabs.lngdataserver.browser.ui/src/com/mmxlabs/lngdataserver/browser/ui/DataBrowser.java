package com.mmxlabs.lngdataserver.browser.ui;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.BrowserPackage;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Leaf;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.browser.provider.BrowserItemProviderAdapterFactory;
import com.mmxlabs.lngdataserver.browser.ui.context.DataBrowserContextMenuExtensionUtil;
import com.mmxlabs.lngdataserver.browser.ui.context.IDataBrowserContextMenuExtension;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceAdapterFactory;

public class DataBrowser extends ViewPart {

	public static final String ID = "com.mmxlabs.lngdataserver.browser.ui.DataBrowser";

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBrowser.class);

	private GridTreeViewer dataViewer;
	private GridTreeViewer scenarioViewer;
	private CompositeNode root;
	private final Set<Node> selectedNodes = new HashSet<>();
	private Predicate<ScenarioInstance> selectedScenarioChecker = null;

	ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> scenarioTracker;
	private Iterable<IDataBrowserContextMenuExtension> contextMenuExtensions;

	private final ISelectionChangedListener scenarioSelectionListener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			final ISelection selection = scenarioViewer.getSelection();

			selectedNodes.clear();
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) selection;
				final Iterator<?> itr = ss.iterator();
				if (ss.size() == 1) {
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof ScenarioInstance) {
							final ScenarioInstance scenarioInstance = (ScenarioInstance) o;
							final Manifest mf = scenarioInstance.getManifest();
							if (mf != null) {
								for (final ModelArtifact modelArtifact : mf.getModelDependencies()) {
									final String v = modelArtifact.getDataVersion();
									for (final Node n : root.getChildren()) {
										if (n instanceof CompositeNode) {
											final CompositeNode compositeNode = (CompositeNode) n;
											if (Objects.equals(modelArtifact.getKey(), compositeNode.getType())) {
												for (final Node n2 : compositeNode.getChildren()) {
													if (Objects.equals(n2.getDisplayName(), v)) {
														selectedNodes.add(n2);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			dataViewer.refresh(true);
		}
	};
	private final ISelectionChangedListener nodeSelectionListener = new ISelectionChangedListener() {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			final ISelection selection = dataViewer.getSelection();

			selectedScenarioChecker = null;
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) selection;
				final Iterator<?> itr = ss.iterator();
				if (ss.size() == 1) {
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof Leaf) {
							Leaf leaf = (Leaf) o;
							CompositeNode compositeNode = leaf.getParent();
							if (compositeNode != null) {

								selectedScenarioChecker = (scenarioInstance) -> {
									final Manifest mf = scenarioInstance.getManifest();
									if (mf != null) {
										for (final ModelArtifact modelArtifact : mf.getModelDependencies()) {
											final String v = modelArtifact.getDataVersion();
											if (Objects.equals(modelArtifact.getKey(), compositeNode.getType())) {
												if (Objects.equals(leaf.getDisplayName(), v)) {
													return true;
												}
											}
										}
									}
									return false;
								};
							}
						}
					}
				}
			}
			scenarioViewer.refresh(true);
		}
	};

	@Override
	public void createPartControl(final Composite parent) {
		Bundle bundle = FrameworkUtil.getBundle(DataBrowser.class);
		ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> scenarioTracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(bundle.getBundleContext(),
				ScenarioServiceRegistry.class, null);
		scenarioTracker.open();

		SashForm sash = new SashForm(parent, SWT.SMOOTH | SWT.VERTICAL);
		sash.setSashWidth(3);

		// Change the color used to paint the sashes
		sash.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
		Composite dv = new Composite(sash, SWT.NONE);
		dv.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		dv.setLayout(GridLayoutFactory.fillDefaults().create());
		dv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		dataViewer = new GridTreeViewer(dv, SWT.NONE | SWT.SINGLE | SWT.V_SCROLL);
		dataViewer.setContentProvider(new DataBrowserContentProvider(createNewAdapterFactory()));
		dataViewer.setAutoExpandLevel(GridTreeViewer.ALL_LEVELS);
		dataViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		GridViewerColumn data_col1 = new GridViewerColumn(dataViewer, SWT.NONE);
		data_col1.getColumn().setTree(true);
		data_col1.getColumn().setWidth(300);

		data_col1.setLabelProvider(new DataBrowserLabelProvider(createNewAdapterFactory(), selectedNodes));
		GridViewerHelper.configureLookAndFeel(data_col1);

		GridViewerHelper.configureLookAndFeel(dataViewer);

		root = BrowserFactory.eINSTANCE.createCompositeNode();
		root.setDisplayName("Versions");
		dataViewer.setInput(root);

		getSite().setSelectionProvider(dataViewer);
		contextMenuExtensions = DataBrowserContextMenuExtensionUtil.getContextMenuExtensions();
		final MenuManager data_mgr = new MenuManager();
		dataViewer.getControl().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				final ISelection selection = dataViewer.getSelection();

				if (selection.isEmpty()) {
					return;
				}

				if (!(selection instanceof TreeSelection)) {
					return;
				}

				final TreeSelection treeSelection = (TreeSelection) selection;

				if (menu == null) {
					menu = data_mgr.createContextMenu(dataViewer.getControl());
				}
				final IContributionItem[] l = data_mgr.getItems();
				data_mgr.removeAll();
				for (final IContributionItem itm : l) {
					itm.dispose();
				}

				final MenuItem[] items = menu.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}
				boolean itemsAdded = false;
				if (treeSelection.getFirstElement() instanceof Node) {
					final Node selectedNode = (Node) treeSelection.getFirstElement();
					if (!(selectedNode instanceof CompositeNode)) {
						CompositeNode parentNode = (CompositeNode) selectedNode.getParent();
						final IDataBrowserActionsHandler actionHandler = parentNode.getActionHandler();
						if (actionHandler != null) {
							if (actionHandler.supportsRename()) {
								data_mgr.add(new RunnableAction("Rename", () -> {
									// FIXME: Implement! Dialog for user entry then call handler
								}));
								itemsAdded = true;
							} else {
								data_mgr.add(new RunnableAction("Rename (Not suppported)", () -> {

								}));
								itemsAdded = true;
							}
							if (!selectedNode.isPublished() && actionHandler.supportsPublish()) {
								data_mgr.add(new RunnableAction("Publish", () -> {

									if (actionHandler.publish(selectedNode.getDisplayName())) {
										selectedNode.setPublished(true);
									}
								}));
								itemsAdded = true;
							}
							if (actionHandler.supportsDelete()) {
								data_mgr.add(new RunnableAction("Delete", () -> {
									if (actionHandler.delete(selectedNode.getDisplayName())) {
										parentNode.getChildren().remove(selectedNode);
									}
								}));
								itemsAdded = true;
							} else {
								data_mgr.add(new RunnableAction("Delete (Not suppported)", () -> {

								}));
								itemsAdded = true;
							}
							{
								data_mgr.add(new RunnableAction("Set as latest (Not suppported)", () -> {

								}));
								itemsAdded = true;
							}
						}
					}
					if (selectedNode instanceof CompositeNode) {
						final CompositeNode compositeNode = (CompositeNode) selectedNode;
						final IDataBrowserActionsHandler actionHandler = compositeNode.getActionHandler();
						if (actionHandler != null) {
							if (actionHandler.supportsSyncUpstream()) {
								data_mgr.add(new RunnableAction("Check upstream", () -> actionHandler.syncUpstream()));
								itemsAdded = true;
							}
							if (actionHandler.supportsRefreshLocal()) {
								data_mgr.add(new RunnableAction("Check local", () -> actionHandler.refreshLocal()));
								itemsAdded = true;
							}
						}
					}
				}

				if (contextMenuExtensions != null) {
					for (final IDataBrowserContextMenuExtension ext : contextMenuExtensions) {
						itemsAdded |= ext.contributeToDataMenu(treeSelection, data_mgr);
					}
				}
				if (itemsAdded) {
					menu.setVisible(true);
				}
			}
		});

		Composite sv = new Composite(sash, SWT.NONE);
		sv.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		sv.setLayout(GridLayoutFactory.fillDefaults().create());
		sv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		scenarioViewer = new GridTreeViewer(sv, SWT.SINGLE | SWT.V_SCROLL);
		scenarioViewer.getGrid().setBackgroundMode(SWT.INHERIT_NONE);
		scenarioViewer.getGrid().setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridViewerHelper.configureLookAndFeel(scenarioViewer);
		scenarioViewer.getGrid().setLinesVisible(true);
		scenarioViewer.getGrid().setHeaderVisible(true);
		scenarioViewer.setContentProvider(new ScenarioContentProvider());
		scenarioViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		GridViewerColumn c1 = new GridViewerColumn(scenarioViewer, SWT.NONE);
		c1.setLabelProvider(new ScenarioLabelProvider(0, null));
		c1.getColumn().setTree(true);
		c1.getColumn().setWidth(200);
		GridViewerHelper.configureLookAndFeel(c1);

		final Injector injector = Guice.createInjector(new DataExtensionsModule());
		final Iterable<DataExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<DataExtensionPoint>>() {
		}));
		LOGGER.debug("Found " + Iterables.size(extensions) + " extensions");

		for (final DataExtensionPoint extensionPoint : extensions) {
			final DataExtension dataExtension = extensionPoint.getDataExtension();
			if (dataExtension != null) {
				try {
					CompositeNode dataRoot = dataExtension.getDataRoot();
					root.getChildren().add(dataRoot);

					GridViewerColumn c2 = new GridViewerColumn(scenarioViewer, SWT.NONE);
					c2.setLabelProvider(new ScenarioLabelProvider(1, dataRoot));
					c2.getColumn().setWidth(30);
					GridViewerHelper.configureLookAndFeel(c2);

					// Hacky renaming...
					String lbl = dataRoot.getDisplayName().replaceAll(" (loading...)", "");
					c2.getColumn().setText(lbl);
				} catch (final Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		scenarioViewer.setAutoExpandLevel(GridTreeViewer.ALL_LEVELS);
		scenarioViewer.setInput(scenarioTracker);
		scenarioViewer.expandAll();
		final MenuManager scenario_mgr = new MenuManager();
		scenarioViewer.getControl().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				final ISelection selection = scenarioViewer.getSelection();

				if (selection.isEmpty()) {
					return;
				}

				if (!(selection instanceof TreeSelection)) {
					return;
				}

				final TreeSelection treeSelection = (TreeSelection) selection;

				if (menu == null) {
					menu = scenario_mgr.createContextMenu(scenarioViewer.getControl());
				}
				final IContributionItem[] l = scenario_mgr.getItems();
				scenario_mgr.removeAll();
				for (final IContributionItem itm : l) {
					itm.dispose();
				}

				final MenuItem[] items = menu.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}
				boolean itemsAdded = false;

				if (contextMenuExtensions != null) {
					for (final IDataBrowserContextMenuExtension ext : contextMenuExtensions) {
						itemsAdded |= ext.contributeToScenarioMenu(treeSelection, scenario_mgr);
					}
				}
				if (itemsAdded) {
					menu.setVisible(true);
				}
			}
		});
		sash.setWeights(new int[] { 60, 40 });

		scenarioViewer.addSelectionChangedListener(scenarioSelectionListener);
		dataViewer.addSelectionChangedListener(nodeSelectionListener);

		root.eAdapters().add(new EContentAdapter() {
			public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {
				super.notifyChanged(notification);
				if (notification.isTouch()) {
					return;
				}
				if (notification.getFeature() == BrowserPackage.Literals.COMPOSITE_NODE__LATEST) {
					ViewerHelper.refresh(scenarioViewer, false);
				}
				if (notification.getFeature() == BrowserPackage.Literals.COMPOSITE_NODE__CHILDREN) {
					ViewerHelper.runIfViewerValid(dataViewer, false, (v) -> v.expandAll());
				}
			};
		});
		dataViewer.expandAll();

	}

	@Override
	public void dispose() {
		if (scenarioTracker != null) {
			scenarioTracker.close();
		}

		super.dispose();
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(dataViewer);
	}

	private static ComposedAdapterFactory createNewAdapterFactory() {
		// Hook in the global registry to get other adapter factories
		final ComposedAdapterFactory factory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		factory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		factory.addAdapterFactory(new BrowserItemProviderAdapterFactory());
		factory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		return factory;
	}

	private static ComposedAdapterFactory createNewScenarioModelAdapterFactory() {
		// Hook in the global registry to get other adapter factories
		final ComposedAdapterFactory factory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		factory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		factory.addAdapterFactory(new ScenarioServiceAdapterFactory());
		factory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		return factory;
	}

	private static class DataExtensionsModule extends AbstractModule {
		@Override
		protected void configure() {
			install(osgiModule(FrameworkUtil.getBundle(Activator.class).getBundleContext(), eclipseRegistry()));
			bind(iterable(DataExtensionPoint.class)).toProvider(service(DataExtensionPoint.class).multiple());
		}
	}

	class ScenarioLabelProvider extends ColumnLabelProvider {

		private AdapterFactoryLabelProvider lp;
		private int columnIdx;
		private CompositeNode compositeNode;

		public ScenarioLabelProvider(int columnIdx, CompositeNode compositeNode) {
			this.columnIdx = columnIdx;
			this.compositeNode = compositeNode;
			lp = new AdapterFactoryLabelProvider(createNewScenarioModelAdapterFactory());
		}

		@Override
		public String getText(Object object) {
			if (columnIdx > 0) {

				if (object instanceof ScenarioInstance) {
					ScenarioInstance scenarioInstance = (ScenarioInstance) object;
					Manifest mf = scenarioInstance.getManifest();
					if (mf != null) {

						Node latest = compositeNode.getLatest();
						if (latest == null) {
							return "";
						}
						String versionId = latest.getDisplayName();
						if (versionId == null || versionId.toLowerCase().contains("loading")) {
							return "";
						}
						for (ModelArtifact modelArtifact : mf.getModelDependencies()) {
							if (Objects.equals(modelArtifact.getKey(), compositeNode.getType())) {
								if (versionId.equals(modelArtifact.getDataVersion())) {
									return "";
								} else {
									return "X";
								}
							}
						}
					}
					return "X";

				}

				return "";
			}
			return lp.getText(object);
		}

		@Override
		public Color getBackground(Object element) {

			if (element instanceof ScenarioInstance) {
				ScenarioInstance scenarioInstance = (ScenarioInstance) element;
				Predicate<ScenarioInstance> checker = selectedScenarioChecker;
				if (checker != null) {
					if (checker.test(scenarioInstance)) {
						return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GREEN);
					}
				}
			}

			return null;
		}

		@Override
		public Image getImage(Object element) {
			if (columnIdx == 0) {
				return lp.getImage(element);
			}
			return null;
		}
	}

	class ScenarioContentProvider implements ITreeContentProvider {

		public ScenarioContentProvider() {
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public Object[] getElements(Object inputElement) {

			if (inputElement instanceof ServiceTracker<?, ?>) {
				Object service = ((ServiceTracker) inputElement).getService();
				if (service instanceof ScenarioServiceRegistry) {
					ScenarioServiceRegistry registry = (ScenarioServiceRegistry) service;
					List<ScenarioService> localServices = new LinkedList<>();
					for (ScenarioService ss : registry.getScenarioModel().getScenarioServices()) {
						if (ss.isLocal()) {
							localServices.add(ss);
						}
					}
					return localServices.toArray();
				}
			}
			return new Object[0];
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			LinkedList<Object> result = new LinkedList<Object>();

			if (parentElement instanceof Container) {
				for (Object element : ((Container) parentElement).getElements()) {
					if (element instanceof ScenarioInstance) {
						result.add(element);
					} else if (element instanceof Container) {
						if (!((Container) element).getElements().isEmpty()) {
							result.add(element);
						}
					}

				}
			}

			return result.toArray();
		}

		@Override
		public boolean hasChildren(Object element) {
			if (!(element instanceof Container)) {
				return false;
			}
			Container container = (Container) element;
			return !(container.getElements().isEmpty());
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}
	}
}
