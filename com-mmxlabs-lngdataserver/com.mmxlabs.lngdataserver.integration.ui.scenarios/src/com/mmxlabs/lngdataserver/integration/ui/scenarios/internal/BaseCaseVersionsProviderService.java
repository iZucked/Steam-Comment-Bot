/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;

public class BaseCaseVersionsProviderService implements IBaseCaseVersionsProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCaseVersionsProviderService.class);
	private Map<String, String> dataVersions = Collections.emptyMap();

	private final Set<IBaseCaseChanged> changeListeners = Sets.newConcurrentHashSet();
	private @Nullable ScenarioInstance baseCase;
	private @Nullable String lockedBy;

	@Override
	public String getVersion(final String typeId) {
		return dataVersions.get(typeId);
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

	public void setBaseCase(final ScenarioInstance instance) {

		// Reset
		this.dataVersions = Collections.emptyMap();

		this.baseCase = instance;
		if (this.baseCase != null) {
			final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(baseCase);
			final Manifest mf = modelRecord.getManifest();
			this.dataVersions = ScenarioStorageUtil.extractScenarioDataVersions(mf);
		}

		try {
			changeListeners.forEach(IBaseCaseChanged::changed);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void setLockedBy(final String lockedBy) {
		this.lockedBy = lockedBy;
		try {
			changeListeners.forEach(IBaseCaseChanged::changed);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public @Nullable String getLockedBy() {
		return lockedBy;
	}
}
