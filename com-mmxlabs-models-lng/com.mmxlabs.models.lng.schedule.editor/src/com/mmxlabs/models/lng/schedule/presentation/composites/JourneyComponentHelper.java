/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
 * A component helper for Journey instances
 *
 * @generated
 */
public class JourneyComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public JourneyComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public JourneyComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.EVENT));
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.FUEL_USAGE));
	}
	
	/**
	 * add editors to a composite, using Journey as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.JOURNEY);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_destinationEditor(detailComposite, topClass);
		add_ladenEditor(detailComposite, topClass);
		add_routeEditor(detailComposite, topClass);
		add_tollEditor(detailComposite, topClass);
		add_distanceEditor(detailComposite, topClass);
		add_speedEditor(detailComposite, topClass);
		add_canalEntryEditor(detailComposite, topClass);
		add_canalDateEditor(detailComposite, topClass);
		add_canalBookingEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the destination feature on Journey
	 *
	 * @generated
	 */
	protected void add_destinationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__DESTINATION));
	}
	/**
	 * Create the editor for the laden feature on Journey
	 *
	 * @generated
	 */
	protected void add_ladenEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__LADEN));
	}
	/**
	 * Create the editor for the route feature on Journey
	 *
	 * @generated
	 */
	protected void add_routeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__ROUTE));
	}
	/**
	 * Create the editor for the toll feature on Journey
	 *
	 * @generated
	 */
	protected void add_tollEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__TOLL));
	}

	/**
	 * Create the editor for the distance feature on Journey
	 *
	 * @generated
	 */
	protected void add_distanceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__DISTANCE));
	}

	/**
	 * Create the editor for the speed feature on Journey
	 *
	 * @generated
	 */
	protected void add_speedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__SPEED));
	}

	/**
	 * Create the editor for the canalEntry feature on Journey
	 *
	 * @generated
	 */
	protected void add_canalEntryEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__CANAL_ENTRY));
	}

	/**
	 * Create the editor for the canalDate feature on Journey
	 *
	 * @generated
	 */
	protected void add_canalDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__CANAL_DATE));
	}

	/**
	 * Create the editor for the canalBooking feature on Journey
	 *
	 * @generated
	 */
	protected void add_canalBookingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.JOURNEY__CANAL_BOOKING));
	}
}