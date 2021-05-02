package com.github.quillraven.quillycrawler.ashley.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.Pool
import com.github.quillraven.quillycrawler.combat.CombatOrderEffect
import com.github.quillraven.quillycrawler.combat.CombatOrderEffectUndefined
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.collections.GdxArray

class CombatComponent : Component, Pool.Poolable {
  var effect: CombatOrderEffect = CombatOrderEffectUndefined
  val orderTargets = GdxArray<Entity>()

  override fun reset() {
    orderTargets.clear()
    effect = CombatOrderEffectUndefined
  }

  companion object {
    val MAPPER = mapperFor<CombatComponent>()
  }
}

val Entity.combatCmp: CombatComponent
  get() = this[CombatComponent.MAPPER]
    ?: throw GdxRuntimeException("CombatComponent for entity '$this' is null")
