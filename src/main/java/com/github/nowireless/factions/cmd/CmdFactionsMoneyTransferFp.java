package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.arg.ARFaction;
import com.github.nowireless.factions.cmd.arg.ARUPlayer;
import com.github.nowireless.factions.cmd.req.ReqBankCommandsEnabled;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.MConf;
import com.github.nowireless.factions.entity.UPlayer;
import com.github.nowireless.factions.integration.Econ;
import com.massivecraft.massivecore.cmd.arg.ARDouble;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.Txt;

import org.bukkit.ChatColor;


public class CmdFactionsMoneyTransferFp extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMoneyTransferFp()
	{
		// Aliases
		this.addAliases("fp");

		// Args
		this.addRequiredArg("amount");
		this.addRequiredArg("faction");
		this.addRequiredArg("player");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.MONEY_F2P.node));
		this.addRequirements(ReqBankCommandsEnabled.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Double amount = this.arg(0, ARDouble.get());
		if (amount == null) return;
		
		Faction from = this.arg(1, ARFaction.get(sender));
		if (from == null) return;
		
		UPlayer to = this.arg(2, ARUPlayer.getStartAny(sender));
		if (to == null) return;
		
		boolean success = Econ.transferMoney(usender, from, to, amount);

		if (success && MConf.get().logMoneyTransactions)
		{
			Factions.get().log(ChatColor.stripColor(Txt.parse("%s transferred %s from the faction \"%s\" to the player \"%s\"", usender.getName(), Money.format(amount), from.describeTo(null), to.describeTo(null))));
		}
	}
	
}
