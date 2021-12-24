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
 * A component helper for CharterLengthEvent instances
 *
 * @generated
 */
public class CharterLengthEventComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CharterLengthEventComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CharterLengthEventComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.PORT_VISIT));
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER));
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.EVENT_GROUPING));
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.FUEL_USAGE));
	}
	
	/**
	 * add editors to a composite, using CharterLengthEvent as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.CHARTER_LENGTH_EVENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_durationEditor(detailComposite, topClass);
		add_ladenEditor(detailComposite, topClass);
		add_contingencyHoursEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the duration feature on CharterLengthEvent
	 *
	 * @generated
	 */
	protected void add_durationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CHARTER_LENGTH_EVENT__DURATION));
	}

	/**
	 * Create the editor for the laden feature on CharterLengthEvent
	 *
	 * @generated
	 */
	protected void add_ladenEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CHARTER_LENGTH_EVENT__LADEN));
	}

	/**
	 * Create the editor for the contingencyHours feature on CharterLengthEvent
	 *
	 * @generated
	 */
	protected void add_contingencyHoursEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.CHARTER_LENGTH_EVENT__CONTINGENCY_HOURS));
	}
}