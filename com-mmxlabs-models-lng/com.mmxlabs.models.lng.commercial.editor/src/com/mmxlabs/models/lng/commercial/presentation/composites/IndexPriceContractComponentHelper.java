/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
 * A component helper for IndexPriceContract instances
 *
 * @generated
 */
public class IndexPriceContractComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public IndexPriceContractComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated NO - there is an issue with having Contract as the supertype two ways at the moment
	 */
	public IndexPriceContractComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
		
				superClassesHelpers.addAll(registry.getComponentHelpers(CommercialPackage.Literals.SALES_CONTRACT));
		} 
//		{
//			final IComponentHelper helper = registry.getComponentHelper(CommercialPackage.Literals.PURCHASE_CONTRACT);
//			if (helper != null) superClassesHelpers.add(helper);
//		}
	}
	
	/**
	 * add editors to a composite, using IndexPriceContract as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.INDEX_PRICE_CONTRACT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_indexEditor(detailComposite, topClass);
		add_multiplierEditor(detailComposite, topClass);
		add_constantEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the index feature on IndexPriceContract
	 *
	 * @generated
	 */
	protected void add_indexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.INDEX_PRICE_CONTRACT__INDEX));
	}
	/**
	 * Create the editor for the constant feature on IndexPriceContract
	 *
	 * @generated
	 */
	protected void add_constantEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.INDEX_PRICE_CONTRACT__CONSTANT));
	}
	/**
	 * Create the editor for the multiplier feature on IndexPriceContract
	 *
	 * @generated
	 */
	protected void add_multiplierEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.INDEX_PRICE_CONTRACT__MULTIPLIER));
	}
}