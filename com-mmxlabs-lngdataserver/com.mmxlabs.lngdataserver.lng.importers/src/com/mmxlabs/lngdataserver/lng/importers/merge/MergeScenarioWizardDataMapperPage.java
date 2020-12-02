/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.rcp.common.json.EMFJacksonModule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class MergeScenarioWizardDataMapperPage extends WizardPage implements IScenarioDependent {
	
	private TableViewer mergeTableViewer;
	private NamedObjectGetter namedObjectGetter;
	private ModelGetter modelGetter;
	private EStructuralFeature feature;
	
	public MergeScenarioWizardDataMapperPage(String title, NamedObjectGetter namedObjectGetter, ModelGetter modelGetter, EStructuralFeature feature) {
		super(title, title, null);
		this.namedObjectGetter = namedObjectGetter;
		this.modelGetter = modelGetter;
		this.feature = feature;
	}
	
	public ModelGetter getModelGetter() {
		return this.modelGetter;
	}
	
	public EStructuralFeature getFeature() {
		return this.feature;
	}
	
	public Pair<NamedObjectGetter, List<MergeMapping>> getMergeMappings() {
		List<MergeMapping> mm = Collections.emptyList();
		if (mergeTableViewer.getInput() instanceof List) {
			mm = (List<MergeMapping>)mergeTableViewer.getInput();
		}
		return Pair.of(namedObjectGetter, mm);
	}
	
	@Override
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE | SWT.BORDER);
		mergeTableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		container.setLayout(new FillLayout());	
		initialize();
		dialogChanged();
		setControl(container);
	}

	private void initialize() {

	}

	private void dialogChanged() {
		updateStatus(null);
	}

	private void updateStatus(final String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	public void update(ScenarioInstance target, ScenarioInstance source) {
		//TODO: clean out columns.
		
		if (source != null && target != null) {
			List<String> sourceContracts = getItemNames(source, this.namedObjectGetter);
			List<String> targetContracts = getItemNames(target, this.namedObjectGetter);
			List<String> infos = getDiff(source, target, this.namedObjectGetter);
			
			Table table = mergeTableViewer.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			mergeTableViewer.setContentProvider(ArrayContentProvider.getInstance().getInstance());

			String[] titles = { "Source:"+source.getName(), "Target:"+target.getName(), "Info" };

			for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
				TableColumn column = new TableColumn(table, SWT.NONE);
				column.setWidth(200);
				column.setText(titles[loopIndex]);
				
				if (loopIndex == 1) {
					TableViewerColumn viewerColumn = new TableViewerColumn(this.mergeTableViewer, column);
					viewerColumn.setLabelProvider(new ColumnLabelProvider());
					viewerColumn.setEditingSupport(new EditingSupport(mergeTableViewer) {
						
						List<String> mappableContracts = targetContracts;
						
						String[] valuesAddIgnore = new String[2+targetContracts.size()];
						{
							valuesAddIgnore[0] = "Add";
							valuesAddIgnore[1] = "Ignore";
							int i = 2;
							for (var v : targetContracts) {
								valuesAddIgnore[i++] = v;
							}
						}
						String[] valuesOverwrite = new String[1+targetContracts.size()];
						{
							valuesOverwrite[0] = "Overwrite existing";
							int i = 1;
							for (var v : targetContracts) {
								valuesOverwrite[i++] = v;
							}
						}
						
						boolean isMappable(Object element) {
							if (element instanceof MergeMapping) {
								MergeMapping cm = (MergeMapping)element;
								if (mappableContracts.contains(cm.sourceName)) {
									return true;
								}
							}
							return false;
						}
						
						String[] getValues(Object element) {
							if (isMappable(element)) {
								return this.valuesOverwrite;
							}
							else {
								return this.valuesAddIgnore;
							}
						}
						
						@Override
						protected CellEditor getCellEditor(Object element) {
							String[] values = getValues(element);
							return new ComboBoxCellEditor(table, values);
						}

						@Override
						protected boolean canEdit(Object element) {
							return true;
						}

						@Override
						protected Object getValue(Object element) {
							String values[] = getValues(element);
							for (int i = 0; i < values.length; i++) {
								if (((MergeMapping)element).targetName.equals(values[i])) {
									return i;
								}
							}
							return null;
						}

						@Override
						protected void setValue(Object element, Object index) {
							String values[] = getValues(element);
							((MergeMapping)element).targetName = values[(Integer)index];
							getViewer().update(element, null);
						}			
					});
				}
			}

			mergeTableViewer.setLabelProvider(new ITableLabelProvider() {

				
				@Override
				public void addListener(ILabelProviderListener listener) {
				}

				@Override
				public void dispose() {
				}

				@Override
				public boolean isLabelProperty(Object element, String property) {
					return false;
				}

				@Override
				public void removeListener(ILabelProviderListener listener) {
				}

				@Override
				public Image getColumnImage(Object element, int columnIndex) {
					return null;
				}

				@Override
				public String getColumnText(Object element, int columnIndex) {
					if (element instanceof MergeMapping) {
						MergeMapping cm = (MergeMapping)element;
						switch (columnIndex) {
						case 0:
							return cm.sourceName;
						case 1:
							return cm.targetName;
						case 2:
							return cm.info;
						}
					}
					return null;
				}
				
			});
			
			mergeTableViewer.setInput(createModel(sourceContracts, targetContracts, infos));
		}
	}

	private List<MergeMapping> createModel(List<String> sourceContracts, List<String> targetContracts, List<String> infos) {
		List<MergeMapping> cms = new ArrayList<>();
		for (int i = 0; i < sourceContracts.size(); i++) {
			MergeMapping cm = new MergeMapping();
			cm.sourceName = sourceContracts.get(i);
			if (targetContracts.contains(cm.sourceName)) {
				cm.targetName = cm.sourceName;
			}
			else {
				cm.targetName = "Add";
			}
			cm.info = infos.get(i);
			cms.add(cm);
		}
		return cms;
	}
	
	protected List<String> getDiff(ScenarioInstance source, ScenarioInstance target, NamedObjectGetter itemGetter) {
		List<String> diffs = new ArrayList<>();
		final ScenarioModelRecord sourceModelRecord = SSDataManager.Instance.getModelRecord(source);
		try (IScenarioDataProvider scenarioDataProvider = sourceModelRecord.aquireScenarioDataProvider("MergeScenarioWizardContractMapperPage.getDiff1")) {
			final LNGScenarioModel sm = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			final ScenarioModelRecord targetModelRecord = SSDataManager.Instance.getModelRecord(target);
			try (IScenarioDataProvider scenarioDataProvider2 = targetModelRecord.aquireScenarioDataProvider("MergeScenarioWizardContractMapperPage.getDiff2")) {
				final LNGScenarioModel tm = scenarioDataProvider2.getTypedScenario(LNGScenarioModel.class);

				if (sm != null && tm != null) {
					List<?> emfObjectsS = itemGetter.getNamedObjects(sm);
					List<?> emfObjectsT = itemGetter.getNamedObjects(tm);
					for (var o : emfObjectsS) {
						String name = ((NamedObject)o).getName();
						Optional<?> oT = emfObjectsT.stream().filter(ot -> ((NamedObject)ot).getName().equals(name)).findFirst();
						if (oT.isPresent()) {
							if (!isJSONEqual((EObject)o, (EObject)oT.get())) {
								diffs.add("Source different from target version.");
							}
							else {
								diffs.add("");
							}
						}
						else {
							diffs.add("Not present in target.");
						}
					}
				}
			}
		}
		return diffs;
	}

	private boolean isJSONEqual(EObject o1, EObject o2) {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new EMFJacksonModule());
		
		try {
			String o1Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o1);

			String o2Str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o2);

			boolean equal = o1Str.equals(o2Str);
			
