/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.ui.distanceeditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

/**
 * Set the input to a distance model, and this will let you edit it
 * 
 * @author Tom Hinton
 * 
 */
public class DistanceLineViewer extends GridTableViewer {
	/**
	 * @param parent
	 */
	public DistanceLineViewer(final Composite parent) {
		super(parent);
	}

	/**
	 * @param table
	 */
	public DistanceLineViewer(final Table table) {
		super(table);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public DistanceLineViewer(final Composite parent, final int style) {
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
				if (inputElement instanceof Route) {
					final Route dm = (Route) inputElement;
					final Map<Port, Map<Port, RouteLine>> values = new HashMap<Port, Map<Port, RouteLine>>();
					final PortModel portModel = scenario.getReferenceModel().getPortModel();
					for (final Port p : portModel.getPorts()) {
						values.put(p, new HashMap<Port, RouteLine>());
					}

					for (final RouteLine dl : dm.getLines()) {
						values.get(dl.getFrom()).put(dl.getTo(), dl);
					}

					final List<Pair<Port, Map<Port, RouteLine>>> output = new ArrayList<Pair<Port, Map<Port, RouteLine>>>();
					for (final Map.Entry<Port, Map<Port, RouteLine>> entry : values.entrySet()) {
						output.add(new Pair<Port, Map<Port, RouteLine>>(entry.getKey(), entry.getValue()));
					}

					Collections.sort(output, new Comparator<Pair<Port, Map<Port, RouteLine>>>() {
						@Override
						public int compare(final Pair<Port, Map<Port, RouteLine>> o1, final Pair<Port, Map<Port, RouteLine>> o2) {
							return o1.getFirst().getName().compareTo(o2.getFirst().getName());
						}
					});

					return output.toArray();
				}
				return new Object[0];
			}
		});

		final ArrayList<Port> ports = new ArrayList<Port>();
		final PortModel portModel = scenario.getReferenceModel().getPortModel();
		ports.addAll(portModel.getPorts());
		Collections.sort(ports, new Comparator<Port>() {
			@Override
			public int compare(final Port o1, final Port o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (final Port p : ports) {

			final GridViewerColumn toColumn = new GridViewerColumn(this, SWT.NONE);

			toColumn.getColumn().setText(p.getName());

			toColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					final Pair<Port, Map<Port, RouteLine>> e = (Pair<Port, Map<Port, RouteLine>>) element;
					final RouteLine dl = e.getSecond().get(p);
					if (dl != null) {
						return "" + dl.getFullDistance();
					} else {
						return "";
					}
				}
			});

			toColumn.setEditingSupport(new EditingSupport(this) {
				@Override
				protected void setValue(final Object element, final Object value) {
					final Pair<Port, Map<Port, RouteLine>> e = (Pair<Port, Map<Port, RouteLine>>) element;
					final String stringValue = value.toString();
					final Command command;
					RouteLine dl = e.getSecond().get(p);

					if (stringValue.isEmpty()) {
						if (dl == null) {
							return;
						}
						command = RemoveCommand.create(editingDomain, dl);
						e.getSecond().remove(p);
					} else {
						if (dl == null) {
							dl = PortFactory.eINSTANCE.createRouteLine();
							dl.setFrom(e.getFirst());
							dl.setTo(p);
							dl.setDistance(Integer.parseInt(stringValue));
							e.getSecond().put(p, dl);
							command = AddCommand.create(editingDomain, getInput(), PortPackage.eINSTANCE.getRoute_Lines(), dl);
						} else {
							command = SetCommand.create(editingDomain, dl, PortPackage.eINSTANCE.getRouteLine_Distance(), Integer.parseInt(stringValue));
						}
					}
					command.execute();
					refresh(element);
				}

				@Override
				protected Object getValue(final Object element) {
					final Pair<Port, Map<Port, RouteLine>> e = (Pair<Port, Map<Port, RouteLine>>) element;
					final RouteLine dl = e.getSecond().get(p);
					if (dl == null) {
						return "";
					}
					return Integer.toString(dl.getDistance());
				}

				@Override
				protected CellEditor getCellEditor(final Object element) {
					final TextCellEditor tce = new TextCellEditor(getGrid());

					tce.setValidator(new ICellEditorValidator() {
						@Override
						public String isValid(final Object value) {
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
						}
					});

					return tce;
				}

				@Override
				protected boolean canEdit(final Object element) {

					final Pair<Port, Map<Port, RouteLine>> e = (Pair<Port, Map<Port, RouteLine>>) element;
					final RouteLine dl = e.getSecond().get(p);
					return dl == null || dl.getVias().isEmpty();
				}
			});
		}

		setRowHeaderLabelProvider(new CellLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();

				final Pair<Port, Map<Port, RouteLine>> e = (Pair<Port, Map<Port, RouteLine>>) element;
				cell.setText(e.getFirst().getName());

				if (element instanceof RouteLine) {
					final RouteLine routeLine = (RouteLine) element;
					if (routeLine.getVias().isEmpty()) {
						cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
					} else {
						cell.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
					}
				}
			}
		});
	}
}
