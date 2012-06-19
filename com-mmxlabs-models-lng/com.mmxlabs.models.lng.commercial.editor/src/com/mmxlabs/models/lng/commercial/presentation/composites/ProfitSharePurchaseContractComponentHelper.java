/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for ProfitSharePurchaseContract instances
 *
 * @generated
 */
public class ProfitSharePurchaseContractComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ProfitSharePurchaseContractComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ProfitSharePurchaseContractComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CommercialPackage.Literals.PURCHASE_CONTRACT));
	}
	
	/**
	 * add editors to a composite, using ProfitSharePurchaseContract as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_baseMarketEditor(detailComposite, topClass);
		add_indexEditor(detailComposite, topClass);
		add_constantEditor(detailComposite, topClass);
		add_multiplierEditor(detailComposite, topClass);
		add_shareEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the baseMarket feature on ProfitSharePurchaseContract
	 *
	 * @generated
	 */
	protected void add_baseMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__BASE_MARKET));
	}
	/**
	 * Create the editor for the index feature on ProfitSharePurchaseContract
	 *
	 * @generated
	 */
	protected void add_indexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__INDEX));
	}
	/**
	 * Create the editor for the constant feature on ProfitSharePurchaseContract
	 *
	 * @generated
	 */
	protected void add_constantEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__CONSTANT));
	}
	/**
	 * Create the editor for the multiplier feature on ProfitSharePurchaseContract
	 *
	 * @generated
	 */
	protected void add_multiplierEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__MULTIPLIER));
	}
	/**
	 * Create the editor for the share feature on ProfitSharePurchaseContract
	 *
	 * @generated
	 */
	protected void add_shareEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PROFIT_SHARE_PURCHASE_CONTRACT__SHARE));
	}
}