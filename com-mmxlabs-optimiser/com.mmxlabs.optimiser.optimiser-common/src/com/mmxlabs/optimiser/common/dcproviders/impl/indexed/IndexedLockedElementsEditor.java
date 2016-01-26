/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.IIndexBits;
import com.mmxlabs.common.indexedobjects.impl.ArrayIndexBits;
import com.mmxlabs.optimiser.common.dcproviders.ILockedElementsProviderEditor;
import com.mmxlabs.optimiser.core.ISequenceElement;

/**
 * Locked elements editor for indexed elements.
 * 
 * @author achurchill
 * 
 */
public class IndexedLockedElementsEditor implements ILockedElementsProviderEditor {

	private final IIndexBits<ISequenceElement> lockedElements = new ArrayIndexBits<ISequenceElement>();

	@Override
	public boolean isElementLocked(final ISequenceElement element) {
		return lockedElements.isSet(element);
	}

	@Override
	public void setLocked(final ISequenceElement element, final boolean isLocked) {
		if (isLocked) {
			lockedElements.set(element);
		} else {
			lockedElements.clear(element);
		}
	}

}
