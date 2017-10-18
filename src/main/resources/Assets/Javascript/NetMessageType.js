NetMessageType = {};

// send nothing
NetMessageType.PING = 0;

// send name
NetMessageType.ENDPOINT_LOGIN = 1;

//NOT USED FROM CLIENTSIDE
NetMessageType.SERVER_MESSAGE = 2;

// NOT USED FROM CLIENTSIDE
NetMessageType.USER_JOIN = 3;

//NOT USED FROM CLIENTSIDE
NetMessageType.USER_LEAVE = 4;

// send message
NetMessageType.CHATMESSAGE_LOCAL = 5;

// send position
NetMessageType.MOVE = 6;

//NOT USED FROM CLIENTSIDE
NetMessageType.TELEPORT = 7;

//NOT USED FROM CLIENTSIDE
NetMessageType.LOAD_ROOM = 8