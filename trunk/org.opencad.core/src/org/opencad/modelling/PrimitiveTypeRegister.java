package org.opencad.modelling;

import java.util.HashSet;

public class PrimitiveTypeRegister {
	private static HashSet<Class<? extends Primitive>> types = new HashSet<Class<? extends Primitive>>(
			1);

	public static void registerPrimitiveType(
			Class<? extends Primitive> type) {
		types.add(type);
	}

	public static Object[] getPrimitiveTypes() {
		return types.toArray();
	}
}