//			if (!equal) {
//				System.out.println("o1Str = "+o1.toString()+"o2Str = "+o2.toString());
//				String[] o1Lines = o1Str.split("\n");
//				String[] o2Lines = o2Str.split("\n");
//				if (o1Lines.length != o2Lines.length) {
//					System.out.println("o1 lines = "+o1Lines.length+" o2 lines = "+o2Lines.length);
//				}
//
//				for (int i = 0; i < Math.max(o1Lines.length, o2Lines.length); i++) {	
//					if (i < o1Lines.length) {
//						if (!o2Str.contains(o1Lines[i])) {
//							System.out.println("Diff "+i+"(1):\t"+o1Lines[i]);
//						}
//					}
//					if (i < o2Lines.length) {
//						if (!o1Str.contains(o2Lines[i])) {
//							System.out.println("Diff "+i+"(2):\t"+o2Lines[i]);
//						}
//					}
//				}
//			}
			return equal; 
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private List<String> getItemNames(ScenarioInstance source, NamedObjectGetter namedItemsGetter) {
		List<String> names = new ArrayList<>();
		final ScenarioModelRecord sourceModelRecord = SSDataManager.Instance.getModelRecord(source);
		try (IScenarioDataProvider scenarioDataProvider = sourceModelRecord.aquireScenarioDataProvider("MergeScenarioWizardContractMapperPage.getItems")) {
			final LNGScenarioModel sm = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);

			if (sm != null) {
				List<? extends NamedObject> namedObjects = namedItemsGetter.getNamedObjects(sm);
				for (NamedObject no : namedObjects) {
					names.add(no.getName());
				}
			}
		}
		return names;
	}
}
