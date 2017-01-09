/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.autocomplete;

import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IMMXContentProposalProvider extends IContentProposalProvider {

	void setRootObject(MMXRootObject rootObject);

}
