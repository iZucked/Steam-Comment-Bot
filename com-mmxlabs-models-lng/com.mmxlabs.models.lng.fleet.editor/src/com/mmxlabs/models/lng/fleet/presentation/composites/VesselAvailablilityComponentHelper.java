/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import com.mmxlabs.models.lng.fleet.FleetPackage;

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
 * A component helper for VesselAvailablility instances
 *
 * @generated
 */
public class VesselAvailablilityComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselAvailablilityComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselAvailablilityComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
			final IComponentHelper helper = registry.getComponentHelper(MMXCorePackage.Literals.MMX_OBJECT);
			if (helper != null) superClassesHelpers.add(helper);
		}
	}
	
	/**
	 * add editors to a composite, using VesselAvailablility as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL_AVAILABLILITY);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_startAtEditor(detailComposite, topClass);
		add_startAfterEditor(detailComposite, topClass);
		add_startByEditor(detailComposite, topClass);
		add_endAtEditor(detailComposite, topClass);
		add_endAfterEditor(detailComposite, topClass);
		add_endByEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the startAt feature on VesselAvailablility
	 *
	 * @generated
	 */
	protected void add_startAtEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_AVAILABLILITY__START_AT));
	}
	/**
	 * Create the editor for the startAfter feature on VesselAvailablility
	 *
	 * @generated
	 */
	protected void add_startAfterEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_AVAILABLILITY__START_AFTER));
	}
	/**
	 * Create the editor for the startBy feature on VesselAvailablility
	 *
	 * @generated
	 */
	protected void add_startByEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_AVAILABLILITY__START_BY));
	}

	/**
	 * Create the editor for the endAt feature on VesselAvailablility
	 *
	 * @generated
	 */
	protected void add_endAtEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_AVAILABLILITY__END_AT));
	}
	/**
	 * Create the editor for the endAfter feature on VesselAvailablility
	 *
	 * @generated
	 */
	protected void add_endAfterEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_AVAILABLILITY__END_AFTER));
	}
	/**
	 * Create the editor for the endBy feature on VesselAvailablility
	 *
	 * @generated
	 */
	protected void add_endByEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_AVAILABLILITY__END_BY));
	}
}