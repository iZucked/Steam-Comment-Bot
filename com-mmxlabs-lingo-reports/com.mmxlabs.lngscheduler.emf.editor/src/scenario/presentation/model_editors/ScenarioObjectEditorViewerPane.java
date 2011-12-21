/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;

import scenario.ScenarioPackage;
import scenario.presentation.LngEditorPlugin;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.EObjectEditorViewerPane;
import scenario.presentation.cargoeditor.handlers.AddAction;

import com.mmxlabs.lngscheduler.emf.extras.EMFPath;
import com.mmxlabs.lngscheduler.emf.extras.validation.context.ValidationSupport;
import com.mmxlabs.shiplingo.ui.detailview.containers.DetailCompositeDialog;
import com.mmxlabs.shiplingo.ui.detailview.containers.MultiDetailDialog;
import com.mmxlabs.shiplingo.ui.tableview.EObjectTableViewer;
import com.mmxlabs.shiplingo.ui.tableview.ICellManipulator;
import com.mmxlabs.shiplingo.ui.tableview.ICellRenderer;
import com.mmxlabs.shiplingo.ui.tableview.filter.FilterField;
import com.mmxlabs.shiplingo.ui.tableview.filter.FilterProposalProvider;

/**
 * This extension of {@link EObjectEditorViewerPane} adds the following
 * <ol>
 * <li>A handler which pops up either an {@link EObjectDetailDialog} or an
 * {@link EObjectMultiDialog} when you hit return</li>
 * <li>
 * An add action which displays an {@link EObjectDetailDialog} when you add a
 * new element</li>
 * 
 * @author Tom Hinton
 * 
 */
public class ScenarioObjectEditorViewerPane extends EObjectEditorViewerPane {
	final static protected EAttribute namedObjectName = ScenarioPackage.eINSTANCE
			.getNamedObject_Name();

	/**
	 * @param page
	 * @param part
	 */
	public ScenarioObjectEditorViewerPane(final IWorkbenchPage page,
			final ScenarioEditor part) {
		super(page, part);
	}

	/**
	 * A convenience version of {@link #init(List, AdapterFactory)} which takes
	 * varargs.
	 * 
	 * @param adapterFactory
	 * @param path
	 */
	public void init(final AdapterFactory adapterFactory,
			final EReference... path) {
		final List<EReference> list = Arrays.asList(path);
		init(list, adapterFactory);
	}

	/**
	 * Create a custom add action, which delegates to the default behaviour to
	 * create the object, but adds in an editor dialog.
	 */
	@Override
	protected Action createAddAction(final GridTableViewer viewer,
			final EditingDomain editingDomain, final EMFPath contentPath) {
		final AddAction delegate = (AddAction) super.createAddAction(viewer,
				editingDomain, contentPath);

		final Action result = new AddAction(editingDomain, contentPath
				.getTargetType().getName()) {
			@Override
			public EObject createObject(final boolean usingSelection) {
				final EObject newObject = delegate.createObject(usingSelection);

				try {
					ValidationSupport.getInstance().setContainers(
							Collections.singletonList(newObject),
							(EObject) getOwner(), (EReference) getFeature());

					final DetailCompositeDialog dcd = new DetailCompositeDialog(
							viewer.getControl().getShell(), part,
							part.getEditingDomain());
					
					if (dcd.open(Collections.singletonList(newObject)) == Window.OK) {
						return newObject;
					} else {
						return null;
					}
					
				} finally {
					ValidationSupport.getInstance().clearContainers(
							Collections.singletonList(newObject));
				}
			}

			@Override
			public Object getOwner() {
				return delegate.getOwner();
			}

			@Override
			public Object getFeature() {
				return delegate.getFeature();
			}
		};
		return result;
	}
	
	private FilterField filterField;
	
	public void createControl(Composite parent) 
	  {
	    if (getControl() == null)
	    {
	      container = parent;

	      // Create view form.    
	      //control = new ViewForm(parent, getStyle());
	      control = new ViewForm(parent, SWT.NONE);
	      control.addDisposeListener
	        (new DisposeListener()
	         {
	           public void widgetDisposed(DisposeEvent event)
	           {
	             dispose();
	           }
	         });
	      
	      control.marginWidth = 0;
	      control.marginHeight = 0;

	      // Create a title bar.
	      createTitleBar();

	      final Composite inner = new Composite(control, SWT.NONE);
		  filterField = new FilterField(inner);
	    
	      final GridLayout layout = new GridLayout(1, false);
	      layout.marginHeight = 0;
	      layout.marginWidth = 0;
	      inner.setLayout(layout);
	      
	      viewer = createViewer(inner);
	      
	      viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
	      
	      control.setContent(inner);

	      control.setTabList(new Control [] { inner });
	      
	      // When the pane or any child gains focus, notify the workbench.
	      control.addListener(SWT.Activate, this);
	      hookFocus(control);
	      hookFocus(viewer.getControl());
	    }
	  }
	
	@Override
	public EObjectTableViewer createViewer(final Composite parent) {
		final EObjectTableViewer v = super.createViewer(parent);
		
		final ActionContributionItem filter = filterField.getContribution();
		
		getToolBarManager().appendToGroup("filter", filter);
		
		v.getGrid().setCellSelectionEnabled(true);
		v.getControl().addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(final org.eclipse.swt.events.KeyEvent e) {

			}

			@Override
			public void keyPressed(final org.eclipse.swt.events.KeyEvent e) {
				// TODO: Wrap up in a command with keybindings
				if (v.isCellEditorActive()) return;
				final ISelection selection = getViewer().getSelection();
				if (e.keyCode == '\r') {
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection ssel = (IStructuredSelection) selection;
						final List l = Arrays.asList(ssel.toArray());

						if (l.size() > 1 && (e.stateMask & SWT.CONTROL) == 0) {
							final MultiDetailDialog multiDialog = new MultiDetailDialog(
									v.getControl().getShell(),
									part,
									part.getEditingDomain());
														
							if (multiDialog.open(l) == Window.OK) {
								getViewer().refresh();
							}
						} else {
							if (l.size() == 0)
								return;

							final DetailCompositeDialog dcd = new DetailCompositeDialog(
									v.getControl().getShell(), part,
									part.getEditingDomain());

							if (dcd.open(l) == Window.OK) {
								getViewer().refresh();
							}
						}
					}
				}
			}
		});
		
		filterField.setViewer(v);
		
		return v;
	}	
}