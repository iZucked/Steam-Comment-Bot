/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for ElementAssignment instances
 *
 * @generated
 */
public class ElementAssignmentComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ElementAssignmentComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ElementAssignmentComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using ElementAssignment as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, InputPackage.Literals.ELEMENT_ASSIGNMENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_assignedObjectEditor(detailComposite, topClass);
		add_assignmentEditor(detailComposite, topClass);
		add_lockedEditor(detailComposite, topClass);
		add_sequenceEditor(detailComposite, topClass);
		add_spotIndexEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the assignedObject feature on ElementAssignment
	 *
	 * @generated
	 */
	protected void add_assignedObjectEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, InputPackage.Literals.ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT));
	}
	/**
	 * Create the editor for the assignment feature on ElementAssignment
	 *
	 * @generated
	 */
	protected void add_assignmentEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, InputPackage.Literals.ELEMENT_ASSIGNMENT__ASSIGNMENT));
	}
	/**
	 * Create the editor for the locked feature on ElementAssignment
	 *
	 * @generated
	 */
	protected void add_lockedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, InputPackage.Literals.ELEMENT_ASSIGNMENT__LOCKED));
	}
	/**
	 * Create the editor for the sequence feature on ElementAssignment
	 *
	 * @generated
	 */
	protected void add_sequenceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, InputPackage.Literals.ELEMENT_ASSIGNMENT__SEQUENCE));
	}

	/**
	 * Create the editor for the spotIndex feature on ElementAssignment
	 *
	 * @generated
	 */
	protected void add_spotIndexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, InputPackage.Literals.ELEMENT_ASSIGNMENT__SPOT_INDEX));
	}
}