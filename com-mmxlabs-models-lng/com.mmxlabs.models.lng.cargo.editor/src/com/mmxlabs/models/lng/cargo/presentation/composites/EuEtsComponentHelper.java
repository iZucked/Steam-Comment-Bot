package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.function.Function;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EmissionsCoveredTable;
import com.mmxlabs.models.lng.cargo.EuEtsModel;
import com.mmxlabs.models.lng.cargo.editor.SlotExpressionWrapper;
import com.mmxlabs.models.lng.port.ui.editors.PortGroupContentsEditor;
import com.mmxlabs.models.lng.pricing.editor.PriceExpressionWithFormulaeCurvesInlineEditor;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;
import com.mmxlabs.models.ui.tabular.TabularDataInlineEditor;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * A component helper for EU ETS
 *
 * @generated NOT
 */
public class EuEtsComponentHelper extends DefaultComponentHelper {

	private static final CargoPackage cp = CargoPackage.eINSTANCE;

	public EuEtsComponentHelper() {
		super(cp.getEuEtsModel());

		addEditor(CargoPackage.Literals.EU_ETS_MODEL__EUA_PRICE_EXPRESSION, topClass -> {
			return new SlotExpressionWrapper(new PriceExpressionWithFormulaeCurvesInlineEditor(CargoPackage.Literals.EU_ETS_MODEL__EUA_PRICE_EXPRESSION));
		});
		addEditor(CargoPackage.Literals.EU_ETS_MODEL__EMISSIONS_COVERED, createReductionFactorsTable());
	}

	/**
	 * Create the editor for the Reduction Factors
	 *
	 * @generated NOT
	 */
	protected Function<EClass, IInlineEditor> createReductionFactorsTable() {
		return topClass -> {
			final TabularDataInlineEditor.Builder b = new TabularDataInlineEditor.Builder();
			b.withShowHeaders(true);
			b.withLabel("Reduction factors");
			b.withContentProvider(new ArrayContentProvider());
			b.withHeightHint(150);

			b.withComparator(new ViewerComparator() {
				@Override
				public int compare(final Viewer viewer, final Object e1, final Object e2) {

					final EmissionsCoveredTable rf1 = (EmissionsCoveredTable) e1;
					final EmissionsCoveredTable rf2 = (EmissionsCoveredTable) e2;

					//
					final int v1 = rf1 == null ? -1 : rf1.getStartYear();
					final int v2 = rf2 == null ? 2 : rf2.getStartYear();
					//
					return Integer.compare(v1, v2);
				}
			});

			b.buildColumn("Year", cp.getEmissionsCoveredTable_StartYear()).withWidth(70)//
					.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(cp.getEmissionsCoveredTable_StartYear(), ch)).build();

			b.buildColumn("%", cp.getEmissionsCoveredTable_EmissionsCovered()) //
					.withWidth(50) //
					.withRMMaker((ch, rvp) -> new NumericAttributeManipulator(cp.getEmissionsCoveredTable_EmissionsCovered(), ch)) //
					.build();

			// Add action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled), (input, ch, sel) -> {
				final EuEtsModel refData = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getEuEtsModel();
				final EmissionsCoveredTable fer = CargoFactory.eINSTANCE.createEmissionsCoveredTable();
				final Command c = AddCommand.create(ch.getEditingDomain(), refData, CargoPackage.Literals.EU_ETS_MODEL__EMISSIONS_COVERED, fer);
				ch.handleCommand(c, refData, CargoPackage.Literals.EU_ETS_MODEL__EMISSIONS_COVERED);

			});
			// Delete action
			b.withAction(CommonImages.getImageDescriptor(IconPaths.Delete, IconMode.Enabled), (input, ch, sel) -> {
				if (sel instanceof IStructuredSelection ss && !ss.isEmpty()) {
					final EuEtsModel refData = ScenarioModelUtil.getCargoModel((LNGScenarioModel) ch.getModelReference().getInstance()).getEuEtsModel();
					final Command c = RemoveCommand.create(ch.getEditingDomain(), refData, cp.getEuEtsModel_EmissionsCovered(), ss.toList());
					ch.handleCommand(c, refData, cp.getEuEtsModel_EmissionsCovered());
				}
			}, false, (btn, sel) -> btn.setEnabled(!sel.isEmpty()));

			return b.build(cp.getEuEtsModel_EmissionsCovered());
		};
	}
}
