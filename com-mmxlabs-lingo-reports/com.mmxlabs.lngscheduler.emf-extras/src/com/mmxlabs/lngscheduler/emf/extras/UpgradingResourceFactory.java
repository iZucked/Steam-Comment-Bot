/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras;

import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;

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
	final Factory delegate;

	/**
	 * Construct an UpgradingResourceFactory which delegates to the argument.
	 * 
	 * @param delegate the delegate which is really used for loading the resource.
	 */
	public UpgradingResourceFactory(final Factory delegate) {
		this.delegate = delegate;
	}

	/**
	 * Create a resource for the given URI, possibly migrating it to the latest
	 * model version
	 */
	@Override
	public Resource createResource(final URI uri) {
		final String namespace = ReleaseUtil.getNamespaceURI(uri);
		final Migrator migrator = MigratorRegistry.getInstance().getMigrator(
				namespace);

		if (migrator != null) {
			int r = migrator.getRelease(uri).iterator().next();
			try {
				migrator.migrateAndSave(Collections.singletonList(uri), r,
						Integer.MAX_VALUE, null);
			} catch (final MigrationException e) {
				throw new RuntimeException(e);
			}
		}

		return delegate.createResource(uri);
	}
}
