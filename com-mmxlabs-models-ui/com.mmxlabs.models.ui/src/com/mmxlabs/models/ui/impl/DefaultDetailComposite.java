/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;

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
	 */
	protected final FormToolkit toolkit;

	/**
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
	protected EObject object;

	/**
	 */
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
	 * @param dialogContext
	 */
	public void createControls(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final EMFDataBindingContext dbc) {

		this.object = object;
		toolkit.adapt(this);
		setDefaultHelpContext(object);

		for (final IInlineEditor editor : editors) {

			final Label label = layoutProvider.showLabelFor(root, object, editor) ? new Label(this, SWT.NONE) : null;
			editor.setLabel(label);
			final Control control = editor.createControl(this, dbc, toolkit);
			dialogContext.registerEditorControl(object, editor.getFeature(), control);

			control.setLayoutData(layoutProvider.createEditorLayoutData(root, object, editor, control));
			control.setData(LABEL_CONTROL_KEY, label);
			control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			if (label != null) {
				toolkit.adapt(label, true, false);
				label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				label.setLayoutData(layoutProvider.createLabelLayoutData(root, object, editor, control, label));
				dialogContext.registerEditorControl(object, editor.getFeature(), label);
			}
		}
	}

	protected void setDefaultHelpContext(final EObject object) {
		final EClass eClass = object.eClass();
		final String helpContextId = String.format("com.mmxlabs.lingo.doc.DataModel_%s_%s", eClass.getEPackage().getNsPrefix().replaceAll("\\.", "_"), eClass.getName());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, helpContextId);
	}

	/**
	 * Display the given EObject in this container.
	 * 
	 * Recreates the controls if the object's eClass is different to what we had before.
	 * 
	 * @param object
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		final EClass eClass = object.eClass();
		this.object = object;
		setLayout(layoutProvider.createDetailLayout(root, object));
		if (eClass != displayedClass) {
			clear();
			initialize(eClass);
			createControls(dialogContext, root, object, dbc);
		}
		for (final IInlineEditor editor : editors) {
			editor.display(dialogContext, root, object, range);
		}
		checkVisibility(dialogContext);
	}

	private void clear() {
		editors.clear();
		for (final Control c : getChildren()) {
			c.dispose();
		}
		// dialogContext -> removeControl
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
	 * Check the controls state and update visibility accordingly.
	 */
	@Override
	public boolean checkVisibility(final IDialogEditingContext dialogContext) {

		boolean changed = false;
		for (final IInlineEditor editor : editors) {
			final EStructuralFeature feature = editor.getFeature();
			final boolean visible = dialogContext.getDialogController().getEditorVisibility(object, feature);
			final List<Control> controls = dialogContext.getEditorControls(object, feature);
			if (controls != null) {
				for (final Control control : controls) {
					if (control.isVisible() != visible) {
						changed = true;
					}
					// Always do state change as we can sometimes be inconsistent.
					setControlVisibility(control, visible);

					final Object layoutData = control.getLayoutData();
					if (layoutData instanceof GridData) {
						final GridData gridData = (GridData) layoutData;
						if (gridData.exclude != !visible) {
							changed = true;
						}
						// Always do state change as we can sometimes be inconsistent.
						gridData.exclude = !visible;
					}

				}
			}
		}
		if (changed) {
			pack();
		}

		return changed;
	}

	private void setControlVisibility(final Control c, final boolean v) {
		c.setVisible(v);
		if (c instanceof Composite) {
			final Composite composite = (Composite) c;
			for (final Control c2 : composite.getChildren()) {
				setControlVisibility(c2, v);
			}
		}
	}

}
