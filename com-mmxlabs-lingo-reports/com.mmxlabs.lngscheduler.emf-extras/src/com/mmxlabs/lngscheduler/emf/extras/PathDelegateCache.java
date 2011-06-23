/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.tools.DiagnosticCollector;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.osgi.baseadaptor.loader.ClasspathEntry;
import org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.compilation.CharSequenceCompiler;

/**
 * @author Tom Hinton
 * 
 */
public class PathDelegateCache {
	private static final PathDelegateCache INSTANCE = new PathDelegateCache();

	public static PathDelegateCache getInstance() {
		return INSTANCE;
	}

	private final HashMap<List<Object>, IEMFPathDelegate> cache = new HashMap<List<Object>, IEMFPathDelegate>();
	private final CharSequenceCompiler<IEMFPathDelegate> compiler;
	private int index = 0;

	protected PathDelegateCache() {
		String path = "";
		DefaultClassLoader loader = (DefaultClassLoader) getClass().getClassLoader();
		
		for (final ClasspathEntry cpe : loader.getClasspathManager().getHostClasspathEntries()) {
			URI uri = URI.createFileURI(cpe.getBundleFile().getBaseFile().getAbsolutePath());
			path += (path.length() > 0 ? ":" : "" ) + uri.toString();
		}
		
		System.err.println(path);
		
		compiler = new CharSequenceCompiler<IEMFPathDelegate>(
				getClass().getClassLoader(), CollectionsUtil.makeArrayList(
						"-classpath", path));
	}

	public synchronized IEMFPathDelegate getPathDelegate(final List<Object> path) {
		if (cache.containsKey(path)) {
			return cache.get(path);
		} else {
			final IEMFPathDelegate answer = compileDelegate(path);
			cache.put(path, answer);
			return answer;
		}
	}

	/**
	 * @param path
	 * @return
	 */
	private synchronized IEMFPathDelegate compileDelegate(
			final List<Object> path) {
		final StringBuilder source = new StringBuilder();
		final String className = "PathDelegate_" + index;
		index++;

		source.append("package " + getClass().getPackage().getName() + ";\n");

		source.append("public class " + className
				+ " implements IEMFPathDelegate {\n");

		source.append("public Object getValue(final org.eclipse.emf.ecore.EObject var0) {");
		// create the method in here.

		int elementIndex = 0;

		final StringBuffer closers = new StringBuffer();

		for (final Object o : path) {
			final String lastVar = "var" + elementIndex;
			elementIndex++;
			final String thisVar = "var" + elementIndex;
			final String getter;
			final String containerType;
			final String varType;

			if (o instanceof EStructuralFeature) {
				final EStructuralFeature feature = (EStructuralFeature) o;
				getter = "get"
						+ Character.toUpperCase(feature.getName().charAt(0))
						+ feature.getName().substring(1);
				containerType = feature.getContainerClass().getCanonicalName();
				varType = feature.getEType().getInstanceClass()
						.getCanonicalName();
			} else if (o instanceof EOperation) {
				final EOperation operation = (EOperation) o;
				containerType = operation.getEContainingClass()
						.getInstanceClass().getCanonicalName();
				varType = operation.getEType().getInstanceClass()
						.getCanonicalName();
				getter = operation.getName();
			} else {
				return null;
			}
			source.append("\n");
			source.append("if (" + lastVar + "== null) return null;\n");
			source.append("if (" + lastVar + " instanceof " + containerType
					+ ") {\n");
			source.append("final " + varType + " " + thisVar + " = (("
					+ containerType + ")" + lastVar + ")." + getter + "();\n");

			closers.append("} else { return null; }\n");
		}

		final String lastVar = "var" + elementIndex;
		source.append("return " + lastVar + ";\n");

		source.append(closers);

		source.append("return null;\n");

		source.append("}\n");

		source.append("}\n");

		final DiagnosticCollector collector = new DiagnosticCollector();
		try {
			final Class<IEMFPathDelegate> theClass = compiler.compile(
					getClass().getPackage().getName() + "." + className,
					source, collector, IEMFPathDelegate.class);

			final IEMFPathDelegate delegate = theClass.getConstructor()
					.newInstance();
			return delegate;
		} catch (final Exception ex) {
			System.err.println("Error flash-compiling this:");
			System.err.println(source);
			System.err.println(ex);
			System.err.println(collector.getDiagnostics());
			return null;
		}
	}
}
