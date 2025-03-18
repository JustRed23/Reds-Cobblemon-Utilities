package dev.JustRed23.redscobblemonutilities.common.utils

import com.cobblemon.mod.common.api.gui.blitk
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.client.CobblemonResources
import com.cobblemon.mod.common.pokemon.IVs
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.world.phys.Vec2
import org.joml.Vector3f
import kotlin.math.cos
import kotlin.math.sin

class Stats {

    companion object {
        val perfectIVs = createPerfect();

        fun createPerfect(): IVs {
            val ivs = IVs()

            for (stat in Stats.PERMANENT) {
                ivs[stat] = 21
            }

            return ivs;
        }

        private val statsBaseResource = cobblemonResource("textures/gui/summary/summary_stats_chart_base.png")
        private val statsChartResource = cobblemonResource("textures/gui/summary/summary_stats_chart.png")

        fun drawIVs(matrices: PoseStack, pokemon: Pokemon, x: Int, y: Int, width: Float, height: Float) {
            drawStatHexagon(perfectIVs.associate { it.key to it.value }, Vector3f(216F/255, 100F/255, 1F), 31, x, y, width, height)
        }

        fun drawEVs(matrices: PoseStack, pokemon: Pokemon, x: Int, y: Int, width: Float, height: Float) {
            drawStatHexagon(pokemon.evs.associate { it.key to it.value }, Vector3f(1F, 1F, 100F/255), 252, x, y, width, height)
        }

        private fun drawStatHexagon(stats: Map<Stat, Int>, colour: Vector3f, maximum: Int, x: Int, y: Int, width: Float, height: Float) {
            val hexLeftX = x.toFloat()
            val hexRightX = x + width
            val hexTopY = y.toFloat()
            val hexBottomY = y + height
            val hexCenterX = (hexLeftX + hexRightX) / 2
            val hexCenterY = (hexTopY + hexBottomY) / 2
            val minTriangleSize = height * 0.1F
            val minXTriangleLen = sin(Math.toRadians(60.0)).toFloat() * minTriangleSize
            val minYTriangleLen = cos(Math.toRadians(60.0)).toFloat() * minTriangleSize

            val triangleLongEdge = (height / 2 - minTriangleSize)
            val triangleMediumEdge = triangleLongEdge * sin(Math.toRadians(60.0)).toFloat()
            val triangleShortEdge = triangleLongEdge * cos(Math.toRadians(60.0)).toFloat()

            val hpRatio = (stats.getOrDefault(Stats.HP, 0).toFloat() / maximum).coerceIn(0F, 1F)
            val atkRatio = (stats.getOrDefault(Stats.ATTACK, 0).toFloat() / maximum).coerceIn(0F, 1F)
            val defRatio = (stats.getOrDefault(Stats.DEFENCE, 0).toFloat() / maximum).coerceIn(0F, 1F)
            val spAtkRatio = (stats.getOrDefault(Stats.SPECIAL_ATTACK, 0).toFloat() / maximum).coerceIn(0F, 1F)
            val spDefRatio = (stats.getOrDefault(Stats.SPECIAL_DEFENCE, 0).toFloat() / maximum).coerceIn(0F, 1F)
            val spdRatio = (stats.getOrDefault(Stats.SPEED, 0).toFloat() / maximum).coerceIn(0F, 1F)

            val hpPoint = Vec2(hexCenterX, hexCenterY - minTriangleSize - hpRatio * triangleLongEdge)
            val attackPoint = Vec2(hexCenterX + minXTriangleLen + atkRatio * triangleMediumEdge, hexCenterY - minYTriangleLen - atkRatio * triangleShortEdge)
            val defencePoint = Vec2(hexCenterX + minXTriangleLen + defRatio * triangleMediumEdge, hexCenterY + minYTriangleLen + defRatio * triangleShortEdge)
            val specialAttackPoint = Vec2(hexCenterX - minXTriangleLen - spAtkRatio * triangleMediumEdge, hexCenterY - minYTriangleLen - spAtkRatio * triangleShortEdge)
            val specialDefencePoint = Vec2(hexCenterX - minXTriangleLen - spDefRatio * triangleMediumEdge, hexCenterY + minYTriangleLen + spDefRatio * triangleShortEdge)
            val speedPoint = Vec2(hexCenterX, hexCenterY + minTriangleSize + spdRatio * triangleLongEdge)

            val centerPoint = Vec2(hexCenterX, hexCenterY)

            drawTriangle(colour, hpPoint, centerPoint, attackPoint)
            drawTriangle(colour, attackPoint, centerPoint, defencePoint)
            drawTriangle(colour, defencePoint, centerPoint, speedPoint)
            drawTriangle(colour, speedPoint, centerPoint, specialDefencePoint)
            drawTriangle(colour, specialDefencePoint, centerPoint, specialAttackPoint)
            drawTriangle(colour, specialAttackPoint, centerPoint, hpPoint)
        }

        private fun drawTriangle(
            colour: Vector3f,
            v1: Vec2,
            v2: Vec2,
            v3: Vec2
        ) {
            CobblemonResources.WHITE.let { RenderSystem.setShaderTexture(0, it) }
            RenderSystem.setShaderColor(colour.x, colour.y, colour.z, 0.6F)
            val bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION)
            bufferBuilder.addVertex(v1.x, v1.y, 10F)
            bufferBuilder.addVertex(v2.x, v2.y, 10F)
            bufferBuilder.addVertex(v3.x, v3.y, 10F)
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow())
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F)
        }
    }
}