/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.ui.editorpart.events.IVesselEventsTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.ui.editorpart.events.VesselEventsContextMenuExtensionUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class VesselEventViewerPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;
	private Iterable<IVesselEventsTableContextMenuExtension> contextMenuExtensions;

	public VesselEventViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("Type", new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof CharterOutEvent) {
					return "Charter Out";
				} else if (object instanceof DryDockEvent) {
					return "Dry Dock";
				} else if (object instanceof MaintenanceEvent) {
					return "Maintenance Event";
				} else {
					return "Unknown Event";
				}
			}
		});

		addTypicalColumn("Name", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Start after", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselEvent_StartAfter(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Start by", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselEvent_StartBy(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Port", new SingleReferenceManipulator(CargoPackage.eINSTANCE.getVesselEvent_Port(), jointModelEditor.getReferenceValueProviderCache(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Duration", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getVesselEvent_DurationInDays(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Vessel", new AssignmentManipulator(jointModelEditor));
		addTypicalColumn("Allowed Vessels", new VesselEventVesselsManipulator(CargoPackage.eINSTANCE.getVesselEvent_AllowedVessels(), jointModelEditor.getReferenceValueProviderCache(),
				jointModelEditor.getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		setTitle("Vessel Events", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		final MenuManager mgr = new MenuManager();

		contextMenuExtensions = VesselEventsContextMenuExtensionUtil.getContextMenuExtensions();

		scenarioViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				// if (locked) {
				// return;
				// }

				final IStructuredSelection selection = (IStructuredSelection) getScenarioViewer().getSelection();

				if (selection.size() > 0) {
					if (menu == null) {
						menu = mgr.createContextMenu(scenarioViewer.getGrid());
					}
					mgr.removeAll();
					{
						IContributionItem[] mItems = mgr.getItems();
						mgr.removeAll();
						for (IContributionItem item : mItems) {
							item.dispose();
						}
					}

					if (contextMenuExtensions != null) {
						for (final IVesselEventsTableContextMenuExtension ext : contextMenuExtensions) {
							ext.contributeToMenu(scenarioEditingLocation, selection, mgr);
						}
					}

					menu.setVisible(true);
				}
			}

		});

		// IElementComparer to handle selection objects from e.g. schedule
		((StructuredViewer) viewer).setComparer(new IElementComparer() {

			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(final Object a, final Object b) {

				final VesselEvent c1 = getVesselEvent(a);
				final VesselEvent c2 = getVesselEvent(b);

				return Equality.isEqual(c1, c2);
			}

			private VesselEvent getVesselEvent(final Object o) {

				if (o instanceof VesselEvent) {
					return (VesselEvent) o;
				}
				if (o instanceof VesselEventVisit) {
					VesselEventVisit vesselEventVisit = (VesselEventVisit) o;
					return vesselEventVisit.getVesselEvent();
				}
				if (o instanceof Event) {
					Event evt = (Event) o;
					while (evt != null) {
						if (evt instanceof VesselEventVisit) {
							return getVesselEvent(evt);
						} else if (evt instanceof SlotVisit) {
							return null;
						} else if (evt instanceof GeneratedCharterOut) {
							return null;
						}
						evt = evt.getPreviousEvent();
					}
				}
				return null;
			}
		});

		final DragSource source = new DragSource(getScenarioViewer().getControl(), DND.DROP_MOVE);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new BasicDragSource(viewer));
	}
}
