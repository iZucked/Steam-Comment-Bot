/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for NotionalBallastParameters instances
 *
 * @generated
 */
public class NotionalBallastParametersComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public NotionalBallastParametersComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public NotionalBallastParametersComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using NotionalBallastParameters as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_routesEditor(detailComposite, topClass);
		add_speedEditor(detailComposite, topClass);
		add_hireCostEditor(detailComposite, topClass);
		add_nboRateEditor(detailComposite, topClass);
		add_baseConsumptionEditor(detailComposite, topClass);
		add_vesselClassesEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the routes feature on NotionalBallastParameters
	 *
	 * @generated
	 */
	protected void add_routesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS__ROUTES));
	}
	/**
	 * Create the editor for the speed feature on NotionalBallastParameters
	 *
	 * @generated
	 */
	protected void add_speedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS__SPEED));
	}
	/**
	 * Create the editor for the hireCost feature on NotionalBallastParameters
	 *
	 * @generated
	 */
	protected void add_hireCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS__HIRE_COST));
	}

	/**
	 * Create the editor for the nboRate feature on NotionalBallastParameters
	 *
	 * @generated
	 */
	protected void add_nboRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS__NBO_RATE));
	}
	/**
	 * Create the editor for the baseConsumption feature on NotionalBallastParameters
	 *
	 * @generated
	 */
	protected void add_baseConsumptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS__BASE_CONSUMPTION));
	}
	/**
	 * Create the editor for the vesselClasses feature on NotionalBallastParameters
	 *
	 * @generated
	 */
	protected void add_vesselClassesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.NOTIONAL_BALLAST_PARAMETERS__VESSEL_CLASSES));
	}
}