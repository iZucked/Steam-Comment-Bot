package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.columngeneration.EMFReportColumnManager;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public interface ITradesColumnFactory {

	public static final String LOAD_START_GROUP = "TradesBasedColumnFactory.LOAD_START_GROUP";
	public static final String LOAD_MAIN_GROUP = "TradesBasedColumnFactory.LOAD_MAIN_GROUP";
	public static final String LOAD_EXTRA_GROUP = "TradesBasedColumnFactory.LOAD_EXTRA_GROUP";
	public static final String LOAD_END_GROUP = "TradesBasedColumnFactory.LOAD_END_GROUP";
	public static final String DISCHARGE_START_GROUP = "TradesBasedColumnFactory.DISCHARGE_START_GROUP";
	public static final String DISCHARGE_MAIN_GROUP = "TradesBasedColumnFactory.DISCHARGE_MAIN_GROUP";
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
			return true;
		case LOAD_MAIN_GROUP:
			return true;
		case LOAD_EXTRA_GROUP:
			return true;
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
			return true;
		case DISCHARGE_MAIN_GROUP:
			return true;
		case DISCHARGE_EXTRA_GROUP:
			return true;
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