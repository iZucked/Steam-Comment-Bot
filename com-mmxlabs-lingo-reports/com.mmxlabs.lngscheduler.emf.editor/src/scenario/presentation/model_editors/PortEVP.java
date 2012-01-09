/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.contract.Contract;
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortCapability;
import scenario.port.PortPackage;
import scenario.presentation.LngEditorPlugin;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.common.Pair;
import com.mmxlabs.shiplingo.ui.detailview.base.IReferenceValueProvider;
import com.mmxlabs.shiplingo.ui.detailview.editors.TimezoneInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.dialogs.DistanceEditorDialog;
import com.mmxlabs.shiplingo.ui.tableview.BasicAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.EObjectTableViewer;
import com.mmxlabs.shiplingo.ui.tableview.ICellManipulator;
import com.mmxlabs.shiplingo.ui.tableview.ICellRenderer;
import com.mmxlabs.shiplingo.ui.tableview.PercentageAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.ValueListAttributeManipulator;

/**
 * A {@link ScenarioObjectEditorViewerPane} for editing a port model
 * 
 * TODO add cooldown choice
 * 
 * @author Tom Hinton
 * 
 */
public class PortEVP extends NamedObjectEVP {
	public PortEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	private class DefaultContractManipulator extends ContractManipulator {
		public DefaultContractManipulator(EditingDomain editingDomain,
				IReferenceValueProvider valueProvider) {
			super(editingDomain, valueProvider);
		}
		
	}
	
	/**
	 * Column for editing port capabilities.
	 * 
	 * @author hinton
	 */
	private class CapabilityManipulator implements ICellRenderer, ICellManipulator {
		private final PortCapability capability;
		private final EditingDomain editingDomain;

		public CapabilityManipulator(final PortCapability capability, final EditingDomain editingDomain) {
			this.capability = capability;
			this.editingDomain = editingDomain;
		}

		@Override
		public void setValue(Object object, Object value) {
			
			final Port p = (Port) object;
			if ((Integer) value == 0) {
				editingDomain.getCommandStack().execute(AddCommand.create(editingDomain, object, PortPackage.eINSTANCE.getPort_Capabilities(), capability));
			} else {
				editingDomain.getCommandStack().execute(RemoveCommand.create(editingDomain, object, PortPackage.eINSTANCE.getPort_Capabilities(), capability));
			}
		}

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			return new ComboBoxCellEditor(parent, new String[] {"Yes", "No"});
		}

		@Override
		public Object getValue(final Object object) {
			final Port p = (Port) object;
			return p.getCapabilities().contains(capability);
		}

		@Override
		public boolean canEdit(Object object) {
			return true;
		}

		@Override
		public String render(Object object) {
			return ((Boolean) getValue(object)) ? "Yes" : "No";
		}

