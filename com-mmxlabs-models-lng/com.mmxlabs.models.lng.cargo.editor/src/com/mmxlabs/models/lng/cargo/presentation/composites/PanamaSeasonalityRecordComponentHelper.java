/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.models.ui.dates.MonthInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for PanamaSeasonalityRecord instances
 *
 * @generated
 */
public class PanamaSeasonalityRecordComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public PanamaSeasonalityRecordComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PanamaSeasonalityRecordComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using PanamaSeasonalityRecord as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_vesselGroupCanalParameterEditor(detailComposite, topClass);
		add_startDayEditor(detailComposite, topClass);
		add_startMonthEditor(detailComposite, topClass);
		add_startYearEditor(detailComposite, topClass);
		add_northboundWaitingDaysEditor(detailComposite, topClass);
		add_southboundWaitingDaysEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the vesselGroupCanalParameter feature on PanamaSeasonalityRecord
	 *
	 * @generated
	 */
	protected void add_vesselGroupCanalParameterEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER));
	}

	/**
	 * Create the editor for the startDay feature on PanamaSeasonalityRecord
	 *
	 * @generated
	 */
	protected void add_startDayEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_DAY));
	}

	/**
	 * Create the editor for the startMonth feature on PanamaSeasonalityRecord
	 *
	 * @generated NOT
	 */
	protected void add_startMonthEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new MonthInlineEditor(CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_MONTH));
	}

	/**
	 * Create the editor for the startYear feature on PanamaSeasonalityRecord
	 *
	 * @generated
	 */
	protected void add_startYearEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_YEAR));
	}

	/**
	 * Create the editor for the northboundWaitingDays feature on PanamaSeasonalityRecord
	 *
	 * @generated
	 */
	protected void add_northboundWaitingDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS));
	}

	/**
	 * Create the editor for the southboundWaitingDays feature on PanamaSeasonalityRecord
	 *
	 * @generated
	 */
	protected void add_southboundWaitingDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS));
	}
}