/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProvider;

/**
 * A composite for displaying a wiring editor. Contains a {@link WiringDiagram},
 * which lets the user view and edit a matching in a bipartite graph, and
 * provides the interfacing logic to apply a new matching to the cargos passed
 * in through {@link #setCargos(List)}.
 * 
 * The {@link #createApplyCommand(EditingDomain)} applies the changes.
 * 
 * @author Tom Hinton
 * 
 */
public class WiringComposite extends Composite {
	private final ArrayList<Cargo> cargos = new ArrayList<Cargo>();
	private final ArrayList<Integer> wiring = new ArrayList<Integer>();

	final List<PortAndDateComposite> lhsComposites = new ArrayList<PortAndDateComposite>(
			cargos.size());
	final List<PortAndDateComposite> rhsComposites = new ArrayList<PortAndDateComposite>(
			cargos.size());

	final List<String> newNames = new ArrayList<String>();
//	final List<String> newTypes = new ArrayList<String>();

	final List<Cargo> newCargos = new LinkedList<Cargo>();
	
	private IReferenceValueProvider portProvider;

	/**
	 * @param parent
	 * @param style
	 */
	public WiringComposite(Composite parent, int style) {
		super(parent, style);
		createLayout();
		// setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}

	/**
	 * Set the cargos, and reset the wiring to match these cargos.
	 * 
	 * @param cargos
	 */
	public void setCargos(final List<Cargo> cargos) {
		// delete existing children
		for (final Control c : getChildren()) {
			c.dispose();
		}
		wiring.clear();
		this.cargos.clear();
		this.cargos.addAll(cargos);
		for (int i = 0; i < cargos.size(); i++) {
			wiring.add(i); // set default wiring
		}

		wiring.add(-1); // bogus element for the add terminals.

		newNames.clear();
//		newTypes.clear();
		for (final Cargo c : cargos) {
			newNames.add(c.getName());
//			newTypes.add(c.getCargoType().getName());
		}

		createChildren();
	}

	public void addNewCargo(final List<Integer> newWiring) {
		// buffer LHS and RHS values (yuck)
		final List<Port> lhsPorts = new ArrayList<Port>(lhsComposites.size());
		final List<Port> rhsPorts = new ArrayList<Port>(rhsComposites.size());
		final List<Date> lhsDates = new ArrayList<Date>(lhsComposites.size());
		final List<Date> rhsDates = new ArrayList<Date>(rhsComposites.size());

		for (final PortAndDateComposite c : lhsComposites) {
			lhsPorts.add(c.getPort());
			lhsDates.add(c.getDate());
		}

		for (final PortAndDateComposite c : rhsComposites) {
			rhsPorts.add(c.getPort());
			rhsDates.add(c.getDate());
		}

		if (newWiring != null) {
			wiring.clear();
			wiring.addAll(newWiring);
		}
		
		wiring.add(-1); // preserve bogus element
		// duplicate last cargo?
		final Cargo newCargo = EcoreUtil.copy(cargos.get(cargos.size()-1));
		
		final String name = "New cargo " + (newCargos.size() + 1);
		
		newCargo.setName(name);

		newNames.add(name);
		
		cargos.add(newCargo);
//		newTypes.add(newCargo.getCargoType().getName());
		newCargos.add(newCargo);

		for (final Control c : getChildren()) {
			c.dispose();
		}

		createChildren();

		// restore buffered values (yuck)
		for (int i = 0; i < lhsPorts.size(); i++) {
			lhsComposites.get(i).setPort(lhsPorts.get(i));
			rhsComposites.get(i).setPort(rhsPorts.get(i));
			lhsComposites.get(i).setDate(lhsDates.get(i));
			rhsComposites.get(i).setDate(rhsDates.get(i));
		}

		layout();
	}

	private void createLayout() {
		final GridLayout layout = new GridLayout();

		layout.numColumns = 4;
		// layout.makeColumnsEqualWidth = false;

		setLayout(layout);
	}

	private class PortAndDateComposite extends Composite implements
			SelectionListener {
		private static final int LONG_LENGTH = 20;
		public final DateTime dateTime;
		public final Combo combo;
		private int hours;

		final List<Pair<String, EObject>> ports;

		/**
		 * Argh localized dates.
		 * 
		 * @param parent
		 * @param style
		 */
		public PortAndDateComposite(Composite parent, int style,
				final IReferenceValueProvider rvp, final Slot slot) {
			super(parent, style);
			setLayout(new GridLayout(2, false));
			combo = new Combo(this, SWT.READ_ONLY);
			dateTime = new DateTime(this, SWT.MEDIUM | SWT.DATE);
			ports = rvp.getAllowedValues(slot,
					CargoPackage.eINSTANCE.getSlot_Port());
			
			// trim port names
			
			for (final Pair<String, EObject> p : ports) {
				combo.add(shorten(p.getFirst()));
			}

			setPort(slot.getPort());
			setDate(slot.getWindowStart());
			combo.addSelectionListener(this);
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
		public void setDate(Date date) {
			final Calendar cal = Calendar.getInstance(TimeZone
					.getTimeZone(getPort().getTimeZone()));
			cal.clear();
			cal.setTime(date);

			dateTime.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH));

			hours = cal.get(Calendar.HOUR_OF_DAY);
		}

		/**
		 * @param port
		 */
		public void setPort(Port port) {
			combo.setText(shorten(port.getName()));
		}

		public Date getDate() {
			final Calendar cal = Calendar.getInstance(TimeZone
					.getTimeZone(getPort().getTimeZone()));
			cal.clear();
			cal.set(Calendar.YEAR, dateTime.getYear());
			cal.set(Calendar.MONTH, dateTime.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
			cal.set(Calendar.HOUR_OF_DAY, hours);
			return cal.getTime();
		}

		public Port getPort() {
			return (Port) ports.get(combo.getSelectionIndex()).getSecond();
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			notifyListeners(SWT.Selection, new Event());
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			notifyListeners(SWT.DefaultSelection, new Event());
		}
	}

	private void updateWiringColours(final WiringDiagram diagram,
			final List<Integer> wiring, final List<PortAndDateComposite> loads,
			final List<PortAndDateComposite> discharge) {
		final Color red = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		final Color green = Display.getCurrent()
				.getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent()
				.getSystemColor(SWT.COLOR_BLACK);

		for (int i = 0; i < wiring.size() - 1; i++) {
			diagram.setLeftTerminalColor(i, red);
			diagram.setRightTerminalColor(i, red);
		}
		for (int i = 0; i < wiring.size() - 1; i++) {
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
		for (final Cargo cargo : cargos) {
			final int finalIndex = index;
			final Text idLabel = new Text(this, SWT.RIGHT | SWT.BORDER);

			idLabel.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					newNames.set(finalIndex, idLabel.getText());
				}
			});

//			final Combo typeCombo = new Combo(this, SWT.READ_ONLY);
//			for (final CargoType type : CargoType.values()) {
//				typeCombo.add(type.getName());
//			}
//
//			typeCombo.setText(newTypes.get(index));
//
//			typeCombo.addSelectionListener(new SelectionListener() {
//				void apply() {
//					newTypes.set(finalIndex, typeCombo.getText());
//				}
//
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					apply();
//				}
//
//				@Override
//				public void widgetDefaultSelected(SelectionEvent e) {
//					apply();
//				}
//			});

			final PortAndDateComposite loadSide = new PortAndDateComposite(
					this, SWT.NONE, portProvider, cargo.getLoadSlot());

			if (wiringDiagram == null) {
				wiringDiagram = new WiringDiagram(this, getStyle()
						& ~SWT.BORDER) {
					@Override
					protected List<Float> getTerminalPositions() {
						final float littleOffset = getBounds().y;
						final ArrayList<Float> vMidPoints = new ArrayList<Float>(
								cargos.size());
						// get vertical coordinates of labels

						float lastMidpointButOne = 0;
						float lastMidpoint = 0;

						for (final Composite l : lhsComposites) {
							final Rectangle lbounds = l.getBounds();
							lastMidpointButOne = lastMidpoint;
							lastMidpoint = -littleOffset + lbounds.y
									+ lbounds.height / 2.0f;
							vMidPoints.add(lastMidpoint);
						}

						vMidPoints.add(lastMidpoint
								+ (lastMidpoint - lastMidpointButOne));

						return vMidPoints;
					}

					@Override
					protected void wiringChanged(final List<Integer> newWiring) {
						// check for wiring to add terminal
						int addIndex = newWiring.size() - 1;
						if (newWiring.get(addIndex) != -1
								|| newWiring.indexOf(addIndex) != -1) {
							addNewCargo(new ArrayList<Integer>(newWiring));
						} else {
							WiringComposite.this.wiring.clear();
							WiringComposite.this.wiring.addAll(newWiring);
							updateWiringColours(this, newWiring, lhsComposites,
									rhsComposites);
						}
						WiringComposite.this.notifyListeners(SWT.Modify,
								new Event());
					}
				};
				// wiring diagram is tall
				wiringDiagram.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
						true, true, 1, cargos.size() + 1));

