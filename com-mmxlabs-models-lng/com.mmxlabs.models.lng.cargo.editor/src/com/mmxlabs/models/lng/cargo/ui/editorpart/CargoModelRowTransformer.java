package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.util.emfpath.EMFPath;

public class CargoModelRowTransformer {
	final Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	final Color darkRed = Display.getDefault().getSystemColor(SWT.COLOR_DARK_RED);
	final Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
	final Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	final Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	private final Comparator<? extends Slot> slotComparator = new Comparator<Slot>() {

		@Override
		public int compare(final Slot o1, final Slot o2) {
			if (o1 == null) {
				return -1;
			} else if (o2 == null) {
				return 1;
			} else {
				final Date d1 = o1.getWindowStartWithSlotOrPortTime();
				final Date d2 = o1.getWindowStartWithSlotOrPortTime();

				if (d1 == null) {
					return -1;
				} else if (d2 == null) {
					return 1;
				} else {
					return d1.compareTo(d2);
				}
			}
		}
	};

	public RootData transform(final InputModel inputModel, final CargoModel cargoModel, final Map<Object, IStatus> validationInfo) {
		return transform(inputModel, cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots(), validationInfo);

	}

	public RootData transform(final InputModel inputModel, final List<Cargo> cargoes, final List<LoadSlot> allLoadSlots, final List<DischargeSlot> allDischargeSlots,
			final Map<Object, IStatus> validationInfo) {
		System.out.println("trasform");

		final RootData root = new RootData();

		for (final Cargo cargo : cargoes) {
			final GroupData group = new GroupData();
			root.getGroups().add(group);
			group.getObjects().add(cargo);

			final List<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
			final List<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();

			for (final Object slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					loadSlots.add((LoadSlot) slot);
				} else if (slot instanceof DischargeSlot) {
					dischargeSlots.add((DischargeSlot) slot);
				} else {
					// Assume some kind of discharge?
					// dischargeSlots.add((Slot) slot);
				}

			}

			for (final Object slot : cargo.getSlots()) {

				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					for (final Object slot2 : cargo.getSlots()) {
						if (slot2 instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot2;
							final WireData wire = new WireData();
							wire.loadSlot = loadSlot;
							wire.dischargeSlot = dischargeSlot;
							group.getWires().add(wire);
						}
					}
				}
			}
			setWiringColour(validationInfo, group);

			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {
				final RowData row = new RowData();
				row.setGroup(group);
				group.getRows().add(row);
				root.getRows().add(row);

				row.cargo = cargo;
				if (i < loadSlots.size()) {
					row.loadSlot = loadSlots.get(i);
				}
				if (i < dischargeSlots.size()) {
					row.dischargeSlot = dischargeSlots.get(i);
				}
				final ElementAssignment elementAssignment = AssignmentEditorHelper.getElementAssignment(inputModel, cargo);
				if (elementAssignment != null) {
					row.elementAssignment = elementAssignment;
				}

				row.loadTerminalColour = green;
				row.dischargeTerminalColour = green;

				for (final WireData wire : group.getWires()) {
					if (wire.loadSlot != null && wire.loadSlot == row.loadSlot) {
						wire.loadRowData = row;
					}
					if (wire.dischargeSlot != null && wire.dischargeSlot == row.dischargeSlot) {
						wire.dischargeRowData = row;
					}
				}
			}

		}

		for (final LoadSlot slot : allLoadSlots) {
			if (slot.getCargo() == null) {

				final GroupData group = new GroupData();
				root.getGroups().add(group);
				group.getObjects().add(slot);

				final RowData row = new RowData();
				row.setGroup(group);
				root.getRows().add(row);
				row.loadSlot = slot;

				row.loadTerminalColour = red;
				row.dischargeTerminalColour = red;
			}
		}

		for (final DischargeSlot slot : allDischargeSlots) {
			if (slot.getCargo() == null) {

				final GroupData group = new GroupData();
				root.getGroups().add(group);
				group.getObjects().add(slot);

				final RowData row = new RowData();
				row.setGroup(group);
				root.getRows().add(row);
				row.dischargeSlot = slot;

				row.loadTerminalColour = red;
				row.dischargeTerminalColour = red;
			}
		}

		for (final RowData rd : root.getRows()) {
			root.getCargoes().add(rd.getCargo());
			root.getLoadSlots().add(rd.getLoadSlot());
			root.getDischargeSlots().add(rd.getDischargeSlot());

		}

		return root;
	}

	public static class RowData extends EObjectImpl {

		GroupData group;
		Cargo cargo;
		LoadSlot loadSlot;
		DischargeSlot dischargeSlot;
		ElementAssignment elementAssignment;

		// GUI STATE
		Color loadTerminalColour;
		Color dischargeTerminalColour;

		/**
		 * This is used in the {@link EObjectTableViewer} implementation of {@link ViewerComparator} for the fixed sort order.
		 */
		@Override
		public boolean equals(final Object obj) {

			if (obj instanceof RowData) {
				final RowData other = (RowData) obj;
				return Equality.isEqual(cargo, other.cargo) && Equality.isEqual(loadSlot, other.loadSlot) && Equality.isEqual(dischargeSlot, other.dischargeSlot)
						&& Equality.isEqual(elementAssignment, other.elementAssignment);
			}

			return false;
		}

		public void setCargo(final Cargo cargo) {
			this.cargo = cargo;
		}

		public void setLoadSlot(final LoadSlot loadSlot) {
			this.loadSlot = loadSlot;
		}

		public void setDischargeSlot(final DischargeSlot dischargeSlot) {
			this.dischargeSlot = dischargeSlot;
		}

		public void setElementAssignment(final ElementAssignment elementAssignment) {
			this.elementAssignment = elementAssignment;
		}

		public GroupData getGroup() {
			return group;
		}

		public Cargo getCargo() {
			return cargo;
		}

		public LoadSlot getLoadSlot() {
			return loadSlot;
		}

		public DischargeSlot getDischargeSlot() {
			return dischargeSlot;
		}

		public ElementAssignment getElementAssignment() {
			return elementAssignment;
		}

		public void setGroup(final GroupData group) {
			this.group = group;
		}

	}

	public static class WireData {

		Color colour;
		LoadSlot loadSlot;
		RowData loadRowData;
		DischargeSlot dischargeSlot;
		RowData dischargeRowData;

	}

	public static class GroupData extends EObjectImpl {

		private final List<RowData> rows = new ArrayList<RowData>();
		private final List<EObject> objects = new ArrayList<EObject>();
		private final List<WireData> wires = new ArrayList<WireData>();

		public List<EObject> getObjects() {
			return objects;
		}

		public List<RowData> getRows() {
			return rows;
		}

		public List<WireData> getWires() {
			return wires;
		}

	}

	public static class RootData extends EObjectImpl {

		private final List<RowData> rows = new ArrayList<RowData>();
		private final List<GroupData> groups = new ArrayList<GroupData>();

		private final ArrayList<Cargo> cargoes = new ArrayList<Cargo>();
		private final ArrayList<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
		private final ArrayList<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();

		public List<GroupData> getGroups() {
			return groups;
		}

		public List<RowData> getRows() {
			return rows;
		}

		public ArrayList<Cargo> getCargoes() {
			return cargoes;
		}

		public ArrayList<LoadSlot> getLoadSlots() {
			return loadSlots;
		}

		public ArrayList<DischargeSlot> getDischargeSlots() {
			return dischargeSlots;
		}

	}

	public static class RowDataEMFPath extends EMFPath {

		private final Type type;

		public RowDataEMFPath(final Type type, final boolean failSilently, final Iterable<?> path) {
			super(failSilently, path);
			this.type = type;
		}

		public RowDataEMFPath(final Type type, final boolean failSilently, final Object... path) {
			super(failSilently, path);
			this.type = type;
		}

		@Override
		public Object get(final EObject root, final int depth) {

			if (root instanceof RowData) {
				switch (type) {
				case ASSIGNMENT:
					return super.get(((RowData) root).elementAssignment, depth);
				case CARGO:
					return super.get(((RowData) root).cargo, depth);
				case DISCHARGE:
					return super.get(((RowData) root).dischargeSlot, depth);
				case LOAD:
					return super.get(((RowData) root).loadSlot, depth);
				}
			}
			return super.get(root, depth);
		}
	}

	public enum Type {
		CARGO, LOAD, DISCHARGE, ASSIGNMENT
	}

	public void updateWiringValidity(final RootData rootData, final Map<Object, IStatus> validationInformation) {

		for (final GroupData g : rootData.getGroups()) {
			setWiringColour(validationInformation, g);
		}
	}

	private void setWiringColour(final Map<Object, IStatus> validationInformation, final GroupData g) {
		boolean validWire = true;
		Cargo c = null;
		for (final Object obj : g.getObjects()) {

			if (obj instanceof Cargo) {
				c = (Cargo) obj;
			}

			if (validationInformation.containsKey(obj)) {
				final IStatus status = validationInformation.get(obj);
				if (status.matches(IStatus.ERROR)) {
					validWire = false;
				}
			}
		}
		if (c != null) {
			for (final WireData wire : g.getWires()) {
				wire.colour = validWire ? (c.isAllowRewiring() ? gray : black) : darkRed;
			}
		}
	}
}