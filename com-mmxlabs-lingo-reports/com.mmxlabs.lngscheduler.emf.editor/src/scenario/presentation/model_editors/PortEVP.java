/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.contract.Contract;
import scenario.contract.ContractModel;
import scenario.contract.ContractPackage;
import scenario.fleet.FleetFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.port.Canal;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortCapability;
import scenario.port.PortFactory;
import scenario.port.PortPackage;
import scenario.presentation.LngEditorPlugin;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.handlers.delete.DeleteHelper;

import com.mmxlabs.common.Pair;
import com.mmxlabs.rcp.common.actions.AbstractMenuAction;
import com.mmxlabs.shiplingo.ui.detailview.base.IReferenceValueProvider;
import com.mmxlabs.shiplingo.ui.detailview.containers.DetailCompositeDialog;
import com.mmxlabs.shiplingo.ui.detailview.editors.TimezoneInlineEditor;
import com.mmxlabs.shiplingo.ui.detailview.editors.dialogs.DistanceEditorDialog;
import com.mmxlabs.shiplingo.ui.tableview.BasicAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.EObjectTableViewer;
import com.mmxlabs.shiplingo.ui.tableview.ICellManipulator;
import com.mmxlabs.shiplingo.ui.tableview.ICellRenderer;
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
	public PortEVP(final IWorkbenchPage page, final ScenarioEditor part) {
		super(page, part);
	}

	private class DefaultContractManipulator extends ContractManipulator {
		public DefaultContractManipulator(final EditingDomain editingDomain, final IReferenceValueProvider valueProvider) {
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
		public void setValue(final Object object, final Object value) {

			final Port p = (Port) object;
			if ((Integer) value == 0) {
				editingDomain.getCommandStack().execute(AddCommand.create(editingDomain, object, PortPackage.eINSTANCE.getPort_Capabilities(), capability));
			} else {
				editingDomain.getCommandStack().execute(RemoveCommand.create(editingDomain, object, PortPackage.eINSTANCE.getPort_Capabilities(), capability));
			}
		}

		@Override
		public CellEditor getCellEditor(final Composite parent, final Object object) {
			return new ComboBoxCellEditor(parent, new String[] { "Yes", "No" });
		}

		@Override
		public Object getValue(final Object object) {
			final Port p = (Port) object;
			return p.getCapabilities().contains(capability) ? 0 : 1;
		}

		@Override
		public boolean canEdit(final Object object) {
			return true;
		}

		@Override
		public String render(final Object object) {
			return ((Integer) getValue(object)) == 0 ? "Yes" : "No";
		}

		@Override
		public Comparable getComparable(final Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(final Object object) {
			return getComparable(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
			return Collections.emptySet();
		}
	}

	/**
	 * A special-case column for specifying default contract from the port even though it's an attribute on the contract really.
	 * 
	 * @author Tom Hinton
	 * 
	 */
	private class ContractManipulator implements ICellRenderer, ICellManipulator {

		public ContractManipulator(final EditingDomain editingDomain, final IReferenceValueProvider valueProvider) {
			super();
			this.editingDomain = editingDomain;
			this.valueProvider = valueProvider;
		}

		private ComboBoxCellEditor editor;
		private final EditingDomain editingDomain;
		private final IReferenceValueProvider valueProvider;

		@Override
		public CellEditor getCellEditor(final Composite parent, final Object object) {
			editor = new ComboBoxCellEditor(parent, new String[] { "empty" });
			setEditorNames(editor);
			return editor;
		}

		private void setEditorNames(final ComboBoxCellEditor editor) {
			if (editor != null) {
				editor.setItems(names.toArray(new String[names.size()]));
			}
		}

		@Override
		public Object getValue(final Object object) {
			Contract c = null;
			if (object instanceof Port) {
				final Port p = (Port) object;

				if (part.getScenario().getContractModel() != null) {
					c = part.getScenario().getContractModel().getDefaultContract(p);
				}
			}

			return values.indexOf(c);
		}

		@Override
		public void setValue(final Object object, final Object value) {
			if ((object instanceof Port) && (value instanceof Integer)) {
				final Port port = (Port) object;
				final int index = (Integer) value;
				if (index != -1) {
					final Contract c2 = (Contract) values.get(index);
					final ContractModel cm = part.getScenario().getContractModel();
					if (cm != null) {
						final CompoundCommand cc = new CompoundCommand();
						final Contract c1 = cm.getDefaultContract(port);
						if (c1 == c2) {
							return;
						}
						if (c1 != null) {
							cc.append(RemoveCommand.create(editingDomain, c1, ContractPackage.eINSTANCE.getContract_DefaultPorts(), port));
						}
						cc.append(AddCommand.create(editingDomain, c2, ContractPackage.eINSTANCE.getContract_DefaultPorts(), port));
						editingDomain.getCommandStack().execute(cc);
					}
				}
			}
		}

		final ArrayList<String> names = new ArrayList<String>();
		final ArrayList<EObject> values = new ArrayList<EObject>();

		@Override
		public boolean canEdit(final Object object) {
			final List<Pair<String, EObject>> both = valueProvider.getAllowedValues(null, null);

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
		public Object getFilterValue(final Object object) {
			return getComparable(object);
		}

		@Override
		public String render(final Object object) {
			if (object instanceof Port) {
				final Port p = (Port) object;
				final Scenario scenario = part.getScenario();
				if (scenario.getContractModel() != null) {
					final ContractModel cm = scenario.getContractModel();
					final Contract c = cm.getDefaultContract(p);
					if (c != null) {
						return c.getName();
					}
				}
			}
			return "empty";
		}

		@Override
		public Comparable getComparable(final Object object) {
			return render(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
			return Collections.emptySet();
		}

	}

	class DistanceMatrixEditorAction extends AbstractMenuAction {
		public DistanceMatrixEditorAction() {
			super("Edit distances");
			setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(LngEditorPlugin.getPlugin().getSymbolicName(), "/icons/table.gif"));
			setToolTipText("Edit distance matrices and canals");
		}

		@Override
		protected void populate(final Menu menu) {
			final Action editDefault = createMatrixEditor("direct", part.getScenario().getDistanceModel());
			addActionToMenu(editDefault, menu);
			new MenuItem(menu, SWT.SEPARATOR);
			for (final Canal canal : part.getScenario().getCanalModel().getCanals()) {
				final Action canalEditor = new AbstractMenuAction(canal.getName()) {
					@Override
					protected void populate(final Menu menu2) {
						final Action editCanal = createMatrixEditor(canal.getName(), canal.getDistanceModel());
						addActionToMenu(editCanal, menu2);
						final Action renameCanal = new Action("Rename " + canal.getName() + "...") {
							@Override
							public void run() {
								final HashSet<String> existingNames = new HashSet<String>();
								for (final Canal c : part.getScenario().getCanalModel().getCanals()) {
									if (c != canal) {
										existingNames.add(c.getName());
									}
								}
								final InputDialog input = new InputDialog(part.getEditorSite().getShell(), "Rename canal " + canal.getName(), "Enter a new name for the canal " + canal.getName(),
										canal.getName(), new IInputValidator() {
											@Override
											public String isValid(final String newText) {
												if (newText.trim().isEmpty()) {
													return "The canal must have a name";
												}
												if (existingNames.contains(newText)) {
													return "Another canal already has the name " + newText;
												}
												return null;
											}
										});
								if (input.open() == Window.OK) {
									final String newName = input.getValue();
									part.getEditingDomain().getCommandStack().execute(SetCommand.create(part.getEditingDomain(), canal, ScenarioPackage.eINSTANCE.getNamedObject_Name(), newName));
								}
							}
						};
						addActionToMenu(renameCanal, menu2);
						final Action deleteCanal = new Action("Delete " + canal.getName() + "...") {
							@Override
							public void run() {
								if (MessageDialog.openQuestion(part.getEditorSite().getShell(), "Delete canal " + canal.getName(), "Are you sure you want to delete the canal \"" + canal.getName()
										+ "\"?")) {
									part.getEditingDomain().getCommandStack().execute(DeleteHelper.createDeleteCommand(part.getEditingDomain(), Collections.singleton(canal)));
								}
							}
						};
						addActionToMenu(deleteCanal, menu2);
					}
				};
				addActionToMenu(canalEditor, menu);
			}

			new MenuItem(menu, SWT.SEPARATOR);

			addActionToMenu(new Action("Add new canal...") {
				@Override
				public void run() {
					final HashSet<String> existingNames = new HashSet<String>();
					for (final Canal c : part.getScenario().getCanalModel().getCanals()) {
						existingNames.add(c.getName());
					}
					final InputDialog input = new InputDialog(part.getEditorSite().getShell(), "Create canal", "Enter a name for the new canal", "", new IInputValidator() {
						@Override
						public String isValid(final String newText) {
							if (newText.trim().isEmpty()) {
								return "The canal must have a name";
							}
							if (existingNames.contains(newText)) {
								return "Another canal already has the name " + newText;
							}
							return null;
						}
					});
					if (input.open() == Window.OK) {
						final CompoundCommand cc = new CompoundCommand();
						final String newName = input.getValue();
						final Canal c = PortFactory.eINSTANCE.createCanal();
						c.setName(newName);
						DistanceModel dm = PortFactory.eINSTANCE.createDistanceModel();
						final DistanceEditorDialog ded = new DistanceEditorDialog(part.getEditorSite().getShell());
						if (ded.open(part, part.getEditingDomain(), dm) == Window.OK) {
							dm = ded.getResult();
						}
						c.setDistanceModel(dm);
						final VesselClassCost prototype = FleetFactory.eINSTANCE.createVesselClassCost();
						prototype.setCanal(c);

						final DetailCompositeDialog editor = new DetailCompositeDialog(part.getEditorSite().getShell(), part, part.getEditingDomain());

						editor.open(Collections.singletonList((EObject) prototype));
						for (final VesselClass vc : part.getScenario().getFleetModel().getVesselClasses()) {
							cc.append(AddCommand.create(part.getEditingDomain(), vc, FleetPackage.eINSTANCE.getVesselClass_CanalCosts(), EcoreUtil.copy(prototype)));
						}
						cc.append(AddCommand.create(part.getEditingDomain(), part.getScenario().getCanalModel(), PortPackage.eINSTANCE.getCanalModel_Canals(), c));
						part.getEditingDomain().getCommandStack().execute(cc);

					}
				}
			}, menu);
		}

		protected Action createMatrixEditor(final String name, final DistanceModel distanceModel) {
			return new Action("Edit " + name + " distances...") {
				@Override
				public void run() {
					final Scenario scenario = part.getScenario();
					final EditingDomain domain = part.getEditingDomain();
					if (distanceModel.eContainer() == scenario) {
						final DistanceModel newModel = edit(distanceModel);
						if (newModel == null) {
							return;
						}
						final CompoundCommand cc = new CompoundCommand();
						cc.append(DeleteCommand.create(domain, distanceModel));
						cc.append(AddCommand.create(domain, distanceModel.eContainer(), distanceModel.eContainingFeature(), newModel));
						cc.append(SetCommand.create(domain, scenario, ScenarioPackage.eINSTANCE.getScenario_DistanceModel(), newModel));
						domain.getCommandStack().execute(cc);
					} else if (distanceModel.eContainer() instanceof Canal) {
						if (distanceModel.eContainer().eContainer().eContainer() == scenario) {
							final DistanceModel newModel = edit(distanceModel);
							if (newModel == null) {
								return;
							}
							domain.getCommandStack().execute(SetCommand.create(domain, distanceModel.eContainer(), distanceModel.eContainingFeature(), newModel));
						}
					}

					// display error
				}

				private DistanceModel edit(final DistanceModel distanceModel) {
					final DistanceEditorDialog ded = new DistanceEditorDialog(part.getEditorSite().getShell());
					if (ded.open(part, part.getEditingDomain(), distanceModel) == Window.OK) {
						return ded.getResult();
					}
					return null;
				}
			};
		}
	}

	@Override
	public EObjectTableViewer createViewer(final Composite parent) {
		final EObjectTableViewer v = super.createViewer(parent);
		{
			// TODO find image for editor.

			final DistanceMatrixEditorAction dmaAction = new DistanceMatrixEditorAction();
			getToolBarManager().appendToGroup("pack", dmaAction);

			// MessageDialog
			// .openError(
			// v.getControl().getShell(),
			// "Distance model is linked",
			// "The distance model is stored in linked data, and so cannot be edited - open the static data model and edit it from there");

		}
		return v;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		// add columns
		final PortPackage pp = PortPackage.eINSTANCE;

		final BasicAttributeManipulator manipulator = new ValueListAttributeManipulator(pp.getPort_TimeZone(), part.getEditingDomain(), TimezoneInlineEditor.getTimezones());
		addColumn("Timezone", manipulator, manipulator);

		if (part.getScenario().getContractModel() != null) {
			addTypicalColumn("Default Contract", new DefaultContractManipulator(part.getEditingDomain(), part.getContractProvider()));
		}

		for (final PortCapability capability : PortCapability.values()) {
			addTypicalColumn("Can " + capability.getName(), new CapabilityManipulator(capability, part.getEditingDomain()));
		}
	}
}
