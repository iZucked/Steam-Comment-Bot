/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.contingencyeditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;

public class ContingencyMatrixViewer extends GridTableViewer {
	/**
	 * @param parent
	 */
	public ContingencyMatrixViewer(final Composite parent) {
		super(parent);
	}

	/**
	 * @param table
	 */
	public ContingencyMatrixViewer(final Table table) {
		super(table);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ContingencyMatrixViewer(final Composite parent, final int style) {
		super(parent, style);
	}

	/**
	 */
	public void init(final EditingDomain editingDomain, final LNGScenarioModel scenario) {
		setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object inputElement) {
				if (inputElement instanceof ContingencyMatrix) {
					final ContingencyMatrix dm = (ContingencyMatrix) inputElement;
					final Map<Port, Map<Port, ContingencyMatrixEntry>> values = new HashMap<>();
					final PortModel portModel = ScenarioModelUtil.getPortModel(scenario);
					for (final Port p : portModel.getPorts()) {
						values.put(p, new HashMap<>());
					}

					for (final ContingencyMatrixEntry dl : dm.getEntries()) {
						values.get(dl.getFromPort()).put(dl.getToPort(), dl);
					}

					final List<Pair<Port, Map<Port, ContingencyMatrixEntry>>> output = new ArrayList<>();
					for (final Map.Entry<Port, Map<Port, ContingencyMatrixEntry>> entry : values.entrySet()) {
						output.add(new Pair<>(entry.getKey(), entry.getValue()));
					}

					Collections.sort(output, (o1, o2) -> o1.getFirst().getName().compareTo(o2.getFirst().getName()));

					return output.toArray();
				}
				return new Object[0];
			}
		});

		final ArrayList<Port> ports = new ArrayList<>();
		final PortModel portModel = scenario.getReferenceModel().getPortModel();
		ports.addAll(portModel.getPorts());
		Collections.sort(ports, (o1, o2) -> o1.getName().compareTo(o2.getName()));

		for (final Port p : ports) {

			final GridViewerColumn toColumn = new GridViewerColumn(this, SWT.NONE);
			toColumn.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			toColumn.getColumn().setText(p.getName());
			toColumn.getColumn().setResizeable(true);
			toColumn.getColumn().pack();

			toColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					final Pair<Port, Map<Port, ContingencyMatrixEntry>> e = (Pair<Port, Map<Port, ContingencyMatrixEntry>>) element;
					final ContingencyMatrixEntry dl = e.getSecond().get(p);
					if (dl != null) {
						return "" + dl.getDuration();
					} else {
						return "";
					}
				}
			});

			toColumn.setEditingSupport(new EditingSupport(this) {
				@Override
				protected void setValue(final Object element, final Object value) {
					final Pair<Port, Map<Port, ContingencyMatrixEntry>> e = (Pair<Port, Map<Port, ContingencyMatrixEntry>>) element;
					final String stringValue = value.toString();
					final Command command;
					ContingencyMatrixEntry dl = e.getSecond().get(p);

					if (stringValue.isEmpty()) {
						if (dl == null) {
							return;
						}
						command = RemoveCommand.create(editingDomain, dl);
						e.getSecond().remove(p);
					} else {
						if (dl == null) {
							dl = PortFactory.eINSTANCE.createContingencyMatrixEntry();
							dl.setFromPort(e.getFirst());
							dl.setToPort(p);
							dl.setDuration(Integer.parseInt(stringValue));
							e.getSecond().put(p, dl);
							command = AddCommand.create(editingDomain, getInput(), PortPackage.eINSTANCE.getContingencyMatrix_Entries(), dl);
						} else {
							command = SetCommand.create(editingDomain, dl, PortPackage.eINSTANCE.getContingencyMatrixEntry_Duration(), Integer.parseInt(stringValue));
						}
					}
					command.execute();
					refresh(element);
				}

				@Override
				protected Object getValue(final Object element) {
					final Pair<Port, Map<Port, ContingencyMatrixEntry>> e = (Pair<Port, Map<Port, ContingencyMatrixEntry>>) element;
					final ContingencyMatrixEntry dl = e.getSecond().get(p);
					if (dl == null) {
						return "";
					}
					return Integer.toString(dl.getDuration());
				}

				@Override
				protected CellEditor getCellEditor(final Object element) {
					final TextCellEditor tce = new TextCellEditor(getGrid());

					tce.setValidator(value -> {
						final String s = value.toString();
						if (s.isEmpty()) {
							return null;
						}
						try {
							final int i = Integer.parseInt(s);
							if (i < 0) {
								return s + " is negative";
							}
							return null;
						} catch (final NumberFormatException nfe) {
							return s + " is not an integer";
						}
					});

					return tce;
				}

				@Override
				protected boolean canEdit(final Object element) {
					return true;
				}
			});
		}

		setRowHeaderLabelProvider(new CellLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();

				final Pair<Port, Map<Port, ContingencyMatrixEntry>> e = (Pair<Port, Map<Port, ContingencyMatrixEntry>>) element;
				cell.setText(e.getFirst().getName());

				if (element instanceof ContingencyMatrixEntry) {
					final ContingencyMatrixEntry ContingencyMatrixEntry = (ContingencyMatrixEntry) element;
					cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				}
			}
		});
	}
}
