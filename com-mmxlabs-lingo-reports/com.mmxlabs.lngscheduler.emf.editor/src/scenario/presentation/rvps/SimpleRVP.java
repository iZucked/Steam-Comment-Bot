/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.rvps;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import scenario.ScenarioPackage;

import com.mmxlabs.common.Pair;

public abstract class SimpleRVP extends ScenarioRVP {
		private List<Pair<String, EObject>> cachedValues = null;
		private final EReference containingReference;

		public SimpleRVP(final EReference containingReference) {
			super(ScenarioPackage.eINSTANCE.getNamedObject_Name());
			this.containingReference = containingReference;
		}

		public SimpleRVP(final EReference containingReference,
				final EAttribute name) {
			super(name);
			this.containingReference = containingReference;
		}

		@Override
		public List<Pair<String, EObject>> getAllowedValues(EObject target,
				EStructuralFeature field) {
			if (cachedValues == null) {
				install();
				cacheValues();
			}
			return cachedValues;
		}

		protected abstract void install();

		@Override
		protected boolean isRelevantTarget(Object target, Object feature) {
			return (super.isRelevantTarget(target, feature) && (containingReference
					.getEReferenceType().isSuperTypeOf(((EObject) target)
					.eClass())))
					|| feature == containingReference;
		}

		@Override
		protected void cacheValues() {
			cachedValues = getSortedNames(getObjects(), nameAttribute);
			final Pair<String, EObject> none = getEmptyObject();
			if (none != null)
				cachedValues.add(0, none);
		}

		protected Pair<String, EObject> getEmptyObject() {
			return null;
		}

		protected abstract EList<? extends EObject> getObjects();
	}