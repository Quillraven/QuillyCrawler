package com.github.quillraven.quillycrawler.ui.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.I18NBundle
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.StringBuilder
import com.github.quillraven.commons.input.XboxInputProcessor
import com.github.quillraven.quillycrawler.ashley.component.GearType
import com.github.quillraven.quillycrawler.ashley.component.StatsType
import com.github.quillraven.quillycrawler.ui.*
import com.github.quillraven.quillycrawler.ui.model.InventoryListener
import com.github.quillraven.quillycrawler.ui.model.InventoryViewModel
import ktx.collections.GdxArray
import ktx.scene2d.*
import java.util.*
import com.badlogic.gdx.scenes.scene2d.ui.List as GdxList

class InventoryView(private val viewModel: InventoryViewModel, private val bundle: I18NBundle) : View(),
  InventoryListener {
  // item details
  private val goldLabel: Label
  private val itemsScrollPane: ScrollPane
  private val bagItems: GdxList<String>
  private val itemImage: Image
  private val itemDescriptionLabel: Label

  // gear labels
  private val helmetLabel: Label
  private val amuletLabel: Label
  private val armorLabel: Label
  private val weaponLabel: Label
  private val shieldLabel: Label
  private val glovesLabel: Label
  private val bootsLabel: Label

  // stats labels
  private val lifeLabel: Label
  private val manaLabel: Label
  private val strengthLabel: Label
  private val agilityLabel: Label
  private val intelligenceLabel: Label
  private val physDamageLabel: Label
  private val physArmorLabel: Label
  private val magicDamageLabel: Label
  private val magicArmorLabel: Label

  init {
    setFillParent(true)
    background = skin.getDrawable(SkinImages.WINDOW.regionKey)

    // header
    textButton(bundle["InventoryView.title"], SkinTextButtonStyle.TITLE.name) { cell ->
      cell.expand()
        .top().padTop(8f)
        .height(25f).width(95f)
        .colspan(2)
        .row()
    }

    // item bag table
    table { cell ->
      background = skin.getDrawable(SkinImages.FRAME_1.regionKey)
      defaults().expand().fill()

      // gold info
      table { tableCell ->
        background = skin.getDrawable(SkinImages.FRAME_2.regionKey)

        image(SkinImages.GOLD.regionKey)
        this@InventoryView.goldLabel = label("", SkinLabelStyle.DEFAULT.name) { labelCell ->
          labelCell.padLeft(3f)
        }

        tableCell.expand(false, false)
          .padTop(3f)
          .height(15f).width(65f)
          .row()
      }

      // items
      val itemTable = Table(skin).apply {
        defaults().expand().fill().pad(3f, 2f, 0f, 2f)
        this@InventoryView.bagItems = GdxList<String>(skin, SkinListStyle.DEFAULT.name)
        this.add(this@InventoryView.bagItems)
      }
      this@InventoryView.itemsScrollPane = scrollPane(SkinScrollPaneStyle.NO_BGD.name) { spCell ->
        setScrollingDisabled(true, false)
        setScrollbarsVisible(false)
        actor = itemTable
        spCell.expand().padBottom(3f)
      }

      cell.expand()
        .padBottom(3f)
        .width(95f).height(120f)
    }

    // item details and stats table
    table { tableCell ->
      background = skin.getDrawable(SkinImages.FRAME_1.regionKey)

      // item details
      this@InventoryView.itemImage = image(skin.getDrawable(SkinImages.UNDEFINED.regionKey)) { cell ->
        setScaling(Scaling.fit)
        cell.top().padLeft(4f).padTop(6f).minWidth(20f)
      }
      this@InventoryView.itemDescriptionLabel = label("", SkinLabelStyle.DEFAULT.name) { cell ->
        setAlignment(Align.left)
        wrap = true
        cell.expandX().fill()
          .padLeft(4f).padRight(4f).padTop(2f)
          .height(23f)
          .row()
      }

      // gear
      table { gearTableCell ->
        background = skin.getDrawable(SkinImages.FRAME_2.regionKey)
        defaults().width(40f).expandX().fill().left()

        this@InventoryView.helmetLabel = label("", SkinLabelStyle.DEFAULT.name) { it.padLeft(8f) }
        this@InventoryView.amuletLabel = label("", SkinLabelStyle.DEFAULT.name) { it.row() }
        this@InventoryView.armorLabel = label("", SkinLabelStyle.DEFAULT.name) { it.padLeft(8f) }
        this@InventoryView.shieldLabel = label("", SkinLabelStyle.DEFAULT.name) { it.row() }
        this@InventoryView.bootsLabel = label("", SkinLabelStyle.DEFAULT.name) { it.padLeft(8f) }
        this@InventoryView.glovesLabel = label("", SkinLabelStyle.DEFAULT.name) { it.row() }
        this@InventoryView.weaponLabel = label("", SkinLabelStyle.DEFAULT.name) { it.padLeft(8f) }

        gearTableCell.expand().fill().colspan(2)
          .padTop(2f)
          .width(176f).height(34f)
          .row()
      }

      // stats
      table { statsTableCell ->
        background = skin.getDrawable(SkinImages.FRAME_2.regionKey)
        defaults().width(86f).fill().left()

        this@InventoryView.lifeLabel = label("", SkinLabelStyle.DEFAULT.name) { it.padLeft(8f) }
        this@InventoryView.physDamageLabel = label("", SkinLabelStyle.DEFAULT.name) { it.row() }
        this@InventoryView.manaLabel = label("", SkinLabelStyle.DEFAULT.name) { it.padLeft(8f) }
        this@InventoryView.magicDamageLabel = label("", SkinLabelStyle.DEFAULT.name) { it.row() }
        this@InventoryView.physArmorLabel = label("", SkinLabelStyle.DEFAULT.name) { it.padLeft(8f) }
        this@InventoryView.magicArmorLabel = label("", SkinLabelStyle.DEFAULT.name) { it.row() }

        this@InventoryView.strengthLabel = label("", SkinLabelStyle.DEFAULT.name) { it.colspan(2).padLeft(8f).row() }
        this@InventoryView.intelligenceLabel =
          label("", SkinLabelStyle.DEFAULT.name) { it.colspan(2).padLeft(8f).row() }
        this@InventoryView.agilityLabel =
          label("", SkinLabelStyle.DEFAULT.name) { it.colspan(2).padLeft(8f).padBottom(3f) }

        statsTableCell.expandX().fillX()
          .width(176f).height(51f)
          .pad(2f)
          .colspan(2)
      }

      tableCell.expand()
        .padBottom(3f)
        .width(180f).height(120f)
        .row()
    }

    // controls table
    table { cell ->
      defaults().padRight(2f)

      image(skin.getDrawable(SkinImages.GAME_PAD_DOWN.regionKey))
      image(skin.getDrawable(SkinImages.GAME_PAD_UP.regionKey))
      image(skin.getDrawable(SkinImages.KEY_BOARD_DOWN.regionKey))
      image(skin.getDrawable(SkinImages.KEY_BOARD_UP.regionKey))
      label(this@InventoryView.bundle["InventoryView.navigateInfo1"], SkinLabelStyle.DEFAULT.name)

      image(skin.getDrawable(SkinImages.GAME_PAD_A.regionKey)) { it.padLeft(20f) }
      image(skin.getDrawable(SkinImages.KEY_BOARD_SPACE.regionKey))
      label(this@InventoryView.bundle["InventoryView.navigateInfo2"], SkinLabelStyle.DEFAULT.name)

      image(skin.getDrawable(SkinImages.GAME_PAD_B.regionKey)) { it.padLeft(20f) }
      image(skin.getDrawable(SkinImages.KEY_BOARD_ESCAPE.regionKey))
      label(this@InventoryView.bundle["InventoryView.navigateInfo3"], SkinLabelStyle.DEFAULT.name)

      cell.expand().left()
        .colspan(2)
        .padLeft(12f).padBottom(6f)
    }

    // debugAll()
  }

  override fun onShow() {
    viewModel.addInventoryListener(this)

    with(goldLabel) {
      text.setLength(0)
      text.append(viewModel.gold())
      invalidateHierarchy()
    }
    bagItems.run {
      clearItems()
      viewModel.load()
    }
  }

  override fun onHide() {
    viewModel.removeInventoryListener(this)
  }

  override fun onSelectionChange(newIndex: Int, regionKey: String, description: String) {
    bagItems.selectedIndex = newIndex

    itemImage.drawable = skin.getDrawable(regionKey)
    itemImage.isVisible = regionKey != SkinImages.UNDEFINED.regionKey

    itemDescriptionLabel.setText(description)

    if (bagItems.items.size > 0) {
      itemsScrollPane.scrollPercentY = bagItems.selectedIndex.toFloat() / bagItems.items.size
    } else {
      itemsScrollPane.scrollPercentY = 0f
    }
  }

  override fun onStatsUpdated(statsInfo: EnumMap<StatsType, StringBuilder>) {
    lifeLabel.setText(statsInfo[StatsType.LIFE])
    manaLabel.setText(statsInfo[StatsType.MANA])
    strengthLabel.setText(statsInfo[StatsType.STRENGTH])
    agilityLabel.setText(statsInfo[StatsType.AGILITY])
    intelligenceLabel.setText(statsInfo[StatsType.INTELLIGENCE])
    physDamageLabel.setText(statsInfo[StatsType.PHYSICAL_DAMAGE])
    physArmorLabel.setText(statsInfo[StatsType.PHYSICAL_ARMOR])
    magicDamageLabel.setText(statsInfo[StatsType.MAGIC_DAMAGE])
    magicArmorLabel.setText(statsInfo[StatsType.MAGIC_ARMOR])
  }

  override fun onGearUpdated(gearInfo: EnumMap<GearType, StringBuilder>) {
    helmetLabel.setText(gearInfo[GearType.HELMET])
    amuletLabel.setText(gearInfo[GearType.AMULET])
    armorLabel.setText(gearInfo[GearType.ARMOR])
    weaponLabel.setText(gearInfo[GearType.WEAPON])
    shieldLabel.setText(gearInfo[GearType.SHIELD])
    bootsLabel.setText(gearInfo[GearType.BOOTS])
    glovesLabel.setText(gearInfo[GearType.GLOVES])
  }

  override fun onBagUpdated(items: GdxArray<String>, selectionIndex: Int) {
    bagItems.run {
      clear()
      setItems(items)
      selectedIndex = selectionIndex
    }
  }

  override fun keyDown(keycode: Int): Boolean {
    when (keycode) {
      Input.Keys.DOWN -> viewModel.moveItemSelectionIndex(1)
      Input.Keys.UP -> viewModel.moveItemSelectionIndex(-1)
      Input.Keys.SPACE -> viewModel.equipOrUseSelectedItem()
      Input.Keys.ESCAPE -> viewModel.returnToGame()
      else -> return false
    }

    return true
  }

  override fun buttonDown(controller: Controller?, buttonCode: Int): Boolean {
    when (buttonCode) {
      XboxInputProcessor.BUTTON_DOWN -> viewModel.moveItemSelectionIndex(1)
      XboxInputProcessor.BUTTON_UP -> viewModel.moveItemSelectionIndex(-1)
      XboxInputProcessor.BUTTON_A -> viewModel.equipOrUseSelectedItem()
      XboxInputProcessor.BUTTON_B -> viewModel.returnToGame()
      else -> return false
    }

    return true
  }
}
