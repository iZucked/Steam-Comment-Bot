/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * A default extra validation context instance; allows overriding of container and containment, and can be stacked on top of other validation contexts.
 * 
 * @author hinton
 * 
 */
public class DefaultExtraValidationContext implements IExtraValidationContext {
	private final MMXRootObject rootObject;
	private IExtraValidationContext outerContext;

	private HashMap<EObject, EObject> containerOverrides = new HashMap<EObject, EObject>();
	private HashMap<EObject, EReference> containmentOverrides = new HashMap<EObject, EReference>();

	private HashMap<Pair<EObject, EReference>, List<EObject>> extraContainedObjects = new HashMap<Pair<EObject, EReference>, List<EObject>>();

	private HashMap<Pair<EObject, EReference>, List<EObject>> ignoredContainedObjects = new HashMap<Pair<EObject, EReference>, List<EObject>>();
	private HashMap<EObject, EObject> replacements = new HashMap<EObject, EObject>();
	private HashMap<EObject, EObject> originals = new HashMap<EObject, EObject>();

	private final boolean validatingClone;

	/**
	 */
	public DefaultExtraValidationContext(final MMXRootObject rootObject, boolean validatingClone) {
		this.rootObject = rootObject;
		this.validatingClone = validatingClone;
	}

	/**
	 */
	public DefaultExtraValidationContext(final IExtraValidationContext outerContext, boolean validatingClone) {
		this.outerContext = outerContext;
		this.rootObject = outerContext.getRootObject();
		this.validatingClone = validatingClone;
	}

	@Override
	public MMXRootObject getRootObject() {
		return rootObject;
	}

	public void ignore(final EObject object) {
		final Pair<EObject, EReference> key = new Pair<EObject, EReference>(getContainer(object), getContainment(object));

		List<EObject> ignore = ignoredContainedObjects.get(key);
		if (ignore == null) {
			ignore = new ArrayList<EObject>();
			ignoredContainedObjects.put(key, ignore);
		}

		ignore.add(object);

		containerOverrides.put(object, null);
		containmentOverrides.put(object, null);
	}

	public void setApparentContainment(final EObject object, final EObject container, final EReference reference) {
		final Pair<EObject, EReference> key = new Pair<EObject, EReference>(container, reference);
		containerOverrides.put(object, container);
		containmentOverrides.put(object, reference);
		List<EObject> extra = extraContainedObjects.get(key);
		if (extra == null) {
			extra = new ArrayList<EObject>();
			extraContainedObjects.put(key, extra);
		}
		extra.add(object);
	}

	@Override
	public EObject getContainer(final EObject object) {
		if (containerOverrides.containsKey(object)) {
			return containerOverrides.get(object);
		} else if (outerContext != null) {
			return outerContext.getContainer(object);
		} else {
			return object.eContainer();
		}
	}

	@Override
	public EReference getContainment(EObject object) {
		if (containmentOverrides.containsKey(object)) {
			return containmentOverrides.get(object);
		} else if (outerContext != null) {
			return outerContext.getContainment(object);
		} else {
			return (EReference) object.eContainingFeature();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EObject> getContents(EObject container, EReference reference) {
		final List<EObject> result;
		if (outerContext != null) {
			result = outerContext.getContents(container, reference);

			final Iterator<EObject> iterator = result.iterator();
			while (iterator.hasNext()) {
				final EObject object = iterator.next();
				if (getContainer(object) != container || getContainment(object) != reference)
					iterator.remove();
			}
		} else {
			result = new LinkedList<EObject>((Collection<? extends EObject>) container.eGet(reference));
		}
		final Pair<EObject, EReference> key = new Pair<EObject, EReference>(container, reference);
		final List<EObject> insertions = extraContainedObjects.get(key);
		if (insertions != null) {
			result.addAll(insertions);
		}
		final List<EObject> deletions = ignoredContainedObjects.get(key);
		if (deletions != null) {
			result.removeAll(deletions);
		}

		return result;
	}

	/**
	 * replace one with two
	 * 
	 * @param one
	 * @param two
	 */
	public void replace(final EObject one, final EObject two) {
		setApparentContainment(two, getContainer(one), getContainment(one));
		ignore(one);
		replacements.put(one, two);
		originals.put(two, one);
	}

	@Override
	public List<EObject> getSiblings(final EObject object) {
		return getContents(getContainer(object), getContainment(object));
	}

	@Override
	public EObject getReplacement(final EObject object) {
		if (replacements.containsKey(object)) {
			return replacements.get(object);
		} else if (outerContext != null) {
			return outerContext.getReplacement(object);
		} else {
			return object;
		}
	}

	@Override
	public EObject getOriginal(final EObject object) {
		if (originals.containsKey(object)) {
			return originals.get(object);
		} else if (outerContext != null) {
			return outerContext.getOriginal(object);
		} else {
			return object;
		}
	}

	/**
	 */
	@Override
	public boolean isValidatingClone() {
		return validatingClone;
	}

}
