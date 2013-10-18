/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection.providers;

import com.mmxlabs.optimiser.core.ISequenceElement;


public interface IOriginalDateProviderEditor extends IOriginalDataProvider {

	void setOriginalDate(ISequenceElement loadElement, int time);
}
