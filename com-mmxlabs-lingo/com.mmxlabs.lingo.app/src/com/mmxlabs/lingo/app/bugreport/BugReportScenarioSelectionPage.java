/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.bugreport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.rcp.common.widgets.TextEmptyMessageWrapper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.scenario.service.ScenarioServiceRegistry;
import com.mmxlabs.scenario.service.model.Container;
import com.mmxlabs.scenario.service.model.Folder;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioModel;
import com.mmxlabs.scenario.service.model.ScenarioService;

public class BugReportScenarioSelectionPage extends WizardPage {

	protected Control control = null;
	protected RadioSelectionGroup scenarioSelectionGroup;
	protected CheckboxTreeViewer scenarioTreeViewer;

	protected final ScenarioInstance currentScenario;

	public static final int CHOICE_CURRENT_SCENARIO = 0;
	public static final int CHOICE_SELECTED_SCENARIOS = CHOICE_CURRENT_SCENARIO + 1;

	private Text subjectTextField;
	private Text descriptionTextField;

	private Button errorLogButton;

	public BugReportScenarioSelectionPage(final String pageName, final ScenarioInstance currentScenario) {
		super(pageName);
		this.currentScenario = currentScenario;
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
			if (element instanceof Folder) {
				return CommonImages.getImage(CommonImages.IconPaths.Folder, IconMode.Enabled);
			}
			if (element instanceof ScenarioInstance) {
				return CommonImages.getImage(CommonImages.IconPaths.Scenario, IconMode.Enabled);
			}
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof Folder folder) {
				return folder.getName();
			}
			if (element instanceof ScenarioInstance si) {
				return si.getName();
			}
			if (element instanceof ScenarioService ss) {
				return ss.getName();
			}
			if (element instanceof NamedObject no) {
				return no.getName();
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

			final LinkedList<Object> result = new LinkedList<>();
			if (parentElement instanceof Container parentContainer) {
				for (final Object element : parentContainer.getElements()) {
					if (element instanceof ScenarioInstance scenarioInstance) {
						result.add(scenarioInstance);
					} else if (element instanceof Container container) {
						if (!container.getElements().isEmpty()) {
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
			if (object instanceof ScenarioInstance si) {
				result.add(si);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all scenario instances which descend (directly or
	 * indirectly) from a given parent container.
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
	 * Returns the scenarios explicitly or implicitly selected by the user. If the
	 * user specified "All scenarios", return all available scenarios. If the user
	 * specified "Current scenario", return the current editing scenario only. If
	 * the user specified "Selected scenarios only", return the scenarios selected
	 * in the tree control.
	 * 
	 * @return
	 */
	public List<ScenarioInstance> getSelectedScenarios() {

		switch (scenarioSelectionGroup.getSelectedValue()) {
		case 0: { // "Current Scenario"
			return Arrays.asList(currentScenario);
		}
		case 1: { // "Selected Scenarios Only"
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
		Bundle bundle = FrameworkUtil.getBundle(BugReportScenarioSelectionPage.class);
		final ServiceTracker<ScenarioServiceRegistry, ScenarioServiceRegistry> tracker = new ServiceTracker<>(bundle.getBundleContext(), ScenarioServiceRegistry.class, null);
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
	 * Is there *really* no standard JFace way to bind a radio button group to a
	 * single variable?
	 */

	public static class RadioSelectionGroup extends Composite {
		int selectedIndex = -1;
		final ArrayList<Button> buttons = new ArrayList<>();
		final Group group;
		final int[] values;

		public RadioSelectionGroup(final Composite parent, final String title, final int style, final String[] labels, final int[] values) {
			super(parent, style);

			final GridLayout layout = new GridLayout(1, true);
			// layout.marginRight = 0;
			layout.marginWidth = 0;
			setLayout(layout);

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
		{
			// set the layout for the whole functional region
			final GridLayout layout = new GridLayout(2, false);

			container.setLayout(layout);
			final GridData ld = new GridData();

			ld.verticalIndent = 0;
			ld.horizontalAlignment = SWT.FILL;
			ld.grabExcessHorizontalSpace = true;
			container.setLayoutData(ld);
		}

		{
			Label lbl = new Label(container, SWT.WRAP);
			lbl.setText(
					"To submit a new bug report, please add in relevant scenarios and the error log. Pressing the 'Finish' button will generate a draft .eml file and open it with your system E-Mail client. The contents can be reviewed and additional information such as screenshots and email signatures can be added before sending.");

			final GridData ld = new GridData();
			ld.horizontalSpan = 2;
			ld.verticalIndent = 0;
			ld.horizontalAlignment = SWT.FILL;
			ld.grabExcessHorizontalSpace = true;

			lbl.setLayoutData(ld);
		}

		{
			Label lbl = new Label(container, SWT.NONE);
			lbl.setText("Subject:");

			subjectTextField = new Text(container, SWT.NONE | SWT.BORDER);
			TextEmptyMessageWrapper.wrap(subjectTextField, "Bug report subject line");

			final GridData ld = new GridData();
			ld.horizontalAlignment = SWT.FILL;
			ld.grabExcessHorizontalSpace = true;

			subjectTextField.setLayoutData(ld);

			subjectTextField.addModifyListener(e -> getContainer().updateButtons());
		}

		{
			Label lbl = new Label(container, SWT.NONE);
			lbl.setText("Description:");

			descriptionTextField = new Text(container, SWT.NONE | SWT.WRAP | SWT.BORDER);
			TextEmptyMessageWrapper.wrap(descriptionTextField,
					"Please describe your issue here.\nInclude any steps to reproduce. Screen shots can be added later in the generated email prior to sending.");

			final GridData ld = new GridData();
			ld.heightHint = 70;
			ld.horizontalAlignment = SWT.FILL;
			ld.grabExcessHorizontalSpace = true;

			descriptionTextField.setLayoutData(ld);

			descriptionTextField.addModifyListener(e -> getContainer().updateButtons());

		}

		{

			errorLogButton = new Button(container, SWT.CHECK);
			errorLogButton.setText("Include error log");
			// Enable by default
			errorLogButton.setSelection(true);

			final GridData ld = new GridData();
			ld.horizontalSpan = 2;
			ld.verticalIndent = 0;
			ld.horizontalAlignment = SWT.FILL;
			ld.grabExcessHorizontalSpace = true;

			errorLogButton.setLayoutData(ld);
		}

		// create a radiobutton group for specifying how scenarios are selected
		final int selectedOnlyIndex;
		if (currentScenario != null) {
			final String currentScenarioOption = String.format("Current ('%s')", currentScenario.getName());
			scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { currentScenarioOption, "Selected" },
					new int[] { CHOICE_CURRENT_SCENARIO, CHOICE_SELECTED_SCENARIOS });
			selectedOnlyIndex = 1;
		} else {
			scenarioSelectionGroup = new RadioSelectionGroup(container, "Scenarios", SWT.NONE, new String[] { "Selected" }, new int[] { CHOICE_SELECTED_SCENARIOS });
			selectedOnlyIndex = 0;
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
		scenarioTreeViewer = new CheckboxTreeViewer(viewerComposite, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
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

		// when a parent element checkbox is clicked in the tree, propagate the change
		// to its descendants
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
		selectedOnlyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				scenarioTreeViewer.getTree().setEnabled(selectedOnlyButton.getSelection());
			}
		});

		setControl(container);
		control = container;
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public boolean isPageComplete() {

		return !subjectTextField.getText().isBlank() && !descriptionTextField.getText().isBlank();
	}

	public String getSubject() {
		return subjectTextField.getText();
	}

	public String getBody() {
		return descriptionTextField.getText();
	}

	public boolean isIncludeErrorLog() {
		return errorLogButton.getSelection();
	}
}
