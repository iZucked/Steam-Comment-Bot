package com.mmxlabs.lngdataserver.browser.ui;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.browser.provider.BrowserItemProviderAdapterFactory;
import com.mmxlabs.lngdataserver.browser.ui.context.DataBrowserContextMenuExtensionUtil;
import com.mmxlabs.lngdataserver.browser.ui.context.IDataBrowserContextMenuExtension;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class DataBrowser extends ViewPart {

	public static final String ID = "com.mmxlabs.lngdataserver.browser.ui.DataBrowser";

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBrowser.class);

	private TreeViewer viewer;
	private CompositeNode root;
	private final Set<Object> selectedNodes = new HashSet<>();

	private Iterable<IDataBrowserContextMenuExtension> contextMenuExtensions;

	private final ISelectionListener scenarioSelectionListener = new ISelectionListener() {

		@Override
		public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

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
			viewer.refresh(true);
		}
	};

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new TreeViewer(parent, SWT.SINGLE);
		viewer.setContentProvider(new DataBrowserContentProvider(createNewAdapterFactory()));
		viewer.setLabelProvider(new DataBrowserLabelProvider(createNewAdapterFactory(), selectedNodes));

		root = BrowserFactory.eINSTANCE.createCompositeNode();
		root.setDisplayName("Versions");
		viewer.setInput(root);

		getSite().setSelectionProvider(viewer);
		final MenuManager mgr = new MenuManager();
		contextMenuExtensions = DataBrowserContextMenuExtensionUtil.getContextMenuExtensions();

		viewer.getControl().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				final ISelection selection = viewer.getSelection();

				if (selection.isEmpty()) {
					return;
				}

				if (!(selection instanceof TreeSelection)) {
					return;
				}

				final TreeSelection treeSelection = (TreeSelection) selection;

				if (menu == null) {
					menu = mgr.createContextMenu(viewer.getControl());
				}
				final IContributionItem[] l = mgr.getItems();
				mgr.removeAll();
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
								mgr.add(new RunnableAction("Rename", () -> {
									// FIXME: Implement! Dialog for user entry then call handler
								}));
								itemsAdded = true;
							}
							if (!selectedNode.isPublished() && actionHandler.supportsPublish()) {
								mgr.add(new RunnableAction("Publish", () -> {

									if (actionHandler.publish(selectedNode.getDisplayName())) {
										selectedNode.setPublished(true);
									}
								}));
								itemsAdded = true;
							}
							if (actionHandler.supportsDelete()) {
								mgr.add(new RunnableAction("Delete", () -> {
									if (actionHandler.delete(selectedNode.getDisplayName())) {
										parentNode.getChildren().remove(selectedNode);
									}
								}));
								itemsAdded = true;
							}
						}
						// if (!publishCallbacks.isEmpty()) {
						// mgr.add(new RunnableAction("Publish", () -> {
						// LOGGER.debug("publishing {}", selectedNode.getDisplayName());
						// RunnerHelper.asyncExec(() -> {
						// publishCallbacks.get(selectedNode.getParent()).accept(selectedNode.getDisplayName());
						// LOGGER.debug("published {}", selectedNode.getDisplayName());
						// });
						//
						// }));
						// itemsAdded = true;
						// }
						// if (selectedNode.isPublished()) {
						// // grey out for already published versions
						// newItem.setText("(already published)");
						// newItem.setEnabled(false);
						// }
					}
					if (selectedNode instanceof CompositeNode) {
						final CompositeNode compositeNode = (CompositeNode) selectedNode;
						final IDataBrowserActionsHandler actionHandler = compositeNode.getActionHandler();
						if (actionHandler != null) {
							if (actionHandler.supportsSyncUpstream()) {
								mgr.add(new RunnableAction("Check upstream", () -> actionHandler.supportsSyncUpstream()));
								itemsAdded = true;
							}
							if (actionHandler.supportsRefreshLocal()) {
								mgr.add(new RunnableAction("Check local", () -> actionHandler.refreshLocal()));
								itemsAdded = true;
							}
						}
					}
				}

				if (contextMenuExtensions != null) {
					for (final IDataBrowserContextMenuExtension ext : contextMenuExtensions) {
						itemsAdded |= ext.contributeToMenu(treeSelection, mgr);
					}
				}
				if (itemsAdded) {
					menu.setVisible(true);
				}
			}
		});

		final Injector injector = Guice.createInjector(new DataExtensionsModule());
		final Iterable<DataExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<DataExtensionPoint>>() {
		}));
		LOGGER.debug("Found " + Iterables.size(extensions) + " extensions");
		for (final DataExtensionPoint extensionPoint : extensions) {
			final DataExtension dataExtension = extensionPoint.getDataExtension();
			if (dataExtension != null) {
				try {
					root.getChildren().add(dataExtension.getDataRoot());
				} catch (final Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}

		getViewSite().getWorkbenchWindow().getSelectionService().addSelectionListener("com.mmxlabs.scenario.service.ui.navigator", scenarioSelectionListener);
	}

	@Override
	public void dispose() {
		getViewSite().getWorkbenchWindow().getSelectionService().removeSelectionListener("com.mmxlabs.scenario.service.ui.navigator", scenarioSelectionListener);
		super.dispose();
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	private static ComposedAdapterFactory createNewAdapterFactory() {
		// Hook in the global registry to get other adapter factories
		final ComposedAdapterFactory factory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		factory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		factory.addAdapterFactory(new BrowserItemProviderAdapterFactory());
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
}
