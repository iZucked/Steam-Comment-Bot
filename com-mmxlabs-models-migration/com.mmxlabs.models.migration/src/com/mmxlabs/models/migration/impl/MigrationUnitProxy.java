/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionPoint;

/**
 * A proxy class to wrap around a {@link IMigrationUnit} and laxy instantiate the class when required.
 * 
 * @author Simon Goodall
 * 
 */
class MigrationUnitProxy implements IMigrationUnit {
	private final MigrationUnitExtensionPoint ext;
	private IMigrationUnit unit;

	private final int sourceVersion;
	private final int destinationVersion;

	public MigrationUnitProxy(@NonNull final MigrationUnitExtensionPoint ext) {
		this.ext = ext;
		sourceVersion = Integer.parseInt(ext.getFrom());
		destinationVersion = Integer.parseInt(ext.getTo());
	}

	@Override
	public String getContext() {
		return ext.getContext();
	}

	@Override
	public int getSourceVersion() {
		return sourceVersion;
	}

	@Override
	public int getDestinationVersion() {
		return destinationVersion;
	}

	@Override
	public void migrate(final @NonNull List<URI> uris, final @NonNull URIConverter uc, @Nullable final Map<String, URI> extraPackages) throws Exception {
		if (unit == null) {
			unit = ext.createMigrationUnit();
		}
		unit.migrate(uris, uc, extraPackages);
	}

}
