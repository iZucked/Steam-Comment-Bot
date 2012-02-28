/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package org.eclipse.emf.codegen.ecore.templates.editor;

import org.eclipse.emf.codegen.ecore.genmodel.*;
import java.util.*;

public class BuildProperties
{
  protected static String nl;
  public static synchronized BuildProperties create(String lineSeparator)
  {
    nl = lineSeparator;
    BuildProperties result = new BuildProperties();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "# ";
  protected final String TEXT_2 = NL + "# <copyright>" + NL + "# </copyright>" + NL + "#" + NL + "# ";
  protected final String TEXT_3 = "Id";
  protected final String TEXT_4 = NL + NL + "bin.includes = ";
  protected final String TEXT_5 = ",\\" + NL + "               icons/,\\";
  protected final String TEXT_6 = NL + "               META-INF/,\\";
  protected final String TEXT_7 = NL + "               plugin.xml,\\";
  protected final String TEXT_8 = NL + "               plugin.properties" + NL + "jars.compile.order = ";
  protected final String TEXT_9 = NL + "source.";
  protected final String TEXT_10 = " = ";
  protected final String TEXT_11 = NL + "output.";
  protected final String TEXT_12 = " = ";
  protected final String TEXT_13 = "bin";
  protected final String TEXT_14 = "war/WEB-INF/classes";
  protected final String TEXT_15 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/**
 * <copyright>
 *
 * Copyright (c) 2002-2008 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 */

    GenModel genModel = (GenModel)argument;
    String pluginClassesLocation = genModel.isRuntimeJar() ? genModel.getEditorPluginID()+".jar" : ".";
    List<String> sourceFolders = genModel.getEditorSourceFolders();
    {GenBase copyrightHolder = argument instanceof GenBase ? (GenBase)argument : argument instanceof Object[] && ((Object[])argument)[0] instanceof GenBase ? (GenBase)((Object[])argument)[0] : null;
    if (copyrightHolder != null && copyrightHolder.hasCopyright()) {
    stringBuffer.append(TEXT_1);
    stringBuffer.append(copyrightHolder.getCopyright(copyrightHolder.getGenModel().getIndentation(stringBuffer)));
    } else {
    stringBuffer.append(TEXT_2);
    stringBuffer.append("$");
    stringBuffer.append(TEXT_3);
    stringBuffer.append("$");
    }}
    stringBuffer.append(TEXT_4);
    stringBuffer.append(pluginClassesLocation);
    stringBuffer.append(TEXT_5);
    if (genModel.isBundleManifest()) {
    stringBuffer.append(TEXT_6);
    }
    if (genModel.getRuntimePlatform() != GenRuntimePlatform.GWT) {
    stringBuffer.append(TEXT_7);
    }
    stringBuffer.append(TEXT_8);
    stringBuffer.append(pluginClassesLocation);
     boolean first=true; for (Iterator<String> i = sourceFolders.iterator(); i.hasNext();) { String sourceFolder = i.next(); if (i.hasNext()){sourceFolder +=",\\";} if (first) {
    stringBuffer.append(TEXT_9);
    stringBuffer.append(pluginClassesLocation);
    stringBuffer.append(TEXT_10);
    stringBuffer.append(sourceFolder);
    first=false;} else {
    stringBuffer.append(sourceFolder);
    }}
    stringBuffer.append(TEXT_11);
    stringBuffer.append(pluginClassesLocation);
    stringBuffer.append(TEXT_12);
    if (genModel.getRuntimePlatform() != GenRuntimePlatform.GWT) {
    stringBuffer.append(TEXT_13);
    } else {
    stringBuffer.append(TEXT_14);
    }
    stringBuffer.append(TEXT_15);
    return stringBuffer.toString();
  }
}
