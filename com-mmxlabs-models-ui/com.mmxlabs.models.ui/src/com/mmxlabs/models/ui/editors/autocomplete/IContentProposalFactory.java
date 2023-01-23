/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.autocomplete;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.Nullable;

public interface IContentProposalFactory {

	@Nullable
	IMMXContentProposalProvider create(@Nullable ETypedElement feature);

	@Nullable
	IMMXContentProposalProvider create(@Nullable String type);
}
