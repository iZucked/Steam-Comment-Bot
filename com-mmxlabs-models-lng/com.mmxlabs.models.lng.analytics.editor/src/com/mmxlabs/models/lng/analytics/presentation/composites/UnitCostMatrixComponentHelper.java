/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * A component helper for UnitCostMatrix instances
 *
 * @generated
 */
public class UnitCostMatrixComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public UnitCostMatrixComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public UnitCostMatrixComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using UnitCostMatrix as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.UNIT_COST_MATRIX);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_fromPortsEditor(detailComposite, topClass);
		add_toPortsEditor(detailComposite, topClass);
		add_vesselEditor(detailComposite, topClass);
		add_notionalDayRateEditor(detailComposite, topClass);
		add_speedEditor(detailComposite, topClass);
		add_roundTripEditor(detailComposite, topClass);
		add_minimumLoadEditor(detailComposite, topClass);
		add_maximumLoadEditor(detailComposite, topClass);
		add_minimumDischargeEditor(detailComposite, topClass);
		add_maximumDischargeEditor(detailComposite, topClass);
		add_retainHeelEditor(detailComposite, topClass);
		add_cargoPriceEditor(detailComposite, topClass);
		add_baseFuelPriceEditor(detailComposite, topClass);
		add_cvValueEditor(detailComposite, topClass);
		add_costLinesEditor(detailComposite, topClass);
		add_allowedRoutesEditor(detailComposite, topClass);
		add_revenueShareEditor(detailComposite, topClass);
		add_ladenTimeAllowanceEditor(detailComposite, topClass);
		add_ballastTimeAllowanceEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the ports feature on UnitCostMatrix
	 *
	 * Disabled because ports is deprecated in favour of from and to ports.
	 *
	 * @generated NO
	 */
	protected void add_portsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__PORTS));
	}
	/**
	 * Create the editor for the fromPorts feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_fromPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__FROM_PORTS));
	}

	/**
	 * Create the editor for the toPorts feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_toPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__TO_PORTS));
	}

	/**
	 * Create the editor for the vessel feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_vesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__VESSEL));
	}
	/**
	 * Create the editor for the notionalDayRate feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_notionalDayRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__NOTIONAL_DAY_RATE));
	}
	/**
	 * Create the editor for the speed feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_speedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__SPEED));
	}
	/**
	 * Create the editor for the roundTrip feature on UnitCostMatrix
	 *
	 * @generated NO disabled
	 */
	protected void add_roundTripEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__ROUND_TRIP));
	}
	/**
	 * Create the editor for the minimumLoad feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_minimumLoadEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__MINIMUM_LOAD));
	}
	/**
	 * Create the editor for the maximumLoad feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_maximumLoadEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__MAXIMUM_LOAD));
	}
	/**
	 * Create the editor for the minimumDischarge feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_minimumDischargeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__MINIMUM_DISCHARGE));
	}
	/**
	 * Create the editor for the maximumDischarge feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_maximumDischargeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__MAXIMUM_DISCHARGE));
	}
	/**
	 * Create the editor for the retainHeel feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_retainHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__RETAIN_HEEL));
	}

	/**
	 * Create the editor for the cargoPrice feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_cargoPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__CARGO_PRICE));
	}
	/**
	 * Create the editor for the baseFuelPrice feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_baseFuelPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__BASE_FUEL_PRICE));
	}
	/**
	 * Create the editor for the cvValue feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_cvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__CV_VALUE));
	}
	/**
	 * Create the editor for the dischargeIdleTime feature on UnitCostMatrix
	 *
	 * @generated NOT
	 */
	protected void add_dischargeIdleTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME));
	}
	/**
	 * Create the editor for the returnIdleTime feature on UnitCostMatrix
	 *
	 * @generated NOT
	 */
	protected void add_returnIdleTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__RETURN_IDLE_TIME));
	}
	/**
	 * Create the editor for the costLines feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_costLinesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__COST_LINES));
	}

	/**
	 * Create the editor for the allowedRoutes feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_allowedRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__ALLOWED_ROUTES));
	}

	/**
	 * Create the editor for the revenueShare feature on UnitCostMatrix
	 *
	 * @generated NO
	 */
	protected void add_revenueShareEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__REVENUE_SHARE));
	}

	/**
	 * Create the editor for the ladenTimeAllowance feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_ladenTimeAllowanceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__LADEN_TIME_ALLOWANCE));
	}

	/**
	 * Create the editor for the ballastTimeAllowance feature on UnitCostMatrix
	 *
	 * @generated
	 */
	protected void add_ballastTimeAllowanceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.UNIT_COST_MATRIX__BALLAST_TIME_ALLOWANCE));
	}
}