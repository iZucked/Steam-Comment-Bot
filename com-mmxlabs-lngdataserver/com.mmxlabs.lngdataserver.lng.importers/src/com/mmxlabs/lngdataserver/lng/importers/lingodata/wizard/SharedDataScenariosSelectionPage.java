/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.lngdataserver.lng.importers.lingodata.wizard.SharedScenarioDataUtils.DataOptions;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;

/**
 */
public class SharedDataScenariosSelectionPage extends WizardPage {
	protected Control control = null;
	protected RadioSelectionGroup scenarioSelectionGroup;
	protected CheckboxTreeViewer scenarioTreeViewer;

	

	private final Set<DataOptions> selectedDataOptions = EnumSet.noneOf(DataOptions.class);

	public static final int CHOICE_ALL_SCENARIOS = 0;
	public static final int CHOICE_SELECTED_SCENARIOS = CHOICE_ALL_SCENARIOS + 1;

	public SharedDataScenariosSelectionPage(final String pageName) {
		super(pageName);
		setTitle("Select data and scenarios");
		setDescription("Choose data types and scenario to copy data into");
	}

	/**
	 * Class to provide labels for the elements in the scenario selector tree.
	 * 
	 * @author Simon McGregor
	 * 
	 */
	class ScenarioLabelProvider implements ILabelProvider {
		@Override
		public void addListener(final ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(final Object element, final String property) {
			return false;
		}

		@Override
		public void removeListener(final ILabelProviderListener listener) {
		}

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
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
	 * 
	 * @author Simon McGregor
	 * 
	 */
	class ScenarioContentProvider implements ITreeContentProvider {
		private List<ScenarioService> services;

		public ScenarioContentProvider() {
		}

		@Override
		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		@Override
		public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
			services = (List<ScenarioService>) newInput;
		}

		@Override
		public Object[] getElements(final Object inputElement) {
			if (services.size() == 1) {
				return getChildren(services.get(0));
			} else {
				return services.toArray();
			}
		}

		@Override
		public Object[] getChildren(final Object parentElement) {
			final LinkedList<Object> result = new LinkedList<>();

			if (parentElement instanceof Container) {
				for (final Object element : ((Container) parentElement).getElements()) {
					if (element instanceof ScenarioInstance) {
						result.add(element);
					} else if (element instanceof Container) {
						if (!((Container) element).getElements().isEmpty()) {
							result.add(element);
						}
					}

				}
			}

			return result.toArray();
		}

		@Override
		public boolean hasChildren(final Object element) {
			if (!(element instanceof Container)) {
				return false;
			}
			final Container container = (Container) element;
			return !(container.getElements().isEmpty());
		}

		@Override
		public Object getParent(final Object element) {
			return null;
		}

	}

