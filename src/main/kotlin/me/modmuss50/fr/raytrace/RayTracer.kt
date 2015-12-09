package me.modmuss50.fr.raytrace

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.*
import net.minecraft.world.World


//Kotlin port of https://github.com/asiekierka/CharsetMC/blob/master/src/main/java/pl/asie/charset/lib/RayTraceUtils.java
class RayTracer {
    class Result(val hit: MovingObjectPosition?, val box: AxisAlignedBB?) {

        fun valid(): Boolean {
            return hit != null && box != null
        }
    }

    fun getCollision(world: World, pos: BlockPos, player: EntityPlayer, list: List<AxisAlignedBB>): Result {
        val reachDistance = if (player is EntityPlayerMP) player.theItemInWorldManager.blockReachDistance else 5.0

        val lookVec = player.lookVec
        val origin = Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ)
        val direction = origin.addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance)

        return getCollision(world, pos, origin, direction, list)
    }

    fun getCollision(world: World, pos: BlockPos, origin: Vec3, direction: Vec3, list: List<AxisAlignedBB>): Result {
        var minDistance = java.lang.Double.POSITIVE_INFINITY
        var hit: MovingObjectPosition? = null

        for (i in list.indices) {
            if (list[i] == null) {
                continue
            }

            val mop = getCollision(pos, origin, direction, list[i], i)
            if (mop != null) {
                val d = mop.hitVec.squareDistanceTo(origin)
                if (d < minDistance) {
                    minDistance = d
                    hit = mop
                }
            }
        }

        return Result(hit, if (hit != null) list[hit.subHit] else null)
    }

    fun getCollision(pos: BlockPos, start: Vec3, end: Vec3, aabb: AxisAlignedBB, subHit: Int): MovingObjectPosition? {
        var start = start
        var end = end
        start = start.addVector((-pos.x).toDouble(), (-pos.y).toDouble(), (-pos.z).toDouble())
        end = end.addVector((-pos.x).toDouble(), (-pos.y).toDouble(), (-pos.z).toDouble())

        var vec3: Vec3? = start.getIntermediateWithXValue(end, aabb.minX)
        var vec31: Vec3? = start.getIntermediateWithXValue(end, aabb.maxX)
        var vec32: Vec3? = start.getIntermediateWithYValue(end, aabb.minY)
        var vec33: Vec3? = start.getIntermediateWithYValue(end, aabb.maxY)
        var vec34: Vec3? = start.getIntermediateWithZValue(end, aabb.minZ)
        var vec35: Vec3? = start.getIntermediateWithZValue(end, aabb.maxZ)

        if (!isVecInsideYZBounds(aabb, vec3)) {
            vec3 = null
        }

        if (!isVecInsideYZBounds(aabb, vec31)) {
            vec31 = null
        }

        if (!isVecInsideXZBounds(aabb, vec32)) {
            vec32 = null
        }

        if (!isVecInsideXZBounds(aabb, vec33)) {
            vec33 = null
        }

        if (!isVecInsideXYBounds(aabb, vec34)) {
            vec34 = null
        }

        if (!isVecInsideXYBounds(aabb, vec35)) {
            vec35 = null
        }

        var vec36: Vec3? = null

        if (vec3 != null && (vec36 == null || start.squareDistanceTo(vec3) < start.squareDistanceTo(vec36))) {
            vec36 = vec3
        }

        if (vec31 != null && (vec36 == null || start.squareDistanceTo(vec31) < start.squareDistanceTo(vec36))) {
            vec36 = vec31
        }

        if (vec32 != null && (vec36 == null || start.squareDistanceTo(vec32) < start.squareDistanceTo(vec36))) {
            vec36 = vec32
        }

        if (vec33 != null && (vec36 == null || start.squareDistanceTo(vec33) < start.squareDistanceTo(vec36))) {
            vec36 = vec33
        }

        if (vec34 != null && (vec36 == null || start.squareDistanceTo(vec34) < start.squareDistanceTo(vec36))) {
            vec36 = vec34
        }

        if (vec35 != null && (vec36 == null || start.squareDistanceTo(vec35) < start.squareDistanceTo(vec36))) {
            vec36 = vec35
        }

        if (vec36 == null) {
            return null
        } else {
            var enumfacing: EnumFacing? = null

            if (vec36 === vec3) {
                enumfacing = EnumFacing.WEST
            }

            if (vec36 === vec31) {
                enumfacing = EnumFacing.EAST
            }

            if (vec36 === vec32) {
                enumfacing = EnumFacing.DOWN
            }

            if (vec36 === vec33) {
                enumfacing = EnumFacing.UP
            }

            if (vec36 === vec34) {
                enumfacing = EnumFacing.NORTH
            }

            if (vec36 === vec35) {
                enumfacing = EnumFacing.SOUTH
            }

            val mop = MovingObjectPosition(vec36.addVector(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()), enumfacing, pos)
            mop.subHit = subHit
            return mop
        }
    }

    private fun isVecInsideYZBounds(aabb: AxisAlignedBB, point: Vec3?): Boolean {
        return if (point == null) false else point.yCoord >= aabb.minY && point.yCoord <= aabb.maxY && point.zCoord >= aabb.minZ && point.zCoord <= aabb.maxZ
    }

    private fun isVecInsideXZBounds(aabb: AxisAlignedBB, point: Vec3?): Boolean {
        return if (point == null) false else point.xCoord >= aabb.minX && point.xCoord <= aabb.maxX && point.zCoord >= aabb.minZ && point.zCoord <= aabb.maxZ
    }

    private fun isVecInsideXYBounds(aabb: AxisAlignedBB, point: Vec3?): Boolean {
        return if (point == null) false else point.xCoord >= aabb.minX && point.xCoord <= aabb.maxX && point.yCoord >= aabb.minY && point.yCoord <= aabb.maxY
    }
}