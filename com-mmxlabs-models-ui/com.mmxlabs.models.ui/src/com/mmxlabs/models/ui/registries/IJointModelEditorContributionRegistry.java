/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.registries;

import java.util.List;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public interface IJointModelEditorContributionRegistry {
	public List<IJointModelEditorContribution> initEditorContributions(final JointModelEditorPart part, final MMXRootObject root);
}
