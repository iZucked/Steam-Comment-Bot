/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

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
 * A component helper for SolutionOptionMicroCase instances
 *
 * @generated
 */
public class SolutionOptionMicroCaseComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SolutionOptionMicroCaseComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SolutionOptionMicroCaseComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using SolutionOptionMicroCase as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.SOLUTION_OPTION_MICRO_CASE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_scheduleSpecificationEditor(detailComposite, topClass);
		add_scheduleModelEditor(detailComposite, topClass);
		add_extraVesselAvailabilitiesEditor(detailComposite, topClass);
		add_charterInMarketOverridesEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the scheduleSpecification feature on SolutionOptionMicroCase
	 *
	 * @generated
	 */
	protected void add_scheduleSpecificationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_SPECIFICATION));
	}

	/**
	 * Create the editor for the scheduleModel feature on SolutionOptionMicroCase
	 *
	 * @generated
	 */
	protected void add_scheduleModelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SOLUTION_OPTION_MICRO_CASE__SCHEDULE_MODEL));
	}
	/**
	 * Create the editor for the extraVesselAvailabilities feature on SolutionOptionMicroCase
	 *
	 * @generated
	 */
	protected void add_extraVesselAvailabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SOLUTION_OPTION_MICRO_CASE__EXTRA_VESSEL_AVAILABILITIES));
	}
	/**
	 * Create the editor for the charterInMarketOverrides feature on SolutionOptionMicroCase
	 *
	 * @generated
	 */
	protected void add_charterInMarketOverridesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SOLUTION_OPTION_MICRO_CASE__CHARTER_IN_MARKET_OVERRIDES));
	}
}