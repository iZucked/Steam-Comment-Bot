package com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;

@NonNullByDefault
public interface IMullContainerBuildingStrategy {
	public IMullContainer build(final GlobalStatesContainer globalStatesContainer, final AlgorithmState algorithmState);
}
