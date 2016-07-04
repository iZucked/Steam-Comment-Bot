/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.license.ssl.preferences;

import java.security.cert.X509Certificate;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.license.ssl.LicenseChecker;

public class LicenseInformation extends PreferencePage implements IWorkbenchPreferencePage {

	private static final Logger log = LoggerFactory.getLogger(LicenseInformation.class);

	public LicenseInformation() {
		super();
		setDescription("License Information");
	}

	@Override
	public void init(final IWorkbench workbench) {

	}

	@Override
	protected Control createContents(final Composite parent) {

		final Composite c = new Composite(parent, SWT.None);

		c.setLayout(new GridLayout(2, true));

		X509Certificate cert = null;
		try {
			cert = LicenseChecker.getClientLicense();
		} catch (final Exception e) {
			log.error("Unable to load license: " + e.getMessage(), e);
		}

		{
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("Licensed To:");

			final Text txt = new Text(c, SWT.READ_ONLY);
			if (cert != null) {

				// Based on http://stackoverflow.com/questions/7933468/parsing-the-cn-out-of-a-certificate-dn
				final X500Principal principal = cert.getSubjectX500Principal();
				LdapName ln = null;
				try {
					ln = new LdapName(principal.getName());
				} catch (final InvalidNameException e) {
					// Ignore
				}
				String cn = "Unknown";
				if (ln != null) {
					for (final Rdn rdn : ln.getRdns()) {
						if (rdn.getType().equalsIgnoreCase("CN")) {
							cn = (String) rdn.getValue();
							break;
						}
					}
				}

				txt.setText(cn);
			} else {
				txt.setText("Unable to load license");
			}
		}

		{
			final Label lbl = new Label(c, SWT.NONE);
			lbl.setText("Expires:");

			final Text txt = new Text(c, SWT.READ_ONLY);
			if (cert != null) {
				txt.setText("" + cert.getNotAfter());
			}
		}

		return c;
	}
}
