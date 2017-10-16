package net.ddns.endercrypt.web.socket;

import javax.websocket.CloseReason;

public interface UserEndpointObject
{
	public void onMessage(String message);

	public void onDisconnect(CloseReason closeReason);
}
