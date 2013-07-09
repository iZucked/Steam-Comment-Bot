/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.dates.DateInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Slot instances
 * 
 * @generated
 */
public class SlotComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public SlotComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated NO there are no superclass fields we want here.
	 * 
	 *            TODO this could be an editor factory override instead.
	 */
	public SlotComponentHelper(final IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		{

//			for (final IComponentHelper helper : registry.getComponentHelpers(CargoPackage.Literals.SLOT)) {
//				if (helper != null) {
//					superClassesHelpers.add(helper);
//				}
//			}
		}
		// {
		// final IComponentHelper helper = registry.getComponentHelper(TypesPackage.Literals.ITIMEZONE_PROVIDER);
		// if (helper != null) superClassesHelpers.add(helper);
		// }
	}

	/**
	 * add editors to a composite, using Slot as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.SLOT);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated NOT
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_contractEditor(detailComposite, topClass);
		add_priceExpressionEditor(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_windowStartEditor(detailComposite, topClass);
		add_windowStartTimeEditor(detailComposite, topClass);
		add_windowSizeEditor(detailComposite, topClass);
		add_durationEditor(detailComposite, topClass);
		add_minQuantityEditor(detailComposite, topClass);
		add_maxQuantityEditor(detailComposite, topClass);
		add_optionalEditor(detailComposite, topClass);
		add_pricingDateEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the windowStart feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_windowStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		final IInlineEditor editor;
		if (topClass.getEAllSuperTypes().contains(CargoPackage.eINSTANCE.getSpotSlot())) {
			// For spot slots, only allow month portion to be edited
			editor = new DateInlineEditor(CargoPackage.eINSTANCE.getSlot_WindowStart(), new DateTimeFormatter("MM/yyyy"));
		} else {
			editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_START);
		}
		editor.addNotificationChangedListener(new SlotInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the pricingDate feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_pricingDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		final IInlineEditor editor;
		editor = new DateInlineEditor(CargoPackage.Literals.SLOT__PRICING_DATE, new DateTimeFormatter("MM/yyyy"));
		editor.addNotificationChangedListener(new SlotInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the windowStartTime feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_windowStartTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_START_TIME);
		editor.addNotificationChangedListener(new SlotInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the windowSize feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_windowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_SIZE);
		editor.addNotificationChangedListener(new SlotInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the port feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__PORT);
		editor.addNotificationChangedListener(new SlotInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the contract feature on Slot
	 * 
	 * @generated
	 */
	protected void add_contractEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__CONTRACT));
	}

	/**
	 * Create the editor for the duration feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_durationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final NumberInlineEditor editor = new NumberInlineEditor(CargoPackage.eINSTANCE.getSlot_Duration()); /* {
			protected boolean updateOnChangeToFeature(final Object changedFeature) {
				if (changedFeature == CargoPackage.eINSTANCE.getSlot_Port()) {
					return true;
				}

				return super.updateOnChangeToFeature(changedFeature);
			}

		};
		editor.addNotificationChangedListener(new SlotInlineEditorChangedListener());*/
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the minQuantity feature on Slot
	 * 
	 * @generated
	 */
	protected void add_minQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__MIN_QUANTITY));
	}

	/**
	 * Create the editor for the maxQuantity feature on Slot
	 * 
	 * @generated
	 */
	protected void add_maxQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__MAX_QUANTITY));
	}

	/**
	 * Create the editor for the optional feature on Slot
	 * 
	 * @generated
	 */
	protected void add_optionalEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__OPTIONAL));
	}

	/**
	 * Create the editor for the priceExpression feature on Slot
	 * 
	 * @generated
	 */
	protected void add_priceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__PRICE_EXPRESSION));
	}

	/**
	 * Create the editor for the cargo feature on Slot
	 *
	 * @generated
	 */
	protected void add_cargoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__CARGO));
	}

	/**
	 * Create the editor for the fixedPrice feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_fixedPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__FIXED_PRICE));
	}
}