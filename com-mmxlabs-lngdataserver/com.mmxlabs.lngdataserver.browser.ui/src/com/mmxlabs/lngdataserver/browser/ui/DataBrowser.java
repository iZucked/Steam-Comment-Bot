/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.browser.ui;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.hub.services.users.UsernameProvider;
import com.mmxlabs.lngdataserver.browser.BrowserFactory;
import com.mmxlabs.lngdataserver.browser.BrowserPackage;
import com.mmxlabs.lngdataserver.browser.CompositeNode;
import com.mmxlabs.lngdataserver.browser.Leaf;
import com.mmxlabs.lngdataserver.browser.Node;
import com.mmxlabs.lngdataserver.browser.provider.BrowserItemProviderAdapterFactory;
import com.mmxlabs.lngdataserver.browser.ui.context.DataBrowserContextMenuExtensionUtil;
import com.mmxlabs.lngdataserver.browser.ui.context.IDataBrowserContextMenuExtension;
import com.mmxlabs.lngdataserver.browser.util.DataExtension;
import com.mmxlabs.lngdataserver.commons.IDataBrowserActionsHandler;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.util.ScenarioServiceAdapterFactory;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;
import com.mmxlabs.scenario.service.ui.IScenarioVersionService;

public class DataBrowser extends ViewPart {

