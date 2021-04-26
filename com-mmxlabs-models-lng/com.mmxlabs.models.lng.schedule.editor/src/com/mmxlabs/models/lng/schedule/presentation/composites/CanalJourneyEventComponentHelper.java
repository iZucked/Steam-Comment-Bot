/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import com.mmxlabs.models.lng.schedule.SchedulePackage;

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
 * A component helper for CanalJourneyEvent instances
 *
 * @generated
 */
public class CanalJourneyEventComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CanalJourneyEventComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CanalJourneyEventComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.EVENT));
	}
	
	/**
	 * add editors to a composite, using CanalJourneyEvent as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.CANAL_JOURNEY_EVENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_linkedSequenceEditor(detailComposite, topClass);
		add_linkedJourneyEditor(detailComposite, topClass);
		add_panamaWaitingTimeHoursEditor(detailComposite, topClass);
		add_maxAvailablePanamaWaitingTimeHoursEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the linkedSequence feature on CanalJourneyEvent
	 *
	 * @generated
	 */
	protected void add_linkedSequenceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE));
	}
	/**
	 * Create the editor for the linkedJourney feature on CanalJourneyEvent
	 *
	 * @generated
	 */
	protected void add_linkedJourneyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CANAL_JOURNEY_EVENT__LINKED_JOURNEY));
	}

	/**
	 * Create the editor for the panamaWaitingTimeHours feature on CanalJourneyEvent
	 *
	 * @generated
	 */
	protected void add_panamaWaitingTimeHoursEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS));
	}

	/**
	 * Create the editor for the maxAvailablePanamaWaitingTimeHours feature on CanalJourneyEvent
	 *
	 * @generated
	 */
	protected void add_maxAvailablePanamaWaitingTimeHoursEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS));
	}
}