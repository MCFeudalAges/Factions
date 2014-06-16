package com.github.nowireless.factions.cmd.arg;

import com.github.nowireless.factions.entity.UPlayer;
import com.github.nowireless.factions.entity.UPlayerColls;
import com.massivecraft.massivecore.cmd.arg.ARSenderEntity;
import com.massivecraft.massivecore.cmd.arg.ArgReader;

public class ARUPlayer
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static ArgReader<UPlayer> getFullAny(Object o) { return ARSenderEntity.getFullAny(UPlayerColls.get().get(o)); }
	
	public static ArgReader<UPlayer> getStartAny(Object o) { return ARSenderEntity.getStartAny(UPlayerColls.get().get(o)); }
	
	public static ArgReader<UPlayer> getFullOnline(Object o) { return ARSenderEntity.getFullOnline(UPlayerColls.get().get(o)); }
	
	public static ArgReader<UPlayer> getStartOnline(Object o) { return ARSenderEntity.getStartOnline(UPlayerColls.get().get(o)); }
	
}
