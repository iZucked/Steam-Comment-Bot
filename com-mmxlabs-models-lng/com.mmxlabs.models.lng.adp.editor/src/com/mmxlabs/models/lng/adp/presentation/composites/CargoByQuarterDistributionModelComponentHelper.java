/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for CargoByQuarterDistributionModel instances
 *
 * @generated
 */
public class CargoByQuarterDistributionModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CargoByQuarterDistributionModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CargoByQuarterDistributionModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(ADPPackage.Literals.DISTRIBUTION_MODEL));
	}
	
	/**
	 * add editors to a composite, using CargoByQuarterDistributionModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ADPPackage.Literals.CARGO_BY_QUARTER_DISTRIBUTION_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_q1Editor(detailComposite, topClass);
		add_q2Editor(detailComposite, topClass);
		add_q3Editor(detailComposite, topClass);
		add_q4Editor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the q1 feature on CargoByQuarterDistributionModel
	 *
	 * @generated
	 */
	protected void add_q1Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1));
	}
	/**
	 * Create the editor for the q2 feature on CargoByQuarterDistributionModel
	 *
	 * @generated
	 */
	protected void add_q2Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2));
	}
	/**
	 * Create the editor for the q3 feature on CargoByQuarterDistributionModel
	 *
	 * @generated
	 */
	protected void add_q3Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3));
	}
	/**
	 * Create the editor for the q4 feature on CargoByQuarterDistributionModel
	 *
	 * @generated
	 */
	protected void add_q4Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4));
	}
}