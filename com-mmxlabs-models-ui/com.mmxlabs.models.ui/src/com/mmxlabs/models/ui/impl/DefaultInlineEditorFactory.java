/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.BooleanInlineEditor;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;
import com.mmxlabs.models.ui.editors.impl.LocalDateInlineEditor;
import com.mmxlabs.models.ui.editors.impl.LocalDateTimeInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiEnumInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiReferenceInlineEditor;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.YearMonthInlineEditor;
import com.mmxlabs.models.ui.editors.impl.YesNoInlineEditor;

/**
 * The inline editor factory which creates the normal editors for:
 * 
 * Int
 * Double
 * Float
 * Long
 * Enumerators
 * References
 * Multi-references
 * Strings
 * 
 * Not currently dates, as they need some thought.
 * 
 * @author hinton
 *
 */
public class DefaultInlineEditorFactory implements IInlineEditorFactory {
	
	@Override
	public IInlineEditor createEditor(final EClass owner, final ETypedElement typedElement) {
		if (typedElement instanceof EReference reference) {
			final EClass referenceClass = reference.getEReferenceType();
			if (reference.isContainment()) return null; // containment references are weird, and ought to be being handled by another extension.
			if (reference.isMany()) {
				return new MultiReferenceInlineEditor(typedElement);
			} else {
				return new ReferenceInlineEditor(typedElement);
			}
		} else if (typedElement instanceof EAttribute attribute) {
			final EcorePackage ecore = EcorePackage.eINSTANCE;
			final EDataType attributeType = attribute.getEAttributeType();
			if (attributeType == ecore.getEInt() ||
				attributeType == ecore.getELong()||
				attributeType == ecore.getEDouble()||
				attributeType == ecore.getEFloat()) {
				return new NumberInlineEditor(typedElement);
			} else if (attributeType == ecore.getEBoolean()) {
				if (typedElement instanceof EStructuralFeature feature && feature.isUnsettable()) {
					/* 
					 * an unsettable boolean needs to be a "yes / no" list
					 * because the check box determines whether it is set or not
					 */
					return new YesNoInlineEditor(attribute);
				}
				else {
					/* regular boolean can just be a check box */
					return new BooleanInlineEditor(typedElement);
				}					
			} else if (attributeType == ecore.getEChar()) {
				return null;
			} else if (attributeType == ecore.getEByte()) {
				return null;
			} else if (attributeType == ecore.getEEnumerator()) {
				if (attribute.isMany()) {
					return new MultiEnumInlineEditor(typedElement);
				} else {
					return new EENumInlineEditor(attribute);
				}
			} else if (attributeType == ecore.getEString()) {
				return new TextInlineEditor(typedElement);
			} else if (attributeType == DateTimePackage.Literals.YEAR_MONTH) {
				return new YearMonthInlineEditor(typedElement);
			} else if (attributeType == DateTimePackage.Literals.DATE_TIME) {
//				return new DateTimeInlineEditor(feature);
			} else if (attributeType == DateTimePackage.Literals.LOCAL_DATE) {
				return new LocalDateInlineEditor(typedElement);
			} else if (attributeType == DateTimePackage.Literals.LOCAL_DATE_TIME) {
				return new LocalDateTimeInlineEditor(typedElement);
			}
		}
		return null;
	}
}
