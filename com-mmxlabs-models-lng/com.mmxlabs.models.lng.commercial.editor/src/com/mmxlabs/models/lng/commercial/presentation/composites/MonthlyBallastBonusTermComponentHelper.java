/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.models.lng.commercial.CommercialPackage;

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
 * A component helper for MonthlyBallastBonusTerm instances
 *
 * @generated
 */
public class MonthlyBallastBonusTermComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public MonthlyBallastBonusTermComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public MonthlyBallastBonusTermComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM));
	}
	
	/**
	 * add editors to a composite, using MonthlyBallastBonusTerm as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_monthEditor(detailComposite, topClass);
		add_ballastBonusToEditor(detailComposite, topClass);
		add_ballastBonusPctFuelEditor(detailComposite, topClass);
		add_ballastBonusPctCharterEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the month feature on MonthlyBallastBonusTerm
	 *
	 * @generated
	 */
	protected void add_monthEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__MONTH));
	}
	/**
	 * Create the editor for the ballastBonusTo feature on MonthlyBallastBonusTerm
	 *
	 * @generated
	 */
	protected void add_ballastBonusToEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_TO));
	}
	/**
	 * Create the editor for the ballastBonusPctFuel feature on MonthlyBallastBonusTerm
	 *
	 * @generated
	 */
	protected void add_ballastBonusPctFuelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_FUEL));
	}
	/**
	 * Create the editor for the ballastBonusPctCharter feature on MonthlyBallastBonusTerm
	 *
	 * @generated
	 */
	protected void add_ballastBonusPctCharterEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_CHARTER));
	}
}