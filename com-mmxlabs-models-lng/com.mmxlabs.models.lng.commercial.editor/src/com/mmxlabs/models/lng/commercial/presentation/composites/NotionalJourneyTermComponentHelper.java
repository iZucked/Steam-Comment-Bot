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
 * A component helper for NotionalJourneyTerm instances
 *
 * @generated
 */
public class NotionalJourneyTermComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public NotionalJourneyTermComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public NotionalJourneyTermComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using NotionalJourneyTerm as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM);	
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
		add_includeCanalEditor(detailComposite, topClass);
		add_includeCanalTimeEditor(detailComposite, topClass);
		add_lumpSumPriceExpressionEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the speed feature on NotionalJourneyTerm
	 *
	 * @generated
	 */
	protected void add_speedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__SPEED));
	}
	/**
	 * Create the editor for the fuelPriceExpression feature on NotionalJourneyTerm
	 *
	 * @generated
	 */
	protected void add_fuelPriceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION));
	}
	/**
	 * Create the editor for the hirePriceExpression feature on NotionalJourneyTerm
	 *
	 * @generated
	 */
	protected void add_hirePriceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION));
	}
	/**
	 * Create the editor for the includeCanal feature on NotionalJourneyTerm
	 *
	 * @generated
	 */
	protected void add_includeCanalEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL));
	}
	/**
	 * Create the editor for the includeCanalTime feature on NotionalJourneyTerm
	 *
	 * @generated
	 */
	protected void add_includeCanalTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME));
	}
	/**
	 * Create the editor for the lumpSumPriceExpression feature on NotionalJourneyTerm
	 *
	 * @generated
	 */
	protected void add_lumpSumPriceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION));
	}
}