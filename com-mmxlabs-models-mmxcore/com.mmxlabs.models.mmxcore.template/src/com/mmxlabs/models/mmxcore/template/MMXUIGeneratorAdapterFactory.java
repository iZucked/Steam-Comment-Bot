/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.template;

import java.util.List;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.codegen.jet.JETEmitter;
import org.eclipse.emf.codegen.jet.JETException;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.Monitor;

import com.mmxlabs.models.mmxcore.templates.editor.ComponentHelper;

/**
 * This should generate UI code in the editor, hopefully
 * 
 * @author hinton
 * 
 */
public class MMXUIGeneratorAdapterFactory extends GenBaseGeneratorAdapter {
	public final static String CLASSPATH_VARIABLE_NAME = "MMX_UI_GENERATOR";

	public final static String TEMPLATE_LOCATION = MMXUIGeneratorPlugin.INSTANCE.getBaseURL().toString() + "templates";

	public MMXUIGeneratorAdapterFactory() {
	}

	@Override
	public boolean canGenerate(Object object, Object projectType) {
		if (projectType.equals(GenBaseGeneratorAdapter.EDITOR_PROJECT_TYPE)) {

			GenPackage genPackage = (GenPackage) object;
			GenModel genModel = genPackage.getGenModel();

			// cannot generate without dynamic templates; compiled form of
			// templates is missing from project as well
			// (should I add JET nature?)
			if (genModel.isDynamicTemplates()) {
				return super.canGenerate(object, projectType);
			}
		}
		return false;
	}

	@Override
	protected Diagnostic generateEditor(Object object, Monitor monitor) {
		final GenPackage genPackage = (GenPackage) object;
		final GenModel genModel = genPackage.getGenModel();

		monitor.beginTask("Generating detail composites", genPackage.getGenClasses().size());

		final JETEmitter emitter = createJETEmitter(new JETEmitterDescriptor("editor/ComponentHelper.javajet", ComponentHelper.class.getName()));
		for (final GenClass genClass : genPackage.getGenClasses()) {
			monitor.subTask("Generating composite for " + genClass.getName());
			generateJava(genModel.getEditorDirectory(),
			// compute package name
					genPackage.getPresentationPackageName() + ".composites", genClass.getName() + "ComponentHelper", emitter, new Object[] { genClass }, createMonitor(monitor, 1));
			monitor.worked(1);
		}

		return Diagnostic.OK_INSTANCE;
	}

	@Override
	protected void addBaseTemplatePathEntries(List<String> templatePath) {
		templatePath.add(TEMPLATE_LOCATION);
		super.addBaseTemplatePathEntries(templatePath);
	}

	@Override
	protected void addClasspathEntries(JETEmitter jetEmitter) throws JETException {
		super.addClasspathEntries(jetEmitter);
		jetEmitter.addVariable(CLASSPATH_VARIABLE_NAME, MMXUIGeneratorPlugin.ID);
	}
}
