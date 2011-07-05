/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.tum.cs.cope.migration.execution.MigrationException;
import edu.tum.cs.cope.migration.execution.Migrator;
import edu.tum.cs.cope.migration.execution.MigratorRegistry;
import edu.tum.cs.cope.migration.execution.ReleaseUtil;

/**
 * A {@link Factory} which delegates to another factory, but also runs any COPE
 * migrations on resources before they are loaded.
 * 
 * @author Tom Hinton
 * 
 */
public class UpgradingResourceFactory implements Factory {
	private static final Logger log = LoggerFactory
			.getLogger(UpgradingResourceFactory.class);

	final Factory delegate;

	/**
	 * Construct an UpgradingResourceFactory which delegates to the argument.
	 * 
	 * @param delegate
	 *            the delegate which is really used for loading the resource.
	 */
	public UpgradingResourceFactory(final Factory delegate) {
		this.delegate = delegate;
	}

	/**
	 * This set contains the URI of any resource which is currently being
	 * migrated on this thread. When a resource is loaded on thread A and gets
	 * migrated, subsequent invocations on thread A for the same URI are
	 * presumed to be from the migration mechanism, and so shouldn't trigger
	 * another migrate but just fall through.
	 * 
	 * Invocations from thread B should block on any migrating URI until it's
	 * done - this is handled by the {@link #monitors} map.
	 */
	private ThreadLocal<Set<URI>> currentlyMigrating = new ThreadLocal<Set<URI>>() {
		@Override
		protected Set<URI> initialValue() {
			return new HashSet<URI>();
		}
	};

	/**
	 * This map contains objects used as monitors for each loaded URI - the
	 * purpose of this is to prevent two threads attempting to migrate the same
	 * resource concurrently, or a second thread from loading a resource which
	 * is inconsistent due to being in the process of migration.
	 * 
	 * TODO Consider the leaking issue here. It's a small one.
	 */
	private Map<URI, Object> monitors = new HashMap<URI, Object>();

	/**
	 * Get a monitor for the given URI.
	 * @param uri
	 * @return
	 */
	private Object getMonitor(final URI uri) {
		Object monitor = null;
		synchronized (monitors) {
			monitors.get(uri);
			if (monitor == null) {
				monitor = new Object();
				monitors.put(uri, monitor);
			}
		}
		return monitor;
	}

	private void migrate(final URI uri) {
		if (currentlyMigrating.get().contains(uri))
			return;
		final String namespace = ReleaseUtil.getNamespaceURI(uri);
		final Migrator migrator = MigratorRegistry.getInstance().getMigrator(
				namespace);

		if (migrator != null) {
			// int r = migrator.getRelease(uri).iterator().next();
			int r = Integer.MIN_VALUE;
			for (int x : migrator.getRelease(uri)) {
				r = Math.max(r, x);
			}
			if (r != Integer.MIN_VALUE)
				try {
					currentlyMigrating.get().add(uri);
					log.info("Migrating " + uri + " to version " + r);
					migrator.migrateAndSave(Collections.singletonList(uri), r,
							Integer.MAX_VALUE, new NullProgressMonitor());
				} catch (final MigrationException e) {
					throw new RuntimeException(e);
				} finally {
					currentlyMigrating.get().remove(uri);
				}
		}
	}

	/**
	 * Create a resource for the given URI, possibly migrating it to the latest
	 * model version.
	 */
	@Override
	public Resource createResource(final URI uri) {
		final Object monitor = getMonitor(uri);
		synchronized (monitor) {
			migrate(uri);
		}
		return delegate.createResource(uri);
	}
}
