package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.AVesselSet;

public class MergeScenarioWizardRouteCostMapperPage extends MergeScenarioWizardDataMapperPage {

	public MergeScenarioWizardRouteCostMapperPage(String title) {
		super(title, null, s -> ScenarioModelUtil.findReferenceModel(s).getCostModel(), PricingPackage.Literals.COST_MODEL__ROUTE_COSTS);
	}

	private Pair<EObjectListGetter, List<MergeMapping>> getMergeMappings() {
		List<MergeMapping> mm = Collections.emptyList();
		if (mergeTableViewer.getInput() instanceof List) {
			mm = (List<MergeMapping>) mergeTableViewer.getInput();
		}
		return Pair.of(s -> ScenarioModelUtil.findReferenceModel(s).getCostModel().getRouteCosts(), mm);
	}

	@Override
	protected List<String> getItemNames(LNGScenarioModel sm, NamedObjectListGetter namedItemsGetter) {
		List<String> names = new ArrayList<>();
		if (sm != null) {
			List<RouteCost> routeCosts = ScenarioModelUtil.findReferenceModel(sm).getCostModel().getRouteCosts();
			for (var rc : routeCosts) {
				if (rc.getRouteOption() != null || rc.getVessels() != null) {
					names.add(this.getName(rc));
				} else {
					names.add("Unknown route cost.");
				}
			}
		}
		return names;
	}

	@Override
	protected String getName(Object o) {
		if (o instanceof RouteCost) {
			var rc = (RouteCost) o;
			String name = "";
			if (rc.getRouteOption() != null) {
				name += rc.getRouteOption().getName();
			}
			if (rc.getVessels() != null) {
				if (rc.getVessels().size() > 0) {
					name += ":(";
				}
				boolean firstV = true;
				for (AVesselSet<Vessel> v : rc.getVessels()) {
					if (!firstV) {
						name += ",";
					}
					name += v.getName();
					firstV = false;
				}
				if (rc.getVessels().size() > 0) {
					name += ")";
				}
			}
			return name;
		} else {
			return super.getName(o);
		}
	}

	@Override
	protected List<? extends EObject> getEObjects(LNGScenarioModel sm) {
		List<? extends EObject> emfObjects = ScenarioModelUtil.findReferenceModel(sm).getCostModel().getRouteCosts();
		return emfObjects;
	}
	
	@Override
	public void merge(CompoundCommand cmd, MergeHelper mergeHelper) throws Exception {
		Pair<EObjectListGetter, List<MergeMapping>> mapping = this.getMergeMappings();
		ModelGetter mg = this.getModelGetter();
		EStructuralFeature feature = this.getFeature();
		mergeHelper.merge(cmd, mapping, eO -> getName(eO), mg, feature);
	}
}
