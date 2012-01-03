/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.edapt.history.Release;
import org.eclipse.emf.edapt.migration.MigrationException;
import org.eclipse.emf.edapt.migration.ReleaseUtils;
import org.eclipse.emf.edapt.migration.execution.Migrator;
import org.eclipse.emf.edapt.migration.execution.MigratorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final ThreadLocal<Set<URI>> currentlyMigrating = new ThreadLocal<Set<URI>>() {
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
	private final Map<URI, Object> monitors = new HashMap<URI, Object>();

	/**
	 * Get a monitor for the given URI, to prevent reentry on load.
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
		final String namespace = ReleaseUtils.getNamespaceURI(uri);
		final Migrator migrator = MigratorRegistry.getInstance().getMigrator(
				namespace);

		if (migrator != null) {
			// int r = migrator.getRelease(uri).iterator().next();
			Release latest = null;

			for (final Release x : migrator.getRelease(uri)) {
				if (x.isLatestRelease())
					latest = x;
			}
			if (latest != null)
				try {
					currentlyMigrating.get().add(uri);
					log.info("Migrating " + uri + " to version " + latest);
					migrator.migrateAndSave(Collections.singletonList(uri), latest, null, new NullProgressMonitor());
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
