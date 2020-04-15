package com.mmxlabs.models.lng.scenario.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class RemoveEmptyCargoesHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			return null;
		}

		final IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);
		if (editor == null || editor.getScenarioInstance() == null) {
			return null;
		}
		
		final ScenarioInstance instance = editor.getScenarioInstance();
		final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(instance);
		final IScenarioDataProvider scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ScenarioDataProvide:1");
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioDataProvider);
		final EditingDomain ed = scenarioDataProvider.getEditingDomain();
		
		final CompoundCommand temp = new CompoundCommand();
		
		for (final Cargo cargo : cargoModel.getCargoes()) {
			if (cargo.getSlots().isEmpty() || cargo.getSlots().size() == 1) {
				temp.append(DeleteCommand.create(ed, cargo));
			} else {
				boolean loadExists = false;
				boolean dischargeExists = false;
				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						loadExists = true;
					}
					if (slot instanceof DischargeSlot) {
						dischargeExists = true;
					}
				}
				if (!loadExists || !dischargeExists) {
					temp.append(DeleteCommand.create(ed, cargo));
				}
			}
		}
		
		ed.getCommandStack().execute(temp);
		
		return null;
	}

}
