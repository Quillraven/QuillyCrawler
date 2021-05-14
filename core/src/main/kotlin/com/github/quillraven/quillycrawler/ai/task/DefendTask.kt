package com.github.quillraven.quillycrawler.ai.task

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.github.quillraven.quillycrawler.ashley.component.addCommand
import com.github.quillraven.quillycrawler.combat.command.CommandDefend

class DefendTask : LeafTask<Entity>() {
  override fun copyTo(task: Task<Entity>) = task

  override fun execute(): Status {
    `object`.addCommand<CommandDefend>()

    return Status.SUCCEEDED
  }
}
