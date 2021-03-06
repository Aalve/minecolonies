package com.minecolonies.coremod.blocks.huts;

import com.minecolonies.api.blocks.AbstractBlockHut;
import com.minecolonies.api.colony.IColonyView;
import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.coremod.research.ResearchInitializer;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;

/**
 * Hut for the mechanic. No different from {@link AbstractBlockHut}
 */
public class BlockHutMechanic extends AbstractBlockHut<BlockHutMechanic>
{
    @NotNull
    @Override
    public String getName()
    {
        return "blockhutmechanic";
    }

    @Override
    public BuildingEntry getBuildingEntry()
    {
        return ModBuildings.mechanic;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void checkResearch(final IColonyView colony)
    {
        checkResearch(colony, ResearchInitializer.MECHANIC_RESEARCH);
    }
}
