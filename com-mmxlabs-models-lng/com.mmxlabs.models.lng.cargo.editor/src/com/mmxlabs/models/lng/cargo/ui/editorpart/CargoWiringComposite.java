/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPartSite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.dialogs.WiringDiagram;
import com.mmxlabs.models.lng.cargo.ui.util.SpotSlotHelper;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.dates.LocalDateUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.validation.IDetailConstraintStatus;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

/**
 * A composite for displaying a wiring editor. Contains a {@link WiringDiagram}, which lets the user view and edit a matching in a bipartite graph, and provides the interfacing logic to apply a new
 * matching to the cargoes passed in through {@link #setCargoes(List)}.
 * 
 * 
 * @author Tom Hinton, Simon Goodall
 * 
 */
public class CargoWiringComposite extends Composite {
	final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();

	private IScenarioEditingLocation location;

	private boolean locked = false;

	private final Object updateLock = new Object();

	private final MMXAdapterImpl cargoChangeAdapter = new MMXAdapterImpl() {

		protected void missedNotifications(final List<Notification> missed) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					if (isDisposed()) {
						return;
					}
					synchronized (updateLock) {
						refreshContent();
					}
				}
			});
		}

		@Override
		public synchronized void reallyNotifyChanged(final Notification notification) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					synchronized (updateLock) {
						localNotifyChanged(notification);
						performControlUpdate(false);

					}
				}
			});
		}

		public synchronized void localNotifyChanged(final Notification notification) {

			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}

			boolean rowAdded = false;
			boolean performUpdate = false;

			if (notification.getNotifier() instanceof Cargo) {
				final Cargo cargo = (Cargo) notification.getNotifier();
				// Check cargo wiring
				if (notification.getFeature() == CargoPackage.eINSTANCE.getCargo_LoadSlot()) {
					final Cargo c = (Cargo) notification.getNotifier();
					final LoadSlot loadSlot = (LoadSlot) notification.getNewValue();

					final DischargeSlot dischargeSlot = c.getDischargeSlot();

					final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
					if (ret == 2) {
						rowAdded = true;
					} else if (ret == 1) {
						performUpdate = true;
					}

					if (loadSlot == null) {
						final LoadSlot oldLoadSlot = (LoadSlot) notification.getOldValue();
						if (loadSlots.contains(oldLoadSlot)) {
							final int loadIdx = loadSlots.indexOf(oldLoadSlot);
							wiring.set(loadIdx, -1);
							cargoes.set(loadIdx, null);
							performUpdate = true;
						}
					}

				} else if (notification.getFeature() == CargoPackage.eINSTANCE.getCargo_DischargeSlot()) {
					final Cargo c = (Cargo) notification.getNotifier();
					final LoadSlot loadSlot = c.getLoadSlot();
					final DischargeSlot dischargeSlot = (DischargeSlot) notification.getNewValue();

					final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
					if (ret == 2) {
						rowAdded = true;
					} else if (ret == 1) {
						performUpdate = true;
					}
				}
			} else if (notification.getNotifier() instanceof CargoModel) {
				if (notification.getFeature() == CargoPackage.eINSTANCE.getCargoModel_Cargoes()) {
					if (notification.getEventType() == Notification.ADD) {
						final Cargo cargo = (Cargo) notification.getNewValue();
						cargo.eAdapters().add(cargoChangeAdapter);
						final LoadSlot loadSlot = cargo.getLoadSlot();
						final DischargeSlot dischargeSlot = cargo.getDischargeSlot();

						final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
						if (ret == 2) {
							rowAdded = true;
						} else if (ret == 1) {
							performUpdate = true;
						}

					} else if (notification.getEventType() == Notification.REMOVE) {
						final Cargo cargo = (Cargo) notification.getOldValue();
						final LoadSlot loadSlot = cargo.getLoadSlot();
						final DischargeSlot dischargeSlot = cargo.getDischargeSlot();

						final int ret = performCargoUpdate(null, loadSlot, dischargeSlot);
						if (ret == 2) {
							rowAdded = true;
						} else if (ret == 1) {
							performUpdate = true;
						}
					}
				} else if (notification.getFeature() == CargoPackage.eINSTANCE.getCargoModel_LoadSlots()) {
					if (notification.getEventType() == Notification.ADD) {
						final LoadSlot loadSlot = (LoadSlot) notification.getNewValue();
						final Cargo cargo = loadSlot.getCargo();
						final DischargeSlot dischargeSlot = cargo != null ? cargo.getDischargeSlot() : null;

						final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
						if (ret == 2) {
							rowAdded = true;
						} else if (ret == 1) {
							performUpdate = true;
						}
					} else if (notification.getEventType() == Notification.REMOVE) {
						final LoadSlot slot = (LoadSlot) notification.getOldValue();
						if (loadSlots.contains(slot)) {
							// Remove slot
							final int index = loadSlots.indexOf(slot);
							loadSlots.set(index, null);
							// Remove the wiring
							wiring.set(index, -1);
							// Probably be handled later - but to be safe...
							cargoes.set(index, null);
							performUpdate = true;
						}
					}
				} else if (notification.getFeature() == CargoPackage.eINSTANCE.getCargoModel_DischargeSlots()) {
					if (notification.getEventType() == Notification.ADD) {
						final DischargeSlot dischargeSlot = (DischargeSlot) notification.getNewValue();
						final Cargo cargo = dischargeSlot.getCargo();
						final LoadSlot loadSlot = cargo != null ? cargo.getLoadSlot() : null;

						final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
						if (ret == 2) {
							rowAdded = true;
						} else if (ret == 1) {
							performUpdate = true;
						}
					} else if (notification.getEventType() == Notification.REMOVE) {
						final DischargeSlot slot = (DischargeSlot) notification.getOldValue();
						if (dischargeSlots.contains(slot)) {
							// Remove slot
							final int index = dischargeSlots.indexOf(slot);
							dischargeSlots.set(index, null);
							// Remove the wiring
							final int wiringIndex = wiring.indexOf(index);
							if (wiringIndex != -1) {
								wiring.set(wiringIndex, -1);
								// Probably be handled later - but to be safe...
								cargoes.set(wiringIndex, null);
							}
							performUpdate = true;
						}
					}
				}
			}

			// Perform a prune of empty rows
			boolean rowRemoved = false;
			for (int i = numberOfRows - 1; i >= 0; --i) {
				if (loadSlots.get(i) == null && dischargeSlots.get(i) == null) {
					cargoes.remove(i);
					loadSlots.remove(i);
					dischargeSlots.remove(i);

					// Re-index wiring
					for (int j = 0; j < numberOfRows; ++j) {
						final int idx = wiring.get(j);
						if (idx > i) {
							wiring.set(j, idx - 1);
						}
					}

					wiring.remove(i);
					numberOfRows--;
					performUpdate = true;
					rowRemoved = true;
				}
			}

			if (rowAdded) {

				if (wiring.get(numberOfRows) == null) {
					wiring.set(numberOfRows, -1);
				}
				numberOfRows++;

				// performControlUpdate(true);
			} else if (rowRemoved) {

				// performControlUpdate(true);
			} else if (performUpdate) {
				wiringDiagram.setWiring(wiring);

				// performControlUpdate(false);
			}

		}
	};

	private int performCargoUpdate(final Cargo cargo, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {

		int loadIdx = -1;
		int dischargeIdx = -1;
		boolean performUpdate = false;
		boolean rowAdded = false;
		if (loadSlot != null) {
			if (loadSlots.contains(loadSlot)) {
				loadIdx = loadSlots.indexOf(loadSlot);
			} else {

				// If the load slot next to the discharge is empty, insert the load here rather than append to the end.
				if (dischargeSlot != null && dischargeSlots.indexOf(dischargeSlot) != -1 && loadSlots.get(dischargeSlots.indexOf(dischargeSlot)) == null) {
					loadIdx = dischargeSlots.indexOf(dischargeSlot);
					loadSlots.set(loadIdx, loadSlot);
					performUpdate = true;
				} else {
					loadIdx = numberOfRows;
					ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
					wiring.set(numberOfRows, -1);

					loadSlots.set(loadIdx, loadSlot);
					rowAdded = true;
				}
			}
		} else if (cargo != null) {
			final int oldIndex = loadSlots.indexOf(cargo.getLoadSlot());
			if (oldIndex != -1) {
				wiring.set(oldIndex, -1);
				cargoes.set(oldIndex, null);
				performUpdate = true;
			}
		}

		if (dischargeSlot != null) {

			if (dischargeSlots.contains(dischargeSlot)) {
				dischargeIdx = dischargeSlots.indexOf(dischargeSlot);
			} else {

				// If the discharge slot for an existing load is empty, then attempt to re-use it
				if (!rowAdded && loadSlot != null && loadSlots.indexOf(loadSlot) != -1 && dischargeSlots.get(loadSlots.indexOf(loadSlot)) == null) {
					dischargeIdx = loadSlots.indexOf(loadSlot);
					dischargeSlots.set(dischargeIdx, dischargeSlot);
					performUpdate = true;
				} else {

					dischargeIdx = numberOfRows;
					ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);

					dischargeSlots.set(dischargeIdx, dischargeSlot);
					rowAdded = true;
				}
			}
		} else if (cargo != null) {
			final int oldIndex = loadSlots.indexOf(cargo.getLoadSlot());
			if (oldIndex != -1) {
				wiring.set(oldIndex, -1);
				cargoes.set(oldIndex, null);
				performUpdate = true;
			}
		}

		if (loadIdx != -1 && dischargeIdx != -1 && cargo != null) {
			cargoes.set(loadIdx, cargo);
			wiring.set(loadIdx, dischargeIdx);
			performUpdate = true;
		}

		if (cargo == null) {
			if (loadIdx != -1) {
				cargoes.set(loadIdx, null);
				wiring.set(loadIdx, -1);
				performUpdate = true;
			}
		}

		if (rowAdded) {
			return 2;
		} else if (performUpdate) {
			return 1;
		} else {
			return 0;
		}
	}

	private final ArrayList<Cargo> cargoes = new ArrayList<Cargo>();
	private final ArrayList<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
	private final ArrayList<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();

	private final ArrayList<Boolean> leftTerminalsExist = new ArrayList<Boolean>();
	private final ArrayList<Boolean> rightTerminalsExist = new ArrayList<Boolean>();
	/**
	 * The value of the ith element of wiring is the index of the other end of the wire; -1 indicates no wire is present.
	 * 
	 * There are more elements in here than in {@link #cargoes}, because of the extra terminals
	 */
	private final ArrayList<Integer> wiring = new ArrayList<Integer>();

	// final List<NamedObjectNameComposite> idComposites = new ArrayList<NamedObjectNameComposite>(cargoes.size());
	final List<PortAndDateComposite> lhsComposites = new ArrayList<PortAndDateComposite>(cargoes.size());
	final List<PortAndDateComposite> rhsComposites = new ArrayList<PortAndDateComposite>(cargoes.size());

	private int numberOfRows = 0;

	private final IWorkbenchPartSite site;

	// private final MenuManager menuManager;
	private final Set<EObject> validationMap = new HashSet<EObject>();

	protected IStatusChangedListener statusChangedListener = new IStatusChangedListener() {

		@Override
		public void onStatusChanged(final IStatusProvider provider, final IStatus status) {

			if (status == null || isDisposed()) {
				return;
			}

			validationMap.clear();

			checkStatus(status);

			for (final PortAndDateComposite c : lhsComposites) {
				c.displayValidationStatus(status);
			}
			for (final PortAndDateComposite c : rhsComposites) {
				c.displayValidationStatus(status);
			}
			// for (final NamedObjectNameComposite c : idComposites) {
			// c.displayValidationStatus(status);
			// }

			updateWiringColours(wiringDiagram, wiring, lhsComposites, rhsComposites);
			CargoWiringComposite.this.notifyListeners(SWT.Modify, new Event());

		}

		private void checkStatus(final IStatus status) {
			if (status.isMultiStatus()) {
				final IStatus[] children = status.getChildren();
				for (final IStatus childStatus : children) {
					checkStatus(childStatus);
				}
			}
			if (status instanceof IDetailConstraintStatus && status.getSeverity() == IStatus.ERROR) {
				final IDetailConstraintStatus element = (IDetailConstraintStatus) status;

				final Collection<EObject> objects = element.getObjects();
				for (final EObject object : objects) {
					if (cargoes.contains(object) || loadSlots.contains(object) || dischargeSlots.contains(object)) {
						validationMap.add(object);
					}
				}
			}
		}
	};

	WiringDiagram wiringDiagram = null;

	Listener selectionListener = new Listener() {
		@Override
		public void handleEvent(final Event event) {
			updateWiringColours(wiringDiagram, wiringDiagram.getWiring(), lhsComposites, rhsComposites);
		}
	};

	private final ICommandHandler commandHandler = new ICommandHandler() {

		@Override
		public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
			location.getEditingDomain().getCommandStack().execute(command);
		}

		@Override
		public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
			return location.getReferenceValueProviderCache();
		}

		@Override
		public EditingDomain getEditingDomain() {
			return location.getEditingDomain();
		}
	};

	CompoundCommand currentWiringCommand = null;

	/**
	 * @param parent
	 * @param style
	 */
	public CargoWiringComposite(final Composite parent, final int style, final IWorkbenchPartSite site) {
		super(parent, style);
		createLayout();

		this.site = site;
		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		// menuManager = new MenuManager("#PopupMenu");
		// site.registerContextMenu(menuManager, site.getSelectionProvider());
		// menuManager.setRemoveAllWhenShown(true);
		// final Menu m = menuManager.createContextMenu(this);

		// m.addListener(SWT.Show, new Listener() {
		//
		// @Override
		// public void handleEvent(Event event) {
		// m.setVisible(true);
		// }
		// });
		// this.setMenu(m);
	}

	@Override
	public void dispose() {
		if (this.location != null) {
			this.location.getStatusProvider().removeStatusChangedListener(statusChangedListener);

			final MMXRootObject rootObject = location.getRootObject();
			if (rootObject != null) {
				final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
				if (cargoModel != null) {
					cargoModel.eAdapters().remove(cargoChangeAdapter);
					for (final Cargo c : cargoModel.getCargoes()) {
						c.eAdapters().remove(cargoChangeAdapter);
					}
				}
			}

			for (final Cargo c : cargoes) {
				c.eAdapters().remove(cargoChangeAdapter);
			}

		}
		validationMap.clear();

		this.cargoes.clear();
		this.loadSlots.clear();
		this.dischargeSlots.clear();

		this.lhsComposites.clear();
		this.rhsComposites.clear();

		this.currentWiringCommand = null;
		this.rightTerminalsExist.clear();
		this.leftTerminalsExist.clear();
		this.wiring.clear();

		super.dispose();
	}

	/**
	 * Set the cargoes, and reset the wiring to match these cargoes.
	 * 
	 * @param newCargoes
	 */
	public void setCargoes(final List<Cargo> newCargoes, final List<LoadSlot> loadSlots, final List<DischargeSlot> dischargeSlots) {
		// delete existing children
		for (final Control c : getChildren()) {
			c.dispose();
		}
		lhsComposites.clear();
		rhsComposites.clear();
		wiringDiagram = null;
		wiring.clear();
		this.loadSlots.clear();
		this.dischargeSlots.clear();
		this.cargoes.clear();
		this.leftTerminalsExist.clear();
		this.rightTerminalsExist.clear();

		for (int i = 0; i < newCargoes.size(); i++) {

			this.wiring.add(i); // set default wiring
			this.cargoes.add(newCargoes.get(i));
			this.loadSlots.add(newCargoes.get(i).getLoadSlot());
			this.dischargeSlots.add(newCargoes.get(i).getDischargeSlot());
			this.leftTerminalsExist.add(true);
			this.rightTerminalsExist.add(true);
		}

		for (final LoadSlot slot : loadSlots) {
			if (slot.getCargo() == null) {
				this.cargoes.add(null);
				this.loadSlots.add(slot);
				this.dischargeSlots.add(null);
				this.wiring.add(-1);
				this.leftTerminalsExist.add(false);
				this.rightTerminalsExist.add(false);
			}
		}
		for (final DischargeSlot slot : dischargeSlots) {
			if (slot.getCargo() == null) {
				this.loadSlots.add(null);
				this.cargoes.add(null);
				this.dischargeSlots.add(slot);
				this.wiring.add(-1);
				this.leftTerminalsExist.add(false);
				this.rightTerminalsExist.add(false);
			}
		}

		numberOfRows = wiring.size();

		performControlUpdate(true);

		if (this.location != null && location.getStatusProvider() != null) {
			// Perform initial validation
			final IStatusProvider statusProvider = location.getStatusProvider();
			statusChangedListener.onStatusChanged(statusProvider, statusProvider.getStatus());
		}
	}

	public IScenarioEditingLocation getEditingLocation() {
		return location;
	}

	public void setLocation(final IScenarioEditingLocation location) {

		if (this.location != null) {
			this.location.getStatusProvider().removeStatusChangedListener(statusChangedListener);

			final MMXRootObject rootObject = location.getRootObject();
			if (rootObject != null) {
				final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
				if (cargoModel != null) {
					cargoModel.eAdapters().remove(cargoChangeAdapter);
					for (final Cargo c : cargoModel.getCargoes()) {
						c.eAdapters().remove(cargoChangeAdapter);
					}
				}
			}

		}

		this.location = location;
		if (this.location != null) {
			this.location.getStatusProvider().addStatusChangedListener(statusChangedListener);

			final MMXRootObject rootObject = location.getRootObject();
			if (rootObject != null) {
				final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
				if (cargoModel != null) {
					cargoModel.eAdapters().add(cargoChangeAdapter);
					for (final Cargo c : cargoModel.getCargoes()) {
						c.eAdapters().add(cargoChangeAdapter);
					}
				}
			}

		}
	}

	public void setLocked(final boolean locked) {

		if (isDisposed()) {
			return;
		}

		if (this.locked == locked) {
			return;
		}

		this.locked = locked;
		wiringDiagram.setLocked(locked);
		for (final PortAndDateComposite c : lhsComposites) {
			c.setEditorLocked(locked);
		}
		for (final PortAndDateComposite c : rhsComposites) {
			c.setEditorLocked(locked);
		}
		// for (final NamedObjectNameComposite c : idComposites) {
		// c.setEditorLocked(locked);
		// }
	}

	private void createLayout() {

		final GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginBottom = 0;
		gridLayout.marginTop = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginRight = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);
	}

	private void createChildren() {
		final boolean alwaysUpdate = false;
		// lhsComposites.clear();
		// rhsComposites.clear();
		leftTerminalsExist.clear();
		rightTerminalsExist.clear();
		// idComposites.clear();
		// if (wiringDiagram != null) {
		// wiringDiagram.dispose();
		// wiringDiagram = null;
		// }
		final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		for (int index = 0; index < numberOfRows; ++index) {

			// final NamedObjectNameComposite idComposite;
			// final boolean newIdComposite;
			// if (index < idComposites.size()) {
			// idComposite = idComposites.get(index);
			// newIdComposite = false;
			// } else {
			// newIdComposite = true;
			// idComposite = new NamedObjectNameComposite(this, SWT.NONE) {
			// @Override
			// public void addInlineEditor(final IInlineEditor editor) {
			// editors.add(editor);
			// editor.setCommandHandler(commandHandler);
			// final Control control = editor.createControl(this);
			// final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			// gd.widthHint = 60;
			// control.setLayoutData(gd);
			// control.setBackground(WHITE);
			// }
			// };
			// idComposite.setCommandHandler(commandHandler);
			// idComposites.add(idComposite);
			// final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			// // gd.widthHint = 70;
			// // gd.grabExcessHorizontalSpace = true;
			// // gd.grabExcessVerticalSpace = true;
			// idComposite.setLayoutData(gd);
			// idComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			// idComposite.setEnabled(!locked);
			// }
			//
			// if (alwaysUpdate || newIdComposite || idComposite.getObject() != cargoes.get(index)) {
			// idComposite.display(location, location.getRootObject(), cargoes.get(index), Collections.<EObject> emptyList());
			// }
			boolean newLoadSide = false;
			final PortAndDateComposite loadSide;
			if (index < lhsComposites.size()) {
				loadSide = lhsComposites.get(index);
				newLoadSide = false;
			} else {
				newLoadSide = true;
				loadSide = new PortAndDateComposite(this, SWT.NONE, site, true);
				final GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				loadSide.setLayoutData(gd2);
				loadSide.setBackground(WHITE);

				loadSide.setCommandHandler(commandHandler);

				loadSide.addMenuListener(createLoadSlotMenuListener(index));
				lhsComposites.add(loadSide);
				loadSide.addListener(SWT.Selection, selectionListener);
				loadSide.addListener(SWT.DefaultSelection, selectionListener);
				loadSide.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

				loadSide.setEnabled(!locked);
			}
			if (alwaysUpdate || newLoadSide || loadSide.getSlot() != loadSlots.get(index)) {
				loadSide.display(location, location.getRootObject(), loadSlots.get(index), Collections.<EObject> emptyList());
			}
			leftTerminalsExist.add(loadSlots.get(index) != null);

			if (wiringDiagram == null) {
				createWiringDiagram();
			}
			if (index == 0) {
				// wiring diagram is tall
				final GridData gd3 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, numberOfRows);
				gd3.widthHint = 90;
				gd3.minimumWidth = 90;
				wiringDiagram.setLayoutData(gd3);
				// wiringDiagram.setBackground(WHITE);
			}

			boolean newDischargeSide;
			final PortAndDateComposite dischargeSide;

			if (index < rhsComposites.size()) {
				dischargeSide = rhsComposites.get(index);
				newDischargeSide = false;
			} else {
				newDischargeSide = true;
				dischargeSide = new PortAndDateComposite(this, SWT.NONE, site, false);
				dischargeSide.setCommandHandler(commandHandler);
				dischargeSide.setBackground(WHITE);
				dischargeSide.addMenuListener(createDischargeSlotMenuListener(index));
				rhsComposites.add(dischargeSide);
				dischargeSide.addListener(SWT.Selection, selectionListener);
				dischargeSide.addListener(SWT.DefaultSelection, selectionListener);
				dischargeSide.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
				dischargeSide.setEnabled(!locked);
			}
			if (alwaysUpdate || newDischargeSide || dischargeSide.getSlot() != dischargeSlots.get(index)) {
				dischargeSide.display(location, location.getRootObject(), dischargeSlots.get(index), Collections.<EObject> emptyList());
			}
			rightTerminalsExist.add(dischargeSlots.get(index) != null);
		}

		// Dispose extra controls
		while (lhsComposites.size() > numberOfRows) {
			final Composite c = lhsComposites.remove(lhsComposites.size() - 1);
			c.dispose();
		}
		while (rhsComposites.size() > numberOfRows) {
			final Composite c = rhsComposites.remove(rhsComposites.size() - 1);
			c.dispose();
		}
		// while (idComposites.size() > numberOfRows) {
		// final Composite c = idComposites.remove(idComposites.size() - 1);
		// c.dispose();

		// }

		if (wiringDiagram == null) {
			createWiringDiagram();
		}

		wiringDiagram.setWiring(wiring);
		wiringDiagram.setTerminalsValid(leftTerminalsExist, rightTerminalsExist);
		final ArrayList<Pair<Color, Color>> terminalColors = new ArrayList<Pair<Color, Color>>();
		final ArrayList<Pair<Boolean, Boolean>> terminalOptionals = new ArrayList<Pair<Boolean, Boolean>>();
		final ArrayList<Color> wireColors = new ArrayList<Color>();
		final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

		for (int i = 0; i < numberOfRows; ++i) {
			terminalColors.add(new Pair<Color, Color>(green, green));
			terminalOptionals.add(new Pair<Boolean, Boolean>(false, false));
			wireColors.add(black);
		}

		final Color addColor = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);

		terminalColors.add(new Pair<Color, Color>(addColor, addColor));
		terminalColors.add(new Pair<Color, Color>(addColor, addColor));

		wiringDiagram.setWireColors(wireColors);
		wiringDiagram.setTerminalColors(terminalColors);
		wiringDiagram.setTerminalOptionals(terminalOptionals);
		updateWiringColours(wiringDiagram, wiring, lhsComposites, rhsComposites);
	}

	private void createWiringDiagram() {
		wiringDiagram = new WiringDiagram(this, SWT.NONE) {

			@Override
			public synchronized void paintControl(final PaintEvent e) {
				synchronized (updateLock) {
					super.paintControl(e);
				}
			}

			@Override
			protected synchronized List<Float> getTerminalPositions() {
				final float littleOffset = getBounds().y;
				final ArrayList<Float> vMidPoints = new ArrayList<Float>(numberOfRows);
				// get vertical coordinates of labels

				float lastMidpointButOne = 0;
				float lastMidpoint = 0;

				for (int i = 0; i < numberOfRows; ++i) {
					if (i >= lhsComposites.size()) {
						continue;
					}
					final Composite l = lhsComposites.get(i);
					final Rectangle lbounds = l.getBounds();
					lastMidpointButOne = lastMidpoint;
					lastMidpoint = -littleOffset + lbounds.y + lbounds.height / 2.0f;
					vMidPoints.add(lastMidpoint);
				}

				return vMidPoints;
			}

			@Override
			protected synchronized void wiringChanged(final List<Integer> newWiring) {
				doWiringChanged(newWiring);
			}

			@Override
			protected void openContextMenu(final boolean leftSide, final int terminal, final int mouseX, final int mouseY) {
				// if (terminal < numberOfRows) {
				// final IMenuListener menuListener;
				// if (leftSide) {
				// menuListener = createLoadSlotMenuListener(loadSlots.get(terminal));
				// } else {
				// menuListener = createDischargeSlotMenuListener(dischargeSlots.get(terminal));
				// }
				// // menuManager.addMenuListener(menuListener);
				//
				// final Menu menu = menuManager.getMenu();
				//
				// menu.setLocation(toDisplay(mouseX, mouseY));
				// System.out.println(mouseX + " -- " + mouseY);
				// menuManager.removeAll();
				// menuListener.menuAboutToShow(menuManager);
				// menuManager.updateAll(true);
				// ;
				// // Event event = new Event();
				// // event.type = SWT.Show;
				// // event.button = 3;
				// // menu.notifyListeners(SWT.Show, event);
				// // menu.setLocation(this.toDisplay(0,0));
				// menu.setVisible(true);
				//
				// // menuManager.removeMenuListener(menuListener);
				// }
				//
			}
		};
		wiringDiagram.setLocked(locked);

		// Hook in a listener to notify mouse events
		final WiringDiagramMouseListener listener = new WiringDiagramMouseListener();
		wiringDiagram.addMouseMoveListener(listener);
		wiringDiagram.addMouseListener(listener);
	}

	/*
	 * TODO highlight lines when mouseover labels TODO feedback validation state to containing dialog TODO generate command to apply changes.
	 */

	private synchronized void updateWiringColours(final WiringDiagram diagram, final List<Integer> wiring, final List<PortAndDateComposite> loads, final List<PortAndDateComposite> discharge) {
		final Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		final Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		final Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

		for (int i = 0; i < numberOfRows; ++i) {
			final LoadSlot loadSlot = loadSlots.get(i);
			final DischargeSlot dischargeSlot = dischargeSlots.get(i);
			if (loadSlot != null && loadSlot.isOptional()) {
				diagram.setLeftTerminalColor(i, green);
				diagram.setLeftTerminalOptional(i, true);
			} else {
				diagram.setLeftTerminalColor(i, red);
				diagram.setLeftTerminalOptional(i, false);
			}
			if (dischargeSlot != null && dischargeSlot.isOptional()) {
				diagram.setRightTerminalColor(i, green);
				diagram.setRightTerminalOptional(i, true);
			} else {
				diagram.setRightTerminalColor(i, red);
				diagram.setRightTerminalOptional(i, false);
			}
		}
		for (int i = 0; i < numberOfRows; i++) {
			final int j = wiring.get(i);
			if (j == -1) {
				continue;
			}

			final LoadSlot loadSlot = loadSlots.get(i);
			final DischargeSlot dischargeSlot = dischargeSlots.get(j);

			if (loadSlot == null) {
				continue;
			}
			if (dischargeSlot == null) {
				continue;
			}

			diagram.setLeftTerminalColor(i, green);
			diagram.setRightTerminalColor(j, green);

			// TODO this is not exactly proper validation.
			if (isWiringValid(cargoes.get(i), loadSlot, dischargeSlot)) {
				if (cargoes.get(i).isAllowRewiring()) {
					diagram.setWireColor(i, black);
				} else {
					diagram.setWireColor(i, gray);
				}
			} else {
				diagram.setWireColor(i, red);
			}
		}
		diagram.redraw();
	}

	private void ensureCapacity(final int size, final List<?>... lists) {
		for (final List<? extends Object> l : lists) {
			if (l.size() < size) {
				l.add(size - 1, null);
			}
		}
	}

	private void performControlUpdate(final boolean rowAdded) {
		synchronized (updateLock) {
			if (isDisposed()) {
				return;
			}
			// if (rowAdded) {
			createChildren();
			layout();
			CargoWiringComposite.this.notifyListeners(SWT.Modify, new Event());
			// } else {
			// updateWiringColours(wiringDiagram, wiring, lhsComposites, rhsComposites);
			// CargoWiringComposite.this.notifyListeners(SWT.Modify, new Event());
			// }
		}
	}

	protected void doWiringChanged(final List<Integer> newWiring) {
		currentWiringCommand = new CompoundCommand("Rewire Cargoes");
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final InputModel inputModel = location.getRootObject().getSubModel(InputModel.class);

		for (int i = 0; i < numberOfRows; ++i) {
			if (wiring.get(i).equals(newWiring.get(i))) {
				// No change
				continue;
			}
			final Integer newIndex = newWiring.get(i);
			final LoadSlot loadSlot = loadSlots.get(i);
			if (newIndex >= 0 && newIndex < newWiring.size()) {
				final DischargeSlot otherDischarge = dischargeSlots.get(newIndex);
				Cargo c = cargoes.get(i);
				if (c != null) {
					if (c.getDischargeSlot() != otherDischarge) {
						currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), otherDischarge));

						// Optional market slots can be removed.
						if (c.getDischargeSlot() != null) {
							final DischargeSlot oldSlot = c.getDischargeSlot();
							if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
								currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), oldSlot));
							}
						}
					}
				} else {
					// create a new cargo
					c = createNewCargo(cargoModel);
					c.setName(loadSlot.getName());
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), otherDischarge));
				}
				appendFOBDESCommands(currentWiringCommand, location.getEditingDomain(), inputModel, c, loadSlot, otherDischarge);
			} else if (newIndex == -1) {
				final Cargo c = cargoes.get(i);
				if (c != null) {
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), null));
					currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), c));
					// Optional market slots can be removed.
					if (c.getDischargeSlot() != null) {
						final DischargeSlot oldSlot = c.getDischargeSlot();
						if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
							currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), oldSlot));
						}
					}

					// Optional market slots can be removed.
					if (c.getLoadSlot() != null) {
						final LoadSlot oldSlot = c.getLoadSlot();
						if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
							currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), oldSlot));
						}
					}
				} else {
					// Error?
				}
			} else {
				final DischargeSlot dischargeSlot = createNewDischarge(cargoModel, false);
				// ensureCapacity(newIndex + 1, cargoes, loadSlots, dischargeSlots);
				dischargeSlots.set(newIndex, dischargeSlot);
				Cargo c = cargoes.get(i);
				if (c == null) {
					// create a cargo
					c = createNewCargo(cargoModel);
					c.setName(loadSlot.getName());
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
				} else {
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
				}
				appendFOBDESCommands(currentWiringCommand, location.getEditingDomain(), inputModel, c, loadSlot, dischargeSlot);

			}
		}

		executeCurrentWiringCommand();
	}

	private void executeCurrentWiringCommand() {
		// Delete commands can be slow, so show the busy indicator while deleting.
		if (currentWiringCommand.isEmpty()) {
			currentWiringCommand = null;
			return;
		}
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				location.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			}
		};
		BusyIndicator.showWhile(null, runnable);
		currentWiringCommand = null;
	}

	private void appendFOBDESCommands(final CompoundCommand cmd, final EditingDomain editingDomain, final InputModel inputModel, final Cargo cargo, final LoadSlot loadSlot,
			final DischargeSlot dischargeSlot) {

		if (loadSlot.isDESPurchase()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));

			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getLoadSlot_ArriveCold(), false));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
			if (loadSlot instanceof SpotSlot) {
				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, loadSlot, dischargeSlot, cmd);
			} else {
				cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
				cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));
			}
		} else if (dischargeSlot.isFOBSale()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
			if (dischargeSlot instanceof SpotSlot) {
				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, dischargeSlot, loadSlot, cmd);
			} else {
				cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
				cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));
			}
		}
	}

	private void refreshContent() {

		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);

		// Get current data
		final Set<Cargo> newCargoes = new HashSet<Cargo>(cargoModel.getCargoes());
		final Set<LoadSlot> newLoadSlots = new HashSet<LoadSlot>(cargoModel.getLoadSlots());
		final Set<DischargeSlot> newDischargeSlots = new HashSet<DischargeSlot>(cargoModel.getDischargeSlots());

		// Get set of new items now known about
		newCargoes.removeAll(cargoes);
		newLoadSlots.removeAll(loadSlots);
		newDischargeSlots.removeAll(dischargeSlots);

		// New items need to be added to the list.. however we also need to remove other items. These should not have a eContainer at this point.

		for (int i = 0; i < cargoes.size(); ++i) {
			final EObject obj = cargoes.get(i);
			if (obj != null && obj.eContainer() == null) {
				cargoes.set(i, null);
			}
		}
		for (int i = 0; i < loadSlots.size(); ++i) {
			final EObject obj = loadSlots.get(i);
			if (obj != null && obj.eContainer() == null) {
				loadSlots.set(i, null);
			}
		}
		for (int i = 0; i < dischargeSlots.size(); ++i) {
			final EObject obj = dischargeSlots.get(i);
			if (obj != null && obj.eContainer() == null) {
				dischargeSlots.set(i, null);
			}
		}

		// Internal list should be full of valid items, so now we can add in the new items. First, add by cargo, then add remaining slots.

		for (final Cargo c : newCargoes) {
			// Hook in adapter to catch changes.
			c.eAdapters().add(cargoChangeAdapter);

			ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
			boolean addedItem = false;
			if (loadSlots.contains(c.getLoadSlot())) {
				cargoes.set(loadSlots.indexOf(c.getLoadSlot()), c);
			} else {
				boolean reusedRow = false;

				if (dischargeSlots.contains(c.getDischargeSlot())) {
					final int dischargeIdx = dischargeSlots.indexOf(c.getDischargeSlot());
					if (loadSlots.get(dischargeIdx) == null) {
						cargoes.set(dischargeIdx, c);
						loadSlots.set(dischargeIdx, c.getLoadSlot());
						newLoadSlots.remove(c.getLoadSlot());
						reusedRow = true;
					}
				}

				if (!reusedRow) {

					cargoes.set(numberOfRows, c);
					loadSlots.set(numberOfRows, c.getLoadSlot());
					newLoadSlots.remove(c.getLoadSlot());
					addedItem = true;
				}
			}
			if (dischargeSlots.contains(c.getDischargeSlot())) {
				dischargeSlots.set(numberOfRows, null);
			} else {

				boolean reusedRow = false;

				if (loadSlots.contains(c.getLoadSlot())) {
					final int loadIdx = loadSlots.indexOf(c.getLoadSlot());
					if (dischargeSlots.get(loadIdx) == null) {
						dischargeSlots.set(loadIdx, c.getDischargeSlot());
						newDischargeSlots.remove(c.getDischargeSlot());
						reusedRow = true;
					}
				}

				if (!reusedRow) {
					dischargeSlots.set(numberOfRows, c.getDischargeSlot());
					newDischargeSlots.remove(c.getDischargeSlot());
					addedItem = true;
				}
			}
			if (addedItem) {
				++numberOfRows;
			}
		}
		for (final LoadSlot slot : newLoadSlots) {
			ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
			cargoes.set(numberOfRows, null);
			loadSlots.set(numberOfRows, slot);
			dischargeSlots.set(numberOfRows, null);

			++numberOfRows;
		}
		for (final DischargeSlot slot : newDischargeSlots) {
			ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
			cargoes.set(numberOfRows, null);
			loadSlots.set(numberOfRows, null);
			dischargeSlots.set(numberOfRows, slot);

			++numberOfRows;
		}

		// Now all the right data should be present. Lets update the wiring
		for (int i = 0; i < numberOfRows; ++i) {
			if (cargoes.get(i) != null) {
				final int dischargeIndex = dischargeSlots.indexOf(cargoes.get(i).getDischargeSlot());
				wiring.set(i, dischargeIndex);
			} else {
				wiring.set(i, -1);
			}
		}

		// Perform a prune of empty rows
		for (int i = numberOfRows - 1; i >= 0; --i) {
			if (loadSlots.get(i) == null && dischargeSlots.get(i) == null) {
				cargoes.remove(i);
				loadSlots.remove(i);
				dischargeSlots.remove(i);

				// Re-index wiring
				for (int j = 0; j < numberOfRows; ++j) {
					final int idx = wiring.get(j);
					if (idx > i) {
						wiring.set(j, idx - 1);
					}
				}

				wiring.remove(i);
				numberOfRows--;
			}
		}

		performControlUpdate(true);
	}

	private void runWiringUpdate(final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final InputModel inputModel = location.getRootObject().getSubModel(InputModel.class);

		// Discharge has an existing slot, so remove the cargo & wiring
		if (dischargeSlot.getCargo() != null) {
			currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), dischargeSlot.getCargo()));

			// Optional market slots can be removed.
			final LoadSlot oldSlot = dischargeSlot.getCargo().getLoadSlot();
			if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
				currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), oldSlot));
			}
		}

		// Do we need to create a new cargo or re-wire and existing one.
		Cargo cargo = loadSlot.getCargo();
		if (cargo != null) {
			currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));

			// Optional market slots can be removed.
			if (cargo.getDischargeSlot() != null) {
				final DischargeSlot oldSlot = cargo.getDischargeSlot();
				if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
					currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), oldSlot));
				}
			}
		} else {
			cargo = createNewCargo(cargoModel);
			cargo.setName(loadSlot.getName());
			currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
			currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
		}

		appendFOBDESCommands(currentWiringCommand, location.getEditingDomain(), inputModel, cargo, loadSlot, dischargeSlot);

		executeCurrentWiringCommand();
	}

	private void createMenus(final IMenuManager manager, final Slot source, final List<? extends Slot> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot>> slotsByDate = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByContract = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByPort = new TreeMap<String, Set<Slot>>();

		for (final Slot target : possibleTargets) {

			final int daysDifference;
			// Perform some filtering on the possible targets
			{
				final LoadSlot loadSlot;
				final DischargeSlot dischargeSlot;
				if (sourceIsLoad) {
					loadSlot = (LoadSlot) source;
					dischargeSlot = (DischargeSlot) target;
				} else {
					loadSlot = (LoadSlot) target;
					dischargeSlot = (DischargeSlot) source;
				}
				// Filter out current pairing
				if (loadSlot.getCargo() != null && loadSlot.getCargo() == dischargeSlot.getCargo()) {
					continue;
				}

				// Filter backwards pairings
				if (loadSlot.getWindowStart().after(dischargeSlot.getWindowStart())) {
					continue;
				}
				final long diff = dischargeSlot.getWindowStart().getTime() - loadSlot.getWindowStart().getTime();
				daysDifference = (int) (diff / 1000 / 60 / 60 / 24);
			}

			final Contract contract = target.getContract();
			if (contract != null) {
				addSlotToTargets(target, contract.getName(), slotsByContract);
			}
			final Port port = target.getPort();
			if (port != null) {
				addSlotToTargets(target, port.getName(), slotsByPort);
			}

			if (daysDifference < 5) {
				addSlotToTargets(target, "Less than 5 Days", slotsByDate);
			}
			if (daysDifference < 10) {
				addSlotToTargets(target, "Less than 10 Days", slotsByDate);
			}
			if (daysDifference < 20) {
				addSlotToTargets(target, "Less than 20 Days", slotsByDate);
			}
			if (daysDifference < 30) {
				addSlotToTargets(target, "Less than 30 Days", slotsByDate);
			}
			if (daysDifference < 60) {
				addSlotToTargets(target, "Less than 60 Days", slotsByDate);
			}
			addSlotToTargets(target, "Any", slotsByDate);

		}
		{
			buildSubMenu(manager, "Slots By Contract", source, sourceIsLoad, slotsByContract, false, true);
			buildSubMenu(manager, "Slots By Date", source, sourceIsLoad, slotsByDate, true, true);
			buildSubMenu(manager, "Slots By Port", source, sourceIsLoad, slotsByPort, true, false);
		}
	}

	private void addSlotToTargets(final Slot target, final String group, final Map<String, Set<Slot>> targets) {
		Set<Slot> targetGroupSlots;
		if (targets.containsKey(group)) {
			targetGroupSlots = targets.get(group);
		} else {
			targetGroupSlots = createSlotTreeSet();
			targets.put(group, targetGroupSlots);
		}
		targetGroupSlots.add(target);
	}

	private void buildSubMenu(final IMenuManager manager, final String name, final Slot source, final boolean sourceIsLoad, final Map<String, Set<Slot>> targets, final boolean includeContract,
			final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);

		// For single item sub menus, skip the sub menu and add item directly
		if (targets.size() == 1) {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				for (final Slot target : e.getValue()) {
					createWireAction(subMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
			}

		} else {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
				for (final Slot target : e.getValue()) {
					createWireAction(subSubMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
				subMenu.add(subSubMenu);
			}

		}

		manager.add(subMenu);
	}

	private TreeSet<Slot> createSlotTreeSet() {
		final TreeSet<Slot> slotsByDate = new TreeSet<Slot>(new Comparator<Slot>() {

			@Override
			public int compare(final Slot o1, final Slot o2) {
				return o1.getWindowStart().compareTo(o2.getWindowStart());
			}
		});
		return slotsByDate;
	}

	private String getActionName(final Slot slot, final boolean includePort, final boolean includeContract) {
		final StringBuilder sb = new StringBuilder();

		if (includeContract && slot.isSetContract()) {
			sb.append(slot.getContract().getName());
			sb.append(" - ");
		}

		if (includePort && slot.getPort() != null) {
			sb.append(slot.getPort().getName());
			sb.append(" - ");
		}
		{
			final DateFormat df = DateFormat.getDateInstance();
			if (slot.getPort() != null) {
				final TimeZone zone = LocalDateUtil.getTimeZone(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
				df.setTimeZone(zone);
			}
			sb.append(df.format(slot.getWindowStart()));
			sb.append(" - ");
		}
		{
			sb.append(slot.getName());
			sb.append(" - ");

		}
		Cargo c = null;
		if (slot instanceof LoadSlot) {
			if (((LoadSlot) slot).isDESPurchase()) {
				sb.append("(DES Purchase) ");
			}
			c = ((LoadSlot) slot).getCargo();
		}
		if (slot instanceof DischargeSlot) {
			if (((DischargeSlot) slot).isFOBSale()) {
				sb.append("(FOB Sale) ");
			}
			c = ((DischargeSlot) slot).getCargo();
		}
		if (slot instanceof SpotSlot) {
			sb.append(((SpotSlot) slot).getMarket().getName());
			sb.append(" - ");

		}
		if (c != null) {
			sb.append(" (Cargo " + c.getName() + ")");
		} else {
			sb.append("(unused)");
		}
		return sb.toString();
	}

	private <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);

		final Collection<? extends ISetting> settings = factory.createInstance(location.getRootObject(), container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;
	}

	private Cargo createNewCargo(final CargoModel cargoModel) {
		// Create a cargo
		final Cargo newCargo = createObject(CargoPackage.eINSTANCE.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargoModel);
		newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());

		// Allow re-wiring
		newCargo.setAllowRewiring(true);

		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));
		return newCargo;
	}

	private SpotLoadSlot createNewSpotLoad(final CargoModel cargoModel, final boolean isDESPurchase, final SpotMarket market) {

		final SpotLoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getSpotLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newLoad.setMarket(market);
		// newLoad.setContract((Contract) market.getContract());
		newLoad.setOptional(true);
		newLoad.setName("");
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	private LoadSlot createNewLoad(final CargoModel cargoModel, final boolean isDESPurchase) {

		final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newLoad.setName("");
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	private DischargeSlot createNewDischarge(final CargoModel cargoModel, final boolean isFOBSale) {

		final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setName("");
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	private SpotDischargeSlot createNewSpotDischarge(final CargoModel cargoModel, final boolean isFOBSale, final SpotMarket market) {

		final SpotDischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getSpotDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setMarket(market);
		// newDischarge.setContract((Contract) market.getContract());
		newDischarge.setName("");
		if (market instanceof DESSalesMarket) {

			final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
			newDischarge.setPort((Port) desSalesMarket.getNotionalPort());
		}
		newDischarge.setOptional(true);
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	IMenuListener createLoadSlotMenuListener(final int index) {
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final LoadSlot loadSlot = loadSlots.get(index);
				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (loadSlot.isDESPurchase()) {
					createNewSlotMenu(newMenuManager, loadSlot, true);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
				} else {
					createNewSlotMenu(newMenuManager, loadSlot, true);
					createMenus(manager, loadSlot, cargoModel.getDischargeSlots(), true);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot, true);
				}
				createEditMenu(manager, loadSlot, loadSlot.getCargo());
				createDeleteSlotMenu(manager, loadSlot);
			}
		};
		return l;

	}

	IMenuListener createDischargeSlotMenuListener(final int index) {
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				final DischargeSlot dischargeSlot = dischargeSlots.get(index);

				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (dischargeSlot.isFOBSale()) {
					createNewSlotMenu(newMenuManager, dischargeSlot, false);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
				} else {
					createNewSlotMenu(newMenuManager, dischargeSlot, false);
					createMenus(manager, dischargeSlot, cargoModel.getLoadSlots(), false);
					createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot, false);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
				}
				createEditMenu(manager, dischargeSlot, dischargeSlot.getCargo());
				createDeleteSlotMenu(manager, dischargeSlot);
			}

		};
		return l;

	}

	private void createDeleteSlotMenu(final IMenuManager newMenuManager, final Slot slot) {
		final Action deleteAction = new Action("Delete") {
			@Override
			public void run() {

				currentWiringCommand = new CompoundCommand("Delete slot");
				currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), slot));
				Cargo cargo = null;
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					cargo = loadSlot.getCargo();
				}
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					cargo = dischargeSlot.getCargo();
				}
				if (cargo != null) {
					currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), cargo));
				}
				executeCurrentWiringCommand();
			}
		};
		newMenuManager.add(new Separator());
		newMenuManager.add(deleteAction);

	}

	private void createEditMenu(final IMenuManager newMenuManager, final Slot slot, final Cargo cargo) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Slot", slot));
		if (cargo != null) {
			newMenuManager.add(new EditAction("Edit Cargo", cargo));
		}
	}

	void createSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Slot source, final boolean sourceIsLoad) {
		final SpotMarketsModel spotMarketsModel = location.getRootObject().getSubModel(SpotMarketsModel.class);
		final Collection<SpotMarket> validMarkets = new LinkedList<SpotMarket>();
		String menuName = "";
		boolean isSpecial = false;
		if (spotType == SpotType.DES_PURCHASE) {
			menuName = "DES Purchase";
			final SpotMarketGroup group = spotMarketsModel.getDesPurchaseSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<APort> ports = SetUtils.getPorts(((DESPurchaseMarket) market).getDestinationPorts());
				if (ports.contains(source.getPort())) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		} else if (spotType == SpotType.DES_SALE) {
			menuName = "DES Sale";
			validMarkets.addAll(spotMarketsModel.getDesSalesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_PURCHASE) {
			menuName = "FOB Purchase";
			validMarkets.addAll(spotMarketsModel.getFobPurchasesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_SALE) {
			menuName = "FOB Sale";
			final SpotMarketGroup group = spotMarketsModel.getFobSalesSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final APort loadPort = ((FOBSalesMarket) market).getLoadPort();
				if (loadPort == source.getPort()) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		}
		final MenuManager subMenu = new MenuManager("New " + menuName + " Market Slot", null);

		for (final SpotMarket market : validMarkets) {
			subMenu.add(new CreateSlotAction("Create " + market.getName() + " slot", source, market, sourceIsLoad, isSpecial));
		}

		manager.add(subMenu);

	}

	void createNewSlotMenu(final IMenuManager menuManager, final Slot source, final boolean sourceIsLoad) {

		if (sourceIsLoad) {
			final LoadSlot loadSlot = (LoadSlot) source;
			if (loadSlot.isDESPurchase()) {
				// Only create new Discharge
				menuManager.add(new CreateSlotAction("Discharge", source, null, sourceIsLoad, false));
			} else {
				//
				menuManager.add(new CreateSlotAction("Discharge", source, null, sourceIsLoad, false));
				menuManager.add(new CreateSlotAction("FOB Sale", source, null, sourceIsLoad, true));
			}
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) source;
			if (dischargeSlot.isFOBSale()) {
				// only load
				menuManager.add(new CreateSlotAction("Load", source, null, sourceIsLoad, false));
			} else {
				menuManager.add(new CreateSlotAction("Load", source, null, sourceIsLoad, false));
				menuManager.add(new CreateSlotAction("DES Purchase", source, null, sourceIsLoad, true));
			}
		}
	}

	private final class EditAction extends Action {
		private final EObject target;

		private EditAction(final String text, final EObject target) {
			super(text);
			this.target = target;
		}

		@Override
		public void run() {

			final DetailCompositeDialog dcd = new DetailCompositeDialog(CargoWiringComposite.this.getShell(), location.getDefaultCommandHandler());
			try {
				location.getEditorLock().claim();
				location.setDisableUpdates(true);
				dcd.open(location, location.getRootObject(), Collections.<EObject> singletonList(target), locked);
			} finally {
				location.setDisableUpdates(false);
				location.getEditorLock().release();
			}
		}
	}

	class CreateSlotAction extends Action {

		private final Slot source;
		private final SpotMarket market;
		private final boolean sourceIsLoad;
		private final boolean isSpecial;

		public CreateSlotAction(final String name, final Slot source, final SpotMarket market, final boolean sourceIsLoad, final boolean isSpecial) {
			super(name);
			this.source = source;
			this.market = market;
			this.sourceIsLoad = sourceIsLoad;
			this.isSpecial = isSpecial;
		}

		@Override
		public void run() {
			final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);

			currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			LoadSlot loadSlot;
			DischargeSlot dischargeSlot;
			if (sourceIsLoad) {
				loadSlot = (LoadSlot) source;
				if (market == null) {
					dischargeSlot = createNewDischarge(cargoModel, isSpecial);
				} else {
					dischargeSlot = createNewSpotDischarge(cargoModel, isSpecial, market);
				}
			} else {
				if (market == null) {
					loadSlot = createNewLoad(cargoModel, isSpecial);
				} else {
					loadSlot = createNewSpotLoad(cargoModel, isSpecial, market);
				}
				dischargeSlot = (DischargeSlot) source;
			}
			runWiringUpdate(loadSlot, dischargeSlot);

		}
	}

	class WireAction extends Action {

		final private LoadSlot loadSlot;
		final private DischargeSlot dischargeSlot;

		public WireAction(final String text, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
			super(text);
			this.loadSlot = loadSlot;
			this.dischargeSlot = dischargeSlot;
		}

		@Override
		public void run() {

			currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			runWiringUpdate(loadSlot, dischargeSlot);
		}

	}

	private void createWireAction(final MenuManager subMenu, final Slot source, final Slot target, final boolean sourceIsLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, includeContract, includePort);
		if (sourceIsLoad) {
			subMenu.add(new WireAction(name, (LoadSlot) source, (DischargeSlot) target));
		} else {
			subMenu.add(new WireAction(name, (LoadSlot) target, (DischargeSlot) source));
		}
	}

	/**
	 * Sub-classes should override to handle mouse drag notifications
	 * 
	 * @param newXPos
	 * @param newYPos
	 */
	protected void requestScrollTo(final int newXPos, final int newYPos) {

	}

	/**
	 * A combined {@link MouseListener} and {@link MouseMoveListener} to call {@link CargoWiringComposite#requestScrollTo(int, int)} during mouse drag
	 * 
	 */
	private class WiringDiagramMouseListener implements MouseListener, MouseMoveListener {

		private boolean dragging = false;

		@Override
		public void mouseMove(final MouseEvent e) {
			if (dragging) {

				final Point p = wiringDiagram.toDisplay(e.x, e.y);
				requestScrollTo(p.x, p.y);
			}
		}

		@Override
		public void mouseDoubleClick(final MouseEvent e) {

		}

		@Override
		public void mouseDown(final MouseEvent e) {
			dragging = true;

		}

		@Override
		public void mouseUp(final MouseEvent e) {
			dragging = false;
		}
	}

	boolean isWiringValid(final Cargo cargo, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {

		if (validationMap.contains(loadSlot) || validationMap.contains(dischargeSlot) || validationMap.contains(cargo)) {
			return false;
		}
		return true;
	}
}
