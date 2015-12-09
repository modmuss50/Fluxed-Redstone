package me.modmuss50.fr.powernet


import me.modmuss50.fr.tile.TilePipe
import java.util.*

class PowerNetwork {

    var pipes = ArrayList<TilePipe>()

    var isValid = true;

    constructor(valid : Boolean){
        isValid = valid
    }

    constructor(){

    }


    /**
     * This is the current rf for the whole system.
     */
    var networkRF = 0;

    val RFTick = 64;

    val RFPerPipe = 1000;

    fun addElement(tile: TilePipe) {
        if (!pipes.contains(tile) && pipes.size < 5000) {
            pipes.add(tile)
        }
    }

    fun removeElement(tile: TilePipe) {
        pipes.remove(tile)
        rebuild()
    }

    private fun rebuild() {
        for (tile in pipes) {
            tile.findAndJoinNetwork(tile.world, tile.pos.x, tile.pos.y, tile.pos.z)
        }
    }

    fun merge(network: PowerNetwork) {
        if (network != this) {
            val tiles = ArrayList<TilePipe>()
            tiles.addAll(network.pipes)
            network.clear(false)
            for (tile in tiles) {
                tile.setNetwork(this)
            }
            networkRF = Math.min(network.networkRF, (pipes.size * RFPerPipe))
        }
    }

    private fun clear(clearTiles: Boolean) {
        if (clearTiles) {
            for (tile in pipes) {
                tile.resetNetwork()
            }
        }
        pipes.clear()
    }
}