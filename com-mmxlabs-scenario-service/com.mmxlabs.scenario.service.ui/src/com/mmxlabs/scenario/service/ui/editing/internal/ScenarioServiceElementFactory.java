/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.editing.internal;

import java.lang.ref.WeakReference;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import com.mmxlabs.scenario.service.IScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.ScenarioServiceEditorInput;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class ScenarioServiceElementFactory implements IElementFactory {

	/**
	 * Factory id. The workbench plug-in registers a factory by this name with the "org.eclipse.ui.elementFactories" extension point.
	 */
	public static final String ID_FACTORY = "com.mmxlabs.scenario.service.ui.editing.ScenarioServiceElementFactory";

	private static final String TAG_UUID = "uuid"; //$NON-NLS-1$

	/*
	 * (non-Javadoc) Method declared on IElementFactory.
	 */
	@Override
	public IAdaptable createElement(final IMemento memento) {
		final String uuid = memento.getString(TAG_UUID);
		if (uuid == null) {
			return null;
		}

		for (final WeakReference<IScenarioService> ref : Activator.getDefault().getScenarioServices().values()) {
			IScenarioService ss = ref.get();
			if (ss == null) {
				continue;
			}
			if (ss.exists(uuid)) {
				final TreeIterator<EObject> itr = ss.getServiceModel().eAllContents();
				while (itr.hasNext()) {
					final EObject obj = itr.next();
					if (obj instanceof ScenarioInstance) {
						if (uuid.equals(((ScenarioInstance) obj).getUuid())) {
							return new ScenarioServiceEditorInput((ScenarioInstance) obj);
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Returns the element factory id for this class.
	 * 
	 * @return the element factory id
	 */
	public static String getFactoryId() {
		return ID_FACTORY;
	}

	/**
	 * Saves the state of the given file editor input into the given memento.
	 * 
	 * @param memento
	 *            the storage area for element state
	 * @param input
	 *            the scenario service editor input
	 */
	public static void saveState(final IMemento memento, final ScenarioServiceEditorInput input) {
		final String uuid = input.getScenarioInstance().getUuid();
		memento.putString(TAG_UUID, uuid);
	}
}
