/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Locked elements DCP which tells you which elements are locked in a sequence
 * 
 * @author achurchill
 * 
 */
public interface ILockedElementsProvider extends IDataComponentProvider {
	/**
	 * Returns true if the given element is allowed to be moved in a sequence.
	 * 
	 * 
	 * @param element
	 * @return
	 */
	boolean isElementLocked(ISequenceElement element);

}
