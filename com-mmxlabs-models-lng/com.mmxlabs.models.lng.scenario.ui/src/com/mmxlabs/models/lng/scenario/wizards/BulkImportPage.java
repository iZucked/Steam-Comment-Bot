/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.wizards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
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
 */
public class BulkImportPage extends WizardPage {

	private static final String DELIMITER_KEY = "lastDelimiter";
	private static final String DECIMAL_SEPARATOR_KEY = "lastDecimalSeparator";
	private static final String SECTION_NAME = "BulkImportPage.section";

	protected Control control = null;
	// private RadioSelectionGroup dataImportGroup;
	protected RadioSelectionGroup scenarioSelectionGroup;
	protected RadioSelectionGroup csvSelectionGroup;
	protected RadioSelectionGroup decimalSelectionGroup;
	protected CheckboxTreeViewer scenarioTreeViewer;
	protected FileFieldEditor importFileEditor;
	protected final FieldChoice importField;

	protected final ScenarioInstance currentScenario;

	// TODO: refactor to remove dependence on this enumeration
	public enum FieldChoice {
		CHOICE_COMMODITY_INDICES, //
		CHOICE_CARGOES, //
		CHOICE_CHARTER_INDICES, //
		CHOICE_BASE_FUEL_CURVES, //
		CHOICE_CURRENCY_CURVES, //
		CHOICE_ALL_INDICIES
	};

	public static final int CHOICE_COMMA = 0;
	public static final int CHOICE_SEMICOLON = CHOICE_COMMA + 1;
	public static final int CHOICE_PERIOD = CHOICE_COMMA + 1;

	public static final int CHOICE_ALL_SCENARIOS = 0;
	public static final int CHOICE_CURRENT_SCENARIO = CHOICE_ALL_SCENARIOS + 1;
	public static final int CHOICE_SELECTED_SCENARIOS = CHOICE_CURRENT_SCENARIO + 1;

