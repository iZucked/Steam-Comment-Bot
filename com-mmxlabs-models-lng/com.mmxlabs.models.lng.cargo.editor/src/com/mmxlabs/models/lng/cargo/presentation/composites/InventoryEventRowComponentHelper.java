/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for InventoryEventRow instances
 *
 * @generated
 */
public class InventoryEventRowComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public InventoryEventRowComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public InventoryEventRowComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using InventoryEventRow as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.INVENTORY_EVENT_ROW);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_startDateEditor(detailComposite, topClass);
		add_endDateEditor(detailComposite, topClass);
		add_periodEditor(detailComposite, topClass);
		add_counterPartyEditor(detailComposite, topClass);
		add_reliabilityEditor(detailComposite, topClass);
		add_volumeEditor(detailComposite, topClass);
		add_forecastDateEditor(detailComposite, topClass);
		add_volumeLowEditor(detailComposite, topClass);
		add_volumeHighEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the startDate feature on InventoryEventRow
	 *
	 * @generated
	 */
	protected void add_startDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__START_DATE));
	}
	/**
	 * Create the editor for the endDate feature on InventoryEventRow
	 *
	 * @generated
	 */
	protected void add_endDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__END_DATE));
	}
	/**
	 * Create the editor for the period feature on InventoryEventRow
	 *
	 * @generated
	 */
	protected void add_periodEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__PERIOD));
	}
	/**
	 * Create the editor for the counterParty feature on InventoryEventRow
	 *
	 * @generated
	 */
	protected void add_counterPartyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__COUNTER_PARTY));
	}
	/**
	 * Create the editor for the reliability feature on InventoryEventRow
	 *
	 * @generated
	 */
	protected void add_reliabilityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__RELIABILITY));
	}

	/**
	 * Create the editor for the volume feature on InventoryEventRow
	 *
	 * @generated
	 */
	protected void add_volumeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__VOLUME));
	}

	/**
	 * Create the editor for the forecastDate feature on InventoryEventRow
	 *
	 * @generated
	 */
	protected void add_forecastDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__FORECAST_DATE));
	}

	/**
	 * Create the editor for the volumeLow feature on InventoryEventRow
	 *
	 * @generated NOT
	 */
	protected void add_volumeLowEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		//detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__VOLUME_LOW));
	}

	/**
	 * Create the editor for the volumeHigh feature on InventoryEventRow
	 *
	 * @generated NOT
	 */
	protected void add_volumeHighEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		//detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.INVENTORY_EVENT_ROW__VOLUME_HIGH));
	}
}