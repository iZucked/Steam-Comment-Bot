/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters;
import com.mmxlabs.models.lng.port.CanalEntry;
import com.mmxlabs.models.lng.port.ui.editors.CanalEntryAttributeManipulator;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.MonthAttributeManipulator;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * A component helper for CanalBookings instances
 *
 * @generated NOT
 */
public class CanalBookingsComponentHelper extends DefaultComponentHelper {

	public CanalBookingsComponentHelper() {
		super(CargoPackage.Literals.CANAL_BOOKINGS);

		// Ignore other features
//		add_canalBookingSlotsEditor(detailComposite, topClass);
//		add_vesselGroupCanalParametersEditor(detailComposite, topClass);
//		add_panamaSeasonalityRecordsEditor(detailComposite, topClass);
//		add_arrivalMarginHoursEditor(detailComposite, topClass);

		addEditor(CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS, createCanalBookingSlotsEditor());
		addEditor(CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, createVesselGroupCanalParametersEditor());
		addEditor(CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS, createPanamaSeasonalityRecordsEditor());
	}

	/**
	 * Create the editor for the canalBookingSlots feature on CanalBookings
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createCanalBookingSlotsEditor() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Bookings");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(200);

			addLocalDateColumn(b, "Date", CargoPackage.Literals.CANAL_BOOKING_SLOT__BOOKING_DATE);
			addCanalEntryColumn(b, "Direction", CargoPackage.Literals.CANAL_BOOKING_SLOT__CANAL_ENTRANCE, PortModelLabeller::getDirection);
			addSingleReferenceColumn(b, "Vessel", CargoPackage.Literals.CANAL_BOOKING_SLOT__VESSEL);
			addSingleReferenceColumn(b, "Booking code", CargoPackage.Literals.CANAL_BOOKING_SLOT__BOOKING_CODE);
			addTextColumn(b, "Notes", CargoPackage.Literals.CANAL_BOOKING_SLOT__NOTES);

			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				final CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
				final CanalBookingSlot cbs = CargoFactory.eINSTANCE.createCanalBookingSlot();
				final Command c = AddCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS, cbs);
				ch.handleCommand(c, cbs, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);

			});

			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
				if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
					final CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
					final Command c = DeleteCommand.create(ch.getEditingDomain(), ss.toList());
					ch.handleCommand(c, canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(CargoPackage.Literals.CANAL_BOOKINGS__CANAL_BOOKING_SLOTS);
		};

	}

	/**
	 * Create the editor for the vesselGroupCanalParameters feature on CanalBookings
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createVesselGroupCanalParametersEditor() {

		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Vessel groups");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(100);

			addTextColumn(b, "Booking Code", MMXCorePackage.Literals.NAMED_OBJECT__NAME);

			addColumn(b, "Vessels", CargoPackage.eINSTANCE.getVesselGroupCanalParameters(), CargoPackage.Literals.VESSEL_GROUP_CANAL_PARAMETERS__VESSEL_GROUP);

			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				final CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
				final VesselGroupCanalParameters vgcp = CargoFactory.eINSTANCE.createVesselGroupCanalParameters();
				final Command c = AddCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, vgcp);
				ch.handleCommand(c, vgcp, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS);
			});
			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {

				if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
					final CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
					final Command c = RemoveCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS, ss.toList());
					ch.handleCommand(c, canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS);
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(CargoPackage.Literals.CANAL_BOOKINGS__VESSEL_GROUP_CANAL_PARAMETERS);
		};
	}

	/**
	 * Create the editor for the panamaSeasonalityRecords feature on CanalBookings
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createPanamaSeasonalityRecordsEditor() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Seasonal waiting days");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(100);

			addSingleReferenceColumn(b, "Booking code", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__VESSEL_GROUP_CANAL_PARAMETER);

			addNumericColumn(b, "Start day", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_DAY);

			addMonthColumn(b, "Start month", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__START_MONTH);

			addNumericColumn(b, "Northbound", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__NORTHBOUND_WAITING_DAYS);

			addNumericColumn(b, "Southbound", CargoPackage.Literals.PANAMA_SEASONALITY_RECORD__SOUTHBOUND_WAITING_DAYS);

			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				final CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
				final PanamaSeasonalityRecord psr = CargoFactory.eINSTANCE.createPanamaSeasonalityRecord();
				final Command c = AddCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS, psr);
				ch.handleCommand(c, psr, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS);

			});

			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {

				if (sel instanceof final IStructuredSelection ss && !ss.isEmpty()) {
					final CanalBookings canalBookings = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCanalBookings();
					final Command c = RemoveCommand.create(ch.getEditingDomain(), canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS, ss.toList());
					ch.handleCommand(c, canalBookings, CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS);
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(CargoPackage.Literals.CANAL_BOOKINGS__PANAMA_SEASONALITY_RECORDS);
		};
	}

	private void addTextColumn(final TabularDataInlineEditor.Builder b, final String columnName, final EAttribute feature) {
		b.buildColumn(columnName, feature) //
				.withWidth(120) //
				.withRMMaker((ed, rvp) -> new StringAttributeManipulator(feature, ed)) //
				.build();
	}

	private void addColumn(final TabularDataInlineEditor.Builder b, final String name, final EClass owner, final EReference reference) {
		b.buildColumn(name, reference)
				.withRMMaker((ed, rvp) -> new MultipleReferenceManipulator(reference, ed, rvp.getReferenceValueProvider(owner, reference), MMXCorePackage.eINSTANCE.getNamedObject_Name())) //
				.withWidth(150)//
				.build();
	}

	private void addSingleReferenceColumn(final TabularDataInlineEditor.Builder b, final String name, final EReference reference) {
		b.buildColumn(name, reference).withRMMaker((ed, rvp) -> new SingleReferenceManipulator(reference, rvp, ed)) //
				.withWidth(100)//
				.build();
	}

	private void addNumericColumn(final TabularDataInlineEditor.Builder b, final String columnName, final EAttribute feature) {
		b.buildColumn(columnName, feature) //
				.withWidth(120) //
				.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(feature, ed)) //
				.build();
	}

	private void addMonthColumn(final TabularDataInlineEditor.Builder b, final String columnName, final EAttribute feature) {
		b.buildColumn(columnName, feature) //
				.withWidth(120) //
				.withRMMaker((ed, rvp) -> new MonthAttributeManipulator(feature, ed)) //
				.build();
	}

	private void addLocalDateColumn(final TabularDataInlineEditor.Builder b, final String columnName, final EAttribute feature) {
		b.buildColumn(columnName, feature) //
				.withWidth(120) //
				.withRMMaker((ed, rvp) -> new LocalDateAttributeManipulator(feature, ed)) //
				.build();
	}

	private void addCanalEntryColumn(final TabularDataInlineEditor.Builder b, final String columnName, final EAttribute feature, final Function<CanalEntry, String> nameProvider) {
		b.buildColumn(columnName, feature) //
				.withWidth(120) //
				.withRMMaker((ed, rvp) -> new CanalEntryAttributeManipulator(null, feature, ed, nameProvider)) //
				.build();
	}
}