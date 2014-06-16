package com.github.nowireless.factions.task;

import com.github.nowireless.factions.entity.FactionColl;
import com.github.nowireless.factions.entity.FactionColls;
import com.github.nowireless.factions.entity.MConf;
import com.github.nowireless.factions.entity.UConf;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.TimeUnit;

public class TaskEconLandReward extends ModuloRepeatTask
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static TaskEconLandReward i = new TaskEconLandReward();
	public static TaskEconLandReward get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE: MODULO REPEAT TASK
	// -------------------------------------------- //
	
	@Override
	public long getDelayMillis()
	{
		return (long) (MConf.get().taskEconLandRewardMinutes * TimeUnit.MILLIS_PER_MINUTE);
	}
	
	@Override
	public void setDelayMillis(long delayMillis)
	{
		MConf.get().taskEconLandRewardMinutes = delayMillis / (double) TimeUnit.MILLIS_PER_MINUTE;
	}
	
	@Override
	public void invoke(long now)
	{
		for (FactionColl coll : FactionColls.get().getColls())
		{
			// Check disabled
			if (UConf.isDisabled(coll)) continue;
						
			coll.econLandRewardRoutine();
		}
	}
	
}