	/**
	 * Returns a list of the scenario instances selected in the tree control.
	 * 
	 * @return
	 */
	protected List<ScenarioInstance> getCheckedScenariosFromTree() {
		final List<ScenarioInstance> result = new ArrayList<>();

		for (final Object object : scenarioTreeViewer.getCheckedElements()) {
			if (object instanceof ScenarioInstance) {
				result.add((ScenarioInstance) object);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all known scenario instances.
	 * 
	 * @return
	 */
	protected List<ScenarioInstance> getAllAvailableScenarioInstances() {
		final List<ScenarioInstance> result = new LinkedList<>();

		for (final ScenarioService service : getServices()) {
			result.addAll(getAllDescendantScenarioInstances(service));
		}

		return result;
	}

	/**
	 * Returns a list of all scenario instances which descend (directly or indirectly) from a given parent container.
	 * 
	 * @param parent
	 * @return
	 */
	protected List<ScenarioInstance> getAllDescendantScenarioInstances(final Container parent) {
		final List<ScenarioInstance> result = new LinkedList<>();

		for (final Object object : parent.getElements()) {
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
	 * Returns the scenarios explicitly or implicitly selected by the user. If the user specified "All scenarios", return all available scenarios. If the user specified "Current scenario", return the
	 * current editing scenario only. If the user specified "Selected scenarios only", return the scenarios selected in the tree control.
	 * 
	 * @return
	 */
	public List<ScenarioInstance> getSelectedScenarios() {

		switch (scenarioSelectionGroup.getSelectedValue()) {
		case CHOICE_ALL_SCENARIOS: { // "All Scenarios"
			return getAllAvailableScenarioInstances();
		}
		case CHOICE_SELECTED_SCENARIOS: { // "Selected Scenarios Only"
			return getCheckedScenariosFromTree();
		}
		}

		return null;
	}

	/**
	 * Returns a list of all scenario services currently available.
	 * 
	 * @return
	 */
	private List<ScenarioService> getServices() {
		final BundleContext bundleContext = FrameworkUtil.getBundle(SharedDataScenariosSelectionPage.class).getBundleContext();

		final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker = new ServiceTracker<>(bundleContext, ScenarioServiceRegistry.class, null);
		tracker.open();
		final ScenarioModel scenarioModel = tracker.getService().getScenarioModel();
		tracker.close();

		return scenarioModel.getScenarioServices().stream().filter(ScenarioService::isLocal).collect(Collectors.toList());
	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {

		if (scenarioSelectionGroup.selectedIndex == -1) {
			updateStatus("Please select a scenario option");
			return;
		}

		else if (scenarioSelectionGroup.selectedIndex == CHOICE_SELECTED_SCENARIOS) {

			if (getCheckedScenariosFromTree().isEmpty()) {
				updateStatus("Please select a scenario");
				return;
			}
		}

		if (getSelectedDataOptions().isEmpty()) {
			updateStatus("At least one data option should be selected");
			return;
		}

		updateStatus(null);
	}

	public Set<DataOptions> getSelectedDataOptions() {
		return selectedDataOptions;
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/*
	 * Is there *really* no standard JFace way to bind a radio button group to a single variable?
	 */
	class RadioSelectionGroup extends Composite {
		int selectedIndex = -1;
		final ArrayList<Button> buttons = new ArrayList<>();
		final Group group;
		final int[] values;

		public RadioSelectionGroup(final Composite parent, final String title, final int style, final String[] labels, final int[] values) {
			super(parent, style);
			group = new Group(parent, style);
			for (final String label : labels) {
				addButton(label);
			}
			setLayout(new GridLayout(1, false));
			group.setLayout(new GridLayout(labels.length, false));
			final GridData groupLayoutData = new GridData();
			group.setLayoutData(groupLayoutData);
			group.setText(title);
			this.values = values;
		}

		public Button addButton(final String text) {
			final int index = buttons.size();

			final Button button = new Button(group, SWT.RADIO);
			buttons.add(button);

			button.setText(text);
			button.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (button.getSelection()) {
						RadioSelectionGroup.this.selectedIndex = index;
					}
					dialogChanged();
				}
			});

			return button;
		}

		public int getSelectedIndex() {
			return selectedIndex;
		}

		public void setSelectedIndex(final int value) {
			buttons.get(value).setSelection(true);
			selectedIndex = value;
		}

		public int getSelectedValue() {
			return values[getSelectedIndex()];
		}

	}

	@Override
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.BORDER);

		// set the layout for the whole functional region
		final GridLayout layout = new GridLayout(1, true);
		layout.verticalSpacing = 9;
		container.setLayout(layout);
		final GridData ld = new GridData();
		ld.widthHint = 400;
		container.setLayoutData(ld);

		final Group dataOptions = new Group(container, SWT.NONE);
		dataOptions.setText("Data");
		final GridLayout layout2 = new GridLayout();
		dataOptions.setLayout(layout2);
		dataOptions.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		dataOptions.setFont(parent.getFont());

		for (final DataOptions option : DataOptions.values()) {
			final Button btn = new Button(dataOptions, SWT.CHECK);
			btn.setText(option.getName());
			btn.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (btn.getSelection()) {
						selectedDataOptions.add(option);
					} else {
						selectedDataOptions.remove(option);
					}
					dialogChanged();
				}
			});
		}

		// create a radiobutton group for specifying how scenarios are selected
		final int selectedButtonIdx;
		scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { "All", "Selected" }, new int[] { CHOICE_ALL_SCENARIOS, CHOICE_SELECTED_SCENARIOS });
		selectedButtonIdx = 1;

		// create a container for the scenario tree control (so we can hide it)
		final Composite viewerComposite = new Composite(container, SWT.BORDER);
		viewerComposite.setLayout(new GridLayout(1, true));
		final GridData viewerCompositeLayoutData = new GridData();
		viewerCompositeLayoutData.widthHint = 450;
		viewerCompositeLayoutData.grabExcessVerticalSpace = true;
		viewerCompositeLayoutData.verticalAlignment = SWT.FILL;
		viewerComposite.setLayoutData(viewerCompositeLayoutData);

		// create a control to display the scenario tree
		scenarioTreeViewer = new CheckboxTreeViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		ScenarioContentProvider contentProvider = new ScenarioContentProvider();
		scenarioTreeViewer.setContentProvider(contentProvider);
		scenarioTreeViewer.setLabelProvider(new ScenarioLabelProvider());
		scenarioTreeViewer.setAutoExpandLevel(3);
		scenarioTreeViewer.setInput(getServices());
		final GridData viewerLayoutData = new GridData();
		viewerLayoutData.widthHint = 400;
		viewerLayoutData.grabExcessVerticalSpace = true;
		viewerLayoutData.verticalAlignment = SWT.FILL;
		scenarioTreeViewer.getTree().setLayoutData(viewerLayoutData);

		// when a parent element checkbox is clicked in the tree, propagate the change
		// to its descendants
		scenarioTreeViewer.addCheckStateListener(event -> {
			final Object element = event.getElement();
			if (!(element instanceof ScenarioInstance)) {
				scenarioTreeViewer.setSubtreeChecked(element, event.getChecked());
			}
			dialogChanged();
		});

		// This will be current if available or selected
		scenarioSelectionGroup.setSelectedIndex(1);

		// only enable the scenario tree when the appropriate radio button is selected
		final Button selectedOnlyButton = scenarioSelectionGroup.buttons.get(selectedButtonIdx);
		scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
		selectedOnlyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
				dialogChanged();
			}
		});

		setControl(container);
		control = container;

		dialogChanged();
	}

	@Override
	public Control getControl() {
		return control;
	}

}
