/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for GroupProfitAndLoss instances
 *
 * @generated
 */
public class GroupProfitAndLossComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public GroupProfitAndLossComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public GroupProfitAndLossComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using GroupProfitAndLoss as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.GROUP_PROFIT_AND_LOSS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_profitAndLossEditor(detailComposite, topClass);
		add_profitAndLossPreTaxEditor(detailComposite, topClass);
		add_taxValueEditor(detailComposite, topClass);
		add_entityProfitAndLossesEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the profitAndLoss feature on GroupProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_profitAndLossEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS));
	}
	/**
	 * Create the editor for the profitAndLossPreTax feature on GroupProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_profitAndLossPreTaxEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.GROUP_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX));
	}

	/**
	 * Create the editor for the taxValue feature on GroupProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_taxValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.GROUP_PROFIT_AND_LOSS__TAX_VALUE));
	}

	/**
	 * Create the editor for the entityProfitAndLosses feature on GroupProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_entityProfitAndLossesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.GROUP_PROFIT_AND_LOSS__ENTITY_PROFIT_AND_LOSSES));
	}
}