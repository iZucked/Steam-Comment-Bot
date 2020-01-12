/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.columngeneration.EMFReportColumnManager;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public interface ITradesColumnFactory {

	public static final String LOAD_START_GROUP = "TradesBasedColumnFactory.LOAD_START_GROUP";
	public static final String LOAD_PORT_GROUP = "TradesBasedColumnFactory.LOAD_PORT_GROUP";
	public static final String LOAD_DIVERSION_GROUP = "TradesBasedColumnFactory.LOAD_DIVERSION_GROUP";
	public static final String LOAD_PRICING_GROUP = "TradesBasedColumnFactory.LOAD_PRICING_GROUP";
	public static final String LOAD_PRICING_EXTRA_GROUP = "TradesBasedColumnFactory.LOAD_PRICING_EXTRA_GROUP";
	public static final String LOAD_VOLUME_GROUP = "TradesBasedColumnFactory.LOAD_VOLUME_GROUP";
	public static final String LOAD_WINDOW_GROUP = "TradesBasedColumnFactory.LOAD_WINDOW_GROUP";
	public static final String LOAD_EXTRA_GROUP = "TradesBasedColumnFactory.LOAD_EXTRA_GROUP";
	public static final String LOAD_END_GROUP = "TradesBasedColumnFactory.LOAD_END_GROUP";
	public static final String DISCHARGE_START_GROUP = "TradesBasedColumnFactory.DISCHARGE_START_GROUP";
	public static final String DISCHARGE_PORT_GROUP = "TradesBasedColumnFactory.DISCHARGE_PORT_GROUP";
	public static final String DISCHARGE_DIVERSION_GROUP = "TradesBasedColumnFactory.DISCHARGE_DIVERSION_GROUP";
	public static final String DISCHARGE_PRICING_GROUP = "TradesBasedColumnFactory.DISCHARGE_PRICING_GROUP";
	public static final String DISCHARGE_PRICING_EXTRA_GROUP = "TradesBasedColumnFactory.DISCHARGE_PRICING_EXTRA_GROUP";
	public static final String DISCHARGE_VOLUME_GROUP = "TradesBasedColumnFactory.DISCHARGE_VOLUME_GROUP";
	public static final String DISCHARGE_WINDOW_GROUP = "TradesBasedColumnFactory.DISCHARGE_WINDOW_GROUP";
	public static final String DISCHARGE_EXTRA_GROUP = "TradesBasedColumnFactory.DISCHARGE_EXTRA_GROUP";
	public static final String DISCHARGE_END_GROUP = "TradesBasedColumnFactory.DISCHARGE_END_GROUP";
	
	public static final String CARGO_START_GROUP = "TradesBasedColumnFactory.CARGO_START_GROUP";
	public static final String CARGO_END_GROUP = "TradesBasedColumnFactory.CARGO_END_GROUP";

	public static final String DEFAULT_BLOCK_TYPE = null;
	public static final String DEFAULT_ORDER_KEY = null;

	public static boolean isLoadGroup(String group) {
		if (group == null) {
			return false;
		}
		switch (group) {
		case LOAD_START_GROUP:
		case LOAD_PORT_GROUP:
		case LOAD_WINDOW_GROUP:
		case LOAD_PRICING_GROUP:
		case LOAD_PRICING_EXTRA_GROUP:
		case LOAD_VOLUME_GROUP:
		case LOAD_EXTRA_GROUP:
		case LOAD_END_GROUP:
			return true;
		}
		return false;
	}

	public static boolean isDischargeGroup(String group) {
		if (group == null) {
			return false;
		}
		switch (group) {
		case DISCHARGE_START_GROUP:
		case DISCHARGE_PORT_GROUP:
		case DISCHARGE_WINDOW_GROUP:
		case DISCHARGE_PRICING_GROUP:
		case DISCHARGE_PRICING_EXTRA_GROUP:
		case DISCHARGE_VOLUME_GROUP:
		case DISCHARGE_EXTRA_GROUP:
		case DISCHARGE_END_GROUP:
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param report
	 *            TODO
	 */
	void registerColumn(final String columnID, final EMFReportColumnManager columnManager, IReferenceValueProviderProvider referenceValueProvider, EditingDomain editingDomain,
			IScenarioEditingLocation editingLocation, EClass eclass, IAdaptable report);
}