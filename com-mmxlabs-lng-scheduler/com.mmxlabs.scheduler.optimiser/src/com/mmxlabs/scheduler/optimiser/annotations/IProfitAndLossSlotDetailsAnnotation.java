/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.IElementAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;

public interface IProfitAndLossSlotDetailsAnnotation extends IElementAnnotation {

	IPortSlot getPortSlot();

	/**
	 * @return an {@link IDetailTree} of details for this annotation
	 */
	IDetailTree getDetails();
}
