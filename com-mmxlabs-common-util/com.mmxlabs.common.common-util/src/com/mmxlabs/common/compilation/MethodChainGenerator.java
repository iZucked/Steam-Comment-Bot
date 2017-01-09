/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.compilation;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.mmxlabs.common.CollectionsUtil;

/**
 * A class which uses ASM to create an {@link ITransformer} which calls a chain of getters.
 * 
 * @author Tom Hinton
 * 
 */
public class MethodChainGenerator implements Opcodes {
	private final AtomicInteger counter = new AtomicInteger(0);
	private final static String GENERATED_CLASS_PREFIX = "GeneratedMethodChain_";
	private final static String OUTPUT_PACKAGE = "com.mmxlabs.common.compilation";

	public Class<? extends ITransformer> createTransformer(final List<Method> chain, final IInjectableClassLoader loader) {
		final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

		final String newClassName = GENERATED_CLASS_PREFIX + counter.getAndIncrement();

		final String fqn = OUTPUT_PACKAGE + "." + newClassName;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, OUTPUT_PACKAGE.replace('.', '/') + "/" + newClassName, null, "java/lang/Object", new String[] { OUTPUT_PACKAGE.replace('.', '/') + "/ITransformer" });

		addBlankConstructor(cw);

		addTransformMethod(cw, chain);

		cw.visitEnd();

		try {
			return loader.injectAndLoadClass(fqn, cw.toByteArray());
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generate the transform method. This should be pretty efficient, I can't really see how to make it any better.
	 * 
	 * @param cw
	 */
	private void addTransformMethod(final ClassWriter cw, final List<Method> chain) {
		final MethodVisitor mw = cw.visitMethod(ACC_PUBLIC, "transform", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
		mw.visitCode();

		// each step in the chain does this
		// 1. check that the top of the stack has the right type
		// 2. invoke the current getter method if it does
		// 2a. box / unbox if required.

		final Label returnNull = new Label();
		// load variable 1 onto the stack
		mw.visitVarInsn(ALOAD, 1);

		for (final Method m : chain) {
			// check that stack 1 contains something of the right type
			final Class<?> dc = m.getDeclaringClass();
			final String containerTypeName = dc.getCanonicalName().replace('.', '/');
			// first duplicate the top element, as the instanceof consumes
			// whatever is on the stack.
			mw.visitInsn(DUP);
			// test whether it has the right type
			// this pops the top of the stack, and pushes 1 if instanceof
			// succeeded, zero otherwise
			mw.visitTypeInsn(INSTANCEOF, containerTypeName);
			// return null if stack 1 contains 0; this pops the 1 or zero we
			// had, leaving the current object.
			mw.visitJumpInsn(IFEQ, returnNull);
			// perform checkcast (may not be needed? we did an instanceof, which
			// helpfully also checks for null)
			mw.visitTypeInsn(CHECKCAST, containerTypeName);
			// call method

			final String methodReturnCode = getTypeCode(m.getReturnType());
			if (dc.isInterface()) {
				mw.visitMethodInsn(INVOKEINTERFACE, containerTypeName, m.getName(), "()" + methodReturnCode, true);
			} else {
				mw.visitMethodInsn(INVOKEVIRTUAL, containerTypeName, m.getName(), "()" + methodReturnCode, false);
			}
			// result should now be at stack top
			final Class<?> typeOnRegister = m.getReturnType();
			if (typeOnRegister.isPrimitive()) {
				String boxedType = null;

				if (typeOnRegister == Boolean.TYPE) {
					boxedType = "Boolean";
				} else if (typeOnRegister == Character.TYPE) {
					boxedType = "Character";
				} else if (typeOnRegister == Byte.TYPE) {
					boxedType = "Byte";
				} else if (typeOnRegister == Short.TYPE) {
					boxedType = "Short";
				} else if (typeOnRegister == Integer.TYPE) {
					boxedType = "Integer";
				} else if (typeOnRegister == Long.TYPE) {
					boxedType = "Long";
				} else if (typeOnRegister == Float.TYPE) {
					boxedType = "Float";
				} else if (typeOnRegister == Double.TYPE) {
					boxedType = "Double";
				}
				// execute boxing valueOf call.
				if (boxedType != null) {
					boxedType = "java/lang/" + boxedType;
					final String boxingSignature = "(" + methodReturnCode + ")L" + boxedType + ";";
					mw.visitMethodInsn(INVOKESTATIC, boxedType, "valueOf", boxingSignature, false);
				}
			}

		}

		// OK, so hopefully we have called our method sequence, and we can
		// return the result which should be top of stack.
		mw.visitInsn(ARETURN);

		// add the return null block
		mw.visitLabel(returnNull);
		mw.visitFrame(F_SAME, 0, null, 0, null);
		mw.visitInsn(ACONST_NULL);
		mw.visitInsn(ARETURN);
		mw.visitMaxs(4, 2);

		mw.visitEnd();
	}

	/**
	 * @param returnType
	 * @return
	 */
	private String getTypeCode(final Class<?> type) {
		if (type == Boolean.TYPE) {
			return "Z";
		} else if (type == Character.TYPE) {
			return "C";
		} else if (type == Byte.TYPE) {
			return "B";
		} else if (type == Short.TYPE) {
			return "S";
		} else if (type == Integer.TYPE) {
			return "I";
		} else if (type == Long.TYPE) {
			return "J";
		} else if (type == Float.TYPE) {
			return "F";
		} else if (type == Double.TYPE) {
			return "D";
		} else if (type == Void.TYPE) {
			return "V";
		} else {
			return "L" + type.getCanonicalName().replace('.', '/') + ";";
		}
	}

	/**
	 * Add the default constructor.
	 * 
	 * @param cw
	 */
	private void addBlankConstructor(final ClassWriter cw) {
		final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

	public static void main(final String args[]) throws Exception {
		final MethodChainGenerator generator = new MethodChainGenerator();

		final Class<? extends ITransformer> tc = generator.createTransformer(CollectionsUtil.makeArrayList(Integer.class.getMethod("intValue"), String.class.getMethod("toLowerCase")),
				new InjectableClassLoader(generator.getClass().getClassLoader()));
		final ITransformer t = tc.newInstance();
		System.err.println(t.transform("hello world"));
	}
}
