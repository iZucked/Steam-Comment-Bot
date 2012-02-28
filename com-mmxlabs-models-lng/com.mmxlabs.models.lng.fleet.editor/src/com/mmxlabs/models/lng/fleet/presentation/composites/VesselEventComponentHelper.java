/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import com.mmxlabs.models.lng.fleet.FleetPackage;

import com.mmxlabs.models.lng.types.TypesPackage;

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
 * A component helper for VesselEvent instances
 *
 * @generated
 */
public class VesselEventComponentHelper implements IComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public VesselEventComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public VesselEventComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
			final IComponentHelper helper = registry.getComponentHelper(TypesPackage.Literals.AVESSEL_EVENT);
			if (helper != null) superClassesHelpers.add(helper);
		} {
			final IComponentHelper helper = registry.getComponentHelper(TypesPackage.Literals.ITIMEZONE_PROVIDER);
			if (helper != null) superClassesHelpers.add(helper);
		}
	}
	
	/**
	 * add editors to a composite, using VesselEvent as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, FleetPackage.Literals.VESSEL_EVENT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_durationInDaysEditor(detailComposite, topClass);
		add_allowedVesselsEditor(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_startAfterEditor(detailComposite, topClass);
		add_startByEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the durationInDays feature on VesselEvent
	 *
	 * @generated
	 */
	protected void add_durationInDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_EVENT__DURATION_IN_DAYS));
	}
	/**
	 * Create the editor for the allowedVessels feature on VesselEvent
	 *
	 * @generated
	 */
	protected void add_allowedVesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_EVENT__ALLOWED_VESSELS));
	}
	/**
	 * Create the editor for the port feature on VesselEvent
	 *
	 * @generated
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_EVENT__PORT));
	}
	/**
	 * Create the editor for the startAfter feature on VesselEvent
	 *
	 * @generated
	 */
	protected void add_startAfterEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_EVENT__START_AFTER));
	}

	/**
	 * Create the editor for the startBy feature on VesselEvent
	 *
	 * @generated
	 */
	protected void add_startByEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL_EVENT__START_BY));
	}
}