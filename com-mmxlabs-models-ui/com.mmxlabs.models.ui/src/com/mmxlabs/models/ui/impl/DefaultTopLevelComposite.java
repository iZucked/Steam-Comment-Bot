/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.util.EditorUtils;

/**
 * The default composite used to display an EObject
 * 
 * @author hinton
 * 
 */
public class DefaultTopLevelComposite extends Composite implements IDisplayComposite {
	protected IDisplayComposite topLevel = null;
	protected List<EReference> childReferences = new LinkedList<EReference>();
	protected List<IDisplayComposite> childComposites = new LinkedList<IDisplayComposite>();
	/**
	 */
	protected List<EObject> childObjects = new LinkedList<EObject>();
	protected ICommandHandler commandHandler;
	protected IDisplayCompositeLayoutProvider layoutProvider = new DefaultDisplayCompositeLayoutProvider();
	protected IInlineEditorWrapper editorWrapper = IInlineEditorWrapper.IDENTITY;
	protected IDialogEditingContext dialogContext;
	protected FormToolkit toolkit;

	public DefaultTopLevelComposite(final Composite parent, final int style, final IDialogEditingContext dialogContext, final FormToolkit toolkit) {
		super(parent, style);
		// this.location = location;
		this.dialogContext = dialogContext;
		this.toolkit = toolkit;
		toolkit.adapt(this);
	}

	/**
	 */
	@Override
	public void display(final IDialogEditingContext dialogContext, final MMXRootObject root, final EObject object, final Collection<EObject> range, final EMFDataBindingContext dbc) {
		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		toolkit.adapt(g);
		g.setText(EditorUtils.unmangle(eClass.getName()));
		g.setLayout(new FillLayout());
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		g.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass, dialogContext, toolkit);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);

		createChildComposites(root, object, eClass, this);

		topLevel.display(dialogContext, root, object, range, dbc);
		final Iterator<IDisplayComposite> children = childComposites.iterator();
		final Iterator<EObject> childObjectsItr = childObjects.iterator();

		while (childObjectsItr.hasNext()) {
			children.next().display(dialogContext, root, childObjectsItr.next(), range, dbc);
		}

		setLayout(layoutProvider.createTopLevelLayout(root, object, childComposites.size() + 1));
	}

	/**
	 * SPECULATIVE DOCUMENTATION
	 *
	 * Creates a series of display components for all of the "reference" subcomponents of an object,
	 * for the purpose of a GUI editor.
	 *  
	 * Silently populates the following fields:
	 *		childReferences
	 *		childComposites
	 *		childObjects
	 * 
	 * @param root The root object for the entire data model
	 * @param object The object being edited
	 * @param eClass The object's class
	 * @param parent The GUI component to add sub-components to
	 */
	protected void createChildComposites(final MMXRootObject root, final EObject object, final EClass eClass, final Composite parent) {
		for (final EReference ref : eClass.getEAllReferences()) {
			if (shouldDisplay(ref)) {
				if (ref.isMany()) {
					final List values = (List) object.eGet(ref);
					for (final Object o : values) {
						if (o instanceof EObject) {
							createChildArea(root, object, parent, ref, (EObject) o);
						}
					}
				} else {
					final EObject value = (EObject) object.eGet(ref);

					createChildArea(root, object, parent, ref, value);
				}
			}
		}
	}


	/**
	 * SPECULATIVE DOCUMENTATION
	 * 
	 * Creates a "sub level composite" display component for a GUI editor on the object "object"
	 * which allows editing of one of its subcomponents. 
	 * 
	 * Silently modifies the following fields:
	 *		childReferences
	 *		childComposites
	 *		childObjects
	 * 
	 * @param root The root object for the entire data model
	 * @param object The parent data object being edited
	 * @param parent The display component to add relevant new visual sub-components to
	 * @param ref The EReference representing the field metadata
	 * @param value The object's sub-component value (which may be one of many, if the field is a list)
	 */
	protected void createChildArea(final MMXRootObject root, final EObject object, final Composite parent, final EReference ref, final EObject value) {
		if (value != null) {
			final Group g2 = new Group(parent, SWT.NONE);
			toolkit.adapt(g2);
			g2.setText(EditorUtils.unmangle(ref.getName()));
			g2.setLayout(new FillLayout());
			g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));

			final IDisplayComposite sub = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(value.eClass())
					.createSublevelComposite(g2, value.eClass(), dialogContext, toolkit);

			sub.setCommandHandler(commandHandler);
			sub.setEditorWrapper(editorWrapper);
			childReferences.add(ref);
			childComposites.add(sub);
			childObjects.add(value);
		}
	}

	protected boolean shouldDisplay(final EReference ref) {
		return ref.isContainment() && !ref.isMany();
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
		topLevel.displayValidationStatus(status);
		for (final IDisplayComposite child : childComposites) {
			child.displayValidationStatus(status);
		}
	}

	@Override
	public void setEditorWrapper(final IInlineEditorWrapper wrapper) {
		this.editorWrapper = wrapper;
		if (topLevel != null)
			topLevel.setEditorWrapper(editorWrapper);
		for (final IDisplayComposite child : childComposites) {
			child.setEditorWrapper(editorWrapper);
		}
	}

	public ICommandHandler getCommandHandler() {
		return commandHandler;
	}

	public void setLayoutProvider(final IDisplayCompositeLayoutProvider layoutProvider) {
		this.layoutProvider = layoutProvider;
	}

	@Override
	public boolean checkVisibility(final IDialogEditingContext context) {

		boolean changed = false;
		topLevel.checkVisibility(context);
		for (final IDisplayComposite child : childComposites) {
			changed |= child.checkVisibility(context);
		}
		return changed;
	}
}
