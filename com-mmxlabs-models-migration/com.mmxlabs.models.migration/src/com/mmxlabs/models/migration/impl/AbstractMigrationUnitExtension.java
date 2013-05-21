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

/**
 * Abstract class handling the wrapping calls to a {@link IMigrationUnit}. The {@link #migrate(java.util.List, org.eclipse.emf.ecore.resource.URIConverter, java.util.Map)} still needs to be
 * implemented.
 * 
 * @author Simon Goodall
 * @since 3.0
 * 
 */
public abstract class AbstractMigrationUnitExtension implements IMigrationUnitExtension {
	private IMigrationUnit migrationUnit;

	public AbstractMigrationUnitExtension(@Nullable final IMigrationUnit migrationUnit) {
		this.migrationUnit = migrationUnit;
	}

	public AbstractMigrationUnitExtension() {
	}

	@Override
	public abstract void migrate(@NonNull List<URI> uris, @NonNull URIConverter uc, @Nullable Map<String, URI> extraPackages) throws Exception;

	@Override
	public IMigrationUnit getMigrationUnit() {
		return migrationUnit;
	}

	@Override
	public void setMigrationUnit(final IMigrationUnit migrationUnit) {
		this.migrationUnit = migrationUnit;
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
