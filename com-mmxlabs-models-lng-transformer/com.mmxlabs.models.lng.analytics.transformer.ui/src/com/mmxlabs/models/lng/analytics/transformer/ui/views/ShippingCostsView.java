/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.ui.views;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostPlan;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.transformer.IShippingCostTransformer;
import com.mmxlabs.models.lng.analytics.transformer.impl.ShippingCostTransformer;
import com.mmxlabs.models.lng.analytics.ui.properties.UnitCostLinePropertySource;
import com.mmxlabs.models.ui.editorpart.ScenarioInstanceView;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ShippingCostsView extends ScenarioInstanceView {

	private final IShippingCostTransformer transformer = new ShippingCostTransformer();

	private ShippingCostPlanViewerPane plans;
	private ShippingCostRowViewerPane rows;

	private PropertySheetPage propertySheetPage;

	private Composite parent;

	@Override
	protected void displayScenarioInstance(final ScenarioInstance instance) {
		if (instance != getScenarioInstance()) {
			disposeContents();
			getSite().setSelectionProvider(null);
			super.displayScenarioInstance(instance);
			if (instance != null) {
				createContents();
			}
		}
	}

	private void disposeContents() {
		if (rows != null) {
			final Control control = rows.getControl();
			// This can be null if the pane has already been disposed.
			if (control != null) {
				control.dispose();
			}
			rows.dispose();
			rows = null;
		}
		if (plans != null) {
			final Control control = plans.getControl();
			if (control != null) {
				control.dispose();
			}
			plans.dispose();
			plans = null;
		}

	}

	private void createContents() {
		plans = new ShippingCostPlanViewerPane(getSite().getPage(), this, this, this.getViewSite().getActionBars());

		plans.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
		plans.createControl(parent);
		plans.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		plans.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getAnalyticsModel_ShippingCostPlans()), getAdapterFactory());
		plans.getViewer().setInput(getRootObject().getSubModel(AnalyticsModel.class));

		rows = new ShippingCostRowViewerPane(getSite().getPage(), this, this, null);
		// rows.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
		rows.createControl(parent);
		rows.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		rows.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows()), getAdapterFactory());

		createMenuManager(rows.getScenarioViewer().getGrid());
		getViewSite().setSelectionProvider(rows.getViewer());

		plans.getScenarioViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				final ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					final Object element = iStructuredSelection.getFirstElement();
					if (element instanceof ShippingCostPlan) {

						rows.getViewer().setInput(element);
					}
				}
			}
		});
		parent.layout(true);
		// parent.layout(true);
		// pack(true);
	}

	@Override
	public void createPartControl(final Composite parent) {
		this.parent = parent;
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		parent.setLayout(gridLayout);
		listenToScenarioSelection();
	}

	@Override
	public void setFocus() {
		if (rows != null && rows.getControl() != null && !rows.getControl().isDisposed()) {
			rows.getControl().setFocus();
		} else if (plans != null && plans.getControl() != null && !plans.getControl().isDisposed()) {
			plans.getControl().setFocus();
			// } else if (top != null && !top.isDisposed()) {
			// top.setFocus();
			// }
		}
	}

	@Override
	public Object getAdapter(final Class adapter) {
		if (adapter.isAssignableFrom(IPropertySheetPage.class)) {
			if (propertySheetPage == null && plans != null) {
				propertySheetPage = new PropertySheetPage();
				propertySheetPage.setPropertySourceProvider(new IPropertySourceProvider() {
					@Override
					public IPropertySource getPropertySource(final Object object) {
						if (object instanceof ShippingCostPlan) {
							final List<UnitCostLine> lines = transformer.evaulateShippingPlan(getRootObject(), (ShippingCostPlan) object, new NullProgressMonitor());
							try {
								if (lines != null && !lines.isEmpty()) {
									// TODO: Handle multiple cases
									final UnitCostLine line = (UnitCostLine) lines.get(0);
									return new UnitCostLinePropertySource(line);
								}
							} catch (final Throwable th) {
								return null;
							}
						} else if (object instanceof IPropertySource) {
							return (IPropertySource) object;
						}
						return null;
					}
				});
			}
			return propertySheetPage;
		}
		return null;
	}

	public void dispose() {
		disposeContents();
		getSite().setSelectionProvider(null);

		super.dispose();
	}

	@Override
	public void setLocked(final boolean locked) {
		if (plans != null) {
			plans.setLocked(locked);
		}
		if (rows != null) {
			rows.setLocked(locked);
		}
		super.setLocked(locked);
	}

	IMenuManager createMenuManager(final Control parent) {

		final MenuManager mgr = new MenuManager();
		final Menu createContextMenu = mgr.createContextMenu(parent);
		parent.setMenu(createContextMenu);
		getSite().registerContextMenu(mgr, rows.getScenarioViewer());

		mgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				manager.removeAll();
				final Object input = rows.getScenarioViewer().getInput();
				final ShippingCostPlan plan = (ShippingCostPlan) input;
				if (plan == null) {
					return;
				}

				final EList<ShippingCostRow> planRows = plan.getRows();

				final int selectionIndex = rows.getScenarioViewer().getGrid().getSelectionIndex();

				manager.add(new Action("Insert new row") {
					@Override
					public void run() {

						if (input instanceof ShippingCostPlan) {

							final ShippingCostRow row = AnalyticsFactory.eINSTANCE.createShippingCostRow();
							row.setDestinationType(DestinationType.OTHER);
							// No insertion before first element!
							final int insertionIndex = Math.max(planRows.size() == 0 ? 0 : 1, selectionIndex);

							final Command addCmd = AddCommand.create(getEditingDomain(), plan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), row, insertionIndex);
							getEditingDomain().getCommandStack().execute(addCmd);
						}

						super.run();
					}
				});

				if (selectionIndex >= 0 && selectionIndex < plan.getRows().size() - 1) {
					manager.add(new Action("Move Down") {
						@Override
						public void run() {

							final Object input = rows.getScenarioViewer().getInput();
							if (input instanceof ShippingCostPlan) {

								final ShippingCostRow rowA = planRows.remove(selectionIndex);
								planRows.add(selectionIndex + 1, rowA);
								final CompoundCommand cmd = new CompoundCommand("Move row Down");
								cmd.append(SetCommand.create(getEditingDomain(), plan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), planRows));
								getEditingDomain().getCommandStack().execute(cmd);
							}

							super.run();
						}
					});
				}

				if (selectionIndex > 0) {
					manager.add(new Action("Move Up") {
						@Override
						public void run() {

							final Object input = rows.getScenarioViewer().getInput();
							if (input instanceof ShippingCostPlan) {

								final ShippingCostRow rowA = planRows.remove(selectionIndex);
								planRows.add(selectionIndex - 1, rowA);
								final CompoundCommand cmd = new CompoundCommand("Move row up");
								cmd.append(SetCommand.create(getEditingDomain(), plan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), planRows));
								getEditingDomain().getCommandStack().execute(cmd);
							}

							super.run();
						}
					});
				}
			}
		});

		return mgr;
	}
}
