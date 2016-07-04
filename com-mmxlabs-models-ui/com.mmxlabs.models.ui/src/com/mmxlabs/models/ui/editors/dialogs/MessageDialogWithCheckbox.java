/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple MessageDialog with a checkbox. 
 * Copy-and-pasted from jface MessageDialogWithToggle class, but with most of the logic
 * snipped out since it messed with return values (and we don't need a preference store).
 * 
 * Behaves identically to MessageDialog except that it takes "toggleMessage" and "toggleState"
 * parameters in its constructor, displays a checkbox with a message, and can return the
 * toggle state via a getToggleState() accessor.
 * 
 * 
 * Simon McGregor 24/10/2012
 * 
 */
public class MessageDialogWithCheckbox extends MessageDialog {

    /**
     * The toggle button (widget). This value is <code>null</code> until the
     * dialog is created.
     */
    private Button toggleButton = null;

    /**
     * The message displayed to the user, with the toggle button. This is the
     * text besides the toggle. If it is <code>null</code>, this means that
     * the default text for the toggle should be used.
     */
    private String toggleMessage;

    /**
     * The initial selected state of the toggle.
     */
    private boolean toggleState;

    /**
     * Creates a message dialog with a toggle. See the superclass constructor
     * for info on the other parameters.
     * 
     * @param parentShell
     *            the parent shell
     * @param dialogTitle
     *            the dialog title, or <code>null</code> if none
     * @param image
     *            the dialog title image, or <code>null</code> if none
     * @param message
     *            the dialog message
     * @param dialogImageType
     *            one of the following values:
     *            <ul>
     *            <li><code>MessageDialog.NONE</code> for a dialog with no
     *            image</li>
     *            <li><code>MessageDialog.ERROR</code> for a dialog with an
     *            error image</li>
     *            <li><code>MessageDialog.INFORMATION</code> for a dialog
     *            with an information image</li>
     *            <li><code>MessageDialog.QUESTION </code> for a dialog with a
     *            question image</li>
     *            <li><code>MessageDialog.WARNING</code> for a dialog with a
     *            warning image</li>
     *            </ul>
     * @param dialogButtonLabels
     *            an array of labels for the buttons in the button bar
     * @param defaultIndex
     *            the index in the button label array of the default button
     * @param toggleMessage
     *            the message for the toggle control, or <code>null</code> for
     *            the default message
     * @param toggleState
     *            the initial state for the toggle
     *  
     */
    public MessageDialogWithCheckbox(Shell parentShell, String dialogTitle,
            Image image, String message, int dialogImageType,
            String[] dialogButtonLabels, int defaultIndex,
            String toggleMessage, boolean toggleState) {
        super(parentShell, dialogTitle, image, message, dialogImageType,
                dialogButtonLabels, defaultIndex);
        this.toggleMessage = toggleMessage;
        this.toggleState = toggleState;
        setButtonLabels(dialogButtonLabels);
    }


    /**
     * @see Dialog#createDialogArea(Composite)
     */
    @Override
	protected Control createDialogArea(Composite parent) {
        Composite dialogAreaComposite = (Composite) super
                .createDialogArea(parent);
        setToggleButton(createToggleButton(dialogAreaComposite));
        return dialogAreaComposite;
    }

    /**
     * Creates a toggle button without any text or state.  The text and state
     * will be created by <code>createDialogArea</code>. 
     * 
     * @param parent
     *            The composite in which the toggle button should be placed;
     *            must not be <code>null</code>.
     * @return The added toggle button; never <code>null</code>.
     */
    protected Button createToggleButton(Composite parent) {
        final Button button = new Button(parent, SWT.CHECK | SWT.LEFT);

        GridData data = new GridData(SWT.NONE);
        data.horizontalSpan = 2;
        button.setLayoutData(data);
        button.setFont(parent.getFont());

        button.addSelectionListener(new SelectionAdapter() {

            @Override
			public void widgetSelected(SelectionEvent e) {
                toggleState = button.getSelection();
            }

        });

        return button;
    }

    /**
     * Returns the toggle button.
     * 
     * @return the toggle button
     */
    protected Button getToggleButton() {
        return toggleButton;
    }

    /**
     * Returns the toggle state. This can be called even after the dialog is
     * closed.
     * 
     * @return <code>true</code> if the toggle button is checked,
     *         <code>false</code> if not
     */
    public boolean getToggleState() {
        return toggleState;
    }


    /**
     * A mutator for the button providing the toggle option. If the button
     * exists, then it will automatically get the text set to the current toggle
     * message, and its selection state set to the current selection state.
     * 
     * @param button
     *            The button to use; must not be <code>null</code>.
     */
    protected void setToggleButton(Button button) {
        if (button == null) {
            throw new NullPointerException(
                    "A message dialog with toggle may not have a null toggle button.");} //$NON-NLS-1$

        if (!button.isDisposed()) {
            final String text;
            if (toggleMessage == null) {
                text = JFaceResources
                        .getString("MessageDialogWithToggle.defaultToggleMessage"); //$NON-NLS-1$
            } else {
                text = toggleMessage;
            }
            button.setText(text);
            button.setSelection(toggleState);
        }

        this.toggleButton = button;
    }

    /**
     * A mutator for the text on the toggle button. The button will
     * automatically get updated with the new text, if it exists.
     * 
     * @param message
     *            The new text of the toggle button; if it is <code>null</code>,
     *            then used the default toggle message.
     */
    protected void setToggleMessage(String message) {
        this.toggleMessage = message;

        if ((toggleButton != null) && (!toggleButton.isDisposed())) {
            final String text;
            if (toggleMessage == null) {
                text = JFaceResources
                        .getString("MessageDialogWithToggle.defaultToggleMessage"); //$NON-NLS-1$
            } else {
                text = toggleMessage;
            }
            toggleButton.setText(text);
        }
    }

    /**
     * A mutator for the state of the toggle button. This method will update the
     * button, if it exists.
     * 
     * @param toggleState
     *            The desired state of the toggle button (<code>true</code>
     *            means the toggle should be selected).
     */
    public void setToggleState(boolean toggleState) {
        this.toggleState = toggleState;

        // Update the button, if it exists.
        if ((toggleButton != null) && (!toggleButton.isDisposed())) {
            toggleButton.setSelection(toggleState);
        }
    }
    
}
