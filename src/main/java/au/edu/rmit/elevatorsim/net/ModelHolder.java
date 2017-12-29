package au.edu.rmit.elevatorsim.net;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Container for the model information transmitted in the modelChangedEvent.
 * It is intended that Controller will subclasses will take the information
 * in this class and use it to initialize their own model data structures
 * @author Joshua Richards
 */
public final class ModelHolder
{
	/**
	 * Container for the car information transmitted in the modelChanged event
	 * @author Joshua Richards
	 */
	public static final class CarHolder
	{
		public final int[] servicedFloors;
		public final double currentHeight;
		public final int id;
		public final int occupants;
		public final int capacity;
		
		public CarHolder(JSONObject carJson)
		{
			JSONArray servicedFloorsJson = carJson.getJSONArray("servicedFloors");
			servicedFloors = new int[servicedFloorsJson.length()];
			for (int i = 0; i < servicedFloors.length; i++)
			{
				servicedFloors[i] = servicedFloorsJson.getInt(i);
			}
			
			currentHeight = carJson.getDouble("currentHeight");
			id = carJson.getInt("id");
			occupants = carJson.getInt("occupants");
			capacity = carJson.getInt("capacity");
		}
	}

	/**
	 * Container for the floor information transmitted in the modelChanged event
	 * @author Joshua Richards
	 *
	 */
	public static final class FloorHolder
	{
		public final int id;
		public final double height;
		
		public FloorHolder(JSONObject floorJson)
		{
			id = floorJson.getInt("id");
			height = floorJson.getDouble("height");
		}
	}

	public final ModelHolder.CarHolder[] cars;
	public final ModelHolder.FloorHolder[] floors;

	public ModelHolder(JSONObject modelJson)
	{
		JSONArray carsJson = modelJson.getJSONArray("cars");
		cars = new ModelHolder.CarHolder[carsJson.length()];
		for (int i = 0; i < carsJson.length(); i++)
		{
			cars[i] = new ModelHolder.CarHolder(carsJson.getJSONObject(i));
		}

		JSONArray floorsJson = modelJson.getJSONArray("floors");
		floors = new ModelHolder.FloorHolder[floorsJson.length()];
		for (int i = 0; i < floorsJson.length(); i++)
		{
			floors[i] = new ModelHolder.FloorHolder(floorsJson.getJSONObject(i));
		}
	}
}