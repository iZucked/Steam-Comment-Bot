/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui.navigator;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManager;
import com.mmxlabs.jobmanager.eclipse.manager.IEclipseJobManagerListener;
import com.mmxlabs.jobmanager.jobs.EJobState;
import com.mmxlabs.jobmanager.jobs.IJobControl;
import com.mmxlabs.jobmanager.jobs.IJobControlListener;
import com.mmxlabs.jobmanager.jobs.IJobDescriptor;
import com.mmxlabs.jobmanager.manager.IJobManager;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionChangedListener;
import com.mmxlabs.scenario.service.ui.IScenarioServiceSelectionProvider;
import com.mmxlabs.scenario.service.ui.internal.Activator;

public class ScenarioServiceNavigator extends CommonNavigator {

	private static final Logger log = LoggerFactory.getLogger(ScenarioServiceNavigator.class);

	protected AdapterFactoryEditingDomain editingDomain;

	protected ComposedAdapterFactory adapterFactory = ScenarioServiceComposedAdapterFactory.getAdapterFactory();

	private final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker;

	private CommonViewer viewer = null;
	
	private IScenarioServiceSelectionChangedListener selectionChangedListener = new IScenarioServiceSelectionChangedListener() {
		@Override
		public void selected(IScenarioServiceSelectionProvider provider, Collection<ScenarioInstance> deselected) {
			if (viewer != null) {
				for (final ScenarioInstance instance : deselected) viewer.refresh(instance, true);
			}
		}
		
		@Override
		public void deselected(IScenarioServiceSelectionProvider provider, Collection<ScenarioInstance> deselected) {
			if (viewer != null) {
				for (final ScenarioInstance instance : deselected) viewer.refresh(instance, true);
			}
		}
	};

	private IEclipseJobManagerListener jobManagerListener = new IEclipseJobManagerListener() {
		
		private IJobControlListener jobControlListener = new IJobControlListener() {
			@Override
			public boolean jobStateChanged(IJobControl job, EJobState oldState, EJobState newState) {
				tryRefresh();
				return true;
			}
			
			@Override
			public boolean jobProgressUpdated(IJobControl job, int progressDelta) {
				tryRefresh();
				return true;
			}
		};

		@Override
		public void jobSelected(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl jobControl, Object resource) {
			
		}
		
		@Override
		public void jobRemoved(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource) {
			control.removeListener(jobControlListener);
			tryRefresh();
		}
		
		@Override
		public void jobManagerRemoved(IEclipseJobManager eclipseJobManager, IJobManager jobManager) {
			
		}
		
		@Override
		public void jobManagerAdded(IEclipseJobManager eclipseJobManager, IJobManager jobManager) {
			
		}
		
		@Override
		public void jobDeselected(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl jobControl, Object resource) {
			
		}
		
		@Override
		public void jobAdded(IEclipseJobManager eclipseJobManager, IJobDescriptor job, IJobControl control, Object resource) {
			control.addListener(jobControlListener);
			tryRefresh();
		}
	};
	
	public ScenarioServiceNavigator() {
		super();

		tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(Activator.getDefault().getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();
		
		Activator.getDefault().getScenarioServiceSelectionProvider().addSelectionChangedListener(selectionChangedListener);
		Activator.getDefault().getEclipseJobManager().addEclipseJobManagerListener(jobManagerListener);
	}

	@Override
	public void dispose() {
		tracker.close();
		Activator.getDefault().getScenarioServiceSelectionProvider().removeSelectionChangedListener(selectionChangedListener);
		Activator.getDefault().getEclipseJobManager().removeEclipseJobManagerListener(jobManagerListener);
		super.dispose();
	}
	
	protected void tryRefresh() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				final CommonViewer viewer1 = viewer;
				if (viewer1 != null) viewer1.refresh();
			}
		
		});
	}

	@Override
	protected CommonViewer createCommonViewer(final Composite aParent) {
		final CommonViewer viewer = super.createCommonViewer(aParent);
		
		this.viewer = viewer;
		
		viewer.getTree().setHeaderVisible(true);
		
		TreeColumn labelColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
		labelColumn.setText("Name");
		labelColumn.setWidth(300);
		
		TreeColumn checkColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
		checkColumn.setText("Show");
		
		TreeColumn progressColumn = new TreeColumn(viewer.getTree(), SWT.NONE);
		progressColumn.setText("Opt");
		
		checkColumn.pack();
		progressColumn.pack();
		
		final Tree tree = viewer.getTree();
		tree.addMouseListener(new MouseAdapter() {

			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseDown(MouseEvent e) {
				for(TreeItem item : tree.getSelection()) {
					if(item.getImage() != null) {
						if((e.x > item.getImageBounds(1).x) && (e.x < (item.getImageBounds(1).x + item.getImage().getBounds().width))) {
							if((e.y > item.getImageBounds(1).y) && (e.y < (item.getImageBounds(1).y + item.getImage().getBounds().height))) {
								final Object data = item.getData();
								if (data instanceof ScenarioInstance) {
									final ScenarioInstance instance = (ScenarioInstance) data;
									Activator.getDefault().getScenarioServiceSelectionProvider().toggleSelection(instance);
								}
							}
						}
					}
				}
			}
			
		});
		
		return viewer;
	}

	@Override
	protected Object getInitialInput() {

		final ScenarioModel scenarioModel = tracker.getService().getScenarioModel();

		return scenarioModel;
	}

	/**
	 * This accesses a cached version of the property sheet. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IPropertySheetPage getPropertySheetPage() {
		ExtendedPropertySheetPage propertySheetPage = new ExtendedPropertySheetPage(editingDomain) {
			@Override
			public void setSelectionToViewer(final List<?> selection) {
				// ScenarioServiceNavigator.this.setSelectionToViewer(selection);
				ScenarioServiceNavigator.this.setFocus();
			}

			@Override
			public void setActionBars(final IActionBars actionBars) {
				super.setActionBars(actionBars);
				// getActionBarContributor().shareGlobalActions(this, actionBars);
			}
		};
		propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));

		return propertySheetPage;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class key) {
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		} else {
			return super.getAdapter(key);
		}
	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		ICommandService commandService = (ICommandService) getSite().getService(ICommandService.class);

		Command command = commandService.getCommand("com.mmxlabs.scenario.service.ui.open");
		if (command.isEnabled()) {
			IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
			try {
				handlerService.executeCommand("com.mmxlabs.scenario.service.ui.open", null);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} else {
			super.handleDoubleClick(anEvent);
		}

	}
}
