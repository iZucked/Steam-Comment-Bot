/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
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
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.DESPurchaseMarket;
import com.mmxlabs.models.lng.pricing.FOBSalesMarket;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.lng.pricing.SpotType;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
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
		public void notifyChanged(Notification notification) {

			super.notifyChanged(notification);

		};

		public void enable(boolean skip) {
			// Normally this means we should ignore anything that has happened.
			// However here we ignore this and process the data anyway.
			// TODO: This needs testing!
			super.enable(false);
		};

		protected void missedNotifications(final List<Notification> missed) {
			final ArrayList<Notification> missedNotifications = new ArrayList<Notification>(missed);
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					synchronized (updateLock) {
						for (final Notification n : missedNotifications) {
							localNotifyChanged(n);
						}
						performControlUpdate(false);

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
					if (notification.getEventType() == Notification.REMOVE) {
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
					if (notification.getEventType() == Notification.REMOVE) {
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
				loadIdx = numberOfRows;
				ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
				wiring.set(numberOfRows, -1);

				loadSlots.set(loadIdx, loadSlot);
				rowAdded = true;
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
				dischargeIdx = numberOfRows;
				ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);

				dischargeSlots.set(dischargeIdx, dischargeSlot);
				rowAdded = true;
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

	private final ArrayList<Boolean> leftTerminalsValid = new ArrayList<Boolean>();
	private final ArrayList<Boolean> rightTerminalsValid = new ArrayList<Boolean>();
	/**
	 * The value of the ith element of wiring is the index of the other end of the wire; -1 indicates no wire is present.
	 * 
	 * There are more elements in here than in {@link #cargoes}, because of the extra terminals
	 */
	private final ArrayList<Integer> wiring = new ArrayList<Integer>();

	final List<NamedObjectNameComposite> idComposites = new ArrayList<NamedObjectNameComposite>(cargoes.size());
	final List<PortAndDateComposite> lhsComposites = new ArrayList<PortAndDateComposite>(cargoes.size());
	final List<PortAndDateComposite> rhsComposites = new ArrayList<PortAndDateComposite>(cargoes.size());

	private int numberOfRows = 0;

	private final IWorkbenchPartSite site;

	// private final MenuManager menuManager;

	protected IStatusChangedListener statusChangedListener = new IStatusChangedListener() {

		@Override
		public void onStatusChanged(final IStatusProvider provider, final IStatus status) {

			for (final PortAndDateComposite c : lhsComposites) {
				c.displayValidationStatus(status);
			}
			for (final PortAndDateComposite c : rhsComposites) {
				c.displayValidationStatus(status);
			}
			for (final NamedObjectNameComposite c : idComposites) {
				c.displayValidationStatus(status);
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

	private final IInlineEditorWrapper wrapper = IInlineEditorWrapper.IDENTITY;

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

		}
	}

	/**
	 * Set the cargoes, and reset the wiring to match these cargoes.
	 * 
	 * @param cargoes
	 */
	public void setCargoes(final List<Cargo> cargoes) {
		// delete existing children
		for (final Control c : getChildren()) {
			c.dispose();
		}
		wiring.clear();
		this.loadSlots.clear();
		this.dischargeSlots.clear();
		this.cargoes.clear();
		this.cargoes.addAll(cargoes);
		this.leftTerminalsValid.clear();
		this.rightTerminalsValid.clear();
		for (int i = 0; i < cargoes.size(); i++) {

			wiring.add(i); // set default wiring
			loadSlots.add(cargoes.get(i).getLoadSlot());
			dischargeSlots.add(cargoes.get(i).getDischargeSlot());
			leftTerminalsValid.add(true);
			rightTerminalsValid.add(true);
		}

		numberOfRows = cargoes.size();

		createChildren();
	}

	public void setStatusProvider(final IStatusProvider statusProvider) {
		statusProvider.addStatusChangedListener(statusChangedListener);
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
		this.locked = locked;
		wiringDiagram.setLocked(locked);
		for (final PortAndDateComposite c : lhsComposites) {
			c.setEnabled(!locked);
		}
		for (final PortAndDateComposite c : rhsComposites) {
			c.setEnabled(!locked);
		}
		for (final NamedObjectNameComposite c : idComposites) {
			c.setEnabled(!locked);
		}
	}

	/**
	 * @return
	 */
	public boolean isWiringFeasible() {

		for (int loadIdx = 0; loadIdx < wiring.size(); ++loadIdx) {
			final int dischargeIdx = wiring.get(loadIdx);

			if (dischargeIdx == -1) {
				return false;
			}
		}

		return true;
	}

	private void createLayout() {

		final GridLayout gridLayout = new GridLayout(4, false);
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
		// lhsComposites.clear();
		// rhsComposites.clear();
		leftTerminalsValid.clear();
		rightTerminalsValid.clear();
		// idComposites.clear();
		// if (wiringDiagram != null) {
		// wiringDiagram.dispose();
		// wiringDiagram = null;
		// }
		final Color WHITE = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		for (int index = 0; index < numberOfRows; ++index) {

			final NamedObjectNameComposite idComposite;
			if (index < idComposites.size()) {
				idComposite = idComposites.get(index);
			} else {

				idComposite = new NamedObjectNameComposite(this, SWT.NONE) {
					@Override
					public void addInlineEditor(final IInlineEditor editor) {
						editors.add(editor);
						editor.setCommandHandler(commandHandler);
						final Control control = editor.createControl(this);
						final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
						gd.widthHint = 60;
						control.setLayoutData(gd);
						control.setBackground(WHITE);
					}
				};
				idComposite.setCommandHandler(commandHandler);
				idComposites.add(idComposite);
				final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				// gd.widthHint = 70;
				// gd.grabExcessHorizontalSpace = true;
				// gd.grabExcessVerticalSpace = true;
				idComposite.setLayoutData(gd);
				idComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
			}

			if (true || idComposite.getObject() != cargoes.get(index)) {
				idComposite.display(location, location.getRootObject(), cargoes.get(index), Collections.<EObject> emptyList());
			}
			final PortAndDateComposite loadSide;
			if (index < lhsComposites.size()) {
				loadSide = lhsComposites.get(index);
			} else {
				loadSide = new PortAndDateComposite(this,SWT.NONE, site, true);
				final GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				loadSide.setLayoutData(gd2);
				loadSide.setBackground(WHITE);

				loadSide.setCommandHandler(commandHandler);

				loadSide.addMenuListener(createLoadSlotMenuListener(loadSlots.get(index)));
				lhsComposites.add(loadSide);
				loadSide.addListener(SWT.Selection, selectionListener);
				loadSide.addListener(SWT.DefaultSelection, selectionListener);
				loadSide.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

			}
			if (true || loadSide.getSlot() != loadSlots.get(index)) {
				loadSide.display(location, location.getRootObject(), loadSlots.get(index), Collections.<EObject> emptyList());
			}
			leftTerminalsValid.add(loadSlots.get(index) != null);

			if (wiringDiagram == null) {
				wiringDiagram = new WiringDiagram(this, SWT.NONE) {

					@Override
					public synchronized void paintControl(PaintEvent e) {
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
			}
			if (index == 0) {
				// wiring diagram is tall
				final GridData gd3 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, numberOfRows);
				gd3.widthHint = 90;
				gd3.minimumWidth = 90;
				wiringDiagram.setLayoutData(gd3);
				// wiringDiagram.setBackground(WHITE);
			}

			final PortAndDateComposite dischargeSide;

			if (index < rhsComposites.size()) {
				dischargeSide = rhsComposites.get(index);
			} else {
				dischargeSide = new PortAndDateComposite(this, SWT.NONE, site, false);
				dischargeSide.setCommandHandler(commandHandler);
				dischargeSide.setBackground(WHITE);
				dischargeSide.addMenuListener(createDischargeSlotMenuListener(dischargeSlots.get(index)));
				rhsComposites.add(dischargeSide);
				dischargeSide.addListener(SWT.Selection, selectionListener);
				dischargeSide.addListener(SWT.DefaultSelection, selectionListener);
				dischargeSide.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
			}
			if (true || dischargeSide.getSlot() != dischargeSlots.get(index)) {
				dischargeSide.display(location, location.getRootObject(), dischargeSlots.get(index), Collections.<EObject> emptyList());
			}
			rightTerminalsValid.add(dischargeSlots.get(index) != null);
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
		while (idComposites.size() > numberOfRows) {
			final Composite c = idComposites.remove(idComposites.size() - 1);
			c.dispose();
		}

		wiringDiagram.setWiring(wiring);
		wiringDiagram.setTerminalsValid(leftTerminalsValid, rightTerminalsValid);
		final ArrayList<Pair<Color, Color>> terminalColors = new ArrayList<Pair<Color, Color>>();
		final ArrayList<Color> wireColors = new ArrayList<Color>();
		final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

		for (int i = 0; i < numberOfRows; ++i) {
			terminalColors.add(new Pair<Color, Color>(green, green));
			wireColors.add(black);
		}

		final Color addColor = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);

		terminalColors.add(new Pair<Color, Color>(addColor, addColor));
		terminalColors.add(new Pair<Color, Color>(addColor, addColor));

		wiringDiagram.setWireColors(wireColors);
		wiringDiagram.setTerminalColors(terminalColors);
		updateWiringColours(wiringDiagram, wiring, lhsComposites, rhsComposites);

		// update locked status.
		setLocked(locked);
	}

	/*
	 * TODO highlight lines when mouseover labels TODO feedback validation state to containing dialog TODO generate command to apply changes.
	 */

	private synchronized void updateWiringColours(final WiringDiagram diagram, final List<Integer> wiring, final List<PortAndDateComposite> loads, final List<PortAndDateComposite> discharge) {
		final Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		final Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

		for (int i = 0; i < numberOfRows; ++i) {
			diagram.setLeftTerminalColor(i, red);
			diagram.setRightTerminalColor(i, red);
		}
		for (int i = 0; i < numberOfRows; i++) {
			final int j = wiring.get(i);
			if (j == -1) {
				continue;
			}

			// check dates
			final Date loadDate = loads.get(i).getDate();
			if (loadDate == null) {
				continue;
			}
			final Date dischargeDate = discharge.get(j).getDate();
			if (dischargeDate == null) {
				continue;
			}
			diagram.setLeftTerminalColor(i, green);
			diagram.setRightTerminalColor(j, green);
			// TODO this is not exactly proper validation.
			if (dischargeDate.before(loadDate)) {
				diagram.setWireColor(i, red);
			} else {
				diagram.setWireColor(i, black);
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

		for (int i = 0; i < newWiring.size(); ++i) {
			if (wiring.get(i).equals(newWiring.get(i))) {
				// No change
				continue;
			}
			final Integer newIndex = newWiring.get(i);
			LoadSlot loadSlot = loadSlots.get(i);
			if (newIndex >= 0 && newIndex < newWiring.size()) {
				final DischargeSlot otherDischarge = dischargeSlots.get(newIndex);
				Cargo c = cargoes.get(i);
				if (c != null) {
					if (c.getDischargeSlot() != otherDischarge) {
						currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), otherDischarge));
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

		location.getEditingDomain().getCommandStack().execute(currentWiringCommand);
		currentWiringCommand = null;
	}

	private void appendFOBDESCommands(CompoundCommand cmd, EditingDomain editingDomain, InputModel inputModel, Cargo cargo, LoadSlot loadSlot, DischargeSlot dischargeSlot) {

		if (loadSlot.isDESPurchase()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));

			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));
		} else if (dischargeSlot.isFOBSale()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));

		}

	}

	private void runWiringUpdate(final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final InputModel inputModel = location.getRootObject().getSubModel(InputModel.class);

		// Discharge has an existing slot, so remove the cargo & wiring
		if (dischargeSlot.getCargo() != null) {
			currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), dischargeSlot.getCargo()));
		}

		// Do we need to create a new cargo or re-wire and existing one.
		Cargo cargo = loadSlot.getCargo();
		if (cargo != null) {
			currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
		} else {
			cargo = createNewCargo(cargoModel);
			cargo.setName(loadSlot.getName());
			currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
			currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
		}

		appendFOBDESCommands(currentWiringCommand, location.getEditingDomain(), inputModel, cargo, loadSlot, dischargeSlot);

		location.getEditingDomain().getCommandStack().execute(currentWiringCommand);
		currentWiringCommand = null;
	}

	private void createMenus(final IMenuManager manager, final Slot source, final List<? extends Slot> possibleTargets, final boolean sourceIsLoad) {

		final TreeMap<String, TreeSet<Slot>> slotsByDate = new TreeMap<String, TreeSet<Slot>>();
		final TreeMap<String, TreeSet<Slot>> slotsByContract = new TreeMap<String, TreeSet<Slot>>();
		final TreeMap<String, TreeSet<Slot>> slotsByPort = new TreeMap<String, TreeSet<Slot>>();

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

	private void addSlotToTargets(final Slot target, final String group, final TreeMap<String, TreeSet<Slot>> targets) {
		TreeSet<Slot> targetGroupSlots;
		if (targets.containsKey(group)) {
			targetGroupSlots = targets.get(group);
		} else {
			targetGroupSlots = createSlotTreeSet();
			targets.put(group, targetGroupSlots);
		}
		targetGroupSlots.add(target);
	}

	private void buildSubMenu(final IMenuManager manager, final String name, final Slot source, final boolean sourceIsLoad, final TreeMap<String, TreeSet<Slot>> targets,
			final boolean includeContract, final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);
		for (final Map.Entry<String, TreeSet<Slot>> e : targets.entrySet()) {
			final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
			for (final Slot target : e.getValue()) {
				createWireAction(subSubMenu, source, target, sourceIsLoad, includeContract, includePort);
			}
			subMenu.add(subSubMenu);
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

		if (includeContract && slot.getContract() != null) {
			sb.append(slot.getContract().getName());
			sb.append(" - ");
		}

		if (includePort && slot.getPort() != null) {
			sb.append(slot.getPort().getName());
			sb.append(" - ");
		}
		{
			sb.append(DateFormat.getDateInstance().format(slot.getWindowStart()));
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
		newLoad.setContract((Contract) market.getContract());
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	private LoadSlot createNewLoad(final CargoModel cargoModel, final boolean isDESPurchase) {

		final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	private DischargeSlot createNewDischarge(final CargoModel cargoModel, final boolean isFOBSale) {

		final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	private SpotDischargeSlot createNewSpotDischarge(final CargoModel cargoModel, final boolean isFOBSale, final SpotMarket market) {

		final SpotDischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getSpotDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setMarket(market);
		newDischarge.setContract((Contract) market.getContract());
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	IMenuListener createLoadSlotMenuListener(final LoadSlot loadSlot) {
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
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
			}
		};
		return l;

	}

	IMenuListener createDischargeSlotMenuListener(final DischargeSlot dischargeSlot) {
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
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
			}
		};
		return l;

	}

	void createSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Slot source, final boolean sourceIsLoad) {
		final PricingModel pricingModel = location.getRootObject().getSubModel(PricingModel.class);
		final Collection<SpotMarket> validMarkets = new LinkedList<SpotMarket>();
		String menuName = "";
		boolean isSpecial = false;
		if (spotType == SpotType.DES_PURCHASE) {
			menuName = "DES Purchase";
			final SpotMarketGroup group = pricingModel.getDesPurchaseSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<APort> ports = SetUtils.getPorts(((DESPurchaseMarket) market).getDestinationPorts());
				if (ports.contains(source.getPort())) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		} else if (spotType == SpotType.DES_SALE) {
			menuName = "DES Sale";
			validMarkets.addAll(pricingModel.getDesSalesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_PURCHASE) {
			menuName = "FOB Purchase";
			validMarkets.addAll(pricingModel.getDesSalesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_SALE) {
			menuName = "FOB Sale";
			final SpotMarketGroup group = pricingModel.getFobSalesSpotMarket();
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
}
