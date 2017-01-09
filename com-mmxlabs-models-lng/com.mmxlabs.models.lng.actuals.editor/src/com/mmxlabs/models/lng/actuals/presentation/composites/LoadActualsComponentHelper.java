/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for LoadActuals instances
 *
 * @generated
 */
public class LoadActualsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public LoadActualsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public LoadActualsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(ActualsPackage.Literals.SLOT_ACTUALS));
	}
	
	/**
	 * add editors to a composite, using LoadActuals as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ActualsPackage.Literals.LOAD_ACTUALS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_contractTypeEditor(detailComposite, topClass);
		add_startingHeelM3Editor(detailComposite, topClass);
		add_startingHeelMMBTuEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the contractType feature on LoadActuals
	 *
	 * @generated
	 */
	protected void add_contractTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.LOAD_ACTUALS__CONTRACT_TYPE));
	}

	/**
	 * Create the editor for the startingHeelM3 feature on LoadActuals
	 *
	 * @generated
	 */
	protected void add_startingHeelM3Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_M3));
	}

	/**
	 * Create the editor for the startingHeelMMBTu feature on LoadActuals
	 *
	 * @generated
	 */
	protected void add_startingHeelMMBTuEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_MMB_TU));
	}
}