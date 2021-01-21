/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;

/**
 * Locked elements DCP which tells you which elements are locked in a sequence
 * 
 * @author achurchill
 * 
 */
@NonNullByDefault
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
