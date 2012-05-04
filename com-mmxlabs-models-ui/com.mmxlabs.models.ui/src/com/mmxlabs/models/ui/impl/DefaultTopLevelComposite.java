/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.ui.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditorWrapper;
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
	protected ICommandHandler commandHandler;
	protected IDisplayCompositeLayoutProvider layoutProvider = new DefaultDisplayCompositeLayoutProvider();
	protected IInlineEditorWrapper editorWrapper = IInlineEditorWrapper.IDENTITY;

	public DefaultTopLevelComposite(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	public void display(final MMXRootObject root, final EObject object, final Collection<EObject> range) {
		final EClass eClass = object.eClass();
		final Group g = new Group(this, SWT.NONE);
		g.setText(EditorUtils.unmangle(eClass.getName()));
		g.setLayout(new FillLayout());
		g.setLayoutData(layoutProvider.createTopLayoutData(root, object, object));
		topLevel = Activator.getDefault().getDisplayCompositeFactoryRegistry().
				getDisplayCompositeFactory(eClass).createSublevelComposite(g, eClass);
		topLevel.setCommandHandler(commandHandler);
		topLevel.setEditorWrapper(editorWrapper);
		for (final EReference ref : eClass.getEAllReferences()) {
			if (shouldDisplay(ref)) {
				final EObject value = (EObject) object.eGet(ref);
				if (value != null) {
					final Group g2 = new Group(this, SWT.NONE);
					g2.setText(EditorUtils.unmangle(ref.getName()));
					g2.setLayout(new FillLayout());
					g2.setLayoutData(layoutProvider.createTopLayoutData(root, object, value));
					
					final IDisplayComposite sub = Activator.getDefault().getDisplayCompositeFactoryRegistry().
							getDisplayCompositeFactory(value.eClass()).createSublevelComposite(g2, value.eClass());
					
					sub.setCommandHandler(commandHandler);
					sub.setEditorWrapper(editorWrapper);
					childReferences.add(ref);
					childComposites.add(sub);
				}
			}
		}

		topLevel.display(root, object, range);
		final Iterator<EReference> refs = childReferences.iterator();
		final Iterator<IDisplayComposite> children = childComposites
				.iterator();

		while (refs.hasNext()) {
			children.next().display(root, (EObject) object.eGet(refs.next()), range);
		}
		
		setLayout(layoutProvider.createTopLevelLayout(root, object, childComposites.size() + 1));
	}

	protected boolean shouldDisplay(EReference ref) {
		return ref.isContainment() && !ref.isMany();
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
	public void displayValidationStatus(IStatus status) {
		topLevel.displayValidationStatus(status);
		for (final IDisplayComposite child : childComposites) {
			child.displayValidationStatus(status);
		}
	}

	@Override
	public void setEditorWrapper(IInlineEditorWrapper wrapper) {
		this.editorWrapper = wrapper;
		if (topLevel != null) topLevel.setEditorWrapper(editorWrapper);
		for (final IDisplayComposite child : childComposites) {
			child.setEditorWrapper(editorWrapper);
		}
	}
	
	public ICommandHandler getCommandHandler() {
		return commandHandler;
	}
}
