/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.common.util.CheckedConsumer;
import com.mmxlabs.common.util.CheckedFunction;

public final class ServiceHelper {

	private static final String MSG_SERVICE_NOT_FOUND = "Service not found";

	@SuppressWarnings("null")
	public static <T> void withOptionalServiceConsumer(final Class<T> cls, final Consumer<@Nullable T> withFunc) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference != null) {
			final T service = bundleContext.getService(serviceReference);
			try {
				withFunc.accept(service);
			} finally {
				bundleContext.ungetService(serviceReference);
			}
		} else {
			withFunc.accept((T) null);
		}
	}

	@SuppressWarnings("null")
	public static <T, V, E extends Exception> V withCheckedOptionalService(final Class<T> cls, final CheckedFunction<@Nullable T, V, E> withFunc) throws E {
		Bundle bundle = FrameworkUtil.getBundle(ServiceHelper.class);
		if (bundle == null) {
			return withFunc.apply((T) null);
		}
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference != null) {
			final T service = bundleContext.getService(serviceReference);
			try {
				return withFunc.apply(service);
			} finally {
				bundleContext.ungetService(serviceReference);
			}
		} else {
			return withFunc.apply((T) null);
		}
	}

	@SuppressWarnings("null")
	public static <T, E extends Exception> void withCheckedOptionalServiceConsumer(final Class<T> cls, final CheckedConsumer<T, E> withFunc) throws E {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference != null) {
			final T service = bundleContext.getService(serviceReference);
			try {
				withFunc.accept(service);
			} finally {
				bundleContext.ungetService(serviceReference);
			}
		} else {
			withFunc.accept((T) null);
		}
	}

	@SuppressWarnings("null")
	public static <T, V, E extends Exception> V withOptionalService(final Class<T> cls, final CheckedFunction<T, V, E> withFunc) throws E {
		Bundle bundle = FrameworkUtil.getBundle(ServiceHelper.class);
		if (bundle == null) {
			// Non-OSGi codepath
			return withFunc.apply((T) null);
		}
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference != null) {
			final T service = bundleContext.getService(serviceReference);
			try {
				return withFunc.apply(service);
			} finally {
				bundleContext.ungetService(serviceReference);
			}
		} else {
			return withFunc.apply((T) null);
		}
	}

	public static <T> void withServiceConsumer(final Class<T> cls, final Consumer<T> withFunc) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference == null) {
			throw new RuntimeException(MSG_SERVICE_NOT_FOUND);
		}
		final T service = bundleContext.getService(serviceReference);
		try {
			withFunc.accept(service);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static <T, V> V withService(final Class<T> cls, final Function<T, V> withFunc) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference == null) {
			throw new RuntimeException(MSG_SERVICE_NOT_FOUND);
		}
		final T service = bundleContext.getService(serviceReference);
		try {
			return withFunc.apply(service);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static <T, V, E extends Exception> V withCheckedService(final Class<T> cls, final CheckedFunction<T, V, E> withFunc) throws E {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference == null) {
			throw new RuntimeException(MSG_SERVICE_NOT_FOUND);
		}
		final T service = bundleContext.getService(serviceReference);
		try {
			return withFunc.apply(service);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static <T, E extends Exception> void withCheckedServiceConsumer(final Class<T> cls, final CheckedConsumer<T, E> withFunc) throws E {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference == null) {
			throw new RuntimeException(MSG_SERVICE_NOT_FOUND);
		}
		final T service = bundleContext.getService(serviceReference);
		try {
			withFunc.accept(service);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	/**
	 * Loop through all services of requested class. Pass the service instance to the given function. If the function returns false, terminate the loop, otherwise continue to the next service.
	 * 
	 * @param cls
	 * @param withFunc
	 */
	public static <T> void withAllServices(final Class<T> cls, @Nullable String filter, final Predicate<T> withFunc) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		Collection<ServiceReference<T>> serviceReferences;
		try {
			serviceReferences = bundleContext.getServiceReferences(cls, filter);

			for (final ServiceReference<T> serviceReference : serviceReferences) {
				final T service = bundleContext.getService(serviceReference);
				try {
					final boolean cont = withFunc.test(service);
					if (!cont) {
						break;
					}
				} finally {
					bundleContext.ungetService(serviceReference);
				}
			}
		} catch (final InvalidSyntaxException e) {
			// Note: as we pass in a null filter we do not expect to get here.
			assert false;
			throw new RuntimeException(e);
		}
	}
}
