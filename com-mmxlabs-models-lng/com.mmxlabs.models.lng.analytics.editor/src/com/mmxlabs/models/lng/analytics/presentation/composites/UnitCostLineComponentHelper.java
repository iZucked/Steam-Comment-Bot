/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for UnitCostLine instances
 *
 * @generated
 */
public class UnitCostLineComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public UnitCostLineComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public UnitCostLineComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using UnitCostLine as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.UNIT_COST_LINE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_unitCostEditor(detailComposite, topClass);
		add_mmbtuDeliveredEditor(detailComposite, topClass);
		add_fromEditor(detailComposite, topClass);
		add_toEditor(detailComposite, topClass);
		add_durationEditor(detailComposite, topClass);
		add_volumeLoadedEditor(detailComposite, topClass);
		add_volumeDischargedEditor(detailComposite, topClass);
		add_hireCostEditor(detailComposite, topClass);
		add_fuelCostEditor(detailComposite, topClass);
		add_canalCostEditor(detailComposite, topClass);
		add_costComponentsEditor(detailComposite, topClass);
		add_portCostEditor(detailComposite, topClass);
		add_profitEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the unitCost feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_unitCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__UNIT_COST));
	}
	/**
	 * Create the editor for the mmbtuDelivered feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_mmbtuDeliveredEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__MMBTU_DELIVERED));
	}
	/**
	 * Create the editor for the from feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_fromEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__FROM));
	}
	/**
	 * Create the editor for the to feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_toEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__TO));
	}
	/**
	 * Create the editor for the duration feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_durationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__DURATION));
	}
	/**
	 * Create the editor for the volumeLoaded feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_volumeLoadedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__VOLUME_LOADED));
	}
	/**
	 * Create the editor for the volumeDischarged feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_volumeDischargedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__VOLUME_DISCHARGED));
	}

	/**
	 * Create the editor for the hireCost feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_hireCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__HIRE_COST));
	}

	/**
	 * Create the editor for the fuelCost feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_fuelCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__FUEL_COST));
	}

	/**
	 * Create the editor for the canalCost feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_canalCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__CANAL_COST));
	}

	/**
	 * Create the editor for the costComponents feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_costComponentsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__COST_COMPONENTS));
	}

	/**
	 * Create the editor for the portCost feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_portCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__PORT_COST));
	}

	/**
	 * Create the editor for the profit feature on UnitCostLine
	 *
	 * @generated
	 */
	protected void add_profitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_LINE__PROFIT));
	}
}