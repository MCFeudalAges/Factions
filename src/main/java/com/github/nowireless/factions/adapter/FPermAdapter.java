package com.github.nowireless.factions.adapter;

import java.lang.reflect.Type;

import com.github.nowireless.factions.FPerm;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.massivecore.xlib.gson.JsonDeserializer;
import com.massivecraft.massivecore.xlib.gson.JsonElement;
import com.massivecraft.massivecore.xlib.gson.JsonParseException;

public class FPermAdapter implements JsonDeserializer<FPerm>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static FPermAdapter i = new FPermAdapter();
	public static FPermAdapter get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public FPerm deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return FPerm.parse(json.getAsString());
	}
}
