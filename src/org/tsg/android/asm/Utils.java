package org.tsg.android.asm;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.*;

public final class Utils implements Opcodes {

	private Utils() { }

	public static void newAnonymousInnerOnClick(String className, String methodName, File file) {
		String anonName = className + "$1";

		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;

		cw.visit(V1_6, ACC_SUPER, anonName, null, "java/lang/Object", new String[] {"android/view/View$OnClickListener"});
		cw.visitAnnotation("Lorg/tsg/android/api/Annotations$NoTransform;", true);

		// cw.visitSource("MainActivity.java", null);

		cw.visitOuterClass(className, "onCreate", "(Landroid/os/Bundle;)V");
		cw.visitInnerClass(anonName, null, null, 0);
		cw.visitInnerClass("android/view/View$OnClickListener", "android/view/View", "OnClickListener", ACC_PUBLIC + ACC_STATIC + ACC_ABSTRACT + ACC_INTERFACE);

		cw.visitField(ACC_FINAL + ACC_SYNTHETIC, "this$0", "L" + className + ";", null, null).visitEnd();

		{
			mv = cw.visitMethod(0, "<init>", "(L" + className + ";)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, anonName, "this$0", "L" + className + ";");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "L" + anonName + ";", null, l0, l1, 0);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod(ACC_PUBLIC, "onClick", "(Landroid/view/View;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, anonName, "this$0", "L" + className + ";");
			mv.visitMethodInsn(INVOKEVIRTUAL, className, methodName, "()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "L" + anonName + ";", null, l0, l2, 0);
			mv.visitLocalVariable("v", "Landroid/view/View;", null, l0, l2, 1);
			mv.visitMaxs(1, 2);
			mv.visitEnd();
		}

		cw.visitEnd();

		try {
			File inner = new File(file.getParentFile(), file.getName().replace(".class", "$1.class"));

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(inner));
			bos.write(cw.toByteArray());
			bos.flush();
			bos.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
