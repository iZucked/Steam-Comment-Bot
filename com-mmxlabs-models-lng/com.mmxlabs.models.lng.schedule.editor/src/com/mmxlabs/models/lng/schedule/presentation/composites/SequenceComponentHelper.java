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
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Sequence instances
 *
 * @generated
 */
public class SequenceComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SequenceComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SequenceComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using Sequence as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.SEQUENCE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_eventsEditor(detailComposite, topClass);
		add_vesselAvailabilityEditor(detailComposite, topClass);
		add_charterInMarketEditor(detailComposite, topClass);
		add_fitnessesEditor(detailComposite, topClass);
		add_spotIndexEditor(detailComposite, topClass);
		add_sequenceTypeEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the events feature on Sequence
	 *
	 * @generated
	 */
	protected void add_eventsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SEQUENCE__EVENTS));
	}
	/**
	 * Create the editor for the vesselAvailability feature on Sequence
	 *
	 * @generated
	 */
	protected void add_vesselAvailabilityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SEQUENCE__VESSEL_AVAILABILITY));
	}

	/**
	 * Create the editor for the charterInMarket feature on Sequence
	 *
	 * @generated
	 */
	protected void add_charterInMarketEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SEQUENCE__CHARTER_IN_MARKET));
	}

	/**
	 * Create the editor for the fitnesses feature on Sequence
	 *
	 * @generated
	 */
	protected void add_fitnessesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SEQUENCE__FITNESSES));
	}

	/**
	 * Create the editor for the spotIndex feature on Sequence
	 *
	 * @generated
	 */
	protected void add_spotIndexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SEQUENCE__SPOT_INDEX));
	}

	/**
	 * Create the editor for the sequenceType feature on Sequence
	 *
	 * @generated
	 */
	protected void add_sequenceTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SEQUENCE__SEQUENCE_TYPE));
	}
}