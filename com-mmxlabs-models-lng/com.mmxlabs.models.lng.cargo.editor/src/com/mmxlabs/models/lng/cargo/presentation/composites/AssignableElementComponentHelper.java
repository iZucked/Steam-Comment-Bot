/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.SpotIndexInlineEditor;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.AssignableElementEditorWrapper;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.AssignmentInlineEditor;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for AssignableElement instances
 *
 * @generated
 */
public class AssignableElementComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public AssignableElementComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public AssignableElementComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using AssignableElement as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.ASSIGNABLE_ELEMENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_sequenceHintEditor(detailComposite, topClass);
		add_vesselAssignmentTypeEditor(detailComposite, topClass);
		add_spotIndexEditor(detailComposite, topClass);
		add_lockedEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the spotIndex feature on AssignableElement
	 *
	 * @generated NOT
	 */
	protected void add_spotIndexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new AssignableElementEditorWrapper(new SpotIndexInlineEditor( CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX)));
	}
	/**
	 * Create the editor for the sequenceHint feature on AssignableElement
	 *
	 * @generated NOT
	 */
	protected void add_sequenceHintEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(new AssignableElementEditorWrapper(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.ASSIGNABLE_ELEMENT__SEQUENCE_HINT)));
	}
	/**
	 * Create the editor for the locked feature on AssignableElement
	 *
	 * @generated NOT
	 */
	protected void add_lockedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new AssignableElementEditorWrapper(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED)));
	}

	/**
	 * Create the editor for the vesselAssignmentType feature on AssignableElement
	 *
	 * @generated NOT
	 */
	protected void add_vesselAssignmentTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new AssignableElementEditorWrapper(new AssignmentInlineEditor(CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE)));

	}
}