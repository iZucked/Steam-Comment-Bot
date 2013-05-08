package org.eclipse.emf.codegen.ecore.templates.model;

import java.util.*;
import org.eclipse.emf.codegen.ecore.genmodel.*;

public class FactoryClass
{
  protected static String nl;
  public static synchronized FactoryClass create(String lineSeparator)
  {
    nl = lineSeparator;
    FactoryClass result = new FactoryClass();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = "/**";
  protected final String TEXT_3 = NL + " * ";
  protected final String TEXT_4 = NL + " */";
  protected final String TEXT_5 = NL + "package ";
  protected final String TEXT_6 = ";";
  protected final String TEXT_7 = NL + "package ";
  protected final String TEXT_8 = ";";
  protected final String TEXT_9 = NL;
  protected final String TEXT_10 = NL;
  protected final String TEXT_11 = NL + "/**" + NL + " * <!-- begin-user-doc -->" + NL + " * The <b>Factory</b> for the model." + NL + " * It provides a create method for each non-abstract class of the model." + NL + " * <!-- end-user-doc -->";
  protected final String TEXT_12 = NL + " * @see ";
  protected final String TEXT_13 = NL + " * @generated" + NL + " */";
  protected final String TEXT_14 = NL + "/**" + NL + " * <!-- begin-user-doc -->" + NL + " * An implementation of the model <b>Factory</b>." + NL + " * <!-- end-user-doc -->" + NL + " * @generated" + NL + " */";
  protected final String TEXT_15 = NL + "public class ";
  protected final String TEXT_16 = " extends ";
  protected final String TEXT_17 = " implements ";
  protected final String TEXT_18 = NL + "public interface ";
  protected final String TEXT_19 = " extends ";
  protected final String TEXT_20 = NL + "{";
  protected final String TEXT_21 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\t";
  protected final String TEXT_22 = " copyright = ";
  protected final String TEXT_23 = ";";
  protected final String TEXT_24 = NL;
  protected final String TEXT_25 = NL + "\t/**" + NL + "\t * The singleton instance of the factory." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\t";
  protected final String TEXT_26 = " eINSTANCE = init();" + NL;
  protected final String TEXT_27 = NL + "\t/**" + NL + "\t * The singleton instance of the factory." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\t";
  protected final String TEXT_28 = " INSTANCE = ";
  protected final String TEXT_29 = ".eINSTANCE;" + NL;
  protected final String TEXT_30 = NL + "\t/**" + NL + "\t * The singleton instance of the factory." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\t";
  protected final String TEXT_31 = " eINSTANCE = ";
  protected final String TEXT_32 = ".init();" + NL;
  protected final String TEXT_33 = NL + "\t/**" + NL + "\t * Creates the default factory implementation." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_34 = NL + "\tpublic static ";
  protected final String TEXT_35 = " init()" + NL + "\t{" + NL + "\t\ttry" + NL + "\t\t{" + NL + "\t\t\t";
  protected final String TEXT_36 = " the";
  protected final String TEXT_37 = " = (";
  protected final String TEXT_38 = ")";
  protected final String TEXT_39 = ".Registry.INSTANCE.getEFactory(\"";
  protected final String TEXT_40 = "\");";
  protected final String TEXT_41 = " " + NL + "\t\t\tif (the";
  protected final String TEXT_42 = " != null)" + NL + "\t\t\t{" + NL + "\t\t\t\treturn the";
  protected final String TEXT_43 = ";" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "\t\tcatch (Exception exception)" + NL + "\t\t{" + NL + "\t\t\t";
  protected final String TEXT_44 = ".INSTANCE.log(exception);" + NL + "\t\t}" + NL + "\t\treturn new ";
  protected final String TEXT_45 = "();" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * Creates an instance of the factory." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_46 = "()" + NL + "\t{" + NL + "\t\tsuper();" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_47 = NL + "\t@Override";
  protected final String TEXT_48 = NL + "\tpublic EObject create(EClass eClass)" + NL + "\t{" + NL + "\t\tswitch (eClass.getClassifierID())" + NL + "\t\t{";
  protected final String TEXT_49 = NL + "\t\t\tcase ";
  protected final String TEXT_50 = ".";
  protected final String TEXT_51 = ": return ";
  protected final String TEXT_52 = "create";
  protected final String TEXT_53 = "();";
  protected final String TEXT_54 = NL + "\t\t\tdefault:" + NL + "\t\t\t\tthrow new IllegalArgumentException(\"The class '\" + eClass.getName() + \"' is not a valid classifier\");";
  protected final String TEXT_55 = NL + "\t\t}" + NL + "\t}" + NL;
  protected final String TEXT_56 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_57 = NL + "\t@Override";
  protected final String TEXT_58 = NL + "\tpublic Object createFromString(";
  protected final String TEXT_59 = " eDataType, String initialValue)" + NL + "\t{" + NL + "\t\tswitch (eDataType.getClassifierID())" + NL + "\t\t{";
  protected final String TEXT_60 = NL + "\t\t\tcase ";
  protected final String TEXT_61 = ".";
  protected final String TEXT_62 = ":" + NL + "\t\t\t\treturn create";
  protected final String TEXT_63 = "FromString(eDataType, initialValue);";
  protected final String TEXT_64 = NL + "\t\t\tdefault:" + NL + "\t\t\t\tthrow new IllegalArgumentException(\"The datatype '\" + eDataType.getName() + \"' is not a valid classifier\");";
  protected final String TEXT_65 = NL + "\t\t}" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_66 = NL + "\t@Override";
  protected final String TEXT_67 = NL + "\tpublic String convertToString(";
  protected final String TEXT_68 = " eDataType, Object instanceValue)" + NL + "\t{" + NL + "\t\tswitch (eDataType.getClassifierID())" + NL + "\t\t{";
  protected final String TEXT_69 = NL + "\t\t\tcase ";
  protected final String TEXT_70 = ".";
  protected final String TEXT_71 = ":" + NL + "\t\t\t\treturn convert";
  protected final String TEXT_72 = "ToString(eDataType, instanceValue);";
  protected final String TEXT_73 = NL + "\t\t\tdefault:" + NL + "\t\t\t\tthrow new IllegalArgumentException(\"The datatype '\" + eDataType.getName() + \"' is not a valid classifier\");";
  protected final String TEXT_74 = NL + "\t\t}" + NL + "\t}" + NL;
  protected final String TEXT_75 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_76 = " create";
  protected final String TEXT_77 = "()" + NL + "\t{";
  protected final String TEXT_78 = NL + "\t\t";
  protected final String TEXT_79 = " ";
  protected final String TEXT_80 = " = ";
  protected final String TEXT_81 = "super.create(";
  protected final String TEXT_82 = ");";
  protected final String TEXT_83 = NL + "\t\t";
  protected final String TEXT_84 = " ";
  protected final String TEXT_85 = " = new ";
  protected final String TEXT_86 = "()";
  protected final String TEXT_87 = "{}";
  protected final String TEXT_88 = ";";
  protected final String TEXT_89 = NL + "\t\treturn ";
  protected final String TEXT_90 = ";" + NL + "\t}" + NL;
  protected final String TEXT_91 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_92 = NL + "\t@SuppressWarnings(\"unchecked\")";
  protected final String TEXT_93 = NL + "\tpublic ";
  protected final String TEXT_94 = " create";
  protected final String TEXT_95 = "(String ";
  protected final String TEXT_96 = "it";
  protected final String TEXT_97 = "literal";
  protected final String TEXT_98 = ")" + NL + "\t{";
  protected final String TEXT_99 = NL + "\t\t";
  protected final String TEXT_100 = NL + "\t\t";
  protected final String TEXT_101 = " result = ";
  protected final String TEXT_102 = ".get(literal);" + NL + "\t\tif (result == null) throw new IllegalArgumentException(\"The value '\" + literal + \"' is not a valid enumerator of '\" + ";
  protected final String TEXT_103 = ".getName() + \"'\");";
  protected final String TEXT_104 = NL + "\t\treturn result;";
  protected final String TEXT_105 = NL + "\t\treturn new ";
  protected final String TEXT_106 = "(create";
  protected final String TEXT_107 = "(literal));";
  protected final String TEXT_108 = NL + "\t\treturn create";
  protected final String TEXT_109 = "(literal);";
  protected final String TEXT_110 = NL + "\t\treturn new ";
  protected final String TEXT_111 = "(";
  protected final String TEXT_112 = ".create";
  protected final String TEXT_113 = "(literal));";
  protected final String TEXT_114 = NL + "\t\treturn ";
  protected final String TEXT_115 = ".create";
  protected final String TEXT_116 = "(literal);";
  protected final String TEXT_117 = NL + "\t\treturn ";
  protected final String TEXT_118 = "(";
  protected final String TEXT_119 = ")";
  protected final String TEXT_120 = ".createFromString(";
  protected final String TEXT_121 = ", literal);";
  protected final String TEXT_122 = NL + "\t\tif (literal == null) return null;" + NL + "\t\t";
  protected final String TEXT_123 = " result = new ";
  protected final String TEXT_124 = "<";
  protected final String TEXT_125 = ">";
  protected final String TEXT_126 = "();";
  protected final String TEXT_127 = NL + "\t\tfor (";
  protected final String TEXT_128 = " stringTokenizer = new ";
  protected final String TEXT_129 = "(literal); stringTokenizer.hasMoreTokens(); )";
  protected final String TEXT_130 = NL + "\t\tfor (String item : split(literal))";
  protected final String TEXT_131 = NL + "\t\t{";
  protected final String TEXT_132 = NL + "\t\t\tString item = stringTokenizer.nextToken();";
  protected final String TEXT_133 = NL + "\t\t\tresult.add(create";
  protected final String TEXT_134 = "(item));";
  protected final String TEXT_135 = NL + "\t\t\tresult.add(create";
  protected final String TEXT_136 = "FromString(";
  protected final String TEXT_137 = ", item));";
  protected final String TEXT_138 = NL + "\t\t\tresult.add(";
  protected final String TEXT_139 = ".create";
  protected final String TEXT_140 = "(item));";
  protected final String TEXT_141 = NL + "\t\t\tresult.add(";
  protected final String TEXT_142 = ".createFromString(";
  protected final String TEXT_143 = ", item));";
  protected final String TEXT_144 = NL + "\t\t}" + NL + "\t\treturn result;";
  protected final String TEXT_145 = NL + "\t\tif (literal == null) return ";
  protected final String TEXT_146 = ";" + NL + "\t\t";
  protected final String TEXT_147 = " result = ";
  protected final String TEXT_148 = ";" + NL + "\t\tRuntimeException exception = null;";
  protected final String TEXT_149 = NL + "\t\ttry" + NL + "\t\t{";
  protected final String TEXT_150 = NL + "\t\t\tresult = create";
  protected final String TEXT_151 = "(literal);";
  protected final String TEXT_152 = NL + "\t\t\tresult = (";
  protected final String TEXT_153 = ")create";
  protected final String TEXT_154 = "FromString(";
  protected final String TEXT_155 = ", literal);";
  protected final String TEXT_156 = NL + "\t\t\tresult = ";
  protected final String TEXT_157 = ".create";
  protected final String TEXT_158 = "(literal);";
  protected final String TEXT_159 = NL + "\t\t\tresult = (";
  protected final String TEXT_160 = ")";
  protected final String TEXT_161 = ".createFromString(";
  protected final String TEXT_162 = ", literal);";
  protected final String TEXT_163 = NL + "\t\t\tif (";
  protected final String TEXT_164 = "result != null && ";
  protected final String TEXT_165 = ".INSTANCE.validate(";
  protected final String TEXT_166 = ", ";
  protected final String TEXT_167 = "new ";
  protected final String TEXT_168 = "(result)";
  protected final String TEXT_169 = "result";
  protected final String TEXT_170 = ", null, null))" + NL + "\t\t\t{" + NL + "\t\t\t\treturn result;" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "\t\tcatch (RuntimeException e)" + NL + "\t\t{" + NL + "\t\t\texception = e;" + NL + "\t\t}";
  protected final String TEXT_171 = NL + "\t\tif (";
  protected final String TEXT_172 = "result != null || ";
  protected final String TEXT_173 = "exception == null) return result;" + NL + "    " + NL + "\t\tthrow exception;";
  protected final String TEXT_174 = NL + "\t\treturn (";
  protected final String TEXT_175 = ")super.createFromString(literal);";
  protected final String TEXT_176 = NL + "\t\t// TODO: implement this method" + NL + "\t\t// Ensure that you remove @generated or mark it @generated NOT" + NL + "\t\tthrow new ";
  protected final String TEXT_177 = "();";
  protected final String TEXT_178 = NL + "\t\treturn ((";
  protected final String TEXT_179 = ")super.createFromString(";
  protected final String TEXT_180 = ", literal)).";
  protected final String TEXT_181 = "();";
  protected final String TEXT_182 = NL + "\t\treturn ";
  protected final String TEXT_183 = "(";
  protected final String TEXT_184 = ")";
  protected final String TEXT_185 = "super.createFromString(";
  protected final String TEXT_186 = ", literal);";
  protected final String TEXT_187 = NL + "\t}" + NL;
  protected final String TEXT_188 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_189 = NL + "\t@SuppressWarnings(\"unchecked\")";
  protected final String TEXT_190 = NL + "\tpublic ";
  protected final String TEXT_191 = " create";
  protected final String TEXT_192 = "FromString(";
  protected final String TEXT_193 = " eDataType, String initialValue)" + NL + "\t{";
  protected final String TEXT_194 = NL + "\t\treturn create";
  protected final String TEXT_195 = "(initialValue);";
  protected final String TEXT_196 = NL + "\t\t";
  protected final String TEXT_197 = " result = ";
  protected final String TEXT_198 = ".get(initialValue);" + NL + "\t\tif (result == null) throw new IllegalArgumentException(\"The value '\" + initialValue + \"' is not a valid enumerator of '\" + eDataType.getName() + \"'\");";
  protected final String TEXT_199 = NL + "\t\treturn result;";
  protected final String TEXT_200 = NL + "\t\treturn ";
  protected final String TEXT_201 = "(";
  protected final String TEXT_202 = ")";
  protected final String TEXT_203 = "create";
  protected final String TEXT_204 = "FromString(";
  protected final String TEXT_205 = ", initialValue);";
  protected final String TEXT_206 = NL + "\t\treturn ";
  protected final String TEXT_207 = "(";
  protected final String TEXT_208 = ")";
  protected final String TEXT_209 = ".createFromString(";
  protected final String TEXT_210 = ", initialValue);";
  protected final String TEXT_211 = NL + "\t\treturn create";
  protected final String TEXT_212 = "(initialValue);";
  protected final String TEXT_213 = NL + "\t\tif (initialValue == null) return null;" + NL + "\t\t";
  protected final String TEXT_214 = " result = new ";
  protected final String TEXT_215 = "<";
  protected final String TEXT_216 = ">";
  protected final String TEXT_217 = "();";
  protected final String TEXT_218 = NL + "\t\tfor (";
  protected final String TEXT_219 = " stringTokenizer = new ";
  protected final String TEXT_220 = "(initialValue); stringTokenizer.hasMoreTokens(); )";
  protected final String TEXT_221 = NL + "\t\tfor (String item : split(initialValue))";
  protected final String TEXT_222 = NL + "\t\t{";
  protected final String TEXT_223 = NL + "\t\t\tString item = stringTokenizer.nextToken();";
  protected final String TEXT_224 = NL + "\t\t\tresult.add(create";
  protected final String TEXT_225 = "FromString(";
  protected final String TEXT_226 = ", item));";
  protected final String TEXT_227 = NL + "\t\t\tresult.add(";
  protected final String TEXT_228 = "(";
  protected final String TEXT_229 = ")";
  protected final String TEXT_230 = ".createFromString(";
  protected final String TEXT_231 = ", item));";
  protected final String TEXT_232 = NL + "\t\t}" + NL + "\t\treturn result;";
  protected final String TEXT_233 = NL + "\t\treturn new ";
  protected final String TEXT_234 = "(create";
  protected final String TEXT_235 = "(initialValue));";
  protected final String TEXT_236 = NL + "\t\treturn create";
  protected final String TEXT_237 = "(initialValue);";
  protected final String TEXT_238 = NL + "\t\tif (initialValue == null) return null;" + NL + "\t\t";
  protected final String TEXT_239 = " result = null;" + NL + "\t\tRuntimeException exception = null;";
  protected final String TEXT_240 = NL + "\t\ttry" + NL + "\t\t{";
  protected final String TEXT_241 = NL + "\t\t\tresult = ";
  protected final String TEXT_242 = "(";
  protected final String TEXT_243 = ")";
  protected final String TEXT_244 = "create";
  protected final String TEXT_245 = "FromString(";
  protected final String TEXT_246 = ", initialValue);";
  protected final String TEXT_247 = NL + "\t\t\tresult = ";
  protected final String TEXT_248 = "(";
  protected final String TEXT_249 = ")";
  protected final String TEXT_250 = ".createFromString(";
  protected final String TEXT_251 = ", initialValue);";
  protected final String TEXT_252 = NL + "\t\t\tif (result != null && ";
  protected final String TEXT_253 = ".INSTANCE.validate(eDataType, result, null, null))" + NL + "\t\t\t{" + NL + "\t\t\t\treturn result;" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "\t\tcatch (RuntimeException e)" + NL + "\t\t{" + NL + "\t\t\texception = e;" + NL + "\t\t}";
  protected final String TEXT_254 = NL + "\t\tif (result != null || exception == null) return result;" + NL + "    " + NL + "\t\tthrow exception;";
  protected final String TEXT_255 = NL + "\t\treturn create";
  protected final String TEXT_256 = "(initialValue);";
  protected final String TEXT_257 = NL + "\t\treturn ";
  protected final String TEXT_258 = "(";
  protected final String TEXT_259 = ")";
  protected final String TEXT_260 = "super.createFromString(initialValue);";
  protected final String TEXT_261 = NL + "\t\t// TODO: implement this method" + NL + "\t\t// Ensure that you remove @generated or mark it @generated NOT" + NL + "\t\tthrow new ";
  protected final String TEXT_262 = "();";
  protected final String TEXT_263 = NL + "\t\treturn ";
  protected final String TEXT_264 = "(";
  protected final String TEXT_265 = ")";
  protected final String TEXT_266 = "super.createFromString(eDataType, initialValue);";
  protected final String TEXT_267 = NL + "\t}" + NL;
  protected final String TEXT_268 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic String convert";
  protected final String TEXT_269 = "(";
  protected final String TEXT_270 = " ";
  protected final String TEXT_271 = "it";
  protected final String TEXT_272 = "instanceValue";
  protected final String TEXT_273 = ")" + NL + "\t{";
  protected final String TEXT_274 = NL + "\t\t";
  protected final String TEXT_275 = NL + "\t\treturn instanceValue == null ? null : instanceValue.toString();";
  protected final String TEXT_276 = NL + "\t\treturn instanceValue == null ? null : convert";
  protected final String TEXT_277 = "(instanceValue";
  protected final String TEXT_278 = ".";
  protected final String TEXT_279 = "()";
  protected final String TEXT_280 = ");";
  protected final String TEXT_281 = NL + "\t\treturn convert";
  protected final String TEXT_282 = "(instanceValue);";
  protected final String TEXT_283 = NL + "\t\treturn ";
  protected final String TEXT_284 = ".convert";
  protected final String TEXT_285 = "(instanceValue);";
  protected final String TEXT_286 = NL + "\t\treturn ";
  protected final String TEXT_287 = ".convertToString(";
  protected final String TEXT_288 = ", instanceValue);";
  protected final String TEXT_289 = NL + "\t\tif (instanceValue == null) return null;" + NL + "\t\tif (instanceValue.isEmpty()) return \"\";" + NL + "\t\t";
  protected final String TEXT_290 = " result = new ";
  protected final String TEXT_291 = "();";
  protected final String TEXT_292 = NL + "\t\tfor (";
  protected final String TEXT_293 = " i = instanceValue.iterator(); i.hasNext(); )";
  protected final String TEXT_294 = NL + "\t\tfor (";
  protected final String TEXT_295 = " item : instanceValue)";
  protected final String TEXT_296 = NL + "\t\t{";
  protected final String TEXT_297 = NL + "\t\t\tresult.append(convert";
  protected final String TEXT_298 = "((";
  protected final String TEXT_299 = ")";
  protected final String TEXT_300 = "));";
  protected final String TEXT_301 = NL + "\t\t\tresult.append(convert";
  protected final String TEXT_302 = "ToString(";
  protected final String TEXT_303 = ", ";
  protected final String TEXT_304 = "));";
  protected final String TEXT_305 = NL + "\t\t\tresult.append(";
  protected final String TEXT_306 = ".convert";
  protected final String TEXT_307 = "((";
  protected final String TEXT_308 = ")";
  protected final String TEXT_309 = "));";
  protected final String TEXT_310 = NL + "\t\t\tresult.append(";
  protected final String TEXT_311 = ".convertToString(";
  protected final String TEXT_312 = ", ";
  protected final String TEXT_313 = "));";
  protected final String TEXT_314 = NL + "\t\t\tresult.append(' ');" + NL + "\t\t}" + NL + "\t\treturn result.substring(0, result.length() - 1);";
  protected final String TEXT_315 = NL + "\t\tif (instanceValue == null) return null;";
  protected final String TEXT_316 = NL + "\t\tif (";
  protected final String TEXT_317 = ".isInstance(instanceValue))" + NL + "\t\t{" + NL + "\t\t\ttry" + NL + "\t\t\t{";
  protected final String TEXT_318 = NL + "\t\t\t\tString value = convert";
  protected final String TEXT_319 = "(instanceValue);";
  protected final String TEXT_320 = NL + "\t\t\t\tString value = convert";
  protected final String TEXT_321 = "(((";
  protected final String TEXT_322 = ")instanceValue).";
  protected final String TEXT_323 = "());";
  protected final String TEXT_324 = NL + "\t\t\t\tString value = convert";
  protected final String TEXT_325 = "((";
  protected final String TEXT_326 = ")instanceValue);";
  protected final String TEXT_327 = NL + "\t\t\t\tString value = convert";
  protected final String TEXT_328 = "ToString(";
  protected final String TEXT_329 = ", instanceValue);";
  protected final String TEXT_330 = NL + "\t\t\t\tString value = ";
  protected final String TEXT_331 = ".convert";
  protected final String TEXT_332 = "((";
  protected final String TEXT_333 = ")instanceValue);";
  protected final String TEXT_334 = NL + "\t\t\t\tString value = ";
  protected final String TEXT_335 = ".convertToString(";
  protected final String TEXT_336 = ", instanceValue);";
  protected final String TEXT_337 = NL + "\t\t\t\tif (value != null) return value;" + NL + "\t\t\t}" + NL + "\t\t\tcatch (Exception e)" + NL + "\t\t\t{" + NL + "\t\t\t\t// Keep trying other member types until all have failed." + NL + "\t\t\t}" + NL + "\t\t}";
  protected final String TEXT_338 = NL + "\t\ttry" + NL + "\t\t{";
  protected final String TEXT_339 = NL + "\t\t\tString value = convert";
  protected final String TEXT_340 = "(instanceValue);";
  protected final String TEXT_341 = NL + "\t\t\tString value = convert";
  protected final String TEXT_342 = "ToString(";
  protected final String TEXT_343 = ", ";
  protected final String TEXT_344 = "new ";
  protected final String TEXT_345 = "(instanceValue)";
  protected final String TEXT_346 = "instanceValue";
  protected final String TEXT_347 = ");";
  protected final String TEXT_348 = NL + "\t\t\tString value = ";
  protected final String TEXT_349 = ".convert";
  protected final String TEXT_350 = "(instanceValue);";
  protected final String TEXT_351 = NL + "\t\t\tString value = ";
  protected final String TEXT_352 = ".convertToString(";
  protected final String TEXT_353 = ", ";
  protected final String TEXT_354 = "new ";
  protected final String TEXT_355 = "(instanceValue)";
  protected final String TEXT_356 = "instanceValue";
  protected final String TEXT_357 = ");";
  protected final String TEXT_358 = NL + "\t\t\tif (value != null) return value;" + NL + "\t\t}" + NL + "\t\tcatch (Exception e)" + NL + "\t\t{" + NL + "\t\t\t// Keep trying other member types until all have failed." + NL + "\t\t}";
  protected final String TEXT_359 = NL + "\t\tthrow new IllegalArgumentException(\"Invalid value: '\"+instanceValue+\"' for datatype :\"+";
  protected final String TEXT_360 = ".getName());";
  protected final String TEXT_361 = NL + "\t\treturn super.convertToString(instanceValue);";
  protected final String TEXT_362 = NL + "\t\t// TODO: implement this method" + NL + "\t\t// Ensure that you remove @generated or mark it @generated NOT" + NL + "\t\tthrow new ";
  protected final String TEXT_363 = "();";
  protected final String TEXT_364 = NL + "\t\treturn super.convertToString(";
  protected final String TEXT_365 = ", new ";
  protected final String TEXT_366 = "(instanceValue));";
  protected final String TEXT_367 = NL + "\t\treturn super.convertToString(";
  protected final String TEXT_368 = ", instanceValue);";
  protected final String TEXT_369 = NL + "\t}" + NL;
  protected final String TEXT_370 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_371 = NL + "\t@SuppressWarnings(\"unchecked\")";
  protected final String TEXT_372 = NL + "\tpublic String convert";
  protected final String TEXT_373 = "ToString(";
  protected final String TEXT_374 = " eDataType, Object instanceValue)" + NL + "\t{";
  protected final String TEXT_375 = NL + "\t\treturn convert";
  protected final String TEXT_376 = "((";
  protected final String TEXT_377 = ")instanceValue);";
  protected final String TEXT_378 = NL + "\t\treturn instanceValue == null ? null : instanceValue.toString();";
  protected final String TEXT_379 = NL + "\t\treturn convert";
  protected final String TEXT_380 = "ToString(";
  protected final String TEXT_381 = ", instanceValue);";
  protected final String TEXT_382 = NL + "\t\treturn ";
  protected final String TEXT_383 = ".convertToString(";
  protected final String TEXT_384 = ", instanceValue);";
  protected final String TEXT_385 = NL + "\t\treturn convert";
  protected final String TEXT_386 = "((";
  protected final String TEXT_387 = ")instanceValue);";
  protected final String TEXT_388 = NL + "\t\tif (instanceValue == null) return null;" + NL + "\t\t";
  protected final String TEXT_389 = " list = (";
  protected final String TEXT_390 = ")instanceValue;" + NL + "\t\tif (list.isEmpty()) return \"\";" + NL + "\t\t";
  protected final String TEXT_391 = " result = new ";
  protected final String TEXT_392 = "();";
  protected final String TEXT_393 = NL + "\t\tfor (";
  protected final String TEXT_394 = " i = list.iterator(); i.hasNext(); )";
  protected final String TEXT_395 = NL + "\t\tfor (";
  protected final String TEXT_396 = " item : list)";
  protected final String TEXT_397 = NL + "\t\t{";
  protected final String TEXT_398 = NL + "\t\t\tresult.append(convert";
  protected final String TEXT_399 = "ToString(";
  protected final String TEXT_400 = ", ";
  protected final String TEXT_401 = "));";
  protected final String TEXT_402 = NL + "\t\t\tresult.append(";
  protected final String TEXT_403 = ".convertToString(";
  protected final String TEXT_404 = ", ";
  protected final String TEXT_405 = "));";
  protected final String TEXT_406 = NL + "\t\t\tresult.append(' ');" + NL + "\t\t}" + NL + "\t\treturn result.substring(0, result.length() - 1);";
  protected final String TEXT_407 = NL + "\t\treturn instanceValue == null ? null : convert";
  protected final String TEXT_408 = "(((";
  protected final String TEXT_409 = ")instanceValue)";
  protected final String TEXT_410 = ".";
  protected final String TEXT_411 = "()";
  protected final String TEXT_412 = ");";
  protected final String TEXT_413 = NL + "\t\treturn convert";
  protected final String TEXT_414 = "(instanceValue);";
  protected final String TEXT_415 = NL + "\t\tif (instanceValue == null) return null;";
  protected final String TEXT_416 = NL + "\t\tif (";
  protected final String TEXT_417 = ".isInstance(instanceValue))" + NL + "\t\t{" + NL + "\t\t\ttry" + NL + "\t\t\t{";
  protected final String TEXT_418 = NL + "\t\t\t\tString value = convert";
  protected final String TEXT_419 = "ToString(";
  protected final String TEXT_420 = ", instanceValue);";
  protected final String TEXT_421 = NL + "\t\t\t\tString value = ";
  protected final String TEXT_422 = ".convertToString(";
  protected final String TEXT_423 = ", instanceValue);";
  protected final String TEXT_424 = NL + "\t\t\t\tif (value != null) return value;" + NL + "\t\t\t}" + NL + "\t\t\tcatch (Exception e)" + NL + "\t\t\t{" + NL + "\t\t\t\t// Keep trying other member types until all have failed." + NL + "\t\t\t}" + NL + "\t\t}";
  protected final String TEXT_425 = NL + "\t\tthrow new IllegalArgumentException(\"Invalid value: '\"+instanceValue+\"' for datatype :\"+eDataType.getName());";
  protected final String TEXT_426 = NL + "\t\treturn instanceValue == null ? null : convert";
  protected final String TEXT_427 = "(";
  protected final String TEXT_428 = "(";
  protected final String TEXT_429 = "(";
  protected final String TEXT_430 = ")instanceValue";
  protected final String TEXT_431 = ").";
  protected final String TEXT_432 = "()";
  protected final String TEXT_433 = ");";
  protected final String TEXT_434 = NL + "\t\treturn convert";
  protected final String TEXT_435 = "((";
  protected final String TEXT_436 = ")instanceValue);";
  protected final String TEXT_437 = NL + "\t\treturn super.convertToString(instanceValue);";
  protected final String TEXT_438 = NL + "\t\t// TODO: implement this method" + NL + "\t\t// Ensure that you remove @generated or mark it @generated NOT" + NL + "\t\tthrow new ";
  protected final String TEXT_439 = "();";
  protected final String TEXT_440 = NL + "\t\treturn super.convertToString(eDataType, instanceValue);";
  protected final String TEXT_441 = NL + "\t}" + NL;
  protected final String TEXT_442 = NL + "\t/**" + NL + "\t * Returns a new object of class '<em>";
  protected final String TEXT_443 = "</em>'." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @return a new object of class '<em>";
  protected final String TEXT_444 = "</em>'." + NL + "\t * @generated" + NL + "\t */" + NL + "\t";
  protected final String TEXT_445 = " create";
  protected final String TEXT_446 = "();" + NL;
  protected final String TEXT_447 = NL + "\t/**" + NL + "\t * Returns an instance of data type '<em>";
  protected final String TEXT_448 = "</em>' corresponding the given literal." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @param literal a literal of the data type." + NL + "\t * @return a new instance value of the data type." + NL + "\t * @generated" + NL + "\t */" + NL + "\t";
  protected final String TEXT_449 = " create";
  protected final String TEXT_450 = "(String literal);" + NL + "" + NL + "\t/**" + NL + "\t * Returns a literal representation of an instance of data type '<em>";
  protected final String TEXT_451 = "</em>'." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @param instanceValue an instance value of the data type." + NL + "\t * @return a literal representation of the instance value." + NL + "\t * @generated" + NL + "\t */" + NL + "\tString convert";
  protected final String TEXT_452 = "(";
  protected final String TEXT_453 = " instanceValue);" + NL;
  protected final String TEXT_454 = NL + "\t/**" + NL + "\t * Returns the package supported by this factory." + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @return the package supported by this factory." + NL + "\t * @generated" + NL + "\t */" + NL + "\t";
  protected final String TEXT_455 = " get";
  protected final String TEXT_456 = "();" + NL;
  protected final String TEXT_457 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic ";
  protected final String TEXT_458 = " get";
  protected final String TEXT_459 = "()" + NL + "\t{" + NL + "\t\treturn (";
  protected final String TEXT_460 = ")getEPackage();" + NL + "\t}" + NL + "" + NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t * @deprecated" + NL + "\t * @generated" + NL + "\t */";
  protected final String TEXT_461 = NL + "\t@Deprecated";
  protected final String TEXT_462 = NL + "\tpublic static ";
  protected final String TEXT_463 = " getPackage()" + NL + "\t{" + NL + "\t\treturn ";
  protected final String TEXT_464 = ".eINSTANCE;" + NL + "\t}" + NL;
  protected final String TEXT_465 = NL + "} //";
  protected final String TEXT_466 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
/**
 * Copyright (c) 2002-2010 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 */

    GenPackage genPackage = (GenPackage)((Object[])argument)[0]; GenModel genModel=genPackage.getGenModel(); /* Trick to import java.util.* without warnings */Iterator.class.getName();
    boolean isInterface = Boolean.TRUE.equals(((Object[])argument)[1]); boolean isImplementation = Boolean.TRUE.equals(((Object[])argument)[2]);
    String publicStaticFinalFlag = isImplementation ? "public static final " : "";
    stringBuffer.append(TEXT_1);
    stringBuffer.append(TEXT_2);
    {GenBase copyrightHolder = argument instanceof GenBase ? (GenBase)argument : argument instanceof Object[] && ((Object[])argument)[0] instanceof GenBase ? (GenBase)((Object[])argument)[0] : null;
    if (copyrightHolder != null && copyrightHolder.hasCopyright()) {
    stringBuffer.append(TEXT_3);
    stringBuffer.append(copyrightHolder.getCopyright(copyrightHolder.getGenModel().getIndentation(stringBuffer)));
    }}
    stringBuffer.append(TEXT_4);
    if (isInterface || genModel.isSuppressInterfaces()) {
    stringBuffer.append(TEXT_5);
    stringBuffer.append(genPackage.getReflectionPackageName());
    stringBuffer.append(TEXT_6);
    } else {
    stringBuffer.append(TEXT_7);
    stringBuffer.append(genPackage.getClassPackageName());
    stringBuffer.append(TEXT_8);
    }
    stringBuffer.append(TEXT_9);
    if (isImplementation) {
    genModel.addPseudoImport("org.eclipse.emf.ecore.impl.MinimalEObjectImpl.Container");
    genModel.addPseudoImport("org.eclipse.emf.ecore.impl.MinimalEObjectImpl.Container.Dynamic");
    genModel.addImport("org.eclipse.emf.ecore.EClass");
    genModel.addImport("org.eclipse.emf.ecore.EObject");
    if (!genPackage.hasJavaLangConflict() && !genPackage.hasInterfaceImplConflict() && !genPackage.getClassPackageName().equals(genPackage.getInterfacePackageName())) genModel.addImport(genPackage.getInterfacePackageName() + ".*");
    }
    genModel.markImportLocation(stringBuffer);
    stringBuffer.append(TEXT_10);
    if (isInterface) {
    stringBuffer.append(TEXT_11);
    if (!genModel.isSuppressEMFMetaData()) {
    stringBuffer.append(TEXT_12);
    stringBuffer.append(genPackage.getQualifiedPackageInterfaceName());
    }
    stringBuffer.append(TEXT_13);
    } else {
    stringBuffer.append(TEXT_14);
    }
    if (isImplementation) {
    stringBuffer.append(TEXT_15);
    stringBuffer.append(genPackage.getFactoryClassName());
    stringBuffer.append(TEXT_16);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.impl.EFactoryImpl"));
    if (!genModel.isSuppressInterfaces()) {
    stringBuffer.append(TEXT_17);
    stringBuffer.append(genPackage.getImportedFactoryInterfaceName());
    }
    } else {
    stringBuffer.append(TEXT_18);
    stringBuffer.append(genPackage.getFactoryInterfaceName());
    if (!genModel.isSuppressEMFMetaData()) {
    stringBuffer.append(TEXT_19);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EFactory"));
    }
    }
    stringBuffer.append(TEXT_20);
    if (genModel.hasCopyrightField()) {
    stringBuffer.append(TEXT_21);
    stringBuffer.append(publicStaticFinalFlag);
    stringBuffer.append(genModel.getImportedName("java.lang.String"));
    stringBuffer.append(TEXT_22);
    stringBuffer.append(genModel.getCopyrightFieldLiteral());
    stringBuffer.append(TEXT_23);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_24);
    }
    if (isImplementation && (genModel.isSuppressEMFMetaData() || genModel.isSuppressInterfaces())) {
    stringBuffer.append(TEXT_25);
    stringBuffer.append(publicStaticFinalFlag);
    stringBuffer.append(genPackage.getFactoryClassName());
    stringBuffer.append(TEXT_26);
    }
    if (isInterface && genModel.isSuppressEMFMetaData()) {
    stringBuffer.append(TEXT_27);
    stringBuffer.append(publicStaticFinalFlag);
    stringBuffer.append(genPackage.getFactoryInterfaceName());
    stringBuffer.append(TEXT_28);
    stringBuffer.append(genPackage.getQualifiedFactoryClassName());
    stringBuffer.append(TEXT_29);
    } else if (isInterface && !genModel.isSuppressInterfaces()) {
    stringBuffer.append(TEXT_30);
    stringBuffer.append(publicStaticFinalFlag);
    stringBuffer.append(genPackage.getFactoryInterfaceName());
    stringBuffer.append(TEXT_31);
    stringBuffer.append(genPackage.getQualifiedFactoryClassName());
    stringBuffer.append(TEXT_32);
    }
    if (isImplementation) {
    stringBuffer.append(TEXT_33);
    String factoryType = genModel.isSuppressEMFMetaData() ? genPackage.getFactoryClassName() : genPackage.getImportedFactoryInterfaceName();
    stringBuffer.append(TEXT_34);
    stringBuffer.append(factoryType);
    stringBuffer.append(TEXT_35);
    stringBuffer.append(factoryType);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(genPackage.getFactoryName());
    stringBuffer.append(TEXT_37);
    stringBuffer.append(factoryType);
    stringBuffer.append(TEXT_38);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EPackage"));
    stringBuffer.append(TEXT_39);
    stringBuffer.append(genPackage.getNSURI());
    stringBuffer.append(TEXT_40);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(TEXT_41);
    stringBuffer.append(genPackage.getFactoryName());
    stringBuffer.append(TEXT_42);
    stringBuffer.append(genPackage.getFactoryName());
    stringBuffer.append(TEXT_43);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.plugin.EcorePlugin"));
    stringBuffer.append(TEXT_44);
    stringBuffer.append(genPackage.getImportedFactoryClassName());
    stringBuffer.append(TEXT_45);
    stringBuffer.append(genPackage.getFactoryClassName());
    stringBuffer.append(TEXT_46);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_47);
    }
    stringBuffer.append(TEXT_48);
    for (GenClass genClass : genPackage.getGenClasses()) {
    if (!genClass.isAbstract()) {
    stringBuffer.append(TEXT_49);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_50);
    stringBuffer.append(genClass.getClassifierID());
    stringBuffer.append(TEXT_51);
    stringBuffer.append(!genClass.isEObjectExtension() ? "(EObject)" : "" );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_53);
    }
    }
    stringBuffer.append(TEXT_54);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_55);
    if (!genPackage.getAllGenDataTypes().isEmpty()) {
    stringBuffer.append(TEXT_56);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_57);
    }
    stringBuffer.append(TEXT_58);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EDataType"));
    stringBuffer.append(TEXT_59);
    for (GenDataType genDataType : genPackage.getAllGenDataTypes()) {
    if (genDataType.isSerializable()) {
    stringBuffer.append(TEXT_60);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_61);
    stringBuffer.append(genDataType.getClassifierID());
    stringBuffer.append(TEXT_62);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_63);
    }
    }
    stringBuffer.append(TEXT_64);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_65);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_66);
    }
    stringBuffer.append(TEXT_67);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EDataType"));
    stringBuffer.append(TEXT_68);
    for (GenDataType genDataType : genPackage.getAllGenDataTypes()) {
    if (genDataType.isSerializable()) {
    stringBuffer.append(TEXT_69);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_70);
    stringBuffer.append(genDataType.getClassifierID());
    stringBuffer.append(TEXT_71);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_72);
    }
    }
    stringBuffer.append(TEXT_73);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(TEXT_74);
    }
    for (GenClass genClass : genPackage.getGenClasses()) {
    if (!genClass.isAbstract()) {
    stringBuffer.append(TEXT_75);
    stringBuffer.append(genClass.getTypeParameters());
    stringBuffer.append(genClass.getImportedInterfaceName());
    stringBuffer.append(genClass.getInterfaceTypeArguments());
    stringBuffer.append(TEXT_76);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_77);
    if (genClass.isDynamic()) {
    stringBuffer.append(TEXT_78);
    stringBuffer.append(genClass.getImportedInterfaceName());
    stringBuffer.append(genClass.getInterfaceTypeArguments());
    stringBuffer.append(TEXT_79);
    stringBuffer.append(genClass.getSafeUncapName());
    stringBuffer.append(TEXT_80);
    stringBuffer.append(genClass.getCastFromEObject());
    stringBuffer.append(TEXT_81);
    stringBuffer.append(genClass.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_82);
    } else {
    stringBuffer.append(TEXT_83);
    stringBuffer.append(genClass.getImportedClassName());
    stringBuffer.append(genClass.getClassTypeArguments());
    stringBuffer.append(TEXT_84);
    stringBuffer.append(genClass.getSafeUncapName());
    stringBuffer.append(TEXT_85);
    stringBuffer.append(genClass.getImportedClassName());
    stringBuffer.append(genClass.getClassTypeArguments());
    stringBuffer.append(TEXT_86);
    if (genModel.isSuppressInterfaces() && !genPackage.getReflectionPackageName().equals(genPackage.getInterfacePackageName())) {
    stringBuffer.append(TEXT_87);
    }
    stringBuffer.append(TEXT_88);
    }
    stringBuffer.append(TEXT_89);
    stringBuffer.append(genClass.getSafeUncapName());
    stringBuffer.append(TEXT_90);
    }
    }
    for (GenDataType genDataType : genPackage.getAllGenDataTypes()) {
    if (genDataType.isSerializable()) {
    if (genPackage.isDataTypeConverters() || genDataType.hasCreatorBody()) { String eDataType = genDataType.getQualifiedClassifierAccessor();
    stringBuffer.append(TEXT_91);
    if (genModel.useGenerics() && genDataType.isUncheckedCast() && !genDataType.hasCreatorBody()) {
    stringBuffer.append(TEXT_92);
    }
    stringBuffer.append(TEXT_93);
    stringBuffer.append(genDataType.getImportedParameterizedInstanceClassName());
    stringBuffer.append(TEXT_94);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_95);
    if (genDataType.hasCreatorBody()) {
    stringBuffer.append(TEXT_96);
    } else {
    stringBuffer.append(TEXT_97);
    }
    stringBuffer.append(TEXT_98);
    if (genDataType.hasCreatorBody()) {
    stringBuffer.append(TEXT_99);
    stringBuffer.append(genDataType.getCreatorBody(genModel.getIndentation(stringBuffer)));
    } else if (genDataType instanceof GenEnum) {
    stringBuffer.append(TEXT_100);
    stringBuffer.append(genDataType.getImportedInstanceClassName());
    stringBuffer.append(TEXT_101);
    stringBuffer.append(genDataType.getImportedInstanceClassName());
    stringBuffer.append(TEXT_102);
    stringBuffer.append(eDataType);
    stringBuffer.append(TEXT_103);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(genModel.getNonNLS(3));
    stringBuffer.append(TEXT_104);
    } else if (genDataType.getBaseType() != null) { GenDataType genBaseType = genDataType.getBaseType(); boolean isPrimitiveConversion = !genDataType.isPrimitiveType() && genBaseType.isPrimitiveType();
    if (genBaseType.getGenPackage() == genPackage) {
    if (isPrimitiveConversion && genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_105);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_106);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_107);
    } else {
    stringBuffer.append(TEXT_108);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_109);
    }
    } else if (genBaseType.getGenPackage().isDataTypeConverters()) {
    if (isPrimitiveConversion && genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_110);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_111);
    stringBuffer.append(genBaseType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_112);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_113);
    } else {
    stringBuffer.append(TEXT_114);
    stringBuffer.append(genBaseType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_115);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_116);
    }
    } else {
    stringBuffer.append(TEXT_117);
    if (!genDataType.isObjectType()) {
    stringBuffer.append(TEXT_118);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_119);
    }
    stringBuffer.append(genBaseType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_120);
    stringBuffer.append(genBaseType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_121);
    }
    } else if (genDataType.getItemType() != null) { GenDataType genItemType = genDataType.getItemType(); 
    stringBuffer.append(TEXT_122);
    stringBuffer.append(genDataType.getImportedParameterizedInstanceClassName());
    stringBuffer.append(TEXT_123);
    stringBuffer.append(genModel.getImportedName("java.util.ArrayList"));
    if (genModel.useGenerics()) {
    stringBuffer.append(TEXT_124);
    stringBuffer.append(genItemType.getObjectType().getImportedParameterizedInstanceClassName());
    stringBuffer.append(TEXT_125);
    }
    stringBuffer.append(TEXT_126);
    if (genModel.getRuntimeVersion().getValue() < GenRuntimeVersion.EMF26_VALUE) {
    stringBuffer.append(TEXT_127);
    stringBuffer.append(genModel.getImportedName("java.util.StringTokenizer"));
    stringBuffer.append(TEXT_128);
    stringBuffer.append(genModel.getImportedName("java.util.StringTokenizer"));
    stringBuffer.append(TEXT_129);
    } else {
    stringBuffer.append(TEXT_130);
    }
    stringBuffer.append(TEXT_131);
    if (genModel.getRuntimeVersion().getValue() < GenRuntimeVersion.EMF26_VALUE) {
    stringBuffer.append(TEXT_132);
    }
    if (genItemType.getGenPackage() == genPackage) {
    if (genPackage.isDataTypeConverters()) { genItemType = genItemType.getObjectType();
    stringBuffer.append(TEXT_133);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_134);
    } else {
    stringBuffer.append(TEXT_135);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_136);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_137);
    }
    } else {
    if (genItemType.getGenPackage().isDataTypeConverters()) { genItemType = genItemType.getObjectType();
    stringBuffer.append(TEXT_138);
    stringBuffer.append(genItemType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_139);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_140);
    } else {
    stringBuffer.append(TEXT_141);
    stringBuffer.append(genItemType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_142);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_143);
    }
    }
    stringBuffer.append(TEXT_144);
    } else if (!genDataType.getMemberTypes().isEmpty()) {
    stringBuffer.append(TEXT_145);
    stringBuffer.append(genDataType.getStaticValue(null));
    stringBuffer.append(TEXT_146);
    stringBuffer.append(genDataType.getImportedInstanceClassName());
    stringBuffer.append(TEXT_147);
    stringBuffer.append(genDataType.getStaticValue(null));
    stringBuffer.append(TEXT_148);
    for (GenDataType genMemberType : genDataType.getMemberTypes()) {
    stringBuffer.append(TEXT_149);
    if (genMemberType.getGenPackage() == genPackage) {
    if (genPackage.isDataTypeConverters()) { if (!genDataType.isPrimitiveType()) genMemberType = genMemberType.getObjectType();
    stringBuffer.append(TEXT_150);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_151);
    } else {
    stringBuffer.append(TEXT_152);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_153);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_154);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_155);
    }
    } else {
    if (genPackage.isDataTypeConverters()) { if (!genDataType.isPrimitiveType()) genMemberType = genMemberType.getObjectType();
    stringBuffer.append(TEXT_156);
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_157);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_158);
    } else {
    stringBuffer.append(TEXT_159);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_160);
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_161);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_162);
    }
    }
    stringBuffer.append(TEXT_163);
    if (!genDataType.isPrimitiveType()) {
    stringBuffer.append(TEXT_164);
    }
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.Diagnostician"));
    stringBuffer.append(TEXT_165);
    stringBuffer.append(eDataType);
    stringBuffer.append(TEXT_166);
    if (genDataType.isPrimitiveType() && genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_167);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_168);
    } else {
    stringBuffer.append(TEXT_169);
    }
    stringBuffer.append(TEXT_170);
    }
    stringBuffer.append(TEXT_171);
    if (!genDataType.isPrimitiveType()) {
    stringBuffer.append(TEXT_172);
    }
    stringBuffer.append(TEXT_173);
    } else if (genModel.useGenerics() && (genDataType.isArrayType() || !genDataType.getEcoreDataType().getETypeParameters().isEmpty() || genDataType.getEcoreDataType().getInstanceTypeName().contains("<"))) {
    stringBuffer.append(TEXT_174);
    stringBuffer.append(genDataType.getImportedParameterizedObjectInstanceClassName());
    stringBuffer.append(TEXT_175);
    } else if (genDataType.isArrayType()) {
    stringBuffer.append(TEXT_176);
    stringBuffer.append(genModel.getImportedName("java.lang.UnsupportedOperationException"));
    stringBuffer.append(TEXT_177);
    } else if (genDataType.isPrimitiveType() && genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_178);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_179);
    stringBuffer.append(eDataType);
    stringBuffer.append(TEXT_180);
    stringBuffer.append(genDataType.getPrimitiveValueFunction());
    stringBuffer.append(TEXT_181);
    } else {
    stringBuffer.append(TEXT_182);
    if (!genDataType.isObjectType()) {
    stringBuffer.append(TEXT_183);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_184);
    }
    stringBuffer.append(TEXT_185);
    stringBuffer.append(eDataType);
    stringBuffer.append(TEXT_186);
    }
    stringBuffer.append(TEXT_187);
    }
    stringBuffer.append(TEXT_188);
    if (!genPackage.isDataTypeConverters() && genModel.useGenerics() && genDataType.isUncheckedCast() && !genDataType.hasCreatorBody()) {
    stringBuffer.append(TEXT_189);
    }
    stringBuffer.append(TEXT_190);
    stringBuffer.append(genDataType.getImportedParameterizedObjectInstanceClassName());
    stringBuffer.append(TEXT_191);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_192);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EDataType"));
    stringBuffer.append(TEXT_193);
    if (genDataType instanceof GenEnum) {
    if (genPackage.isDataTypeConverters() || genDataType.hasCreatorBody()) {
    stringBuffer.append(TEXT_194);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_195);
    } else {
    stringBuffer.append(TEXT_196);
    stringBuffer.append(((GenEnum)genDataType).getImportedInstanceClassName());
    stringBuffer.append(TEXT_197);
    stringBuffer.append(((GenEnum)genDataType).getImportedInstanceClassName());
    stringBuffer.append(TEXT_198);
    stringBuffer.append(genModel.getNonNLS());
    stringBuffer.append(genModel.getNonNLS(2));
    stringBuffer.append(genModel.getNonNLS(3));
    stringBuffer.append(TEXT_199);
    }
    } else if (genDataType.getBaseType() != null) { GenDataType genBaseType = genDataType.getBaseType(); 
    if (genBaseType.getGenPackage() == genPackage) {
    stringBuffer.append(TEXT_200);
    if (!genDataType.getObjectInstanceClassName().equals(genBaseType.getObjectInstanceClassName())) {
    stringBuffer.append(TEXT_201);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_202);
    }
    stringBuffer.append(TEXT_203);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_204);
    stringBuffer.append(genBaseType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_205);
    } else {
    stringBuffer.append(TEXT_206);
    if (!genDataType.isObjectType()) {
    stringBuffer.append(TEXT_207);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_208);
    }
    stringBuffer.append(genBaseType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_209);
    stringBuffer.append(genBaseType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_210);
    }
    } else if (genDataType.getItemType() != null) { GenDataType genItemType = genDataType.getItemType(); 
    if (genPackage.isDataTypeConverters()) {
    stringBuffer.append(TEXT_211);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_212);
    } else {
    stringBuffer.append(TEXT_213);
    stringBuffer.append(genDataType.getImportedParameterizedObjectInstanceClassName());
    stringBuffer.append(TEXT_214);
    stringBuffer.append(genModel.getImportedName("java.util.ArrayList"));
    if (genModel.useGenerics()) {
    stringBuffer.append(TEXT_215);
    stringBuffer.append(genItemType.getObjectType().getImportedParameterizedInstanceClassName());
    stringBuffer.append(TEXT_216);
    }
    stringBuffer.append(TEXT_217);
    if (genModel.getRuntimeVersion().getValue() < GenRuntimeVersion.EMF26_VALUE) {
    stringBuffer.append(TEXT_218);
    stringBuffer.append(genModel.getImportedName("java.util.StringTokenizer"));
    stringBuffer.append(TEXT_219);
    stringBuffer.append(genModel.getImportedName("java.util.StringTokenizer"));
    stringBuffer.append(TEXT_220);
    } else {
    stringBuffer.append(TEXT_221);
    }
    stringBuffer.append(TEXT_222);
    if (genModel.getRuntimeVersion().getValue() < GenRuntimeVersion.EMF26_VALUE) {
    stringBuffer.append(TEXT_223);
    }
    if (genItemType.getGenPackage() == genPackage) {
    stringBuffer.append(TEXT_224);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_225);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_226);
    } else {
    stringBuffer.append(TEXT_227);
    if (!genItemType.isObjectType()) {
    stringBuffer.append(TEXT_228);
    stringBuffer.append(genItemType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_229);
    }
    stringBuffer.append(genItemType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_230);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_231);
    }
    stringBuffer.append(TEXT_232);
    }
    } else if (!genDataType.getMemberTypes().isEmpty()) {
    if (genPackage.isDataTypeConverters()) {
    if (genDataType.isPrimitiveType() && genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_233);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_234);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_235);
    } else {
    stringBuffer.append(TEXT_236);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_237);
    }
    } else {
    stringBuffer.append(TEXT_238);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_239);
    for (GenDataType genMemberType : genDataType.getMemberTypes()) {
    stringBuffer.append(TEXT_240);
    if (genMemberType.getGenPackage() == genPackage) {
    stringBuffer.append(TEXT_241);
    if (!genDataType.isObjectType() && !genDataType.getObjectInstanceClassName().equals(genMemberType.getObjectInstanceClassName())) {
    stringBuffer.append(TEXT_242);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_243);
    }
    stringBuffer.append(TEXT_244);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_245);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_246);
    } else {
    stringBuffer.append(TEXT_247);
    if (!genDataType.isObjectType()) {
    stringBuffer.append(TEXT_248);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_249);
    }
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_250);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_251);
    }
    stringBuffer.append(TEXT_252);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.util.Diagnostician"));
    stringBuffer.append(TEXT_253);
    }
    stringBuffer.append(TEXT_254);
    }
    } else if (genPackage.isDataTypeConverters() || genDataType.hasCreatorBody()) {
    stringBuffer.append(TEXT_255);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_256);
    } else if (genModel.useGenerics() && (genDataType.isArrayType() || !genDataType.getEcoreDataType().getETypeParameters().isEmpty() || genDataType.getEcoreDataType().getInstanceTypeName().contains("<"))) {
    stringBuffer.append(TEXT_257);
    if (!genDataType.isObjectType()) {
    stringBuffer.append(TEXT_258);
    stringBuffer.append(genDataType.getImportedParameterizedObjectInstanceClassName());
    stringBuffer.append(TEXT_259);
    }
    stringBuffer.append(TEXT_260);
    } else if (genDataType.isArrayType()) {
    stringBuffer.append(TEXT_261);
    stringBuffer.append(genModel.getImportedName("java.lang.UnsupportedOperationException"));
    stringBuffer.append(TEXT_262);
    } else {
    stringBuffer.append(TEXT_263);
    if (!genDataType.isObjectType()) {
    stringBuffer.append(TEXT_264);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_265);
    }
    stringBuffer.append(TEXT_266);
    }
    stringBuffer.append(TEXT_267);
    if (genPackage.isDataTypeConverters() || genDataType.hasConverterBody()) { String eDataType = genDataType.getQualifiedClassifierAccessor();
    stringBuffer.append(TEXT_268);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_269);
    stringBuffer.append(genDataType.getImportedBoundedWildcardInstanceClassName());
    stringBuffer.append(TEXT_270);
    if (genDataType.hasConverterBody()) {
    stringBuffer.append(TEXT_271);
    } else {
    stringBuffer.append(TEXT_272);
    }
    stringBuffer.append(TEXT_273);
    if (genDataType.hasConverterBody()) {
    stringBuffer.append(TEXT_274);
    stringBuffer.append(genDataType.getConverterBody(genModel.getIndentation(stringBuffer)));
    } else if (genDataType instanceof GenEnum) {
    stringBuffer.append(TEXT_275);
    } else if (genDataType.getBaseType() != null) { GenDataType genBaseType = genDataType.getBaseType(); boolean isPrimitiveConversion = !genDataType.isPrimitiveType() && genBaseType.isPrimitiveType();
    if (genBaseType.getGenPackage() == genPackage) {
    if (isPrimitiveConversion) {
    stringBuffer.append(TEXT_276);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_277);
    if (genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_278);
    stringBuffer.append(genBaseType.getPrimitiveValueFunction());
    stringBuffer.append(TEXT_279);
    }
    stringBuffer.append(TEXT_280);
    } else {
    stringBuffer.append(TEXT_281);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_282);
    }
    } else if (genBaseType.getGenPackage().isDataTypeConverters()) {
    stringBuffer.append(TEXT_283);
    stringBuffer.append(genBaseType.getGenPackage().getQualifiedFactoryInstanceAccessor());
    stringBuffer.append(TEXT_284);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_285);
    } else {
    stringBuffer.append(TEXT_286);
    stringBuffer.append(genBaseType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_287);
    stringBuffer.append(genBaseType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_288);
    }
    } else if (genDataType.getItemType() != null) { GenDataType genItemType = genDataType.getItemType(); 
    stringBuffer.append(TEXT_289);
    stringBuffer.append(genModel.getImportedName("java.lang.StringBuffer"));
    stringBuffer.append(TEXT_290);
    stringBuffer.append(genModel.getImportedName("java.lang.StringBuffer"));
    stringBuffer.append(TEXT_291);
    String item; if (!genModel.useGenerics()) { item = "i.next()"; 
    stringBuffer.append(TEXT_292);
    stringBuffer.append(genModel.getImportedName("java.util.Iterator"));
    stringBuffer.append(TEXT_293);
    } else { item = "item";
    stringBuffer.append(TEXT_294);
    stringBuffer.append(genModel.getImportedName("java.lang.Object"));
    stringBuffer.append(TEXT_295);
    }
    stringBuffer.append(TEXT_296);
    if (genItemType.getGenPackage() == genPackage) {
    if (genPackage.isDataTypeConverters()) { genItemType = genItemType.getObjectType();
    stringBuffer.append(TEXT_297);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_298);
    stringBuffer.append(genItemType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_299);
    stringBuffer.append(item);
    stringBuffer.append(TEXT_300);
    } else {
    stringBuffer.append(TEXT_301);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_302);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_303);
    stringBuffer.append(item);
    stringBuffer.append(TEXT_304);
    }
    } else {
    if (genItemType.getGenPackage().isDataTypeConverters()) { genItemType = genItemType.getObjectType();
    stringBuffer.append(TEXT_305);
    stringBuffer.append(genItemType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_306);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_307);
    stringBuffer.append(genItemType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_308);
    stringBuffer.append(item);
    stringBuffer.append(TEXT_309);
    } else {
    stringBuffer.append(TEXT_310);
    stringBuffer.append(genItemType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_311);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_312);
    stringBuffer.append(item);
    stringBuffer.append(TEXT_313);
    }
    }
    stringBuffer.append(TEXT_314);
    } else if (!genDataType.getMemberTypes().isEmpty()) {
    if (!genDataType.isPrimitiveType()) {
    stringBuffer.append(TEXT_315);
    for (GenDataType genMemberType : genDataType.getMemberTypes()) {
    stringBuffer.append(TEXT_316);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_317);
    if (genMemberType.getGenPackage() == genPackage) {
    if (genPackage.isDataTypeConverters()) {
    if (genMemberType.getQualifiedInstanceClassName().equals(genDataType.getQualifiedInstanceClassName())) {
    stringBuffer.append(TEXT_318);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_319);
    } else if (genMemberType.isPrimitiveType() && genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_320);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_321);
    stringBuffer.append(genMemberType.getObjectType().getImportedInstanceClassName());
    stringBuffer.append(TEXT_322);
    stringBuffer.append(genMemberType.getPrimitiveValueFunction());
    stringBuffer.append(TEXT_323);
    } else {
    stringBuffer.append(TEXT_324);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_325);
    stringBuffer.append(genMemberType.getObjectType().getImportedBoundedWildcardInstanceClassName());
    stringBuffer.append(TEXT_326);
    }
    } else {
    stringBuffer.append(TEXT_327);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_328);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_329);
    }
    } else {
    if (genMemberType.getGenPackage().isDataTypeConverters()) { genMemberType = genMemberType.getObjectType();
    stringBuffer.append(TEXT_330);
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_331);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_332);
    stringBuffer.append(genMemberType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_333);
    } else {
    stringBuffer.append(TEXT_334);
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_335);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_336);
    }
    }
    stringBuffer.append(TEXT_337);
    }
    } else {
    for (GenDataType genMemberType : genDataType.getMemberTypes()) {
    stringBuffer.append(TEXT_338);
    if (genMemberType.getGenPackage() == genPackage) {
    if (genPackage.isDataTypeConverters()) {
    stringBuffer.append(TEXT_339);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_340);
    } else {
    stringBuffer.append(TEXT_341);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_342);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_343);
    if (genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_344);
    stringBuffer.append(genMemberType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_345);
    } else {
    stringBuffer.append(TEXT_346);
    }
    stringBuffer.append(TEXT_347);
    }
    } else {
    if (genMemberType.getGenPackage().isDataTypeConverters()) {
    stringBuffer.append(TEXT_348);
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_349);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_350);
    } else {
    stringBuffer.append(TEXT_351);
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_352);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_353);
    if (genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_354);
    stringBuffer.append(genMemberType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_355);
    } else {
    stringBuffer.append(TEXT_356);
    }
    stringBuffer.append(TEXT_357);
    }
    }
    stringBuffer.append(TEXT_358);
    }
    }
    stringBuffer.append(TEXT_359);
    stringBuffer.append(eDataType);
    stringBuffer.append(TEXT_360);
    } else if (genModel.useGenerics() && (genDataType.isArrayType() || !genDataType.getEcoreDataType().getETypeParameters().isEmpty() || genDataType.getEcoreDataType().getInstanceTypeName().contains("<"))) {
    stringBuffer.append(TEXT_361);
    } else if (genDataType.isArrayType()) {
    stringBuffer.append(TEXT_362);
    stringBuffer.append(genModel.getImportedName("java.lang.UnsupportedOperationException"));
    stringBuffer.append(TEXT_363);
    } else if (genDataType.isPrimitiveType() && genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_364);
    stringBuffer.append(eDataType);
    stringBuffer.append(TEXT_365);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_366);
    } else {
    stringBuffer.append(TEXT_367);
    stringBuffer.append(eDataType);
    stringBuffer.append(TEXT_368);
    }
    stringBuffer.append(TEXT_369);
    }
    stringBuffer.append(TEXT_370);
    if (genModel.useGenerics() && (genDataType.getItemType() != null || genDataType.isUncheckedCast()) && (genPackage.isDataTypeConverters() || genDataType.hasCreatorBody())) {
    stringBuffer.append(TEXT_371);
    }
    stringBuffer.append(TEXT_372);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_373);
    stringBuffer.append(genModel.getImportedName("org.eclipse.emf.ecore.EDataType"));
    stringBuffer.append(TEXT_374);
    if (genDataType instanceof GenEnum) {
    if (genPackage.isDataTypeConverters() || genDataType.hasConverterBody()) {
    stringBuffer.append(TEXT_375);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_376);
    stringBuffer.append(genDataType.getImportedInstanceClassName());
    stringBuffer.append(TEXT_377);
    } else {
    stringBuffer.append(TEXT_378);
    }
    } else if (genDataType.getBaseType() != null) { GenDataType genBaseType = genDataType.getBaseType(); 
    if (genBaseType.getGenPackage() == genPackage) {
    stringBuffer.append(TEXT_379);
    stringBuffer.append(genBaseType.getName());
    stringBuffer.append(TEXT_380);
    stringBuffer.append(genBaseType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_381);
    } else {
    stringBuffer.append(TEXT_382);
    stringBuffer.append(genBaseType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_383);
    stringBuffer.append(genBaseType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_384);
    }
    } else if (genDataType.getItemType() != null) { GenDataType genItemType = genDataType.getItemType(); 
    if (genPackage.isDataTypeConverters() || genDataType.hasCreatorBody()) {
    stringBuffer.append(TEXT_385);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_386);
    stringBuffer.append(genDataType.getImportedBoundedWildcardInstanceClassName());
    stringBuffer.append(TEXT_387);
    } else { final String singleWildcard = genModel.useGenerics() ? "<?>" : "";
    stringBuffer.append(TEXT_388);
    stringBuffer.append(genModel.getImportedName("java.util.List"));
    stringBuffer.append(singleWildcard);
    stringBuffer.append(TEXT_389);
    stringBuffer.append(genModel.getImportedName("java.util.List"));
    stringBuffer.append(singleWildcard);
    stringBuffer.append(TEXT_390);
    stringBuffer.append(genModel.getImportedName("java.lang.StringBuffer"));
    stringBuffer.append(TEXT_391);
    stringBuffer.append(genModel.getImportedName("java.lang.StringBuffer"));
    stringBuffer.append(TEXT_392);
    String item; if (!genModel.useGenerics()) { item = "i.next()"; 
    stringBuffer.append(TEXT_393);
    stringBuffer.append(genModel.getImportedName("java.util.Iterator"));
    stringBuffer.append(TEXT_394);
    } else { item = "item";
    stringBuffer.append(TEXT_395);
    stringBuffer.append(genModel.getImportedName("java.lang.Object"));
    stringBuffer.append(TEXT_396);
    }
    stringBuffer.append(TEXT_397);
    if (genItemType.getGenPackage() == genPackage) {
    stringBuffer.append(TEXT_398);
    stringBuffer.append(genItemType.getName());
    stringBuffer.append(TEXT_399);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_400);
    stringBuffer.append(item);
    stringBuffer.append(TEXT_401);
    } else {
    stringBuffer.append(TEXT_402);
    stringBuffer.append(genItemType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_403);
    stringBuffer.append(genItemType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_404);
    stringBuffer.append(item);
    stringBuffer.append(TEXT_405);
    }
    stringBuffer.append(TEXT_406);
    }
    } else if (!genDataType.getMemberTypes().isEmpty()) {
    if (genPackage.isDataTypeConverters() || genDataType.hasConverterBody()) {
    if (genDataType.isPrimitiveType()) {
    stringBuffer.append(TEXT_407);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_408);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_409);
    if (genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_410);
    stringBuffer.append(genDataType.getPrimitiveValueFunction());
    stringBuffer.append(TEXT_411);
    }
    stringBuffer.append(TEXT_412);
    } else {
    stringBuffer.append(TEXT_413);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_414);
    }
    } else {
    stringBuffer.append(TEXT_415);
    for (GenDataType genMemberType : genDataType.getMemberTypes()) {
    stringBuffer.append(TEXT_416);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_417);
    if (genMemberType.getGenPackage() == genPackage) {
    stringBuffer.append(TEXT_418);
    stringBuffer.append(genMemberType.getName());
    stringBuffer.append(TEXT_419);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_420);
    } else {
    stringBuffer.append(TEXT_421);
    stringBuffer.append(genMemberType.getGenPackage().getQualifiedEFactoryInternalInstanceAccessor());
    stringBuffer.append(TEXT_422);
    stringBuffer.append(genMemberType.getQualifiedClassifierAccessor());
    stringBuffer.append(TEXT_423);
    }
    stringBuffer.append(TEXT_424);
    }
    stringBuffer.append(TEXT_425);
    }
    } else if (genPackage.isDataTypeConverters() || genDataType.hasConverterBody()) {
    if (genDataType.isPrimitiveType()) {
    stringBuffer.append(TEXT_426);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_427);
    if (genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_428);
    }
    stringBuffer.append(TEXT_429);
    stringBuffer.append(genDataType.getObjectInstanceClassName());
    stringBuffer.append(TEXT_430);
    if (genModel.getComplianceLevel().getValue() < GenJDKLevel.JDK50) {
    stringBuffer.append(TEXT_431);
    stringBuffer.append(genDataType.getPrimitiveValueFunction());
    stringBuffer.append(TEXT_432);
    }
    stringBuffer.append(TEXT_433);
    } else {
    stringBuffer.append(TEXT_434);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_435);
    stringBuffer.append(genDataType.getImportedBoundedWildcardInstanceClassName());
    stringBuffer.append(TEXT_436);
    }
    } else if (genModel.useGenerics() && (genDataType.isArrayType() || !genDataType.getEcoreDataType().getETypeParameters().isEmpty() || genDataType.getEcoreDataType().getInstanceTypeName().contains("<"))) {
    stringBuffer.append(TEXT_437);
    } else if (genDataType.isArrayType()) {
    stringBuffer.append(TEXT_438);
    stringBuffer.append(genModel.getImportedName("java.lang.UnsupportedOperationException"));
    stringBuffer.append(TEXT_439);
    } else {
    stringBuffer.append(TEXT_440);
    }
    stringBuffer.append(TEXT_441);
    }
    }
    } else {
    for (GenClass genClass : genPackage.getGenClasses()) {
    if (genClass.hasFactoryInterfaceCreateMethod()) {
    stringBuffer.append(TEXT_442);
    stringBuffer.append(genClass.getFormattedName());
    stringBuffer.append(TEXT_443);
    stringBuffer.append(genClass.getFormattedName());
    stringBuffer.append(TEXT_444);
    stringBuffer.append(genClass.getTypeParameters());
    stringBuffer.append(genClass.getImportedInterfaceName());
    stringBuffer.append(genClass.getInterfaceTypeArguments());
    stringBuffer.append(TEXT_445);
    stringBuffer.append(genClass.getName());
    stringBuffer.append(TEXT_446);
    }
    }
    if (genPackage.isDataTypeConverters()) {
    for (GenDataType genDataType : genPackage.getAllGenDataTypes()) {
    if (genDataType.isSerializable()) {
    stringBuffer.append(TEXT_447);
    stringBuffer.append(genDataType.getFormattedName());
    stringBuffer.append(TEXT_448);
    stringBuffer.append(genDataType.getImportedParameterizedInstanceClassName());
    stringBuffer.append(TEXT_449);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_450);
    stringBuffer.append(genDataType.getFormattedName());
    stringBuffer.append(TEXT_451);
    stringBuffer.append(genDataType.getName());
    stringBuffer.append(TEXT_452);
    stringBuffer.append(genDataType.getImportedBoundedWildcardInstanceClassName());
    stringBuffer.append(TEXT_453);
    }
    }
    }
    }
    if (!isImplementation && !genModel.isSuppressEMFMetaData()) {
    stringBuffer.append(TEXT_454);
    stringBuffer.append(genPackage.getPackageInterfaceName());
    stringBuffer.append(TEXT_455);
    stringBuffer.append(genPackage.getBasicPackageName());
    stringBuffer.append(TEXT_456);
    } else if (isImplementation) {
    stringBuffer.append(TEXT_457);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_458);
    stringBuffer.append(genPackage.getBasicPackageName());
    stringBuffer.append(TEXT_459);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_460);
    if (genModel.useClassOverrideAnnotation()) {
    stringBuffer.append(TEXT_461);
    }
    stringBuffer.append(TEXT_462);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_463);
    stringBuffer.append(genPackage.getImportedPackageInterfaceName());
    stringBuffer.append(TEXT_464);
    }
    stringBuffer.append(TEXT_465);
    stringBuffer.append(isInterface ? genPackage.getFactoryInterfaceName() : genPackage.getFactoryClassName());
    genModel.emitSortedImports();
    stringBuffer.append(TEXT_466);
    return stringBuffer.toString();
  }
}
