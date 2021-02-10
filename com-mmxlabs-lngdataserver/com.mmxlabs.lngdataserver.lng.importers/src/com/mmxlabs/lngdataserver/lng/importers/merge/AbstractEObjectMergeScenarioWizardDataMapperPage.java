package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public abstract class AbstractEObjectMergeScenarioWizardDataMapperPage extends MergeScenarioWizardDataMapperPage {

	protected AbstractEObjectMergeScenarioWizardDataMapperPage(String title, ModelGetter modelGetter, EStructuralFeature feature) {
		super(title, null, modelGetter, feature);
	}

	private Pair<EObjectListGetter, List<MergeMapping>> getEObjectMergeMappings() {
		List<MergeMapping> mm = Collections.emptyList();
		if (mergeTableViewer.getInput() instanceof List<?>) {
			mm = (List<MergeMapping>) mergeTableViewer.getInput();
		}
		return Pair.of(s -> getEObjects(s), mm);
	}
	
	@Override
	public void merge(CompoundCommand cmd, MergeHelper mergeHelper) throws Exception {
		Pair<EObjectListGetter, List<MergeMapping>> mapping = this.getEObjectMergeMappings();
		ModelGetter mg = this.getModelGetter();
		EStructuralFeature feature = this.getFeature();
		mergeHelper.merge(cmd, mapping, eO -> getName(eO), mg, feature);
	}

	@Override
	protected abstract List<String> getItemNames(LNGScenarioModel sm, NamedObjectListGetter namedItemsGetter);

	@Override
	protected abstract List<? extends EObject> getEObjects(LNGScenarioModel sm);
}
