package com.mmxlabs.models.ui.editorpart;

import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

public interface IJointModelEditorContribution {
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject);
	public void addPages(Composite parent);
}
