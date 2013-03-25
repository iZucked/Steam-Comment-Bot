package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jface.viewers.ViewerComparator;

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

	public RootData transform(final InputModel inputModel, final CargoModel cargoModel) {
		return transform(inputModel, cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots());

	}

	public RootData transform(final InputModel inputModel, final List<Cargo> cargoes, final List<LoadSlot> allLoadSlots, final List<DischargeSlot> allDischargeSlots) {

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

			for (int i = 0; i < Math.max(loadSlots.size(), dischargeSlots.size()); ++i) {
				final RowData row = new RowData();
				row.setGroup(group);
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

	public static class GroupData extends EObjectImpl {

		private final List<RowData> rows = new ArrayList<RowData>();
		private final List<EObject> objects = new ArrayList<EObject>();

		public List<EObject> getObjects() {
			return objects;
		}

		public List<RowData> getRows() {
			return rows;
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
}