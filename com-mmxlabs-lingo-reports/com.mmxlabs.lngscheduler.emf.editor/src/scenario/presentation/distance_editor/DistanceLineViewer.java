/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.distance_editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.common.Pair;

/**
 * Set the input to a distance model, and this will let you edit it
 * 
 * @author Tom Hinton
 * 
 */
public class DistanceLineViewer extends TableViewer {
	/**
	 * @param parent
	 */
	public DistanceLineViewer(Composite parent) {
		super(parent);
	}

	/**
	 * @param table
	 */
	public DistanceLineViewer(Table table) {
		super(table);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public DistanceLineViewer(Composite parent, int style) {
		super(parent, style);
	}

	public void init(final ScenarioEditor part) {
		setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof DistanceModel) {
					final DistanceModel dm = (DistanceModel) inputElement;
					final Map<Port, Map<Port, DistanceLine>> values = new HashMap<Port, Map<Port, DistanceLine>>();
					for (final Port p : part.getScenario().getPortModel()
							.getPorts()) {
						values.put(p, new HashMap<Port, DistanceLine>());
					}

					for (final DistanceLine dl : dm.getDistances()) {
						values.get(dl.getFromPort()).put(dl.getToPort(), dl);
					}

					final List<Pair<Port, Map<Port, DistanceLine>>> output = new ArrayList<Pair<Port, Map<Port, DistanceLine>>>();
					for (final Map.Entry<Port, Map<Port, DistanceLine>> entry : values.entrySet()) {
						output.add(new Pair<Port, Map<Port,DistanceLine>>(entry.getKey(), entry.getValue()));
					}
					
					Collections.sort(output, new Comparator<Pair<Port, Map<Port, DistanceLine>>>() {
						@Override
						public int compare(
								Pair<Port, Map<Port, DistanceLine>> o1,
								Pair<Port, Map<Port, DistanceLine>> o2) {
							return o1.getFirst().getName().compareTo(o2.getFirst().getName());
						}
					});
					
					return output.toArray();
				}
				return new Object[0];
			}
		});
		
		final TableViewerColumn fromColumn = new TableViewerColumn(this, SWT.NONE);
		fromColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair<Port, Map<Port, DistanceLine>>) element).getFirst().getName();
			}
		});
		
		final ArrayList<Port> ports = new ArrayList<Port>();
		ports.addAll(part.getScenario().getPortModel().getPorts());
		Collections.sort(ports, new Comparator<Port>() {
			@Override
			public int compare(Port o1, Port o2) {
				return o1.getName().compareTo(o2.getName());
			}});
		
		for (final Port p : ports) {
			final TableViewerColumn toColumn = new TableViewerColumn(this, SWT.NONE);
			toColumn.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final Pair<Port, Map<Port, DistanceLine>> e = (Pair<Port, Map<Port, DistanceLine>>) element;
					final DistanceLine dl = e.getSecond().get(p);
					if (dl != null) {
						return ""+dl.getDistance();
					} else {
						return "";
					}
				}});
			
			// TODO add editing support here
		}
	}
}
