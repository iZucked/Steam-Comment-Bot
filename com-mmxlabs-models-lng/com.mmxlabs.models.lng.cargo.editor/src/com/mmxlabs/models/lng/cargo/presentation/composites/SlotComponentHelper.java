/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.editors.impl.NumberInlineEditor;

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
		// final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry(); {
		// final IComponentHelper helper = registry.getComponentHelper(TypesPackage.Literals.ASLOT);
		// if (helper != null) superClassesHelpers.add(helper);
		// } {
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
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_windowStartEditor(detailComposite, topClass);
		add_windowStartTimeEditor(detailComposite, topClass);
		add_windowSizeEditor(detailComposite, topClass);
		add_contractEditor(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_durationEditor(detailComposite, topClass);
		add_minQuantityEditor(detailComposite, topClass);
		add_maxQuantityEditor(detailComposite, topClass);
		add_fixedPriceEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the windowStart feature on Slot
	 * 
	 * @generated
	 */
	protected void add_windowStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_START));
	}

	/**
	 * Create the editor for the windowStartTime feature on Slot
	 * 
	 * @generated
	 */
	protected void add_windowStartTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_START_TIME));
	}

	/**
	 * Create the editor for the windowSize feature on Slot
	 * 
	 * @generated
	 */
	protected void add_windowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_SIZE));
	}

	/**
	 * Create the editor for the port feature on Slot
	 * 
	 * @generated
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__PORT));
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

		final NumberInlineEditor editor = new NumberInlineEditor(CargoPackage.eINSTANCE.getSlot_Duration()) {
			protected boolean updateOnChangeToFeature(final Object changedFeature) {
				if (changedFeature == CargoPackage.eINSTANCE.getSlot_Port()) {
					return true;
				}
				if (changedFeature == CargoPackage.eINSTANCE.getLoadSlot_DESPurchase()) {
					return true;
				}
				if (changedFeature == CargoPackage.eINSTANCE.getDischargeSlot_FOBSale()) {
					return true;
				}
				return super.updateOnChangeToFeature(changedFeature);
			}

			@Override
			protected void updateDisplay(final Object value) {

				boolean enabled = true;
				if (input instanceof LoadSlot) {
					enabled = !((LoadSlot) input).isDESPurchase();
				} else if (input instanceof DischargeSlot) {
					enabled = !((DischargeSlot) input).isFOBSale();
				}
				super.updateDisplay(value);

//				setControlEnablement(enabled);
			}
		};
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
	 * Create the editor for the fixedPrice feature on Slot
	 * 
	 * @generated
	 */
	protected void add_fixedPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__FIXED_PRICE));
	}
}