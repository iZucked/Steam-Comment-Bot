package com.mmxlabs.rcp.common.debug;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.eclipse.EclipseRegistry.eclipseRegistry;
import static org.ops4j.peaberry.util.TypeLiterals.iterable;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.osgi.service.debug.DebugOptions;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.debug.impl.DebugOptionsExtensionPoint;

public class DebugOptionsUtil {

	public static List<IDebugOptionsProvider> getDebugOptionsProviders() {

		return ServiceHelper.withService(DebugOptions.class, debugOptions -> {
			final BundleContext bundleContext = FrameworkUtil.getBundle(DebugOptionsUtil.class).getBundleContext();

			final Injector injector = Guice.createInjector(Peaberry.osgiModule(bundleContext, eclipseRegistry()), new AbstractModule() {

				@Override
				protected void configure() {

					bind(iterable(DebugOptionsExtensionPoint.class)).toProvider(service(DebugOptionsExtensionPoint.class).multiple());
				}
			});

			class Options {
				@Inject
				private Iterable<DebugOptionsExtensionPoint> debugOptionsProviders;
			}
			final Options options = new Options();
			injector.injectMembers(options);

			final List<IDebugOptionsProvider> providers = new LinkedList<>();
			options.debugOptionsProviders.forEach(ext -> providers.add(ext.getDebugOptionsProvider()));
			return providers;
		});
	}
}
