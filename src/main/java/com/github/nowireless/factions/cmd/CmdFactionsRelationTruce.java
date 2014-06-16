package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Rel;

public class CmdFactionsRelationTruce extends CmdFactionsRelationAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsRelationTruce()
	{
		// Aliases
		this.addAliases("truce");
		
		// Misc
		this.targetRelation = Rel.TRUCE;
	}
	
}
