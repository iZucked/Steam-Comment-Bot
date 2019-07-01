/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
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
 * A component helper for PortVisit instances
 *
 * @generated
 */
public class PortVisitComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public PortVisitComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PortVisitComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.EVENT));
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.CAPACITY_VIOLATIONS_HOLDER));
	}
	
	/**
	 * add editors to a composite, using PortVisit as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.PORT_VISIT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_portCostEditor(detailComposite, topClass);
		add_latenessEditor(detailComposite, topClass);
		add_heelCostEditor(detailComposite, topClass);
		add_heelRevenueEditor(detailComposite, topClass);
		add_heelCostUnitPriceEditor(detailComposite, topClass);
		add_heelRevenueUnitPriceEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the portCost feature on PortVisit
	 *
	 * @generated
	 */
	protected void add_portCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.PORT_VISIT__PORT_COST));
	}

	/**
	 * Create the editor for the lateness feature on PortVisit
	 *
	 * @generated
	 */
	protected void add_latenessEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.PORT_VISIT__LATENESS));
	}

	/**
	 * Create the editor for the heelCost feature on PortVisit
	 *
	 * @generated
	 */
	protected void add_heelCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.PORT_VISIT__HEEL_COST));
	}

	/**
	 * Create the editor for the heelRevenue feature on PortVisit
	 *
	 * @generated
	 */
	protected void add_heelRevenueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.PORT_VISIT__HEEL_REVENUE));
	}

	/**
	 * Create the editor for the heelCostUnitPrice feature on PortVisit
	 *
	 * @generated
	 */
	protected void add_heelCostUnitPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.PORT_VISIT__HEEL_COST_UNIT_PRICE));
	}

	/**
	 * Create the editor for the heelRevenueUnitPrice feature on PortVisit
	 *
	 * @generated
	 */
	protected void add_heelRevenueUnitPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.PORT_VISIT__HEEL_REVENUE_UNIT_PRICE));
	}
}