/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ManifestFactory;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;

@NonNullByDefault
public class ModelsLNGVersionMaker {

	public static Manifest createDefaultManifest() {
		return ServiceHelper.withOptionalService(IMigrationRegistry.class, (registry) -> {
			if (registry != null && registry.getDefaultMigrationContext() != null) {
				final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
				manifest.setUUID(EcoreUtil.generateUUID());
				manifest.setVersionContext(registry.getDefaultMigrationContext());
				manifest.setScenarioVersion(registry.getLatestContextVersion(manifest.getVersionContext()));

				@Nullable
				final String clientContext = registry.getDefaultClientMigrationContext();
				manifest.setClientVersionContext(clientContext);
				if (clientContext != null) {
					manifest.setClientScenarioVersion(registry.getLatestClientContextVersion(clientContext));
				}

				manifest.setScenarioType(ScenarioStorageUtil.DEFAULT_CONTENT_TYPE);
				return manifest;
			} else {
				// We should only get here from JUnit tests..
				final Manifest manifest = ManifestFactory.eINSTANCE.createManifest();
				manifest.setVersionContext("scenario");
				manifest.setScenarioVersion(1);

				manifest.setClientVersionContext("client");
				manifest.setClientScenarioVersion(1);

				manifest.setScenarioType(ScenarioStorageUtil.DEFAULT_CONTENT_TYPE);
				return manifest;
			}
		});
	}
}
