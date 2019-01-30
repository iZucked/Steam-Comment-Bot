package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.scenario.service.manifest.Manifest;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioStorageUtil;
import com.mmxlabs.scenario.service.ui.IBaseCaseVersionsProvider;
import com.mmxlabs.scenario.service.ui.IScenarioVersionService;

public class ScenarioVersionsService implements IScenarioVersionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioVersionsService.class);

	private final Set<IChangedListener> changeListeners = Sets.newConcurrentHashSet();

	private IBaseCaseVersionsProvider baseCaseVersionsProvider;

	private static final Collection<String> BASE_CASE_TYPES_TO_CHECK = Lists.newArrayList( //
			LNGScenarioSharedModelTypes.MARKET_CURVES.getID() //
	);

	public ScenarioVersionsService(IBaseCaseVersionsProvider baseCaseVersionsProvider) {
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
	public boolean differentToBaseCase(ScenarioInstance instance) {
		if (instance == null) {
			return false;
		}
		final Manifest mf = instance.getManifest();
		if (mf != null) {
			return needUpdateToBase(mf, BASE_CASE_TYPES_TO_CHECK);
		}
		return false;
	}

	private boolean needUpdateToBase(final Manifest mf, Collection<String> typesToCheck) {

		Map<String, String> dataVersions = ScenarioStorageUtil.extractScenarioDataVersions(mf);
		if (dataVersions == null) {
			return false;
		}
		for (String typeId : typesToCheck) {
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
		for (IChangedListener l : changeListeners) {
			try {
				l.changed();
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
