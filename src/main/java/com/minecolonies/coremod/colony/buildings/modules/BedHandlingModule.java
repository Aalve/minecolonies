package com.minecolonies.coremod.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.modules.*;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.state.properties.BedPart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.minecolonies.api.util.constant.NbtTagConstants.TAG_BEDS;

/**
 * The class of the citizen hut.
 */
public class BedHandlingModule extends AbstractBuildingModule implements IModuleWithExternalBlocks, IModuleWithData, IBuildingEventsModule
{
    /**
     * List of all beds.
     */
    @NotNull
    private final List<BlockPos> bedList = new ArrayList<>();

    /**
     * Creates a new home building module.
     * @param building the building it is assigned to.
     */
    public BedHandlingModule(final IBuilding building)
    {
        super(building);
    }

    @Override
    public void deserializeNBT(final CompoundNBT compound)
    {
        final ListNBT bedTagList = compound.getList(TAG_BEDS, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < bedTagList.size(); ++i)
        {
            final CompoundNBT bedCompound = bedTagList.getCompound(i);
            final BlockPos bedPos = NBTUtil.readBlockPos(bedCompound);
            if (!bedList.contains(bedPos))
            {
                bedList.add(bedPos);
            }
        }
    }

    @Override
    public void serializeNBT(final CompoundNBT compound)
    {
        if (!bedList.isEmpty())
        {
            @NotNull final ListNBT bedTagList = new ListNBT();
            for (@NotNull final BlockPos pos : bedList)
            {
                bedTagList.add(NBTUtil.writeBlockPos(pos));
            }
            compound.put(TAG_BEDS, bedTagList);
        }
    }

    @Override
    public void onBlockPlacedInBuilding(@NotNull final BlockState blockState, @NotNull final BlockPos pos, @NotNull final World world)
    {
        BlockPos registrationPosition = pos;
        if (blockState.getBlock() instanceof BedBlock)
        {
            if (blockState.get(BedBlock.PART) == BedPart.FOOT)
            {
                registrationPosition = registrationPosition.offset(blockState.get(BedBlock.HORIZONTAL_FACING));
            }

            if (!bedList.contains(registrationPosition))
            {
                bedList.add(registrationPosition);
            }
        }
    }

    @Override
    public List<BlockPos> getRegisterBlocks()
    {
        return new ArrayList<>(bedList);
    }

    @Override
    public void onWakeUp()
    {
        final World world = building.getColony().getWorld();
        if (world == null)
        {
            return;
        }

        for (final BlockPos pos : bedList)
        {
            BlockState state = world.getBlockState(pos);
            state = state.getBlock().getExtendedState(state, world, pos);
            if (state.getBlock() instanceof BedBlock
                  && state.get(BedBlock.OCCUPIED)
                  && state.get(BedBlock.PART).equals(BedPart.HEAD))
            {
                world.setBlockState(pos, state.with(BedBlock.OCCUPIED, false), 0x03);
            }
        }
    }

}