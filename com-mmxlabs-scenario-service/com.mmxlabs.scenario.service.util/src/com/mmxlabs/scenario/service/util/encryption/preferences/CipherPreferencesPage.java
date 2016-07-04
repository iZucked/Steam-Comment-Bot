/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.encryption.preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
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
import com.mmxlabs.scenario.service.util.encryption.ui.NewPasswordPromptDialog;

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
	public void init(final IWorkbench workbench) {
	}

	@Override
	protected Control createContents(final Composite parent) {

		final Button btn = new Button(parent, SWT.PUSH);
		btn.setText("Generate a key");
		btn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final NewPasswordPromptDialog dialog = new NewPasswordPromptDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
				dialog.setBlockOnOpen(true);
				if (dialog.open() == Window.OK) {

					final KeyFile newKey = KeyFile.generateKey(256);
					final File f = new File("C:/temp/lingo.data");
					try (FileOutputStream fos = new FileOutputStream(f)) {
						final KeyFileHeader header = new KeyFileHeader(KeyFileHeader.VERSION__0, KeyFileHeader.PASSWORD_TYPE__PROMPT, dialog.getName());
						header.write(fos);

						final char[] password = dialog.getPassword();
						newKey.save(fos, password);
						// Zero out array
						Arrays.fill(password, (char) 0);
					} catch (final Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {

			}
		});

		// TODO Auto-generated method stub
		return parent;
	}
}