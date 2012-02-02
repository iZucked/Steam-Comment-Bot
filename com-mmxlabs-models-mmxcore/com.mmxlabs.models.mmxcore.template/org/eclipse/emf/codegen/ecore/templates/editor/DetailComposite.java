package org.eclipse.emf.codegen.ecore.templates.editor;

import java.util.*;
import org.eclipse.emf.codegen.ecore.genmodel.*;

public class DetailComposite
{
  protected static String nl;
  public static synchronized DetailComposite create(String lineSeparator)
  {
    nl = lineSeparator;
    DetailComposite result = new DetailComposite();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    


    return stringBuffer.toString();
  }
}
