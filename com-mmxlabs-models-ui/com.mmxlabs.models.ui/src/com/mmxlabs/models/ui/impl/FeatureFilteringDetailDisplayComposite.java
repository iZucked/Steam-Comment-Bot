/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.ui.editors.IInlineEditor;

/**
 * Implementation of {@link DefaultDetailComposite} with extra API to filter in or out certain features. Useful for custom layouts with an object's feature split across multiple composites.
 * 
 * @author Simon Goodall
 *
 */
public class FeatureFilteringDetailDisplayComposite extends DefaultDetailComposite {

	private Set<EStructuralFeature> featuresToInclude = new HashSet<>();
	private Set<EStructuralFeature> featuresToExclude = new HashSet<>();

	public FeatureFilteringDetailDisplayComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	/**
	 * Include a particular feature. Implies anything not included will be excluded.
	 * 
	 * @param feature
	 */
	public void includeFeature(EStructuralFeature feature) {
		featuresToInclude.add(feature);
	}

	/**
	 * Exclude a particular feature. Implies anything not excluded will be included.
	 * 
	 * @param feature
	 */
	public void excludeFeature(EStructuralFeature feature) {
		featuresToExclude.add(feature);
	}

	@Override
	public IInlineEditor addInlineEditor(@Nullable IInlineEditor editor) {
		if (!featuresToInclude.isEmpty()) {
			if (editor != null && featuresToInclude.contains(editor.getFeature())) {
				return super.addInlineEditor(editor);
			}
			return null;
		}
		if (!featuresToExclude.isEmpty()) {
			if (editor != null && featuresToExclude.contains(editor.getFeature())) {
				return null;
			}
		}
		return super.addInlineEditor(editor);
	}

}
