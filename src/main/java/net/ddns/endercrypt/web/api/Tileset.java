package net.ddns.endercrypt.web.api;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

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

@Path("/Tileset")
public class Tileset
{
	private static final File file = new File("data/gfx/tileset.bmp");
	private static final Dimension tileSize = new Dimension(32, 32);

	private TileMetaInfo tileInfo = new TileMetaInfo();
	private BufferedImage[] tileset;

	public Tileset() throws IOException
	{
		tileInfo.width = tileSize.width;
		tileInfo.height = tileSize.height;

		BufferedImage image = ImageIO.read(file);
		int tilesx = image.getWidth() / tileSize.width;
		int tilesy = image.getHeight() / tileSize.height;
		tileInfo.count = tilesx * tilesy;
		tileset = new BufferedImage[tileInfo.count];
		int i = -1;
		for (int y = 0; y < tilesy; y++)
		{
			for (int x = 0; x < tilesx; x++)
			{
				i++;
				tileset[i] = image.getSubimage(x * tileSize.width, y * tileSize.height, tileSize.width, tileSize.height);
			}
		}
	}

	@GET
	@Path("/Info")
	@Produces(MediaType.APPLICATION_JSON)
	public TileMetaInfo main() throws IOException
	{
		return tileInfo;
	}

	@GET
	@Path("/Tile/{index}")
	@Produces("image/png")
	public Response main(@PathParam("index") int index) throws IOException
	{
		if ((index < 0) || (index >= tileset.length))
		{
			return Response.status(HttpStatus.BAD_REQUEST_400).build();
		}

		BufferedImage image = tileset[index];
		return Response.ok().entity(new StreamingOutput()
		{
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException
			{
				ImageIO.write(image, "PNG", output);
				output.flush();
			}
		}).build();
	}

	private class TileMetaInfo
	{
		public int count = 0;
		public int width = 0;
		public int height = 0;
	}
}
