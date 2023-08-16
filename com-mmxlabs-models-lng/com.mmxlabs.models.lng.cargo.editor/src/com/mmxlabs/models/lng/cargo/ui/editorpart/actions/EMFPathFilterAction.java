package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.filters.EMFPathFilter;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.util.emfpath.IEMFPath;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class EMFPathFilterAction extends DefaultMenuCreatorAction {
	/**
	 * 
	 */
	private final EObject sourceObject;
	private final EStructuralFeature sourceFeature;
	private final IEMFPath filterPath;
	private final StructuredViewerFilterManager filterManager;

	/**
	 * 
	 * An action which updates the filter on the trades wiring table and refreshes the table.
	 * 
	 * @param label
	 *            The label to associate with this action (the feature from the cargo row it represents).
	 * @param sourceObject
	 *            The source object in the EMF model which holds the list of possible values for the filter.
	 * @param sourceFeature
	 *            The EMF feature of the source object where the list of possible values resides.
	 * @param filterPath
	 *            The path within a cargo row object of the field which the table is being filtered on.
	 */
	public EMFPathFilterAction(StructuredViewerFilterManager filterManager, final String label, final EObject sourceObject, final EStructuralFeature sourceFeature, final IEMFPath filterPath) {
		super(label);
		this.sourceObject = sourceObject;
		this.sourceFeature = sourceFeature;
		this.filterPath = filterPath;
		this.filterManager = filterManager;
	}

	/**
	 * Add actions to the submenu associated with this action.
	 */
	@Override
	protected void populate(final Menu menu) {
		List<NamedObject> copiedValues = new ArrayList(getValues());
		Collections.sort(copiedValues, Comparator.comparing(NamedObject::getName));

		final Action clearAction = new Action("Clear " + this.getText() + " filters") {
			@Override
			public void run() {
				getFilterManager().removeFilter(EMFPathFilterAction.this.getText());
			}
		};
		if (getFilterManager().filterExists(this.getText())) {
			addActionToMenu(clearAction, menu);
		}

		if (copiedValues.size() > 15) {
			int counter = 0;
			String firstEntry = "";
			String lastEntry = "";
			final List<Action> collection = new LinkedList<>();
			for (final NamedObject value : copiedValues) {
				if (counter == 0) {
					firstEntry = value.getName();
				} else {
					lastEntry = value.getName();
				}
				final Action action = createAction(value);
				collection.add(action);
				++counter;

				if (counter == 15) {
					addListOfActionsToMenu(menu, firstEntry, lastEntry, collection);
					counter = 0;
				}
			}
			if (counter > 0) {
				addListOfActionsToMenu(menu, firstEntry, lastEntry, collection);
			}
		} else {
			// Show the list of labels (one for each item in the source object feature)
			for (final NamedObject value : copiedValues) {

				final Action action = createAction(value);
				addActionToMenu(action, menu);
			}
		}

	}

	protected Action createAction(final NamedObject value) {
		ViewerFilter filter = new EMFPathFilter(filterPath, value);
		if (getFilterManager().containsFilter(EMFPathFilterAction.this.getText(), filter)) {
			final Action action = new Action(value.getName(), IAction.AS_CHECK_BOX) {
				public void run() {
					getFilterManager().removeFilter(EMFPathFilterAction.this.getText(), filter, true);
				}
			};
			action.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
			return action;
		} else {
			return new Action(value.getName(), IAction.AS_CHECK_BOX) {
				@Override
				public void runWithEvent(Event event) {
					if ((event.stateMask & SWT.CTRL) != 0) {
						getFilterManager().addFilterAsUnion(EMFPathFilterAction.this.getText(), filter);
					} else {
						getFilterManager().addFilter(EMFPathFilterAction.this.getText(), new EMFPathFilter(filterPath, value));
					}
				}
			};
		}
	}

	private void addListOfActionsToMenu(final Menu menu, final String firstEntry, final String lastEntry, final List<Action> collection) {
		final String title = String.format("%s ... %s", firstEntry, lastEntry);

		final DefaultMenuCreatorAction dmca = new DefaultMenuCreatorAction(title) {
			final List<Action> local = new LinkedList<>(collection);

			@Override
			protected void populate(final Menu menu) {
				for (final Action a : this.local) {
					addActionToMenu(a, menu);
				}
			}
		};
		addActionToMenu(dmca, menu);
		collection.clear();
	}

	protected Collection<NamedObject> getValues() {
		// Get the labels to populate the menu from the source object in the EMF model
		final EList<NamedObject> values = (EList<NamedObject>) getSourceObject().eGet(getSourceFeature());
		final List<NamedObject> copiedValues = new LinkedList<>(values);
		Collections.sort(copiedValues, (a, b) -> {
			if (a != null && a.getName() != null && b != null) {
				return a.getName().compareTo(b.getName());
			}
			return 0;
		});
		return copiedValues;
	}

	EStructuralFeature getSourceFeature() {
		return sourceFeature;
	}

	protected EObject getSourceObject() {
		return sourceObject;
	}

	StructuredViewerFilterManager getFilterManager() {
		return filterManager;
	}

}