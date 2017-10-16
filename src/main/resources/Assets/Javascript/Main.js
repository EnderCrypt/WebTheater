Theater = {}
Theater.userID = null;
Theater.Listerners = [];

$(function()
{
	// elements
	Theater.LoginScreen = $(".LoginScreen")
	Theater.MainCanvas = $("#MainCanvas")[0];
	Theater.CanvasContext = Theater.MainCanvas.getContext("2d");

	// web socket
	Theater.WsUrl = "ws://" + location.host + "/WsTheater";
	if ('WebSocket' in window)
	{
		Theater.Websocket = new WebSocket(Theater.WsUrl);  
	}
	else if ('MozWebSocket' in window)
	{
		Theater.Websocket = new MozWebSocket(Theater.WsUrl);  
	}
	else
	{
	    alert("Youe web browser does not appear to support required technology");
		return
	}
	
	// listeners
	Theater.Websocket.onopen = function(){console.log("Websocket connected!")};
	Theater.Websocket.onclose = function(event){
		messageReload("Disconnected from server! (code: "+event.code+")\nReason: "+event.reason);
		return;
	};
	Theater.Websocket.onmessage = onMessage;

	// tiles
	$.ajax(
	{
		  method: "GET",
		  url: "Api/Tileset/Info"
	}).done(function(data)
	{
		Theater.TileInfo = data;
		console.log("Tileset Meta Info: ", Theater.TileInfo)
		Theater.Tiles = new Array();
		for (i=0;i<Theater.TileInfo.count;i++)
		{
			var img = new Image();
			img.src = "http://"+location.host+"/Api/Tileset/Tile/"+i;
			console.log("Loading tile "+i+" url: "+img.src)
			Theater.Tiles.push(img);
		}
	});
})

// callback
function onMessage(event)
{
	var message = event.data;
	//console.log("[RAW RECIEVE] ", message);
	var index = message.indexOf(":");
	if (index == -1)
	{
		messageReload("Recieved a malformed message (missing delimiter)");
		return;
	}
	var eventType = message.substring(0, index);
	var data = message.substring(index+1, message.length);
	console.log("[RECV] event: ", eventType, " message: "+message)
	// activate event listener
	var listener = Theater.Listerners[eventType];
	if (listener == null)
	{
		messageReload("Recieved unexpected event type: "+eventType); 
		return;
	}
	listener(data)
}

// event

Theater.Listerners[NetMessageType.PING] = function(data){}; // ignore
Theater.Listerners[NetMessageType.ENDPOINT_LOGIN] = function(data)
{
	Theater.userID = Number(data);
	console.log("connected with user id: "+Theater.userID)
};
Theater.Listerners[NetMessageType.SERVER_MESSAGE] = function(data)
{
	console.log("[SERVER MSG] ", data)
};


// functions
function send(eventType, message)
{
	Theater.Websocket.send(eventType+":"+message);
}

function login()
{
	var name = $("#LoginName").val();
	if (name.length < 3)
	{
		alert("Please choose a longer name")
		return;
	}
	send(NetMessageType.ENDPOINT_LOGIN, name);
	Theater.LoginScreen.remove();
}

function messageReload(message)
{
	alert(message);
	location.reload();
}