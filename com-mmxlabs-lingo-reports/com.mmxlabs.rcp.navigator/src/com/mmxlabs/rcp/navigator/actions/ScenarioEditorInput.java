package com.mmxlabs.rcp.navigator.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

import com.mmxlabs.rcp.navigator.scenario.ScenarioTreeNodeClass;

/**
 * Extended version of {@link FileEditorInput} to present the underlying
 * {@link IFile} resource, but with the UI visible name of the folder.
 * 
 * @author Simon Goodall
 * 
 */
public class ScenarioEditorInput extends FileEditorInput {

	private final ScenarioTreeNodeClass node;

	public ScenarioEditorInput(ScenarioTreeNodeClass node) {
		super((IFile) node.getResource());
		this.node = node;
	}

	@Override
	public String getName() {
		return node.getContainer().getName();
	}

	@Override
	public String getToolTipText() {
		return node.getContainer().getFullPath().toString();
	}
}