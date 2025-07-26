package com.example.scribesoul.commands // Sesuaikan dengan nama package Anda

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.scribesoul.models.* // Impor model dari file sebelumnya

// --- Implementasi Command Pattern ---
interface Command {
    fun execute()
    fun undo()
}

class AddDrawableCommand(private val drawable: DrawablePath, private val paths: MutableList<DrawablePath>) : Command {
    override fun execute() { paths.add(drawable) }
    override fun undo() { paths.remove(drawable) }
}

class EraseCommand(
    private val originalPaths: List<DrawablePath>,
    private val pathsAfterErase: List<DrawablePath>,
    private val paths: MutableList<DrawablePath>
) : Command {
    override fun execute() {
        paths.clear()
        paths.addAll(pathsAfterErase)
    }
    override fun undo() {
        paths.clear()
        paths.addAll(originalPaths)
    }
}

class AddTextCommand(private val text: EditableText, private val texts: MutableList<EditableText>) : Command {
    override fun execute() { texts.add(text) }
    override fun undo() { texts.remove(text) }
}
class DeleteItemsCommand(
    private val itemsToDelete: List<Movable>,
    private val allLists: List<MutableList<out Movable>>
) : Command {
    // Kita asumsikan urutan list selalu sama: texts, shapes, imageLayers, groups
    private val texts = allLists[0] as MutableList<EditableText>
    private val shapes = allLists[1] as MutableList<ShapeItem>
    private val imageLayers = allLists[2] as MutableList<ImageLayer>
    private val groups = allLists[3] as MutableList<ItemGroup>

    private val deletedData = mutableListOf<Triple<Movable, Int, MutableList<out Movable>>>()

    override fun execute() {
        deletedData.clear()
        // Iterasi pada salinan agar aman saat menghapus
        itemsToDelete.toList().forEach { item ->
            when (item) {
                is EditableText -> deleteFrom(item, texts)
                is ShapeItem -> deleteFrom(item, shapes)
                is ImageLayer -> deleteFrom(item, imageLayers)
                is ItemGroup -> deleteFrom(item, groups)
            }
        }
    }

    private fun <T : Movable> deleteFrom(item: T, list: MutableList<T>) {
        val index = list.indexOf(item)
        if (index != -1) {
            deletedData.add(Triple(item, index, list))
            list.removeAt(index)
        }
    }

    override fun undo() {
        deletedData.sortedBy { it.second }.forEach { (item, index, list) ->
            @Suppress("UNCHECKED_CAST")
            (list as MutableList<Movable>).add(index, item)
        }
    }
}

class AddShapeCommand(private val shape: ShapeItem, private val shapes: MutableList<ShapeItem>) : Command {
    override fun execute() { shapes.add(shape) }
    override fun undo() { shapes.remove(shape) }
}

class AddImageCommand(private val image: ImageLayer, private val imageLayers: MutableList<ImageLayer>) : Command {
    override fun execute() { imageLayers.add(image) }
    override fun undo() { imageLayers.remove(image) }
}

class MoveCommand(private val target: Movable, private val from: Offset, private val to: Offset) : Command {
    override fun execute() { target.offset = to }
    override fun undo() { target.offset = from }
}

class ChangeColorCommand(
    private val targets: List<Any>, // <-- Ubah dari List<Movable> ke List<Any>
    private val newColor: Color
) : Command {
    // Logika di dalam sini tidak perlu diubah, karena sudah menggunakan filterIsInstance
    private val oldColors = targets.filterIsInstance<Colorable>().associateWith { it.color }

    override fun execute() {
        targets.filterIsInstance<Colorable>().forEach { it.color = newColor }
    }

    override fun undo() {
        oldColors.forEach { (item, oldColor) ->
            (item as Colorable).color = oldColor
        }
    }
}

class GroupCommand(
    private val itemsToGroup: List<Movable>,
    private val allLists: List<MutableList<out Movable>>,
    private val groups: MutableList<ItemGroup>
) : Command {
    private lateinit var newGroup: ItemGroup
    // Simpan posisi absolut asli untuk proses undo
    private val originalOffsets = itemsToGroup.associateWith { it.offset }

    override fun execute() {
        // Hitung titik tengah dari semua item yang dipilih
        val centerX = itemsToGroup.map { it.offset.x }.average().toFloat()
        val centerY = itemsToGroup.map { it.offset.y }.average().toFloat()
        val groupCenter = Offset(centerX, centerY)

        // PENTING: Ubah offset setiap item menjadi RELATIF terhadap pusat grup
        itemsToGroup.forEach { item ->
            item.offset = item.offset - groupCenter
        }

        newGroup = ItemGroup(items = itemsToGroup.toMutableList(), offset = groupCenter)

        // Hapus item dari list aslinya
        allLists.forEach { list ->
            list.removeAll(itemsToGroup)
        }
        // Tambahkan grup baru
        groups.add(newGroup)
    }

    override fun undo() {
        // Hapus grup
        groups.remove(newGroup)
        // Kembalikan item ke list aslinya DAN kembalikan offset absolut aslinya
        newGroup.items.forEach { item ->
            // SOLUSI: Hitung offset absolut baru dari item.
            // Offset absolut = posisi terakhir grup + offset relatif item di dalam grup.
            // Saat ini, `item.offset` adalah offset RELATIF karena sudah diubah di `execute()`.
            item.offset = newGroup.offset + item.offset

            // Kembalikan item ke list yang sesuai
            when (item) {
                is EditableText -> (allLists[0] as MutableList<EditableText>).add(item)
                is ShapeItem -> (allLists[1] as MutableList<ShapeItem>).add(item)
                is ImageLayer -> (allLists[2] as MutableList<ImageLayer>).add(item)
            }
        }
    }
}

