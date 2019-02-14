/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.mmxlabs.lngdataserver.commons.IBaseCaseVersionsProvider;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.manifest.ModelArtifact;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class BaseCaseVersionsProviderService implements IBaseCaseVersionsProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCaseVersionsProviderService.class);
	private String pricingVersion;
	private String portsVersion;
	private String distancesVersion;
	private String fleetVersion;

	private final Set<IBaseCaseChanged> changeListeners = Sets.newConcurrentHashSet();
	private @Nullable ScenarioInstance baseCase;

	@Override
	public String getPricingVersion() {
		return pricingVersion;
	}

	@Override
	public String getPortsVersion() {
		return portsVersion;
	}

	@Override
	public String getDistancesVersion() {
		return distancesVersion;
	}

	@Override
	public String getFleetVersion() {
		return fleetVersion;
	}

	@Override
	public ScenarioInstance getBaseCase() {
		return baseCase;
	}

	@Override
	public void addChangedListener(final IBaseCaseChanged listener) {
		changeListeners.add(listener);
	}

	@Override
	public void removeChangedListener(final IBaseCaseChanged listener) {
		changeListeners.remove(listener);
	}

	public void setBaseCase(ScenarioInstance instance) {

		// Reset
		this.pricingVersion = null;
		this.portsVersion = null;
		this.distancesVersion = null;
		this.fleetVersion = null;

		this.baseCase = instance;
		if (this.baseCase != null) {
			ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(baseCase);
			Manifest mf = modelRecord.getManifest();

			this.pricingVersion = getVersion(mf, LNGScenarioSharedModelTypes.MARKET_CURVES.getID());
			this.distancesVersion = getVersion(mf, LNGScenarioSharedModelTypes.DISTANCES.getID());
			this.portsVersion = getVersion(mf, LNGScenarioSharedModelTypes.LOCATIONS.getID());
			this.fleetVersion = getVersion(mf, LNGScenarioSharedModelTypes.FLEET.getID());
		}

		try {
			changeListeners.forEach(IBaseCaseChanged::changed);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private String getVersion(Manifest mf, String type) {
		if (mf != null) {
			Optional<@NonNull ModelArtifact> artifact = mf.getModelDependencies().stream() //
					.filter(d -> Objects.equals(type, d.getKey())) //
					.findFirst();
			if (artifact.isPresent()) {
				return artifact.get().getDataVersion();
			}
		}
		return null;
	}
}
