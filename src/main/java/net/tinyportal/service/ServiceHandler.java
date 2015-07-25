package net.tinyportal.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * La classe serviceHandler sert de proxy au service.
 * Il g�re le m�canisme de changement de classLoader et d'invocation
 * de la m�thode attendu sur l'impl�mentation du service.
 * @author seb0uil
 *
 */
public class ServiceHandler implements InvocationHandler {
	ClassLoader targetServiceClassLoader;
	String serviceName;
	Object instance;

	protected ServiceHandler(ClassLoader targetServiceClassLoader, String serviceName) {
		this.targetServiceClassLoader = targetServiceClassLoader;
		this.serviceName = serviceName;
		try {
			Class<?> clazz = targetServiceClassLoader.loadClass(serviceName);
			instance = clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (targetServiceClassLoader == null) {
			throw new Exception();
		}

		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(targetServiceClassLoader);
						
			Method getBeanMethod = instance.getClass().getMethod( method.getName(), method.getParameterTypes() );
			getBeanMethod.setAccessible(true);
			return  getBeanMethod.invoke(instance, args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
		return null;
	}
}
