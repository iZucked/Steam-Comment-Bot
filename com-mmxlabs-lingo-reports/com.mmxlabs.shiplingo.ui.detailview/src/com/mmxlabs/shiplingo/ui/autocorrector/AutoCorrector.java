/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.autocorrector;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;

/**
 * Gadget for changing values when other values change; although this could just be a bunch of separate adapters that seems less tidy and more prone to recursive activation.
 * 
 * TODO hook it into the interface to let users know when a change has been made in this way.
 * 
 * @author Tom Hinton
 * 
 */
public class AutoCorrector extends EContentAdapter {
	private final List<ICorrector> correctors = new LinkedList<ICorrector>();
	private final EditingDomain editingDomain;
	private boolean correcting = false;

	public AutoCorrector(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	@Override
	public synchronized void notifyChanged(final Notification notification) {
		super.notifyChanged(notification);

		if (notification.isTouch() == false) {
			if (correcting) {
				return;
			}
			correcting = true;

			for (final ICorrector corrector : correctors) {
				final Pair<String, Command> correction = corrector.correct(notification, editingDomain);
				if (correction != null) {
					editingDomain.getCommandStack().execute(correction.getSecond());
				}
			}
			correcting = false;
		}
	}

	public void addCorrector(final ICorrector corrector) {
		correctors.add(corrector);
	}

	public interface ICorrector {
		public Pair<String, Command> correct(final Notification notification, final EditingDomain editingDomain);
	}
}
