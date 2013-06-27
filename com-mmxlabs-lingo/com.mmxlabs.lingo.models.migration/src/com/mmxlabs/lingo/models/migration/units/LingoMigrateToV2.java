/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.models.migration.units;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.models.migration.LingoMigrationConstants;
import com.mmxlabs.lingo.models.migration.internal.MigratorInjectionModule;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;

/**
 * Migration Unit delegating to Models LNG migration
 * 
 * @author Simon Goodall
 * 
 */
public class LingoMigrateToV2 implements IMigrationUnit {

	/**
	 * See {@link MigratorInjectionModule}. Ensure class name is prefixed with "org.ops4j.peaberry.eclipse.GuiceExtensionFactory:" to enable dependency injection via plugin.xml
	 */
	@Inject
	private IMigrationRegistry registry;

	@Override
	public String getContext() {
		return LingoMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 1;
	}

	@Override
	public int getDestinationVersion() {
		return 2;
	}

	@Override
	public void migrate(@NonNull final URI baseURI, @Nullable final Map<URI, PackageData> extraPackagesOrig) throws Exception {

		final List<IMigrationUnit> chain = registry.getMigrationChain(ModelsLNGMigrationConstants.Context, 1, 2);
		for (final IMigrationUnit unit : chain) {
			unit.migrate(baseURI, null);
		}
	}

}
