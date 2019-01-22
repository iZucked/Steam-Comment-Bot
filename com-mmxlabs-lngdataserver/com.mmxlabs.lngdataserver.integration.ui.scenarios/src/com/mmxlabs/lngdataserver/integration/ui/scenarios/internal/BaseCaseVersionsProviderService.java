package com.mmxlabs.lngdataserver.integration.ui.scenarios.internal;

import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.mmxlabs.lngdataserver.commons.IBaseCaseVersionsProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class BaseCaseVersionsProviderService implements IBaseCaseVersionsProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseCaseVersionsProviderService.class);
	private String pricingVersion;

	private final Set<IBaseCaseChanged> changeListeners = Sets.newConcurrentHashSet();
	private @Nullable ScenarioInstance baseCase;

	@Override
	public String getPricingVersion() {
		return pricingVersion;
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

	public void setBaseCase(ScenarioInstance instance, String pricingVersion) {
		this.baseCase = instance;
		this.pricingVersion = pricingVersion;
		try {
			changeListeners.forEach(IBaseCaseChanged::changed);
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
}
