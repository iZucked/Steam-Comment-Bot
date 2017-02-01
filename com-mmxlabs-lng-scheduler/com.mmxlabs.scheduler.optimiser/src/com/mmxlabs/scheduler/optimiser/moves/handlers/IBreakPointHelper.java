package com.mmxlabs.scheduler.optimiser.moves.handlers;

import java.util.ArrayList;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public interface IBreakPointHelper {

	ArrayList<Pair<ISequenceElement, ISequenceElement>> getValidBreaks();

	void init(IOptimisationData data);

}