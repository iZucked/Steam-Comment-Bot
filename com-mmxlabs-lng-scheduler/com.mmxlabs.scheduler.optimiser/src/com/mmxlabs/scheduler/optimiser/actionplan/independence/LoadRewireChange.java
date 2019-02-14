/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionplan.independence;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Rewire change
 * @author achurchill
 *
 */
public class LoadRewireChange extends RewireChange {
	
	public LoadRewireChange(String description, ISequenceElement loadA, ISequenceElement dischargeA, ISequenceElement loadB, ISequenceElement dischargeB, IResource resourceA, IResource resourceB) {
		super(description, loadA, dischargeA, loadB, dischargeB, resourceA, resourceB);
	}
	
	
}
