/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.modelbased;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.modelbased.annotations.LingoEquivalents;

public class DefaultModelBasedSelectionMapper<M> implements IModelBasedSelectionMapper<M> {

	private final Class<M> modelClass;

	private Field equivalentsField;

	private Viewer viewer;

	public DefaultModelBasedSelectionMapper(final Class<M> modelClass, Viewer viewer) {
		this.modelClass = modelClass;
		this.viewer = viewer;
		for (final Field f : modelClass.getFields()) {
			if (f.getAnnotation(LingoEquivalents.class) != null) {
				this.equivalentsField = f;
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.lingo.reports.modelbased.IModelBasedSelectionMapper#adaptSelectionToExternal(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public ISelection adaptSelectionToExternal(final ISelection selection) {
		if (selection instanceof IStructuredSelection && equivalentsField != null) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			final Iterator<?> itr = ss.iterator();
			final Set<Object> newObjects = new HashSet<>();
			while (itr.hasNext()) {
				final Object o = itr.next();
				if (modelClass.isInstance(o)) {
					try {
						final Set<?> equivalents = (Set<?>) equivalentsField.get(o);
						if (equivalents != null) {
							newObjects.addAll(equivalents);
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
			return new StructuredSelection(new ArrayList<>(newObjects));
		}
		return selection;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.lingo.reports.modelbased.IModelBasedSelectionMapper#adaptSelectionToInternal(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public ISelection adaptSelectionToInternal(final ISelection selection) {
		if (selection instanceof IStructuredSelection && equivalentsField != null) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			final Iterator<?> itr = ss.iterator();
			final Set<Object> newObjects = new HashSet<>();
			while (itr.hasNext()) {
				final Object o = itr.next();

				// TODO: Make this more efficient.
				for (final Object m : (Collection<?>) viewer.getInput()) {
					if (modelClass.isInstance(m)) {
						if (o == m) {
							newObjects.add(m);
						} else {
							try {
								final Set<?> equivalents = (Set<?>) equivalentsField.get(m);
								if (equivalents != null && equivalents.contains(o)) {
									newObjects.add(m);
								}
							} catch (final Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			return new StructuredSelection(new ArrayList<>(newObjects));
		}
		return selection;
	}
}
