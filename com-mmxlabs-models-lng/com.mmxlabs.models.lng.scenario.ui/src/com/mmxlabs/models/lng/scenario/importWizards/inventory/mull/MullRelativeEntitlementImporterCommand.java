package com.mmxlabs.models.lng.scenario.importWizards.inventory.mull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.adp.utils.IMullRelativeEntitlementImportCommandProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class MullRelativeEntitlementImporterCommand implements IMullRelativeEntitlementImportCommandProvider {

	@Override
	public void run(@NonNull final MullSubprofile subprofile, @NonNull final ScenarioInstance scenarioInstance) {
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		final MullRelativeEntitlementImportWizard wizard = new MullRelativeEntitlementImportWizard(scenarioInstance, "Import MULL entitlements", subprofile);
		wizard.init(activeWorkbenchWindow.getWorkbench(), null);
		final Shell shell = activeWorkbenchWindow.getShell();
		final WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();
	}

}
