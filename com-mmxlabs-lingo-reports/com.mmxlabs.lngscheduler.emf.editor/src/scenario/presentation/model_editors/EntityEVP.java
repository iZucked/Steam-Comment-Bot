/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPage;

import scenario.Scenario;
import scenario.contract.ContractPackage;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.shiplingo.importer.importers.ExportCSVAction;
import com.mmxlabs.shiplingo.importer.importers.ImportCSVAction;
import com.mmxlabs.shiplingo.ui.tableview.PercentageAttributeManipulator;

/**
 * EVP for entities
 * 
 * @author Tom Hinton
 *
 */
public class EntityEVP extends NamedObjectEVP {
	public EntityEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addTypicalColumn(
				"Tax Rate",
				new PercentageAttributeManipulator(ContractPackage.eINSTANCE
						.getEntity_TaxRate(), part.getEditingDomain()));

		addTypicalColumn(
				"Ownership",
				new PercentageAttributeManipulator(ContractPackage.eINSTANCE
						.getEntity_Ownership(), part.getEditingDomain()));

	}
	
	@Override
	protected Action createExportAction(TableViewer viewer,
			EMFPath ePath) {
		final ExportCSVAction delegate = (ExportCSVAction) super
				.createExportAction(viewer, ePath);
		return new ExportCSVAction() {
			@Override
			public List<EObject> getObjectsToExport() {
				final List<EObject> export = delegate
						.getObjectsToExport();

				export.add(part.getScenario().getContractModel()
						.getShippingEntity());

				return export;
			}

			@Override
			public EClass getExportEClass() {
				return delegate.getExportEClass();
			}
		};
	}

	@Override
	protected Action createImportAction(TableViewer viewer,
			EditingDomain editingDomain, EMFPath ePath) {
		final ImportCSVAction delegate = (ImportCSVAction) super
				.createImportAction(viewer, editingDomain, ePath);
		return new ImportCSVAction() {
			@Override
			protected EObject getToplevelObject() {
				return part.getScenario();
			}

			@Override
			protected EClass getImportClass() {
				return ContractPackage.eINSTANCE.getEntity();
			}

			@Override
			public void addObjects(Collection<EObject> newObjects) {
				delegate.addObjects(newObjects);
				final Scenario scenario = part.getScenario();
				scenario.getContractModel().setShippingEntity(
						scenario.getContractModel()
								.getEntities()
								.get(scenario.getContractModel()
										.getEntities().size() - 1));
			}
		};
	}
}
