package com.mmxlabs.lngdataserver.browser.ui;

import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
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
import com.mmxlabs.lngdataserver.browser.provider.BrowserItemProviderAdapterFactory;
import com.mmxlabs.rcp.common.ViewerHelper;

public class DataBrowser extends ViewPart {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataBrowser.class);

	private TreeViewer viewer;
	private CompositeNode root;

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

		Injector injector = Guice.createInjector(new DataExtensionsModule());
		Iterable<DataExtensionPoint> extensions = injector.getInstance(Key.get(new TypeLiteral<Iterable<DataExtensionPoint>>() {
		}));
		LOGGER.debug("Found " + Iterables.size(extensions) + " extensions");
		for (DataExtensionPoint extensionPoint : extensions) {
			DataExtension dataExtension = extensionPoint.getDataExtension();
			if (dataExtension != null) {
				try {
					root.getChildren().add(dataExtension.getDataRoot());
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
