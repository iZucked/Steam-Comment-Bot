/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.portshipsizeconstraint;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;

/**
 * An editor interface for {@link IPortShipSizeProvider}
 */
@NonNullByDefault
public interface IPortShipSizeProviderEditor extends IPortShipSizeProvider {

	/**
	 * Add a vessel and sequence elements that are incompatible with respect to the port ship size constraint.
	 * @param vessel
	 * @param element
	 */
	void addIncompatibleResourceElements(IVessel vessel, Collection<ISequenceElement> elements);
}
