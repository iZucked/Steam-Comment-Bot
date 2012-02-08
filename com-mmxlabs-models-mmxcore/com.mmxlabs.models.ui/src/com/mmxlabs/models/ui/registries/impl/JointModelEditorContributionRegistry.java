package com.mmxlabs.models.ui.registries.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.extensions.IJointModelEditorExtension;
import com.mmxlabs.models.ui.registries.IJointModelEditorContributionRegistry;

public class JointModelEditorContributionRegistry implements IJointModelEditorContributionRegistry {
	@Inject Iterable<IJointModelEditorExtension> extensions;
	
	@Override
	public IJointModelEditorContribution createEditorContribution(final EClass subModelClass) {
		for (final IJointModelEditorExtension extension : extensions) {
			if (extension.getSubModelClassName().equals(subModelClass.getInstanceClass().getCanonicalName())) {
				return extension.instantiate();
			}
		}
		return null;
	}
}
