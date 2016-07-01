/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.util.function.Consumer;
import java.util.function.Function;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public final class ServiceHelper {

	public static <T> void withOptionalService(Class<T> cls, final Consumer<T> withFunc) {
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

	public static <T, V, E extends Exception> V withCheckedOptionalService(Class<T> cls, final CheckedFunction<T, V, E> withFunc) throws E {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
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

	public static <T, V, E extends Exception> V withOptionalService(Class<T> cls, final CheckedFunction<T, V, E> withFunc) throws E {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
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

	public static <T> void withService(Class<T> cls, final Consumer<T> withFunc) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference == null) {
			throw new RuntimeException("Service not found");
		}
		final T service = bundleContext.getService(serviceReference);
		try {
			withFunc.accept(service);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static <T, V> V withService(Class<T> cls, final Function<T, V> withFunc) {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference == null) {
			throw new RuntimeException("Service not found");
		}
		final T service = bundleContext.getService(serviceReference);
		try {
			return withFunc.apply(service);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	@FunctionalInterface
	public interface CheckedFunction<T, R, E extends Exception> {

		/**
		 * Applies this function to the given argument.
		 *
		 * @param t
		 *            the function argument
		 * @return the function result
		 */
		R apply(T t) throws E;
	}

	public static <T, V, E extends Exception> V withCheckedService(Class<T> cls, final CheckedFunction<T, V, E> withFunc) throws E {
		final BundleContext bundleContext = FrameworkUtil.getBundle(ServiceHelper.class).getBundleContext();
		final ServiceReference<T> serviceReference = bundleContext.getServiceReference(cls);
		if (serviceReference == null) {
			throw new RuntimeException("Service not found");
		}
		final T service = bundleContext.getService(serviceReference);
		try {
			return withFunc.apply(service);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}
}
