package com.mmxlabs.models.lng.scenario.importWizards;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoImportAction;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.wizards.BulkImportPage;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.lng.ui.actions.SimpleImportAction;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * Class to bulk-import information from CSV files.
 * 
 * Contains modified cut&paste code from ImportAction and SimpleImportAction (class was written in a hurry). TODO: Move duplicated code into a common code object.
 * 
 * @author Simon McGregor
 * @since 3.0
 * 
 */
public class BulkImportCSVHandler extends AbstractHandler {
	public ImportAction getImportAction(final int importTarget, final ImportAction.ImportHooksProvider ihp) {
		final MMXRootObject root = ihp.getRootObject();
		if (root instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) root;
			if (importTarget == BulkImportPage.CHOICE_COMMODITY_INDICES) {
				return new SimpleImportAction(ihp, new SimpleImportAction.FieldInfoProvider() {
					@Override
					public EObject getContainer() {
						return scenarioModel.getPricingModel();
					}

					@Override
					public EReference getContainment() {
						return PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES;
					}

				});
			}

			if (importTarget == BulkImportPage.CHOICE_CARGOES) {
				return new CargoImportAction(ihp, new SimpleImportAction.FieldInfoProvider() {

					@Override
					public EReference getContainment() {
						return CargoPackage.Literals.CARGO_MODEL__CARGOES;
					}

					@Override
					public EObject getContainer() {
						return scenarioModel.getPortfolioModel().getCargoModel();
					}
				});
			}
		}

		return null;
	}

	public ImportAction.ImportHooksProvider getHooksProvider(final ScenarioInstance instance, final Shell shell, final String importFilePath, final char csvSeparator) {
		return new ImportAction.ImportHooksProvider() {
			ScenarioLock lock;

			@Override
			public Shell getShell() {
				return shell;
			}

			@Override
			public MMXRootObject getRootObject() {
				return (MMXRootObject) instance.getInstance();
			}

			@Override
			public EditingDomain getEditingDomain() {
				return (EditingDomain) instance.getAdapters().get(EditingDomain.class);
			}

			@Override
			public String getImportFilePath() {
				// TODO Auto-generated method stub
				return importFilePath;
			}

			@Override
			public void lock() {
				((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(true);
				lock = instance.getLock(ScenarioLock.EDITORS);
			}

			@Override
			public void unlock() {
				((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(false);
				if (lock != null) {
					lock.release();
				}
			}

			@Override
			public char getCsvSeparator() {
				return csvSeparator;
			}

		};
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			// action has been disposed
			return null;
		}

		Shell shell = HandlerUtil.getActiveShell(event);
		IScenarioServiceEditorInput editor = (IScenarioServiceEditorInput) HandlerUtil.getActiveEditorInput(event);

		BulkImportWizard wizard = new BulkImportWizard(editor.getScenarioInstance());
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);

		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();

		List<ScenarioInstance> scenarios = wizard.getSelectedScenarios();
		String importFilename = wizard.getImportFilename();

		int importTarget = wizard.getImportedField();

		if (scenarios != null && importFilename != null && !"".equals(importFilename)) {
			char separator = wizard.getCsvSeparator();

			bulkImport(importTarget, shell, scenarios, importFilename, separator);
		}

		return null;
	}

	void bulkImport(int importTarget, Shell shell, List<ScenarioInstance> instances, String filename, char separator) {
		try {
			for (ScenarioInstance instance : instances) {
				if (instance.getInstance() == null) {
					instance.getScenarioService().load(instance);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (ScenarioInstance instance : instances) {
			ImportAction.ImportHooksProvider ihp = getHooksProvider(instance, shell, filename, separator);
			ImportAction action = getImportAction(importTarget, ihp);

			action.safelyImport();
		}

	}

}
