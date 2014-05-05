package com.github.nowireless.factions.adapter;

import java.lang.reflect.Type;
import java.util.Map;

import com.github.nowireless.factions.RegionAccess;
import com.github.nowireless.factions.entity.RegionBoard;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.xlib.gson.JsonDeserializationContext;
import com.massivecraft.mcore.xlib.gson.JsonDeserializer;
import com.massivecraft.mcore.xlib.gson.JsonElement;
import com.massivecraft.mcore.xlib.gson.JsonParseException;
import com.massivecraft.mcore.xlib.gson.JsonSerializationContext;
import com.massivecraft.mcore.xlib.gson.JsonSerializer;

public class RegionBoardMapAdapter implements JsonSerializer<RegionBoard>, JsonDeserializer<RegionBoard> {
	private static RegionBoardMapAdapter i = new RegionBoardMapAdapter();
	public static RegionBoardMapAdapter get() { return i; }


	@SuppressWarnings("unchecked")
	@Override
	public RegionBoard deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		return new RegionBoard((Map<PS, RegionAccess>) context.deserialize(json, RegionBoard.MAP_TYPE));
	}

	@Override
	public JsonElement serialize(RegionBoard src, Type typeOfSrc,
			JsonSerializationContext context) {
		return context.serialize(src.getMap(), RegionBoard.MAP_TYPE);
	}
}
