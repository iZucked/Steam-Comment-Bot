/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.ui.preferences;

import java.io.File;

import javax.crypto.SecretKey;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileLoader;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;
import com.mmxlabs.scenario.service.model.util.encryption.ui.NewPasswordPromptDialog;

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
		btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(final SelectionEvent e) {

				final NewPasswordPromptDialog dialog = new NewPasswordPromptDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
				dialog.setBlockOnOpen(true);
				if (dialog.open() == Window.OK) {

					final SecretKey newKey = KeyFileV2.generateKey(256);
					final File f = new File("C:/temp/lingo.data");
					try {
						if (f.exists()) {
							KeyFileLoader.initalise(f);
						}
						{
							final byte[] keyUUID = new byte[KeyFileUtil.UUID_LENGTH];
							EcoreUtil.generateUUID(keyUUID);
							KeyFileLoader.addToStore(f, keyUUID, newKey, KeyFileV2.KEYFILE_TYPE);
						}
					} catch (final Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		return parent;
	}
}