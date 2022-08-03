package com.mmxlabs.rcp.common.bugreporting;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.Lists;

public class BugReportException extends RuntimeException {

	public static record AttachedFile(File file, String description) {
	}

	private final String userPromptMsg;
	private final String subject;
	private final String htmlBody;
	private final List<AttachedFile> attachments;
	private final Path tempDataDir;

	public BugReportException(final Path tempDataDir, final String logMessage, String userPromptMsg, final String subject, final String htmlBody, final AttachedFile... attachments) {
		this(tempDataDir, logMessage, userPromptMsg, subject, htmlBody, Lists.newArrayList(attachments));
	}

	public BugReportException(final Path tempDataDir, final String logMessage, String userPromptMsg, final String subject, final String htmlBody, final List<AttachedFile> attachments) {
		super(logMessage);
		this.tempDataDir = tempDataDir;

		this.userPromptMsg = userPromptMsg;
		this.subject = subject;
		this.htmlBody = htmlBody;
		this.attachments = attachments;
	}

	public String getSubject() {
		return subject;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public List<AttachedFile> getAttachments() {
		return attachments;
	}

	public Path getTempDataDir() {
		return tempDataDir;
	}

	public String getUserPromptMsg() {
		return userPromptMsg;
	}
}
