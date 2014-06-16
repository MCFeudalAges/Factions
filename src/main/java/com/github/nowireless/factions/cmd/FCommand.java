package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Rel;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.MPlayer;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.util.Txt;

public abstract class FCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public MPlayer msender;
	public UPlayer usender;
	public Faction usenderFaction;
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void fixSenderVars()
	{
		this.msender = MPlayer.get(sender);
		
		this.usender = null;
		this.usenderFaction = null;			
		
		// Check disabled
		if (UConf.isDisabled(sender)) return;
		
		this.usender = UPlayer.get(this.sender);
		this.usenderFaction = this.usender.getFaction();
	}
	
	// -------------------------------------------- //
	// COMMONLY USED LOGIC
	// -------------------------------------------- //
	
	public boolean canIAdministerYou(UPlayer i, UPlayer you)
	{
		if ( ! i.getFaction().equals(you.getFaction()))
		{
			i.sendMessage(Txt.parse("%s <b>is not in the same faction as you.",you.describeTo(i, true)));
			return false;
		}
		
		if (i.getRole().isMoreThan(you.getRole()) || i.getRole().equals(Rel.LEADER) )
		{
			return true;
		}
		
		if (you.getRole().equals(Rel.LEADER))
		{
			i.sendMessage(Txt.parse("<b>Only the faction leader can do that."));
		}
		else if (i.getRole().equals(Rel.OFFICER))
		{
			if ( i == you )
			{
				return true; //Moderators can control themselves
			}
			else
			{
				i.sendMessage(Txt.parse("<b>Moderators can't control each other..."));
			}
		}
		else
		{
			i.sendMessage(Txt.parse("<b>You must be a faction moderator to do that."));
		}
		
		return false;
	}
	
}
