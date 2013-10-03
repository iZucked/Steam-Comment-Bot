/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CharterOutEvent instances
 * 
 * @generated
 */
public class CharterOutEventComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public CharterOutEventComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated
	 */
	public CharterOutEventComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(FleetPackage.Literals.VESSEL_EVENT));
	}

	/**
	 * add editors to a composite, using CharterOutEvent as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.CHARTER_OUT_EVENT);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_relocateToEditor(detailComposite, topClass);
		add_heelOptionsEditor(detailComposite, topClass);
		add_repositioningFeeEditor(detailComposite, topClass);
		add_hireRateEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the relocateTo feature on CharterOutEvent
	 * 
	 * @generated
	 */
	protected void add_relocateToEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.CHARTER_OUT_EVENT__RELOCATE_TO));
	}

	/**
	 * Create the editor for the heelOptions feature on CharterOutEvent
	 * 
	 * @generated
	 */
	protected void add_heelOptionsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.CHARTER_OUT_EVENT__HEEL_OPTIONS));
	}

	/**
	 * Create the editor for the repositioningFee feature on CharterOutEvent
	 * 
	 * @generated
	 */
	protected void add_repositioningFeeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.CHARTER_OUT_EVENT__REPOSITIONING_FEE));
	}

	/**
	 * Create the editor for the hireRate feature on CharterOutEvent
	 * 
	 * @generated
	 */
	protected void add_hireRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.CHARTER_OUT_EVENT__HIRE_RATE));
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject rootObject, final EObject value) {
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final EObject assignment = (EObject) AssignmentEditorHelper.getElementAssignment(scenarioModel.getPortfolioModel().getAssignmentModel(), (UUIDObject) value);
			if (assignment != null) {
				return Collections.singletonList(assignment);
			}
		}
		return super.getExternalEditingRange(rootObject, value);
	}
}