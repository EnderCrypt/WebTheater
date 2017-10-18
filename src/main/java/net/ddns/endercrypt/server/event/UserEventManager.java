package net.ddns.endercrypt.server.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UserEventManager<T>
{
	private Method[] methods;

	public UserEventManager(Class<T> clazz, int maxMessageType)
	{
		methods = new Method[maxMessageType];
		for (Method method : clazz.getDeclaredMethods())
		{
			WsEventListener wsEventListener = method.getAnnotation(WsEventListener.class);
			if (wsEventListener != null)
			{
				int index = wsEventListener.value();
				method.setAccessible(true);
				methods[index] = method;
			}
		}
	}

	public void activateEvent(T object, int event, String data) throws ReflectiveOperationException
	{
		Method method = methods[event];
		if (method == null)
		{
			throw new UnknownNetMessageType("Unknown event type: " + event);
		}
		try
		{
			method.invoke(object, data);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new ReflectiveOperationException(e);
		}
	}
}
