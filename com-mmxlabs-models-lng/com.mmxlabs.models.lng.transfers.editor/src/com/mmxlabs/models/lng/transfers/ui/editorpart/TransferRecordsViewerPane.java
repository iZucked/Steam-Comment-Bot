package com.mmxlabs.models.lng.transfers.ui.editorpart;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersFactory;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.ui.manipulators.TransferIncotermEnumAttributeManipulator;
import com.mmxlabs.models.lng.transfers.ui.manipulators.TransferStatusEnumAttributeManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * Pane (table) which shows TransferRecords in tabular view
 * @author FM
 *
 */
public class TransferRecordsViewerPane extends ScenarioTableViewerPane {
	
	public TransferRecordsViewerPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location, IActionBars actionBars) {
		super(page, part, location, actionBars);
		//final ESelectionService service = PlatformUI.getWorkbench().getService(ESelectionService.class);
		//service.addPostSelectionListener(selectionListener);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		{
			getToolBarManager().appendToGroup(ADD_REMOVE_GROUP, createAddAction());
		}
		
		getToolBarManager().update(true);
		
		addNameManipulator("Name");
		addTypicalColumn("Agreement", new SingleReferenceManipulator(TransfersPackage.eINSTANCE.getTransferRecord_TransferAgreement(), //
				getReferenceValueProviderCache(), getCommandHandler()));
		addTypicalColumn("Deal", new SingleReferenceManipulator(TransfersPackage.eINSTANCE.getTransferRecord_Lhs(), //
				getReferenceValueProviderCache(), getCommandHandler()));
		addTypicalColumn("Next transfer", new SingleReferenceManipulator(TransfersPackage.eINSTANCE.getTransferRecord_Rhs(), //
				getReferenceValueProviderCache(), getCommandHandler()));
		addTypicalColumn("Price", new BasicAttributeManipulator(TransfersPackage.eINSTANCE.getTransferRecord_PriceExpression(), //
				getCommandHandler()));
		addTypicalColumn("Inco", new TransferIncotermEnumAttributeManipulator(TransfersPackage.eINSTANCE.getTransferRecord_Incoterm(), //
				getCommandHandler()));
		addTypicalColumn("Status", new TransferStatusEnumAttributeManipulator(TransfersPackage.eINSTANCE.getTransferRecord_Status(), //
				getCommandHandler()));
		addTypicalColumn("Stale", new BooleanFlagAttributeManipulator(TransfersPackage.eINSTANCE.getTransferRecord_Stale(), //
				getCommandHandler()));
	}

	private Action createAddAction() {
		addAction = new RunnableAction("Add", CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), () -> {
			final CompoundCommand cmd = new CompoundCommand("New transfer record");
			final TransferRecord tr = TransfersFactory.eINSTANCE.createTransferRecord();
			tr.setCargoReleaseDate(LocalDate.now());
			tr.setPricingDate(LocalDate.now());
			final IScenarioDataProvider scenarioDataProvider = getScenarioEditingLocation().getScenarioDataProvider();
			final LNGScenarioModel scenarioModel = ScenarioModelUtil.getScenarioModel(scenarioDataProvider);
			TransferModel tm = ScenarioModelUtil.getTransferModel(scenarioModel);
			if (tm == null) {
				tm = TransfersFactory.eINSTANCE.createTransferModel();
				tm.getTransferRecords().add(tr);
				cmd.append(SetCommand.create(getEditingDomain(), scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_TransferModel(), tm));
			} else {
				cmd.append(AddCommand.create(getEditingDomain(), ScenarioModelUtil.getTransferModel(getScenarioEditingLocation().getScenarioDataProvider()),
						TransfersPackage.Literals.TRANSFER_MODEL__TRANSFER_RECORDS, Collections.singleton(tr)));
			}
			final CommandStack commandStack = getEditingDomain().getCommandStack();
			commandStack.execute(cmd);
			DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), tr, commandStack.getMostRecentCommand());
		});
		return addAction;
	}
	
	@Override
	protected Action createDuplicateAction() {
		return null;
	}
	
//	@Override
//	public void dispose() {
//		final ESelectionService service = PlatformUI.getWorkbench().getService(ESelectionService.class);
//		service.removePostSelectionListener(selectionListener);
//
//		super.dispose();
//	}
//	
//	private final ISelectionListener selectionListener = new ISelectionListener() {
//
//		private AtomicBoolean inSelectionChanged = new AtomicBoolean(false);
//
//		@Override
//		public void selectionChanged(final MPart part, final Object selectedObject) {
//
//			{
//				final IWorkbenchPart view = SelectionHelper.getE3Part(part);
//
//				if (view == TransferRecordsViewerPane.this.part) {
//					return;
//				}
//				if (view instanceof PropertySheet) {
//					return;
//				}
//			}
//
//			// Convert selection
//			final ISelection selection = SelectionHelper.adaptSelection(selectedObject);
//
//			// Avoid re-entrant selection changes.
//			if (inSelectionChanged.compareAndSet(false, true)) {
//				try {
//					// Avoid cyclic selection changes
//					if (TransferRecordsViewerPane.this.page == null || TransferRecordsViewerPane.this.page.getActivePart() == TransferRecordsViewerPane.this.part) {
//						return;
//					}
//					if (viewer != null) {
//						try {
//							viewer.setSelection(selection, true);
//						} catch (final Exception e) {
//							//log.error(e.getMessage(), e);
//						}
//					}
//				} finally {
//					inSelectionChanged.set(false);
//				}
//			}
//		}
//
//	};
}
