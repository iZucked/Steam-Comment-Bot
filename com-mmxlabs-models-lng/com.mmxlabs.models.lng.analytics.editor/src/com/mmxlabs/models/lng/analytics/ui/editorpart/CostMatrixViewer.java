package com.mmxlabs.models.lng.analytics.ui.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;

public class CostMatrixViewer extends ViewerPane implements ISelectionChangedListener {
	private UnitCostMatrixViewerPane selectionProvider;
	private GridTableViewer grid;
	private UnitCostMatrix currentInput;
	private JointModelEditorPart jointModelEditorPart;

	public CostMatrixViewer(final IWorkbenchPage page, final JointModelEditorPart part, final UnitCostMatrixViewerPane selectionProvider) {
		super(page, part);
		this.jointModelEditorPart = part;
		this.selectionProvider = selectionProvider;
	}

	private void makeViewer(final Composite composite) {
		this.grid = new GridTableViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		// setLayout(new FillLayout());
		// Set up grid.

		final Grid table = grid.getGrid();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setRowHeaderVisible(true);
		table.setCellSelectionEnabled(true);

		grid.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof UnitCostMatrix) {
					final UnitCostMatrix settings = (UnitCostMatrix) inputElement;
					final ArrayList<Pair<Port, Map<Port, UnitCostLine>>> result = new ArrayList<Pair<Port, Map<Port, UnitCostLine>>>();
					final Map<Port, Map<Port, UnitCostLine>> temp = new HashMap<Port, Map<Port, UnitCostLine>>();
					for (final UnitCostLine line : settings.getCostLines()) {
						final Port from = line.getFrom();
						final Port to = line.getTo();
						Map<Port, UnitCostLine> sub = temp.get(from);
						if (sub == null) {
							sub = new HashMap<Port, UnitCostLine>();
							temp.put(from, sub);
						}
						sub.put(to, line);
					}
					for (final Map.Entry<Port, Map<Port, UnitCostLine>> e : temp.entrySet()) {
						result.add(new Pair<Port, Map<Port, UnitCostLine>>(e.getKey(), e.getValue()));
					}
					Collections.sort(result, new Comparator<Pair<Port, Map<Port, UnitCostLine>>>() {

						@Override
						public int compare(Pair<Port, Map<Port, UnitCostLine>> o1, Pair<Port, Map<Port, UnitCostLine>> o2) {
							return o1.getFirst().getName().compareTo(o2.getFirst().getName());
						}
					});

					return result.toArray();
				}
				return new Object[0];
			}
		});

		grid.setRowHeaderLabelProvider(new CellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Pair<Port, ?>) (cell.getElement())).getFirst().getName());
			}
		});

		grid.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(final DoubleClickEvent event) {
				Point[] y = grid.getGrid().getCellSelection();

				if (y.length == 1) {
					final Pair<Port, Map<Port, UnitCostLine>> element = (Pair<Port, Map<Port, UnitCostLine>>) grid.getElementAt(y[0].y);
					final UnitCostLine line = element.getSecond().get(columnPorts.get(y[0].x));
					// display detail view for line
				}
			}
		});

		selectionProvider.getViewer().addSelectionChangedListener(this);
	}

	@Override
	public void selectionChanged(final SelectionChangedEvent event) {
		final ISelection selection = event.getSelection();
		if (selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			clearInput();
		} else {
			for (final Object o : ((IStructuredSelection) selection).toArray()) {
				if (o instanceof UnitCostMatrix) {
					setInput((UnitCostMatrix) o);
					return;
				}
			}
			clearInput();
		}
	}

	private List<Port> columnPorts;
	private MMXContentAdapter adapter = new MMXContentAdapter() {
		boolean waitingForRefresh = false;

		@Override
		public void reallyNotifyChanged(final Notification notification) {
			missedNotification();
		}

		@Override
		protected void missedNotification() {
			if (!waitingForRefresh) {
				waitingForRefresh = true;
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						setInput(currentInput);
						waitingForRefresh = false;
					}
				});
			}
		}
	};

	private void setInput(UnitCostMatrix o) {
		if (currentInput != null) {
			currentInput.eAdapters().remove(adapter);
		}
		currentInput = o;
		currentInput.eAdapters().add(adapter);
		// create appropriate columns
		final HashSet<Port> toPorts = new HashSet<Port>();
		for (final UnitCostLine costLine : o.getCostLines()) {
			toPorts.add(costLine.getTo());
		}
		final ArrayList<Port> toPorts_ = new ArrayList<Port>(toPorts);
		Collections.sort(toPorts_, new Comparator<Port>() {
			@Override
			public int compare(Port o1, Port o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (GridColumn column : grid.getGrid().getColumns()) {
			column.dispose();
		}
		columnPorts = toPorts_;
		for (final Port p : toPorts_) {
			final GridViewerColumn column = new GridViewerColumn(grid, SWT.NONE);
			column.getColumn().setText(p.getName());

			column.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					final Pair<Port, Map<Port, UnitCostLine>> el = (Pair<Port, Map<Port, UnitCostLine>>) element;
					if (el == null)
						return "";
					final UnitCostLine costLine = el.getSecond().get(p);
					if (costLine == null)
						return "";
					return String.format("$%,.2f", costLine.getUnitCost());
				}
			});
			column.getColumn().pack();
		}
		setTitle(o.getName(), part.getTitleImage());
		grid.setInput(currentInput);
	}

	private void clearInput() {
		currentInput = null;
		grid.setInput(null);
	}

	private class ViewerWrapper extends Viewer implements ISelectionChangedListener {

		private GridTableViewer delegate;

		public ViewerWrapper(final GridTableViewer v) {
			this.delegate = v;
			v.addSelectionChangedListener(this);
		}

		@Override
		public void selectionChanged(SelectionChangedEvent arg0) {
			final SelectionChangedEvent ee2 = new SelectionChangedEvent(this, getSelection());
			fireSelectionChanged(ee2);
		}

		private ISelection getCellSelection() {
			Point[] y = delegate.getGrid().getCellSelection();
			if (y.length == 1) {
				final Pair<Port, Map<Port, UnitCostLine>> element = (Pair<Port, Map<Port, UnitCostLine>>) grid.getElementAt(y[0].y);
				final UnitCostLine line = element.getSecond().get(columnPorts.get(y[0].x));
				if (line == null) return StructuredSelection.EMPTY;
				return new StructuredSelection(line);
			} else {
				return StructuredSelection.EMPTY;
			}
		}

		@Override
		public Control getControl() {
			return delegate.getControl();
		}

		@Override
		public Object getInput() {
			return delegate.getInput();
		}

		@Override
		public ISelection getSelection() {
			return getCellSelection();
		}

		@Override
		public void refresh() {
			delegate.refresh();
		}

		@Override
		public void setInput(Object input) {
			delegate.setInput(input);
		}

		@Override
		public void setSelection(ISelection selection, boolean reveal) {
			delegate.setSelection(selection, reveal);
		}
	}

	@Override
	public Viewer createViewer(Composite arg0) {
		makeViewer(arg0);
		CopyGridToClipboardAction copy = new CopyGridToClipboardAction(grid.getGrid());
		copy.setRowHeadersIncluded(true);
		getToolBarManager().add(copy);
		setTitle("Selected Cost Matrix", part.getTitleImage());
		getToolBarManager().update(true);

		return new ViewerWrapper(grid);
	}

	@Override
	protected void requestActivation() {
		super.requestActivation();

		jointModelEditorPart.setCurrentViewer(viewer);
	}
}