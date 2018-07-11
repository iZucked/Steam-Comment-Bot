package com.mmxlabs.models.ui.validation;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

@FunctionalInterface
public interface IValidationRootObjectTransformerService {

	Collection<? extends EObject> getTargets(EObject rootObject);
}
