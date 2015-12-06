package me.modmuss50.fr.powernet

import me.modmuss50.fr.tile.TilePipe
import java.util.*

class PowerNetwork {

    var pipes = ArrayList<TilePipe>()

    fun addElement(lesuStorage: TilePipe) {
        if (!pipes.contains(lesuStorage) && pipes.size < 5000) {
            pipes.add(lesuStorage)
        }
    }

    fun removeElement(lesuStorage: TilePipe) {
        pipes.remove(lesuStorage)
        rebuild()
    }

    private fun rebuild() {
        for (lesuStorage in pipes) {
            lesuStorage.findAndJoinNetwork(lesuStorage.world, lesuStorage.pos.x, lesuStorage.pos.y, lesuStorage.pos.z)
        }
    }

    fun merge(network: PowerNetwork) {
        if (network !== this) {
            val tileLesuStorages = ArrayList<TilePipe>()
            tileLesuStorages.addAll(network.pipes)
            network.clear(false)
            for (lesuStorage in tileLesuStorages) {
                lesuStorage.setNetwork(this)
            }
        }
    }

    private fun clear(clearTiles: Boolean) {
        if (clearTiles) {
            for (tileLesuStorage in pipes) {
                tileLesuStorage.resetNetwork()
            }
        }
        pipes.clear()
    }

}