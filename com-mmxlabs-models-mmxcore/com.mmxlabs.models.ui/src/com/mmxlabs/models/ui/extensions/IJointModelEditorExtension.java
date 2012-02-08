package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;

@ExtensionBean("com.mmxlabs.models.ui.jointmodeleditors")
public interface IJointModelEditorExtension {
	@MapName("id") String getID();
	@MapName("subModelClass") String getSubModelClassName();
	@MapName("factory") IJointModelEditorContribution instantiate();
}
