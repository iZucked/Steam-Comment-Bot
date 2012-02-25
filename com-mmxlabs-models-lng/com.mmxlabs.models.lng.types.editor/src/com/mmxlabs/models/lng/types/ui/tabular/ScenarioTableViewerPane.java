package com.mmxlabs.models.lng.types.ui.tabular;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;

public class ScenarioTableViewerPane extends ViewerPane {
	private ScenarioTableViewer scenarioViewer;
	private JointModelEditorPart jointModelEditorPart;

	public ScenarioTableViewerPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
		this.jointModelEditorPart = part;
	}

	@Override
	public ScenarioTableViewer createViewer(Composite parent) {
		if (scenarioViewer == null) {
			scenarioViewer = new ScenarioTableViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, jointModelEditorPart);
			scenarioViewer.addDoubleClickListener(new IDoubleClickListener() {
				
				@Override
				public void doubleClick(DoubleClickEvent event) {

					if (event.getSelection() instanceof IStructuredSelection) {
						final IStructuredSelection structuredSelection = (IStructuredSelection) event
								.getSelection();
						if (structuredSelection.isEmpty() == false) {
							final DetailCompositeDialog dcd = new DetailCompositeDialog(
									event.getViewer().getControl().getShell(),
									jointModelEditorPart
											.getDefaultCommandHandler());
							dcd.open(jointModelEditorPart.getRootObject(), structuredSelection.toList());
						}
					}

				}
			});
			return scenarioViewer;
		} else {
			throw new RuntimeException("Did not expect two calls to createViewer()");
		}

	}

	public <T extends ICellManipulator & ICellRenderer> void addTypicalColumn(final String columnName, final T manipulatorAndRenderer, final Object... path) {
		this.addColumn(columnName, manipulatorAndRenderer, manipulatorAndRenderer, path);
	}
	
	public void addColumn(final String columnName, final ICellRenderer renderer, final ICellManipulator manipulator, final Object... pathObjects) {
		scenarioViewer.addColumn(columnName, renderer, manipulator, pathObjects);
	}

	public EditingDomain getEditingDomain() {
		return jointModelEditorPart.getEditingDomain();
	}

	public AdapterFactory getAdapterFactory() {
		return jointModelEditorPart.getAdapterFactory();
	}

	public IReferenceValueProviderProvider getReferenceValueProviderCache() {
		return jointModelEditorPart.getReferenceValueProviderCache();
	}
	
	public JointModelEditorPart getJointModelEditorPart() {
		return jointModelEditorPart;
	}
	
	protected void addNameManipulator(final String nameName) {
		addTypicalColumn(nameName, new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()));
	}
	
	protected void defaultSetTitle(final String string) {
		setTitle(string, PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
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
