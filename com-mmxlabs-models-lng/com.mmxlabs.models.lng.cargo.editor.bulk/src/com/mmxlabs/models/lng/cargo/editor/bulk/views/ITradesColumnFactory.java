package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.columngeneration.EMFReportColumnManager;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public interface ITradesColumnFactory {

	/**
	 * @param report TODO
	 */
	void registerColumn(final String columnID, final EMFReportColumnManager columnManager, IReferenceValueProviderProvider referenceValueProvider, EditingDomain editingDomain, IScenarioEditingLocation editingLocation, EClass eclass, IAdaptable report);
}