/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.editor.views;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.PortGroupEditorPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class PortGroupView extends ScenarioInstanceView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.models.lng.port.editor.views.PortGroupView";

	private SashForm sash;
	private PortGroupEditorPane viewerPane;

	private CheckboxTableViewer contentViewer;

	@Override
	public void createPartControl(final Composite parent) {
		sash = new SashForm(parent, SWT.HORIZONTAL);
		listenToScenarioSelection();
	}

	@Override
	public void setFocus() {
		sash.setFocus();
	}

	final ILabelProvider nameLabelProvider = new LabelProvider() {
		@Override
		public String getText(Object element) {
			if (element instanceof NamedObject) {
				return ((NamedObject) element).getName();
			} else {
				return "";
			}
		}
	};

	private IContentProvider portProvider = new IStructuredContentProvider() {
		// Viewer viewer;
		// private MMXContentAdapter contentAdapter = new MMXContentAdapter() {
		// @Override
		// public void reallyNotifyChanged(Notification notification) {
		// if (notification.isTouch() == false)
		// refresh();
		// }
		//
		// private void refresh() {
		// if (viewer != null && viewer.getControl().isDisposed()) {
		// // remove myself
		// if (lastInput != null)
		// lastInput.eAdapters().remove(this);
		// } else {
		// Display.getDefault().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		// if (viewer != null && !viewer.getControl().isDisposed())
		// viewer.refresh();
		// }
		// });
		// }
		// }
		// };

		// private EObject lastInput;

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// if (oldInput instanceof EObject) {
			// ((EObject) oldInput).eAdapters().remove(contentAdapter);
			// }
			// if (newInput instanceof EObject) {
			// // ((EObject) newInput).eAdapters().add(contentAdapter);
			// lastInput = (EObject) newInput;
			// } else {
			// lastInput = null;
			// }

			if (newInput != oldInput)
				viewer.refresh();
		}

		@Override
		public void dispose() {
			// if (lastInput != null) {
			// lastInput.eAdapters().remove(contentAdapter);
			// }
			// lastInput = null;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof PortModel) {
				NamedObject[] array = ((PortModel) inputElement).getPorts().toArray(new NamedObject[0]);
				Arrays.sort(array, new Comparator<NamedObject>() {
					@Override
					public int compare(NamedObject o1, NamedObject o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				return array;
			}
			return new Object[0];
		}
	};

	@Override
	protected void displayScenarioInstance(ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			if (viewerPane != null) {
				viewerPane.dispose();
				viewerPane = null;
				contentViewer = null;
			}

			final Composite parent = sash.getParent();
			sash.dispose();
			sash = new SashForm(parent, SWT.HORIZONTAL);

			super.displayScenarioInstance(instance);
			if (instance != null) {
				viewerPane = new PortGroupEditorPane(getSite().getPage(), this, this, getViewSite().getActionBars());
				viewerPane.setExternalMenuManager((MenuManager) getViewSite().getActionBars().getMenuManager());
				viewerPane.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
				viewerPane.createControl(sash);
				viewerPane.init(Lists.newArrayList(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ReferenceModel(), LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_PortModel(), PortPackage.eINSTANCE.getPortModel_PortGroups()), getAdapterFactory(),
						getEditingDomain().getCommandStack());
				viewerPane.getViewer().setInput(getRootObject());

				contentViewer = CheckboxTableViewer.newCheckList(sash, SWT.SINGLE | SWT.V_SCROLL | SWT.BORDER);
				contentViewer.setLabelProvider(nameLabelProvider);
				contentViewer.setContentProvider(portProvider);

				viewerPane.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						final ISelection selection = event.getSelection();
						if (selection instanceof IStructuredSelection) {
							final Object element = ((IStructuredSelection) selection).getFirstElement();
							if (element instanceof PortGroup) {
								contentViewer.setInput(((PortGroup) element).eContainer());
								return;
							}
						}

						contentViewer.setInput(null);
					}
				});

				contentViewer.setCheckStateProvider(new ICheckStateProvider() {
					@Override
					public boolean isGrayed(Object element) {
						return false;
					}

					@Override
					public boolean isChecked(Object element) {
						if (viewerPane == null)
							return false;
						if (viewerPane.getViewer() == null)
							return false;
						if (viewerPane.getViewer().getControl() == null)
							return false;
						if (viewerPane.getViewer().getControl().isDisposed())
							return false;
						final ISelection selection = viewerPane.getViewer().getSelection();

						if (selection instanceof IStructuredSelection) {
							final Object e = ((IStructuredSelection) selection).getFirstElement();
							if (e instanceof PortGroup) {
								return ((PortGroup) e).getContents().contains(element);
							}
						}

						return false;
					}
				});

				contentViewer.addCheckStateListener(new ICheckStateListener() {
					@Override
					public void checkStateChanged(CheckStateChangedEvent event) {
						if (viewerPane.getViewer().getControl().isDisposed())
							return;
						final ISelection selection = viewerPane.getViewer().getSelection();

						if (selection instanceof IStructuredSelection) {
							final Object e = ((IStructuredSelection) selection).getFirstElement();
							if (e instanceof PortGroup) {
								final EditingDomain ed = getEditingDomain();
								if (event.getChecked())
									ed.getCommandStack().execute(AddCommand.create(ed, e, PortPackage.eINSTANCE.getPortGroup_Contents(), event.getElement()));
								else
									ed.getCommandStack().execute(RemoveCommand.create(ed, e, PortPackage.eINSTANCE.getPortGroup_Contents(), event.getElement()));
							}
						}
					}
				});

				sash.setWeights(new int[] { 2, 2 });
			}
			parent.layout(true);
		}
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			PortGroup portGroup = null;
			if (dcsd.getTarget() instanceof PortGroup) {
				portGroup = (PortGroup) dcsd.getTarget();
			}
			if (portGroup != null) {
				getSite().getPage().activate(this);
				viewerPane.getScenarioViewer().setSelection(new StructuredSelection(portGroup), true);
			}
		}
	}
}
