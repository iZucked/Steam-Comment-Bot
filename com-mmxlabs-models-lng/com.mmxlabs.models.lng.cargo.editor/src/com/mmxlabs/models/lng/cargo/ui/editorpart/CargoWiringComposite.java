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
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPartSite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.ui.dialogs.WiringDiagram;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
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
 * The {@link #createApplyCommand(EditingDomain)} applies the changes.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoWiringComposite extends Composite {
	final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	private final MenuManager menuManager;

	private IScenarioEditingLocation location;

	public IScenarioEditingLocation getEditingLocation() {
		return location;
	}

	public void setLocation(final IScenarioEditingLocation location) {

		if (this.location != null) {
			this.location.getStatusProvider().removeStatusChangedListener(statusChangedListener);
		}

		this.location = location;
		if (this.location != null) {
			this.location.getStatusProvider().addStatusChangedListener(statusChangedListener);
		}
	}

	private final ArrayList<Cargo> cargoes = new ArrayList<Cargo>();
	private final ArrayList<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
	private final ArrayList<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();
	/**
	 * The value of the ith element of wiring is the index of the other end of the wire; -1 indicates no wire is present.
	 * 
	 * There are more elements in here than in {@link #cargoes}, because of the extra terminals
	 */
	private final ArrayList<Integer> wiring = new ArrayList<Integer>();

	final List<PortAndDateComposite> lhsComposites = new ArrayList<PortAndDateComposite>(cargoes.size());
	final List<PortAndDateComposite> rhsComposites = new ArrayList<PortAndDateComposite>(cargoes.size());

	private Label lhsFleetComposite;
	private Label rhsFleetComposite;
	private Label lhsFOBDESComposite;
	private Label rhsFOBDESComposite;

	// final List<String> newNames = new ArrayList<String>();
	// final List<String> newTypes = new ArrayList<String>();

	final List<Cargo> newCargoes = new LinkedList<Cargo>();

	private int numberOfRows = 0;

	private final IWorkbenchPartSite site;

	/**
	 * @param parent
	 * @param style
	 */
	public CargoWiringComposite(final Composite parent, final int style, final IWorkbenchPartSite site) {
		super(parent, style);
		createLayout();
		this.site = site;
		// setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		menuManager = new MenuManager("#PopupMenu");
		site.registerContextMenu(menuManager, site.getSelectionProvider());

		menuManager.setRemoveAllWhenShown(true);

		menuManager.addMenuListener(menuListener);

		final Menu m = menuManager.createContextMenu(this);
		this.setMenu(m);
	}

	private final IMenuListener menuListener = new IMenuListener() {

		@Override
		public void menuAboutToShow(final IMenuManager manager) {
			// TODO Auto-generated method stub

			final Action a = new Action("Action!") {

			};
			manager.add(a);
		}
	};

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
		this.newCargoes.clear();
		this.cargoes.addAll(cargoes);
		for (int i = 0; i < cargoes.size(); i++) {
			wiring.add(i); // set default wiring
			loadSlots.add(cargoes.get(i).getLoadSlot());
			dischargeSlots.add(cargoes.get(i).getDischargeSlot());
		}

		numberOfRows = cargoes.size();

		wiring.add(-1); // bogus element for the add terminals.
		wiring.add(-1); // bogus element for the add terminals.

		createChildren();
	}

	private void createLayout() {
		final GridLayout layout = new GridLayout();

		layout.numColumns = 4;
		// layout.makeColumnsEqualWidth = false;

		setLayout(layout);
	}

	private void updateWiringColours(final WiringDiagram diagram, final List<Integer> wiring, final List<PortAndDateComposite> loads, final List<PortAndDateComposite> discharge) {
		final Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

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

	WiringDiagram wiringDiagram = null;
	Listener selectionListener = new Listener() {
		@Override
		public void handleEvent(final Event event) {
			updateWiringColours(wiringDiagram, wiringDiagram.getWiring(), lhsComposites, rhsComposites);
		}
	};

	private void createChildren() {
		lhsComposites.clear();
		rhsComposites.clear();
		if (wiringDiagram != null) {
			wiringDiagram.dispose();
			wiringDiagram = null;
		}
		int index = 0;
		for (int ii = 0; ii < numberOfRows; ++ii) {
			final Text idLabel = new Text(this, SWT.RIGHT | SWT.BORDER);

			idLabel.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					// newNames.set(finalIndex, idLabel.getText());
				}
			});

			final PortAndDateComposite loadSide = new PortAndDateComposite(this, SWT.BORDER, site);
			loadSide.setCommandHandler(commandHandler);
			loadSide.display(location, location.getRootObject(), loadSlots.get(index), Collections.<EObject> emptyList());

			loadSide.addMenuListener(createLoadSlotMenuListener(loadSlots.get(index)));

			if (wiringDiagram == null) {
				wiringDiagram = new WiringDiagram(this, getStyle() & ~SWT.BORDER) {

					@Override
					protected List<Float> getTerminalPositions() {
						final float littleOffset = getBounds().y;
						final ArrayList<Float> vMidPoints = new ArrayList<Float>(cargoes.size());
						// get vertical coordinates of labels

						float lastMidpointButOne = 0;
						float lastMidpoint = 0;

						for (final Composite l : lhsComposites) {
							final Rectangle lbounds = l.getBounds();
							lastMidpointButOne = lastMidpoint;
							lastMidpoint = -littleOffset + lbounds.y + lbounds.height / 2.0f;
							vMidPoints.add(lastMidpoint);
						}

						// Draw extra terminals
						{
							final Rectangle lbounds = lhsFleetComposite.getBounds();
							lastMidpointButOne = lastMidpoint;
							lastMidpoint = -littleOffset + lbounds.y + lbounds.height / 2.0f;
							vMidPoints.add(lastMidpoint);
						}

						{
							final Rectangle lbounds = lhsFOBDESComposite.getBounds();
							lastMidpointButOne = lastMidpoint;
							lastMidpoint = -littleOffset + lbounds.y + lbounds.height / 2.0f;
							vMidPoints.add(lastMidpoint);
						}

						return vMidPoints;
					}

					@Override
					protected void wiringChanged(final List<Integer> newWiring) {
						doWiringChanged(newWiring);
					}

				};
				// wiring diagram is tall
				wiringDiagram.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, cargoes.size() + 2));

			}

			final PortAndDateComposite dischargeSide = new PortAndDateComposite(this, SWT.BORDER, site);
			dischargeSide.setCommandHandler(commandHandler);
			dischargeSide.display(location, location.getRootObject(), dischargeSlots.get(index), Collections.<EObject> emptyList());
			final String id = "ID";// newNames.get(index);

			dischargeSide.addMenuListener(createDischargeSlotMenuListener(dischargeSlots.get(index)));

			
			lhsComposites.add(loadSide);
			rhsComposites.add(dischargeSide);
			idLabel.setText(id);

			loadSide.addListener(SWT.Selection, selectionListener);
			dischargeSide.addListener(SWT.Selection, selectionListener);

			loadSide.addListener(SWT.DefaultSelection, selectionListener);
			dischargeSide.addListener(SWT.DefaultSelection, selectionListener);

			idLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			loadSide.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			dischargeSide.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));


			index++;
		}

		{
			lhsFleetComposite = new Label(this, SWT.NONE);
			lhsFleetComposite.setText("Shipped");

			rhsFleetComposite = new Label(this, SWT.NONE);
			rhsFleetComposite.setText("Shipped");

			final GridData gd1 = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
			gd1.horizontalSpan = 2;
			lhsFleetComposite.setLayoutData(gd1);
			final GridData gd2 = new GridData(SWT.LEFT, SWT.CENTER, true, false);
			gd2.horizontalSpan = 1;
			rhsFleetComposite.setLayoutData(gd2);

			lhsFOBDESComposite = new Label(this, SWT.NONE);
			lhsFOBDESComposite.setText("DES Purchase");

			rhsFOBDESComposite = new Label(this, SWT.NONE);
			rhsFOBDESComposite.setText("FOB Sale");

			final GridData gd3 = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
			gd3.horizontalSpan = 2;
			lhsFOBDESComposite.setLayoutData(gd3);
			final GridData gd4 = new GridData(SWT.LEFT, SWT.CENTER, true, false);
			gd4.horizontalSpan = 1;
			rhsFOBDESComposite.setLayoutData(gd4);
		}

		// add bogus packing label
		new Label(this, SWT.NONE).setLayoutData(new GridData(GridData.FILL_VERTICAL));
		new Label(this, SWT.NONE).setLayoutData(new GridData(GridData.FILL_VERTICAL));

		wiringDiagram.setWiring(wiring);
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
		updateWiringColours(wiringDiagram, wiring, lhsComposites, lhsComposites);
	}

	/*
	 * TODO highlight lines when mouseover labels TODO feedback validation state to containing dialog TODO generate command to apply changes.
	 */

	/**
	 * @return
	 */
	public boolean isWiringFeasible() {

		for (int loadIdx = 0; loadIdx < wiring.size() - 2; ++loadIdx) {
			final int dischargeIdx = wiring.get(loadIdx);

			if (dischargeIdx == -1) {
				return false;
			}
		}

		return true;
	}

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

	public void setStatusProvider(final IStatusProvider statusProvider) {
		statusProvider.addStatusChangedListener(statusChangedListener);
	}

	protected IStatusChangedListener statusChangedListener = new IStatusChangedListener() {

		@Override
		public void onStatusChanged(final IStatusProvider provider, final IStatus status) {

			for (final PortAndDateComposite c : lhsComposites) {
				c.displayValidationStatus(status);
			}
			for (final PortAndDateComposite c : rhsComposites) {
				c.displayValidationStatus(status);
			}
		}
	};

	@Override
	public void dispose() {
		if (this.location != null) {
			this.location.getStatusProvider().removeStatusChangedListener(statusChangedListener);
		}
	}

	CompoundCommand currentWiringCommand = null;

	private void createNewCargo(final CargoModel cargoModel, final int idx) {
		ensureCapacity(idx + 1, cargoes, loadSlots, dischargeSlots);
		// Create a cargo
		final String name = "New cargo" + (newCargoes.size() + 1);

		final Cargo newCargo = createObject(CargoPackage.eINSTANCE.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargoModel);
		newCargo.setName(name);
		newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());

		cargoes.set(idx, newCargo);

		newCargoes.add(newCargo);

		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));
	}

	private void ensureCapacity(final int size, final List<?>... lists) {
		// TODO Auto-generated method stub
		for (final List<? extends Object> l : lists) {
			if (l.size() < size) {
				l.add(size - 1, null);
			}
		}
	}

	private void createNewLoad(final CargoModel cargoModel, final int idx, final boolean isDESPurchase) {
		ensureCapacity(idx + 1, cargoes, loadSlots, dischargeSlots);

		final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		final String name = "New cargo " + (newCargoes.size() + 1);
		newLoad.setName("load-" + name);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		loadSlots.set(idx, newLoad);
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
	}

	private void createNewDischarge(final CargoModel cargoModel, final int idx, final boolean isFOBSale) {
		ensureCapacity(idx + 1, cargoes, loadSlots, dischargeSlots);

		final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		final String name = "New cargo " + (newCargoes.size() + 1);
		newDischarge.setName("discharge-" + name);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		dischargeSlots.set(idx, newDischarge);
		currentWiringCommand.append(AddCommand.create(location.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
	}

	protected void doWiringChanged(final List<Integer> newWiring) {
		currentWiringCommand = new CompoundCommand("Rewire Cargoes");
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);

		// check for wiring to add terminal
		final int topIndex = newWiring.size() - 2;
		final int bottomIndex = newWiring.size() - 1;

		boolean addNewElement = false;
		for (int i = 0; i < newWiring.size() - 2; ++i) {
			if (wiring.get(i).equals(newWiring.get(i))) {
				// No change
				continue;
			}
			final Integer newIndex = newWiring.get(i);
			if (newIndex >= 0 && newIndex < newWiring.size() - 2) {
				final DischargeSlot otherDischarge = dischargeSlots.get(newIndex);
				final Cargo c = cargoes.get(i);
				if (c != null) {
					if (c.getDischargeSlot() != otherDischarge) {
						currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), otherDischarge));
					}
				} else {
					// create a new cargo
					createNewCargo(cargoModel, i);
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(i), CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlots.get(i)));
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(i), CargoPackage.eINSTANCE.getCargo_DischargeSlot(), otherDischarge));
					// New element added - but no increase in number of rows...
				}
			} else if (newIndex == -1) {
				final Cargo c = cargoes.get(i);
				if (c != null) {
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), null));
					currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), c));
					cargoes.set(i, null);
				} else {
					// Error?
				}
			} else {
				createNewDischarge(cargoModel, newIndex, newIndex == bottomIndex);
				final Cargo c = cargoes.get(i);
				if (c == null) {
					// create a cargo
					createNewCargo(cargoModel, i);
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(i), CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlots.get(i)));
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(i), CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlots.get(newIndex)));
				} else {
					currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(i), CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlots.get(newIndex)));
				}
				addNewElement = true;
			}
		}

		if (newWiring.get(topIndex) != -1 || newWiring.indexOf(topIndex) != -1) {
			// cmd.append(addNewCargo(new ArrayList<Integer>(newWiring), false));
		} else if (newWiring.get(bottomIndex) != -1 || newWiring.indexOf(bottomIndex) != -1) {

			if (newWiring.get(bottomIndex) != -1) {
				newWiring.set(newWiring.size() - 2, newWiring.get(bottomIndex));
				newWiring.set(newWiring.size() - 1, -1);
			} else {
				newWiring.set(newWiring.indexOf(bottomIndex), newWiring.size() - 2);

			}
		} else {
			// TODO: Handle new Load Slot
		}

		location.getEditingDomain().getCommandStack().execute(currentWiringCommand);
		currentWiringCommand = null;

		CargoWiringComposite.this.wiring.clear();
		CargoWiringComposite.this.wiring.addAll(newWiring);
		if (addNewElement) {
			numberOfRows++;
			CargoWiringComposite.this.wiring.add(-1);

			for (final Control c : getChildren()) {
				c.dispose();
			}

			createChildren();
			layout();
		} else {
			updateWiringColours(wiringDiagram, newWiring, lhsComposites, rhsComposites);
		}

		CargoWiringComposite.this.notifyListeners(SWT.Modify, new Event());
	}

	private <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);

		final Collection<? extends ISetting> settings = factory.createInstance(location.getRootObject(), container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			final CompoundCommand add = new CompoundCommand();
			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;
	}

	IMenuListener createLoadSlotMenuListener(final LoadSlot loadSlot) {
		final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				if (loadSlot.isDESPurchase()) {
					// ?
				} else {
					createMenus(manager, loadSlot, cargoModel.getDischargeSlots(), true);
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
				if (dischargeSlot.isFOBSale()) {
					// ?
				} else {
					createMenus(manager, dischargeSlot, cargoModel.getLoadSlots(), false);
				}
			}
		};
		return l;

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

			final CargoModel cargoModel = location.getRootObject().getSubModel(CargoModel.class);
			currentWiringCommand = new CompoundCommand("Rewire Cargoes");

			final int loadIdx;

			// Find or add load slot to load slots list (and bring in cargo and discharge if not present)
			boolean insertedLoad = false;
			if (loadSlots.contains(loadSlot)) {
				loadIdx = loadSlots.indexOf(loadSlot);
			} else {
				loadIdx = loadSlots.size();
				loadSlots.add(loadSlot);
				insertedLoad = true;
				// Inserting the load slot - should insert the cargo and discharge also
				final Cargo c = loadSlot.getCargo();

				ensureCapacity(loadIdx + 1, cargoes, dischargeSlots);
				if (c != null) {
					cargoes.set(loadIdx, c);
					dischargeSlots.set(loadIdx, c.getDischargeSlot());
					wiring.set(numberOfRows, numberOfRows);
					wiring.add(-1);
				}
			}

			// Find or add discharge slot to discharge slots list (and bring in cargo and load if not present)
			final int dischargeIdx;
			boolean insertedDischarge = false;
			if (dischargeSlots.contains(dischargeSlot)) {
				dischargeIdx = dischargeSlots.indexOf(dischargeSlot);
			} else {
				dischargeIdx = dischargeSlots.size();
				dischargeSlots.add(dischargeSlot);
				insertedDischarge = true;

				// Inserting the discharge slot - should insert the cargo and load also
				final Cargo c = dischargeSlot.getCargo();

				ensureCapacity(dischargeIdx + 1, cargoes, loadSlots);
				if (c != null) {
					cargoes.set(dischargeIdx, c);
					loadSlots.set(dischargeIdx, c.getLoadSlot());
					wiring.set(numberOfRows, numberOfRows);
					wiring.add(-1);
				}
			}

			// Do we need to create a new cargo or re-wire and existing one.
			final Cargo cargo = loadSlot.getCargo();
			if (cargo != null) {
				currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(loadIdx), CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
				final int current = wiring.indexOf(dischargeIdx);
				if (current != -1) {
					wiring.set(current, -1);
					if (cargoes.get(current) != null) {
						currentWiringCommand.append(DeleteCommand.create(location.getEditingDomain(), dischargeSlot.getCargo()));
						cargoes.set(current, null);
					}
				}
				wiring.set(loadIdx, dischargeIdx);
			} else {
				createNewCargo(cargoModel, loadIdx);
				currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(loadIdx), CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
				currentWiringCommand.append(SetCommand.create(location.getEditingDomain(), cargoes.get(loadIdx), CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
			}

			location.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			currentWiringCommand = null;

			if (insertedLoad || insertedDischarge) {
				numberOfRows++;

				for (final Control c : getChildren()) {
					c.dispose();
				}

				createChildren();
				layout();
			} else {
				wiringDiagram.setWiring(wiring);
				updateWiringColours(wiringDiagram, wiring, lhsComposites, rhsComposites);
			}

			CargoWiringComposite.this.notifyListeners(SWT.Modify, new Event());

		}
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
				// Filter out current paring
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

			addSlotToTargets(target, target.getContract().getName(), slotsByContract);
			addSlotToTargets(target, target.getPort().getName(), slotsByPort);

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

	private void createWireAction(final MenuManager subMenu, final Slot source, final Slot target, final boolean sourceIsLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, includeContract, includePort);
		if (sourceIsLoad) {
			subMenu.add(new WireAction(name, (LoadSlot) source, (DischargeSlot) target));
		} else {
			subMenu.add(new WireAction(name, (LoadSlot) target, (DischargeSlot) source));
		}
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

		if (includeContract) {
			sb.append(slot.getContract().getName());
			sb.append(" - ");
		}
		;

		if (includePort) {
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
}
