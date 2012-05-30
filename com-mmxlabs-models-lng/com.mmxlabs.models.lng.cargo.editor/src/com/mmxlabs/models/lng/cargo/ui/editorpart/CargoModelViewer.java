/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.ui.actions.RewireAction;
import com.mmxlabs.models.lng.cargo.ui.actions.RotateSlotsAction;
import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.BasicOperationRenderer;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class CargoModelViewer extends ScenarioTableViewerPane {
	private final IScenarioEditingLocation part;

	// TODO: Make these colours a preference so they can be consistently used across various UI parts
	private Color desCargo = new Color(Display.getDefault(), 150, 210, 230);
	private Color fobCargo = new Color(Display.getDefault(), 190, 220, 180);

	public CargoModelViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.part = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		final RewireAction rewire = new RewireAction(getJointModelEditorPart());
		final RotateSlotsAction rotate = new RotateSlotsAction(getJointModelEditorPart());
		viewer.addSelectionChangedListener(rotate);
		viewer.addSelectionChangedListener(rewire);
		getToolBarManager().appendToGroup("edit", rewire);
		getToolBarManager().appendToGroup("edit", rotate);
		getToolBarManager().update(true);
		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;
		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = part.getReferenceValueProviderCache();
		final EditingDomain editingDomain = part.getEditingDomain();

		addTypicalColumn("ID", new BasicAttributeManipulator(mmx.getNamedObject_Name(), editingDomain));

		addTypicalColumn("Type ", new BasicOperationRenderer(pkg.getCargo__GetCargoType(), editingDomain));

		addTypicalColumn("Load Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_LoadSlot());

		addTypicalColumn("Load Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), pkg.getCargo_LoadSlot());

		addTypicalColumn("Load Contract", new SingleReferenceManipulator(pkg.getSlot_Contract(), provider, editingDomain), pkg.getCargo_LoadSlot());

		addTypicalColumn("Discharge Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_DischargeSlot());

		addTypicalColumn("Discharge Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), pkg.getCargo_DischargeSlot());

		addTypicalColumn("Discharge Contract", new SingleReferenceManipulator(pkg.getSlot_Contract(), provider, editingDomain), pkg.getCargo_DischargeSlot());

		final InputModel input = part.getRootObject().getSubModel(InputModel.class);
		
		if (input != null) {
			addTypicalColumn("Assignment", new AssignmentManipulator(part));
		}
		
		getToolBarManager().appendToGroup(EDIT_GROUP, new Action() {
			{
				setImageDescriptor(
						AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/group.gif"));
			}
			@Override
			public void run() {
				final DetailCompositeDialog dcd = new DetailCompositeDialog(CargoModelViewer.this.getJointModelEditorPart().getShell(), CargoModelViewer.this.getJointModelEditorPart().getDefaultCommandHandler());
				dcd.open(getJointModelEditorPart(), getJointModelEditorPart().getRootObject(), (EObject) viewer.getInput(), CargoPackage.eINSTANCE.getCargoModel_CargoGroups());
			}
		});
		getToolBarManager().update(true);
		
		setTitle("Cargoes", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	@Override
	protected Action createImportAction() {
		return new SimpleImportAction(part, getScenarioViewer()) {
			@Override
			protected Command mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {
				final List<EObject> cargoes = new ArrayList<EObject>();
				final List<EObject> loads = new ArrayList<EObject>();
				final List<EObject> discharges = new ArrayList<EObject>();

				for (final EObject o : imports) {
					if (o instanceof Cargo)
						cargoes.add(o);
					else if (o instanceof LoadSlot)
						loads.add(o);
					else if (o instanceof DischargeSlot)
						discharges.add(o);
				}

				final CompoundCommand mergeAll = new CompoundCommand();
				mergeAll.append(super.mergeImports(container, containment, cargoes));
				mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loads));
				mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), discharges));
				return mergeAll;
			}
		};
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		ScenarioTableViewer scenarioTableViewer = new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart()) {

			@Override
			protected EObject getElementForNotificationTarget(final EObject source) {
				if (source instanceof LoadSlot) {
					return ((LoadSlot) source).getCargo();
				} else if (source instanceof DischargeSlot) {
					return ((DischargeSlot) source).getCargo();
				}

				return super.getElementForNotificationTarget(source);
			}

		};
		scenarioTableViewer.setColourProvider(new IColorProvider() {

			@Override
			public Color getForeground(Object element) {
				return null;
			}

			@Override
			public Color getBackground(Object element) {

				if (element instanceof Cargo) {
					CargoType cargoType = ((Cargo) element).getCargoType();
					switch (cargoType) {
					case DES:
						return desCargo;
					case FLEET:
						break;
					case FOB:
						return fobCargo;
					}
				}
				return null;
			}

		});
		return scenarioTableViewer;

	}

	@Override
	public void dispose() {

		fobCargo.dispose();
		desCargo.dispose();

		super.dispose();
	}
	
	class AssignmentManipulator implements ICellRenderer, ICellManipulator {
		private IScenarioEditingLocation location;
		private IReferenceValueProvider valueProvider;
		private List<Pair<String, EObject>> allowedValues;
		private List<EObject> vessels = new ArrayList<EObject>();

		public AssignmentManipulator(final IScenarioEditingLocation location) {
			this.location = location;
			this.valueProvider = location.getReferenceValueProviderCache().getReferenceValueProvider(InputPackage.eINSTANCE.getAssignment(), InputPackage.eINSTANCE.getAssignment_Vessels());
			getValues();
		}
		
		private void getValues() {
			allowedValues = valueProvider.getAllowedValues(null, InputPackage.eINSTANCE.getAssignment_Vessels());
			vessels.clear();
			for (final Pair<String, EObject> p : allowedValues) vessels.add(p.getSecond());
		}
		
		@Override
		public void setValue(Object object, Object value) {
			// grar.
			final InputModel input = location.getRootObject().getSubModel(InputModel.class);
			if (input != null) {
				if (value == null || value.equals(-1)) return;
				final AVesselSet set = (AVesselSet) vessels.get((Integer) value);
				Assignment newAssignment;
				if (set instanceof AVessel) {
					// find vessel
					newAssignment = AssignmentEditorHelper.getAssignmentForVessel(input, set);
					if (newAssignment == null) {
						newAssignment = InputFactory.eINSTANCE.createAssignment();
						newAssignment.getVessels().add(set);
						newAssignment.getAssignedObjects().add((UUIDObject) object);
						location.getEditingDomain().getCommandStack().execute(
								AddCommand.create(location.getEditingDomain(),
										input, InputPackage.eINSTANCE.getInputModel_Assignments(), newAssignment));
						return;
					} else {
						location.getEditingDomain().getCommandStack().execute(
								AssignmentEditorHelper.taskReassigned(getEditingDomain(), input, (UUIDObject) object, null, null, AssignmentEditorHelper.getAssignmentForTask(input, (UUIDObject) object),
										newAssignment
										));
					}
				} else if (set instanceof AVesselClass) {
					// add to spot
					newAssignment = InputFactory.eINSTANCE.createAssignment();
					newAssignment.getVessels().add(set);
					newAssignment.setAssignToSpot(true);
					newAssignment.getAssignedObjects().add((UUIDObject) object);
					location.getEditingDomain().getCommandStack().execute(
							AddCommand.create(location.getEditingDomain(),
									input, InputPackage.eINSTANCE.getInputModel_Assignments(), newAssignment));
					return;
				}
				
			}
		}

		@Override
		public CellEditor getCellEditor(Composite parent, Object object) {
			getValues();
			final String[] items = new String[allowedValues.size()-1];
			for (int i = 0; i<items.length; i++) {
				items[i] = allowedValues.get(i).getFirst();
			}
			return new ComboBoxCellEditor(parent, items);
		}

		@Override
		public Integer getValue(final Object object) {
			if (object instanceof Cargo) {
				final Cargo cargo = (Cargo) object;
				
				final InputModel input = location.getRootObject().getSubModel(InputModel.class);
				if (input != null) {
					for (final Assignment assignment : input.getAssignments()) {
						if (assignment.getAssignedObjects().contains(cargo)) {
							return vessels.indexOf(assignment.getVessels().iterator().next());
						}
					}
				}
			}
			
			return null;
		}

		@Override
		public boolean canEdit(final Object object) {
			return object instanceof Cargo;
		}

		@Override
		public String render(final Object object) {
			Integer value = getValue(object);
			if (value == null || value == -1) return "";
			return allowedValues.get(value).getFirst();
		}

		@Override
		public Comparable getComparable(final Object object) {
			return render(object);
		}

		@Override
		public Object getFilterValue(final Object object) {
			return render(object);
		}

		@Override
		public Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(final Object object) {
			return Collections.singleton(new Pair<Notifier, List<Object>>(location.getRootObject().getSubModel(InputModel.class), Collections.emptyList()));
		}
	}
}
