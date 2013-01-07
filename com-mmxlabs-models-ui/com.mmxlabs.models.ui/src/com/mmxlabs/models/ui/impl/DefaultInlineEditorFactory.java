/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;

import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.impl.BooleanInlineEditor;
import com.mmxlabs.models.ui.editors.impl.EENumInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiEnumInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiReferenceInlineEditor;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;
import com.mmxlabs.models.ui.editors.impl.ReferenceInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;

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
	public IInlineEditor createEditor(final EClass owner, final EStructuralFeature feature) {
		if (feature instanceof EReference) {
			final EReference reference = (EReference) feature;
			final EClass referenceClass = reference.getEReferenceType();
			if (reference.isContainment()) return null; // containment references are weird, and ought to be being handled by another extension.
			if (reference.isMany()) {
				return new MultiReferenceInlineEditor(feature);
			} else {
				return new ReferenceInlineEditor(feature);
			}
		} else if (feature instanceof EAttribute) {
			final EcorePackage ecore = EcorePackage.eINSTANCE;
			final EAttribute attribute = (EAttribute) feature;
			final EDataType attributeType = attribute.getEAttributeType();
			if (attributeType == ecore.getEInt() ||
				attributeType == ecore.getELong()||
				attributeType == ecore.getEDouble()||
				attributeType == ecore.getEFloat()) {
				return new NumberInlineEditor(feature);
			} else if (attributeType == ecore.getEBoolean()) {
				return new BooleanInlineEditor(feature);
			} else if (attributeType == ecore.getEChar()) {
				return null;
			} else if (attributeType == ecore.getEByte()) {
				return null;
			} else if (attributeType == ecore.getEEnumerator()) {
				if (attribute.isMany()) {
					return new MultiEnumInlineEditor(feature);
				} else {
					return new EENumInlineEditor(attribute);
				}
			} else if (attributeType == ecore.getEString()) {
				return new TextInlineEditor(feature);
			}
		}
		return null;
	}
}
