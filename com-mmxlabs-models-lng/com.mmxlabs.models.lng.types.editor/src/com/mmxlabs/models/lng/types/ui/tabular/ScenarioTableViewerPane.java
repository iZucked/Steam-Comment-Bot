package com.mmxlabs.models.lng.types.ui.tabular;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class ScenarioTableViewerPane extends ViewerPane {
	private ScenarioTableViewer scenarioViewer;

	public ScenarioTableViewerPane(IWorkbenchPage page, IWorkbenchPart part) {
		super(page, part);
	}

	@Override
	public ScenarioTableViewer createViewer(Composite parent) {
		return (scenarioViewer = new ScenarioTableViewer(parent, SWT.BORDER));
	}

	public <T extends ICellManipulator & ICellRenderer> void addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final Object... path) {
		this.addColumn(columnName, manipulatorAndRenderer, manipulatorAndRenderer, path);
	}
	
	public void addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final Object... pathObjects) {
		scenarioViewer.addColumn(columnName, renderer, manipulator, pathObjects);
	}

	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		scenarioViewer.init(adapterFactory, path.toArray(new EReference[path.size()]));
		final Grid table = scenarioViewer.getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

	/*	{
			final ToolBarManager x = getToolBarManager();
			final EMFPath ePath = new EMFPath(true, path);
			{
				final Action addAction = createAddAction(eObjectTableViewer, part.getEditingDomain(), ePath);
				if (addAction != null) {
					x.appendToGroup("edit", LockableAction.wrap(addAction));
				}
			}
			{
				final Action deleteAction = createDeleteAction(eObjectTableViewer, part.getEditingDomain());
				if (deleteAction != null) {
					x.appendToGroup("edit", LockableAction.wrap(deleteAction));

				}
			}
			{
				final Action importAction = createImportAction(eObjectTableViewer, part.getEditingDomain(), ePath);
				if (importAction != null) {
					x.appendToGroup("importers", LockableAction.wrap(importAction));
				}
			}
			{
				final Action a = createExportAction(eObjectTableViewer, ePath);
				if (a != null) {
					x.appendToGroup("exporters", a);
				}
			}

			x.update(true);
		} */
	}
}
