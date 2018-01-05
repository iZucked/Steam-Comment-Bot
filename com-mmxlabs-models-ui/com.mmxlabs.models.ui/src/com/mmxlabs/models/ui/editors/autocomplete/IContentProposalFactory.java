/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.autocomplete;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;

public interface IContentProposalFactory {

	@Nullable
	IMMXContentProposalProvider create(@Nullable EStructuralFeature feature);

	@Nullable
	IMMXContentProposalProvider create(@Nullable String type);
}
