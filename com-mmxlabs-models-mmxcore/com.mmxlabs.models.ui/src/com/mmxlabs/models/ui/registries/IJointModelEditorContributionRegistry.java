package com.mmxlabs.models.ui.registries;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;

public interface IJointModelEditorContributionRegistry {
	public IJointModelEditorContribution createEditorContribution(final EClass subModelClass);
}
