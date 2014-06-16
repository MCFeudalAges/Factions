package com.github.nowireless.factions.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.nowireless.factions.entity.MConf;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.entity.UPlayer;
import com.github.nowireless.factions.event.FactionsEventPowerChange;
import com.github.nowireless.factions.event.FactionsEventPowerChange.PowerChangeReason;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.TimeUnit;

public class TaskPlayerPowerUpdate extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskPlayerPowerUpdate i = new TaskPlayerPowerUpdate();
	public static TaskPlayerPowerUpdate get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: MODULO REPEAT TASK
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return (long) (MConf.get().taskPlayerPowerUpdateMinutes * TimeUnit.MILLIS_PER_MINUTE);
	}
	
	@Override
	public void setDelayMillis(long delayMillis)
	{
		MConf.get().taskPlayerPowerUpdateMinutes = delayMillis / (double) TimeUnit.MILLIS_PER_MINUTE;
	}
	
	@Override
	public void invoke(long now)
	{
		long millis = this.getDelayMillis();
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			// Check disabled
			if (UConf.isDisabled(player)) continue;
						
			if (player.isDead()) continue;
			
			UPlayer uplayer = UPlayer.get(player);
			double newPower = uplayer.getPower() + uplayer.getPowerPerHour() * millis / TimeUnit.MILLIS_PER_HOUR;
			
			FactionsEventPowerChange event = new FactionsEventPowerChange(null, uplayer, PowerChangeReason.TIME, newPower);
			event.run();
			if (event.isCancelled()) continue;
			newPower = event.getNewPower();
			
			uplayer.setPower(newPower);
		}
	}
	
}
