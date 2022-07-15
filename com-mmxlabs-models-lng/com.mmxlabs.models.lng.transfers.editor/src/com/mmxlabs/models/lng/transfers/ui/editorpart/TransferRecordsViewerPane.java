package com.mmxlabs.models.lng.transfers.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersFactory;
import com.mmxlabs.models.lng.transfers.TransfersPackage;
import com.mmxlabs.models.lng.transfers.ui.manipulators.TransferIncotermEnumAttributeManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
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

	private final IScenarioEditingLocation scenarioEditingLocation;
	
	public TransferRecordsViewerPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location, IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.scenarioEditingLocation = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		{
			getToolBarManager().appendToGroup(ADD_REMOVE_GROUP, createAddAction());
		}
		
		getToolBarManager().update(true);
		
		addNameManipulator("Name");
		addTypicalColumn("Price Expression", new BasicAttributeManipulator(TransfersPackage.eINSTANCE.getTransferRecord_PriceExpression(), //
				getCommandHandler()));
		addTypicalColumn("Incoterm", new TransferIncotermEnumAttributeManipulator(TransfersPackage.eINSTANCE.getTransferRecord_Incoterm(), //
				getCommandHandler()));

	}

	private Action createAddAction() {
		addAction = new RunnableAction("Add", CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), () -> {
			final CompoundCommand cmd = new CompoundCommand("New transfer record");
			final TransferRecord ta = TransfersFactory.eINSTANCE.createTransferRecord();
			final IScenarioDataProvider scenarioDataProvider = getScenarioEditingLocation().getScenarioDataProvider();
			final LNGScenarioModel scenarioModel = ScenarioModelUtil.getScenarioModel(scenarioDataProvider);
			TransferModel tm = ScenarioModelUtil.getTransferModel(scenarioModel);
			if (tm == null) {
				tm = TransfersFactory.eINSTANCE.createTransferModel();
				tm.getTransferRecords().add(ta);
				cmd.append(SetCommand.create(getEditingDomain(), scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_TransferModel(), tm));
			} else {
				cmd.append(AddCommand.create(getEditingDomain(), ScenarioModelUtil.getTransferModel(getScenarioEditingLocation().getScenarioDataProvider()),
						TransfersPackage.Literals.TRANSFER_MODEL__TRANSFER_RECORDS, Collections.singleton(ta)));
			}
			final CommandStack commandStack = getEditingDomain().getCommandStack();
			commandStack.execute(cmd);
			DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), ta, commandStack.getMostRecentCommand());
		});
		return addAction;
	}
	
	@Override
	protected Action createDuplicateAction() {
		return null;
	}
}
