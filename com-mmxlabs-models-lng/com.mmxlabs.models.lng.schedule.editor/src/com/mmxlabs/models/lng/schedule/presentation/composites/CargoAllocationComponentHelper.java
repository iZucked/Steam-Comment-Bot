/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * A component helper for CargoAllocation instances
 *
 * @generated
 */
public class CargoAllocationComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CargoAllocationComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CargoAllocationComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
			final IComponentHelper helper = registry.getComponentHelper(MMXCorePackage.Literals.MMX_OBJECT);
			if (helper != null) superClassesHelpers.add(helper);
		}
	}
	
	/**
	 * add editors to a composite, using CargoAllocation as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.CARGO_ALLOCATION);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_loadVisitEditor(detailComposite, topClass);
		add_dischargeVisitEditor(detailComposite, topClass);
		add_loadVolumeEditor(detailComposite, topClass);
		add_dischargeVolumeEditor(detailComposite, topClass);
		add_inputCargoEditor(detailComposite, topClass);
		add_ladenLegEditor(detailComposite, topClass);
		add_ballastLegEditor(detailComposite, topClass);
		add_ladenIdleEditor(detailComposite, topClass);
		add_ballastIdleEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the loadVisit feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_loadVisitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__LOAD_VISIT));
	}
	/**
	 * Create the editor for the dischargeVisit feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_dischargeVisitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_VISIT));
	}
	/**
	 * Create the editor for the loadVolume feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_loadVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__LOAD_VOLUME));
	}
	/**
	 * Create the editor for the dischargeVolume feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_dischargeVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_VOLUME));
	}
	/**
	 * Create the editor for the inputCargo feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_inputCargoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__INPUT_CARGO));
	}

	/**
	 * Create the editor for the ladenLeg feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_ladenLegEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__LADEN_LEG));
	}

	/**
	 * Create the editor for the ballastLeg feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_ballastLegEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__BALLAST_LEG));
	}

	/**
	 * Create the editor for the ladenIdle feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_ladenIdleEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__LADEN_IDLE));
	}

	/**
	 * Create the editor for the ballastIdle feature on CargoAllocation
	 *
	 * @generated
	 */
	protected void add_ballastIdleEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CARGO_ALLOCATION__BALLAST_IDLE));
	}
}