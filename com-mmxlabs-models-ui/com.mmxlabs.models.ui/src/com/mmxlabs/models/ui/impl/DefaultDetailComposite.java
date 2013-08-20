/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;

/**
 * The default detail composite implementation; does not do anything about having child composites.
 * 
 * @author hinton
 * 
 */
public class DefaultDetailComposite extends Composite implements IInlineEditorContainer, IDisplayComposite {

	private ICommandHandler commandHandler;
	private EClass displayedClass;
	protected IDisplayCompositeLayoutProvider layoutProvider = createLayoutProvider();
	private IInlineEditorWrapper wrapper = IInlineEditorWrapper.IDENTITY;
	/**
	 * @since 6.0
	 */
	protected final FormToolkit toolkit;

	/**
	 * @since 6.0
	 */
	public DefaultDetailComposite(final Composite parent, final int style, final FormToolkit toolkit) {
		super(parent, style);
		this.toolkit = toolkit;
		toolkit.adapt(this);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {
		return new DefaultDisplayCompositeLayoutProvider();
	}

	protected final LinkedList<IInlineEditor> editors = new LinkedList<IInlineEditor>();
	
	@Override
	public IInlineEditor addInlineEditor(IInlineEditor editor) {
		
		editor = wrapper.wrap(editor);
		if (editor != null) {
			editor.setCommandHandler(commandHandler);
			editors.add(editor);
		}
		
		return editor;
	}

	/**
	 * @since 6.0
	 */
	public void createControls(final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc) {
		
		toolkit.adapt(this);
		
		for (final IInlineEditor editor : editors) {
			final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(this, SWT.NONE) : null;
			editor.setLabel(label);
			final Control control = editor.createControl(this, dbc, toolkit);
			control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
			control.setData(LABEL_CONTROL_KEY, label);
			control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			if (label != null) {
				toolkit.adapt(label, true, false);
				label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
			}
		}
	}

	/**
	 * Display the given EObject in this container.
	 * 
	 * Recreates the controls if the object's eClass is different to what we had before.
	 * 
	 * @param object
	 * @since 6.0
	 */
	@Override
	public void display(final IScenarioEditingLocation location, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		final EClass eClass = object.eClass();
		setLayout(layoutProvider.createDetailLayout(root, object));
		if (eClass != displayedClass) {
			clear();
			initialize(eClass);
			createControls(root, object, dbc);
		}
		for (final IInlineEditor editor : editors) {
			editor.display(location, root, object, range);
		}
	}

	private void clear() {
		editors.clear();
		for (final Control c : getChildren())
			c.dispose();
	}

	private void initialize(final EClass eClass) {
		this.displayedClass = eClass;
		final List<IComponentHelper> helpers = Activator.getDefault().getComponentHelperRegistry().getComponentHelpers(displayedClass);
		for (final IComponentHelper helper : helpers) {
			helper.addEditorsToComposite(this);
		}
	}

	@Override
	public Composite getComposite() {
		return this;
	}

	@Override
	public void setCommandHandler(final ICommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void displayValidationStatus(final IStatus status) {
		for (final IInlineEditor editor : editors)
			editor.processValidation(status);
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		this.wrapper = wrapper;
	}

	public void setLayoutProvider(final IDisplayCompositeLayoutProvider layoutProvider) {
		this.layoutProvider = layoutProvider;
	}

	/**
	 * @since 6.1
	 */
	 	protected static Control createLabelledEditorControl(MMXRootObject root, EObject object, Composite c, IInlineEditor editor, EMFDataBindingContext dbc, IDisplayCompositeLayoutProvider layoutProvider, FormToolkit toolkit) {
		final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(c, SWT.NONE) : null;
		if (label != null) label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		editor.setLabel(label);
		final Control control = editor.createControl(c, dbc, toolkit);
		control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
		control.setData(LABEL_CONTROL_KEY, label);
		control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		if (label != null) label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
		return control;
	}

	/** @deprecated */
	protected Control createLabelledEditorControl(MMXRootObject root, EObject object, Composite c, IInlineEditor editor, EMFDataBindingContext dbc) {
		return createLabelledEditorControl(root, object, c, editor, dbc, layoutProvider, toolkit);
	}
}
