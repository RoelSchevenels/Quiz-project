package Protocol;

import java.util.HashMap;

import Protocol.requests.Request;


public class requestManager {
	private static HashMap<String,requestListener> listeners = new HashMap<String, requestListener>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addRequestListener(Class requestClass,requestListener listener) {
		if(!requestClass.isAssignableFrom(Request.class) || requestClass.equals(Request.class)) {
			throw new IllegalArgumentException("the given class must be an extend on Request");
		}
		
		listeners.put(requestClass.getName(), listener);
	}
	
	public static void fireRequest(Request r) {
		String name = r.getClass().getName();
		requestListener listenener = listeners.get(name);
		if(listenener == null) {
			r.sendException("De server reageert niet op dit type request");
		} else {
			listenener.handleRequest(r);
		}
		
		
	}
	
	public static void removeRequestListener(requestListener listener) {
		for(String s: listeners.keySet()) {
			if(listeners.get(s).equals(listener)) {
				listeners.remove(s);
			}
		}
	}
	
	public static void removeRequestListener(Class requestClass) {
		listeners.remove(requestClass.getName());
	}
}
