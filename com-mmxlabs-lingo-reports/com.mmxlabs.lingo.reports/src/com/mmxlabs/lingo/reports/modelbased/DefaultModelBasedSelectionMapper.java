package com.mmxlabs.lingo.reports.modelbased;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.modelbased.annotations.LingoEquivalents;
import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.IScenarioComparisonServiceListener;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.TransformedSelectedDataProvider;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerSortingSupport;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.ui.ScenarioResult;
import com.mmxlabs.scenario.service.ui.navigator.ScenarioServiceNavigator;

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
