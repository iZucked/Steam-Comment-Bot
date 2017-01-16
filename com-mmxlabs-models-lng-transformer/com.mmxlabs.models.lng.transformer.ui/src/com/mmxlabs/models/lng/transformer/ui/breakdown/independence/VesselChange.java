/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown.independence;

import java.io.Serializable;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Rewire change
 * @author achurchill
 *
 */
public class VesselChange extends RewireChange implements Serializable {
	@NonNull
	transient ISequenceElement loadA;
	@NonNull
	transient ISequenceElement dischargeA;
	
	public VesselChange(String description, ISequenceElement loadA, ISequenceElement dischargeA, ISequenceElement loadB, ISequenceElement dischargeB, IResource resourceA, IResource resourceB) {
		super(description, loadA, dischargeA, loadB, dischargeB, resourceA, resourceB);
		this.loadA = loadA;
		this.dischargeA = dischargeA;
	}
	
	
}
