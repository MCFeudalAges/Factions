package com.github.nowireless.factions.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import com.github.nowireless.factions.RegionAccess;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.RelationParticipator;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.xlib.gson.reflect.TypeToken;

public class RegionBoard extends Entity<RegionBoard> implements RegionBoardInterface{

	public static final transient Type MAP_TYPE = new TypeToken<Map<PS, RegionAccess>>(){}.getType();

	public static RegionBoard get(Object oid) {
		return RegionBoardCollections.get().get2(oid);
	}

	@Override
	public RegionBoard load(RegionBoard that) {
		this.map = that.map;
		return this;
	}

	private ConcurrentSkipListMap<PS, RegionAccess> map;
	public Map<PS, RegionAccess> getMap() { return Collections.unmodifiableMap(this.map); }

	public RegionBoard() {
		this.map = new ConcurrentSkipListMap<PS, RegionAccess>();
	}

	public RegionBoard(Map<PS, RegionAccess> map) {
		this.map = new ConcurrentSkipListMap<PS, RegionAccess>(map);
	}

	@Override
	public RegionAccess getRegionAccessAt(PS ps) {
		if(ps == null) return null;
		ps = ps.getChunkCoords(true);
		RegionAccess ret = this.map.get(ps);
		if(ret == null) ret = RegionAccess.valueOf(UConf.get(this).regionIdNone);
		return ret;
	}

	@Override
	public Region getRegionAt(PS ps) {
			if(ps == null) return null;
			RegionAccess ra = this.getRegionAccessAt(ps);
			if(ra == null) return null;
			return ra.getAssociatedRegion(this);
	}

	@Override
	public void setRegionAccessAt(PS ps, RegionAccess regionAccess) {
		ps = ps.getChunkCoords(true);

		if(regionAccess == null || (regionAccess.getAssociatedRegionID().equals(UConf.get(this).regionIdNone))) {
			this.map.remove(ps);
		} else {
			this.map.put(ps, regionAccess);
		}
		this.changed();
	}

	@Override
	public void setRegionAt(PS ps, Region region) {
		RegionAccess regionAccess = null;
		if(region != null) {
			regionAccess = RegionAccess.valueOf(region.getId());
		}
		this.setRegionAccessAt(ps, regionAccess);
	}

	@Override
	public void removeAt(PS ps) {
		this.setRegionAccessAt(ps, null);
	}

	@Override
	public void removeAll(Region region) {
		String regionId = region.getId();
		for(Entry<PS, RegionAccess> entry : this.map.entrySet()) {
			RegionAccess regionAccess = entry.getValue();
			if(!regionAccess.getAssociatedRegionID().equals(regionId)) continue;

			PS ps = entry.getKey();
			this.removeAt(ps);
		}
	}

	@Override
	public Set<PS> getChunks(Region region) {
		return this.getChunks(region.getId());
	}

	public  Set<PS> getChunks(String regionID) {
		Set<PS> ret = new HashSet<PS>();
		for (Entry<PS, RegionAccess> entry : this.map.entrySet()) {
			RegionAccess ra = entry.getValue();
			if(!ra.getAssociatedRegionID().equals(regionID)) continue;

			PS ps = entry.getKey();
			ps = ps.withWorld(this.getId());
			ret.add(ps);
		}
		return ret;
	}

	@Override
	public int getCount(Region region) {
		return this.getCount(region.getId());
	}

	public int getCount(String regionID) {
		int ret = 0;
		for(RegionAccess ra : this.map.values()) {
			if(!ra.getAssociatedRegionID().equals(regionID)) continue;
			ret++;
		}
		return ret;
	}
	@Override
	public ArrayList<String> getMap(RelationParticipator observer, PS centerPs,
			double inDegrees) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clean() {
		RegionBoardCollection regionMapColl = RegionBoardCollections.get().get(this);

		for(Entry<PS, RegionAccess> entry : this.map.entrySet()) {
			RegionAccess regionAccess = entry.getValue();
			String  regionID = regionAccess.getAssociatedRegionID();
			if(regionMapColl.containsId(regionID)) continue;

			PS ps = entry.getKey();
			this.removeAt(ps);

			Factions.get().log("RegionMap Cleaner Removed " + regionID + " from " + ps);
		}
	}
}