	public static final String ID = "com.mmxlabs.lngdataserver.browser.ui.DataBrowser";

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBrowser.class);

	private Image bcImage;
	private GridTableViewer baseCaseViewer;
	private GridTreeViewer dataViewer;
	private GridTreeViewer scenarioViewer;
	private CompositeNode root;
	private final Set<Node> selectedNodes = new HashSet<>();
	private Predicate<ScenarioInstance> selectedScenarioChecker = null;

	private ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> scenarioTracker;

	private ServiceTracker<IBaseCaseVersionsProvider, IBaseCaseVersionsProvider> baseCaseVersionsTracker;

	private ServiceTracker<IScenarioVersionService, IScenarioVersionService> scenarioVersionsTracker;

	private ServiceTracker<DataExtension, DataExtension> dataExtensionTracker;

	private Iterable<IDataBrowserContextMenuExtension> contextMenuExtensions;
	private GridViewerColumn dataViewerColumn1;

	private final IBaseCaseVersionsProvider.IBaseCaseChanged baseChangedListener = () -> {

		ViewerHelper.setInput(baseCaseViewer, false, getBaseCaseList(baseCaseVersionsTracker.getService()));
		ViewerHelper.refresh(dataViewer, false);
		ViewerHelper.refresh(scenarioViewer, false);
		String lockedBy = baseCaseVersionsTracker.getService().getLockedBy();
		if (lockedBy == null) {
			dataViewerColumn1.getColumn().setText("not locked");
		} else {
			dataViewerColumn1.getColumn().setText("  locked by " + UsernameProvider.INSTANCE.getDisplayName(lockedBy));
		}
	};

	private final IScenarioVersionService.IChangedListener scenarioVersionsChangedListener = () -> {
		ViewerHelper.refresh(scenarioViewer, false);
	};

	private final ISelectionChangedListener scenarioSelectionListener = event -> {
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
	};
	private final ISelectionChangedListener nodeSelectionListener = event -> {
		final ISelection selection = dataViewer.getSelection();

		selectedScenarioChecker = null;
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			final Iterator<?> itr = ss.iterator();
			if (ss.size() == 1) {
				while (itr.hasNext()) {
					final Object o = itr.next();
					if (o instanceof Leaf) {
						final Leaf leaf = (Leaf) o;
						final CompositeNode compositeNode = leaf.getParent();
						if (compositeNode != null) {

							selectedScenarioChecker = scenarioInstance -> {
								final Manifest mf = scenarioInstance.getManifest();
								if (mf != null) {
									for (final ModelArtifact modelArtifact : mf.getModelDependencies()) {
										final String v = modelArtifact.getDataVersion();
										if (Objects.equals(modelArtifact.getKey(), compositeNode.getType())) {
											if (Objects.equals(leaf.getVersionIdentifier(), v)) {
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
	};

	private DataBrowserLabelProvider dataLabelProvider;


	@Override
	public void createPartControl(final Composite parent) {
		final Bundle bundle = FrameworkUtil.getBundle(DataBrowser.class);

		bcImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/base-flag.png").createImage();

		scenarioTracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(bundle.getBundleContext(), ScenarioServiceRegistry.class, null) {
			@Override
			public ScenarioServiceRegistry addingService(final ServiceReference<ScenarioServiceRegistry> reference) {
				final ScenarioServiceRegistry reg = super.addingService(reference);
				ViewerHelper.setInput(scenarioViewer, false, reg);
				ViewerHelper.runIfViewerValid(scenarioViewer, false, () -> scenarioViewer.expandToLevel(2));

				return reg;
			}

			@Override
			public void removedService(ServiceReference<ScenarioServiceRegistry> reference, ScenarioServiceRegistry service) {
				ViewerHelper.setInput(scenarioViewer, false, (Object) null);
				super.removedService(reference, service);
			}
		};
		scenarioTracker.open();

		baseCaseVersionsTracker = new ServiceTracker<IBaseCaseVersionsProvider, IBaseCaseVersionsProvider>(bundle.getBundleContext(), IBaseCaseVersionsProvider.class, null) {
			@Override
			public IBaseCaseVersionsProvider addingService(final ServiceReference<IBaseCaseVersionsProvider> reference) {
				final IBaseCaseVersionsProvider provider = super.addingService(reference);
				provider.addChangedListener(baseChangedListener);
				ViewerHelper.refresh(scenarioViewer, false);
				if (dataLabelProvider != null) {
					dataLabelProvider.setBaseCaseProvider(provider);
					ViewerHelper.refresh(dataViewer, false);
				}
				ViewerHelper.setInput(baseCaseViewer, false, getBaseCaseList(provider));

				return provider;
			}

			@Override
			public void removedService(final ServiceReference<IBaseCaseVersionsProvider> reference, final IBaseCaseVersionsProvider provider) {
				provider.removeChangedListener(baseChangedListener);
				super.removedService(reference, provider);
				if (dataLabelProvider != null) {
					dataLabelProvider.setBaseCaseProvider(null);
					ViewerHelper.refresh(dataViewer, false);
				}
				ViewerHelper.refresh(scenarioViewer, false);
				ViewerHelper.setInput(baseCaseViewer, false, Collections.emptyList());

			}
		};
		baseCaseVersionsTracker.open();
		final IBaseCaseVersionsProvider s = baseCaseVersionsTracker.getService();
		if (s != null) {
			s.addChangedListener(baseChangedListener);
		}

		scenarioVersionsTracker = new ServiceTracker<IScenarioVersionService, IScenarioVersionService>(bundle.getBundleContext(), IScenarioVersionService.class, null) {
			@Override
			public IScenarioVersionService addingService(final ServiceReference<IScenarioVersionService> reference) {
				final IScenarioVersionService provider = super.addingService(reference);
				provider.addChangedListener(scenarioVersionsChangedListener);
				ViewerHelper.refresh(scenarioViewer, false);

				return provider;
			}

			@Override
			public void removedService(final ServiceReference<IScenarioVersionService> reference, final IScenarioVersionService provider) {
				provider.removeChangedListener(scenarioVersionsChangedListener);
				super.removedService(reference, provider);
				ViewerHelper.refresh(scenarioViewer, false);
			}
		};
		scenarioVersionsTracker.open();
		final IScenarioVersionService s2 = scenarioVersionsTracker.getService();
		if (s2 != null) {
			s2.addChangedListener(scenarioVersionsChangedListener);
		}

		final SashForm sash = new SashForm(parent, SWT.SMOOTH | SWT.HORIZONTAL);
		sash.setSashWidth(3);

		// Change the colour used to paint the sashes
		sash.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));

		contextMenuExtensions = DataBrowserContextMenuExtensionUtil.getContextMenuExtensions();

		final SashForm navSash = new SashForm(sash, SWT.SMOOTH | SWT.VERTICAL);
		navSash.setSashWidth(3);

		createBaseCaseViewer(parent, navSash);
		createScenarioViewer(parent, navSash);
		createDataViewer(parent, sash);
		navSash.setWeights(new int[] { 10, 95 });
		sash.setWeights(new int[] { 27, 73 });

		scenarioViewer.addSelectionChangedListener(scenarioSelectionListener);
		dataViewer.addSelectionChangedListener(nodeSelectionListener);

		root.eAdapters().add(new EContentAdapter() {
			@Override
			public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {
				super.notifyChanged(notification);
				if (notification.isTouch()) {
					return;
				}
				if (notification.getFeature() == BrowserPackage.Literals.COMPOSITE_NODE__CURRENT) {
					ViewerHelper.refresh(scenarioViewer, false);
				}
				ViewerHelper.runIfViewerValid(dataViewer, false, v -> v.expandAll());
			}
		});
		dataViewer.expandAll();

		dataExtensionTracker = new ServiceTracker<DataExtension, DataExtension>(bundle.getBundleContext(), DataExtension.class, null) {
			@Override
			public DataExtension addingService(final ServiceReference<DataExtension> reference) {
				final DataExtension reg = super.addingService(reference);
				final CompositeNode dataRoot = reg.getDataRoot();
				RunnerHelper.asyncExec(() -> root.getChildren().add(dataRoot));
				return reg;
			}

			@Override
			public void removedService(ServiceReference<DataExtension> reference, DataExtension reg) {
				final CompositeNode dataRoot = reg.getDataRoot();
				RunnerHelper.asyncExec(() -> root.getChildren().remove(dataRoot));
				super.removedService(reference, reg);
			}
		};
		dataExtensionTracker.open();
	}

	private void createBaseCaseViewer(final Composite parent, final SashForm sash) {
		final Composite sv = new Composite(sash, SWT.NONE);
		sv.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		sv.setLayout(GridLayoutFactory.fillDefaults().create());
		sv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		baseCaseViewer = new GridTableViewer(sv, SWT.SINGLE | SWT.V_SCROLL);
		baseCaseViewer.setContentProvider(new ArrayContentProvider());
		baseCaseViewer.getGrid().setBackgroundMode(SWT.INHERIT_NONE);
		baseCaseViewer.getGrid().setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridViewerHelper.configureLookAndFeel(baseCaseViewer);
		baseCaseViewer.getGrid().setLinesVisible(true);
		baseCaseViewer.getGrid().setHeaderVisible(true);
		baseCaseViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		{
			final GridViewerColumn c1 = new GridViewerColumn(baseCaseViewer, SWT.NONE);
			c1.setLabelProvider(new ScenarioLabelProvider(0));
			c1.getColumn().setText("Base case");
			c1.getColumn().setWidth(450);
			GridViewerHelper.configureLookAndFeel(c1);
		}
		// {
		// final GridViewerColumn c2 = new GridViewerColumn(baseCaseViewer, SWT.NONE);
		// c2.setLabelProvider(new ScenarioLabelProvider(1));
		// c2.getColumn().setWidth(60);
		// GridViewerHelper.configureLookAndFeel(c2);
		// c2.getColumn().setText("Status");
		// }

		IBaseCaseVersionsProvider service2 = baseCaseVersionsTracker.getService();
		if (service2 != null) {
			ViewerHelper.setInput(baseCaseViewer, false, getBaseCaseList(service2));
		}

		final MenuManager scenarioMgr = new MenuManager();
		baseCaseViewer.getControl().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				final ISelection selection = baseCaseViewer.getSelection();

				if (selection.isEmpty()) {
					return;
				}

				if (menu == null) {
					menu = scenarioMgr.createContextMenu(baseCaseViewer.getControl());
				}
				final IContributionItem[] l = scenarioMgr.getItems();
				scenarioMgr.removeAll();
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
						 itemsAdded |= ext.contributeToBaseCaseMenu(scenarioMgr);
					}
				}

				if (itemsAdded) {
					menu.setVisible(true);
				}
			}
		});
//		baseCaseViewer.getControl().setEnabled(false);
	}

	private void createScenarioViewer(final Composite parent, final SashForm sash) {
		final Composite sv = new Composite(sash, SWT.NONE);
		sv.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		sv.setLayout(GridLayoutFactory.fillDefaults().create());
		sv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		scenarioViewer = new GridTreeViewer(sv, SWT.SINGLE | SWT.V_SCROLL);
		scenarioViewer.getGrid().setBackgroundMode(SWT.INHERIT_NONE);
		scenarioViewer.getGrid().setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridViewerHelper.configureLookAndFeel(scenarioViewer);
		scenarioViewer.getGrid().setLinesVisible(true);
		scenarioViewer.getGrid().setHeaderVisible(true);
		scenarioViewer.setContentProvider(new LocalScenarioServiceContentProvider());
		scenarioViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		{
			final GridViewerColumn c1 = new GridViewerColumn(scenarioViewer, SWT.NONE);
			c1.setLabelProvider(new ScenarioLabelProvider(0));
			c1.getColumn().setTree(true);
			c1.getColumn().setWidth(400);
			GridViewerHelper.configureLookAndFeel(c1);
		}
		{
			final GridViewerColumn c2 = new GridViewerColumn(scenarioViewer, SWT.NONE);
			c2.setLabelProvider(new ScenarioLabelProvider(1));
			c2.getColumn().setWidth(60);
			GridViewerHelper.configureLookAndFeel(c2);
			c2.getColumn().setText("Status");
		}
		scenarioViewer.setAutoExpandLevel(GridTreeViewer.ALL_LEVELS);
		scenarioViewer.setInput(scenarioTracker.getService());
		scenarioViewer.expandToLevel(2);
		final MenuManager scenarioMgr = new MenuManager();
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
					menu = scenarioMgr.createContextMenu(scenarioViewer.getControl());
				}
				final IContributionItem[] l = scenarioMgr.getItems();
				scenarioMgr.removeAll();
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
						itemsAdded |= ext.contributeToScenarioMenu(treeSelection, scenarioMgr);
					}
				}

				if (itemsAdded) {
					menu.setVisible(true);
				}
			}
		});
	}

	private void createDataViewer(final Composite parent, final SashForm sash) {
		final Composite dv = new Composite(sash, SWT.NONE);
		dv.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		dv.setLayout(GridLayoutFactory.fillDefaults().create());
		dv.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		dataViewer = new GridTreeViewer(dv, SWT.NONE | SWT.SINGLE | SWT.V_SCROLL);
		dataViewer.setContentProvider(new DataBrowserContentProvider(createNewAdapterFactory()));
		dataViewer.setAutoExpandLevel(GridTreeViewer.ALL_LEVELS);
		dataViewer.getGrid().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		dataViewerColumn1 = new GridViewerColumn(dataViewer, SWT.NONE);
		dataViewerColumn1.getColumn().setTree(true);
		dataViewerColumn1.getColumn().setWidth(300);

		dataLabelProvider = new DataBrowserLabelProvider(createNewAdapterFactory(), selectedNodes);
		dataViewerColumn1.setLabelProvider(dataLabelProvider);
		dataLabelProvider.setBaseCaseProvider(baseCaseVersionsTracker.getService());
		GridViewerHelper.configureLookAndFeel(dataViewerColumn1);

		GridViewerHelper.configureLookAndFeel(dataViewer);

		root = BrowserFactory.eINSTANCE.createCompositeNode();
		root.setDisplayName("Versions");
		dataViewer.setInput(root);

		getSite().setSelectionProvider(dataViewer);

		final MenuManager dataMgr = new MenuManager();
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
					menu = dataMgr.createContextMenu(dataViewer.getControl());
				}
				final IContributionItem[] l = dataMgr.getItems();
				dataMgr.removeAll();
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
						final CompositeNode parentNode = selectedNode.getParent();
						if (parentNode != null) {
							final IDataBrowserActionsHandler actionHandler = parentNode.getActionHandler();
							if (actionHandler != null) {
								// if (actionHandler.supportsRename()) {
								// dataMgr.add(new RunnableAction("Rename", () -> {
								// final IInputValidator validator = null;
								// final InputDialog dialog = new InputDialog(Display.getDefault().getActiveShell(), "Rename version " + selectedNode.getDisplayName(), "Choose new element name",
								// "", validator);
								// if (dialog.open() == Window.OK) {
								// dialog.getValue();
								// }
								//
								// if (actionHandler.rename(selectedNode.getVersionIdentifier(), dialog.getValue())) {
								// selectedNode.setDisplayName(dialog.getValue());
								// selectedNode.setVersionIdentifier(dialog.getValue());
								// }
								// }));
								// itemsAdded = true;
								// } else {
								// dataMgr.add(new RunnableAction("Rename (Not suppported)", () -> {
								//
								// }));
								// itemsAdded = true;
								// }
								// if (/* !selectedNode.isPublished() && */ actionHandler.supportsPublish()) {
								// dataMgr.add(new RunnableAction("Publish", () -> {
								// if (actionHandler.publish(selectedNode.getVersionIdentifier())) {
								// // selectedNode.setPublished(true);
								// }
								// }));
								// itemsAdded = true;
								// }
								// if (actionHandler.supportsDelete()) {
								// dataMgr.add(new RunnableAction("Delete", () -> {
								// if (actionHandler.delete(selectedNode.getVersionIdentifier())) {
								// parentNode.getChildren().remove(selectedNode);
								// }
								// }));
								// itemsAdded = true;
								// } else {
								// dataMgr.add(new RunnableAction("Delete (Not suppported)", () -> {
								//
								// }));
								// itemsAdded = true;
								// }

								// if (actionHandler.supportsSetCurrent()) {
								// data_mgr.add(new RunnableAction("Set as Current", () -> {
								// if (actionHandler.setCurrent(selectedNode.getVersionIdentifier())) {
								// parentNode.setCurrent(selectedNode);
								// selectedNode.getParent().setCurrent(selectedNode);
								// }
								// }));
								// itemsAdded = true;
								// } else {
								// data_mgr.add(new RunnableAction("Set as Current (Not suppported)", () -> {
								//
								// }));
								// itemsAdded = true;
								// }
							}
						}
					}
					if (selectedNode instanceof CompositeNode) {
						final CompositeNode compositeNode = (CompositeNode) selectedNode;
						final IDataBrowserActionsHandler actionHandler = compositeNode.getActionHandler();
						if (actionHandler != null) {
							if (actionHandler.supportsSyncUpstream()) {
								dataMgr.add(new RunnableAction("Check upstream", actionHandler::syncUpstream));
								itemsAdded = true;
							}
							if (actionHandler.supportsRefreshLocal()) {
								dataMgr.add(new RunnableAction("Refresh", actionHandler::refreshLocal));
								itemsAdded = true;
							}
						}
					}
				}

				if (contextMenuExtensions != null) {
					for (final IDataBrowserContextMenuExtension ext : contextMenuExtensions) {
						itemsAdded |= ext.contributeToDataMenu(treeSelection, dataMgr);
					}
				}
				if (itemsAdded) {
					menu.setVisible(true);
				}
			}
		});
	}

	@Override
	public void dispose() {
		if (scenarioTracker != null) {
			scenarioTracker.close();
		}
		if (baseCaseVersionsTracker != null) {
			baseCaseVersionsTracker.getService().removeChangedListener(baseChangedListener);
			baseCaseVersionsTracker.close();
		}
		if (scenarioVersionsTracker != null) {
			scenarioVersionsTracker.getService().removeChangedListener(scenarioVersionsChangedListener);
			scenarioVersionsTracker.close();
		}
		if (dataExtensionTracker != null) {
			dataExtensionTracker.close();
		}
		if (bcImage != null) {
			bcImage.dispose();
			bcImage = null;
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

	class ScenarioLabelProvider extends ColumnLabelProvider {

		private final AdapterFactoryLabelProvider lp;
		private final int columnIdx;

		public ScenarioLabelProvider(final int columnIdx) {
			this.columnIdx = columnIdx;
			lp = new AdapterFactoryLabelProvider(createNewScenarioModelAdapterFactory());
		}

		@Override
		public String getText(final Object object) {
			if (columnIdx > 0) {

				if (object instanceof ScenarioInstance) {
					ScenarioInstance scenarioInstance = (ScenarioInstance) object;
					IScenarioVersionService svs = scenarioVersionsTracker.getService();
					if (svs != null) {
						if (svs.differentToBaseCase(scenarioInstance)) {
							return "↑";
						} else {
							return "";
						}
					}
					return "↑";

				}

				return "";
			}
			String text = lp.getText(object);
			if (object instanceof ScenarioInstance) {

				ScenarioInstance scenarioInstance = (ScenarioInstance) object;
				final IScenarioService ss = SSDataManager.Instance.findScenarioService(scenarioInstance);
				if (ss != null && !ss.getServiceModel().isLocal()) {
					final Metadata metadata = scenarioInstance.getMetadata();
					if (metadata != null) {
						final Date created = metadata.getCreated();
						if (created != null) {
							final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
							format.setTimeZone(TimeZone.getTimeZone("UTC"));
							text += " [" + format.format(created) + "]";// [" + metadata.getCreator() + "]";
						}
					}
				}
			}
			return text;
		}

		@Override
		public Color getBackground(final Object element) {

			if (element instanceof ScenarioInstance) {
				final ScenarioInstance scenarioInstance = (ScenarioInstance) element;
				final Predicate<ScenarioInstance> checker = selectedScenarioChecker;
				if (checker != null && checker.test(scenarioInstance)) {
					return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GREEN);
				}
			}

			return null;
		}

		@Override
		public Image getImage(final Object element) {
			if (columnIdx == 0) {

				IBaseCaseVersionsProvider service = baseCaseVersionsTracker.getService();
				if (service != null && element == service.getBaseCase()) {
					return bcImage;
				}

				return lp.getImage(element);
			}
			return null;
		}
	}

	private static @NonNull List<@Nullable ScenarioInstance> getBaseCaseList(final IBaseCaseVersionsProvider provider) {
		ScenarioInstance baseCase = provider.getBaseCase();
		if (baseCase != null) {
			return Collections.singletonList(baseCase);
		} else {
			return Collections.emptyList();
		}
	}
}
