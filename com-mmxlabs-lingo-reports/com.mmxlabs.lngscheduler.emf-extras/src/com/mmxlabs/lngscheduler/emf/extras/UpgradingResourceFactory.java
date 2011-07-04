/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.Collections;

import org.eclipse.core.runtime.IProgressMonitor;
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
	 * A threadlocal is used here because it's only re-entry in the same thread
	 * that's necessarily a problem.
	 */
	private ThreadLocal<Boolean> alreadyMigrating = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};

	private void migrate(final URI uri) {
		if (alreadyMigrating.get())
			return;
		alreadyMigrating.set(true);

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
					log.info("Migrating " + uri + " to version " + r);
					migrator.migrateAndSave(Collections.singletonList(uri), r,
							Integer.MAX_VALUE, new IProgressMonitor() {

								@Override
								public void worked(int work) {
									// TODO Auto-generated method stub

								}

								@Override
								public void subTask(String name) {
									// TODO Auto-generated method stub

								}

								@Override
								public void setTaskName(String name) {
									// TODO Auto-generated method stub

								}

								@Override
								public void setCanceled(boolean value) {
									// TODO Auto-generated method stub

								}

								@Override
								public boolean isCanceled() {
									// TODO Auto-generated method stub
									return false;
								}

								@Override
								public void internalWorked(double work) {
									// TODO Auto-generated method stub

								}

								@Override
								public void done() {
									// TODO Auto-generated method stub

								}

								@Override
								public void beginTask(String name, int totalWork) {
									// TODO Auto-generated method stub

								}
							});
				} catch (final MigrationException e) {
					throw new RuntimeException(e);
				}
		}
		alreadyMigrating.set(false);
	}

	/**
	 * Create a resource for the given URI, possibly migrating it to the latest
	 * model version.
	 */
	@Override
	public Resource createResource(final URI uri) {
		migrate(uri);
		return delegate.createResource(uri);
	}
}
