/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
 * A component helper for NotionalJourneyBallastBonusContractLine instances
 *
 * @generated
 */
public class NotionalJourneyBallastBonusContractLineComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public NotionalJourneyBallastBonusContractLineComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public NotionalJourneyBallastBonusContractLineComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CommercialPackage.Literals.BALLAST_BONUS_CONTRACT_LINE));
	}
	
	/**
	 * add editors to a composite, using NotionalJourneyBallastBonusContractLine as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_speedEditor(detailComposite, topClass);
		add_fuelPriceExpressionEditor(detailComposite, topClass);
		add_hirePriceExpressionEditor(detailComposite, topClass);
		add_returnPortsEditor(detailComposite, topClass);
		add_includeCanalEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the speed feature on NotionalJourneyBallastBonusContractLine
	 *
	 * @generated
	 */
	protected void add_speedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED));
	}
	/**
	 * Create the editor for the fuelPriceExpression feature on NotionalJourneyBallastBonusContractLine
	 *
	 * @generated
	 */
	protected void add_fuelPriceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION));
	}
	/**
	 * Create the editor for the hirePriceExpression feature on NotionalJourneyBallastBonusContractLine
	 *
	 * @generated
	 */
	protected void add_hirePriceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION));
	}
	/**
	 * Create the editor for the returnPorts feature on NotionalJourneyBallastBonusContractLine
	 *
	 * @generated
	 */
	protected void add_returnPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS));
	}

	/**
	 * Create the editor for the includeCanal feature on NotionalJourneyBallastBonusContractLine
	 *
	 * @generated
	 */
	protected void add_includeCanalEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__INCLUDE_CANAL));
	}
}