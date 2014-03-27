/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.scenario.service.util.encryption.impl.KeyFileHeader;
import com.mmxlabs.scenario.service.util.encryption.impl.v1.KeyFile;
import com.mmxlabs.scenario.service.util.encryption.ui.PasswordPromptDialog;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that
 * allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main plug-in class. That way, preferences can be accessed directly via the preference
 * store.
 */

public class CipherPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

	public CipherPreferencesPage() {
		super();
		setDescription("Cipher");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {

		Button btn = new Button(parent, SWT.PUSH);
		btn.setText("Generate a key");
		btn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

				PasswordPromptDialog dialog = new PasswordPromptDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
				dialog.setBlockOnOpen(true);
				dialog.open();

				KeyFile newKey = KeyFile.generateKey(128);
				File f = new File("C:/temp/lingodata.key");
				try (FileOutputStream fos = new FileOutputStream(f)) {
					KeyFileHeader header = new KeyFileHeader(KeyFileHeader.VERSION__0, KeyFileHeader.PASSWORD_TYPE__PROMPT, dialog.getName());
					header.write(fos);

					char[] password = dialog.getPassword();
					newKey.save(fos, password);
					// Zero out array
					Arrays.fill(password, (char) 0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// TODO Auto-generated method stub
		return parent;
	}
}