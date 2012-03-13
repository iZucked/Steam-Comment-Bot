/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;

/**
 * The default detail composite implementation; does not do anything about
 * having child composites.
 * 
 * @author hinton
 * 
 */
public class DefaultDetailComposite extends Composite implements
		IInlineEditorContainer, IDisplayComposite {
	private ICommandHandler commandHandler;
	private EClass displayedClass;
	private IDisplayCompositeLayoutProvider layoutProvider = new DefaultDisplayCompositeLayoutProvider();
	private IInlineEditorWrapper wrapper = IInlineEditorWrapper.IDENTITY;

	public DefaultDetailComposite(final Composite parent, final int style) {
		super(parent, style);
	}

	private final LinkedList<IInlineEditor> editors = new LinkedList<IInlineEditor>();

	@Override
	public void addInlineEditor(IInlineEditor editor) {
		editor = wrapper.wrap(editor);
		if (editor != null) {
			editor.setCommandHandler(commandHandler);
			editors.add(editor);
		}
	}

	public void createControls(MMXRootObject root, EObject object) {
		for (final IInlineEditor editor : editors) {
			final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(this, SWT.NONE):null;
			editor.setLabel(label);
			final Control control = editor.createControl(this);
			control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
			if (label != null) {
				label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
			}			
		}
	}

	/**
	 * Display the given EObject in this container.
	 * 
	 * Recreates the controls if the object's eClass is different to what we had
	 * before.
	 * 
	 * @param object
	 */
	@Override
	public void display(final MMXRootObject root, final EObject object) {
		final EClass eClass = object.eClass();
		setLayout(layoutProvider.createDetailLayout(root, object));
		if (eClass != displayedClass) {
			clear();
			initialize(eClass);
			createControls(root, object);
		}
		for (final IInlineEditor editor : editors) {
			editor.display(root, object);
		}
	}

	private void clear() {
		editors.clear();
		for (final Control c : getChildren())
			c.dispose();
	}

	private void initialize(final EClass eClass) {
		this.displayedClass = eClass;
		final IComponentHelper helper = Activator.getDefault()
				.getComponentHelperRegistry()
				.getComponentHelper(displayedClass);
		helper.addEditorsToComposite(this);
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void setCommandHandler(ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public Collection<EObject> getEditingRange(MMXRootObject root, EObject value) {
		return Collections.singleton(value);
	}

	@Override
	public void displayValidationStatus(IStatus status) {
		for (final IInlineEditor editor : editors) editor.processValidation(status);
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		this.wrapper = wrapper;
	}
}
