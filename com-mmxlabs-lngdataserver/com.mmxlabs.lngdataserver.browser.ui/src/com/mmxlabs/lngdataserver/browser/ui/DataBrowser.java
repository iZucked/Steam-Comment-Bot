package com.mmxlabs.lngdataserver.browser.ui;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
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
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ViewerHelper;

public class DataBrowser extends ViewPart {

	public static final String ID = "com.mmxlabs.lngdataserver.browser.ui.DataBrowser";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataBrowser.class);

	private TreeViewer viewer;
	private CompositeNode root;
	private Map<Node, Consumer<String>> publishCallbacks = new HashMap<Node, Consumer<String>>();
	private Map<Node, Consumer<String>> checkUpstreamCallbacks = new HashMap<Node, Consumer<String>>();

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.NONE);
		viewer.setContentProvider(new DataBrowserContentProvider(createNewAdapterFactory()));
		viewer.setLabelProvider(new DataBrowserLabelProvider(createNewAdapterFactory()));

		root = BrowserFactory.eINSTANCE.createCompositeNode();
		root.setDisplayName("Versions");
		viewer.setInput(root);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				System.out.println("selection changed");

			}
		});
		getSite().setSelectionProvider(viewer);

		final Menu menu = new Menu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				ISelection selection = viewer.getSelection();

				if (selection.isEmpty()) {
					return;
				}

				if (!(selection instanceof TreeSelection)) {
					return;
				}

				TreeSelection treeSelection = (TreeSelection) selection;

				MenuItem[] items = menu.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].dispose();
				}
				if (treeSelection.getFirstElement() instanceof Node) {
					Node selectedNode = (Node) treeSelection.getFirstElement();
					if (!(selectedNode instanceof CompositeNode)) {
						MenuItem newItem = new MenuItem(menu, SWT.NONE);
						newItem.setText("publish");
						newItem.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent e) {
								// TODO Auto-generated method stub
								LOGGER.debug("publishing {}", selectedNode.getDisplayName());
								RunnerHelper.asyncExec(() -> {
									publishCallbacks.get(selectedNode.getParent()).accept(selectedNode.getDisplayName());
									LOGGER.debug("published {}", selectedNode.getDisplayName());
								});
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
								// TODO Auto-generated method stub

							}
						});
						if (selectedNode.isPublished()) {
							// grey out for already published versions
							newItem.setText("(already published)");
							newItem.setEnabled(false);
						}
					}
					if (selectedNode instanceof CompositeNode) {

						if (false && checkUpstreamCallbacks.get(selectedNode) != null) {
							MenuItem newItem = new MenuItem(menu, SWT.NONE);
							newItem.setText("Check upstream");
							newItem.addSelectionListener(new SelectionListener() {

								@Override
								public void widgetSelected(SelectionEvent e) {
									LOGGER.debug("Checking upstream {}", selectedNode.getDisplayName());
									RunnerHelper.asyncExec(() -> {
										checkUpstreamCallbacks.get(selectedNode).accept(selectedNode.getDisplayName());
										LOGGER.debug("published {}", selectedNode.getDisplayName());
									});
								}

								@Override
								public void widgetDefaultSelected(SelectionEvent e) {

								}
							});
						}
					}
				}
			}
		});

		Injector injector = Guice.createInjector(new DataExtensionsModule());
		Iterable<DataExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<DataExtensionPoint>>() {
		}));
		LOGGER.debug("Found " + Iterables.size(extensions) + " extensions");
		for (DataExtensionPoint extensionPoint : extensions) {
			DataExtension dataExtension = extensionPoint.getDataExtension();
			if (dataExtension != null) {
				try {
					root.getChildren().add(dataExtension.getDataRoot());
					publishCallbacks.put(extensionPoint.getDataExtension().getDataRoot(), extensionPoint.getDataExtension().getPublishCallback());
					Consumer<String> refreshUpstreamCallback = extensionPoint.getDataExtension().getRefreshUpstreamCallback();
					if (refreshUpstreamCallback != null) {
						checkUpstreamCallbacks.put(extensionPoint.getDataExtension().getDataRoot(), refreshUpstreamCallback);
					}
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
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
