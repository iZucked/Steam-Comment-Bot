/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editorpart;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public interface IJointModelEditorContribution {
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject);
	public void addPages(Composite parent);
	public void setLocked(final boolean locked);
	public void dispose();
}
