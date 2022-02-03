/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.LocalDateTimeHolder;
import com.mmxlabs.models.lng.analytics.PartialCaseRowOptions;
import com.mmxlabs.models.lng.analytics.ui.editors.FuelChoiceInlineEditorHelper;
import com.mmxlabs.models.lng.analytics.ui.editors.RouteChoiceInlineEditorHelper;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;

/**
 * A component helper for PartialCaseRowOptions instances
 *
 * @generated
 */
public class PartialCaseRowOptionsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public PartialCaseRowOptionsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public PartialCaseRowOptionsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}

	/**
	 * add editors to a composite, using PartialCaseRowOptions as the supertype
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass'
	 * features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_ladenRoutesEditor(detailComposite, topClass);
		add_ballastRoutesEditor(detailComposite, topClass);
		add_ladenFuelChoicesEditor(detailComposite, topClass);
		add_ballastFuelChoicesEditor(detailComposite, topClass);
		add_loadDatesEditor(detailComposite, topClass);
		add_dischargeDatesEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the ladenRoutes feature on PartialCaseRowOptions
	 *
	 * @generated NOT
	 */
	protected void add_ladenRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		RouteChoiceInlineEditorHelper.createRouteChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_ROUTES, detailComposite);
	}

	/**
	 * Create the editor for the ballastRoutes feature on PartialCaseRowOptions
	 *
	 * @generated NOT
	 */
	protected void add_ballastRoutesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		RouteChoiceInlineEditorHelper.createRouteChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__BALLAST_ROUTES, detailComposite);
	}

	/**
	 * Create the editor for the ladenFuelChoices feature on PartialCaseRowOptions
	 *
	 * @generated NOT
	 */
	protected void add_ladenFuelChoicesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		FuelChoiceInlineEditorHelper.createFuelChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LADEN_FUEL_CHOICES, detailComposite);
	}

	/**
	 * Create the editor for the ballastFuelChoices feature on PartialCaseRowOptions
	 *
	 * @generated NOT
	 */
	protected void add_ballastFuelChoicesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		FuelChoiceInlineEditorHelper.createFuelChoiceEditors(AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__BALLAST_FUEL_CHOICES, detailComposite);
	}

	/**
	 * Create the editor for the loadDates feature on PartialCaseRowOptions
	 *
	 * @generated NOT
	 */
	protected void add_loadDatesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		createDatesEditor(detailComposite,  AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__LOAD_DATES, "Dates");
	}

	/**
	 * Create the editor for the dischargeDates feature on PartialCaseRowOptions
	 *
	 * @generated NOT
	 */
	protected void add_dischargeDatesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		createDatesEditor(detailComposite,  AnalyticsPackage.Literals.PARTIAL_CASE_ROW_OPTIONS__DISCHARGE_DATES, "Dates");
	}
	
	/**
	 * @generated NOT
	 */
	protected void createDatesEditor(final IInlineEditorContainer detailComposite, final EStructuralFeature feature, String label) {

		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
		b.withShowHeaders(false);
		b.withLabel(label);
		b.withHeightHint(100);
		b.withContentProvider(new ArrayContentProvider());

		b.withComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				final LocalDateTimeHolder b1 = (LocalDateTimeHolder) e1;
				final LocalDateTimeHolder b2 = (LocalDateTimeHolder) e2;
				if (b1.getDateTime() == null) {
					return -1;
				}
				if (b2.getDateTime() == null) {
					return 1;
				}
				
				return b1.getDateTime().compareTo(b2.getDateTime());
			}
		});

		b.buildColumn("", feature)
			.withWidth(100)  //
			.withRMMaker((ch, rvp) -> new LocalDateTimeAttributeManipulator(AnalyticsPackage.Literals.LOCAL_DATE_TIME_HOLDER__DATE_TIME, ch)) //
		.build();

		b.withAction("Add", (input, ch, sel) -> {
			PartialCaseRowOptions options = (PartialCaseRowOptions) input;
			LocalDateTimeHolder holder = AnalyticsFactory.eINSTANCE.createLocalDateTimeHolder();
			holder.setDateTime(LocalDate.now().atStartOfDay());
			Command c = AddCommand.create(ch.getEditingDomain(), options, feature, holder);
			ch.handleCommand(c, options, feature);

		});
		b.withAction("Delete", (input, ch, sel) -> {

			if (sel instanceof IStructuredSelection ss) {
				if (!ss.isEmpty()) {
					PartialCaseRowOptions options = (PartialCaseRowOptions) input;
					Command c = RemoveCommand.create(ch.getEditingDomain(), options, feature, ss.toList());
					ch.handleCommand(c, options, feature);
				}
			}
		}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

		detailComposite.addInlineEditor(b.build(feature));

	}
}