	public BulkImportPage(final String pageName, final FieldChoice fieldChoice, final ScenarioInstance currentScenario) {
		super(pageName);
		this.currentScenario = currentScenario;
		this.importField = fieldChoice;
		setTitle("Select data and scenarios");
		setDescription("Choose scenario(s) and a file for bulk data import.");
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
				final List<ScenarioService> localServices = new LinkedList<>();
				for (final ScenarioService ss : services) {
					if (ss.isLocal()) {
						localServices.add(ss);
					}
				}
				if (localServices.size() == 1) {
					return getChildren(localServices.get(0));
				} else {
					return localServices.toArray();
				}
			}
		}

		@Override
		public Object[] getChildren(final Object parentElement) {

			final LinkedList<Object> result = new LinkedList<Object>();
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
		final List<ScenarioInstance> result = new ArrayList<ScenarioInstance>();

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
		final List<ScenarioInstance> result = new LinkedList<ScenarioInstance>();

		for (final ScenarioService service : getLocalServices()) {
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
		final List<ScenarioInstance> result = new LinkedList<ScenarioInstance>();

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
		case 1: { // "Current Scenario"
			return Arrays.asList(new ScenarioInstance[] { currentScenario });
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

	public char getDecimalSeparator() {
		return (decimalSelectionGroup.getSelectedValue() == CHOICE_COMMA ? ',' : '.');
	}

	public FieldChoice getImportedField() {
		return importField;
	}

	/**
	 * Returns a list of all scenario services currently available.
	 * 
	 * @return
	 */
	private List<ScenarioService> getServices() {
		final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker = new ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry>(Activator.getDefault().getBundle()
				.getBundleContext(), ScenarioServiceRegistry.class, null);
		tracker.open();
		final ScenarioModel scenarioModel = tracker.getService().getScenarioModel();
		tracker.close();

		return scenarioModel.getScenarioServices();
	}

	private List<ScenarioService> getLocalServices() {
		final List<ScenarioService> localServices = new LinkedList<>();

		for (final ScenarioService ss : getServices()) {
			if (ss.isLocal()) {
				localServices.add(ss);
			}
		}
		return localServices;
	}

	/*
	 * Is there *really* no standard JFace way to bind a radio button group to a single variable?
	 */

	public static class RadioSelectionGroup extends Composite {
		int selectedIndex = -1;
		final ArrayList<Button> buttons = new ArrayList<Button>();
		final Group group;
		final int[] values;

		public RadioSelectionGroup(final Composite parent, final String title, final int style, final String[] labels, final int[] values) {
			super(parent, style);

			final GridLayout layout = new GridLayout(1, true);
			// layout.marginRight = 0;
			layout.marginWidth = 0;
			setLayout(layout);
			// GridData gd = new GridData();
			// gd.verticalIndent = 0;
			// gd.horizontalIndent = 0;
			// gd.grabExcessHorizontalSpace = true;
			// setLayoutData(gd);

			group = new Group(this, style);
			for (final String label : labels) {
				addButton(label);
			}
			final GridLayout gl = new GridLayout(1, false);
			gl.marginLeft = 0;
			gl.marginRight = 0;
			gl.marginWidth = 0;
			setLayout(gl);
			group.setLayout(new GridLayout(labels.length, false));
			// GridData groupLayoutData = new GridData();
			// group.setLayoutData(groupLayoutData);
			group.setText(title);
			this.values = values;
		}

		public Button addButton(final String text) {
			final int index = buttons.size();

			final Button button = new Button(group, SWT.RADIO);
			buttons.add(button);

			button.setText(text);
			// GridData gd = new GridData();
			// // gd.verticalIndent = 0;
			// // gd.horizontalIndent = 0;
			// // gd.grabExcessHorizontalSpace = true;
			// button.setLayoutData(gd);
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (button.getSelection()) {
						RadioSelectionGroup.this.selectedIndex = index;
					}
				}

				@Override
				public void widgetDefaultSelected(final SelectionEvent e) {
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
		final Composite container = new Composite(parent, SWT.NONE);

		// set the layout for the whole functional region
		final GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);
		final GridData ld = new GridData();
		ld.verticalIndent = 0;
		ld.horizontalAlignment = SWT.FILL;
		ld.grabExcessHorizontalSpace = true;
		container.setLayoutData(ld);

		final Composite datafileC = new Composite(container, SWT.NONE);
		final GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		datafileC.setLayoutData(gd);

		importFileEditor = new FileFieldEditor("Data file", "Data file", datafileC);
		importFileEditor.setFileExtensions(new String[] { "*.csv" });
		importFileEditor.getTextControl(datafileC).addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				BulkImportPage.this.getContainer().updateButtons();
			}
		});

		// Composite separatorsC = new Composite(container, SWT.BORDER);
		// final RowLayout l = new RowLayout();//(2, false);
		// l.marginLeft = 0;
		// l.marginRight = 0;
		// l.marginWidth = 0;
		//
		// //// layout.verticalSpacing = 9;
		// // layout.marginWidth = 0;
		// // layout.verticalSpacing= 0;
		// // layout.horizontalSpacing= 0;
		// separatorsC.setLayout(l);
		// GridData gd2 = new GridData();
		// // gd.horizontalIndent = 0;
		// gd2.horizontalAlignment = SWT.FILL;
		// gd2.horizontalSpan = 2;
		// // gd2.verticalIndent = 0;
		// separatorsC.setLayoutData(gd2);
		//
		// create a radiobutton group for specifying CSV import
		csvSelectionGroup = new RadioSelectionGroup(container, "Format separator", SWT.NONE, new String[] { "comma (\",\")", "semicolon (\";\")" }, new int[] { CHOICE_COMMA, CHOICE_SEMICOLON });
		final GridData csvLayoutData = new GridData();
		// csvLayoutData.widthHint = 220;
		// csvLayoutData.horizontalIndent = 0;
		// csvLayoutData.verticalIndent = 0;
		csvLayoutData.grabExcessHorizontalSpace = true;
		csvLayoutData.horizontalAlignment = SWT.FILL;
		csvSelectionGroup.setLayoutData(csvLayoutData);

		// create a radiobutton group for specifying CSV import
		decimalSelectionGroup = new RadioSelectionGroup(container, "Decimal separator", SWT.NONE, new String[] { "comma (\",\")", "period (\".\")" }, new int[] { CHOICE_COMMA, CHOICE_PERIOD });
		final GridData decimalLayoutData = new GridData();
		// decimalLayoutData.widthHint = 220;
		// decimalLayoutData.horizontalIndent = 0;
		// decimalLayoutData.verticalIndent = 0;
		decimalLayoutData.grabExcessHorizontalSpace = true;
		decimalLayoutData.horizontalAlignment = SWT.FILL;
		decimalSelectionGroup.setLayoutData(decimalLayoutData);

		// get the default export directory from the settings
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		final IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		int delimiterValue = CHOICE_COMMA;
		if (section != null && section.get(DELIMITER_KEY) != null) {
			delimiterValue = section.getInt(DELIMITER_KEY);
		}
		int decimalValue = CHOICE_PERIOD;
		if (section != null && section.get(DECIMAL_SEPARATOR_KEY) != null) {
			decimalValue = section.getInt(DECIMAL_SEPARATOR_KEY);
		}
		// use it to populate the editor
		csvSelectionGroup.setSelectedIndex(delimiterValue);
		decimalSelectionGroup.setSelectedIndex(decimalValue);

		// create a radiobutton group for specifying how scenarios are selected
		final int selectedOnlyIndex;
		if (currentScenario != null) {
			final String currentScenarioOption = String.format("Current ('%s')", currentScenario.getName());
			scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { "All", currentScenarioOption, "Selected" }, new int[] { CHOICE_ALL_SCENARIOS,
					CHOICE_CURRENT_SCENARIO, CHOICE_SELECTED_SCENARIOS });
			selectedOnlyIndex = 2;
		} else {
			scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { "All", "Selected" }, new int[] { CHOICE_ALL_SCENARIOS, CHOICE_SELECTED_SCENARIOS });
			selectedOnlyIndex = 1;
		}
		scenarioSelectionGroup.setSelectedIndex(1);
		final GridData gd3 = new GridData();
		gd3.horizontalAlignment = SWT.FILL;
		gd3.grabExcessHorizontalSpace = true;
		gd3.horizontalSpan = 2;
		scenarioSelectionGroup.setLayoutData(gd3);

		// create a container for the scenario tree control (so we can hide it)
		final Composite viewerComposite = new Composite(container, SWT.NONE);
		viewerComposite.setLayout(new GridLayout(1, true));
		final GridData viewerCompositeLayoutData = new GridData();
		// viewerCompositeLayoutData.widthHint = 250;
		viewerCompositeLayoutData.grabExcessVerticalSpace = true;
		viewerCompositeLayoutData.grabExcessHorizontalSpace = true;
		viewerCompositeLayoutData.verticalAlignment = SWT.FILL;
		viewerCompositeLayoutData.horizontalAlignment = SWT.FILL;
		viewerCompositeLayoutData.horizontalSpan = 2;
		viewerComposite.setLayoutData(viewerCompositeLayoutData);

		// create a control to display the scenario tree
		scenarioTreeViewer = new CheckboxTreeViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		scenarioTreeViewer.setContentProvider(new ScenarioContentProvider());
		scenarioTreeViewer.setLabelProvider(new ScenarioLabelProvider());
		scenarioTreeViewer.setAutoExpandLevel(3);
		scenarioTreeViewer.setInput(getLocalServices());
		final GridData viewerLayoutData = new GridData();
		viewerLayoutData.grabExcessVerticalSpace = true;
		viewerLayoutData.grabExcessHorizontalSpace = true;
		viewerLayoutData.verticalAlignment = SWT.FILL;
		viewerLayoutData.horizontalAlignment = SWT.FILL;
		scenarioTreeViewer.getTree().setLayoutData(viewerLayoutData);

		// when a parent element checkbox is clicked in the tree, propagate the change to its descendants
		scenarioTreeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(final CheckStateChangedEvent event) {
				final Object element = event.getElement();
				if (!(element instanceof ScenarioInstance)) {
					scenarioTreeViewer.setSubtreeChecked(element, event.getChecked());
				}
			}
		});

		// only enable the scenario tree when the appropriate radio button is selected
		final Button selectedOnlyButton = scenarioSelectionGroup.buttons.get(selectedOnlyIndex);
		scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
		selectedOnlyButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {
			}
		});

		setControl(container);
		control = container;
	}

	@Override
	public Control getControl() {
		return control;
	}

	/**
	 * Saves the value of the directory editor field to persistent storage
	 */
	public void saveDirectorySetting() {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(SECTION_NAME);
		if (section == null) {
			section = dialogSettings.addNewSection(SECTION_NAME);
		}
		section.put(DELIMITER_KEY, csvSelectionGroup.getSelectedValue());
		section.put(DECIMAL_SEPARATOR_KEY, decimalSelectionGroup.getSelectedValue());
	}
}