				final WiringDiagram wiringDiagram2 = wiringDiagram;

				selectionListener = new Listener() {
					@Override
					public void handleEvent(Event event) {
						updateWiringColours(wiringDiagram2,
								wiringDiagram2.getWiring(), lhsComposites,
								rhsComposites);
					}
				};
			}

			final PortAndDateComposite dischargeSide = new PortAndDateComposite(
					this, SWT.NONE, portProvider, cargo.getDischargeSlot());

			final String id = newNames.get(index);

			lhsComposites.add(loadSide);
			rhsComposites.add(dischargeSide);
			idLabel.setText(id);

			loadSide.addListener(SWT.Selection, selectionListener);
			dischargeSide.addListener(SWT.Selection, selectionListener);

			loadSide.addListener(SWT.DefaultSelection, selectionListener);
			dischargeSide.addListener(SWT.DefaultSelection, selectionListener);

			idLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
					false));
			loadSide.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
					false));
			dischargeSide.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER,
					false, false));

			index++;
		}

		// add bogus packing label
		new Label(this, SWT.NONE).setLayoutData(new GridData(
				GridData.FILL_VERTICAL));
		new Label(this, SWT.NONE).setLayoutData(new GridData(
				GridData.FILL_VERTICAL));

		wiringDiagram.setWiring(wiring);
		final ArrayList<Pair<Color, Color>> terminalColors = new ArrayList<Pair<Color, Color>>();
		final ArrayList<Color> wireColors = new ArrayList<Color>();
		final Color green = Display.getCurrent()
				.getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getCurrent()
				.getSystemColor(SWT.COLOR_BLACK);
		for (final Cargo cargo : cargos) {
			terminalColors.add(new Pair<Color, Color>(green, green));
			wireColors.add(black);
		}

		final Color addColor = Display.getCurrent().getSystemColor(
				SWT.COLOR_YELLOW);

		terminalColors.add(new Pair<Color, Color>(addColor, addColor));

		wiringDiagram.setWireColors(wireColors);
		wiringDiagram.setTerminalColors(terminalColors);
		updateWiringColours(wiringDiagram, wiring, lhsComposites, lhsComposites);
	}

	/*
	 * TODO highlight lines when mouseover labels TODO feedback validation state
	 * to containing dialog TODO generate command to apply changes.
	 */

	/**
	 * @return
	 */
	public boolean isWiringFeasible() {
		return wiring.indexOf(-1) == wiring.size() - 1;
	}

	public Command createApplyCommand(final EditingDomain domain) {
		final CompoundCommand command = new CompoundCommand();
		
		// update modified properties
		int index = 0;
		for (final Cargo cargo : cargos) {
			command.append(SetCommand.create(domain, cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name(), newNames.get(index)));			
			command.append(SetCommand.create(domain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Port(), lhsComposites.get(index).getPort()));
			command.append(SetCommand.create(domain, cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), lhsComposites.get(index).getDate()));
			command.append(SetCommand.create(domain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_Port(), rhsComposites.get(index).getPort()));
			command.append(SetCommand.create(domain, cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_WindowStart(), rhsComposites.get(index).getDate()));
			
			index++;
		}
		
		// todo sort out new cargo slots.
		
		for (int i = 0; i < wiring.size() - 1; i++) {
			int newTail = wiring.get(i);
			if (newTail != i) {
				final Slot slot = cargos.get(newTail).getDischargeSlot();
				final Cargo cargo = cargos.get(i);
				command.append(SetCommand.create(domain, cargo,
						CargoPackage.eINSTANCE.getCargo_DischargeSlot(), slot));
			}
		}

		// add new cargos back into container
		final EObject container = cargos.get(0).eContainer();
		final EStructuralFeature feature = cargos.get(0).eContainingFeature();
		if (!newCargos.isEmpty())
			command.append(AddCommand.create(domain, container, feature, newCargos));
		
		// scenario's cargo model.

		return command;
	}

	/**
	 * Set the portProvider
	 * 
	 * @param portProvider
	 */
	public void setPortProvider(IReferenceValueProvider portProvider) {
		this.portProvider = portProvider;
	}
}
