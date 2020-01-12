/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityPart;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow;
import com.mmxlabs.rcp.common.SelectionHelper;

/**
 * A service to keep track of the current change sets from the last active of eother the Action set view or the Change set view.
 * 
 * @author Simon Goodall
 *
 */
public class ScenarioChangeSetService {

	private EPartService partService;

	private ESelectionService selectionService;

	private boolean diffToBase;
	private ChangeSetTableRoot changeSetRoot;
	private ChangeSetTableGroup changeSet;
	private Collection<ChangeSetTableRow> changeSetRows;

	private final ConcurrentLinkedQueue<IScenarioChangeSetListener> listeners = new ConcurrentLinkedQueue<>();

	public boolean isDiffToBase() {
		return diffToBase;
	}

	public @Nullable ChangeSetTableGroup getChangeSet() {
		return changeSet;
	}

	@Nullable
	public ChangeSetTableRoot getChangeSetRoot() {
		return changeSetRoot;
	}

	@Nullable
	public Collection<ChangeSetTableRow> getSelectedChangeSetRows() {
		return changeSetRows;
	}

	// Keeping this reference as we probably need to extract out the diff to base value from here
	private ChangeSetView lastChangeSetViewObject = null;
	private Object lastChangeSetViewPart = null;

	private final ISelectionListener changeSetListener = new ISelectionListener() {

		@Override
		public void selectionChanged(final MPart part, final Object selectionObject) {

			if (!part.getElementId().contains("com.mmxlabs.lingo.reports.views.changeset.")) {
				return;
			}
			Object partObject = part.getObject();
			if (partObject instanceof CompatibilityPart) {
				partObject = ((CompatibilityPart) partObject).getPart();
			}
			if (partObject == lastChangeSetViewPart) {

				final ISelection selection = SelectionHelper.adaptSelection(selectionObject);
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

					// Where to get this from?
					final boolean diffToBase = false;
					ChangeSetTableRoot root = null;
					ChangeSetTableGroup set = null;
					final List<ChangeSetTableRow> rows = new LinkedList<>();

					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						Object o = itr.next();
						if (o instanceof ChangeSetTableRow) {
							final ChangeSetTableRow r = (ChangeSetTableRow) o;
							rows.add(r);
							o = r.eContainer();
						}
						if (o instanceof ChangeSetTableGroup) {
							final ChangeSetTableGroup s = (ChangeSetTableGroup) o;
							set = s;
							o = s.eContainer();
						}
						if (o instanceof ChangeSetTableRoot) {
							final ChangeSetTableRoot r = (ChangeSetTableRoot) o;
							root = r;
						}
					}

					fireListeners(root, set, rows, diffToBase);
					return;
				}

				fireListeners(null, null, null, false);
			}
		}
	};

	private final IPartListener partlistener = new IPartListener() {

		@Override
		public void partActivated(final MPart part) {
			Object partObject = part.getObject();
			if (partObject instanceof CompatibilityPart) {
				partObject = ((CompatibilityPart) partObject).getPart();
			}
			if (partObject != lastChangeSetViewPart) {
				if (part.getElementId().contains("com.mmxlabs.lingo.reports.views.changeset.")) {
					lastChangeSetViewPart = partObject;
					lastChangeSetViewObject = (ChangeSetView) partObject;
					changeSetListener.selectionChanged(part, selectionService.getSelection(part.getElementId()));
				}
			}
		}

		@Override
		public void partBroughtToTop(final MPart part) {

		}

		@Override
		public void partDeactivated(final MPart part) {
			if (lastChangeSetViewPart == part) {
				// Clear selection
				// fireListeners(null, null, null, false);
			}
		}

		@Override
		public void partHidden(final MPart part) {
			if (lastChangeSetViewPart == part) {
				// Clear selection
				// fireListeners(null, null, null, false);
			}
		}

		@Override
		public void partVisible(final MPart part) {

			Object partObject = part.getObject();
			if (partObject instanceof CompatibilityPart) {
				partObject = ((CompatibilityPart) partObject).getPart();
			}
			if (partObject != lastChangeSetViewPart) {
				if (part.getElementId().contains("com.mmxlabs.lingo.reports.views.changeset.")) {
					lastChangeSetViewPart = partObject;
					lastChangeSetViewObject = (ChangeSetView) partObject;
					changeSetListener.selectionChanged(part, selectionService.getSelection(part.getElementId()));
				}
			}
		}
	};

	public void start() {

		// FIXME: Need to ensure service is not available before the platform!
		partService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(EPartService.class);
		selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ESelectionService.class);

		partService.addPartListener(partlistener);
		// Add selection listener to only these views
		selectionService.addPostSelectionListener(changeSetListener);
	}

	public void stop() {
		partService.removePartListener(partlistener);
		selectionService.removePostSelectionListener(changeSetListener);

		partService = null;
		selectionService = null;
	}

	public void triggerListener(final IScenarioChangeSetListener l) {
		l.changeSetChanged(changeSetRoot, changeSet, changeSetRows, diffToBase);
	}

	public void addListener(final IScenarioChangeSetListener l) {
		listeners.add(l);
	}

	public void removeListener(final IScenarioChangeSetListener l) {
		listeners.remove(l);
	}

	private synchronized void fireListeners(@Nullable final ChangeSetTableRoot changeSetRoot, @Nullable final ChangeSetTableGroup changeSet,
			@Nullable final Collection<ChangeSetTableRow> changeSetRows, final boolean diffToBase) {
		this.changeSetRoot = changeSetRoot;
		this.changeSet = changeSet;
		this.changeSetRows = changeSetRows;
		this.diffToBase = diffToBase;
		for (final IScenarioChangeSetListener l : listeners) {
			l.changeSetChanged(changeSetRoot, changeSet, changeSetRows, diffToBase);
		}
	}
}
