/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.displaycomposites.VolumeModeEditorWrapper;
import com.mmxlabs.models.lng.analytics.displaycomposites.WindowModeEditorWrapper;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for BuyOpportunity instances
 *
 * @generated
 */
public class BuyOpportunityComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public BuyOpportunityComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public BuyOpportunityComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(AnalyticsPackage.Literals.BUY_OPTION));
	}
	
	/**
	 * add editors to a composite, using BuyOpportunity as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.BUY_OPPORTUNITY);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_desPurchaseEditor(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_contractEditor(detailComposite, topClass);
		add_dateEditor(detailComposite, topClass);
		add_priceExpressionEditor(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_cvEditor(detailComposite, topClass);
		add_cancellationExpressionEditor(detailComposite, topClass);
		add_miscCostsEditor(detailComposite, topClass);
		add_volumeModeEditor(detailComposite, topClass);
		add_minVolumeEditor(detailComposite, topClass);
		add_maxVolumeEditor(detailComposite, topClass);
		add_volumeUnitsEditor(detailComposite, topClass);
		add_specifyWindowEditor(detailComposite, topClass);
		add_windowSizeEditor(detailComposite, topClass);
		add_windowSizeUnitsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the desPurchase feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_desPurchaseEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DES_PURCHASE));
	}

	/**
	 * Create the editor for the port feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT));
	}
	/**
	 * Create the editor for the contract feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_contractEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__CONTRACT));
	}
	/**
	 * Create the editor for the date feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_dateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE));
	}
	/**
	 * Create the editor for the priceExpression feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_priceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PRICE_EXPRESSION));
	}

	/**
	 * Create the editor for the entity feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__ENTITY));
	}

	/**
	 * Create the editor for the cv feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_cvEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__CV));
	}

	/**
	 * Create the editor for the cancellationExpression feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_cancellationExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__CANCELLATION_EXPRESSION));
	}

	/**
	 * Create the editor for the miscCosts feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_miscCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MISC_COSTS));
	}

	/**
	 * Create the editor for the volumeMode feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_volumeModeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_MODE));
	}

	/**
	 * Create the editor for the volumeUnits feature on BuyOpportunity
	 *
	 * @generated NOT
	 */
	protected void add_volumeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_UNITS);
		detailComposite.addInlineEditor(new VolumeModeEditorWrapper(editor));
	}

	/**
	 * Create the editor for the minVolume feature on BuyOpportunity
	 *
	 * @generated NOT
	 */
	protected void add_minVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME);
		detailComposite.addInlineEditor(new VolumeModeEditorWrapper(editor));

	}

	/**
	 * Create the editor for the maxVolume feature on BuyOpportunity
	 *
	 * @generated NOT
	 */
	protected void add_maxVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MAX_VOLUME);
		detailComposite.addInlineEditor(new VolumeModeEditorWrapper(editor));
	}

	/**
	 * Create the editor for the specifyWindow feature on BuyOpportunity
	 *
	 * @generated NOT
	 */
	protected void add_specifyWindowEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new WindowModeEditorWrapper(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__SPECIFY_WINDOW)));
	}

	/**
	 * Create the editor for the windowSize feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_windowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE));
	}

	/**
	 * Create the editor for the windowSizeUnits feature on BuyOpportunity
	 *
	 * @generated
	 */
	protected void add_windowSizeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.BUY_OPPORTUNITY__WINDOW_SIZE_UNITS));
	}
}