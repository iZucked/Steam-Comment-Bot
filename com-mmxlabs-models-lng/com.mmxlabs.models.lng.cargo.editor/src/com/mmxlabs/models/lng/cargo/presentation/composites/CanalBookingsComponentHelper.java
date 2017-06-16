/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for CanalBookings instances
 *
 * @generated
 */
public class CanalBookingsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CanalBookingsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CanalBookingsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using CanalBookings as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CANAL_BOOKINGS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_canalBookingSlotsEditor(detailComposite, topClass);
		add_strictBoundaryOffsetDaysEditor(detailComposite, topClass);
		add_relaxedBoundaryOffsetDaysEditor(detailComposite, topClass);
		add_flexibleBookingAmountEditor(detailComposite, topClass);
		add_arrivalMarginHoursEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the canalBookingSlots feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_canalBookingSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS));
	}
	/**
	 * Create the editor for the strictBoundaryOffsetDays feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_strictBoundaryOffsetDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS));
	}
	/**
	 * Create the editor for the relaxedBoundaryOffsetDays feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_relaxedBoundaryOffsetDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS));
	}
	/**
	 * Create the editor for the flexibleBookingAmount feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_flexibleBookingAmountEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT));
	}

	/**
	 * Create the editor for the arrivalMarginHours feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_arrivalMarginHoursEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS));
	}
}