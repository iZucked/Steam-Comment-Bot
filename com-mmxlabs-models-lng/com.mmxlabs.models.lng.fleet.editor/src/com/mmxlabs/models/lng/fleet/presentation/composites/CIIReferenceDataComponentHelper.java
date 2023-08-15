/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.models.lng.fleet.CIIReductionFactor;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.FuelEmissionReference;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * A component helper for CII Reference data
 *
 * @generated NOT
 */
public class CIIReferenceDataComponentHelper extends DefaultComponentHelper {

	private static final FleetPackage fp = FleetPackage.eINSTANCE;
	
	public CIIReferenceDataComponentHelper() {
		super(fp.getCIIReferenceData());
		
		addEditor(fp.getCIIReferenceData_FuelEmissions(), createFuelReferenceTable());
		addEditor(fp.getCIIReferenceData_ReductionFactors(), createReductionFactorsTable());
	}
	
	/**
	 * Create the editor for the Fuel Emissions 
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createFuelReferenceTable() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Fuel reference table by type");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(200);

			b.withComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {
					
					final FuelEmissionReference fer1 = (FuelEmissionReference) e1;
					final FuelEmissionReference fer2 = (FuelEmissionReference) e2;
					
					//
					final double v1 = fer1 == null ? -1 : fer1.getCf();
					final double v2 = fer2 == null ? 2 : fer2.getCf();
					//
					return Double.compare(v1, v2);
				}
			});

			b.buildColumn("Name", MMXCorePackage.Literals.NAMED_OBJECT__NAME)
					.withWidth(100)//
					.withRMMaker((ch, rvp) -> new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), ch))
					.build();

			b.buildColumn("ISO Reference", fp.getFuelEmissionReference_IsoReference()) //
					.withWidth(200) //
					.withRMMaker((ch, rvp) -> new StringAttributeManipulator(fp.getFuelEmissionReference_IsoReference(), ch)) //
					.build();

			b.buildColumn("CF (t-CO/t Fuel)", fp.getFuelEmissionReference_Cf()) //
					.withWidth(150) //
					.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getFuelEmissionReference_Cf(), ch)) //
					.build();
			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
				final FuelEmissionReference fer = FleetFactory.eINSTANCE.createFuelEmissionReference();
				final Command c = AddCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_FuelEmissions(), fer);
				ch.handleCommand(c, refData, fp.getCIIReferenceData_FuelEmissions());

			});
			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
				if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
					final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
					final Command c = RemoveCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_FuelEmissions(), ss.toList());
					ch.handleCommand(c, refData, fp.getCIIReferenceData_FuelEmissions());
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(fp.getCIIReferenceData_FuelEmissions());
		};
	}
		
		/**
		 * Create the editor for the Fuel Emissions 
		 *
		 * @generated NOT
		 */
		protected Function<EClass, IInlineEditor> createReductionFactorsTable() {
			return topClass -> {
				final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
				b.withShowHeaders(true);
				b.withLabel("Reduction factors");
				b.withContentProvider(new ArrayContentProvider());
				b.withHeightHint(200);

				b.withComparator(new ViewerComparator() {
					@Override
					public int compare(final Viewer viewer, final Object e1, final Object e2) {
						
						final FuelEmissionReference fer1 = (FuelEmissionReference) e1;
						final FuelEmissionReference fer2 = (FuelEmissionReference) e2;
						
						//
						final double v1 = fer1 == null ? -1 : fer1.getCf();
						final double v2 = fer2 == null ? 2 : fer2.getCf();
						//
						return Double.compare(v1, v2);
					}
				});

				b.buildColumn("Year", fp.getCIIReductionFactor_Year())
						.withWidth(100)//
						.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIReductionFactor_Year(), ch))
						.build();
				
				b.buildColumn("%", fp.getCIIReductionFactor_Percentage()) //
				.withWidth(150) //
				.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(fp.getCIIReductionFactor_Percentage(), ch)) //
				.build();
				
				b.buildColumn("Remark", fp.getCIIReductionFactor_Remark()) //
						.withWidth(200) //
						.withRMMaker((ch, rvp) -> new StringAttributeManipulator(fp.getCIIReductionFactor_Remark(), ch)) //
						.build();

				// Add action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
					final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
					final CIIReductionFactor fer = FleetFactory.eINSTANCE.createCIIReductionFactor();
					final Command c = AddCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_ReductionFactors(), fer);
					ch.handleCommand(c, refData, fp.getCIIReferenceData_ReductionFactors());

				});
				// Delete action
				b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
					if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
						final CIIReferenceData refData = ScenarioModelUtil.getFleetModel((LNGScenarioModel) ch.getModelReference().getInstance()).getCiiReferences();
						final Command c = RemoveCommand.create(ch.getEditingDomain(), refData, fp.getCIIReferenceData_ReductionFactors(), ss.toList());
						ch.handleCommand(c, refData, fp.getCIIReferenceData_ReductionFactors());
					}
				}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

				return b.build(fp.getCIIReferenceData_ReductionFactors());
			};
	}
}