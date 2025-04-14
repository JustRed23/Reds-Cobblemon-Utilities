package dev.JustRed23.redscobblemonutilities.common.utils

import com.cobblemon.mod.common.pokemon.IVs

class StatComparer {

    companion object {
        fun isMax(ivs: IVs): Boolean {
            return ivs.all { isMax(it.value) }
        }

        fun isMax(stat: Int): Boolean {
            return stat == IVs.MAX_VALUE
        }

        fun isMin(ivs: IVs): Boolean {
            return ivs.all { isMin(it.value) }
        }

        fun isMin(stat: Int): Boolean {
            return stat == 0
        }
    }
}