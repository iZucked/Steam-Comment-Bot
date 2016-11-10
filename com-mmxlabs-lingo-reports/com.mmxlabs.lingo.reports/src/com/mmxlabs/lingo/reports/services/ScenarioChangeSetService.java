package com.mmxlabs.lingo.reports.services;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.lingo.reports.views.changeset.ChangeSetView;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.rcp.common.SelectionHelper;

/**
 * A service to keep track of the current change sets from the last active of eother the Action set view or the Change set view.
 * 
 * @author Simon Goodall
 *
 */
public class ScenarioChangeSetService {

	private static final @NonNull String actionSetViewId = "com.mmxlabs.lingo.reports.views.changeset.ActionSetView";
	private static final @NonNull String changeSetViewId = "com.mmxlabs.lingo.reports.views.changeset.ChangeSetView";

	private EPartService partService;

	private ESelectionService selectionService;

	private boolean diffToBase;
	private ChangeSetRoot changeSetRoot;
	private ChangeSet changeSet;
	private ChangeSetRow changeSetRow;

	private final ConcurrentLinkedQueue<IScenarioChangeSetListener> listeners = new ConcurrentLinkedQueue<>();

	public boolean isDiffToBase() {
		return diffToBase;
	}

	public @Nullable ChangeSet getChangeSet() {
		return changeSet;
	}

	@Nullable
	public ChangeSetRoot getChangeSetRoot() {
		return changeSetRoot;
	}

	@Nullable
	public ChangeSetRow getSelectedChangeSetRows() {
		return changeSetRow;
	}

	// Keeping this reference as we probably need to extract out the diff to base value from here
	private ChangeSetView lastChangeSetViewObject = null;
	private MPart lastChangeSetViewPart = null;

	private final ISelectionListener changeSetListener = new ISelectionListener() {

		@Override
		public void selectionChanged(final MPart part, final Object selectionObject) {
			if (part == lastChangeSetViewPart) {

				final ISelection selection = SelectionHelper.adaptSelection(selectionObject);
				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

					// Where to get this from?
					final boolean diffToBase = false;
					ChangeSetRoot root = null;
					ChangeSet set = null;
					ChangeSetRow row = null;

					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						Object o = itr.next();
						if (o instanceof ChangeSetRow) {
							final ChangeSetRow r = (ChangeSetRow) o;
							row = r;
							o = r.eContainer();
						}
						if (o instanceof ChangeSet) {
							final ChangeSet s = (ChangeSet) o;
							set = s;
							o = s.eContainer();
						}
						if (o instanceof ChangeSetRoot) {
							final ChangeSetRoot r = (ChangeSetRoot) o;
							root = r;
						}
					}

					fireListeners(root, set, row, diffToBase);
					return;
				}

				fireListeners(null, null, null, false);
			}
		}
	};

	private final IPartListener partlistener = new IPartListener() {

		@Override
		public void partActivated(final MPart part) {
			if (part != lastChangeSetViewPart) {
				if (changeSetViewId.equals(part.getElementId()) || actionSetViewId.equals(part.getElementId())) {
					lastChangeSetViewPart = part;
					lastChangeSetViewObject = (ChangeSetView) part.getObject();
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
				fireListeners(null, null, null, false);
			}
		}

		@Override
		public void partHidden(final MPart part) {
			if (lastChangeSetViewPart == part) {
				// Clear selection
				fireListeners(null, null, null, false);
			}
		}

		@Override
		public void partVisible(final MPart part) {
			if (part != lastChangeSetViewPart) {
				if (changeSetViewId.equals(part.getElementId()) || actionSetViewId.equals(part.getElementId())) {
					lastChangeSetViewPart = part;
					lastChangeSetViewObject = (ChangeSetView) part.getObject();
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
		selectionService.addPostSelectionListener(changeSetViewId, changeSetListener);
		selectionService.addPostSelectionListener(actionSetViewId, changeSetListener);
	}

	public void stop() {
		partService.removePartListener(partlistener);
		selectionService.removePostSelectionListener(changeSetViewId, changeSetListener);
		selectionService.removePostSelectionListener(actionSetViewId, changeSetListener);

		partService = null;
		selectionService = null;
	}

	public void triggerListener(final IScenarioChangeSetListener l) {
		l.changeSetChanged(changeSetRoot, changeSet, changeSetRow, diffToBase);
	}

	public void addListener(final IScenarioChangeSetListener l) {
		listeners.add(l);
	}

	public void removeListener(final IScenarioChangeSetListener l) {
		listeners.remove(l);
	}

	private synchronized void fireListeners(@Nullable final ChangeSetRoot changeSetRoot, @Nullable final ChangeSet changeSet, @Nullable final ChangeSetRow changeSetRow, final boolean diffToBase) {
		this.changeSetRoot = changeSetRoot;
		this.changeSet = changeSet;
		this.changeSetRow = changeSetRow;
		this.diffToBase = diffToBase;
		for (final IScenarioChangeSetListener l : listeners) {
			l.changeSetChanged(changeSetRoot, changeSet, changeSetRow, diffToBase);
		}
	}
}
