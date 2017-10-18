package net.ddns.endercrypt.web.api;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.eclipse.jetty.http.HttpStatus;

import net.ddns.endercrypt.server.map.tileset.Tile;
import net.ddns.endercrypt.server.map.tileset.Tileset;

@Path("/Tileset")
public class TilesetApi
{
	private static final File tilesetImageFile = new File("data/gfx/tileset.bmp");
	private static final File tilesetSolidityFile = new File("data/gfx/tileset.txt");
	private static final Dimension tileSize = new Dimension(32, 32);

	private static final Tileset tileset;
	static
	{
		try
		{
			tileset = new Tileset(tilesetImageFile, tilesetSolidityFile, tileSize);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public TilesetApi() throws IOException
	{

	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Object main() throws IOException
	{
		return tileset.getTilesetInfo().serializable();
	}

	@GET
	@Path("/Full")
	@Produces("image/png")
	public Response fullTileset(@PathParam("index") int index) throws IOException
	{
		return Response.ok().entity(new StreamingOutput()
		{
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				ImageIO.write(tileset.getFullTileset(), "PNG", output);
				output.flush();
			}
		}).build();
	}

	@GET
	@Path("/Solid")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> solidity() throws IOException
	{
		return tileset.getAll().stream().map(t -> t.getSolidity().ordinal()).collect(Collectors.toList());
	}

	@GET
	@Path("/{index}")
	@Produces("image/png")
	public Response getTile(@PathParam("index") int index) throws IOException
	{
		if ((index < 0) || (index >= tileset.getTilesetInfo().getCount()))
		{
			return Response.status(HttpStatus.BAD_REQUEST_400).build();
		}

		Tile tile = tileset.get(index);
		return Response.ok().entity(new StreamingOutput()
		{
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				ImageIO.write(tile.getImage(), "PNG", output);
				output.flush();
			}
		}).build();
	}
}
