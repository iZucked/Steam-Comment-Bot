/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.scenario.IDataComponentProvider;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * @since 2.0
 */
public interface IShortCargoReturnElementProvider extends IDataComponentProvider {
	public ISequenceElement getReturnElement(ILoadOption loadOption);

	public ISequenceElement getReturnElement(ISequenceElement loadElement);
}
