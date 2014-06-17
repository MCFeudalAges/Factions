package com.github.nowireless.factions;

import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.Region;
import com.github.nowireless.factions.entity.RegionCollections;

public class RegionAccess {
	private final String associatedRegionID;
	public String getAssociatedRegionID() { return associatedRegionID; }

	public RegionAccess withAssociatedRegion(String regionID) { return valueOf(regionID); }
	/*
	 * Private Constructer
	 */
	private RegionAccess(String regionID) {
		if(regionID == null) throw new IllegalArgumentException("ownerFaction is null");
		this.associatedRegionID = regionID;
	}

	public static RegionAccess valueOf(String regionID) {
		return new RegionAccess(regionID);
	}

	public Region getAssociatedRegion(Object universe) {
		return RegionCollections.get().get(universe).get(this.getAssociatedRegionID());
	}
	
	public Faction getOwningFaction(Object universe) {
		return getAssociatedRegion(universe).getOwningFaction();
	}
	
	public String getOwningFactionId(Object universe) {
		return getAssociatedRegion(universe).getOwningFactionId();
	}
}
