package com.mmxlabs.models.mmxcore.templates.editor;

import java.util.*;
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
  protected final String TEXT_4 = NL + " * <copyright>" + NL + " * </copyright>" + NL + " *" + NL + " * ";
  protected final String TEXT_5 = "Id";
  protected final String TEXT_6 = NL + " */" + NL + "package ";
  protected final String TEXT_7 = ".composites;" + NL;
  protected final String TEXT_8 = NL + "import com.mmxlabs.models.ui.IComponentHelper;" + NL + "import com.mmxlabs.models.ui.ComponentHelperUtils;" + NL + "import com.mmxlabs.models.ui.IInlineEditorContainer;" + NL + "" + NL + "import org.eclipse.core.runtime.IAdapterManager;" + NL + "import org.eclipse.core.runtime.Platform;" + NL + "" + NL + "/**" + NL + " * A component helper for ";
  protected final String TEXT_9 = " instances" + NL + " *" + NL + " * @generated" + NL + " */" + NL + "public class ";
  protected final String TEXT_10 = " implements IComponentHelper {" + NL + "\tprotected ";
  protected final String TEXT_11 = "<IComponentHelper> superClassesHelpers = new ";
  protected final String TEXT_12 = "<IComponentHelper>();" + NL + "" + NL + "\t/**" + NL + "\t * Construct a new instance, using the platform adapter manager" + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_13 = "() {" + NL + "\t\tthis(Platform.getAdapterManager());" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * Construct a new instance of this helper" + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_14 = "(IAdapterManager adapterManager) {";
  protected final String TEXT_15 = NL + "\t\t{" + NL + "\t\t\tIComponentHelper helper = adapterManager.getAdapter(";
  protected final String TEXT_16 = ", IComponentHelper.class);" + NL + "\t\t\tif (helper == null) = adapterManager.loadAdapter(";
  protected final String TEXT_17 = ", IComponentHelper.class.getCanonicalName());" + NL + "\t\t\tif (helper != null) superClassesHelpers.add(helper);" + NL + "\t\t}";
  protected final String TEXT_18 = NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * add editors to a composite, using ";
  protected final String TEXT_19 = " as the supertype" + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\t @Override" + NL + "\tpublic void addEditorsToComposite(final IDealComposite detailComposite) {" + NL + "\t\taddEditorsToComposite(detailComposite, ";
  protected final String TEXT_20 = ");\t" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * Create the editors for features on this class directly, and superclass' features." + NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\t@Override" + NL + "\tpublic void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {" + NL + "\t\tfor (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);";
  protected final String TEXT_21 = NL + "\t\tadd_";
  protected final String TEXT_22 = "Editor(detailComposite, topClass);";
  protected final String TEXT_23 = NL + "\t}";
  protected final String TEXT_24 = NL + "\t/**" + NL + "\t * Create the editor for the ";
  protected final String TEXT_25 = " feature on ";
  protected final String TEXT_26 = NL + "\t *" + NL + "\t * @generated" + NL + "\t */" + NL + "\tprotected void add_";
  protected final String TEXT_27 = "Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {" + NL + "\t\tdetailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(";
  protected final String TEXT_28 = ", topClass));" + NL + "\t}";
  protected final String TEXT_29 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     
/**
 * <copyright>
 * Copyright (c) 2012 Minimax Laboratories Inc. All Rights Reserved.
 * </copyright>
 */
 
    final GenClass genClass = (GenClass) argument;
    final GenPackage genPackage = genClass.getGenPackage();
    final GenModel genModel = genPackage.getGenModel(); 
    final boolean isJDK50 = genModel.getComplianceLevel().getValue() >= GenJDKLevel.JDK50;
    final String thisClassName = genClass.getName()+"ComponentHelper"; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    {GenBase copyrightHolder = argument instanceof GenBase ? (GenBase)argument : argument instanceof Object[] && ((Object[])argument)[0] instanceof GenBase ? (GenBase)((Object[])argument)[0] : null;
    if (copyrightHolder != null && copyrightHolder.hasCopyright()) {
    stringBuffer.append(TEXT_3);
    stringBuffer.append(copyrightHolder.getCopyright(copyrightHolder.getGenModel().getIndentation(stringBuffer)));
    } else {
    stringBuffer.append(TEXT_4);
    stringBuffer.append("$");
    stringBuffer.append(TEXT_5);
    stringBuffer.append("$");
    }}
    stringBuffer.append(TEXT_6);
    stringBuffer.append(genPackage.getPresentationPackageName());
    stringBuffer.append(TEXT_7);
    genModel.markImportLocation(stringBuffer, genPackage);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_9);
    stringBuffer.append(thisClassName);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(genModel.getImportedName("java.util.List"));
    stringBuffer.append(TEXT_11);
    stringBuffer.append(genModel.getImportedName("java.util.ArrayList"));
    stringBuffer.append(TEXT_12);
    stringBuffer.append(thisClassName);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(thisClassName);
    stringBuffer.append(TEXT_14);
     for (final GenClass superGenClass : genClass.getBaseGenClasses()) { 
    stringBuffer.append(TEXT_15);
    stringBuffer.append(superGenClass.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_16);
    stringBuffer.append(superGenClass.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_17);
     } // for superclasses 
    stringBuffer.append(TEXT_18);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_19);
    stringBuffer.append(genClass.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_20);
     for (final GenFeature genFeature : genClass.getGenFeatures()) { 
    stringBuffer.append(TEXT_21);
    stringBuffer.append(genFeature.getName());
    stringBuffer.append(TEXT_22);
     } // genClass.getGenFeatures() 
    stringBuffer.append(TEXT_23);
     for (final GenFeature genFeature : genClass.getGenFeatures()) { 
    stringBuffer.append(TEXT_24);
    stringBuffer.append(genFeature.getName());
    stringBuffer.append(TEXT_25);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_26);
    stringBuffer.append(genFeature.getName());
    stringBuffer.append(TEXT_27);
    stringBuffer.append(genFeature.getQualifiedFeatureAccessor());
    stringBuffer.append(TEXT_28);
     } 
    stringBuffer.append(TEXT_29);
    genModel.emitSortedImports();
    return stringBuffer.toString();
  }
}
