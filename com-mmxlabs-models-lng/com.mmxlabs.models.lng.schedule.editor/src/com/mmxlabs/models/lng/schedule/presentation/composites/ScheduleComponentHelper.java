/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Schedule instances
 *
 * @generated
 */
public class ScheduleComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ScheduleComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ScheduleComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using Schedule as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.SCHEDULE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_sequencesEditor(detailComposite, topClass);
		add_cargoAllocationsEditor(detailComposite, topClass);
		add_openSlotAllocationsEditor(detailComposite, topClass);
		add_marketAllocationsEditor(detailComposite, topClass);
		add_slotAllocationsEditor(detailComposite, topClass);
		add_fitnessesEditor(detailComposite, topClass);
		add_unusedElementsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the sequences feature on Schedule
	 *
	 * @generated
	 */
	protected void add_sequencesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__SEQUENCES));
	}
	/**
	 * Create the editor for the cargoAllocations feature on Schedule
	 *
	 * @generated
	 */
	protected void add_cargoAllocationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__CARGO_ALLOCATIONS));
	}

	/**
	 * Create the editor for the openSlotAllocations feature on Schedule
	 *
	 * @generated
	 */
	protected void add_openSlotAllocationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__OPEN_SLOT_ALLOCATIONS));
	}

	/**
	 * Create the editor for the marketAllocations feature on Schedule
	 *
	 * @generated
	 */
	protected void add_marketAllocationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__MARKET_ALLOCATIONS));
	}

	/**
	 * Create the editor for the slotAllocations feature on Schedule
	 *
	 * @generated
	 */
	protected void add_slotAllocationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__SLOT_ALLOCATIONS));
	}

	/**
	 * Create the editor for the fitnesses feature on Schedule
	 *
	 * @generated
	 */
	protected void add_fitnessesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__FITNESSES));
	}

	/**
	 * Create the editor for the unusedElements feature on Schedule
	 *
	 * @generated
	 */
	protected void add_unusedElementsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__UNUSED_ELEMENTS));
	}
}