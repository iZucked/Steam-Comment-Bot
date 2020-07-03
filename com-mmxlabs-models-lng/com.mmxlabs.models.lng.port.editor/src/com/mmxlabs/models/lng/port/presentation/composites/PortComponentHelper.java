/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.EnumCheckboxEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Port instances
 * 
 * @generated
 */
public class PortComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public PortComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated
	 */
	public PortComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.APORT_SET));
	}

	/**
	 * add editors to a composite, using Port as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PortPackage.Literals.PORT);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_shortNameEditor(detailComposite, topClass);
		add_locationEditor(detailComposite, topClass);
		add_capabilitiesEditor(detailComposite, topClass);
		add_loadDurationEditor(detailComposite, topClass);
		add_dischargeDurationEditor(detailComposite, topClass);
		add_berthsEditor(detailComposite, topClass);
		add_cvValueEditor(detailComposite, topClass);
		add_defaultStartTimeEditor(detailComposite, topClass);
		add_allowCooldownEditor(detailComposite, topClass);
		add_defaultWindowSizeEditor(detailComposite, topClass);
		add_defaultWindowSizeUnitsEditor(detailComposite, topClass);
		add_minCvValueEditor(detailComposite, topClass);
		add_maxCvValueEditor(detailComposite, topClass);
		add_minVesselSizeEditor(detailComposite, topClass);
		add_maxVesselSizeEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the shortName feature on Port
	 *
	 * @generated
	 */
	protected void add_shortNameEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__SHORT_NAME));
	}

	/**
	 * Create the editor for the capabilities feature on Port
	 * 
	 * @generated NO port capability enum editor factory not connected up, and the NSURI will change each release so will just override here.
	 */
	protected void add_capabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final PortCapability capability : PortCapability.values()) {

			if (capability != PortCapability.TRANSFER || LicenseFeatures.isPermitted("features:shiptoship")) {
				detailComposite.addInlineEditor(new EnumCheckboxEditor(PortPackage.Literals.PORT__CAPABILITIES, capability, "Can "));
			}
		}
	}

	/**
	 * Create the editor for the loadDuration feature on Port
	 * 
	 * @generated NO Load duration is disabled if the port does not have load capability
	 */
	protected void add_loadDurationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__LOAD_DURATION);
		detailComposite.addInlineEditor(new PortCapabilityEditorWrapper(PortCapability.LOAD, editor));
	}

	/**
	 * Create the editor for the dischargeDuration feature on Port
	 * 
	 * @generated NO
	 */
	protected void add_dischargeDurationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__DISCHARGE_DURATION);
		detailComposite.addInlineEditor(new PortCapabilityEditorWrapper(PortCapability.DISCHARGE, editor));
	}

	/**
	 * Create the editor for the berths feature on Port
	 *
	 * @generated
	 */
	protected void add_berthsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__BERTHS));
	}

	/**
	 * Create the editor for the cvValue feature on Port
	 * 
	 * @generated NO
	 */
	protected void add_cvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__CV_VALUE);
		detailComposite.addInlineEditor(new PortCapabilityEditorWrapper(PortCapability.LOAD, editor));
	}

	/**
	 * Create the editor for the defaultStartTime feature on Port
	 * 
	 * @generated
	 */
	protected void add_defaultStartTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__DEFAULT_START_TIME));
	}

	/**
	 * Create the editor for the allowCooldown feature on Port
	 * 
	 * @generated NO
	 */
	protected void add_allowCooldownEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__ALLOW_COOLDOWN);
		detailComposite.addInlineEditor(new PortCapabilityEditorWrapper(PortCapability.LOAD, editor));
	}

	/**
	 * Create the editor for the defaultWindowSize feature on Port
	 * 
	 * @generated NOT
	 */
	protected void add_defaultWindowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE));
//		detailComposite.addInlineEditor(new WindowSizeInlineEditor(PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE  , PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE_UNITS));

	}

	/**
	 * Create the editor for the defaultWindowSizeUnits feature on Port
	 *
	 * @generated
	 */
	protected void add_defaultWindowSizeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE_UNITS));
	}

	/**
	 * Create the editor for the location feature on Port
	 * 
	 * @generated
	 */
	protected void add_locationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__LOCATION));
	}

	/**
	 * Create the editor for the minCvValue feature on Port
	 *
	 * @generated NO minCvValue is disabled if the port does not have discharge capability
	 */
	protected void add_minCvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__MIN_CV_VALUE);
		detailComposite.addInlineEditor(new PortCapabilityEditorWrapper(PortCapability.DISCHARGE, editor));
	}

	/**
	 * Create the editor for the maxCvValue feature on Port
	 *
	 * @generated NO minCvValue is disabled if the port does not have discharge capability
	 */
	protected void add_maxCvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__MAX_CV_VALUE);
		detailComposite.addInlineEditor(new PortCapabilityEditorWrapper(PortCapability.DISCHARGE, editor));
	}

	/**
	 * Create the editor for the minVesselSize feature on Port
	 *
	 * @generated
	 */
	protected void add_minVesselSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__MIN_VESSEL_SIZE));
	}

	/**
	 * Create the editor for the maxVesselSize feature on Port
	 *
	 * @generated
	 */
	protected void add_maxVesselSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__MAX_VESSEL_SIZE));
	}

	/**
	 * Create the editor for the mmxId feature on Port
	 *
	 * @generated NOT
	 */
	protected void add_mmxIdEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__MMX_ID));
	}

	/**
	 * Simple class to enable an editor field only when a port has a particular capability.
	 * 
	 * @generated NOT
	 */
	private class PortCapabilityEditorWrapper extends IInlineEditorEnablementWrapper {
		private final PortCapability capability;

		/**
		 * Return an inline editor which is enabled only when the linked port has a particular capability.
		 * 
		 * @param capability
		 *            The capability required to enable this GUI editor field.
		 * @param wrapped
		 *            The editor field to enable / disable.
		 */
		public PortCapabilityEditorWrapper(final PortCapability capability, final IInlineEditor wrapped) {
			super(wrapped);
			this.capability = capability;
		}

		@Override
		protected boolean respondToNotification(final Notification notification) {
			return notification.getFeature() == PortPackage.eINSTANCE.getPort_Capabilities();
		}

		@Override
		protected boolean isEnabled() {
			return ((Port) input).getCapabilities().contains(capability);
		}

		@Override
		public Object createLayoutData(MMXRootObject root, EObject value,
				Control control) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}