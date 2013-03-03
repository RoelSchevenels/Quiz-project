package Protocol;

import java.util.HashMap;

import Protocol.requests.Request;


public class RequestManager {
	/**
	 * zorgt dat de juiste request listener wordt uitgevoerd bij het binnen komen van een request
	 * voorkomt de nood aan een gigantische switch
	 * @author Vrolijkx
	 * @author Roel
	 */
	private static HashMap<String,RequestListener> listeners = new HashMap<String, RequestListener>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addRequestListener(Class requestClass,RequestListener listener) {
		if(!Request.class.isAssignableFrom(requestClass) || requestClass.equals(Request.class)) {
			throw new IllegalArgumentException("the given class must be an extend on Request");
		}
		
		listeners.put(requestClass.getName(), listener);
	}
	
	public static void fireRequest(Request r) {
		String name = r.getClass().getName();
		RequestListener listenener = listeners.get(name);
		if(listenener == null) {
			r.sendException("De server reageert niet op dit type request");
		} else {
			listenener.handleRequest(r);
		}
	}
	
	public static void removeRequestListener(RequestListener listener) {
		for(String s: listeners.keySet()) {
			if(listeners.get(s).equals(listener)) {
				listeners.remove(s);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void removeRequestListener(Class requestClass) {
		listeners.remove(requestClass.getName());
	}
}
