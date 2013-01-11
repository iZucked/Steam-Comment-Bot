/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.dialogs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * A composite for displaying a wiring editor. Contains a {@link WiringDiagram}, which lets the user view and edit a matching in a bipartite graph, and provides the interfacing logic to apply a new
 * matching to the cargoes passed in through {@link #setCargoes(List)}.
 * 
 * The {@link #createApplyCommand(EditingDomain)} applies the changes.
 * 
 * @author Tom Hinton
 * 
 */
public class WiringComposite extends Composite {
	private final ArrayList<Cargo> cargoes = new ArrayList<Cargo>();
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

	final List<String> newNames = new ArrayList<String>();
	// final List<String> newTypes = new ArrayList<String>();

	final List<Cargo> newCargoes = new LinkedList<Cargo>();

	private IReferenceValueProvider portProvider;
	private IReferenceValueProvider contractProvider;

	/**
	 * @param parent
	 * @param style
	 */
	public WiringComposite(final Composite parent, final int style) {
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
		this.cargoes.clear();
		this.cargoes.addAll(cargoes);
		for (int i = 0; i < cargoes.size(); i++) {
			wiring.add(i); // set default wiring
		}

		wiring.add(-1); // bogus element for the add terminals.
		wiring.add(-1); // bogus element for the add terminals.

		newNames.clear();
		// newTypes.clear();
		for (final Cargo c : cargoes) {
			newNames.add(c.getName());
			// newTypes.add(c.getCargoType().getName());
		}

		createChildren();
	}

	public void addNewCargo(final List<Integer> newWiring, final boolean isFOBorDES) {
		// buffer LHS and RHS values (yuck)
		final List<Port> lhsPorts = new ArrayList<Port>(lhsComposites.size());
		final List<Port> rhsPorts = new ArrayList<Port>(rhsComposites.size());
		final List<Date> lhsDates = new ArrayList<Date>(lhsComposites.size());
		final List<Date> rhsDates = new ArrayList<Date>(rhsComposites.size());
		final List<Contract> lhsContracts = new ArrayList<Contract>(lhsComposites.size());
		final List<Contract> rhsContracts = new ArrayList<Contract>(rhsComposites.size());
		final List<Boolean> lhsFOBORDES = new ArrayList<Boolean>(lhsComposites.size());
		final List<Boolean> rhsFOBORDES = new ArrayList<Boolean>(rhsComposites.size());

		for (final PortAndDateComposite c : lhsComposites) {
			lhsPorts.add(c.getPort());
			lhsDates.add(c.getDate());
			lhsContracts.add(c.getContract());
			lhsFOBORDES.add(c.isFOBOrDES());
		}

		for (final PortAndDateComposite c : rhsComposites) {
			rhsPorts.add(c.getPort());
			rhsDates.add(c.getDate());
			rhsContracts.add(c.getContract());
			rhsFOBORDES.add(c.isFOBOrDES());
		}

		if (newWiring != null) {
			wiring.clear();
			wiring.addAll(newWiring);
		}

		wiring.add(-1); // preserve bogus element
		// duplicate last cargo?
		final Cargo prototype = cargoes.get(cargoes.size() - 1);
		final Cargo newCargo = EcoreUtil.copy(prototype);
		final LoadSlot newLoad = EcoreUtil.copy(prototype.getLoadSlot());
		newLoad.setDESPurchase(isFOBorDES);
		final DischargeSlot newDischarge = EcoreUtil.copy(prototype.getDischargeSlot());
		newDischarge.setFOBSale(isFOBorDES);
		newCargo.setLoadSlot(newLoad);
		newCargo.setDischargeSlot(newDischarge);

		final String name = "New cargo " + (newCargoes.size() + 1);

		newCargo.setName(name);
		newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newLoad.setName(name);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setName("discharge - " + name);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());

		newNames.add(name);

		cargoes.add(newCargo);
		// newTypes.add(newCargo.getCargoType().getName());
		newCargoes.add(newCargo);

		for (final Control c : getChildren()) {
			c.dispose();
		}

		createChildren();

		// restore buffered values (yuck)
		for (int i = 0; i < lhsPorts.size(); i++) {
			lhsComposites.get(i).setContract(lhsContracts.get(i));
			rhsComposites.get(i).setContract(rhsContracts.get(i));
			lhsComposites.get(i).setPort(lhsPorts.get(i));
			rhsComposites.get(i).setPort(rhsPorts.get(i));
			lhsComposites.get(i).setDate(lhsDates.get(i));
			rhsComposites.get(i).setDate(rhsDates.get(i));
			lhsComposites.get(i).setFOBOrDES(lhsFOBORDES.get(i));
			rhsComposites.get(i).setFOBOrDES(rhsFOBORDES.get(i));
		}

		layout();
	}

	private void createLayout() {
		final GridLayout layout = new GridLayout();

		layout.numColumns = 4;
		// layout.makeColumnsEqualWidth = false;

		setLayout(layout);
	}

	private class PortAndDateComposite extends Composite implements SelectionListener {
		private static final int LONG_LENGTH = 20;
		public final DateTime dateTime;
		public final Combo contractCombo;
		public final Combo portCombo;
		private int hours;

		private boolean fobOrDES = false;

		List<Pair<String, EObject>> ports;
		final List<Pair<String, EObject>> contracts;
		private final Label fobDESLabel;
		private final IReferenceValueProvider portValueProvider;

		private final boolean isLoad;

		/**
		 * Argh localized dates.
		 * 
		 * @param parent
		 * @param style
		 */
		public PortAndDateComposite(final Composite parent, final int style, final boolean isLoad, final IReferenceValueProvider portValueProvider, final IReferenceValueProvider contractReferenceProvider,
				final Slot slot) {
			super(parent, style);
			this.isLoad = isLoad;
			this.portValueProvider = portValueProvider;
			setLayout(new GridLayout(4, false));
			fobDESLabel = new Label(this, SWT.NONE);
			contractCombo = new Combo(this, SWT.READ_ONLY);
			portCombo = new Combo(this, SWT.READ_ONLY);
			dateTime = new DateTime(this, SWT.MEDIUM | SWT.DATE);
			ports = portValueProvider.getAllowedValues(slot, CargoPackage.eINSTANCE.getSlot_Port());
			contracts = contractReferenceProvider.getAllowedValues(slot, CargoPackage.eINSTANCE.getSlot_Contract());

			// trim port names
			for (final Pair<String, EObject> p : ports) {
				portCombo.add(shorten(p.getFirst()));
			}

			for (final Pair<String, EObject> p : contracts) {
				contractCombo.add(shorten(p.getFirst()));
			}

			setContract(slot.getContract());
			setPort(slot.getPort());
			setDate(slot.getWindowStart());
			if (slot instanceof LoadSlot && ((LoadSlot) slot).isDESPurchase()) {
				fobDESLabel.setText("DES Purchase");
				setFOBOrDES(true);
			} else if (slot instanceof DischargeSlot && ((DischargeSlot) slot).isFOBSale()) {
				fobDESLabel.setText("FOB Sale");
				setFOBOrDES(true);
			} else {
				fobDESLabel.setText("        ");
				setFOBOrDES(false);
			}
			portCombo.addSelectionListener(this);
			contractCombo.addSelectionListener(this);
			dateTime.addSelectionListener(this);
		}

		private String shorten(final String longString) {
			if (longString.length() > LONG_LENGTH + 3) {
				return longString.substring(0, LONG_LENGTH) + "...";
			}
			return longString;
		}

		/**
		 * @param date
		 */
		public void setDate(final Date date) {
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(getPort().getTimeZone()));
			cal.clear();
			cal.setTime(date);

			dateTime.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

			hours = cal.get(Calendar.HOUR_OF_DAY);
		}

		/**
		 * @param port
		 */
		public void setPort(final Port port) {
			portCombo.setText(shorten(port.getName()));
		}

		/**
		 * @param contract
		 */
		public void setContract(final Contract contract) {
			contractCombo.setText(shorten(contract.getName()));
			
			updatePorts(contract);
		}
		
		protected void updatePorts(final Contract contract) {
			// Create a fake slot to pass into the value provider
			final Slot slot = isLoad ? CargoFactory.eINSTANCE.createLoadSlot() : CargoFactory.eINSTANCE.createDischargeSlot();
			slot.setContract(contract);
			ports = portValueProvider.getAllowedValues(slot, CargoPackage.eINSTANCE.getSlot_Port());

			// Keep the old name in case we can reuse it
			final String oldName = portCombo.getText();
			// Clear existing names

			portCombo.removeAll();
			// Add updated ports list
			for (final Pair<String, EObject> p : ports) {
				portCombo.add(shorten(p.getFirst()));
			}
			// Set an initial value...
			portCombo.setText(shorten(ports.get(0).getFirst()));
			// ... then try and restore port name
			portCombo.setText(oldName);
		}

		public Date getDate() {
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(getPort().getTimeZone()));
			cal.clear();
			cal.set(Calendar.YEAR, dateTime.getYear());
			cal.set(Calendar.MONTH, dateTime.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
			cal.set(Calendar.HOUR_OF_DAY, hours);
			return cal.getTime();
		}

		public Port getPort() {
			int selectionIndex = portCombo.getSelectionIndex();
			if (selectionIndex == -1) {
				selectionIndex = 0;
			}
			return (Port) ports.get(selectionIndex).getSecond();
		}

		public Contract getContract() {
			return (Contract) contracts.get(contractCombo.getSelectionIndex()).getSecond();
		}

		@Override
		public void widgetSelected(final SelectionEvent e) {
			notifyListeners(SWT.Selection, new Event());
			
			if (e.widget == contractCombo) {
				updatePorts(getContract());
			}
		}

		@Override
		public void widgetDefaultSelected(final SelectionEvent e) {
			notifyListeners(SWT.DefaultSelection, new Event());
		}

		public boolean isFOBOrDES() {
			return fobOrDES;
		}

		public void setFOBOrDES(final boolean isFOBOrDes) {
			this.fobOrDES = isFOBOrDes;
			fobDESLabel.setVisible(isFOBOrDes);
			// contractCombo.setVisible(!isFOBOrDes);
			portCombo.setVisible(!isFOBOrDes);
			dateTime.setVisible(!isFOBOrDes);
		}
	}

	private void updateWiringColours(final WiringDiagram diagram, final List<Integer> wiring, final List<PortAndDateComposite> loads, final List<PortAndDateComposite> discharge) {
		final Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

		for (int i = 0; i < wiring.size() - 2; i++) {
			diagram.setLeftTerminalColor(i, red);
			diagram.setRightTerminalColor(i, red);
		}
		for (int i = 0; i < wiring.size() - 2; i++) {
			final int j = wiring.get(i);
			if (j == -1)
				continue;
			diagram.setLeftTerminalColor(i, green);
			diagram.setRightTerminalColor(j, green);

			// check dates
			final Date loadDate = loads.get(i).getDate();
			final Date dischargeDate = discharge.get(j).getDate();

			// TODO this is not exactly proper validation.
			if (dischargeDate.before(loadDate)) {
				diagram.setWireColor(i, red);
			} else {
				diagram.setWireColor(i, black);
			}
		}
		diagram.redraw();
	}

	private void createChildren() {
		lhsComposites.clear();
		rhsComposites.clear();

		WiringDiagram wiringDiagram = null;
		Listener selectionListener = null;
		int index = 0;
		for (final Cargo cargo : cargoes) {
			final int finalIndex = index;
			final Text idLabel = new Text(this, SWT.RIGHT | SWT.BORDER);

			idLabel.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(final ModifyEvent e) {
					newNames.set(finalIndex, idLabel.getText());
				}
			});

			// final Combo typeCombo = new Combo(this, SWT.READ_ONLY);
			// for (final CargoType type : CargoType.values()) {
			// typeCombo.add(type.getName());
			// }
			//
			// typeCombo.setText(newTypes.get(index));
			//
			// typeCombo.addSelectionListener(new SelectionListener() {
			// void apply() {
			// newTypes.set(finalIndex, typeCombo.getText());
			// }
			//
			// @Override
			// public void widgetSelected(SelectionEvent e) {
			// apply();
			// }
			//
			// @Override
			// public void widgetDefaultSelected(SelectionEvent e) {
			// apply();
			// }
			// });

			final PortAndDateComposite loadSide = new PortAndDateComposite(this, SWT.BORDER, true, portProvider, contractProvider, cargo.getLoadSlot());

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

						// check for wiring to add terminal
						final int topIndex = newWiring.size() - 2;
						final int bottomIndex = newWiring.size() - 1;
						if (newWiring.get(topIndex) != -1 || newWiring.indexOf(topIndex) != -1) {
							addNewCargo(new ArrayList<Integer>(newWiring), false);
						} else if (newWiring.get(bottomIndex) != -1 || newWiring.indexOf(bottomIndex) != -1) {

							if (newWiring.get(bottomIndex) != -1) {
								newWiring.set(newWiring.size() - 2, newWiring.get(bottomIndex));
								newWiring.set(newWiring.size() - 1, -1);
							} else {
								newWiring.set(newWiring.indexOf(bottomIndex), newWiring.size() - 2);

							}
							addNewCargo(new ArrayList<Integer>(newWiring), true);
						} else {
							WiringComposite.this.wiring.clear();
							WiringComposite.this.wiring.addAll(newWiring);
							updateWiringColours(this, newWiring, lhsComposites, rhsComposites);
						}
						WiringComposite.this.notifyListeners(SWT.Modify, new Event());
					}
				};
				// wiring diagram is tall
				wiringDiagram.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, cargoes.size() + 2));

				final WiringDiagram wiringDiagram2 = wiringDiagram;

				selectionListener = new Listener() {
					@Override
					public void handleEvent(final Event event) {
						updateWiringColours(wiringDiagram2, wiringDiagram2.getWiring(), lhsComposites, rhsComposites);
					}
				};
			}

			final PortAndDateComposite dischargeSide = new PortAndDateComposite(this, SWT.BORDER, false, portProvider, contractProvider, cargo.getDischargeSlot());

			final String id = newNames.get(index);

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

		// set base values for colours, optional and wiring
		wiringDiagram.setWiring(wiring);
		final ArrayList<Pair<Color, Color>> terminalColors = new ArrayList<Pair<Color, Color>>();
		final ArrayList<Pair<Boolean,Boolean>> terminalOptionals = new ArrayList<Pair<Boolean, Boolean>>();
		final ArrayList<Color> wireColors = new ArrayList<Color>();
		final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		for (final Cargo c : cargoes) {
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

			final PortAndDateComposite load = lhsComposites.get(loadIdx);
			final PortAndDateComposite discharge = rhsComposites.get(dischargeIdx);

			if (load.isFOBOrDES() && discharge.isFOBOrDES()) {
				return false;
			}
		}

		return true;
	}

	public Command createApplyCommand(final EditingDomain domain, final MMXRootObject rootObject) {
		final CompoundCommand command = new CompoundCommand();

		// update modified properties
		int index = 0;
		for (final Cargo cargo : cargoes) {
			final boolean isDESPurchase = lhsComposites.get(index).isFOBOrDES();
			final boolean isFOBSale = rhsComposites.get(index).isFOBOrDES();

			appendIfChanged(command, domain, cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name(), newNames.get(index));
			appendIfChanged(command, domain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getLoadSlot_DESPurchase(), isDESPurchase);
			if (isDESPurchase) {
				appendIfChanged(command, domain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Port(), rhsComposites.get(index).getPort());
				appendIfChanged(command, domain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), rhsComposites.get(index).getDate());
			} else {
				appendIfChanged(command, domain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Port(), lhsComposites.get(index).getPort());
				appendIfChanged(command, domain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), lhsComposites.get(index).getDate());
			}

			appendIfChanged(command, domain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getDischargeSlot_FOBSale(), isFOBSale);
			if (isFOBSale) {
				appendIfChanged(command, domain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_Port(), lhsComposites.get(index).getPort());
				appendIfChanged(command, domain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), lhsComposites.get(index).getDate());
			} else {
				appendIfChanged(command, domain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_Port(), rhsComposites.get(index).getPort());
				appendIfChanged(command, domain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), rhsComposites.get(index).getDate());
			}

			index++;
		}

		// todo sort out new cargo slots.

		for (int i = 0; i < wiring.size() - 2; i++) {
			final int newTail = wiring.get(i);
			if (newTail != i) {
				final Slot slot = cargoes.get(newTail).getDischargeSlot();
				final Cargo cargo = cargoes.get(i);
				command.append(SetCommand.create(domain, cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), slot));

				final boolean isDESPurchase = cargo.getLoadSlot().isDESPurchase();
				final boolean isFOBSale = ((DischargeSlot) slot).isFOBSale();

				if (isFOBSale && isDESPurchase) {
					throw new IllegalStateException("Cannot pair DES Purchase to FOB Sale");
				}

				if (isFOBSale || isDESPurchase) {
					command.append(AssignmentEditorHelper.unassignElement(domain, rootObject.getSubModel(InputModel.class), cargo));
				}
			}
		}

		// add new cargoes back into container
		final EObject container = cargoes.get(0).eContainer();
		final EStructuralFeature feature = cargoes.get(0).eContainingFeature();
		if (!newCargoes.isEmpty()) {
			command.append(AddCommand.create(domain, container, feature, newCargoes));
			for (final Cargo cargo : newCargoes) {
				command.append(AddCommand.create(domain, container, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargo.getLoadSlot()));
				command.append(AddCommand.create(domain, container, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargo.getDischargeSlot()));
			}
		}

		// scenario's cargo model.

		return command;
	}

	private void appendIfChanged(final CompoundCommand command, final EditingDomain domain, final EObject owner, final EStructuralFeature feature, final Object newValue) {
		if (!Equality.isEqual(owner.eGet(feature), newValue)) {
			command.append(SetCommand.create(domain, owner, feature, newValue));
		}
	}

	/**
	 * Set the portProvider
	 * 
	 * @param portProvider
	 */
	public void setPortProvider(final IReferenceValueProvider portProvider) {
		this.portProvider = portProvider;
	}

	/**
	 * Set the contractProvider
	 * 
	 * @param contractProvider
	 */
	public void setContractProvider(final IReferenceValueProvider contractProvider) {
		this.contractProvider = contractProvider;
	}
}
