/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for PaperDeal instances
 *
 * @generated
 */
public class PaperDealComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public PaperDealComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PaperDealComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using PaperDeal as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.PAPER_DEAL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_priceEditor(detailComposite, topClass);
		add_indexEditor(detailComposite, topClass);
		add_instrumentEditor(detailComposite, topClass);
		add_quantityEditor(detailComposite, topClass);
		add_startDateEditor(detailComposite, topClass);
		add_endDateEditor(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_yearEditor(detailComposite, topClass);
		add_commentEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the quantity feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_quantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__QUANTITY));
	}
	/**
	 * Create the editor for the startDate feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_startDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__START_DATE));
	}

	/**
	 * Create the editor for the endDate feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_endDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__END_DATE));
	}

	/**
	 * Create the editor for the entity feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__ENTITY));
	}

	/**
	 * Create the editor for the year feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_yearEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__YEAR));
	}

	/**
	 * Create the editor for the comment feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_commentEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__COMMENT));
	}

	/**
	 * Create the editor for the instrument feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_instrumentEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__INSTRUMENT));
	}

	/**
	 * Create the editor for the price feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_priceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__PRICE));
	}

	/**
	 * Create the editor for the index feature on PaperDeal
	 *
	 * @generated
	 */
	protected void add_indexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PAPER_DEAL__INDEX));
	}
}