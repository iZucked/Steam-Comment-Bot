package com.mmxlabs.models.lng.ui.tabular;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.ui.actions.AddModelAction;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.lng.ui.actions.AddModelAction.IAddContext;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.MultiDetailDialog;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;

public class ScenarioTableViewerPane extends ViewerPane {
	/**
	 * 
	 */
	protected static final String VIEW_GROUP = "view";
	/**
	 * 
	 */
	protected static final String ADD_REMOVE_GROUP = "addremove";
	/**
	 * 
	 */
	protected static final String EDIT_GROUP = "edit";
	private ScenarioTableViewer scenarioViewer;
	private JointModelEditorPart jointModelEditorPart;

	public ScenarioTableViewerPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
		this.jointModelEditorPart = part;
	}

	@Override
	public ScenarioTableViewer createViewer(Composite parent) {
		if (scenarioViewer == null) {
			scenarioViewer = new ScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, jointModelEditorPart);
			scenarioViewer.addDoubleClickListener(new IDoubleClickListener() {
				
				@Override
				public void doubleClick(DoubleClickEvent event) {

					if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
						final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer
								.getSelection();
						if (structuredSelection.isEmpty() == false) {
							if (structuredSelection.size() == 1) {
							final DetailCompositeDialog dcd = new DetailCompositeDialog(
									event.getViewer().getControl().getShell(),
									jointModelEditorPart
											.getDefaultCommandHandler());
							dcd.open(jointModelEditorPart.getRootObject(), structuredSelection.toList());
							} else {
								final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(),
										jointModelEditorPart.getRootObject(), jointModelEditorPart.getDefaultCommandHandler()
										); 
								mdd.open(structuredSelection.toList());
							}
						}
					}

				}
			});
			scenarioViewer.getGrid().setCellSelectionEnabled(true);
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

		// set up toolbars
		final ToolBarManager toolbar = getToolBarManager();
		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTableColumnsAction(scenarioViewer));
		
		final EReference containment = path.get(path.size()-1);
		final Action addAction = AddModelAction.create(containment.getEReferenceType(), 
				new IAddContext() {
					@Override
					public MMXRootObject getRootObject() {
						return jointModelEditorPart.getRootObject();
					}
					
					@Override
					public EReference getContainment() {
						return containment;
					}
					
					@Override
					public EObject getContainer() {
						return scenarioViewer.getCurrentContainer();
					}
					
					@Override
					public ICommandHandler getCommandHandler() {
						return jointModelEditorPart.getDefaultCommandHandler();
					}
				});
		if (addAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);
		}
		final Action deleteAction = createDeleteAction();
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		
		toolbar.update(true);
	}
	
	protected Action createDeleteAction() {
		return new ScenarioModifyingAction("Delete") {
			{
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				jointModelEditorPart.addSelectionChangedListener(this);
			}
			@Override
			public void run() {
				final ISelection sel = getLastSelection();
				if (sel instanceof IStructuredSelection) {
					final EditingDomain ed = jointModelEditorPart.getEditingDomain();
					final List objects = ((IStructuredSelection) sel).toList();
					ed.getCommandStack().execute(DeleteCommand.create(ed, objects));
				}
			}

			@Override
			protected boolean isApplicableToSelection(ISelection selection) {
				return selection.isEmpty() == false && selection instanceof IStructuredSelection;
			}
		};
	}

	@Override
	protected void requestActivation() {
		super.requestActivation();
		jointModelEditorPart.setCurrentViewer(scenarioViewer);
	}
}
