/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for ShippingCostRow instances
 *
 * @generated
 */
public class ShippingCostRowComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ShippingCostRowComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ShippingCostRowComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using ShippingCostRow as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.SHIPPING_COST_ROW);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_dateEditor(detailComposite, topClass);
		add_cargoPriceEditor(detailComposite, topClass);
		add_cvValueEditor(detailComposite, topClass);
		add_destinationTypeEditor(detailComposite, topClass);
		add_heelVolumeEditor(detailComposite, topClass);
		add_includePortCostsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the plan feature on ShippingCostRow
	 *
	 * @generated NOT
	 */
	protected void add_planEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__PLAN));
	}

	/**
	 * Create the editor for the port feature on ShippingCostRow
	 *
	 * @generated
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__PORT));
	}
	/**
	 * Create the editor for the date feature on ShippingCostRow
	 *
	 * @generated
	 */
	protected void add_dateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__DATE));
	}
	/**
	 * Create the editor for the cargoPrice feature on ShippingCostRow
	 *
	 * @generated NOT
	 */
	protected void add_cargoPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__CARGO_PRICE);
		editor.addNotificationChangedListener(new ShippingCostRowEditorListener());
		detailComposite.addInlineEditor(editor);
	}
	/**
	 * Create the editor for the cvValue feature on ShippingCostRow
	 *
	 * @generated NOT
	 */
	protected void add_cvValueEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__CV_VALUE);
		editor.addNotificationChangedListener(new ShippingCostRowEditorListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the destinationType feature on ShippingCostRow
	 *
	 * @generated
	 */
	protected void add_destinationTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__DESTINATION_TYPE));
	}

	/**
	 * Create the editor for the heelVolume feature on ShippingCostRow
	 *
	 * @generated
	 */
	protected void add_heelVolumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__HEEL_VOLUME));
	}

	/**
	 * Create the editor for the includePortCosts feature on ShippingCostRow
	 *
	 * @generated
	 */
	protected void add_includePortCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, AnalyticsPackage.Literals.SHIPPING_COST_ROW__INCLUDE_PORT_COSTS));
	}
}