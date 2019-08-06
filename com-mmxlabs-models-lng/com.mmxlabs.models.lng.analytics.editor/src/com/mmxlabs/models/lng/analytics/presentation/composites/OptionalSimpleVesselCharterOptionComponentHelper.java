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
 * A component helper for OptionalSimpleVesselCharterOption instances
 *
 * @generated
 */
public class OptionalSimpleVesselCharterOptionComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public OptionalSimpleVesselCharterOptionComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public OptionalSimpleVesselCharterOptionComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(AnalyticsPackage.Literals.SIMPLE_VESSEL_CHARTER_OPTION));
	}
	
	/**
	 * add editors to a composite, using OptionalSimpleVesselCharterOption as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_ballastBonusEditor(detailComposite, topClass);
		add_repositioningFeeEditor(detailComposite, topClass);
		add_startEditor(detailComposite, topClass);
		add_endEditor(detailComposite, topClass);
		add_startPortEditor(detailComposite, topClass);
		add_endPortEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the ballastBonus feature on OptionalSimpleVesselCharterOption
	 *
	 * @generated
	 */
	protected void add_ballastBonusEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__BALLAST_BONUS));
	}
	/**
	 * Create the editor for the repositioningFee feature on OptionalSimpleVesselCharterOption
	 *
	 * @generated
	 */
	protected void add_repositioningFeeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__REPOSITIONING_FEE));
	}
	/**
	 * Create the editor for the start feature on OptionalSimpleVesselCharterOption
	 *
	 * @generated
	 */
	protected void add_startEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START));
	}
	/**
	 * Create the editor for the end feature on OptionalSimpleVesselCharterOption
	 *
	 * @generated
	 */
	protected void add_endEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END));
	}
	/**
	 * Create the editor for the startPort feature on OptionalSimpleVesselCharterOption
	 *
	 * @generated
	 */
	protected void add_startPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START_PORT));
	}
	/**
	 * Create the editor for the endPort feature on OptionalSimpleVesselCharterOption
	 *
	 * @generated
	 */
	protected void add_endPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END_PORT));
	}
}