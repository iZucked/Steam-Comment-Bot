package com.mmxlabs.models.lng.cargo.ui.actions;

import java.util.ArrayList;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.MessageDialogWithCheckbox;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * Action which deletes selected cargo, prompting the user to decide whether they want to
 * delete the associated slots, using a "Use this choice for all" checkbox if the selection
 * has more than one item in it. If the user selects "Cancel" while going through the 
 * cargoes, no cargoes are deleted.
 * 
 * Internally performs a single delete action which can be reversed by "Undo".
 * 
 * @author smcgregor
 *
 */
public class DeleteSelectedCargoAction extends ScenarioModifyingAction {
	IScenarioEditingLocation part;
	Viewer viewer;
	
	public DeleteSelectedCargoAction(final IScenarioEditingLocation jointModelEditorPart, final Viewer aViewer) {
		super("Delete");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		this.part = jointModelEditorPart;
		this.viewer = aViewer;
		viewer.addSelectionChangedListener(this);
	}
	
	public void run() {
		// some constants for the dialog options
		final int LOAD = 0; 
		final int CARGO = 1; 
		final int ALL = 2; 
		final int DISCHARGE = 3; 
		final int CANCEL = 4;
		final String [] dialogOptions = new String[5];
		dialogOptions[LOAD] = "Cargo and Load";
		dialogOptions[CARGO] = "Cargo Only";
		dialogOptions[ALL] = "Cargo, Load and Discharge";
		dialogOptions[DISCHARGE] = "Cargo and Discharge";
		dialogOptions[CANCEL] = "Cancel";				
		
		// Delete commands can be slow, so show the busy indicator while deleting.
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {

				final ScenarioLock editorLock = part.getEditorLock();
				editorLock.awaitClaim();
				part.setDisableUpdates(true);
				
				try {
					final ISelection sel = getLastSelection();
					if (sel instanceof IStructuredSelection) {
						final EditingDomain ed = part.getEditingDomain();
						// Copy selection								
						final ArrayList<Object> cargoes = new ArrayList<Object>(((IStructuredSelection) sel).toList());
						// Set up a list of objects for deletion 
						// This will include the cargoes but may also include load or discharge slots
						final ArrayList<Object> trash = new ArrayList<Object>();
						trash.addAll(0, cargoes);

						int dialogResponse = 0;
						boolean useResponseForAll = false;
						boolean dialogDefault = true;
						int size = cargoes.size();
						
						for (int i = 0; i < size; i++) {
							Object cargo = cargoes.get(i);
							if (!useResponseForAll) {
								MessageDialog dialog;
								String dialogMessage = String.format("Do you want to delete the load and discharge slots for cargo %s?", ((Cargo) cargo).getName());
								String dialogTitle = "Delete Cargo";
								// get the active shell for the default display - a better solution would be to get the shell for the app
								Shell shell = Display.getDefault().getActiveShell();
								// don't display a checkbox if there is only one remaining item
								if (i < size-1) {
									// we set the default for the dialog to "Cancel" since this is what will be returned if the dialog is closed
									int remaining = size-1-i; 
									String checkboxTitle = String.format("Use this choice for another %d selected cargo%s?", remaining, (remaining == 1 ? "" : "es"));
									dialog = new MessageDialogWithCheckbox(shell, dialogTitle, null, dialogMessage, MessageDialog.QUESTION, dialogOptions, CANCEL, checkboxTitle, dialogDefault);
								}
								else {
									// we set the default for the dialog to "Cancel" since this is what will be returned if the dialog is closed
									dialog = new MessageDialog(shell, dialogTitle, null, dialogMessage, MessageDialog.QUESTION, dialogOptions, CANCEL);
								}
								dialogResponse = dialog.open();
								if (dialogResponse == CANCEL) {
									return;
								}
								if (dialog instanceof MessageDialogWithCheckbox) {								
									dialogDefault = useResponseForAll = ((MessageDialogWithCheckbox) dialog).getToggleState();
								}
							}
							
							boolean deleteLoad = (dialogResponse == LOAD) || (dialogResponse == ALL);
							boolean deleteDischarge = (dialogResponse == DISCHARGE) || (dialogResponse == ALL);				
							if (cargo instanceof Cargo) {
								LoadSlot loadSlot = ((Cargo) cargo).getLoadSlot();
								DischargeSlot dischargeSlot = ((Cargo) cargo).getDischargeSlot();
								if (deleteLoad && (loadSlot != null)) {											
									trash.add(loadSlot);
								}
								if (deleteDischarge && (dischargeSlot != null)) {
									trash.add(((Cargo) cargo).getDischargeSlot());
								}
							}
						}
						
						
						// Clear current selection
						selectionChanged(new SelectionChangedEvent(viewer, StructuredSelection.EMPTY));

						// Execute command
						final Command deleteCommand = DeleteCommand.create(ed, trash);
						ed.getCommandStack().execute(deleteCommand);
					}
				} finally {
					editorLock.release();
					part.setDisableUpdates(false);
				}
			}
		};
		BusyIndicator.showWhile(null, runnable);
	}

	@Override
	protected boolean isApplicableToSelection(final ISelection selection) {
		return selection.isEmpty() == false && selection instanceof IStructuredSelection;
	}			

}
