/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.EnumCheckboxEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;
import com.mmxlabs.models.ui.editors.impl.TimezoneInlineEditor;
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
		add_capabilitiesEditor(detailComposite, topClass);
		add_timeZoneEditor(detailComposite, topClass);
		add_loadDurationEditor(detailComposite, topClass);
		add_dischargeDurationEditor(detailComposite, topClass);
		add_cvValueEditor(detailComposite, topClass);
		add_defaultStartTimeEditor(detailComposite, topClass);
		add_allowCooldownEditor(detailComposite, topClass);
		add_defaultWindowSizeEditor(detailComposite, topClass);
		add_locationEditor(detailComposite, topClass);
		add_unlocodeEditor(detailComposite, topClass);
		add_atobviacCodeEditor(detailComposite, topClass);
		add_dataloyCodeEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the capabilities feature on Port
	 *
	 * @generated NO port capability enum editor factory not connected up, and the NSURI will change each release so will just override here.
	 */
	protected void add_capabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final PortCapability capability : PortCapability.values()) {
			detailComposite.addInlineEditor(
					new EnumCheckboxEditor(PortPackage.Literals.PORT__CAPABILITIES, capability, "Can ")
					);
		}
	}
	/**
	 * Create the editor for the timeZone feature on Port
	 *
	 * @generated NO
	 */
	protected void add_timeZoneEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(
				new TimezoneInlineEditor(PortPackage.Literals.PORT__TIME_ZONE));
	}
	/**
	 * Create the editor for the loadDuration feature on Port
	 *
	 * @generated NO
	 * Load duration is disabled if the port does not have load capability
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
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__ALLOW_COOLDOWN);
		detailComposite.addInlineEditor(new PortCapabilityEditorWrapper(PortCapability.LOAD, editor));
	}

	/**
	 * Create the editor for the defaultWindowSize feature on Port
	 *
	 * @generated
	 */
	protected void add_defaultWindowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__DEFAULT_WINDOW_SIZE));
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
	 * Create the editor for the unlocode feature on Port
	 *
	 * @generated
	 */
	protected void add_unlocodeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__UNLOCODE));
	}

	/**
	 * Create the editor for the atobviacCode feature on Port
	 *
	 * @generated
	 */
	protected void add_atobviacCodeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__ATOBVIAC_CODE));
	}

	/**
	 * Create the editor for the dataloyCode feature on Port
	 *
	 * @generated
	 */
	protected void add_dataloyCodeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PortPackage.Literals.PORT__DATALOY_CODE));
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
		 * @param capability The capability required to enable this GUI editor field. 
		 * @param wrapped The editor field to enable / disable.
		 */
		public PortCapabilityEditorWrapper(PortCapability capability, IInlineEditor wrapped) {
			super(wrapped);
			this.capability = capability;
		}

		@Override
		protected boolean respondToNotification(Notification notification) {
			return notification.getFeature() == PortPackage.eINSTANCE.getPort_Capabilities();
		}

		@Override
		protected boolean isEnabled() {
			return ((Port) input).getCapabilities().contains(capability);
		}
		
	}
}