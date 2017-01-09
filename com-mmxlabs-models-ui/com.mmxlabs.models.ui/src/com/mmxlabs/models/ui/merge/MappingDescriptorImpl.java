/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.merge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Default implementation of {@link IMappingDescriptor}
 * 
 * @author Simon Goodall
 * 
 */
class MappingDescriptorImpl implements IMappingDescriptor {
	private final Map<EObject, EObject> destinationToSourceMap;
	private final List<EObject> objectsAdded;
	private final Collection<EObject> objectsRemoved;
	private final EObject sourceContainer;
	private final EObject desinationContainer;
	private final EReference ref;

	public MappingDescriptorImpl(final EObject sourceContainer, final EObject desinationContainer, final EReference ref, final Map<EObject, EObject> destinationToSourceMap,
			final List<EObject> objectsAdded, final Collection<EObject> objectsRemoved) {
		this.sourceContainer = sourceContainer;
		this.desinationContainer = desinationContainer;
		this.ref = ref;
		this.destinationToSourceMap = destinationToSourceMap;
		this.objectsAdded = objectsAdded;
		this.objectsRemoved = objectsRemoved;
	}

	@Override
	public Map<EObject, EObject> getDestinationToSourceMap() {
		return destinationToSourceMap;
	}

	@Override
	public List<EObject> getAddedObjects() {
		return objectsAdded;
	}

	@Override
	public Collection<EObject> getRemovedObjects() {
		return objectsRemoved;
	}

	@Override
	public EReference getReference() {
		return ref;
	}

	@Override
	public EObject getDestinationContainer() {
		return desinationContainer;
	}

	@Override
	public EObject getSourceContainer() {
		return sourceContainer;
	}
}