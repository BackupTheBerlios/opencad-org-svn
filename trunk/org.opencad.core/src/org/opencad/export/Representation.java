package org.opencad.export;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;

public class Representation implements Serializable {
	private static final long serialVersionUID = 1L;

	// Stores all the classNames used in the representation
	// Index is classId
	final String[] classNames;

	// Stores all the field names for each class
	// first index is classId
	// second index is nameId
	final String[][] names;

	// Stores all the classIds of all objects in the representation
	// This is a lookup table
	// Index is beanId
	final int[] beans;

	// Id of the root bean;
	final int bean;

	// Stores all the primitives in the representation
	byte[] bytes;

	short[] shorts;

	int[] ints;

	long[] longs;

	float[] floats;

	double[] doubles;

	boolean[] booleans;

	char[] chars;

	// Stores all the ids of the values of each object
	// First index is beanId
	// Second index is an index in one of the primitive tables or object table
	// above
	final int[][] values;

	public Representation(Object object) throws InvocationTargetException,
			IllegalAccessException {
		/*
		 * Before we can do anything, we'll need a list of all the objects in
		 * the current object. To do that, we'll use IdentityHashMap. We will
		 * make a method that recursevly finds out such a map for any given
		 * object.
		 * 
		 * All primitive types will be wrapped after this traversal. We will
		 * have to unwrap them later.
		 */
		IdentityHashMap<Object, Integer> manifest = new IdentityHashMap<Object, Integer>();
		getObjectManifest(object, manifest);
		bean = manifest.get(object);

		HashMap<Class<?>, Integer> types = new HashMap<Class<?>, Integer>();
		int classId = 0;
		int beanId = 0;
		beans = new int[manifest.size()];
		for (Object item : manifest.keySet()) {
			/* This builds the types map */
			if (!types.containsKey(item.getClass())) {
				types.put(item.getClass(), classId++);
			}
			/* This builds the beans vector */
			beans[beanId++] = types.get(item.getClass());
		}
		classNames = new String[types.size()];
		names = new String[classNames.length][];
		for (Class<?> type : types.keySet()) {
			classId = types.get(type);
			/* This builds the class names vector */
			classNames[classId] = type.getCanonicalName();
			HashSet<BeanField> fields = BeanDescriptorFactory
					.getDescriptor(type).fields;
			/* This builds the names vector */
			names[classId] = new String[fields.size()];
			int fieldId = 0;
			for (BeanField field : BeanDescriptorFactory.getDescriptor(type).fields) {
				names[classId][fieldId++] = field.name;
			}
		}
		HashMap<Class<?>, HashMap<Object, Integer>> mapFinder = new HashMap<Class<?>, HashMap<Object, Integer>>();
		for (Class<?> primitiveType : primitives) {
			mapFinder.put(primitiveType, new HashMap<Object, Integer>());
		}
		values = new int[manifest.size()][];
		for (Object item : manifest.keySet()) {
			BeanDescriptor bd = BeanDescriptorFactory.getDescriptor(item
					.getClass());
			beanId = manifest.get(item);
			values[beanId] = new int[bd.fields.size()];
			int fieldId = 0;
			for (BeanField field : bd.fields) {
				Object value = field.getter.invoke(item, new Object[] {});
				if (value != null) {
					if (isWrappedPrimitive(value.getClass())) {
						HashMap<Object, Integer> map = mapFinder.get(value
								.getClass());
						int valueId = map.size();
						map.put(value, valueId);
						values[beanId][fieldId++] = valueId;
					} else {
						values[beanId][fieldId++] = manifest.get(value);
					}
				}
			}
		}
		for (Class primitiveType : primitives) {
			HashMap<Object, Integer> map = mapFinder.get(primitiveType);
			int size = map.size();
			if (Byte.class.isAssignableFrom(primitiveType)) {
				bytes = new byte[size];
				for (Object primitive : map.keySet()) {
					bytes[map.get(primitive)] = (Byte) primitive;
				}
			} else if (Short.class.isAssignableFrom(primitiveType)) {
				shorts = new short[size];
				for (Object primitive : map.keySet()) {
					shorts[map.get(primitive)] = (Short) primitive;
				}
			} else if (Integer.class.isAssignableFrom(primitiveType)) {
				ints = new int[size];
				for (Object primitive : map.keySet()) {
					ints[map.get(primitive)] = (Integer) primitive;
				}
			} else if (Long.class.isAssignableFrom(primitiveType)) {
				longs = new long[size];
				for (Object primitive : map.keySet()) {
					longs[map.get(primitive)] = (Long) primitive;
				}
			} else if (Float.class.isAssignableFrom(primitiveType)) {
				floats = new float[size];
				for (Object primitive : map.keySet()) {
					floats[map.get(primitive)] = (Float) primitive;
				}
			} else if (Double.class.isAssignableFrom(primitiveType)) {
				doubles = new double[size];
				for (Object primitive : map.keySet()) {
					doubles[map.get(primitive)] = (Double) primitive;
				}
			} else if (Boolean.class.isAssignableFrom(primitiveType)) {
				booleans = new boolean[size];
				for (Object primitive : map.keySet()) {
					booleans[map.get(primitive)] = (Boolean) primitive;
				}
			} else if (Character.class.isAssignableFrom(primitiveType)) {
				chars = new char[size];
				for (Object primitive : map.keySet()) {
					chars[map.get(primitive)] = (Character) primitive;
				}
			}
		}
	}

