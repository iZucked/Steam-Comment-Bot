/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.IInlineEditorEnablementWrapper;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CooldownPrice instances
 *
 * @generated
 */
public class CooldownPriceComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CooldownPriceComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated NOT
	 */
	public CooldownPriceComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
//		superClassesHelpers.addAll(registry.getComponentHelpers(PricingPackage.Literals.PORTS_PRICE_MAP));
	}
	
	/**
	 * add editors to a composite, using CooldownPrice as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.COOLDOWN_PRICE);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_indexEditor(detailComposite, topClass);
		add_expressionEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the index feature on PortsPriceMap
	 *
	 * @generated NOT
	 */
	protected void add_indexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PORTS_PRICE_MAP__INDEX);
		detailComposite.addInlineEditor(new CooldownPriceTypeEditorWrapper(editor));
	}

	/**
	 * Create the editor for the expression feature on CooldownPrice
	 *
	 * @generated
	 */
	protected void add_expressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.COOLDOWN_PRICE__EXPRESSION);
		detailComposite.addInlineEditor(editor);
	}
	
	/**
	 * Simple class to enable an editor field only when a port has a particular capability.
	 * 
	 * @generated NOT
	 */
	private class CooldownPriceTypeEditorWrapper extends IInlineEditorEnablementWrapper {
		/**
		 * Return an inline editor which is enabled only when the linked port has a particular capability.
		 * 
		 * @param capability
		 *            The capability required to enable this GUI editor field.
		 * @param wrapped
		 *            The editor field to enable / disable.
		 */
		public CooldownPriceTypeEditorWrapper(final IInlineEditor wrapped) {
			super(wrapped);
		}

		@Override
		protected boolean respondToNotification(final Notification notification) {
			return notification.getFeature() == PricingPackage.eINSTANCE.getCooldownPrice_Expression();
		}

		@Override
		protected boolean isEnabled() {
			return !((CooldownPrice) input).isSetExpression();
		}

		@Override
		public Object createLayoutData(MMXRootObject root, EObject value,
				Control control) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}