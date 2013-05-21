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
import com.mmxlabs.models.migration.IMigrationUnitExtension;
import com.mmxlabs.models.migration.extensions.MigrationUnitExtensionExtensionPoint;

/**
 * A proxy class to wrap around a {@link IMigrationUnitExtension} and laxy instantiate the class when required.
 * 
 * @author Simon Goodall
 * 
 */
class MigrationUnitExtensionProxy implements IMigrationUnitExtension {
	private final MigrationUnitExtensionExtensionPoint ext;
	private IMigrationUnitExtension unitExtension;
	private IMigrationUnit migrationUnit;

	public MigrationUnitExtensionProxy(@NonNull final MigrationUnitExtensionExtensionPoint ext) {
		this.ext = ext;
	}

	@Override
	public void migrate(final @NonNull List<URI> uris, final @NonNull URIConverter uc, @Nullable final Map<String, URI> extraPackages) throws Exception {
		if (unitExtension == null) {
			unitExtension = ext.createMigrationUnitExtension();
			unitExtension.setMigrationUnit(migrationUnit);
		}
		unitExtension.migrate(uris, uc, extraPackages);
	}

	@Override
	public IMigrationUnit getMigrationUnit() {
		return migrationUnit;
	}

	@Override
	public void setMigrationUnit(final IMigrationUnit migrationUnit) {
		this.migrationUnit = migrationUnit;
		if (unitExtension != null) {
			unitExtension.setMigrationUnit(migrationUnit);
		}
	}

	@Override
	public String getContext() {
		return migrationUnit.getContext();
	}

	@Override
	public int getSourceVersion() {
		return migrationUnit.getSourceVersion();
	}

	@Override
	public int getDestinationVersion() {
		return migrationUnit.getDestinationVersion();
	}
}
