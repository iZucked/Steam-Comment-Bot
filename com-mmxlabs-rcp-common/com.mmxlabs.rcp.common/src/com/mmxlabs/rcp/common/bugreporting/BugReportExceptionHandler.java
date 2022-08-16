/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.bugreporting;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BugReportExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(BugReportExceptionHandler.class);

	public void handleBugReportException(BugReportException ex, boolean promptUser) {
		try {
			if (!promptUser || MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "An error occurred", "Prepare a bug report email?")) {
				final File emailFile = Files.createTempFile(ex.getTempDataDir(), "bugreport", ".eml").toFile();

				try (final FileOutputStream fos = new FileOutputStream(emailFile)) {

					Message message = new MimeMessage(Session.getDefaultInstance(System.getProperties()));
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("support@minimaxlabs.com"));
					message.setSubject(ex.getSubject());

					// Marks email as a draft ready to send
					message.addHeader("X-Unsent", "1");

					Multipart multipart = new MimeMultipart();

					// create the message part
					MimeBodyPart content = new MimeBodyPart();
					// fill message
					content.setContent(ex.getHtmlBody(), "text/html; charset=utf-8");

					multipart.addBodyPart(content);
					// add attachments
					for (var r : ex.getAttachments()) {
						MimeBodyPart attachment = new MimeBodyPart();
						DataSource source = new FileDataSource(r.file());
						attachment.setDataHandler(new DataHandler(source));
						attachment.setFileName(r.file().getName());
						attachment.setHeader("Content-Type", "application/octet-stream");
						attachment.setDescription(r.description());
						attachment.setDisposition(Part.ATTACHMENT);
						multipart.addBodyPart(attachment);
					}

					// integration
					message.setContent(multipart);
					// store file
					message.writeTo(fos);
				}

				// Open the email file using system editor
				java.awt.Desktop.getDesktop().open(emailFile);
			}
		} catch (Exception e) {
			LOG.error("An exception occurred handling a bug report exception. " + e.getMessage(), e);
		}
	}
}
