package us.potatoboy.ledger.listeners

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import us.potatoboy.ledger.actionutils.ActionFactory
import us.potatoboy.ledger.callbacks.BlockBurnCallback
import us.potatoboy.ledger.callbacks.BlockExplodeCallback
import us.potatoboy.ledger.callbacks.BlockFallCallback
import us.potatoboy.ledger.callbacks.BlockLandCallback
import us.potatoboy.ledger.database.DatabaseQueue
import us.potatoboy.ledger.database.queueitems.ActionQueueItem

object BlockEventListener {
    init {
        BlockExplodeCallback.EVENT.register(::onExplode)
        BlockBurnCallback.EVENT.register(::onBurn)
        BlockFallCallback.EVENT.register(::onFall)
        BlockLandCallback.EVENT.register(::onLand)
    }

    private fun onLand(world: World, pos: BlockPos, state: BlockState) {
        DatabaseQueue.addActionToQueue(ActionQueueItem(ActionFactory.blockPlaceAction(world, pos, state, "gravity")))
    }

    private fun onFall(world: World, pos: BlockPos, state: BlockState) {
        DatabaseQueue.addActionToQueue(ActionQueueItem(ActionFactory.blockBreakAction(world, pos, state, "gravity")))
    }

    private fun onBurn(world: World, pos: BlockPos, state: BlockState) {
        DatabaseQueue.addActionToQueue(ActionQueueItem(ActionFactory.blockBreakAction(world, pos, state, "fire")))
    }

    private fun onExplode(
        world: World,
        entity: Entity?,
        blockPos: BlockPos,
        blockState: BlockState,
        blockEntity: BlockEntity?
    ) {
        val source = entity?.let { Registry.ENTITY_TYPE.getId(it.type).path } ?: "explosion"

        DatabaseQueue.addActionToQueue(
            ActionQueueItem(
                ActionFactory.blockBreakAction(
                    world,
                    blockPos,
                    blockState,
                    source
                )
            )
        )
    }
}
