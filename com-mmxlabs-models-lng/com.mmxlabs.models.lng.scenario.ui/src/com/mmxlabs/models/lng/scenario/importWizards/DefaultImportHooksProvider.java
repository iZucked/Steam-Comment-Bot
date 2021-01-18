/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.importWizards;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.common.commandservice.CommandProviderAwareEditingDomain;
import com.mmxlabs.models.lng.ui.actions.ImportAction;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

public class DefaultImportHooksProvider implements ImportAction.ImportHooksProvider {
	private ScenarioLock lock;
	private ScenarioInstance instance;
	private ModelReference modelReference;
	private Shell shell;
	private String importFilePath;
	private char csvSeparator;
	private char decimalSeparator;

	public DefaultImportHooksProvider(final ScenarioInstance instance, final ModelReference modelReference, final Shell shell, final String importFilePath, final char csvSeparator,
			final char decimalSeparator) {
		this.instance = instance;
		this.modelReference = modelReference;
		this.shell = shell;
		this.importFilePath = importFilePath;
		this.csvSeparator = csvSeparator;
		this.decimalSeparator = decimalSeparator;
		this.lock = modelReference.getLock();
	}

	@Override
	public Shell getShell() {
		return shell;
	}

	@Override
	public MMXRootObject getRootObject() {
		return (MMXRootObject) modelReference.getInstance();
	}

	@Override
	public EditingDomain getEditingDomain() {
		return (EditingDomain) modelReference.getEditingDomain();
	}

	@Override
	public String getImportFilePath() {
		return importFilePath;
	}

	@Override
	public void lock() {
		((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(true);
		lock.lock();
	}

	@Override
	public void unlock() {
		((CommandProviderAwareEditingDomain) getEditingDomain()).setCommandProvidersDisabled(false);
		if (lock != null) {
			lock.unlock();
		}
	}

	@Override
	public char getCsvSeparator() {
		return csvSeparator;
	}

	@Override
	public char getDecimalSeparator() {
		return decimalSeparator;
	}

}
