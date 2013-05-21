/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;

/**
 * @since 3.0
 */
public class BulkImportPage extends WizardPage {
	protected Control control = null;
	//private RadioSelectionGroup dataImportGroup;
	protected RadioSelectionGroup scenarioSelectionGroup;
	protected RadioSelectionGroup csvSelectionGroup;
	protected CheckboxTreeViewer scenarioTreeViewer;
	protected FileFieldEditor importFileEditor;
	final protected ScenarioInstance currentScenario;
	
	public final static int CHOICE_COMMODITY_INDICES = 0;
	public final static int CHOICE_CARGOES = CHOICE_COMMODITY_INDICES + 1;
	
	public final static int CHOICE_COMMA = 0;
	public final static int CHOICE_SEMICOLON = CHOICE_COMMA + 1;
	
	public final static int CHOICE_ALL_SCENARIOS = 0;
	public final static int CHOICE_CURRENT_SCENARIO = CHOICE_ALL_SCENARIOS + 1;
	public final static int CHOICE_SELECTED_SCENARIOS = CHOICE_CURRENT_SCENARIO + 1;
	
	public BulkImportPage(String pageName, ScenarioInstance currentScenario) {
		super(pageName);
		this.currentScenario = currentScenario;
		setTitle("Select data and scenarios");
		setDescription("Choose scenario(s) and a file for bulk data import.");
	}

	/**
	 * Class to provide labels for the elements in the scenario selector tree.
	 * @author Simon McGregor
	 *
	 */
	class ScenarioLabelProvider implements ILabelProvider {
		@Override
		public void addListener(ILabelProviderListener listener) { }

