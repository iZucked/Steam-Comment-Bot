/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.ui.views;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
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
	private Composite top;
	private Composite inner;
	private ShippingCostPlanViewerPane plans;
	private ShippingCostRowViewerPane rows;
	// private ShippingCostPlan shippingCostPlan;
	// private IDisplayComposite journeyComposite;
	private PropertySheetPage propertySheetPage;

	// private AdapterImpl journeyChangedAdapter;

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
			Control control = plans.getControl();
			if (control != null) {
				control.dispose();
			}
			plans.dispose();
			plans = null;
		}

		if (inner != null) {
			inner.dispose();
			inner = null;
		}

		// if (shippingCostPlan != null) {
		// shippingCostPlan.eAdapters().remove(this.journeyChangedAdapter);
		// shippingCostPlan = null;
		// }
	}

	private void createContents() {
		final Composite inner = new Composite(top, SWT.NONE);
		inner.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		inner.setLayout(gridLayout);
		final Composite upper = new Composite(inner, SWT.NONE);
		upper.setLayout(new GridLayout());
		upper.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// journey = AnalyticsFactory.eINSTANCE.createJourney();
		//
		// journeyChangedAdapter = new AdapterImpl() {
		// @Override
		// public void notifyChanged(final Notification msg) {
		// if (!msg.isTouch()) {
		// if (pane != null && pane.getControl() != null && !pane.getControl().isDisposed()) {
		// pane.getViewer().refresh();
		// if (propertySheetPage != null) {
		// propertySheetPage.refresh();
		// }
		// }
		// }
		// }
		// };
		// journey.eAdapters().add(journeyChangedAdapter);

		// final IDisplayCompositeFactory factory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(journey.eClass());
		// journeyComposite = factory.createToplevelComposite(upper, journey.eClass(), this);
		// journeyComposite.setCommandHandler(getDefaultCommandHandler());
		// journeyComposite.display(this, getRootObject(), journey, (Collection) Collections.emptyList());
		//
		plans = new ShippingCostPlanViewerPane(getSite().getPage(), this, this, this.getViewSite().getActionBars());// {
		// @Override
		// protected boolean showEvaluateAction() {
		// return false;
		// }
		//
		// @Override
		// public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		// super.init(path, adapterFactory);
		// addTypicalColumn("Unit Cost", new UnitCostManipulator());
		// // new NonEditableColumn() {
		// // @Override
		// // public String render(final Object object) {
		// // if (propertySheetPage != null) propertySheetPage.refresh();
		// // if (journey.getFrom() == null || journey.getTo() == null) return "";
		// // try {
		// // final UnitCostLine line = transformer.createCostLine(getRootObject(), (UnitCostMatrix) object, journey.getFrom(), journey.getTo());
		// // if (line != null) {
		// // return String.format("$%.2f", line.getUnitCost());
		// // } else {
		// // return "";
		// // }
		// // } catch (Throwable th) {
		// // return "";
		// // }
		// // }
		// // });
		// }
		// };

		plans.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
		plans.createControl(top);
		plans.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		plans.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getAnalyticsModel_ShippingCostPlans()), getAdapterFactory());
		plans.getViewer().setInput(getRootObject().getSubModel(AnalyticsModel.class));
		rows = new ShippingCostRowViewerPane(getSite().getPage(), this, this, this.getViewSite().getActionBars());// {
		rows.setExternalToolBarManager((ToolBarManager) getViewSite().getActionBars().getToolBarManager());
		rows.createControl(inner);
		rows.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		rows.init(Collections.singletonList(AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows()), getAdapterFactory());
		// rows.getViewer().setInput(getRootObject().getSubModel(AnalyticsModel.class));
		rows.getScenarioViewer().getGrid().addMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(MenuDetectEvent e) {
				// TODO Auto-generated method stub
				ISelection selection = rows.getScenarioViewer().getSelection();

				Object input = rows.getScenarioViewer().getInput();
				if (input instanceof ShippingCostPlan) {

					ShippingCostRow row = AnalyticsFactory.eINSTANCE.createShippingCostRow();

					ShippingCostPlan plan = (ShippingCostPlan) input;
					Command addCmd = AddCommand.create(getEditingDomain(), plan, AnalyticsPackage.eINSTANCE.getShippingCostPlan_Rows(), row);
					getEditingDomain().getCommandStack().execute(addCmd);
				}
			}
		});
		getViewSite().setSelectionProvider(rows.getViewer());

		plans.getScenarioViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					Object element = iStructuredSelection.getFirstElement();
					if (element instanceof ShippingCostPlan) {

						rows.getViewer().setInput(element);
					}
				}
			}
		});

		top.layout(true);
		top.pack(true);
	}

	@Override
	public void createPartControl(final Composite parent) {
		top = parent;
		final GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = gridLayout.marginHeight = 0;
		top.setLayout(gridLayout);
		listenToScenarioSelection();
	}

	@Override
	public void setFocus() {
		if (rows != null && rows.getControl() != null && !rows.getControl().isDisposed()) {
			rows.getControl().setFocus();
		} else if (plans != null && plans.getControl() != null && !plans.getControl().isDisposed()) {
			plans.getControl().setFocus();
		} else if (top != null && !top.isDisposed()) {
			top.setFocus();
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

	//
	// private class UnitCostManipulator extends NonEditableColumn {
	// @Override
	// public String render(final Object object) {
	// if (propertySheetPage != null) {
	// propertySheetPage.refresh();
	// }
	// if (journey == null) {
	// return "";
	// }
	// if (journey.getFrom() == null || journey.getTo() == null) {
	// return "";
	// }
	// final Double d = evaluate(object);
	// if (d == null) {
	// return "";
	// }
	// return String.format("$%.2f", d);
	// }
	//
	// @Override
	// public CellEditor getCellEditor(final Composite parent, final Object object) {
	// final FormattedTextCellEditor result = new FormattedTextCellEditor(parent);
	//
	// result.setFormatter(new DoubleFormatter());
	//
	// return result;
	// }
	//
	// @Override
	// public boolean canEdit(final Object object) {
	// if (object instanceof UnitCostMatrix) {
	// if (((UnitCostMatrix) object).isSetSpeed()) {
	// return evaluate(object) != null;
	// }
	// }
	// return false;
	// }
	//
	// @Override
	// public void setValue(final Object o, final Object value) {
	// // reverse calculate
	//
	// if (o instanceof UnitCostMatrix) {
	// if (journey.getFrom() == null || journey.getTo() == null)
	// return;
	// try {
	// final UnitCostMatrix unitCostMatrix = (UnitCostMatrix) o;
	// final UnitCostLine line = transformer.createCostLine(getRootObject(), unitCostMatrix, journey.getFrom(), journey.getTo());
	// if (line != null) {
	// if (value instanceof Double) {
	// final Double d = (Double) value;
	// // now calc backwards.
	// // unit cost = ((ship cost) + (other cost)) / (mmbtu)
	// // mmbtu * unit cost = ship + other
	// // ship = mmbtu * unit - other
	// // ship day rate = mmbtu * unit - other / days
	// final double requiredShipCost = line.getMmbtuDelivered() * d - (line.getTotalCost() - line.getHireCost());
	// final int dayRate = (int) (24 * requiredShipCost / line.getDuration());
	// final EAttribute feature = AnalyticsPackage.eINSTANCE.getUnitCostMatrix_NotionalDayRate();
	// getDefaultCommandHandler().handleCommand(SetCommand.create(getEditingDomain(), unitCostMatrix, feature, dayRate), unitCostMatrix, feature);
	// }
	// }
	// } catch (final Throwable th) {
	// }
	// }
	// }
	//
	// @Override
	// public Object getValue(final Object object) {
	// return evaluate(object);
	// }
	//
	// private Double evaluate(final Object o) {
	// if (o instanceof ShippingCostRow) {
	//
	// if (journey == null) {
	// return null;
	// }
	//
	// if (journey.getFrom() == null || journey.getTo() == null)
	// return null;
	// try {
	// final UnitCostLine line = transformer.createCostLine(getRootObject(), (UnitCostMatrix) o, journey.getFrom(), journey.getTo());
	// if (line != null) {
	// return line.getUnitCost();
	// }
	// } catch (final Throwable th) {
	// }
	// }
	// return null;
	// }
	// }

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

}
