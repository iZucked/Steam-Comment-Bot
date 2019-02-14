/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import com.mmxlabs.models.lng.pricing.PricingPackage;

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
 * A component helper for SuezCanalTariffBand instances
 *
 * @generated
 */
public class SuezCanalTariffBandComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SuezCanalTariffBandComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SuezCanalTariffBandComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using SuezCanalTariffBand as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_ladenTariffEditor(detailComposite, topClass);
		add_ballastTariffEditor(detailComposite, topClass);
		add_bandStartEditor(detailComposite, topClass);
		add_bandEndEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the ladenTariff feature on SuezCanalTariffBand
	 *
	 * @generated
	 */
	protected void add_ladenTariffEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__LADEN_TARIFF));
	}
	/**
	 * Create the editor for the ballastTariff feature on SuezCanalTariffBand
	 *
	 * @generated
	 */
	protected void add_ballastTariffEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BALLAST_TARIFF));
	}
	/**
	 * Create the editor for the bandStart feature on SuezCanalTariffBand
	 *
	 * @generated
	 */
	protected void add_bandStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BAND_START));
	}
	/**
	 * Create the editor for the bandEnd feature on SuezCanalTariffBand
	 *
	 * @generated
	 */
	protected void add_bandEndEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.SUEZ_CANAL_TARIFF_BAND__BAND_END));
	}
}