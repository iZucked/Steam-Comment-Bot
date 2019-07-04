/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * A {@link IDataComponentProvider} which stores a set of {@link ISequenceElement} and {@link IResource} pairs which are compatible.
 */
@NonNullByDefault
public interface IPortShipSizeProvider extends IDataComponentProvider {

	/**
	 * Checks whether a given sequence element is compatible with a given vessel or not.
	 * @param vessel
	 * @param sequenceElement
	 * @return true, if compatible, false, otherwise.
	 */
	boolean isCompatible(IVessel vessel, ISequenceElement sequenceElement);
}
