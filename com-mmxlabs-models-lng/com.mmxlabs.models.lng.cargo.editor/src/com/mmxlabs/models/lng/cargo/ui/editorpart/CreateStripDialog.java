/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.common.PairKeyedMap;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.models.datetime.ui.formatters.LocalDateTextFormatter;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.editors.IDisplayComposite;
import com.mmxlabs.models.ui.editors.IDisplayCompositeFactory;
import com.mmxlabs.models.ui.editors.dialogs.DefaultDialogEditingContext;
import com.mmxlabs.models.ui.editors.dialogs.DialogValidationSupport;
import com.mmxlabs.models.ui.editors.dialogs.IDialogController;
import com.mmxlabs.models.ui.editors.util.EditorUtils;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * This dialog is used to create a strip of {@link Slot}s with consistent parameters but with varying date according to some kind of pattern.
 * 
 * @author Simon Goodall
 * 
 */
public class CreateStripDialog extends FormDialog {

	public static enum StripType {
		TYPE_FOB_PURCHASE_SLOT("FOB Purchase"), TYPE_DES_PURCHASE_SLOT("DES Purchase"), TYPE_DES_SALE_SLOT("DES Sale"), TYPE_FOB_SALE_SLOT("FOB Sale");

		private final @NonNull String name;

		private StripType(final @NonNull String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	private ObservablesManager mgr;
	private EMFDataBindingContext dbc;

	private List<EObject> generatedObjects;

	private GridTableViewer previewWiewer;

	private final StripType stripType;
	private EClass referenceClass;

	private final IScenarioEditingLocation scenarioEditingLocation;
	// private final IDialogEditingContext dialogContext;
	private EObject sample;
	private Text pattern_n;
	private Label label1;
	private Label label2;
	private Label label3;
	private ComboViewer repeatType;
	private ComboViewer intervalType;
	private DateTime pattern_periodStart;
	private DateTime pattern_periodEnd;

	private DialogValidationSupport validationSupport;
	private final Map<Object, IStatus> validationErrors = new HashMap<Object, IStatus>();

	private enum RepeatType {
		Periodic, Distributed
	};

	private enum IntervalType {
		days, weeks, months
	};

	private DefaultDialogEditingContext dialogContext;

	private final IDialogController dialogController = new IDialogController() {

		private final PairKeyedMap<EObject, EStructuralFeature, Boolean> visibilityMap = new PairKeyedMap<>();

		@Override
		public void validate() {

		}

		@Override
		public void rebuild(boolean pack) {

		}

		@Override
		public void updateEditorVisibility() {
			// Trigger the recursive UI update
			if (templateDetailComposite != null) {
				templateDetailComposite.checkVisibility(dialogContext);
			}
		}

		@Override
		public void setEditorVisibility(final EObject object, final EStructuralFeature feature, final boolean visible) {
			visibilityMap.put(object, feature, visible);

		}

		@Override
		public boolean getEditorVisibility(final EObject object, final EStructuralFeature feature) {
			if (visibilityMap.contains(object, feature)) {
				return visibilityMap.get(object, feature).booleanValue();
			}
			return true;
		}

		@Override
		public void relayout() {

		}
	};
	private IDisplayComposite templateDetailComposite;

	public CreateStripDialog(@NonNull final IShellProvider parentShell, @NonNull final IScenarioEditingLocation originalScenarioEditingLocation, @NonNull final StripType stripType,
			@Nullable final EObject selectedObject) {
		super(parentShell);
		this.scenarioEditingLocation = createScenarioEditingLocation(originalScenarioEditingLocation);
		// this.dialogContext = dialogContext;
		this.stripType = stripType;
		createSample(selectedObject);
	}

	public CreateStripDialog(@NonNull final Shell parentShell, @NonNull final IScenarioEditingLocation originalScenarioEditingLocation, @NonNull final StripType stripType,
			@Nullable final EObject selectedObject) {
		super(parentShell);
		this.scenarioEditingLocation = createScenarioEditingLocation(originalScenarioEditingLocation);
		// this.dialogContext = dialogContext;
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
		sample.eSet(MMXCorePackage.Literals.NAMED_OBJECT__NAME, "option");

		// // Set a default window start
		sample.eSet(CargoPackage.eINSTANCE.getSlot_WindowStart(), LocalDate.now());

		// Copy valid features across
		if (selectedObject != null) {
			for (final EStructuralFeature f : sample.eClass().getEAllStructuralFeatures()) {
				// Skip unset features.
				if (!selectedObject.eIsSet(f)) {
					continue;
				}
				// Skip UUID
				if (f == MMXCorePackage.eINSTANCE.getUUIDObject_Uuid()) {
					continue;
				}
				// skip cargo ref
				if (f == CargoPackage.eINSTANCE.getSlot_Cargo()) {
					continue;
				}

				// Skip containment references -- again how should we handle this?
				if (f instanceof EReference && ((EReference) f).isContainment()) {
					final EReference reference = (EReference) f;
					if (reference.isMany()) {
						sample.eSet(f, EcoreUtil.copyAll((Collection<EObject>) selectedObject.eGet(f)));
					} else {
						sample.eSet(f, EcoreUtil.copy((EObject) selectedObject.eGet(f)));
					}
					continue;
				}
				// Skip many - how do we handle it?
				if (f.isMany()) {
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

		String title = "Create strip of ";
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

		final Composite patternComposite = toolkit.createComposite(body);
		{
			patternComposite.setLayout(new GridLayout(10, false));
			patternComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			toolkit.createLabel(patternComposite, "Pattern");

			{
				repeatType = new ComboViewer(patternComposite);
				toolkit.adapt(repeatType.getControl(), true, true);

				repeatType.setContentProvider(new ArrayContentProvider());
				repeatType.setLabelProvider(new LabelProvider());

				repeatType.setInput(RepeatType.values());
			}

			{
				final Label spacer = toolkit.createLabel(patternComposite, "");
				final GridData gd = new GridData();
				gd.widthHint = 10;
				spacer.setLayoutData(gd);
			}

			{
				label1 = toolkit.createLabel(patternComposite, "Every");
				final GridData gd = new GridData();
				gd.widthHint = 36;
				label1.setLayoutData(gd);
			}

			{
				pattern_n = toolkit.createText(patternComposite, "1");
				final GridData gd = new GridData();
				gd.widthHint = 20;
				pattern_n.setLayoutData(gd);
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
			}
			{
				label2 = toolkit.createLabel(patternComposite, "slot(s) between");
				final GridData gd = new GridData();
				gd.widthHint = 80;
				label2.setLayoutData(gd);
			}

			{
				intervalType = new ComboViewer(patternComposite);
				toolkit.adapt(intervalType.getControl(), true, true);

				intervalType.setContentProvider(new ArrayContentProvider());
				intervalType.setLabelProvider(new LabelProvider());

				intervalType.setInput(IntervalType.values());

			}
			repeatType.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(final SelectionChangedEvent event) {
					final int idx = repeatType.getCombo().getSelectionIndex();
					final RepeatType rt = RepeatType.values()[idx];
					if (rt == RepeatType.Periodic) {
						label1.setText("Create slots every");
						((GridData) label1.getLayoutData()).widthHint = 96;
						label2.setVisible(false);
						label3.setVisible(true);
						intervalType.getControl().setVisible(true);

						((GridData) label2.getLayoutData()).exclude = true;
						((GridData) label3.getLayoutData()).exclude = false;
						((GridData) intervalType.getControl().getLayoutData()).exclude = false;
					} else {
						assert rt == RepeatType.Distributed;
						label1.setText("Create");
						((GridData) label1.getLayoutData()).widthHint = 40;
						label2.setVisible(true);
						label3.setVisible(false);
						intervalType.getControl().setVisible(false);

						((GridData) label2.getLayoutData()).exclude = false;
						((GridData) label3.getLayoutData()).exclude = true;
						((GridData) intervalType.getControl().getLayoutData()).exclude = true;
					}
					patternComposite.pack();

					refreshPreview();
				}
			});
			intervalType.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(final SelectionChangedEvent event) {

					refreshPreview();
				}
			});

			{
				label3 = toolkit.createLabel(patternComposite, "between");
				pattern_periodStart = new DateTime(patternComposite, SWT.DROP_DOWN);
				toolkit.adapt(pattern_periodStart);
				final GridData gd = new GridData();
				gd.widthHint = 100;
				pattern_periodStart.setLayoutData(gd);
				pattern_periodStart.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {

						final LocalDate date = getLocalDateFromDateTimeWidget(pattern_periodStart);

						sample.eSet(CargoPackage.Literals.SLOT__WINDOW_START, date);

						refreshPreview();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			}

			{
				toolkit.createLabel(patternComposite, "and");
				pattern_periodEnd = new DateTime(patternComposite, SWT.DROP_DOWN);
				toolkit.adapt(pattern_periodEnd);
				final GridData gd = new GridData();
				gd.widthHint = 100;
				pattern_periodEnd.setLayoutData(gd);
				pattern_periodEnd.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						refreshPreview();
					}

					@Override
					public void widgetDefaultSelected(final SelectionEvent e) {

					}
				});
			}

		}
		final Composite splitPane = toolkit.createComposite(body);
		splitPane.setLayout(new GridLayout(2, false));
		splitPane.setLayoutData(new GridData(GridData.FILL_BOTH));
		{
			// Template - take defaults from current selection
			final Group template = new Group(splitPane, SWT.NONE);
			template.setLayout(new GridLayout(2, false));
			template.setLayoutData(new GridData(GridData.FILL_BOTH));

			template.setText("Template");
			toolkit.adapt(template);

			dialogContext = new DefaultDialogEditingContext(dialogController, scenarioEditingLocation, false, true);

			final IDisplayCompositeFactory factory = Activator.getDefault().getDisplayCompositeFactoryRegistry().getDisplayCompositeFactory(sample.eClass());
			templateDetailComposite = factory.createSublevelComposite(template, sample.eClass(), dialogContext, toolkit);
			templateDetailComposite.setCommandHandler(scenarioEditingLocation.getDefaultCommandHandler());

			templateDetailComposite.getComposite().setLayoutData(new GridData(GridData.FILL_BOTH));

			final Collection<EObject> range = factory.getExternalEditingRange(scenarioEditingLocation.getRootObject(), sample);
			range.add(sample);

			templateDetailComposite.display(dialogContext, scenarioEditingLocation.getRootObject(), sample, range, dbc);

			// Trigger update of inline editor visibility and UI state update
			dialogController.updateEditorVisibility();
		}

