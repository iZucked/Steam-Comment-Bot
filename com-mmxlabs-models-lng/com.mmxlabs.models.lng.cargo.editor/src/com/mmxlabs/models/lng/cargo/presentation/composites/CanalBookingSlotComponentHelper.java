/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.ui.editors.RouteOptionInlineEditorFactory;
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
 * A component helper for CanalBookingSlot instances
 *
 * @generated
 */
public class CanalBookingSlotComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CanalBookingSlotComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CanalBookingSlotComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using CanalBookingSlot as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CANAL_BOOKING_SLOT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_routeOptionEditor(detailComposite, topClass);
		add_canalEntranceEditor(detailComposite, topClass);
		add_bookingDateEditor(detailComposite, topClass);
		add_slotEditor(detailComposite, topClass);
		add_notesEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the routeOption feature on CanalBookingSlot
	 *
	 * @generated NOT
	 */
	protected void add_routeOptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(RouteOptionInlineEditorFactory.createPanamaOnlyEditor(topClass, CargoPackage.Literals.CANAL_BOOKING_SLOT__ROUTE_OPTION));
	}

	/**
	 * Create the editor for the bookingDate feature on CanalBookingSlot
	 *
	 * @generated
	 */
	protected void add_bookingDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKING_SLOT__BOOKING_DATE));
	}

	/**
	 * Create the editor for the canalEntrance feature on CanalBookingSlot
	 *
	 * @generated
	 */
	protected void add_canalEntranceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKING_SLOT__CANAL_ENTRANCE));
	}

	/**
	 * Create the editor for the slot feature on CanalBookingSlot
	 *
	 * @generated
	 */
	protected void add_slotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKING_SLOT__SLOT));
	}

	/**
	 * Create the editor for the notes feature on CanalBookingSlot
	 *
	 * @generated NOT
	 */
	protected void add_notesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new MultiTextInlineEditor(CargoPackage.Literals.CANAL_BOOKING_SLOT__NOTES));
	}
}