package com.crazy.util;

import antlr.collections.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public abstract class BeanUtils extends org.springframework.beans.BeanUtils {

	public static void copyProperties(Object source, Object target) throws BeansException {
		BeanUtils.copyProperties(source, target, new String[] {});
	}

	public static void copyProperties(Object source, Object target, String... ignoreProperty) throws BeansException {
		BeanUtils.copyProperties(source, target, null, ignoreProperty);
	}

	public static void copyProperties(Object source, Object target, PropertyConvert pc, String... ignoreProperty) throws BeansException {
		copyProperties(1, source, target, pc, ignoreProperty);
	}

	public static void copyProperties(int level, Object source, Object target, PropertyConvert pc, String... ignoreProperty) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> targetClass = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(targetClass); /* 目标对象属性 */

		for (PropertyDescriptor targetPd : targetPds) {
			if (isIgnoreProperty(targetPd.getName(), ignoreProperty)) continue;

			if (targetPd.getWriteMethod() != null) {
				/* 获取源对象相对应的属性描述器 */
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());

				/* 如果无法找到对应的属性，则从属性转换器获取对应属性重新获取属性描述器 */
				if (null == sourcePd) {
					String targetPdName = null;
					if (null != pc) {
						targetPdName = pc.getTargetPropertyName(targetPd.getName());
					}
					if (targetPdName == null) {
						if (targetPd.getName().endsWith("Vo")) {
							targetPdName = targetPd.getName().substring(0, targetPd.getName().indexOf("Vo"));
						} else {
							targetPdName = targetPd.getName() + "Vo";
						}
					}
					sourcePd = getPropertyDescriptor(source.getClass(), targetPdName);
				}

				if (null != sourcePd && null != sourcePd.getReadMethod()) {

					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);

						// 数据类型是否匹配
						if (!sourcePd.getPropertyType().equals(targetPd.getPropertyType())) {
							// Log4J.out(sourcePd.getName()+":"+sourcePd.getPropertyType().getName()+"->"+targetPd.getPropertyType()+"【"+
							// source.getClass());
							// target是String强行复制，否则进入下次循环
							if (targetPd.getPropertyType().equals(String.class)) {
								value = String.valueOf(value);
							} else if (!ClassUtils.isPrimitiveOrWrapper(targetPd.getPropertyType()) && !ClassUtils.isPrimitiveOrWrapper(sourcePd.getPropertyType())) {
								Object targetValue = targetPd.getReadMethod().invoke(target);
								if (null == value) {
									continue;
								}
								if (null == targetValue) {
									if (!List.class.isAssignableFrom(targetPd.getPropertyType()) && !Map.class.isAssignableFrom(targetPd.getPropertyType())
											&& !Set.class.isAssignableFrom(targetPd.getPropertyType()) && !targetPd.getPropertyType().isArray()) {
										targetValue = targetPd.getPropertyType().newInstance();
										targetPd.getWriteMethod().invoke(target, targetValue);
									} else {
										continue;
									}
								}
								if (level < 5) {
									copyProperties(level + 1, value, targetValue, pc, ignoreProperty);
								}
								continue;
							} else {
								continue;
							}
						}

						// 如果 source 为 null 则不复制
						if (null != value) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}

	private static boolean isIgnoreProperty(String property, String[] ignoreProperty) {
		if (null != ignoreProperty && ignoreProperty.length > 0) {
			for (String tempProperty : ignoreProperty) {
				if (property.equals(tempProperty)) return true;
			}
		}
		return false;

	}

	public static String getValues(Object obj, Map<String, String> aliasMap) {
		StringBuffer strInfo = new StringBuffer("");
		Assert.notNull(obj, "must not be null");
		Field[] files = obj.getClass().getDeclaredFields();
		Assert.notNull(files, " field must not be null");
		for (Field file : files) {
			if (file.getType().equals(Set.class)) {
				continue;
			}
			String lable = file.getName();

			Object value = null;
			PropertyDescriptor propertyDescriptor = getPropertyDescriptor(obj.getClass(), lable);
			if (null == propertyDescriptor) {
				continue;
			}
			Method method = propertyDescriptor.getReadMethod();
			// 内循环不处理
			if (obj.getClass().equals(method.getReturnType())) {
				continue;
			}

			try {
				value = method.invoke(obj);
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			if (null != value) {
				if (null != aliasMap) lable = aliasMap.get(lable);
				if (value instanceof String || value instanceof Integer || value instanceof Float || value instanceof Double || value instanceof Date || value instanceof Boolean) {
					strInfo.append(lable + "=" + value);
					strInfo.append(";");
				} else {
					strInfo.append(value);
				}
			}
		}
		return strInfo.toString();
	}

	public static class A {

		private long id;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
	}

	public static class B {

		private A a;

		public A getA() {
			return a;
		}

		public void setA(A a) {
			this.a = a;
		}

	}

	public static class AVo {

		private long id;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
	}

	public static class BVo {

		private AVo aVo;

		public AVo getAVo() {
			return aVo;
		}

		public void setAVo(AVo aVo) {
			this.aVo = aVo;
		}

	}

	public static void main(String[] args) {
		BVo b = new BVo();

		A a = new A();
		a.setId(1l);

		B b2 = new B();
		b2.setA(a);

		BeanUtils.copyProperties(b2, b);
		System.out.println(b.getAVo().getId());
	}

}