		// Preview Table with generated options
		{
			final Group previewGroup = new Group(splitPane, SWT.NONE);
			previewGroup.setLayout(new GridLayout(1, false));
			previewGroup.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH));

			previewGroup.setText("Preview");
			toolkit.adapt(previewGroup);

			previewWiewer = new GridTableViewer(previewGroup);
			GridViewerHelper.configureLookAndFeel(previewWiewer);

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

		repeatType.setSelection(new StructuredSelection(RepeatType.Periodic));
		intervalType.setSelection(new StructuredSelection(IntervalType.days));

		final LocalDate localDate;
		// Only valid for slots
		if (sample.eIsSet(CargoPackage.eINSTANCE.getSlot_WindowStart())) {
			localDate = (LocalDate) sample.eGet(CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else {
			localDate = LocalDate.now();
		}
		pattern_periodStart.setDate(localDate.getYear(), 1 + localDate.getMonthValue(), localDate.getDayOfMonth());
		pattern_periodEnd.setDate(localDate.getYear(), 1 + localDate.getMonthValue(), localDate.getDayOfMonth());

		// Hook up refresh handlers
		final EContentAdapter changedAdapter = new EContentAdapter() {
			public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {

				super.notifyChanged(notification);
				refreshPreview();
			}
		};
		sample.eAdapters().add(changedAdapter);

		refreshPreview();
	}

	private void refreshPreview() {

		this.generatedObjects = updateGeneratedObjects();
		previewWiewer.setInput(this.generatedObjects);
		for (final GridColumn c : previewWiewer.getGrid().getColumns()) {
			c.pack();
		}
	}

	private void createColumn(final GridTableViewer viewer, final EStructuralFeature feature) {
		final GridViewerColumn col = new GridViewerColumn(viewer, SWT.NONE);
		col.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
		col.setLabelProvider(new ValidationLabelProvider(feature));
		col.getColumn().setWidth(50);
		col.getColumn().setText(EditorUtils.unmangle(feature.getName()));
	}

	@SuppressWarnings("deprecation")
	private List<EObject> updateGeneratedObjects() {
		// Sync dates
		{
			// Only valid for slots
			if (sample.eIsSet(CargoPackage.eINSTANCE.getSlot_WindowStart())) {
				final LocalDate date = (LocalDate) sample.eGet(CargoPackage.eINSTANCE.getSlot_WindowStart());
				pattern_periodStart.setDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
			}
		}
		// Generate the dates
		final List<LocalDate> dates = new LinkedList<>();

		TemporalUnit calUnit = ChronoUnit.MONTHS;
		int calSpacing = 1;
		try {
			calSpacing = Integer.parseInt(pattern_n.getText());
		} catch (final NumberFormatException nfe) {
			// Ignore
		}

		final int n = calSpacing;

		// Min of 1 element
		calSpacing = Math.max(1, calSpacing);

		final LocalDate toDate = getLocalDateFromDateTimeWidget(pattern_periodEnd);
		final LocalDate fromDate = getLocalDateFromDateTimeWidget(pattern_periodStart);

		// ABS as sanity check...
		final int diffInDays = Days.between(fromDate, toDate);

		final int rtIdx = repeatType.getCombo().getSelectionIndex();
		if (rtIdx < 0) {
			return Collections.emptyList();
		}
		final RepeatType rt = RepeatType.values()[rtIdx];
		{
			switch (rt) {
			case Distributed: {
				calUnit = ChronoUnit.DAYS;
				calSpacing = Math.max(1, calSpacing - 1);
				calSpacing = diffInDays / calSpacing;
				calSpacing = Math.max(1, calSpacing);
			}
				break;
			case Periodic: {
				calUnit = ChronoUnit.DAYS;
				final int itIdx = intervalType.getCombo().getSelectionIndex();
				if (itIdx < 0) {
					return Collections.emptyList();
				}
				final IntervalType it = IntervalType.values()[itIdx];
				switch (it) {
				case days:
					calUnit = ChronoUnit.DAYS;
					break;
				case months:
					calUnit = ChronoUnit.MONTHS;
					break;
				case weeks:
					calUnit = ChronoUnit.WEEKS;
					break;
				default:
					break;
				}
			}
				break;
			default:
				break;

			}
		}

		LocalDate sampleDate;
		// Only valid for slots
		if (sample.eIsSet(CargoPackage.eINSTANCE.getSlot_WindowStart())) {
			sampleDate = (LocalDate) sample.eGet(CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else {
			sampleDate = LocalDate.now();
		}

		if (rt == RepeatType.Distributed) {
			for (int i = 0; i < n; ++i) {
				dates.add(sampleDate);
				sampleDate = sampleDate.plus(calSpacing, calUnit);
			}
		} else {
			while (toDate.isAfter(sampleDate)) {
				dates.add(sampleDate);
				sampleDate = sampleDate.plus(calSpacing, calUnit);
			}
		}

		// Pricing date special case - keep the difference in months constant
		int pricingMonthDiff = 0;
		final LocalDate pricingDate = (LocalDate) sample.eGet(CargoPackage.eINSTANCE.getSlot_PricingDate());
		if (sample.eIsSet(CargoPackage.eINSTANCE.getSlot_PricingDate())) {
			final int sampleKey = sampleDate.getYear() * 100 + sampleDate.getMonthValue();
			final int pricingKey = pricingDate.getYear() * 100 + pricingDate.getMonthValue();
			pricingMonthDiff = pricingKey - sampleKey;
		}

		// Generate the slots
		final List<EObject> objects = new ArrayList<EObject>(dates.size());
		final CargoFactory factory = CargoFactory.eINSTANCE;
		int counter = 1;
		for (final LocalDate date : dates) {
			final EObject eObj = factory.create(referenceClass);
			objects.add(eObj);

			// Check FOB/DES elements
			if (stripType == StripType.TYPE_DES_PURCHASE_SLOT) {
				eObj.eSet(CargoPackage.eINSTANCE.getLoadSlot_DESPurchase(), Boolean.TRUE);
			} else if (stripType == StripType.TYPE_FOB_SALE_SLOT) {
				eObj.eSet(CargoPackage.eINSTANCE.getDischargeSlot_FOBSale(), Boolean.TRUE);
			}

			for (final EStructuralFeature feature : sample.eClass().getEAllStructuralFeatures()) {
				if (feature == MMXCorePackage.eINSTANCE.getUUIDObject_Uuid()) {
					continue;
				} else if (feature == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
					final String name = (String) sample.eGet(feature);
					eObj.eSet(feature, name + "-" + (counter++));
				} else if (feature == CargoPackage.eINSTANCE.getSlot_WindowStart()) {
					eObj.eSet(feature, date);
				} else if (feature == CargoPackage.eINSTANCE.getSlot_PricingDate()) {
					if (sample.eIsSet(feature)) {
						// Shift by predetermined month difference
						final LocalDate d2 = date.plusMonths(pricingMonthDiff);
						eObj.eSet(feature, d2);
					}
				} else {
					// Copy from template
					if (sample.eIsSet(feature)) {

						// Skip containment references -- again how should we handle this?
						if (feature instanceof EReference && ((EReference) feature).isContainment()) {
							final EReference reference = (EReference) feature;
							if (reference.isMany()) {
								eObj.eSet(feature, EcoreUtil.copyAll((Collection<EObject>) sample.eGet(feature)));
							} else {
								eObj.eSet(feature, EcoreUtil.copy((EObject) sample.eGet(feature)));
							}
						} else {
							eObj.eSet(feature, sample.eGet(feature));
						}
					} else {
						eObj.eUnset(feature);
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

		// final EditingDomain editingDomain = new AdapterFactoryEditingDomain(original.getAdapterFactory(), new BasicCommandStack());

		final ICommandHandler commandHandler = new ICommandHandler() {

			@Override
			public void handleCommand(final Command command, final EObject target, final EStructuralFeature feature) {
				command.execute();
				// Activator.getDefault().getCommandProviderTracker().
				refreshPreview();
			}

			@Override
			public IReferenceValueProviderProvider getReferenceValueProviderProvider() {
				return original.getReferenceValueProviderCache();
			}

			@Override
			public EditingDomain getEditingDomain() {
				return original.getEditingDomain();
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
				return original.getEditingDomain();
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
			if (element instanceof LocalDate) {
				final LocalDateTextFormatter formatter = new LocalDateTextFormatter();
				formatter.setValue(element);
				return formatter.getDisplayString();
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

	@NonNull
	private LocalDate getLocalDateFromDateTimeWidget(@NonNull final DateTime dateTime) {
		return LocalDate.of(dateTime.getYear(), 1 + dateTime.getMonth(), dateTime.getDay());
	}
}
