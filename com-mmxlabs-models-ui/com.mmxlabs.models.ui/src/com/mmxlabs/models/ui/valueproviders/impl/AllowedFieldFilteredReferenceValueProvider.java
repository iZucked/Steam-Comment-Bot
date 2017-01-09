/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.valueproviders.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 */
public abstract class AllowedFieldFilteredReferenceValueProvider<FieldType> extends
		FilteredReferenceValueProvider {

	public AllowedFieldFilteredReferenceValueProvider(
			IReferenceValueProvider delegate) {
		super(delegate);
	}

	@Override
	protected boolean isAllowedValue(EObject target, EStructuralFeature field,
			Pair<String, EObject> value) {

		// get the list of allowed values in the field
		EList<FieldType> allowed = getAllowedValuesFromField(target, field);
		
		// typically, if the allowed list is empty, this actually means "allow everything"
		if (allowed.isEmpty()) {
			return true;
		}
		
		FieldType queryValue = (FieldType) value.getSecond();
		
		// allow a blank value
		if (queryValue == null) {
			return true;
		}
		
		// if a value is currently set on this object, permit it as a value
		// even if it is prohibited by the "allowed values" list
		if (queryValue == getCurrentValue(target, field)) {
			return true;
		}

		for (FieldType fieldValue: allowed) {
			// permit a value which is explicitly in the allowed values list
			if (Equality.isEqual(fieldValue, queryValue)) {
				return true;
			}
			// permit a value which is included in a selection from the allowed values list
			// e.g. a vessel class
			if (fieldValueIncludesObject(fieldValue, queryValue)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	protected abstract EList<FieldType> getAllowedValuesFromField(
			EObject target, EStructuralFeature field);

	protected abstract FieldType getCurrentValue(EObject target, EStructuralFeature field);

	protected abstract boolean fieldValueIncludesObject(FieldType fieldValue, FieldType queryValue);
}
