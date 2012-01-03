/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.containers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


import com.mmxlabs.shiplingo.ui.detailview.base.AbstractDetailComposite;
import com.mmxlabs.shiplingo.ui.detailview.base.IInlineEditorWrapper;
import com.mmxlabs.shiplingo.ui.detailview.base.IValueProviderProvider;
import com.mmxlabs.shiplingo.ui.detailview.editors.ICommandProcessor;

/**
 * A class which holds and tracks detail composite containers, placing them in
 * another composite and hiding and showing them as appropriate, rather than
 * constructing/disposing them.
 * 
 * @author hinton
 * 
 */
public class DetailCompositeContainer {
	private final Map<EClass, AbstractDetailComposite> classToComposite = new HashMap<EClass, AbstractDetailComposite>();
	private final IValueProviderProvider valueProviderProvider;
	private final EditingDomain editingDomain;
	private final ICommandProcessor processor;
	private final IInlineEditorWrapper wrapper;
	private boolean lockedForEditing = false;

	public DetailCompositeContainer(
			IValueProviderProvider valueProviderProvider,
			EditingDomain editingDomain, ICommandProcessor processor,
			IInlineEditorWrapper wrapper) {
		super();
		this.valueProviderProvider = valueProviderProvider;
		this.editingDomain = editingDomain;
		this.processor = processor;
		this.wrapper = wrapper;
	}

	public DetailCompositeContainer(
			IValueProviderProvider valueProviderProvider,
			EditingDomain editingDomain, ICommandProcessor processor) {
		this(valueProviderProvider, editingDomain, processor,
				IInlineEditorWrapper.IDENTITY);
	}

	/**
	 * Construct or retrieve a detail view
	 * 
	 * @param eClass
	 * @param control2
	 * @return
	 */
	public AbstractDetailComposite getDetailView(final EClass eClass,
			final Composite control2) {
		if (classToComposite.containsKey(eClass)) {
			return classToComposite.get(eClass);
		} else {
			try {
				final Class c = Class
						.forName("com.mmxlabs.shiplingo.ui.detailview.generated."
								+ eClass.getName().substring(0, 1)
										.toUpperCase()
								+ eClass.getName().substring(1) + "Composite");
				final AbstractDetailComposite adc = (AbstractDetailComposite) c
						.getConstructor(Composite.class, Integer.TYPE)
						.newInstance(control2, SWT.NONE);

				adc.init(valueProviderProvider, editingDomain, processor,
						wrapper);

				adc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				
				adc.setLockedForEditing(lockedForEditing);

				classToComposite.put(eClass, adc);
				return adc;
			} catch (final Exception e) {
			}
		}
		return null;
	}
	
	public void setLockedForEditing(final boolean locked) {
		lockedForEditing = locked;
		for (final AbstractDetailComposite composite : classToComposite.values()) {
			composite.setLockedForEditing(locked);
		}
	}
}