		@Override
		public void dispose() { }

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) { }

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof Folder) {					
				return ((Folder) element).getName();
			}
			if (element instanceof ScenarioInstance) {
				return ((ScenarioInstance) element).getName();
			}
			if (element instanceof ScenarioService) {
				return ((ScenarioService) element).getName();
			}
			if (element instanceof NamedObject) {
				return ((NamedObject) element).getName();
			}
			return element.toString();
		}
		
	}
	
	/**
	 * Class to provide content data for the scenario selector tree view control.
	 * @author Simon McGregor
	 *
	 */
	class ScenarioContentProvider implements ITreeContentProvider {
		EList<ScenarioService> services; 
	
		public ScenarioContentProvider() { }
		
		@Override
		public void dispose() { }
	
		@SuppressWarnings("unchecked")
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			services = (EList<ScenarioService>) newInput;
		}
		
		public Object[] getElements(Object inputElement) {
			if (services.size() == 1) {
				return getChildren(services.get(0));
			}
			else {
				return services.toArray();
			}
		}
		
		public Object [] getChildren(Object parentElement) {
			LinkedList<Object> result = new LinkedList<Object>();
			
			if (parentElement instanceof Container) {
				for (Object element: ((Container) parentElement).getElements()) {
					if (element instanceof ScenarioInstance) {
						result.add(element);
					}
					else if (element instanceof Container) {
						if (!((Container) element).getElements().isEmpty()) {
							result.add(element);
						}
					}
					
				}
			}
			
			return result.toArray();
		}
		
		public boolean hasChildren(Object element) {
			if (!(element instanceof Container)) {
				return false;
			}
			Container container = (Container) element;
			return !(container.getElements().isEmpty());
		}
	
		@Override
		public Object getParent(Object element) {
			return null;
		}
		
	}

	/**
	 * Returns a list of the scenario instances selected in the tree control.
	 * @return
	 */
	protected List<ScenarioInstance> getCheckedScenariosFromTree() {
		List<ScenarioInstance> result = new ArrayList<ScenarioInstance>();
		
		for (Object object: scenarioTreeViewer.getCheckedElements()) {
			if (object instanceof ScenarioInstance) {
				result.add((ScenarioInstance) object);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a list of all known scenario instances.
	 * @return
	 */
	protected List<ScenarioInstance> getAllAvailableScenarioInstances() {
		List<ScenarioInstance> result = new LinkedList<ScenarioInstance>();

		for (ScenarioService service: getServices()) {
			result.addAll(getAllDescendantScenarioInstances(service));
		}
		
		return result;				
	}	
	
	/**
	 * Returns a list of all scenario instances which descend (directly or indirectly) 
	 * from a given parent container.
	 * 
	 * @param parent 
	 * @return
	 */
	protected List<ScenarioInstance> getAllDescendantScenarioInstances(Container parent) {
		List<ScenarioInstance> result = new LinkedList<ScenarioInstance>();

		for (Object object: parent.getElements()) {
			if (object instanceof ScenarioInstance) {
				result.add((ScenarioInstance) object);
			}
			if (object instanceof Container) {
				result.addAll(getAllDescendantScenarioInstances((Container) object));
			}
		}				
		
		return result;
	}
	
	/**
	 * Returns the scenarios explicitly or implicitly selected by the user.
	 * If the user specified "All scenarios", return all available scenarios.
	 * If the user specified "Current scenario", return the current editing scenario only.
	 * If the user specified "Selected scenarios only", return the scenarios selected in the tree control.
	 * 
	 * @return
	 */
	public List<ScenarioInstance> getSelectedScenarios() {
		
		switch(scenarioSelectionGroup.getSelectedValue()) {
			case CHOICE_ALL_SCENARIOS: { // "All Scenarios"				
				return getAllAvailableScenarioInstances();
			}
			case 1: { // "Current Scenario"
				return Arrays.asList(new ScenarioInstance[] {currentScenario});
			}
			case 2: { // "Selected Scenarios Only"
				return getCheckedScenariosFromTree();
			}
		}
		
		return null;
	}
	
	public String getImportFilename() {
		return importFileEditor.getStringValue();
	}
	
	public char getCsvSeparator() {
		return (csvSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : ';');
	}
	
	public int getImportedField() {
		return BulkImportPage.CHOICE_COMMODITY_INDICES;
	}
	
	/**
	 * Returns a list of all scenario services currently available.
	 * @return
	 */
	EList<ScenarioService> getServices() {
		ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(Activator.getDefault().getBundle().getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();
		final ScenarioModel scenarioModel = tracker.getService().getScenarioModel();
		tracker.close();

		return scenarioModel.getScenarioServices();		
	}
	
	/*
	 * Is there *really* no standard JFace way to bind a radio button 
	 * group to a single variable? 
	 */
	
	class RadioSelectionGroup extends Composite {
		int selectedIndex = -1;
		final ArrayList<Button> buttons = new ArrayList<Button>();
		final Group group;
		final int [] values;
		
		public RadioSelectionGroup(Composite parent, String title, int style, String [] labels, int [] values) {
			super(parent, style);
			group = new Group(parent, style);
			for (String label: labels) {
				addButton(label);
			}
			setLayout(new GridLayout(1, false));
			group.setLayout(new GridLayout(labels.length, false));
			GridData groupLayoutData = new GridData();
			group.setLayoutData(groupLayoutData);
			group.setText(title);
			this.values = values;
		}
		

		public Button addButton(String text) {
			final int index = buttons.size();
			
			final Button button = new Button(group, SWT.RADIO);
			buttons.add(button);
			
			button.setText(text);
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					if (button.getSelection()) {
						RadioSelectionGroup.this.selectedIndex = index;
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) { }
				
			});
			
			return button;
		}
		
		public int getSelectedIndex() {
			return selectedIndex;
		}
		
		public void setSelectedIndex(int value) {
			buttons.get(value).setSelection(true);
			selectedIndex = value;
		}
		
		public int getSelectedValue() {
			return values[getSelectedIndex()];
		}
		
	}
	
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.BORDER);

		// set the layout for the whole functional region
		final GridLayout layout = new GridLayout(1, true);
		layout.verticalSpacing = 9;
		container.setLayout(layout);
		GridData ld = new GridData();
		ld.widthHint = 400;
		container.setLayoutData(ld);

//		dataImportGroup = new RadioSelectionGroup(container, "Data to import", SWT.NONE, new String[] {"Price curves", "Cargoes"}, new int[] {CHOICE_COMMODITY_INDICES, CHOICE_CARGOES});
//		dataImportGroup.setSelectedIndex(0);
		
		// create a radiobutton group for specifying CSV import
		csvSelectionGroup = new RadioSelectionGroup(container, "Format separator", SWT.NONE, new String[] {"comma (\",\")", "semicolon (\";\")"}, new int[] {CHOICE_COMMA, CHOICE_SEMICOLON});
		csvSelectionGroup.setSelectedIndex(0);
		GridData csvLayoutData = new GridData();
		csvLayoutData.widthHint = 500;
		//csvLayoutData.grabExcessHorizontalSpace = true;
		csvSelectionGroup.setLayoutData(csvLayoutData);

		importFileEditor = new FileFieldEditor("Data file", "Data file", csvSelectionGroup);
		importFileEditor.setFileExtensions(new String[] { "*.csv" });
		
		importFileEditor.getTextControl(csvSelectionGroup).addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				BulkImportPage.this.getContainer().updateButtons();				
			}
		});
		
		// create a radiobutton group for specifying how scenarios are selected
		String currentScenarioOption = String.format("Current ('%s')", currentScenario.getName());
		scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { "All", currentScenarioOption, "Selected"}, new int[] {CHOICE_ALL_SCENARIOS, CHOICE_CURRENT_SCENARIO, CHOICE_SELECTED_SCENARIOS});
		scenarioSelectionGroup.setSelectedIndex(0);
		
		// create a container for the scenario tree control (so we can hide it)
		final Composite viewerComposite = new Composite(container, SWT.BORDER);
		viewerComposite.setLayout(new GridLayout(1, true));
		GridData viewerCompositeLayoutData = new GridData();
		viewerCompositeLayoutData.widthHint = 450;
		viewerCompositeLayoutData.grabExcessVerticalSpace = true;
		viewerCompositeLayoutData.verticalAlignment = SWT.FILL;
		viewerComposite.setLayoutData(viewerCompositeLayoutData);

		// create a control to display the scenario tree
		scenarioTreeViewer = new CheckboxTreeViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		scenarioTreeViewer.setContentProvider(new ScenarioContentProvider());
		scenarioTreeViewer.setLabelProvider(new ScenarioLabelProvider());		
		scenarioTreeViewer.setAutoExpandLevel(3);
		scenarioTreeViewer.setInput(getServices());
		GridData viewerLayoutData = new GridData();
		viewerLayoutData.widthHint = 400;
		viewerLayoutData.grabExcessVerticalSpace = true;
		viewerLayoutData.verticalAlignment = SWT.FILL;
		scenarioTreeViewer.getTree().setLayoutData(viewerLayoutData);
		
		// when a parent element checkbox is clicked in the tree, propagate the change to its descendants
		scenarioTreeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object element = event.getElement();
				if (!(element instanceof ScenarioInstance)) {
					scenarioTreeViewer.setSubtreeChecked(element, event.getChecked());
				}
			}
		});
		
		// only enable the scenario tree when the appropriate radio button is selected 
		final Button selectedOnlyButton = scenarioSelectionGroup.buttons.get(2);
		scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
		selectedOnlyButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }			
		});
		
		
		setControl(container);
		control = container;
	}
	
	@Override
	public Control getControl() {
		return control;
	}

}
