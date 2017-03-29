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
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for EndHeelOptions instances
 *
 * @generated
 */
public class EndHeelOptionsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public EndHeelOptionsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public EndHeelOptionsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using EndHeelOptions as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.END_HEEL_OPTIONS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_tankStateEditor(detailComposite, topClass);
		add_minimumEndHeelEditor(detailComposite, topClass);
		add_maximumEndHeelEditor(detailComposite, topClass);
		add_priceExpressionEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the tankState feature on EndHeelOptions
	 *
	 * @generated
	 */
	protected void add_tankStateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.END_HEEL_OPTIONS__TANK_STATE));
	}

	/**
	 * Create the editor for the minimumEndHeel feature on EndHeelOptions
	 *
	 * @generated
	 */
	protected void add_minimumEndHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.END_HEEL_OPTIONS__MINIMUM_END_HEEL));
	}

	/**
	 * Create the editor for the maximumEndHeel feature on EndHeelOptions
	 *
	 * @generated
	 */
	protected void add_maximumEndHeelEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.END_HEEL_OPTIONS__MAXIMUM_END_HEEL));
	}

	/**
	 * Create the editor for the priceExpression feature on EndHeelOptions
	 *
	 * @generated
	 */
	protected void add_priceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.END_HEEL_OPTIONS__PRICE_EXPRESSION));
	}
}