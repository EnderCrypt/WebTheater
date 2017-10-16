package net.ddns.endercrypt.web.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.ddns.endercrypt.web.page.Pages;

@Path("/")
public class HomeApi
{
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String main() throws IOException
	{
		return Pages.index().toString();
	}
}
