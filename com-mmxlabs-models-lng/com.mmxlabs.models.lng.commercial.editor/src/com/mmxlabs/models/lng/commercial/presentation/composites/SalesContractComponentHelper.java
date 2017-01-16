/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
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
 * A component helper for SalesContract instances
 *
 * @generated
 */
public class SalesContractComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SalesContractComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated NOT
	 */
	public SalesContractComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(CommercialPackage.Literals.CONTRACT));
//		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.ASALES_CONTRACT));
	}
	
	/**
	 * add editors to a composite, using SalesContract as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.SALES_CONTRACT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_minCvValueEditor(detailComposite, topClass);
		add_maxCvValueEditor(detailComposite, topClass);
		add_PurchaseDeliveryTypeEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the minCvValue feature on SalesContract
	 *
	 * @generated
	 */
	protected void add_minCvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.SALES_CONTRACT__MIN_CV_VALUE));
	}

	/**
	 * Create the editor for the maxCvValue feature on SalesContract
	 *
	 * @generated
	 */
	protected void add_maxCvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.SALES_CONTRACT__MAX_CV_VALUE));
	}

	/**
	 * Create the editor for the PurchaseDeliveryType feature on SalesContract
	 *
	 * @generated NO
	 */
	protected void add_PurchaseDeliveryTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE));
//		detailComposite.addInlineEditor(new (CommercialPackage.Literals.SALES_CONTRACT__PURCHASE_DELIVERY_TYPE));
	}
}