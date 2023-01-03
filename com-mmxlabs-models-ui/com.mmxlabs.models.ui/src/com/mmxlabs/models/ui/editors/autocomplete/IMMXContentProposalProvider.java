/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.autocomplete;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import com.mmxlabs.models.mmxcore.MMXRootObject;

public interface IMMXContentProposalProvider extends IContentProposalProvider {

	void setRootObject(MMXRootObject rootObject);
	
	void setInputOject(EObject eObject);

}
