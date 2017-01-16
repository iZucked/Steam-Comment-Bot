/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.actions;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.MessageDialogWithCheckbox;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * Action which deletes selected cargo, prompting the user to decide whether they want to delete the associated slots, using a "Use this choice for all" checkbox if the selection has more than one
 * item in it. If the user selects "Cancel" while going through the cargoes, no cargoes are deleted.
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
		// final int LOADBUTTON = 0;
		final int CARGOBUTTON = 0;
		final int ALLBUTTON = 1;
		// final int DISCHARGEBUTTON = 3;
		final int CANCELBUTTON = 2;
		final String[] dialogOptions = new String[3];
		// dialogOptions[LOADBUTTON] = "Cargo and Load";
		dialogOptions[CARGOBUTTON] = "Cargo Only";
		dialogOptions[ALLBUTTON] = "Both Slots";
		// dialogOptions[DISCHARGEBUTTON] = "Cargo and Discharge";
		dialogOptions[CANCELBUTTON] = "Cancel";

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
						final IStructuredSelection structuredSelection = (IStructuredSelection) sel;
						final EditingDomain ed = part.getEditingDomain();
						// Copy selection
						final ArrayList<?> selectedObjects = new ArrayList<>((List<?>)structuredSelection.toList());
						// Set up a list of objects for deletion
						// This will include the cargoes but may also include load or discharge slots
						final Set<Object> trash = new LinkedHashSet<Object>();
						trash.addAll(selectedObjects);
						// Filter out slots which are already linked to a cargo - this dialog below will re-add them if required.
						for (final Object obj : selectedObjects) {
							if (obj instanceof Cargo) {
								final Cargo cargo = (Cargo) obj;
								trash.removeAll(cargo.getSlots());
							}
						}

						int dialogResponse = 0;
						boolean useResponseForAll = false;
						boolean dialogDefault = true;
						final List<Object> targetObjects = new ArrayList<Object>(trash);
						final int size = targetObjects.size();

						for (int i = 0; i < size; i++) {
							final Object object = targetObjects.get(i);
							final Cargo cargo = (object instanceof Cargo ? (Cargo) object : null);
							if (cargo != null) {
								int loadSlotCount = 0;
								int dischargeSlotCount = 0;
								int spotLoadSlotCount = 0;
								int spotDischargeSlotCount = 0;

								for (final Slot slot : cargo.getSlots()) {
									if (slot instanceof LoadSlot) {
										loadSlotCount++;
										if (slot instanceof SpotLoadSlot) {
											spotLoadSlotCount++;
										}
									} else if (slot instanceof DischargeSlot) {
										dischargeSlotCount++;
										if (slot instanceof SpotDischargeSlot) {
											spotDischargeSlotCount++;
										}
									}
								}

								// Only count as "spot" if all slots of type are spot
								final boolean spotLoad = spotLoadSlotCount > 0 && loadSlotCount == spotLoadSlotCount;
								final boolean spotDischarge = spotDischargeSlotCount > 0 && dischargeSlotCount == spotDischargeSlotCount;

								// Pop up a dialog if there are remaining cargoes and the user has not checked "use this response for all".
								if (!useResponseForAll) {
									// final String dialogMessage = String.format("Do you want to delete the load and discharge slots for cargo %s?", ((Cargo) cargo).getName());
									final String dialogMessage = "What would you like to delete?\n\nNote: Market model slots will be automatically deleted.";

									final String dialogTitle = "Delete Cargo";
									// get the active shell for the default display - a better solution would be to get the shell for the app
									final Shell shell = Display.getDefault().getActiveShell();
									// don't display a checkbox if there is only one remaining item
									final boolean showCheckbox = (i < size - 1);
									final int remaining = size - 1 - i;
									final String cargoSuffix = (remaining == 1 ? "" : "es");
									final String cargoIndicator = (remaining == 1 ? "that" : "those");
									final String checkboxTitle = String.format("Use this choice for another %d selected cargo%s? (Spot slots for %s cargo%s will be automatically deleted.)",
											remaining, cargoSuffix, cargoIndicator, cargoSuffix);

									// create a new customised dialog which hides the checkbox if appropriate and enables / disables
									// the buttons according to spot slots and checkbox status
									final MessageDialogWithCheckbox dialog = new MessageDialogWithCheckbox(shell, dialogTitle, null, dialogMessage, MessageDialog.QUESTION, dialogOptions, ALLBUTTON,
											checkboxTitle, dialogDefault && showCheckbox) {

										// enable / disable the buttons depending on the toggle state
										protected void fixDisplay() {
											final boolean toggleState = getToggleState();

											// disable buttons appropriately if the load or discharge slots are spot slots
											// getButton(DISCHARGEBUTTON).setEnabled(toggleState || !spotLoad);
											// getButton(LOADBUTTON).setEnabled(toggleState || !spotDischarge);
											getButton(CARGOBUTTON).setEnabled(toggleState || (!spotDischarge && !spotLoad));

											// change the dialog message appropriately if there are spot slots for this cargo
											String labelText = dialogMessage;
											if (spotLoad || spotDischarge) {
												labelText += " (Spot slots from this cargo will be deleted.)";
											}
											messageLabel.setText(labelText);
										}

										// make sure the buttons are updated when the checkbox is ticked / unticked
										protected Button createToggleButton(final Composite parent) {
											final Button checkbox = super.createToggleButton(parent);
											// don't display the checkbox if there is only one remaining item
											checkbox.setVisible(showCheckbox);
											checkbox.addSelectionListener(new SelectionAdapter() {
												public void widgetSelected(final SelectionEvent e) {
													fixDisplay();
												}
											});
											return checkbox;
										}

										// make sure the buttons are enabled / disabled correctly when the dialog is created
										protected Control createContents(final Composite shell) {
											final Control result = super.createContents(shell);
											fixDisplay();
											return result;
										}
									};
									dialogResponse = dialog.open();

									// "Cancel" cancels the deletion for all cargoes (including previous ones in this selection)
									if (dialogResponse == CANCELBUTTON || dialogResponse < 0) {
										return;
									}
									// remember the checkbox state
									dialogDefault = useResponseForAll = dialog.getToggleState();
								}
							}

							final boolean deleteLoad = (dialogResponse == ALLBUTTON);
							final boolean deleteDischarge = (dialogResponse == ALLBUTTON);
							if (cargo != null) {
								for (final Slot slot : cargo.getSlots()) {

									if (slot instanceof LoadSlot) {
										final LoadSlot loadSlot = (LoadSlot) slot;

										// delete the load slot if the user has so directed (or delete it automatically
										// if it's a spot slot)
										if (deleteLoad || loadSlot instanceof SpotSlot) {
											trash.add(loadSlot);
										}
									} else if (slot instanceof DischargeSlot) {

										final DischargeSlot dischargeSlot = (DischargeSlot) slot;
										// delete the discharge slot if the user has so directed (or delete it automatically
										// if it's a spot slot)
										if (deleteDischarge || dischargeSlot instanceof SpotSlot) {
											trash.add(dischargeSlot);
										}
									}
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
