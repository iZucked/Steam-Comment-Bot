package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;

public class MergeScenarioWizardVesselAvailabilityMapperPage extends MergeScenarioWizardDataMapperPage {
	Button checkBox;

	public MergeScenarioWizardVesselAvailabilityMapperPage(String title) {
		super(title, null, s -> ScenarioModelUtil.getCargoModel(s), CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES);
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE | SWT.BORDER);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		mergeTableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		mergeTableViewer.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		checkBox = new Button(composite, SWT.CHECK);
		checkBox.setLayoutData(GridDataFactory.swtDefaults().create());
		checkBox.setText("Take earliest/latest start end dates and update target fleet charters.");
		dialogChanged();
		setControl(composite);
	}

	@Override
	public void merge(CompoundCommand cmd, MergeHelper mergeHelper) throws Exception {
		Pair<EObjectListGetter, List<MergeMapping>> mapping = this.getMergeMappings();
		ModelGetter mg = this.getModelGetter();
		EStructuralFeature feature = this.getFeature();
		mergeHelper.merge(cmd, mapping, eO -> getName(eO), mg, feature);
		mergeHelper.updateVesselAvailabilityStartEndDates(cmd);
	}

	private Pair<EObjectListGetter, List<MergeMapping>> getMergeMappings() {
		List<MergeMapping> mm = Collections.emptyList();
		if (mergeTableViewer.getInput() instanceof List) {
			mm = (List<MergeMapping>) mergeTableViewer.getInput();
		}
		return Pair.of(sm -> sm.getCargoModel().getVesselAvailabilities(), mm);
	}

	@Override
	protected List<String> getItemNames(LNGScenarioModel sm, NamedObjectListGetter namedItemsGetter) {
		List<String> names = new ArrayList<>();
		if (sm != null) {
			List<VesselAvailability> vas = sm.getCargoModel().getVesselAvailabilities();
			for (var va : vas) {
				if (va.getVessel() != null) {
					names.add(va.getVessel().getName());
				} else {
					names.add("Unknown charter vessel.");
				}
			}
		}
		return names;
	}

	@Override
	protected String getName(Object o) {
		if (o instanceof VesselAvailability) {
			var va = (VesselAvailability) o;
			if (va.getVessel() == null) {
				return "Unknown";
			} else {
				return va.getVessel().getName() + ":" + Integer.toString(va.getCharterNumber());
			}
		} else {
			return super.getName(o);
		}
	}

	@Override
	protected List<? extends EObject> getEObjects(LNGScenarioModel sm) {
		List<? extends EObject> emfObjects = sm.getCargoModel().getVesselAvailabilities();
		return emfObjects;
	}
}
