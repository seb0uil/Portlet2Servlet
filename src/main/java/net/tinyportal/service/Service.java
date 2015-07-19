package net.tinyportal.service;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

/**
 * la classe Service sert de classe parente aux différentes implémentations pour
 * les services (cf. ServiceTest.java).
 * Celle-ci, à partir de la map {@link #services}, récupére les éléments nécessaires à la constitution
 * du proxy pour l'appel de l'implémentation du service.
 * @author seb0uil
 *
 * @param <T>
 */
public class Service<T> {

	private T service;
	
	/**
	 * Map de correspondance entre les noms des service et les serviceLoader.
	 * Chaque service doit être enregistré pour être accessible, et une entrée est donc
	 * créée dans la map.
	 */
	private static Map<String, ServiceLoader> services = new HashMap<String, ServiceLoader>();
	
	/**
	 * Définie l'interface du service et le nom de celui-ci
	 * Le nom permet de retrouver l'implémentation et le context de l'interface
	 * @param clazz
	 * @param serviceName
	 */
	public void setClass(Class<T> clazz, String serviceName) {
		if (service == null) {
				service = getProxy(clazz, serviceName);
		}
	}

	/**
	 * Retourne le service attendu
	 * @return
	 */
	public T getService() {
		return service;
	}

	
	/**
	 * Génère le proxy pour accéder à l'implémentation attendu du service
	 * @param clazz interface du service
	 * @param serviceName
	 * @return Un proxy à destination de l'implémentation du service
	 */
	@SuppressWarnings("unchecked")
	public T getProxy(Class<T> clazz, String serviceName) {
		T service = (T) Proxy.newProxyInstance(
				Service.class.getClassLoader(),
				new Class[] { clazz },
				new ServiceHandler(services.get(serviceName).getTargetServiceClassLoader(),
						services.get(serviceName).getClassName())
				);
		return service;
	}

	/**
	 * Enregistre un service et son implémentation
	 * @param serviceName
	 * @param className
	 * @param servletContext
	 */
	public static void addService(String serviceName, String className, ServletContext servletContext) {
		ServiceLoader sl = new ServiceLoader(className, servletContext);
		services.put(serviceName, sl);
	}

	/**
	 * ServiceLoader utilisé pour stocker la classe implémentant le service ainsi que le context
	 * dans lequel cette implémentation est disponible.
	 * @author SBE10599
	 *
	 */
	private static class ServiceLoader {
		/**
		 * fqdn de la classe implémentant le service
		 */
		String className;
		/**
		 * classLoader dans lequel {@link #className} est accessible
		 */
		ClassLoader targetServiceClassLoader;
		
		ServiceLoader(String className, ServletContext servletContext ) {
			this.className = className;
			targetServiceClassLoader = servletContext.getContext(servletContext.getContextPath()).getClassLoader();
		}

		/* Getter */
		public String getClassName() {
			return className;
		}

		public ClassLoader getTargetServiceClassLoader() {
			return targetServiceClassLoader;
		}
	}
}
