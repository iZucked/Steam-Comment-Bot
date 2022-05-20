/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.presentation.views.ADPEditorData;

@NonNullByDefault
public interface IInventoryBasedGenerationSolver {
	Command runInventoryBasedGeneration(final ADPEditorData editorData, final MullProfile eMullProfile);
}
