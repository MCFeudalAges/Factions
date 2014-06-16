package com.github.nowireless.factions.task;

import java.util.Set;

import org.bukkit.Bukkit;



import com.github.nowireless.factions.entity.Region;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;

public abstract class RegionChunkTask implements Runnable {
	private transient Set<PS> chunkSet = null;
	private transient PS[] chunkArray= null;
	private transient int chunkArraySize = 0;
	private transient boolean readyToGo = false;
	private transient int taskID = -1;


	//private transient PS currentChunk = null;
	private transient int currentChunkNumber = 0;

	private transient Faction faction = null;
	private transient Region region = null;

	public RegionChunkTask(Set<PS> chunks, Faction playerFaction, Region workingRegion) {
		this.chunkSet = chunks;
		this.chunkArray = chunkSet.toArray(new PS[chunkSet.size()]);
		this.chunkArraySize = chunkArray.length;

		this.faction = playerFaction;
		this.region = workingRegion;

		//this.currentChunk = chunkArray[0];
		this.currentChunkNumber = 0;

		this.readyToGo = true;
		this.setTaskID(Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Factions.get(), this, 2, 2));
	}

	public abstract boolean work(PS chunk, Faction faction, Region region);

	public void ending() {}

	public final PS getCurrentChunk() {
		return chunkArray[currentChunkNumber];
	}

	public final void setTaskID(int ID) {
		if(ID == -1) 
			this.stop();
		taskID = ID;
	}

	public final void run() {
		Factions.get().log("In run method for region chunk task. Chunk Array size: " + chunkArraySize + " Current Chunk: " + currentChunkNumber);
		if(!this.valid() || !readyToGo) return;
		readyToGo = false;
		if(!(currentChunkNumber < chunkArraySize)) return;
		long loopStartTime = now();

		while(now() < (loopStartTime + 20)) {
			Factions.get().log("In while loop of Region Chunk Task");
			if(!this.work(getCurrentChunk(), this.faction, this.region)) {
				//this.finish();
				return;
			}

			if(!this.moveToNext()) {
				this.finish();
				return;
			}
		}
		readyToGo = true;
	}

	public final boolean moveToNext() {

		if(!this.valid()) return false;
		currentChunkNumber++;
		if(currentChunkNumber < chunkArraySize) {
			Factions.get().log("Moving to next chunk: " + currentChunkNumber);
			return true;
		} else {
			Factions.get().log("Chunk Array size has been reached");
			return false;
		}
	}

	public void finish() {
		this.stop();
	}

	public final void stop() {
		if(!this.valid()) return;
		readyToGo = false;
		Bukkit.getServer().getScheduler().cancelTask(taskID);
		taskID = -1;
	}

	public final boolean valid() {
		return taskID != -1;
	}

	private static long now() {
		return System.currentTimeMillis();
	}
}