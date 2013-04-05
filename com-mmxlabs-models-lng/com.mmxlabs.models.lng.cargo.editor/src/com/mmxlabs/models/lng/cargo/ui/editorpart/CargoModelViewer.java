/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EStoreEObjectImpl.EStoreFeatureMap;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.actions.DeleteSelectedCargoAction;
import com.mmxlabs.models.lng.cargo.ui.actions.RotateSlotsAction;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.BasicOperationRenderer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class CargoModelViewer extends ScenarioTableViewerPane {
	private final IScenarioEditingLocation part;

	// TODO: Make these colours a preference so they can be consistently used across various UI parts
	private final Color desCargo = CargoEditorPlugin.getPlugin().getColor(CargoEditorPlugin.COLOR_CARGO_DES);
	private final Color fobCargo = CargoEditorPlugin.getPlugin().getColor(CargoEditorPlugin.COLOR_CARGO_FOB);

	private final Image wiredImage;
	private final Image lockedImage;

	public CargoModelViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.part = location;

		wiredImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LINK);
		lockedImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LOCK);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		// final RewireAction rewireAction = new RewireAction(getJointModelEditorPart());
		final RotateSlotsAction rotate = new RotateSlotsAction(getJointModelEditorPart());
		viewer.addSelectionChangedListener(rotate);
		// viewer.addSelectionChangedListener(rewireAction);
		// getToolBarManager().appendToGroup("edit", rewireAction);
		getToolBarManager().appendToGroup("edit", rotate);
		getToolBarManager().update(true);
		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;
		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = part.getReferenceValueProviderCache();
		final EditingDomain editingDomain = part.getEditingDomain();

		final GridViewerColumn state = getScenarioViewer().addSimpleColumn("", false);
		state.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {

			@Override
			public String getText(final Object element) {
				return null;
			}

			@Override
			public Image getImage(final Object element) {

				if (element instanceof Cargo) {
					final Cargo cargo = (Cargo) element;
					final InputModel inputModel = part.getRootObject().getSubModel(InputModel.class);
					final ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(inputModel, cargo);
					if (assignment != null && assignment.isLocked()) {
						return lockedImage;
					} else if (!cargo.isAllowRewiring()) {
						return wiredImage;

					}
				}

				return super.getImage(element);
			}
		});

		addTypicalColumn("ID", new BasicAttributeManipulator(mmx.getNamedObject_Name(), editingDomain));

		addTypicalColumn("Type ", new BasicOperationRenderer(pkg.getCargo__GetCargoType(), editingDomain));

		addTypicalColumn("Load Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_LoadSlot());

		addTypicalColumn("Load Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), pkg.getCargo_LoadSlot());

		addTypicalColumn("Buy at", new ContractManipulator(provider, editingDomain), pkg.getCargo_LoadSlot());

		addTypicalColumn("Discharge Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), pkg.getCargo_DischargeSlot());

		addTypicalColumn("Discharge Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), pkg.getCargo_DischargeSlot());

		addTypicalColumn("Sell at", new ContractManipulator(provider, editingDomain), pkg.getCargo_DischargeSlot());

		final InputModel input = part.getRootObject().getSubModel(InputModel.class);

		if (input != null) {
			addTypicalColumn("Vessel", new AssignmentManipulator(part));
		}

		// Add action to create and edit cargo groups
		getToolBarManager().appendToGroup(EDIT_GROUP, new Action() {
			{
				setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/group.gif"));
			}

			@Override
			public void run() {
				final DetailCompositeDialog dcd = new DetailCompositeDialog(CargoModelViewer.this.getJointModelEditorPart().getShell(), CargoModelViewer.this.getJointModelEditorPart()
						.getDefaultCommandHandler());
				dcd.open(getJointModelEditorPart(), getJointModelEditorPart().getRootObject(), (EObject) viewer.getInput(), CargoPackage.eINSTANCE.getCargoModel_CargoGroups());
			}
		});
		getToolBarManager().update(true);

		setTitle("Cargoes", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		// IElementComparer to handle selection objects from e.g. schedule
		((GridTableViewer) viewer).setComparer(new IElementComparer() {

			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(final Object a, final Object b) {

				final Cargo c1 = getCargo(a);
				final Cargo c2 = getCargo(b);

				return Equality.isEqual(c1, c2);
			}

			private Cargo getCargo(final Object o) {

				if (o instanceof Cargo) {
					return (Cargo) o;
				}
				if (o instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) o;
					return cargoAllocation.getInputCargo();
				}
				if (o instanceof LoadSlot) {
					return ((LoadSlot) o).getCargo();
				}
				if (o instanceof DischargeSlot) {
					return ((DischargeSlot) o).getCargo();
				}
				if (o instanceof SlotVisit) {
					final SlotAllocation slotAllocation = ((SlotVisit) o).getSlotAllocation();
					if (slotAllocation != null) {
						return getCargo(slotAllocation.getSlot());
					}
				}

				if (o instanceof Event) {
					Event evt = (Event) o;
					while (evt != null) {
						if (evt instanceof VesselEventVisit) {
							return null;
						} else if (evt instanceof GeneratedCharterOut) {
							return null;
						} else if (evt instanceof SlotVisit) {
							return getCargo(evt);
						}
						evt = evt.getPreviousEvent();
					}
				}
				return null;
			}
		});
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
					if (o instanceof Cargo) {
						final Cargo cargo = (Cargo) o;
						// Filter out broken cargoes - those with less than two slots
						if (cargo.getLoadSlot() == null || cargo.getDischargeSlot() == null) {
							if (cargo.getLoadSlot() != null) {
								cargo.getLoadSlot().setCargo(null);
							}
							if (cargo.getDischargeSlot() != null) {
								cargo.getDischargeSlot().setCargo(null);
							}
							// EOpposite should have already done this - but just to be sure
							cargo.setLoadSlot(null);
							cargo.setDischargeSlot(null);
						} else {
							cargoes.add(cargo);
						}

					} else if (o instanceof LoadSlot) {
						loads.add(o);
					} else if (o instanceof DischargeSlot) {
						discharges.add(o);
					}
				}

				final CompoundCommand mergeAll = new CompoundCommand();

				// mergeAll.append(super.mergeImports(container, containment, cargoes));
				mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loads));
				mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), discharges));

				mergeAll.append(mergeCargoes((CargoModel) container, cargoes, loads, discharges));

				return mergeAll;
			}

			/**
			 * This method handles the Cargo update. This includes merging Cargo record updates, re-wiring of slots and removing cargoes which are no longer valid
			 * 
			 * @param container
			 * @param containment
			 * @param cargoes
			 * @param loads
			 * @param discharges
			 * @return
			 */
			private Command mergeCargoes(final CargoModel cargoModel, final List<EObject> newCargoes, final List<EObject> newLoads, final List<EObject> newDischarges) {
				final EditingDomain domain = part.getEditingDomain();
				final CompoundCommand mergeCommand = new CompoundCommand();

				// Build up a count of the number of slots each cargo has. Build a map of slot ID to cargo
				final Map<Cargo, Integer> cargoSlotCount = new HashMap<Cargo, Integer>();
				final Map<String, Cargo> existingLoadSlotMap = new HashMap<String, Cargo>();
				final Map<String, Cargo> existingCargoMap = new HashMap<String, Cargo>();
				final Map<String, Cargo> existingDischargeSlotMap = new HashMap<String, Cargo>();
				for (final Cargo c : cargoModel.getCargoes()) {

					existingCargoMap.put(c.getName(), c);

					int count = 0;
					if (c.getLoadSlot() != null) {
						++count;
						existingLoadSlotMap.put(c.getLoadSlot().getName(), c);
					}
					if (c.getDischargeSlot() != null) {
						++count;
						existingLoadSlotMap.put(c.getDischargeSlot().getName(), c);
					}
					cargoSlotCount.put(c, count);
				}

				for (final EObject newECargo : newCargoes) {
					final Cargo newCargo = (Cargo) newECargo;

					// Existing cargo to be updated
					if (existingCargoMap.containsKey(newCargo.getName())) {
						final Cargo existingCargo = existingCargoMap.get(newCargo.getName());
						// Merge fields
						{
							// TODO: MERGE CARGO RECORDS
							for (final EStructuralFeature feature : newCargo.eClass().getEAllStructuralFeatures()) {
								// Skip UUID to preserve original value
								if (newCargo.eIsSet(feature)) {
									if (feature != MMXCorePackage.eINSTANCE.getUUIDObject_Uuid() && feature != CargoPackage.eINSTANCE.getCargo_LoadSlot()
											&& feature != CargoPackage.eINSTANCE.getCargo_DischargeSlot()) {
										mergeCommand.append(SetCommand.create(domain, existingCargo, feature, newCargo.eGet(feature)));
										// c.eSet(feature, newCargo.eGet(feature));
									}
								}
							}

						}
						if (existingLoadSlotMap.containsKey(newCargo.getLoadSlot().getName())) {
							final Cargo oldCargo = existingLoadSlotMap.get(newCargo.getLoadSlot().getName());
							if (!oldCargo.getName().equals(newCargo.getName())) {
								// Re-wired!
								{
									// Decrement existing count
									final int count = cargoSlotCount.get(oldCargo);
									cargoSlotCount.put(oldCargo, count - 1);
								}
								{
									// Increment current cargo counter
									final int count = cargoSlotCount.get(existingCargo);
									cargoSlotCount.put(existingCargo, count + 1);
								}
							} else {
								// Same wiring - no change
							}
						}
						mergeCommand.append(SetCommand.create(domain, existingCargo, CargoPackage.eINSTANCE.getCargo_LoadSlot(), newCargo.getLoadSlot()));

						if (existingDischargeSlotMap.containsKey(newCargo.getDischargeSlot().getName())) {
							final Cargo oldCargo = existingDischargeSlotMap.get(newCargo.getDischargeSlot().getName());
							if (!oldCargo.getName().equals(newCargo.getName())) {
								// Re-wired!
								{
									// Decrement existing count
									final int count = cargoSlotCount.get(oldCargo);
									cargoSlotCount.put(oldCargo, count - 1);
								}
								{
									// Increment current cargo counter
									final int count = cargoSlotCount.get(existingCargo);
									cargoSlotCount.put(existingCargo, count + 1);
								}
							} else {
								// Same wiring - no change
							}
						} else {

						}
						mergeCommand.append(SetCommand.create(domain, existingCargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), newCargo.getDischargeSlot()));
					} else {
						// New Cargo, but check for re-wiring of slots
						mergeCommand.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));

						// Check re-wiring
						if (existingLoadSlotMap.containsKey(newCargo.getLoadSlot().getName())) {
							final Cargo oldCargo = existingLoadSlotMap.get(newCargo.getLoadSlot().getName());
							// Decrement existing count
							final int count = cargoSlotCount.get(oldCargo);
							cargoSlotCount.put(oldCargo, count - 1);
							mergeCommand.append(SetCommand.create(domain, oldCargo, CargoPackage.eINSTANCE.getCargo_LoadSlot(), newCargo.getLoadSlot()));
						}

						if (existingDischargeSlotMap.containsKey(newCargo.getDischargeSlot().getName())) {
							final Cargo oldCargo = existingDischargeSlotMap.get(newCargo.getDischargeSlot().getName());
							// Decrement existing count
							final int count = cargoSlotCount.get(oldCargo);
							cargoSlotCount.put(oldCargo, count - 1);
							mergeCommand.append(SetCommand.create(domain, oldCargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), newCargo.getDischargeSlot()));
						}
					}
				}

				for (final Map.Entry<Cargo, Integer> e : cargoSlotCount.entrySet()) {
					final int count = e.getValue();
					if (count < 2) {
						mergeCommand.append(DeleteCommand.create(domain, e.getKey()));
					}
					if (count > 2) {
						throw new RuntimeException("Cargo has more than two slots!");
					}
				}

				return mergeCommand;
			}
		};
	}

	@Override
	protected Action createDeleteAction() {
		return new DeleteSelectedCargoAction(jointModelEditorPart, viewer);
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		final ScenarioTableViewer scenarioTableViewer = new ScenarioTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart()) {

			@Override
			public EObject getElementForNotificationTarget(final EObject source) {
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
			public Color getForeground(final Object element) {
				return null;
			}

			@Override
			public Color getBackground(final Object element) {

				if (element instanceof Cargo) {
					final CargoType cargoType = ((Cargo) element).getCargoType();
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
}
