/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * A component helper for EntityProfitAndLoss instances
 *
 * @generated
 */
public class EntityProfitAndLossComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public EntityProfitAndLossComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public EntityProfitAndLossComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using EntityProfitAndLoss as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.ENTITY_PROFIT_AND_LOSS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_entityBookEditor(detailComposite, topClass);
		add_profitAndLossEditor(detailComposite, topClass);
		add_profitAndLossPreTaxEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the entity feature on EntityProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.ENTITY_PROFIT_AND_LOSS__ENTITY));
	}
	/**
	 * Create the editor for the entityBook feature on EntityProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_entityBookEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.ENTITY_PROFIT_AND_LOSS__ENTITY_BOOK));
	}

	/**
	 * Create the editor for the profitAndLoss feature on EntityProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_profitAndLossEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS));
	}

	/**
	 * Create the editor for the profitAndLossPreTax feature on EntityProfitAndLoss
	 *
	 * @generated
	 */
	protected void add_profitAndLossPreTaxEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.ENTITY_PROFIT_AND_LOSS__PROFIT_AND_LOSS_PRE_TAX));
	}
}