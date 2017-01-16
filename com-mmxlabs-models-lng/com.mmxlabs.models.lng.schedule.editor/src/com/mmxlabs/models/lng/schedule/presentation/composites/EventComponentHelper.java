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
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Event instances
 *
 * @generated
 */
public class EventComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public EventComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public EventComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.ITIMEZONE_PROVIDER));
	}
	
	/**
	 * add editors to a composite, using Event as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.EVENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_startEditor(detailComposite, topClass);
		add_endEditor(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_previousEventEditor(detailComposite, topClass);
		add_nextEventEditor(detailComposite, topClass);
		add_sequenceEditor(detailComposite, topClass);
		add_charterCostEditor(detailComposite, topClass);
		add_heelAtStartEditor(detailComposite, topClass);
		add_heelAtEndEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the start feature on Event
	 *
	 * @generated
	 */
	protected void add_startEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__START));
	}
	/**
	 * Create the editor for the end feature on Event
	 *
	 * @generated
	 */
	protected void add_endEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__END));
	}
	/**
	 * Create the editor for the port feature on Event
	 *
	 * @generated
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__PORT));
	}

	/**
	 * Create the editor for the previousEvent feature on Event
	 *
	 * @generated
	 */
	protected void add_previousEventEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__PREVIOUS_EVENT));
	}

	/**
	 * Create the editor for the nextEvent feature on Event
	 *
	 * @generated
	 */
	protected void add_nextEventEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__NEXT_EVENT));
	}

	/**
	 * Create the editor for the sequence feature on Event
	 *
	 * @generated NOT
	 */
	protected void add_sequenceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__SEQUENCE));
	}

	/**
	 * Create the editor for the charterCost feature on Event
	 *
	 * @generated
	 */
	protected void add_charterCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__CHARTER_COST));
	}

	/**
	 * Create the editor for the heelAtStart feature on Event
	 *
	 * @generated
	 */
	protected void add_heelAtStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__HEEL_AT_START));
	}

	/**
	 * Create the editor for the heelAtEnd feature on Event
	 *
	 * @generated
	 */
	protected void add_heelAtEndEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.EVENT__HEEL_AT_END));
	}
}