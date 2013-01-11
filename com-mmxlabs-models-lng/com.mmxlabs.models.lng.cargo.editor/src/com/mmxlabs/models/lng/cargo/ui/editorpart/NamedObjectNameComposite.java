/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collection;
import java.util.LinkedList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;

public class NamedObjectNameComposite extends Composite implements IDisplayComposite, IInlineEditorContainer {

	private NamedObject object;
	protected final LinkedList<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	private ICommandHandler commandHandler;

	public NamedObjectNameComposite(final Composite parent, final int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		// gridLayout.marginBottom = 0;
		// gridLayout.marginTop = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		// gridLayout.marginRight = 0;
		// gridLayout.marginLeft = 0 ;
		gridLayout.marginWidth = 0;
		setLayout(gridLayout);

		addInlineEditor(ComponentHelperUtils.createDefaultEditor(MMXCorePackage.eINSTANCE.getNamedObject(), MMXCorePackage.Literals.NAMED_OBJECT__NAME));
	}

	@Override
	public void dispose() {

		object = null;

		super.dispose();
	}

	public String getObjectName() {
		if (object == null) {
			return null;
		}
		return object.getName();
	}

	@Override
	public void addInlineEditor(final IInlineEditor editor) {
		editors.add(editor);
		editor.setCommandHandler(commandHandler);
		Control control = editor.createControl(this);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		control.setLayoutData(gd);
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject value, final Collection<EObject> range) {

		if (isDisposed()) {
			return;
		}
		if (value instanceof NamedObject) {
			object = (NamedObject) value;
			this.setVisible(true);
		} else {
			object = null;
			this.setVisible(false);
		}
		for (final IInlineEditor editor : editors) {
			editor.setCommandHandler(commandHandler);
			editor.display(location, root, value, range);
		}

	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {

	}

	@Override
	public void displayValidationStatus(final IStatus status) {

		if (isDisposed()) {
			return;
		}
		for (final IInlineEditor editor : editors) {
			editor.processValidation(status);
		}
	}

	public void setEditorEnabled(final boolean enabled) {
		for (final IInlineEditor editor : editors) {
			editor.setEditorEnabled(enabled);
		}
	}

	public void setEditorLocked(final boolean enabled) {
		for (final IInlineEditor editor : editors) {
			editor.setEditorLocked(enabled);
		}
	}

	public NamedObject getObject() {
		return object;
	}
}