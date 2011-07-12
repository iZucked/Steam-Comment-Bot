/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;

import scenario.Scenario;
import scenario.contract.Contract;
import scenario.contract.ContractModel;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortPackage;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.BasicAttributeManipulator;
import scenario.presentation.cargoeditor.ICellManipulator;
import scenario.presentation.cargoeditor.ICellRenderer;
import scenario.presentation.cargoeditor.NumericAttributeManipulator;
import scenario.presentation.cargoeditor.importer.ExportCSVAction;
import scenario.presentation.cargoeditor.importer.ImportCSVAction;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lngscheduler.emf.extras.EMFPath;

/**
 * A {@link ScenarioObjectEditorViewerPane} for editing a port model
 * 
 * @author Tom Hinton
 * 
 */
public class PortEVP extends NamedObjectEVP {
	public PortEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	/**
	 * A special-case column for specifying default contract from the port even though
	 * it's an attribute on the contract really.
	 * 
	 * @author Tom Hinton
	 *
	 */
	private class DefaultContractManipulator implements ICellRenderer, ICellManipulator {
		@Override
		public void setValue(final Object object, final Object value) {
			
		}

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			return null;
		}

		@Override
		public Object getValue(final Object object) {
			
			return null;
		}

		@Override
		public boolean canEdit(Object object) {
			return false;
		}

		@Override
		public String render(Object object) {
			if (object instanceof Port) {
				final Port p = (Port) object;
				final Scenario scenario = part.getScenario();
				if (scenario.getContractModel() != null) {
					final ContractModel cm = scenario.getContractModel();
					final Contract c = cm.getDefaultContract(p);
					if (c != null) return c.getName();
				}
			}
			return "empty";
		}

		@Override
		public Comparable getComparable(Object object) {
			return render(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(
				Object object) {
			return Collections.emptySet();
		}
		
	}
	
	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		// add columns
		final PortPackage pp = PortPackage.eINSTANCE;

		final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(
				pp.getPort_TimeZone(), part.getEditingDomain());
		addColumn("Timezone", manipulator, manipulator);

		addTypicalColumn(
				"Regas Efficiency",
				new NumericAttributeManipulator(PortPackage.eINSTANCE
						.getPort_RegasEfficiency(), part.getEditingDomain()));
		
		if (part.getScenario().getContractModel() != null) {
			addTypicalColumn("Default Contract", new DefaultContractManipulator());
		}
	}

	@Override
	protected Action createExportAction(final TableViewer viewer,
			final EMFPath ePath) {
		final Action exportPortsAction = super
				.createExportAction(viewer, ePath);
		final Action exportDistanceModelAction = new ExportCSVAction() {
			@Override
			public void run() {
				exportPortsAction.run();
				super.run();
			}

			@Override
			public List<EObject> getObjectsToExport() {
				return Collections.singletonList((EObject) part.getScenario()
						.getDistanceModel());
			}

			@Override
			public EClass getExportEClass() {
				return PortPackage.eINSTANCE.getDistanceModel();
			}
		};
		return exportDistanceModelAction;
	}

	@Override
	protected Action createImportAction(final TableViewer viewer,
			final EditingDomain editingDomain, final EMFPath ePath) {
		final ImportCSVAction delegate = (ImportCSVAction) super
				.createImportAction(viewer, editingDomain, ePath);
		return new ImportCSVAction() {
			@Override
			public void run() {
				delegate.run();
				super.run();
			}

			@Override
			protected EObject getToplevelObject() {
				return part.getScenario();
			}

			@Override
			protected EClass getImportClass() {
				return PortPackage.eINSTANCE.getDistanceModel();
			}

			@Override
			public void addObjects(final Collection<EObject> newObjects) {
				part.getScenario().setDistanceModel(
						(DistanceModel) newObjects.iterator().next());
			}
		};
	}
}
