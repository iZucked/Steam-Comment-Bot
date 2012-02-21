package com.mmxlabs.models.ui.registries.impl;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.extensions.IJointModelEditorExtension;
import com.mmxlabs.models.ui.registries.IJointModelEditorContributionRegistry;

public class JointModelEditorContributionRegistry implements IJointModelEditorContributionRegistry {
	private static final Logger log = LoggerFactory.getLogger(JointModelEditorContributionRegistry.class);
	@Inject Iterable<IJointModelEditorExtension> extensions;
	
	@Override
	public IJointModelEditorContribution createEditorContribution(final EClass subModelClass) {
		log.debug("find extensions for " + subModelClass.getInstanceClass().getCanonicalName());
		for (final IJointModelEditorExtension extension : extensions) {
			log.debug(extension.getID() + ":" + extension.getSubModelClassName());
			if (extension.getSubModelClassName().equals(subModelClass.getInstanceClass().getCanonicalName())) {
				log.debug("returning " + extension.getID());
				return extension.instantiate();
			}
		}
		return null;
	}
}
