package com.github.quillraven.quillycrawler.combat

enum class CombatAiType {
  UNDEFINED, OFFENSIVE, DEFENSIVE, SUPPORTIVE
}

enum class TargetType {
  UNDEFINED, NO_TARGET, SINGLE_TARGET
}

interface CombatOrderEffect {
  val aiType: CombatAiType

  val manaCost: Int

  val targetType: TargetType

  fun start(order: CombatOrder) = Unit

  fun update(order: CombatOrder, deltaTime: Float): Boolean = true

  fun end(order: CombatOrder) = Unit

  fun reset() = Unit
}

object CombatOrderEffectUndefined : CombatOrderEffect {
  override val aiType: CombatAiType = CombatAiType.UNDEFINED
  override val manaCost: Int = 0
  override val targetType: TargetType = TargetType.UNDEFINED
}