package com.mmxlabs.models.mmxcore.templates.editor;

import org.eclipse.emf.codegen.ecore.genmodel.*;

public class ComponentHelper
{
  protected static String nl;
  public static synchronized ComponentHelper create(String lineSeparator)
  {
    nl = lineSeparator;
    ComponentHelper result = new ComponentHelper();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = "/**";
  protected final String TEXT_3 = NL + " * ";
  protected final String TEXT_4 = NL + " */" + NL + "package ";
  protected final String TEXT_5 = ".composites;" + NL + "" + NL + "import com.mmxlabs.models.ui.BaseComponentHelper;" + NL + "import com.mmxlabs.models.ui.ComponentHelperUtils;" + NL + "import com.mmxlabs.models.ui.IComponentHelper;" + NL + "import com.mmxlabs.models.ui.IInlineEditorContainer;" + NL + "import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;" + NL + "" + NL + "import org.eclipse.core.runtime.IAdapterManager;" + NL + "import org.eclipse.core.runtime.Platform;";
  protected final String TEXT_6 = NL + NL + "/**" + NL + " * A component helper for ";
  protected final String TEXT_7 = " instances" + NL + " *" + NL + " * @generated" + NL + " */" + NL + "public class ";
  protected final String TEXT_8 = " extends BaseComponentHelper {" + NL + "\tprotected ";
  protected final String TEXT_9 = "<IComponentHelper> superClassesHelpers = new ";
  protected final String TEXT_10 = "<IComponentHelper>();" + NL + "" + NL + "\t/**" + NL + "\t * Construct a new instance, using the platform adapter manager" + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_11 = "() {" + NL + "\t\tthis(Platform.getAdapterManager());" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * Construct a new instance of this helper" + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_12 = "(IAdapterManager adapterManager) {" + NL + "\t\tfinal IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();";
  protected final String TEXT_13 = NL + "\t\tsuperClassesHelpers.addAll(registry.getComponentHelpers(";
  protected final String TEXT_14 = "));";
  protected final String TEXT_15 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * add editors to a composite, using ";
  protected final String TEXT_16 = " as the supertype" + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\t @Override" + NL + "\tpublic void addEditorsToComposite(final IInlineEditorContainer detailComposite) {" + NL + "\t\taddEditorsToComposite(detailComposite, ";
  protected final String TEXT_17 = ");\t" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Create the editors for features on this class directly, and superclass' features." + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\t@Override" + NL + "\tpublic void addEditorsToComposite(final IInlineEditorContainer detailComposite, final ";
  protected final String TEXT_18 = " topClass) {" + NL + "\t\tfor (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);";
  protected final String TEXT_19 = NL + "\t\tadd_";
  protected final String TEXT_20 = "Editor(detailComposite, topClass);";
  protected final String TEXT_21 = NL + "\t}";
  protected final String TEXT_22 = NL + "\t/**" + NL + "\t * Create the editor for the ";
  protected final String TEXT_23 = " feature on ";
  protected final String TEXT_24 = NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected void add_";
  protected final String TEXT_25 = "Editor(final IInlineEditorContainer detailComposite, final ";
  protected final String TEXT_26 = " topClass) {" + NL + "\t\tdetailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ";
  protected final String TEXT_27 = "));" + NL + "\t}";
  protected final String TEXT_28 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
/**
 * <copyright>
 * Copyright (c) 2012 Minimax Labs Ltd. All Rights Reserved.
 * </copyright>
 */
 
    final GenClass genClass = (GenClass) argument;
    final GenPackage genPackage = genClass.getGenPackage();
    final GenModel genModel = genPackage.getGenModel(); 
    final String thisClassName = genClass.getName()+"ComponentHelper"; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    {GenBase copyrightHolder = argument instanceof GenBase ? (GenBase)argument : argument instanceof Object[] && ((Object[])argument)[0] instanceof GenBase ? (GenBase)((Object[])argument)[0] : null;
    if (copyrightHolder != null && copyrightHolder.hasCopyright()) {
    stringBuffer.append(TEXT_3);
    stringBuffer.append(copyrightHolder.getCopyright(copyrightHolder.getGenModel().getIndentation(stringBuffer)));
    }}
    stringBuffer.append(TEXT_4);
    stringBuffer.append(genPackage.getPresentationPackageName());
    stringBuffer.append(TEXT_5);
    genModel.markImportLocation(stringBuffer, genPackage);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_7);
    stringBuffer.append(thisClassName);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(genModel.getImportedName("java.util.List"));
    stringBuffer.append(TEXT_9);
    stringBuffer.append(genModel.getImportedName("java.util.ArrayList"));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(thisClassName);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(thisClassName);
    stringBuffer.append(TEXT_12);
     for (final GenClass superGenClass : genClass.getBaseGenClasses()) { 
    stringBuffer.append(TEXT_13);
    stringBuffer.append(superGenClass.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_14);
     } // for superclasses 
    stringBuffer.append(TEXT_15);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_16);
    stringBuffer.append(genClass.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_17);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EClass"));
    stringBuffer.append(TEXT_18);
     for (final GenFeature genFeature : genClass.getGenFeatures()) { 
    stringBuffer.append(TEXT_19);
    stringBuffer.append(genFeature.getName());
    stringBuffer.append(TEXT_20);
     } // genClass.getGenFeatures() 
    stringBuffer.append(TEXT_21);
     for (final GenFeature genFeature : genClass.getGenFeatures()) { 
    stringBuffer.append(TEXT_22);
    stringBuffer.append(genFeature.getName());
    stringBuffer.append(TEXT_23);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_24);
    stringBuffer.append(genFeature.getName());
    stringBuffer.append(TEXT_25);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EClass"));
    stringBuffer.append(TEXT_26);
    stringBuffer.append(genFeature.getQualifiedFeatureAccessor());
    stringBuffer.append(TEXT_27);
     } 
    stringBuffer.append(TEXT_28);
    genModel.emitSortedImports();
    return stringBuffer.toString();
  }
}
