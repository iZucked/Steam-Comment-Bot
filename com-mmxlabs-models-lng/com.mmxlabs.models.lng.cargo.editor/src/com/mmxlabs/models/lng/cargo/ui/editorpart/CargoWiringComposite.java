/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
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

	/**
	 * @param parent
	 * @param style
	 */
	public CargoWiringComposite(final Composite parent, final int style) {
		super(parent, style);
		createLayout();
		// setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
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

			final PortAndDateComposite loadSide = new PortAndDateComposite(this, SWT.BORDER);
			loadSide.setCommandHandler(commandHandler);
			loadSide.display(location, location.getRootObject(), loadSlots.get(index), Collections.<EObject> emptyList());
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

						System.out.println("Size of terminals " + vMidPoints.size());
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

			final PortAndDateComposite dischargeSide = new PortAndDateComposite(this, SWT.BORDER);
			dischargeSide.setCommandHandler(commandHandler);
			dischargeSide.display(location, location.getRootObject(), dischargeSlots.get(index), Collections.<EObject> emptyList());
			final String id = "ID";// newNames.get(index);

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

		System.out.println(wiring.size() + " -- " + lhsComposites.size() + " -- " + rhsComposites.size());
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

		final int size = Math.max(cargoes.size(), Math.max(loadSlots.size(), Math.max(dischargeSlots.size(), wiring.size() - 2)));

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

	private void ensureCapacity(final int size, final List<? extends Object>... lists) {
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
		System.out.println(addNewElement);
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

	private <T> T createObject(EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		IModelFactory factory = factories.get(0);

		final Collection<? extends ISetting> settings = factory.createInstance(location.getRootObject(), container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			final CompoundCommand add = new CompoundCommand();
			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;

	}

}