		@Override
		public Comparable getComparable(Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(Object object) {
			return getComparable(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
			return Collections.emptySet();
		}
	}
	
	
	/**
	 * A special-case column for specifying default contract from the port even
	 * though it's an attribute on the contract really.
	 * 
	 * @author Tom Hinton
	 * 
	 */
	private class ContractManipulator implements ICellRenderer,
			ICellManipulator {
		
		public ContractManipulator(final EditingDomain editingDomain,
				final IReferenceValueProvider valueProvider) {
			super();
			this.editingDomain = editingDomain;
			this.valueProvider = valueProvider;
		}

		private ComboBoxCellEditor editor;
		private final EditingDomain editingDomain;
		private final IReferenceValueProvider valueProvider;

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			editor = new ComboBoxCellEditor(parent, new String[] { "empty" });
			setEditorNames(editor);
			return editor;
		}

		private void setEditorNames(final ComboBoxCellEditor editor) {
			if (editor != null)
				editor.setItems(names.toArray(new String[names.size()]));
		}

		@Override
		public Object getValue(final Object object) {
			Contract c = null;
			if (object instanceof Port) {
				final Port p = (Port) object;

				if (part.getScenario().getContractModel() != null) {
					c = part.getScenario().getContractModel()
							.getDefaultContract(p);
				}
			}

			return values.indexOf(c);
		}

		@Override
		public void setValue(final Object object, final Object value) {
			if (object instanceof Port && value instanceof Integer) {
				final Port port = (Port) object;
				final int index = (Integer) value;
				if (index != -1) {
					final Contract c2 = (Contract) values.get(index);
					final ContractModel cm = part.getScenario()
							.getContractModel();
					if (cm != null) {
						final CompoundCommand cc = new CompoundCommand();
						final Contract c1 = cm.getDefaultContract(port);
						if (c1 == c2)
							return;
						if (c1 != null) {
							cc.append(RemoveCommand.create(editingDomain, c1,
									ContractPackage.eINSTANCE
											.getContract_DefaultPorts(), port));
						}
						cc.append(AddCommand.create(editingDomain, c2,
								ContractPackage.eINSTANCE
										.getContract_DefaultPorts(), port));
						editingDomain.getCommandStack().execute(cc);
					}
				}
			}
		}

		final ArrayList<String> names = new ArrayList<String>();
		final ArrayList<EObject> values = new ArrayList<EObject>();

		@Override
		public boolean canEdit(final Object object) {
			final List<Pair<String, EObject>> both = valueProvider
					.getAllowedValues(null, null);

			names.clear();
			values.clear();
			for (final Pair<String, EObject> nameAndValue : both) {
				names.add(nameAndValue.getFirst());
				values.add(nameAndValue.getSecond());
			}

			setEditorNames(editor);

			return true;
		}
		
		@Override
		public Object getFilterValue(Object object) {
			return getComparable(object);
		}

		@Override
		public String render(Object object) {
			if (object instanceof Port) {
				final Port p = (Port) object;
				final Scenario scenario = part.getScenario();
				if (scenario.getContractModel() != null) {
					final ContractModel cm = scenario.getContractModel();
					final Contract c = cm.getDefaultContract(p);
					if (c != null)
						return c.getName();
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
	public EObjectTableViewer createViewer(Composite parent) {
		final EObjectTableViewer v = super.createViewer(parent);
		{
			//TODO find image for editor.
			final Action a = new Action() {
				{
					setImageDescriptor(LngEditorPlugin.Implementation
							.imageDescriptorFromPlugin(LngEditorPlugin.getPlugin()
									.getSymbolicName(), "/icons/table.gif"));
					setToolTipText("Edit distance matrix");
					setText("Edit distance matrix");
				}
				@Override
				public void run() {
					final DistanceModel currentModel = part.getScenario()
							.getDistanceModel();
					if (currentModel.eContainer() == part.getScenario()) {

						// open distance model editor
						final DistanceEditorDialog ded = new DistanceEditorDialog(
								v.getControl().getShell());

						if (ded.open(part,part.getEditingDomain(),currentModel) == Window.OK) {
							final DistanceModel newModel = ded.getResult();

							final CompoundCommand cc = new CompoundCommand();
							cc.append(DeleteCommand.create(
									part.getEditingDomain(), currentModel));

							cc.append(SetCommand.create(
									part.getEditingDomain(),
									part.getScenario(),
									ScenarioPackage.eINSTANCE
											.getScenario_DistanceModel(),
									newModel));

							cc.append(AddCommand.create(
									part.getEditingDomain(),
									part.getScenario(),
									ScenarioPackage.eINSTANCE
											.getScenario_ContainedModels(),
									newModel));
							part.getEditingDomain().getCommandStack()
									.execute(cc);
						}
					} else {
						MessageDialog
								.openError(
										v.getControl().getShell(),
										"Distance model is linked",
										"The distance model is stored in linked data, and so cannot be edited - open the static data model and edit it from there");
					}
				}
			};
			getToolBarManager().appendToGroup("pack", a);
		}
		return v;
	}
	
	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		// add columns
		final PortPackage pp = PortPackage.eINSTANCE;

		final BasicAttributeManipulator manipulator = new ValueListAttributeManipulator(
				pp.getPort_TimeZone(), part.getEditingDomain(),
				TimezoneInlineEditor.getTimezones()
				);
		addColumn("Timezone", manipulator, manipulator);
		
		if (part.getScenario().getContractModel() != null) {
			addTypicalColumn("Default Contract",
					new DefaultContractManipulator(part.getEditingDomain(),
							part.getContractProvider()));
		}
		
		for (final PortCapability capability : PortCapability.values()) {
			addTypicalColumn("Can " + capability.getName(), new CapabilityManipulator(capability, part.getEditingDomain()));
		}
	}

//	@Override
//	protected Action createExportAction(final TableViewer viewer,
//			final EMFPath ePath) {
//		final Action exportPortsAction = super
//				.createExportAction(viewer, ePath);
//		final Action exportDistanceModelAction = new ExportCSVAction() {
//			@Override
//			public void run() {
//				exportPortsAction.run();
//				super.run();
//			}
//
//			@Override
//			public List<EObject> getObjectsToExport() {
//				return Collections.singletonList((EObject) part.getScenario()
//						.getDistanceModel());
//			}
//
//			@Override
//			public EClass getExportEClass() {
//				return PortPackage.eINSTANCE.getDistanceModel();
//			}
//		};
//		return exportDistanceModelAction;
//	}
//
//	@Override
//	protected Action createImportAction(final TableViewer viewer,
//			final EditingDomain editingDomain, final EMFPath ePath) {
//		final ImportCSVAction delegate = (ImportCSVAction) super
//				.createImportAction(viewer, editingDomain, ePath);
//		return new ImportCSVAction() {
//			@Override
//			public void run() {
//				delegate.run();
//				super.run();
//			}
//
//			@Override
//			protected EObject getToplevelObject() {
//				return part.getScenario();
//			}
//
//			@Override
//			protected EClass getImportClass() {
//				return PortPackage.eINSTANCE.getDistanceModel();
//			}
//
//			@Override
//			public void addObjects(final Collection<EObject> newObjects) {
//				part.getScenario().setDistanceModel(
//						(DistanceModel) newObjects.iterator().next());
//			}
//		};
//	}
}