// Command BARU untuk menyalin
class CopyCommand(
    private val itemsToCopy: List<Movable>,
    private val allLists: List<MutableList<out Movable>>
) : Command {
    private val copiedItems = mutableListOf<Movable>()

    override fun execute() {
        copiedItems.clear()
        itemsToCopy.forEach { item ->
            val newItem = when (item) {
                is EditableText -> item.copy(offset = item.offset + Offset(20f, 20f))
                is ShapeItem -> item.copy(offset = item.offset + Offset(20f, 20f))
                is ImageLayer -> item.copy(offset = item.offset + Offset(20f, 20f))
                is ItemGroup -> { // Juga bisa menyalin grup
                    val newGroupItems = item.items.map {
                        when(it) {
                            is EditableText -> it.copy() // Hapus penambahan offset di sini
                            is ShapeItem -> it.copy()   // Hapus penambahan offset di sini
                            is ImageLayer -> it.copy()  // Hapus penambahan offset di sini
                            else -> it
                        }
                    }.toMutableList()
                    // SALIN GRUP DAN HANYA UBAH OFFSET GRUP ITU SENDIRI
                    item.copy(items = newGroupItems, offset = item.offset + Offset(20f, 20f))
                }
                else -> item
            }
            copiedItems.add(newItem)
        }

        // Tambahkan item yang disalin ke list yang sesuai
        copiedItems.forEach { item ->
            when (item) {
                is EditableText -> (allLists[0] as MutableList<EditableText>).add(item)
                is ShapeItem -> (allLists[1] as MutableList<ShapeItem>).add(item)
                is ImageLayer -> (allLists[2] as MutableList<ImageLayer>).add(item)
                is ItemGroup -> (allLists[3] as MutableList<ItemGroup>).add(item)
            }
        }
    }

    override fun undo() {
        // Hapus item yang disalin
        copiedItems.forEach { item ->
            when (item) {
                is EditableText -> (allLists[0] as MutableList<EditableText>).remove(item)
                is ShapeItem -> (allLists[1] as MutableList<ShapeItem>).remove(item)
                is ImageLayer -> (allLists[2] as MutableList<ImageLayer>).remove(item)
                is ItemGroup -> (allLists[3] as MutableList<ItemGroup>).remove(item)
            }
        }
    }
}

// Command BARU untuk rotasi
class RotateCommand(
    private val target: Movable,
    private val from: Float,
    private val to: Float
) : Command {
    override fun execute() { target.rotation = to }
    override fun undo() { target.rotation = from }
}

class ResizeCommand(
    private val target: Movable,
    private val fromSize: Size,
    private val toSize: Size
) : Command {
    override fun execute() {
        when (target) {
            is ImageLayer -> target.size = toSize
            // Bisa ditambahkan untuk ShapeItem jika diperlukan di masa depan
        }
    }

    override fun undo() {
        when (target) {
            is ImageLayer -> target.size = fromSize
        }
    }
}

enum class LayerDirection {
    TO_FRONT, TO_BACK
}

// Tambahkan class Command baru ini di file DrawingCommands.kt
class LayeringCommand(
    private val target: Movable,
    private val allLists: List<MutableList<out Movable>>,
    private val direction: LayerDirection
) : Command {
    private var originalIndex = -1
    private var sourceList: MutableList<out Movable>? = null

    // Helper untuk menemukan list asal dari sebuah item
    @Suppress("UNCHECKED_CAST")
    private fun findSourceList(item: Movable): MutableList<out Movable>? {
        return when (item) {
            is EditableText -> allLists[0] as MutableList<EditableText>
            is ShapeItem -> allLists[1] as MutableList<ShapeItem>
            is ImageLayer -> allLists[2] as MutableList<ImageLayer>
            is ItemGroup -> allLists[3] as MutableList<ItemGroup>
            else -> null
        }
    }

    override fun execute() {
        sourceList = findSourceList(target)
        sourceList?.let { list ->
            originalIndex = list.indexOf(target)
            if (originalIndex != -1) {
                // Hapus dulu dari posisi lama
                (list as MutableList<Movable>).remove(target)
                // Tambahkan ke depan atau belakang
                if (direction == LayerDirection.TO_FRONT) {
                    list.add(target) // Tambah di akhir = render paling atas
                } else {
                    list.add(0, target) // Tambah di awal = render paling bawah
                }
            }
        }
    }

    override fun undo() {
        sourceList?.let { list ->
            if (originalIndex != -1) {
                // Hapus dari posisi baru
                (list as MutableList<Movable>).remove(target)
                // Kembalikan ke posisi semula
                list.add(originalIndex, target)
            }
        }
    }
}