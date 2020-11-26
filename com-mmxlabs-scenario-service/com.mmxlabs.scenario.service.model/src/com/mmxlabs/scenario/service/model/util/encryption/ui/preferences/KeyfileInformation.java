/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.util.encryption.ui.preferences;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.resource.URIConverter.Cipher;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.util.encryption.IScenarioCipherProvider;
import com.mmxlabs.scenario.service.model.util.encryption.impl.DelegatingEMFCipher;
import com.mmxlabs.scenario.service.model.util.encryption.impl.IKeyFile;
import com.mmxlabs.scenario.service.model.util.encryption.impl.KeyFileUtil;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileLegacy;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV1;
import com.mmxlabs.scenario.service.model.util.encryption.impl.keyfiles.KeyFileV2;

public class KeyfileInformation extends PreferencePage implements IWorkbenchPreferencePage {

	private static final Logger log = LoggerFactory.getLogger(KeyfileInformation.class);

	public KeyfileInformation() {
		super();
	}

	@Override
	public void init(final IWorkbench workbench) {

	}

	@Override
	protected Control createContents(final Composite parent) {

		final Composite c = new Composite(parent, SWT.None);
		c.setLayout(new FillLayout());

		ServiceHelper.withCheckedOptionalServiceConsumer(IScenarioCipherProvider.class, p -> {
			if (p == null) {
				final Label lbl = new Label(c, SWT.NONE);
				lbl.setText("No encryption keys found");
			} else {
				final Cipher sharedCipher = p.getSharedCipher();
				if (sharedCipher instanceof DelegatingEMFCipher) {
					final DelegatingEMFCipher delegatingEMFCipher = (DelegatingEMFCipher) sharedCipher;

					final byte[] defaultKey = delegatingEMFCipher.getDefaultKey();
					final List<byte[]> keyfiles = delegatingEMFCipher.listKeys();

					final StringBuilder sb = new StringBuilder();
					sb.append("List of encryption keys\n\n");
					for (final byte[] key : keyfiles) {
						final IKeyFile keyfile = delegatingEMFCipher.getKeyFile(key);
						String version = "Unknown";
						if (keyfile instanceof KeyFileV1) {
							version = "v1";
						} else if (keyfile instanceof KeyFileV2) {
							version = "v2";
						} else if (keyfile instanceof KeyFileLegacy) {
							version = "legacy";
						}
						final String isDefault = (Arrays.equals(defaultKey, key)) ? " (current)" : "";

						sb.append(String.format("ID: %s Type: %s%s\n", KeyFileUtil.byteToString(key, ":"), version, isDefault));
					}

					final Text lbl = new Text(c, SWT.READ_ONLY | SWT.MULTI);
					lbl.setText(sb.toString());

				} else {
					final Label lbl = new Label(c, SWT.NONE);
					lbl.setText("No encryption keys found");
				}
			}
		});

		return c;
	}
}
