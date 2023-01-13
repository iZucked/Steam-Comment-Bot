/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.presentation.composites;

import com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand;
import com.mmxlabs.models.lng.pricing.PanamaTariffV2;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;
import com.mmxlabs.models.ui.tabular.SimpleCellRenderer;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

/**
 * A component helper for PanamaCanalTariff instances
 *
 * @generated
 */
public class PanamaCanalTariffComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<>();

	/**
	 * add editors to a composite, using PanamaCanalTariff as the supertype
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, PricingPackage.Literals.PANAMA_CANAL_TARIFF);
	}

	/**
	 * Create the editors for features on this class directly, and superclass'
	 * features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers)
			helper.addEditorsToComposite(detailComposite, topClass);
		add_bandsEditor(detailComposite, topClass);
		add_annualTariffEditor(detailComposite, topClass);
		add_markupRateEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the bands feature on PanamaCanalTariff
	 *
	 * @generated NOT
	 */
	protected void add_bandsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
		b.withShowHeaders(true);
		b.withLabel("Pricing bands per m³ of capacity (before Jan 2023)");
		b.withContentProvider(new ArrayContentProvider());

		b.withComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				final PanamaCanalTariffBand b1 = (PanamaCanalTariffBand) e1;
				final PanamaCanalTariffBand b2 = (PanamaCanalTariffBand) e2;

				final int v1 = b1 == null ? -1 : (b1.isSetBandEnd() ? b1.getBandEnd() : Integer.MAX_VALUE);
				final int v2 = b2 == null ? 2 : (b2.isSetBandEnd() ? b2.getBandEnd() : Integer.MAX_VALUE);

				return Integer.compare(v1, v2);
			}
		});

		b.buildColumn("", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BAND_START).withRenderer(new SimpleCellRenderer() {

			@Override
			public @Nullable String render(Object element) {
				String label = "";
				if (element instanceof PanamaCanalTariffBand band) {
					if (!band.isSetBandStart()) {
						label = String.format("First %,d", band.getBandEnd());
					} else if (!band.isSetBandEnd()) {
						label = String.format("Over %,d", band.getBandStart());
					} else {
						final int diff = band.getBandEnd() - band.getBandStart();
						label = String.format("Next %,d", diff);
					}
				}
				return label;
			}
		}) //
				.withWidth(100)//
				.build();

		b.buildColumn("Laden", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF) //
				.withWidth(75) //
				.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__LADEN_TARIFF, ed)) //
				.build();

		b.buildColumn("Ballast", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF) //
				.withWidth(75) //
				.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_TARIFF, ed)) //
				.build();

		b.buildColumn("Ballast Round-trip", PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF) //
				.withWidth(100) //
				.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.PANAMA_CANAL_TARIFF_BAND__BALLAST_ROUNDTRIP_TARIFF, ed)) //
				.build();

		detailComposite.addInlineEditor(b.build(PricingPackage.Literals.PANAMA_CANAL_TARIFF__BANDS));
	}

	/**
	 * Create the editor for the new 2023+ fee structure feature on
	 * PanamaCanalTariff
	 *
	 * @generated NOT
	 */
	protected void add_annualTariffEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
		b.withShowHeaders(true);
		b.withLabel("Annual pricing per m³ of capacity (after Jan 2023)\nUse \"Reset Canal Costs\" from the \"Data > LiNGO DB\" menus to import data");
		b.withContentProvider(new ArrayContentProvider());

		b.withComparator(new ViewerComparator() {
			@Override
			public int compare(final Viewer viewer, final Object e1, final Object e2) {

				final PanamaTariffV2 b1 = (PanamaTariffV2) e1;
				final PanamaTariffV2 b2 = (PanamaTariffV2) e2;

				final LocalDate v1 = b1.getEffectiveFrom();
				final LocalDate v2 = b2.getEffectiveFrom();

				if (v1 == null) {
					return -1;
				}
				if (v2 == null) {
					return 1;
				}
				return v1.compareTo(v2);
			}
		});

		b.buildColumn("Effective from", PricingPackage.Literals.PANAMA_TARIFF_V2__EFFECTIVE_FROM) //
				.withWidth(100) //
				.withRMMaker((ed, rvp) -> new LocalDateAttributeManipulator(PricingPackage.Literals.PANAMA_TARIFF_V2__EFFECTIVE_FROM, ed)) //
				.build();

		b.buildColumn("Fixed Fee", PricingPackage.Literals.PANAMA_TARIFF_V2__FIXED_FEE) //
				.withWidth(75) //
				.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.PANAMA_TARIFF_V2__FIXED_FEE, ed)) //
				.build();

		b.buildColumn("Capacity Tariff", PricingPackage.Literals.PANAMA_TARIFF_V2__CAPACITY_TARIFF) //
				.withWidth(100) //
				.withRMMaker((ed, rvp) -> new NumericAttributeManipulator(PricingPackage.Literals.PANAMA_TARIFF_V2__CAPACITY_TARIFF, ed)) //
				.build();

		detailComposite.addInlineEditor(b.build(PricingPackage.Literals.PANAMA_CANAL_TARIFF__ANNUAL_TARIFFS));
	}

	/**
	 * Create the editor for the markupRate feature on PanamaCanalTariff
	 *
	 * @generated
	 */
	protected void add_markupRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, PricingPackage.Literals.PANAMA_CANAL_TARIFF__MARKUP_RATE));
	}
}