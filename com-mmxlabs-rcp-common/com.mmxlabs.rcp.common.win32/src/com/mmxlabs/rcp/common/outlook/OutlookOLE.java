/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.outlook;

import java.io.File;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleClientSite;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Shell;

/**
 * Based on http://www.vogella.com/articles/EclipseMicrosoftIntegration/article.html
 * 
 * @see http://stackoverflow.com/questions/8398151/is-oft-outlook-template-supported-by-java-or-another-java-api-out-there
 * @see http://msdn.microsoft.com/en-us/library/cc237549(v=prot.13).aspx
 * 
 *      NOTE: For this to work, Office and Java/App both need to be 32-bit or both need to be 64-bit. If one is 32-bit and the other is 64-bit, the OleClientSite call will fail with a result code of
 *      -2147221164. @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=361543 for further details.
 * 
 * @author Simon Goodall
 * 
 */
public class OutlookOLE {

	public static void send(final Shell shell, final List<String> toAddresses, final List<String> ccAddresses, final List<String> bccAddresses, final String subject, final boolean isHTML,
			final String body, final List<String> attachmentFileNames) {

		// Pre-process attachments in case of error now to avoid creating OLE resources
		if (attachmentFileNames != null) {
			for (final String fName : attachmentFileNames) {
				final File file = new File(fName);
				if (!file.exists()) {
					throw new IllegalArgumentException("Attachment does not exist: " + fName);
				}
			}
		}

		final OleFrame frame = new OleFrame(shell, SWT.NONE);
		// This should start outlook if it is not running yet
		final OleClientSite site = new OleClientSite(frame, SWT.NONE, "OVCtl.OVCtl");
		site.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
		// Now get the outlook application
		final OleClientSite site2 = new OleClientSite(frame, SWT.NONE, "Outlook.Application");
		final OleAutomation outlook = new OleAutomation(site2);
		//
		final OleAutomation mail = invoke(outlook, "CreateItem", 0 /* Mail item */).getAutomation();

		setAddressField("To", toAddresses, mail);
		setAddressField("Cc", ccAddresses, mail);
		setAddressField("Bcc", bccAddresses, mail);
		setProperty(mail, "Subject", subject);
		if (isHTML) {
			setProperty(mail, "BodyFormat", 2 /* HTML */);
			setProperty(mail, "HtmlBody", body);
		} else {
			setProperty(mail, "Body", body);
		}
		if (attachmentFileNames != null) {
			for (final String fName : attachmentFileNames) {
				final File file = new File(fName);
				assert file.exists();
				final OleAutomation attachments = getProperty(mail, "Attachments");
				invoke(attachments, "Add", fName);
			}
		}
		invoke(mail, "Display" /* or "Send" */);

		// TODO: Test to see if this is required/works properly.
		frame.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(final DisposeEvent e) {
				mail.dispose();
				outlook.dispose();
				site2.dispose();
				site.dispose();
				frame.dispose();
			}
		});
	}

	private static void setAddressField(final String field, final List<String> addresses, final OleAutomation mail) {
		if (addresses != null && !addresses.isEmpty()) {
			final StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			for (final String address : addresses) {
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(";");
				}
				sb.append(address);
			}
			setProperty(mail, field, sb.toString());
		}
	}

	private static OleAutomation getProperty(final OleAutomation auto, final String name) {
		final Variant varResult = auto.getProperty(property(auto, name));
		if (varResult != null && varResult.getType() != OLE.VT_EMPTY) {
			final OleAutomation result = varResult.getAutomation();
			varResult.dispose();
			return result;
		}
		return null;
	}

	private static Variant invoke(final OleAutomation auto, final String command, final String value) {
		return auto.invoke(property(auto, command), new Variant[] { new Variant(value) });
	}

	private static Variant invoke(final OleAutomation auto, final String command) {
		return auto.invoke(property(auto, command));
	}

	private static Variant invoke(final OleAutomation auto, final String command, final int value) {
		return auto.invoke(property(auto, command), new Variant[] { new Variant(value) });
	}

	private static boolean setProperty(final OleAutomation auto, final String name, final String value) {
		return auto.setProperty(property(auto, name), new Variant(value));
	}

	private static boolean setProperty(final OleAutomation auto, final String name, final int value) {
		return auto.setProperty(property(auto, name), new Variant(value));
	}

	private static int property(final OleAutomation auto, final String name) {
		return auto.getIDsOfNames(new String[] { name })[0];
	}
}