	static final Class[] primitives = new Class[] { Byte.class, Short.class,
			Integer.class, Long.class, Float.class, Double.class,
			Boolean.class, Character.class };

	boolean isWrappedPrimitive(Class<?> type) {
		for (Class<?> primitiveType : primitives) {
			if (primitiveType.isAssignableFrom(type))
				return true;
		}
		return false;
	}

	public void getObjectManifest(Object object,
			IdentityHashMap<Object, Integer> found)
			throws InvocationTargetException, IllegalAccessException {
		int beanId = found.size();
		if (!found.containsKey(object)) {
			found.put(object, beanId++);
		}
		BeanDescriptor beanDescriptor = BeanDescriptorFactory
				.getDescriptor(object.getClass());
		for (BeanField field : beanDescriptor.fields) {
			Object value = field.getter.invoke(object, new Object[] {});
			if (value != null && !found.containsKey(value)) {
				if (!isWrappedPrimitive(value.getClass())) {
					getObjectManifest(value, found);
					found.put(value, beanId++);
				}
			}
		}
	}

	public Object resurect() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Object[] objects = new Object[beans.length];

		for (int beanId = 0; beanId < beans.length; beanId++) {
			int classId = beans[beanId];
			Class<?> type = Class.forName(classNames[classId]);
			objects[beanId] = type.newInstance();
		}
		for (int beanId = 0; beanId < beans.length; beanId++) {
			int classId = beans[beanId];
			Class<?> type = Class.forName(classNames[classId]);
			BeanDescriptor bd = BeanDescriptorFactory.getDescriptor(type);
			HashMap<String, BeanField> fields = new HashMap<String, BeanField>();
			for (BeanField field : bd.fields) {
				fields.put(field.name, field);
			}
			for (int nameId = 0; nameId < values[beanId].length; nameId++) {
				String fieldName = names[classId][nameId];
				BeanField field = fields.get(fieldName);
				Object value;
				Class<?> primitiveType = field.getter.getReturnType();
				if (Byte.class.isAssignableFrom(primitiveType)) {
					value = bytes[values[beanId][nameId]];
				} else if (Short.class.isAssignableFrom(primitiveType)) {
					value = shorts[values[beanId][nameId]];
				} else if (Integer.class.isAssignableFrom(primitiveType)) {
					value = ints[values[beanId][nameId]];
				} else if (Long.class.isAssignableFrom(primitiveType)) {
					value = longs[values[beanId][nameId]];
				} else if (Float.class.isAssignableFrom(primitiveType)) {
					value = floats[values[beanId][nameId]];
				} else if (Double.class.isAssignableFrom(primitiveType)) {
					value = doubles[values[beanId][nameId]];
				} else if (Boolean.class.isAssignableFrom(primitiveType)) {
					value = booleans[values[beanId][nameId]];
				} else if (Character.class.isAssignableFrom(primitiveType)) {
					value = chars[values[beanId][nameId]];
				} else {
					value = objects[values[beanId][nameId]];
				}
				if (field != null) {
					System.out
							.println(String
									.format(
											"Will call %s.%s(%s) on %s with %s; Lookup was done with %s",
											field.setter.getDeclaringClass()
													.getSimpleName(),
											field.setter.getName(),
											field.setter.getParameterTypes()[0]
													.getSimpleName(),
											objects[beanId].getClass()
													.getSimpleName(), value
													.getClass(), primitiveType
													.getSimpleName()));
					field.setter
							.invoke(objects[beanId], new Object[] { value });
				}
			}
		}
		return objects[bean];
	}
}