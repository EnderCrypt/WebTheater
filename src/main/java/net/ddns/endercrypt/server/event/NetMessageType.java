package net.ddns.endercrypt.server.event;

public class NetMessageType
{
	// send nothing
	public static final int PING = 0;

	// send id after recieving name
	public static final int ENDPOINT_LOGIN = 1;

	// send message
	public static final int SERVER_MESSAGE = 2;

	// send id and name and position
	public static final int USER_JOIN = 3;

	// send id
	public static final int USER_LEAVE = 4;

	// send user id and message
	public static final int CHATMESSAGE_LOCAL = 5;

	// send id and position
	public static final int SETPOS = 6;

	// TELEPORT/ send position
	public static final int MOVE = 7;

	// send room_width, room_height, is_video_room, all tiles
	// user join's and move should be sent directly after this
	public static final int LOAD_ROOM = 8;
}
