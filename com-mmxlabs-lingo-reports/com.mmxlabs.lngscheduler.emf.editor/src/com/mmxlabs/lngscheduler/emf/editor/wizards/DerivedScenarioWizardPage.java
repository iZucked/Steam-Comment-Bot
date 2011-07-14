package com.mmxlabs.lngscheduler.emf.editor.wizards;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.ScenarioPackage;
import scenario.cargo.CargoPackage;
import scenario.contract.ContractPackage;
import scenario.fleet.FleetPackage;
import scenario.market.MarketPackage;
import scenario.port.PortPackage;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (scenario).
 */

public class DerivedScenarioWizardPage extends WizardPage {
	private Text containerText;

	private Text fileText;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public DerivedScenarioWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Create Derived Scenario");
		setDescription("This wizard creates a new scenario derived from static data in other scenarios or model files.");
		this.selection = selection;
	}

	private Map<EClass, String> sourceMap = new HashMap<EClass, String>();

	private ModifyListener createModifyListener(final EClass typeToImport) {
		return new ModifyListener() {
			@Override
			public void modifyText(final ModifyEvent e) {
				final Text text = (Text) e.widget;
				sourceMap.put(typeToImport, text.getText());
			}
		};
	}

	private SelectionListener createSelectionListener(final String caption,
			final Text linkedText, final Set<String> extensions) {
		return new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final ResourceListSelectionDialog rlsd = new ResourceListSelectionDialog(
						getShell(), ResourcesPlugin.getWorkspace().getRoot(),
						IResource.FILE | IResource.DEPTH_INFINITE) {
					@Override
					protected boolean select(final IResource resource) {
						return super.select(resource)
								&& extensions.contains(resource
										.getFileExtension().toLowerCase());
					}
				};

				rlsd.setMessage("Select a " + caption + " source");

				if (rlsd.open() == ResourceListSelectionDialog.OK) {
					linkedText.setText(((IResource) rlsd.getResult()[0])
							.getFullPath().toString());
				}
			}
		};
	}

	private EObject getFirstElementWithClass(final EObject top,
			final EClass eClass) {
		final TreeIterator<EObject> iterator = top.eAllContents();
		while (iterator.hasNext()) {
			final EObject el = iterator.next();
			if (el.eClass().equals(eClass))
				return el;
		}
		return null;
	}

	/**
	 * Load all the referenced files into a resource set and then copy any stuff
	 * that is produced into a scenario. This probably won't work.
	 * 
	 * @return
	 */
	public Scenario createScenario() {
		System.err.println(sourceMap);
		final Scenario result = ScenarioFactory.eINSTANCE.createScenario();

		final ResourceSet resourceSet = new ResourceSetImpl();

		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI,
				ScenarioPackage.eINSTANCE);

		final Set<String> loadedResources = new HashSet<String>();
		
		// read all the elements which we've imported
		for (final Map.Entry<EClass, String> entry : sourceMap.entrySet()) {
			// try and read entry
			if (loadedResources.contains(entry.getValue())) continue;
			loadedResources.add(entry.getValue());
			resourceSet.getResource(URI.createPlatformResourceURI(entry.getValue(), true), true);
//			System.err.println(resourceSet.createResource(URI.createPlatformResourceURI(entry.getValue(), true)));
		}

		here: for (final Map.Entry<EClass, String> entry : sourceMap.entrySet()) {
			for (final Resource resource : resourceSet.getResources()) {
				for (final EObject e : resource.getContents()) {
					
					final EObject e2 = getFirstElementWithClass(e,
							entry.getKey());
					if (e2 != null) {
						// insert e2
						for (final EReference ref : result.eClass()
								.getEAllReferences()) {
							if (ref.getEType().equals(entry.getKey())) {
								result.eSet(ref, e2);
							}
							if (ref.isContainment() == false) {
								result.getContainedModels().add(e2);
							}
						}
						continue here;
					}
				}
			}
		}

		result.createMissingModels();
		
		return result;
	}

	private void createSourcePicker(final Composite container,
			final String caption, final EClass typeToImport,
			final String[] extensions) {
		new Label(container, SWT.NONE).setText(caption + ":");
		final Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(createModifyListener(typeToImport));

		final Button b = new Button(container, SWT.PUSH);
		b.setText("Browse...");

		b.addSelectionListener(createSelectionListener(caption, text,
				new HashSet<String>(Arrays.asList(extensions))));
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(final Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout());

		final Group sources = new Group(container, SWT.NONE);
		sources.setText("Data Sources");
		sources.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		sources.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		createSourcePicker(sources, "&Ports",
				PortPackage.eINSTANCE.getPortModel(), new String[] {
						"scenario", "port" });
		createSourcePicker(sources, "&Distances",
				PortPackage.eINSTANCE.getDistanceModel(), new String[] {
						"scenario", "distancemodel" });
		createSourcePicker(sources, "&Fleet",
				FleetPackage.eINSTANCE.getFleetModel(), new String[] {
						"scenario", "fleet" });
		createSourcePicker(sources, "&Indices",
				MarketPackage.eINSTANCE.getMarketModel(), new String[] {
						"scenario", "market" });
		createSourcePicker(sources, "&Contracts",
				ContractPackage.eINSTANCE.getContractModel(), new String[] {
						"scenario", "contract" });
		createSourcePicker(sources, "C&argos",
				CargoPackage.eINSTANCE.getCargoModel(), new String[] {
						"scenario", "cargo" });

		final Group destination = new Group(container, SWT.NONE);
		destination.setText("Destination");
		layout = new GridLayout();
		destination.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		destination.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(destination, SWT.NULL);
		label.setText("&Container:");

		containerText = new Text(destination, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(destination, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(destination, SWT.NULL);
		label.setText("&File name:");

		fileText = new Text(destination, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
			}
		}
		fileText.setText("new_scenario.scenario");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("scenario") == false) {
				updateStatus("File extension must be \"scenario\"");
				return;
			}
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}
}