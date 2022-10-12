/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.navigator.handlers.editor;

import java.time.LocalDate;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ui.inventory.DischargeTriggerDialog;
import com.mmxlabs.models.lng.transformer.ui.inventory.DischargeTriggerHelper;
import com.mmxlabs.models.lng.transformer.ui.inventory.DischargeTriggerRecord;
import com.mmxlabs.models.lng.transformer.ui.inventory.InventoryFileName;
import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.model.manager.SimpleScenarioDataProvider;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

public class DischargeTriggerEditorActionDelegate implements IEditorActionDelegate, IActionDelegate2{
	private IEditorPart editor;
	
	@Override
	public void run(final IAction action) {
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				final ScenarioInstance scenarioInstance = scenarioServiceEditorInput.getScenarioInstance();
				final IScenarioService scenarioService = SSDataManager.Instance.findScenarioService(scenarioInstance);

				if (scenarioInstance != null) {
					try {
						LNGScenarioModel copyScenario = null;
						
						@NonNull
						final
						ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecordChecked(scenarioInstance);
						final LNGScenarioModel scenarioModel;
						try (final ModelReference scenarioRef = modelRecord.aquireReference("DischargeTrigger:1")) {
							scenarioModel = (LNGScenarioModel) (scenarioRef.getInstance());
							copyScenario = EcoreUtil.copy(scenarioModel);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						
						if (copyScenario != null) {
							DischargeTriggerDialog dialog = new DischargeTriggerDialog(Display.getDefault().getActiveShell(), copyScenario, copyScenario.getPromptPeriodStart());
							dialog.open();
							final DischargeTriggerRecord triggerRecord = dialog.getDischargeTriggerRecord();
							if (dialog.getReturnCode() == 0 && triggerRecord.maxQuantity != null && triggerRecord.cutOffDate != null) {
								DischargeTriggerHelper dischargeTriggerHelper = new DischargeTriggerHelper(triggerRecord);
								dischargeTriggerHelper.doMatchAndMoveDischargeTrigger(copyScenario);
								try {
									final String name = InventoryFileName.getName(scenarioInstance.getName(), "_discharge_trigger");
									final IScenarioDataProvider scenarioDataProvider =  SimpleScenarioDataProvider.make(EcoreUtil.copy(modelRecord.getManifest()), copyScenario);
									scenarioService.copyInto(scenarioInstance, scenarioDataProvider, name, new NullProgressMonitor());
								} catch (final Exception e) {
									e.printStackTrace();
								}
							}
						}
					} catch (final Exception e) {
						throw new RuntimeException("Unable to perform discharge trigger", e);
					}
				}
			}
		}
	}
	
	@Override
	public void setActiveEditor(final IAction action, final IEditorPart targetEditor) {
		this.editor = targetEditor;
		if (editor != null) {
			if (editor.getEditorInput() instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editor.getEditorInput();
				final ScenarioInstance instance = scenarioServiceEditorInput.getScenarioInstance();

				{
					Container c = instance;
					while (c != null && !(c instanceof ScenarioService)) {
						c = c.getParent();
					}
					if (c instanceof ScenarioService) {
						ScenarioService scenarioService = (ScenarioService) c;
						if (!scenarioService.isSupportsForking()) {
							action.setEnabled(false);
							return;
						}
					} else {
						action.setEnabled(false);
						return;
					}
				}
			}
		}

		if (action != null) {
			action.setEnabled(true);
		}
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		
	}
	
	@Override
	public void init(IAction action) {
		
	}
	
	@Override
	public void dispose() {
		
	}
	
	@Override
	public void runWithEvent(IAction action, Event event) {
		run(action);
	}
	
}
