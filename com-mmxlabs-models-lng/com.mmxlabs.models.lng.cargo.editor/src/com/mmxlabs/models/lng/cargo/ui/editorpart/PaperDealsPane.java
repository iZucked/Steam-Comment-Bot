/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CreatePaperStripDialog.StripType;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.ui.ImageConstants;
import com.mmxlabs.models.lng.ui.LngUIActivator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.impl.TextualReferenceInlineEditor;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ValueListAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.YearMonthAttributeManipulator;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class PaperDealsPane extends ScenarioTableViewerPane implements org.eclipse.e4.ui.workbench.modeling.ISelectionListener {

	public PaperDealsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addNameManipulator("Name");
		addColumn("Type", createPaperDealTypeFormatter(), null);
		//addTypicalColumn("Pricing start", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_StartDate(), getCommandHandler()));
		//addTypicalColumn("Pricing end", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_EndDate(), getCommandHandler()));
		addTypicalColumn("Month", new YearMonthAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_PricingMonth(), getCommandHandler())).setEditingSupport(null);;
		addTypicalColumn("Price", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Price(), getCommandHandler())).setEditingSupport(null);;
		addTypicalColumn("MtM curve", new StringAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Index(), getCommandHandler())).setEditingSupport(null);;
		addTypicalColumn("Quantity", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Quantity(), getCommandHandler())).setEditingSupport(null);;
		addTypicalColumn("Pricing", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_PricingType(), getCommandHandler()) {
			//@Override
			@Override
			public String render(final Object object) {
				if (object instanceof PaperDeal pd) {
					return switch(pd.getPricingType()) {
					case INSTRUMENT -> pd.getInstrument().getName();
					case CALENDAR -> "Calendar";
					case PERIOD_AVG -> "Period daily average";
					default -> throw new IllegalArgumentException("Unexpected value: " + pd.getPricingType());
					};
				}
				return null;
			}
		}).setEditingSupport(null);
		
		setTitle("Paper", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

		final CreatePaperStripMenuAction cpsma = new CreatePaperStripMenuAction("Bulk add");
		getToolBarManager().appendToGroup(VIEW_GROUP, cpsma);
		getToolBarManager().update(true);

		final ESelectionService service = part.getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(this);
	}

	@Override
	protected void addExtraAddActions(final List<Action> extraActions) {
		extraActions.add(new Action("Selected from generated", Action.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				if (gSelection instanceof IStructuredSelection ss) {
					final StringBuilder sb = new StringBuilder();
					processSelection(ss, sb);
					if (sb.length() != 0) {
						final MessageBox dialog = new MessageBox(scenarioEditingLocation.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Adding the generated paper deals into the current portfolio");
						dialog.setMessage(sb.toString());
						dialog.open();
					}
				}
			}
		});
	}

	private void processSelection(final IStructuredSelection ss, final StringBuilder sb) {
		final Iterator iter = ss.iterator();
		final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
		if (rootObject instanceof final LNGScenarioModel scenarioModel) {
			final Schedule schedule = scenarioModel.getScheduleModel().getSchedule();
			if (schedule != null) {
				final CompoundCommand cc = new CompoundCommand();
				while (iter.hasNext()) {
					final Object obj = iter.next();
					if (obj instanceof PaperDealAllocation) {
						processSelectedPaperDealAllocation(scenarioModel, schedule, cc, obj, sb);
					} else if (obj instanceof PaperDealAllocationEntry pdae) {
						processSelectedPaperDealAllocation(scenarioModel, schedule, cc, pdae.eContainer(), sb);
					}
				}
				if (!cc.isEmpty()) {
					getEditingDomain().getCommandStack().execute(cc);
				}
			}
		}
	}

	private void processSelectedPaperDealAllocation(final LNGScenarioModel scenarioModel, final Schedule schedule, final CompoundCommand cc, final Object obj, final StringBuilder sb) {
		if (obj instanceof final PaperDealAllocation pda) {
			final PaperDeal pd = pda.getPaperDeal();
			if (pd.eContainmentFeature() == SchedulePackage.eINSTANCE.getSchedule_GeneratedPaperDeals()) {
				if (pda.eContainer() != null && schedule == pda.eContainer()) {
					cc.append(RemoveCommand.create(getEditingDomain(), schedule, SchedulePackage.eINSTANCE.getSchedule_GeneratedPaperDeals(), pd));
					cc.append(AddCommand.create(getEditingDomain(), scenarioModel.getCargoModel(), CargoPackage.eINSTANCE.getCargoModel_PaperDeals(), pd));
					sb.append(String.format("%s is added into paper deals list. Please re-evaluate the scenario!%n", pd.getName()));
				} else {
					sb.append(String.format("%s is not added since it is not from the current scenario!%n", pd.getName()));
				}
			} else {
				sb.append(String.format("%s is not added since it is not a generated paper deal!%n", pd.getName()));
			}
		}
	}

	private class CreatePaperStripMenuAction extends DefaultMenuCreatorAction {

		public CreatePaperStripMenuAction(String label) {
			super(label);
			setImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE));
		}

		@Override
		protected void populate(Menu menu) {
			final CreateStripAction csabp = new CreateStripAction("Buy Paper", StripType.TYPE_PAPER_BUY);
			final CreateStripAction csasp = new CreateStripAction("Sell Paper", StripType.TYPE_PAPER_SELL);
			addActionToMenu(csabp, menu);
			addActionToMenu(csasp, menu);
		}
	}

	private class CreateStripAction extends LockableAction {

		private final StripType stripType;

		public CreateStripAction(final String text, final StripType stripType) {
			super(text);
			this.stripType = stripType;
		}

		@Override
		public void run() {

			final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
			try {
				editorLock.lock();
				try {
					scenarioEditingLocation.setDisableUpdates(true);

					final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {

						PaperDeal selectedDeal = null;
						final ISelection selection = PaperDealsPane.this.getScenarioViewer().getSelection();
						if (selection instanceof IStructuredSelection) {
							final IStructuredSelection ss = (IStructuredSelection) selection;

							final Iterator<?> itr = ss.iterator();
							while (itr.hasNext()) {
								Object o = itr.next();

								if (o instanceof PaperDeal) {
									selectedDeal = (PaperDeal) o;
								}
							}
						}

						final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
						final CreatePaperStripDialog d = new CreatePaperStripDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation, stripType, selectedDeal) {
							@Override
							protected void configureShell(final Shell newShell) {
								newShell.setMinimumSize(SWT.DEFAULT, 630);
								super.configureShell(newShell);
							}
						};
						if (Window.OK == d.open()) {
							final Command cmd = d.createStrip(scenarioModel.getCargoModel(), getEditingDomain());
							if (cmd.canExecute()) {
								getEditingDomain().getCommandStack().execute(cmd);
							}
						}

					} else {
						setEnabled(false);
					}

				} finally {
					scenarioEditingLocation.setDisableUpdates(false);
				}
			} finally {
				editorLock.unlock();
			}
		}
	}

	private ICellRenderer createPaperDealTypeFormatter() {
		return new ICellRenderer() {

			@Override
			public Comparable getComparable(Object object) {
				// TODO Auto-generated method stub
				return render(object);
			}

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof BuyPaperDeal) {
					return "Buy";
				} else if (object instanceof SellPaperDeal) {
					return "Sell";
				}
				// TODO Auto-generated method stub
				return "";
			}

			@Override
			public boolean isValueUnset(Object object) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public @Nullable Object getFilterValue(Object object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public void dispose() {
		final ESelectionService service = part.getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(this);

		super.dispose();
	}

	ISelection gSelection;

	@Override
	public void selectionChanged(MPart part, Object selection) {
		final IWorkbenchPart e3Part = SelectionHelper.getE3Part(part);
		if (e3Part != null) {
			if (e3Part == this) {
				return;
			}
			if (e3Part instanceof PropertySheet) {
				return;
			}
			if (e3Part instanceof JointModelEditorPart) {
				return;
			}
		}
		gSelection = SelectionHelper.adaptSelection(selection);
	}
	
	private class PaperPricingTypeEnumManipulator extends ValueListAttributeManipulator {

		public PaperPricingTypeEnumManipulator(EAttribute field, ICommandHandler commandHandler) {
			super(field, commandHandler, getValues((EEnum) field.getEAttributeType()));
		}
		
		private static List<Pair<String, Object>> getValues(final EEnum eenum) {
			final LinkedList<Pair<String, Object>> values = new LinkedList<Pair<String, Object>>();
			values.add(new Pair<String, Object>("Calendar", PaperPricingType.CALENDAR));
			values.add(new Pair<String, Object>("Instrument", PaperPricingType.INSTRUMENT));
			values.add(new Pair<String, Object>("Period daily average", PaperPricingType.PERIOD_AVG));
			return values;
		}
		
	}
}
