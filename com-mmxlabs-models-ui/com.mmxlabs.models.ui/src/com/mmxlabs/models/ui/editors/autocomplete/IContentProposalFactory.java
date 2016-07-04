/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.autocomplete;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;

public interface IContentProposalFactory {

	@Nullable
	IMMXContentProposalProvider create(@Nullable EStructuralFeature feature);
}
