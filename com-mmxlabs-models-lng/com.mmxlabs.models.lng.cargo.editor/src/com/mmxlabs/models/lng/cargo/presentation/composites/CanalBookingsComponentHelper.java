/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.port.ui.editors.CanalEntryAttributeManipulator;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.dates.MonthAttributeManipulator;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * A component helper for CanalBookings instances
 *
 * @generated
 */
public class CanalBookingsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CanalBookingsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CanalBookingsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.MMX_OBJECT));
	}
	
	/**
	 * add editors to a composite, using CanalBookings as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CANAL_BOOKINGS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated NOT
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_canalBookingSlotsEditor(detailComposite, topClass);
		add_vesselGroupCanalParametersEditor(detailComposite, topClass);
		add_panamaSeasonalityRecordsEditor(detailComposite, topClass);
		add_arrivalMarginHoursEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the canalBookingSlots feature on CanalBookings
	 *
	 * @generated NOT
	 */
	protected void add_canalBookingSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
		b.withShowHeaders(true);
		b.withLabel("Bookings");
		b.withContentProvider(new ArrayContentProvider());
		b.withHeightHint(200);
		
		addLocalDateColumn(b, "Date", CargoPackage.Literals.CANAL_BOOKING_SLOT__BOOKING_DATE);
		addCanalEntryColumn(b, "Direction", CargoPackage.Literals.CANAL_BOOKING_SLOT__CANAL_ENTRANCE, PortModelLabeller::getDirection);
		addSingleReferenceColumn(b, "Vessel", CargoPackage.Literals.CANAL_BOOKING_SLOT__VESSEL);
		addSingleReferenceColumn(b, "Booking code", CargoPackage.Literals.CANAL_BOOKING_SLOT__BOOKING_CODE);
		addTextColumn(b, "Notes", CargoPackage.Literals.CANAL_BOOKING_SLOT__NOTES);
		
		b.withAction("Add", (input, ch, sel) -> {
			CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
			CanalBookingSlot cbs = CargoFactory.eINSTANCE.createCanalBookingSlot();
			Command c = AddCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS, cbs);
			ch.handleCommand(c, cbs, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);

		});
		
		b.withAction("Delete", (input, ch, sel) -> {
			if (sel instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) sel;
				if (!ss.isEmpty()) {
					CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
					Command c = RemoveCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS, ss.toList());
					ch.handleCommand(c, canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);
				}
			}
		}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));
		
		detailComposite.addInlineEditor(b.build(CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS));

	}
	/**
	 * Create the editor for the strictBoundaryOffsetDays feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_strictBoundaryOffsetDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__STRICT_BOUNDARY_OFFSET_DAYS));
	}
	/**
	 * Create the editor for the relaxedBoundaryOffsetDays feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_relaxedBoundaryOffsetDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__RELAXED_BOUNDARY_OFFSET_DAYS));
	}
	/**
	 * Create the editor for the arrivalMarginHours feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_arrivalMarginHoursEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__ARRIVAL_MARGIN_HOURS));
	}

	/**
	 * Create the editor for the flexibleBookingAmountNorthbound feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_flexibleBookingAmountNorthboundEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_NORTHBOUND));
	}

	/**
	 * Create the editor for the flexibleBookingAmountSouthbound feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_flexibleBookingAmountSouthboundEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__FLEXIBLE_BOOKING_AMOUNT_SOUTHBOUND));
	}

	/**
	 * Create the editor for the bookingExemptVessels feature on CanalBookings
	 *
	 * @generated
	 */
	protected void add_bookingExemptVesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CANAL_BOOKINGS__BOOKING_EXEMPT_VESSELS));
	}

	/**
	 * Create the editor for the vesselGroupCanalParameters feature on CanalBookings
	 *
	 * @generated NOT
	 */
	protected void add_vesselGroupCanalParametersEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
		b.withShowHeaders(true);
		b.withLabel("Vessel groups");
		b.withContentProvider(new ArrayContentProvider());
		b.withHeightHint(100);

		addTextColumn(b, "Booking Code", MMXCorePackage.Literals.NAMED_OBJECT__NAME);

		addColumn(b, "Vessels", CargoPackage.eINSTANCE.getVesselGroupCanalParameters(), CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP);

		b.withAction("Add", (input, ch, sel) -> {
			CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
			VesselGroupCanalParameters vgcp = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
			Command c = AddCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, vgcp);
			ch.handleCommand(c, vgcp, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS);
		});
		b.withAction("Delete", (input, ch, sel) -> {

			if (sel instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) sel;
				if (!ss.isEmpty()) {
					CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
					Command c = RemoveCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, ss.toList());
					ch.handleCommand(c, canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS);
				}
			}
		}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

		detailComposite.addInlineEditor(b.build(CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS));
	}

	/**
	 * Create the editor for the panamaSeasonalityRecords feature on CanalBookings
	 *
	 * @generated NOT
	 */
	protected void add_panamaSeasonalityRecordsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
		b.withShowHeaders(true);
		b.withLabel("Seasonal waiting days");
		b.withContentProvider(new ArrayContentProvider());
		b.withHeightHint(100);
		
		addSingleReferenceColumn(b, "Booking code", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER);
		
		addNumericColumn(b, "Start day", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_DAY);
		
		addMonthColumn(b, "Start month", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_MONTH);
		
		addNumericColumn(b, "Northbound", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS);
		
		addNumericColumn(b, "Southbound", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS);
		
		b.withAction("Add", (input, ch, sel) -> {
			CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
			PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
			Command c = AddCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS, psr);
			ch.handleCommand(c, psr, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS);

		});
		
		b.withAction("Delete", (input, ch, sel) -> {

			if (sel instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) sel;
				if (!ss.isEmpty()) {
					CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
					Command c = RemoveCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS, ss.toList());
					ch.handleCommand(c, canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS);
				}
			}
		}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));
		
		detailComposite.addInlineEditor(b.build(CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS));
	}

	private void addTextColumn(TabularDataInlineEditor.Builder b, String columnName, EAttribute feature) {
		b.buildColumn(columnName, feature) //
		.withWidth(120) //
		.withRMMaker((ed, rvp) -> new StringAttributeManipulator(feature, ed)) //
		.build();
	}
	
	private void addColumn(TabularDataInlineEditor.Builder b, String name, EClass owner, EReference reference) {
		b.buildColumn(name, reference)
		.withRMMaker((ed, rvp) -> new MultipleReferenceManipulator(reference, ed,
				rvp.getReferenceValueProvider(owner, 
						reference),
				MMXCorePackage.eINSTANCE.getNamedObject_Name())) //
		.withWidth(150)//
		.build();
	}
	
	private void addSingleReferenceColumn(TabularDataInlineEditor.Builder b, String name, EReference reference) {
		b.buildColumn(name, reference)
		.withRMMaker((ed, rvp) -> new SingleReferenceManipulator(reference, rvp, ed)) //
		.withWidth(100)//
		.build();
	}
	
	private void addNumericColumn(TabularDataInlineEditor.Builder b, String columnName, EAttribute feature) {
		b.buildColumn(columnName, feature) //
		.withWidth(120) //
		.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(feature, ed)) //
		.build();
	}
	
	private void addMonthColumn(TabularDataInlineEditor.Builder b, String columnName, EAttribute feature) {
		b.buildColumn(columnName, feature) //
		.withWidth(120) //
		.withRMMaker((ed, rvp) -> new MonthAttributeManipulator(feature, ed)) //
		.build();
	}
	
	private void addLocalDateColumn(TabularDataInlineEditor.Builder b, String columnName, EAttribute feature) {
		b.buildColumn(columnName, feature) //
		.withWidth(120) //
		.withRMMaker((ed, rvp) -> new LocalDateAttributeManipulator(feature, ed)) //
		.build();
	}
	
	private void addCanalEntryColumn(TabularDataInlineEditor.Builder b, String columnName, EAttribute feature, Function<CanalEntry, String> nameProvider) {
		b.buildColumn(columnName, feature) //
		.withWidth(120) //
		.withRMMaker((ed, rvp) -> new CanalEntryAttributeManipulator(null,feature, ed, nameProvider)) //
		.build();
	}
}