package gui.tilemap;

import java.io.IOException;

/**
 * A class used for the visual representation of the gameworld.
 * @author Barber
 *
 */
public class TileMap {

	/**
	 * Creates a TileMap from the given file located at the given URL.
	 * 
	 * @param mapURL The URL to the map to use.
	 * @throws IOException Thrown if the map-file can't be found.
	 */
	public TileMap(String mapURL) throws IOException {
		TileMapIO.getTileMatrixFromImg(mapURL);
	}
	
}
