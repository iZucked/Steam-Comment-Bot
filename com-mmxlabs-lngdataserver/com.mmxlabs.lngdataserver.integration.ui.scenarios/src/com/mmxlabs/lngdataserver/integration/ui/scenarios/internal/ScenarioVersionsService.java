/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;
import com.mmxlabs.scenario.service.ui.IScenarioVersionService;

public class ScenarioVersionsService implements IScenarioVersionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioVersionsService.class);

	private final Set<IChangedListener> changeListeners = Sets.newConcurrentHashSet();

	private final IBaseCaseVersionsProvider baseCaseVersionsProvider;

	private static final Collection<String> getBaseCaseTypesToCheck() {
		final List<String> types = new LinkedList<>();
		types.add(LNGScenarioSharedModelTypes.MARKET_CURVES.getID());
		if (LicenseFeatures.isPermitted("features:hub-sync-distances")) {
			types.add(LNGScenarioSharedModelTypes.DISTANCES.getID());
			types.add(LNGScenarioSharedModelTypes.LOCATIONS.getID());
			types.add(LNGScenarioSharedModelTypes.PORT_GROUPS.getID());
		}
		if (LicenseFeatures.isPermitted("features:hub-sync-vessels")) {
			types.add(LNGScenarioSharedModelTypes.BUNKER_FUELS.getID());
			types.add(LNGScenarioSharedModelTypes.FLEET.getID());
			types.add(LNGScenarioSharedModelTypes.VESSEL_GROUPS.getID());
		}
		return types;
	}

	public ScenarioVersionsService(final IBaseCaseVersionsProvider baseCaseVersionsProvider) {
		this.baseCaseVersionsProvider = baseCaseVersionsProvider;
		baseCaseVersionsProvider.addChangedListener(this::fireChangedListeners);
	}

	@Override
	public void addChangedListener(final IChangedListener listener) {
		changeListeners.add(listener);
	}

	@Override
	public void removeChangedListener(final IChangedListener listener) {
		changeListeners.remove(listener);
	}

	@Override
	public boolean differentToBaseCase(final ScenarioInstance instance) {
		if (instance == null) {
			return false;
		}
		final Manifest mf = instance.getManifest();
		if (mf != null) {
			return needUpdateToBase(mf, getBaseCaseTypesToCheck());
		}
		return false;
	}

	private boolean needUpdateToBase(final Manifest mf, final Collection<String> typesToCheck) {

		final Map<String, String> dataVersions = ScenarioStorageUtil.extractScenarioDataVersions(mf);
		if (dataVersions == null) {
			return false;
		}
		for (final String typeId : typesToCheck) {
			final String versionId = baseCaseVersionsProvider.getVersion(typeId);
			if (versionId == null) {
				continue;
			}
			if (!Objects.equals(versionId, dataVersions.get(typeId))) {
				return true;
			}
		}
		return false;
	}

	private void fireChangedListeners() {
		for (final IChangedListener l : changeListeners) {
			try {
				l.changed();
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
