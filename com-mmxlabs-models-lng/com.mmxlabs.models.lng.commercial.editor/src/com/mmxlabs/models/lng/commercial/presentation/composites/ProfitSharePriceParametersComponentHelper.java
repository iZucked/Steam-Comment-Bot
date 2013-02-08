/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.models.lng.commercial.CommercialPackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for ProfitSharePriceParameters instances
 *
 * @generated
 */
public class ProfitSharePriceParametersComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ProfitSharePriceParametersComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ProfitSharePriceParametersComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CommercialPackage.Literals.LNG_PRICE_CALCULATOR_PARAMETERS));
	}
	
	/**
	 * add editors to a composite, using ProfitSharePriceParameters as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_baseMarketPortsEditor(detailComposite, topClass);
		add_baseMarketIndexEditor(detailComposite, topClass);
		add_baseMarketMultiplierEditor(detailComposite, topClass);
		add_baseMarketConstantEditor(detailComposite, topClass);
		add_refMarketIndexEditor(detailComposite, topClass);
		add_refMarketMultiplierEditor(detailComposite, topClass);
		add_refMarketConstantEditor(detailComposite, topClass);
		add_shareEditor(detailComposite, topClass);
		add_marginEditor(detailComposite, topClass);
		add_salesMultiplierEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the baseMarketPorts feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_baseMarketPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__BASE_MARKET_PORTS));
	}
	/**
	 * Create the editor for the baseMarketIndex feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_baseMarketIndexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__BASE_MARKET_INDEX));
	}
	/**
	 * Create the editor for the baseMarketMultiplier feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_baseMarketMultiplierEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__BASE_MARKET_MULTIPLIER));
	}
	/**
	 * Create the editor for the baseMarketConstant feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_baseMarketConstantEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__BASE_MARKET_CONSTANT));
	}
	/**
	 * Create the editor for the refMarketIndex feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_refMarketIndexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__REF_MARKET_INDEX));
	}
	/**
	 * Create the editor for the refMarketMultiplier feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_refMarketMultiplierEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__REF_MARKET_MULTIPLIER));
	}
	/**
	 * Create the editor for the refMarketConstant feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_refMarketConstantEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__REF_MARKET_CONSTANT));
	}
	/**
	 * Create the editor for the share feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_shareEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__SHARE));
	}
	/**
	 * Create the editor for the margin feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_marginEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__MARGIN));
	}
	/**
	 * Create the editor for the salesMultiplier feature on ProfitSharePriceParameters
	 *
	 * @generated
	 */
	protected void add_salesMultiplierEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PRICE_PARAMETERS__SALES_MULTIPLIER));
	}
}