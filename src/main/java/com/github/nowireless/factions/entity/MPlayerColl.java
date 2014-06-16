package com.github.nowireless.factions.entity;

import com.github.nowireless.factions.Const;
import com.github.nowireless.factions.Factions;
import com.massivecraft.massivecore.store.MStore;
import com.massivecraft.massivecore.store.SenderColl;

public class MPlayerColl extends SenderColl<MPlayer>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MPlayerColl i = new MPlayerColl();
	public static MPlayerColl get() { return i; }
	private MPlayerColl()
	{
		super(Const.COLLECTION_MPLAYER, MPlayer.class, MStore.getDb(), Factions.get());
	}
	
}
