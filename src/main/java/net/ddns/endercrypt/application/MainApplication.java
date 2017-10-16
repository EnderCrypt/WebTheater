package net.ddns.endercrypt.application;

import javax.websocket.server.ServerEndpointConfig;
import javax.ws.rs.Path;

import org.reflections.Reflections;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.websockets.WebsocketBundle;
import net.ddns.endercrypt.web.socket.WsTheaterEndpoint;

public class MainApplication extends Application<ApplicationConfiguration>
{
	private WebsocketBundle websocketBundle;

	@Override
	public void initialize(Bootstrap<ApplicationConfiguration> bootstrap)
	{
		// super init
		super.initialize(bootstrap);

		// asset
		bootstrap.addBundle(new AssetsBundle("/Assets/Page/", "/", "index.html", "static"));
		bootstrap.addBundle(new AssetsBundle("/Assets/", "/Static/"));

		// websocket configuration
		websocketBundle = new WebsocketBundle(new ServerEndpointConfig.Configurator());
		bootstrap.addBundle(websocketBundle);
	}

	@Override
	public void run(ApplicationConfiguration configuration, Environment environment) throws Exception
	{
		// get jersey
		JerseyEnvironment jersey = environment.jersey();
		environment.jersey().setUrlPattern("/Api/*");

		// websockets
		ServerEndpointConfig config = ServerEndpointConfig.Builder.create(WsTheaterEndpoint.class, "/WsTheater").build();
		websocketBundle.addEndpoint(config);

		// register pages
		Reflections reflections = new Reflections("net.ddns.endercrypt.web.api");
		for (Class<?> clazz : reflections.getTypesAnnotatedWith(Path.class))
		{
			jersey.register(clazz.newInstance());
		}
	}
}
