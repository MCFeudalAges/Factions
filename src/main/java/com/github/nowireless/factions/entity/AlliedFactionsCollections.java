package com.github.nowireless.factions.entity;

import com.github.nowireless.factions.Const;
import com.massivecraft.massivecore.Aspect;

public class AlliedFactionsCollections extends XColls<AlliedFactionsCollection, AlliedFactions> {

	/*
	 * Instance and  Construct
	 */
	
	private static AlliedFactionsCollections i = new AlliedFactionsCollections();
	public static AlliedFactionsCollections get() { return i; }
	
	/*
	 * Override Colls
	 */
	@Override
	public AlliedFactionsCollection createColl(String collName) {
		return new AlliedFactionsCollection(collName);
	}
	
	@Override
	public Aspect getAspect() {
		return FactionColls.get().getAspect();
	}

	@Override
	public String getBasename() {
		return Const.COLLECTION_ALLIED_FACTIONS;
	}
	
	@Override
	public void init() {
		super.init();
	}

}
