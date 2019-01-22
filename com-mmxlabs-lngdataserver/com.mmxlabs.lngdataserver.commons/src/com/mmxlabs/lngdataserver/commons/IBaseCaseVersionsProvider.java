package com.mmxlabs.lngdataserver.commons;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import org.eclipse.jdt.annotation.Nullable;

public interface IBaseCaseVersionsProvider {

	@FunctionalInterface
	interface IBaseCaseChanged {
		void changed();
	}

	@Nullable
	String getPricingVersion();

	@Nullable
	ScenarioInstance getBaseCase();

	void addChangedListener(IBaseCaseChanged listener);

	void removeChangedListener(IBaseCaseChanged listener);
}
