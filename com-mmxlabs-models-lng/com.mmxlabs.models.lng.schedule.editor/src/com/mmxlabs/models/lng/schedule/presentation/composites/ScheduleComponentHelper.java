/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
 * A component helper for Schedule instances
 *
 * @generated
 */
public class ScheduleComponentHelper implements IComponentHelper {
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
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
			final IComponentHelper helper = registry.getComponentHelper(MMXCorePackage.Literals.MMX_OBJECT);
			if (helper != null) superClassesHelpers.add(helper);
		}
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
		add_completeEditor(detailComposite, topClass);
		add_sequencesEditor(detailComposite, topClass);
		add_unscheduledCargosEditor(detailComposite, topClass);
		add_allocationsEditor(detailComposite, topClass);
		add_slotAllocationsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the complete feature on Schedule
	 *
	 * @generated
	 */
	protected void add_completeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__COMPLETE));
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
	 * Create the editor for the unscheduledCargos feature on Schedule
	 *
	 * @generated
	 */
	protected void add_unscheduledCargosEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__UNSCHEDULED_CARGOS));
	}
	/**
	 * Create the editor for the allocations feature on Schedule
	 *
	 * @generated
	 */
	protected void add_allocationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__ALLOCATIONS));
	}
	/**
	 * Create the editor for the slotAllocations feature on Schedule
	 *
	 * @generated
	 */
	protected void add_slotAllocationsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.SCHEDULE__SLOT_ALLOCATIONS));
	}
}