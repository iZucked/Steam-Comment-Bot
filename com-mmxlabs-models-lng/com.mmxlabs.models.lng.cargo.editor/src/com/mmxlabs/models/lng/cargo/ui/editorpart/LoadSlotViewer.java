/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.MultiDetailDialog;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class LoadSlotViewer extends ScenarioTableViewerPane {
	private final IScenarioEditingLocation part;

	// TODO: Make these colours a preference so they can be consistently used across various UI parts
	private final Color desCargo = new Color(Display.getDefault(), 150, 210, 230);

	public LoadSlotViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.part = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);

		final MMXCorePackage mmx = MMXCorePackage.eINSTANCE;
		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = part.getReferenceValueProviderCache();
		final EditingDomain editingDomain = part.getEditingDomain();

		addTypicalColumn("ID", new BasicAttributeManipulator(mmx.getNamedObject_Name(), editingDomain));

		// addTypicalColumn("Type ", new BasicOperationRenderer(pkg.getCargo__GetCargoType(), editingDomain));

		addTypicalColumn("Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain));

		addTypicalColumn("Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain));

		addTypicalColumn("Contract", new SingleReferenceManipulator(pkg.getSlot_Contract(), provider, editingDomain));
		addTypicalColumn("Cargo ID", new BasicAttributeManipulator(mmx.getNamedObject_Name(), editingDomain), pkg.getLoadSlot_Cargo());
		addTypicalColumn("Optional", new BooleanAttributeManipulator(pkg.getSlot_Optional(), editingDomain));

		// final InputModel input = part.getRootObject().getSubModel(InputModel.class);

		// if (input != null) {
		// addTypicalColumn("Assignment", new AssignmentManipulator(part));
		// }

		setTitle("Load Options", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		// IElementComparer to handle selection objects from e.g. schedule
		((GridTableViewer) viewer).setComparer(new IElementComparer() {

			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(final Object a, final Object b) {

				final LoadSlot c1 = getLoadSlot(a);
				final LoadSlot c2 = getLoadSlot(b);

				return Equality.isEqual(c1, c2);
			}

			private LoadSlot getLoadSlot(final Object o) {

				if (o instanceof LoadSlot) {
					return (LoadSlot) o;
				}

				if (o instanceof SlotAllocation) {
					final SlotAllocation cargoAllocation = (SlotAllocation) o;
					return getLoadSlot(cargoAllocation.getSlot());
				}
				if (o instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) o;
					return getLoadSlot(cargoAllocation.getInputCargo());
				}
				if (o instanceof Cargo) {
					return ((Cargo) o).getLoadSlot();
				}
				if (o instanceof SlotVisit) {
					final SlotAllocation slotAllocation = ((SlotVisit) o).getSlotAllocation();
					if (slotAllocation != null) {
						return getLoadSlot(slotAllocation.getSlot());
					}
				}
				if (o instanceof Event) {
					Event evt = (Event) o;
					while (evt != null && !(evt instanceof SlotVisit)) {
						if (evt instanceof VesselEventVisit) {
							evt = null;
						} else if (evt instanceof GeneratedCharterOut) {
							evt = null;
						} else {
							evt = evt.getPreviousEvent();
						}
					}
					if (evt != null) {
						return getLoadSlot(evt);
					}
				}
				return null;
			}

		});
		final MenuManager menuManager = new MenuManager();
		menuManager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		super.part.getSite().registerContextMenu(menuManager, viewer);
		// getSite().setSelectionProvider(viewer);
		((GridTableViewer) viewer).getGrid().setMenu(menuManager.createContextMenu(((GridTableViewer) viewer).getGrid()));

		menuManager.add(new Action("Edit Cargo") {

			@Override
			public void run() {

				if (viewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) viewer.getSelection();
					if (structuredSelection.isEmpty() == false) {

						final List<EObject> selectedCargoes = new LinkedList<EObject>();
						final Iterator<?> itr = structuredSelection.iterator();
						while (itr.hasNext()) {
							final Object o = itr.next();
							if (o instanceof LoadSlot) {
								final LoadSlot slot = (LoadSlot) o;
								if (slot.getCargo() != null) {
									selectedCargoes.add(slot.getCargo());
								}
							}
						}

						if (structuredSelection.size() == 1) {
							final DetailCompositeDialog dcd = new DetailCompositeDialog(viewer.getControl().getShell(), part.getDefaultCommandHandler());
							try {
								part.getEditorLock().claim();
								part.setDisableUpdates(true);
								dcd.open(part, part.getRootObject(), selectedCargoes, getScenarioViewer().isLocked());
							} finally {
								part.setDisableUpdates(false);
								part.getEditorLock().release();
							}
						} else {
							try {
								part.getEditorLock().claim();
								if (getScenarioViewer().isLocked() == false) {
									final MultiDetailDialog mdd = new MultiDetailDialog(viewer.getControl().getShell(), part.getRootObject(), part.getDefaultCommandHandler());
									mdd.open(part, selectedCargoes);
								}
							} finally {
								part.getEditorLock().release();
							}
						}
					}
				}
				// TODO Auto-generated method stub
				super.run();
			}
		});
	}

	// @Override
	// protected Action createImportAction() {
	// return new SimpleImportAction(part, getScenarioViewer()) {
	// @Override
	// protected Commasnd mergeImports(final EObject container, final EReference containment, final Collection<EObject> imports) {
	// final List<EObject> cargoes = new ArrayList<EObject>();
	// final List<EObject> loads = new ArrayList<EObject>();
	// final List<EObject> discharges = new ArrayList<EObject>();
	//
	// for (final EObject o : imports) {
	// if (o instanceof Cargo)
	// cargoes.add(o);
	// else if (o instanceof LoadSlot)
	// loads.add(o);
	// else if (o instanceof DischargeSlot)
	// discharges.add(o);
	// }
	//
	// final CompoundCommand mergeAll = new CompoundCommand();
	// mergeAll.append(super.mergeImports(container, containment, cargoes));
	// mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), loads));
	// mergeAll.append(super.mergeImports(container, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), discharges));
	// return mergeAll;
	// }
	// };
	// }

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		final ScenarioTableViewer scenarioTableViewer = new ScenarioTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, getJointModelEditorPart()) {

			@Override
			public EObject getElementForNotificationTarget(final EObject source) {
				if (source instanceof Cargo) {
					return ((Cargo) source).getLoadSlot();
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

				if (element instanceof LoadSlot) {
					if (((LoadSlot) element).isDESPurchase()) {
						return desCargo;
					}
				}
				return null;
			}

		});
		return scenarioTableViewer;

	}

	@Override
	public void dispose() {

		desCargo.dispose();

		super.dispose();
	}

}
