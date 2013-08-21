package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditorFactory;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

public class CreateStripDialog extends FormDialog {

	public static enum StripType {
		TYPE_FOB_SALE_SLOT("FOB Sale"), TYPE_FOB_PURCHASE_SLOT("FOB Purchase"), TYPE_DES_SALE_SLOT("DES Sale"), TYPE_DES_PURCHASE_SLOT("DES Purchase");

		private final String name;

		private StripType(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	private ObservablesManager mgr;
	private EMFDataBindingContext dbc;

	// TODO: Derive from type
	private List<EObject> generatedObjects;
	private GridTableViewer previewWiewer;
	private List<EStructuralFeature> features;
	private Map<EStructuralFeature, IInlineEditor> editorMap;
	private final StripType stripType;
	private EClass referenceClass;

	private final IScenarioEditingLocation scenarioEditingLocation;
	private EObject sample;
	private Text pattern_n;
	private ComboViewer pattern;
	private Text pattern_quantity;

	private DialogValidationSupport validationSupport;
	private final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	private enum Patterns {
		MONTHLY, N_PER_YEAR, EVERY_N_DAYS
	}

	public CreateStripDialog(@NonNull final IShellProvider parentShell, @NonNull final IScenarioEditingLocation originalScenarioEditingLocation, @NonNull final StripType stripType,
			@Nullable final EObject selectedObject) {
		super(parentShell);
		this.scenarioEditingLocation = createScenarioEditingLocation(originalScenarioEditingLocation);
		this.stripType = stripType;
		createSample(selectedObject);
	}

	public CreateStripDialog(@NonNull final Shell parentShell, @NonNull final IScenarioEditingLocation originalScenarioEditingLocation, @NonNull final StripType stripType,
			@Nullable final EObject selectedObject) {
		super(parentShell);
		this.scenarioEditingLocation = createScenarioEditingLocation(originalScenarioEditingLocation);
		this.stripType = stripType;
		createSample(selectedObject);
	}

	private void createSample(@Nullable final EObject selectedObject) {

		switch (stripType) {
		case TYPE_DES_PURCHASE_SLOT:
			sample = CargoFactory.eINSTANCE.createLoadSlot();
			sample.eSet(CargoPackage.eINSTANCE.getLoadSlot_DESPurchase(), Boolean.TRUE);
			break;
		case TYPE_DES_SALE_SLOT:
			sample = CargoFactory.eINSTANCE.createDischargeSlot();
			break;
		case TYPE_FOB_PURCHASE_SLOT:
			sample = CargoFactory.eINSTANCE.createLoadSlot();
			break;
		case TYPE_FOB_SALE_SLOT:
			sample = CargoFactory.eINSTANCE.createDischargeSlot();
			sample.eSet(CargoPackage.eINSTANCE.getDischargeSlot_FOBSale(), Boolean.TRUE);
			break;
		default:
			break;
		}

		// Copy valid features across
		if (selectedObject != null) {
			for (final EStructuralFeature f : sample.eClass().getEAllStructuralFeatures()) {
				if (f == MMXCorePackage.eINSTANCE.getUUIDObject_Uuid()) {
					continue;
				}
				if (f.isMany()) {
					continue;
				}
				if (f instanceof EReference && ((EReference) f).isContainment()) {
					continue;
				}
				if (selectedObject.eClass().getEAllStructuralFeatures().contains(f) && selectedObject.eIsSet(f)) {
					sample.eSet(f, selectedObject.eGet(f));
				}
			}
		}
	}

	@Override
	protected void createFormContent(final IManagedForm mform) {

		String title = "Create Strip of ";
		switch (stripType) {
		case TYPE_DES_PURCHASE_SLOT:
			referenceClass = CargoPackage.eINSTANCE.getLoadSlot();
			title += "DES Purchases";
			break;
		case TYPE_DES_SALE_SLOT:
			referenceClass = CargoPackage.eINSTANCE.getDischargeSlot();
			title += "DES Sales";
			break;
		case TYPE_FOB_PURCHASE_SLOT:
			referenceClass = CargoPackage.eINSTANCE.getLoadSlot();
			title += "FOB Purchases";
			break;
		case TYPE_FOB_SALE_SLOT:
			referenceClass = CargoPackage.eINSTANCE.getDischargeSlot();
			title += "FOB Sales";
			break;
		default:
			break;
		}

		mform.getForm().setText(title);
		mform.getToolkit().decorateFormHeading(mform.getForm().getForm());

		mgr = new ObservablesManager();
		dbc = new EMFDataBindingContext();

		mgr.runAndCollect(new Runnable() {

			@Override
			public void run() {
				createFormArea(mform);
			}
		});

	}

	@Override
	public boolean close() {

		if (dbc != null) {
			dbc.dispose();
			dbc = null;
		}
		if (mgr != null) {
			mgr.dispose();
			mgr = null;
		}

		return super.close();
	}

	private void createFormArea(final IManagedForm mform) {
		final FormToolkit toolkit = mform.getToolkit();

		final Composite body = mform.getForm().getBody();
		body.setLayout(new GridLayout(1, false));

		validationSupport = new DialogValidationSupport(scenarioEditingLocation.getExtraValidationContext());

		{
			final Composite patternComposite = toolkit.createComposite(body);
			patternComposite.setLayout(new GridLayout(4, false));
			patternComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			toolkit.createLabel(patternComposite, "Repeat Pattern");
			pattern = new ComboViewer(patternComposite);
			{
				GridData gd = new GridData(GridData.GRAB_HORIZONTAL);
				gd.horizontalSpan = 3;
				pattern.getControl().setLayoutData(gd);
			}
			toolkit.adapt(pattern.getControl(), true, true);

			pattern.setContentProvider(new ArrayContentProvider());
			pattern.setLabelProvider(new LabelProvider());

			pattern.setInput(Patterns.values());
			{
				toolkit.createLabel(patternComposite, "n");
				pattern_n = toolkit.createText(patternComposite, "1");
				pattern_n.addVerifyListener(new VerifyListener() {

					@Override
					public void verifyText(final VerifyEvent e) {
						if (e.text == null) {
							// OK
						} else if (e.text.isEmpty()) {
							// OK
						} else {
							try {
								Integer.parseInt(e.text);
							} catch (final NumberFormatException nfe) {
								e.doit = false;
							}
						}
					}
				});
				pattern_n.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(final ModifyEvent e) {
						refreshPreview();
					}
				});
				pattern.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(final SelectionChangedEvent event) {
						final int idx = pattern.getCombo().getSelectionIndex();
						final Patterns p = Patterns.values()[idx];
						if (p == Patterns.EVERY_N_DAYS || p == Patterns.N_PER_YEAR) {
							pattern_n.setEnabled(true);
						} else {
							pattern_n.setEnabled(false);
						}
						refreshPreview();
					}
				});
			}
			{
				toolkit.createLabel(patternComposite, "quantity");
				pattern_quantity = toolkit.createText(patternComposite, "1");
				pattern_quantity.addVerifyListener(new VerifyListener() {

					@Override
					public void verifyText(final VerifyEvent e) {
						if (e.text == null) {
							// OK
						} else if (e.text.isEmpty()) {
							// OK
						} else {
							try {
								Integer.parseInt(e.text);
							} catch (final NumberFormatException nfe) {
								e.doit = false;
							}
						}
					}
				});
				pattern_quantity.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(final ModifyEvent e) {
						refreshPreview();
					}
				});
				pattern.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(final SelectionChangedEvent event) {
						final int idx = pattern.getCombo().getSelectionIndex();
						final Patterns p = Patterns.values()[idx];
						if (p == Patterns.EVERY_N_DAYS || p == Patterns.MONTHLY) {
							pattern_quantity.setEnabled(true);
						} else {
							pattern_quantity.setEnabled(false);
						}
						refreshPreview();
					}
				});
			}
		}
		{
			// Template - take defaults from current selection
			// Slot ? - ID prefix Port Contract, Price Expr, Date
			// Advanced ( CV, volumes, etc)
			final Group template = new Group(body, SWT.NONE);
			template.setLayout(new GridLayout(1, false));
			template.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			template.setText("Template");
			toolkit.adapt(template);

			features = Lists.newArrayList(MMXCorePackage.eINSTANCE.getNamedObject_Name(), CargoPackage.eINSTANCE.getSlot_Contract(), CargoPackage.eINSTANCE.getSlot_PriceExpression(),
					CargoPackage.eINSTANCE.getSlot_Port(), CargoPackage.eINSTANCE.getSlot_WindowStart());
			editorMap = new HashMap<EStructuralFeature, IInlineEditor>();
			for (final EStructuralFeature feature : features) {

				final Composite editorRow = toolkit.createComposite(template);
				editorRow.setLayout(new GridLayout(2, false));

				final Label lbl = toolkit.createLabel(editorRow, "");

				final IInlineEditorFactory factory = Activator.getDefault().getEditorFactoryRegistry().getEditorFactory(referenceClass, feature);
				final IInlineEditor editor = factory.createEditor(referenceClass, feature);
				editor.setCommandHandler(scenarioEditingLocation.getDefaultCommandHandler());
				editor.createControl(editorRow, dbc, toolkit);
				editor.setLabel(lbl);
				editorMap.put(feature, editor);

				// Copied object from selection
				editor.display(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), sample, null);
			}
		}

		// Preview Table with generated options
		{
			previewWiewer = new GridTableViewer(body);
			previewWiewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
			// Enable tooltip support
			ColumnViewerToolTipSupport.enableFor(previewWiewer);

			previewWiewer.getGrid().setHeaderVisible(true);

			previewWiewer.setContentProvider(new ArrayContentProvider());

			createColumn(previewWiewer, MMXCorePackage.eINSTANCE.getNamedObject_Name());
			createColumn(previewWiewer, CargoPackage.eINSTANCE.getSlot_Contract());
			createColumn(previewWiewer, CargoPackage.eINSTANCE.getSlot_PriceExpression());
			createColumn(previewWiewer, CargoPackage.eINSTANCE.getSlot_Port());
			createColumn(previewWiewer, CargoPackage.eINSTANCE.getSlot_WindowStart());

			refreshPreview();
		}

		// Hook up refresh handlers
		final EContentAdapter changedAdapter = new EContentAdapter() {
			public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {

				super.notifyChanged(notification);
				refreshPreview();
			}
		};
		sample.eAdapters().add(changedAdapter);

		pattern.setSelection(new StructuredSelection(Patterns.N_PER_YEAR));
	}

	private void refreshPreview() {

		this.generatedObjects = updateGeneratedObjects();
		previewWiewer.setInput(this.generatedObjects);
	}

	private void createColumn(final GridTableViewer viewer, final EStructuralFeature feature) {
		final GridViewerColumn col = new GridViewerColumn(viewer, SWT.NONE);
		col.setLabelProvider(new ValidationLabelProvider(feature));
		col.getColumn().setWidth(50);
		col.getColumn().setText(feature.getName());
	}

	private List<EObject> updateGeneratedObjects() {

		final List<Date> dates = new LinkedList<Date>();
		// Generate the dates

		int calUnit = Calendar.MONTH;
		int calSpacing = 1;
		int calQuantity = 1;

		{
			int n = 1;
			try {
				n = Integer.parseInt(pattern_n.getText());
			} catch (final NumberFormatException nfe) {
				// Ignore
			}

			int quantity = 1;
			try {
				quantity = Integer.parseInt(pattern_quantity.getText());
			} catch (final NumberFormatException nfe) {
				// Ignore
			}
			final int selectionIndex = pattern.getCombo().getSelectionIndex();
			if (selectionIndex < 0) {
				return Collections.emptyList();
			}

			final Patterns p = Patterns.values()[selectionIndex];
			switch (p) {
			case EVERY_N_DAYS:
				calSpacing = n;
				calUnit = Calendar.DAY_OF_YEAR;
				calQuantity = quantity;
				break;
			case MONTHLY:
				calSpacing = 1;
				calUnit = Calendar.MONTH;
				calQuantity = quantity;
				break;
			case N_PER_YEAR:
				calSpacing = 365 / n;
				calQuantity = Calendar.DAY_OF_YEAR;
				calQuantity = n;
				break;
			default:
				break;
			}
		}

		// TODO: - Derive from pattern
		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		// Only valid for slots
		if (sample.eIsSet(CargoPackage.eINSTANCE.getSlot_WindowStart())) {
			cal.setTime((Date) sample.eGet(CargoPackage.eINSTANCE.getSlot_WindowStart()));
		} else {
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
		}
		for (int i = 0; i < calQuantity; ++i) {
			dates.add(cal.getTime());
			cal.add(calUnit, calSpacing);
		}

		// Generate the slots
		final List<EObject> objects = new ArrayList<EObject>(dates.size());
		final CargoFactory factory = CargoFactory.eINSTANCE;
		int counter = 1;
		for (final Date date : dates) {
			final EObject eObj = factory.create(referenceClass);
			objects.add(eObj);

			// Check FOB/DES elements
			if (stripType == StripType.TYPE_DES_PURCHASE_SLOT) {
				eObj.eSet(CargoPackage.eINSTANCE.getLoadSlot_DESPurchase(), Boolean.TRUE);
			} else if (stripType == StripType.TYPE_FOB_SALE_SLOT) {
				eObj.eSet(CargoPackage.eINSTANCE.getDischargeSlot_FOBSale(), Boolean.TRUE);
			}

			for (final EStructuralFeature feature : features) {
				final IInlineEditor editor = editorMap.get(feature);
				if (editor != null) {
					if (feature == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
						final String name = (String) editor.getEditorTarget().eGet(feature);
						eObj.eSet(feature, name + "-" + (counter++));
					} else if (feature == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
						eObj.eSet(feature, date);
					} else {
						// Copy from template
						eObj.eSet(feature, editor.getEditorTarget().eGet(feature));
					}
				}
			}

		}

		// Run validation
		validationSupport.setValidationTargets(objects);
		scenarioEditingLocation.pushExtraValidationContext(validationSupport.getValidationContext());
		final IStatus status = validationSupport.validate();
		validationErrors.clear();
		validationSupport.processStatus(status, validationErrors);
		scenarioEditingLocation.popExtraValidationContext();

		return objects;
	}

	public Command createStrip(final CargoModel cargoModel, final EditingDomain domain) {
		final CompoundCommand cmd = new CompoundCommand("Create Strip");
		for (final EObject eObject : generatedObjects) {
			if (CargoPackage.eINSTANCE.getLoadSlot().isInstance(eObject)) {
				cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), eObject));
			} else if (CargoPackage.eINSTANCE.getDischargeSlot().isInstance(eObject)) {
				cmd.append(AddCommand.create(domain, cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), eObject));
			} else {
				throw new IllegalStateException("Unexpected type");
			}
		}
		return cmd;
	}

	/**
	 * Create a new {@link IScenarioEditingLocation} wrapping the "original" {@link IScenarioEditingLocation} to handle the editing requirements of this dialog - editing objects which are not yet
	 * contained within the scenario.
	 * 
	 * @param original
	 * @return
	 */
	private IScenarioEditingLocation createScenarioEditingLocation(final IScenarioEditingLocation original) {

		final EditingDomain editingDomain = new AdapterFactoryEditingDomain(original.getAdapterFactory(), new BasicCommandStack());

		final ICommandHandler commandHandler = new ICommandHandler() {

			@Override
			public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
				command.execute();

				refreshPreview();
			}

			@Override
			public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
				return original.getReferenceValueProviderCache();
			}

			@Override
			public EditingDomain getEditingDomain() {
				return editingDomain;
			}
		};

		return new IScenarioEditingLocation() {

			@Override
			public void setLocked(final boolean locked) {

			}

			@Override
			public void setDisableUpdates(final boolean disable) {

			}

			@Override
			public void setDisableCommandProviders(final boolean disable) {

			}

			@Override
			public void setCurrentViewer(final Viewer viewer) {

			}

			@Override
			public void pushExtraValidationContext(final IExtraValidationContext context) {
				original.pushExtraValidationContext(context);
			}

			@Override
			public void popExtraValidationContext() {
				original.popExtraValidationContext();
			}

			@Override
			public boolean isLocked() {
				return false;
			}

			@Override
			public IStatusProvider getStatusProvider() {
				return original.getStatusProvider();
			}

			@Override
			public Shell getShell() {
				return original.getShell();
			}

			@Override
			public ScenarioInstance getScenarioInstance() {
				return original.getScenarioInstance();
			}

			@Override
			public MMXRootObject getRootObject() {
				return original.getRootObject();
			}

			@Override
			public IReferenceValueProviderProvider getReferenceValueProviderCache() {
				return original.getReferenceValueProviderCache();
			}

			@Override
			public IExtraValidationContext getExtraValidationContext() {
				return original.getExtraValidationContext();
			}

			@Override
			public ScenarioLock getEditorLock() {
				return null;
			}

			@Override
			public EditingDomain getEditingDomain() {
				return editingDomain;
			}

			@Override
			public ICommandHandler getDefaultCommandHandler() {
				return commandHandler;
			}

			@Override
			public AdapterFactory getAdapterFactory() {
				return original.getAdapterFactory();
			}
		};
	}

	private class ValidationLabelProvider extends ColumnLabelProvider {

		private final Color errorColour = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		private final Color warningColour = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);

		private final EStructuralFeature feature;

		protected ValidationLabelProvider(final EStructuralFeature feature) {
			this.feature = feature;
		}

		@Override
		public Color getBackground(final Object element) {
			if (validationErrors.containsKey(element)) {
				final IStatus s = validationErrors.get(element);
				if (s.getSeverity() == IStatus.ERROR) {
					return errorColour;
				} else if (s.getSeverity() == IStatus.WARNING) {
					return warningColour;
				}
			}

			return super.getBackground(element);
		}

		@Override
		public String getText(Object element) {

			if (element instanceof EObject) {
				element = ((EObject) element).eGet(feature);
			}
			if (element instanceof NamedObject) {
				return ((NamedObject) element).getName();
			} else if (element != null) {
				return element.toString();
			}
			return super.getText(element);
		}

		@Override
		public String getToolTipText(final Object element) {
			if (validationErrors.containsKey(element)) {
				final IStatus s = validationErrors.get(element);
				if (!s.isOK()) {
					return getMessages(s);
				}
			}
			return super.getToolTipText(element);
		}

		/**
		 * Extract message hierarchy and construct the tool tip message.
		 * 
		 * @param status
		 * @return
		 */
		private String getMessages(final IStatus status) {
			if (status.isMultiStatus()) {
				final StringBuilder sb = new StringBuilder();
				for (final IStatus s : status.getChildren()) {
					sb.append(getMessages(s));
					sb.append("\n");
				}
				return sb.toString();
			} else {
				return status.getMessage();
			}
		}
	}
}
