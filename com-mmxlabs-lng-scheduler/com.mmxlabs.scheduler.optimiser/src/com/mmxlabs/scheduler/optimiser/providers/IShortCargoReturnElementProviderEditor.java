/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 */
public interface IShortCargoReturnElementProviderEditor extends IShortCargoReturnElementProvider {
	public void setReturnElement(ISequenceElement loadElement, ILoadOption loadOption, ISequenceElement returnElement);
}
