package eu.metatools.kfunnels.tools

import com.fasterxml.jackson.core.JsonFactory
import eu.metatools.kfunnels.*
import eu.metatools.kfunnels.base.ServiceModule
import eu.metatools.kfunnels.base.std
import eu.metatools.kfunnels.tools.json.JsonSource
import eu.metatools.kfunnels.tools.json.JsonSourceConfig
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URL
import kotlin.reflect.KProperty1

/**
 * Indexed raw data store. Uses two files and [file] as the base folder.
 * @param file The file pointing to the base folder.
 * @param indexItem The property to index.
 * @param typeItem The type of the item (auto-inferred when using [iraw]).
 * @param typeIndex The type of the index values (auto-inferred when using [iraw]).
 * @param module The module to use.
 * @param utf8 True if UTF-8 is used for string encoding.
 */
class Iraw<T, U>(val file: File,
                 val indexItem: KProperty1<T, U>,
                 val typeItem: Type,
                 val typeIndex: Type,
                 val module: Module = ServiceModule.std,
                 val utf8: Boolean = true) {
    private val MAX_BUFFER_SIZE = 8192

    /**
     * Last time the index was retrieved.
     */
    private var lastTime = Long.MIN_VALUE

    /**
     * The index at last retrieval.
     */
    private var lastIndex = mapOf<U, Int>()

    /**
     * Gets the last edit time of the files.
     */
    private val editTime get() = Math.max(dataFile.lastModified(), indexFile.lastModified())

    /**
     * The data file.
     */
    val dataFile get() = File(file, "data")

    /**
     * The index file.
     */
    val indexFile get() = File(file, "index")

    /**
     * True if the iraw store exists.
     */
    fun exists() = dataFile.exists() && indexFile.exists()

    /**
     * Deletes the store. Deletes the base directory only if it's empty.
     */
    fun delete() {
        dataFile.delete()
        indexFile.delete()
        if (file.listFiles().isEmpty())
            file.delete()
    }

    /**
     * Saves all elements.
     */
    fun save(list: List<T>) {

        file.mkdirs()

        FileOutputStream(dataFile).use {
            val dataSink = IndexedRawSink(it, indexItem.name, utf8)
            module.write(dataSink, Type.list(typeItem), list)

            FileOutputStream(indexFile).use {
                val indexSink = RawSink(it, utf8)
                module.write(indexSink, Type.list(Type.pair(typeIndex, Type.int)), dataSink.index().toList())
            }
        }
    }

    /**
     * Loads all elements
     */
    fun load(): List<T> {
        return FileInputStream(dataFile).use {
            module.read<List<T>>(RawSource(it, utf8), Type.list(typeItem))
        }
    }

    /**
     * Stream loads all elements.
     */
    fun stream() = (ReceiverModule then module).stream<T>(typeItem) {
        RawSource(FileInputStream(dataFile), utf8)
    }

    /**
     * Finds the element with the given index and returns it, otherwise returns null.
     */
    fun find(u: U): T? {
        if (indexOutdated()) {
            // Rebuild index
            lastIndex = readIndex()

            // Store edit time
            lastTime = editTime
        }

        // Get position or return null
        val position = lastIndex[u] ?: return null

        // Get in real file by spooling
        return FileInputStream(dataFile).use {
            it.skip(position.toLong())
            module.read<T>(RawSource(it, utf8), typeItem)
        }
    }

    /**
     * Writes [length] bytes from one file stream to another, using the given buffer.
     */
    private fun transfer(read: FileInputStream, write: FileOutputStream, buffer: ByteArray, length: Int) {
        if (buffer.size < length) {
            var next = buffer.size
            var transferred = 0
            while (transferred < length) {
                transferred += read.read(buffer, 0, next)
                write.write(buffer, 0, next)
                next = Math.min(buffer.size, length - transferred)
            }
        } else {
            read.read(buffer, 0, length)
            write.write(buffer, 0, length)
        }
    }

    /**
     * Removes one item by index.
     */
    fun remove(u: U) = remove(listOf(u))

    /**
     * Removes some items by index.
     */
    fun remove(vararg us: U) = remove(us.toList())

    /**
     * Removes the items with the given indices, returns the indices that were removed.
     */
    fun remove(us: List<U>): List<U> {
        // Pre-calculate file size
        val size = dataFile.length().toInt()

        // Get indices
        val indices = indices()

        // Find those who are contained
        val actual = us.filter { it in indices.keys }

        // Map to slices, i.e., connect to start of next item or end of file
        val slices = actual.map {
            val from = indices.getValue(it)
            val to = indices.values.filter { it > from }.min() ?: size
            from to to
        }.sortedBy { it.first }

        // If nothing to remove, terminate
        if (actual.isEmpty())
            return actual

        // Find the longest slice
        val longest = slices.fold(slices.first()) { l, r ->
            Math.max(l.first, l.second - r.first) to r.second
        }.first

        // Make a buffer that can hold this slice, but limit in size.
        val buffer = ByteArray(Math.min(MAX_BUFFER_SIZE, longest))

        // Make an update file to write to
        val updateFile = File(file, "update")

        // Double-stream
        FileInputStream(dataFile).use { r ->
            FileOutputStream(updateFile).use { w ->
                // Mark last valid position
                var validPos = 0

                // Write content between slices
                for ((f, t) in slices) {
                    if (validPos < f)
                        transfer(r, w, buffer, f - validPos)
                    r.skip((t - f).toLong())
                    validPos = t
                }

                // After slices, transfer rest of file
                if (validPos < size)
                    transfer(r, w, buffer, size - validPos)
            }
        }

        // Move files
        dataFile.delete()
        updateFile.renameTo(dataFile)

        // Track the skipped offsets
        var offset = 0

        // New index created on the fly
        val newIndex = hashMapOf<U, Int>()

        // Linearize the entries by their positions
        val linear = indices.entries.sortedBy { it.value }

        // Paired up write (next is needed to adjust the offset to the new postition
        var (lu, lp) = linear.first()
        for ((u, p) in linear.drop(1)) {
            if (lu in us)
                offset += p - lp
            else
                newIndex[lu] = lp - offset
            lu = u
            lp = p
        }

        // Clone of offset write for the last element.
        if (lu !in us)
            newIndex[lu] = lp - offset

        // Transfer indices
        lastIndex = newIndex

        // Write indices
        FileOutputStream(indexFile).use {
            val indexSink = RawSink(it, utf8)
            module.write(indexSink, Type.list(Type.pair(typeIndex, Type.int)), indices.toList())
        }

        // Adjust edit time
        lastTime = editTime

        return actual
    }

    /**
     * True if the index is outdated.
     */
    private fun indexOutdated() = lastTime < editTime

    /**
     * Reads the index from the file.
     */
    private fun readIndex() =
            FileInputStream(indexFile).use {
                module.read<List<Pair<U, Int>>>(RawSource(it, utf8), Type.list(Type.pair(typeIndex, Type.int)))
            }.associate {
                it
            }

    /**
     * Gets or reads the current index.
     */
    fun indices() =
            if (indexOutdated())
                readIndex()
            else
                lastIndex
}

/**
 * Creates the [Iraw] store using reified parameters to infer the item and key types.
 * @param file The folder to write the data and the index into.
 * @param index The member property defining the elements and the key.
 * @param module The module to use for serialization.
 * @param utf8 True if UTF-8 should be used for string encoding.
 */
inline fun <reified T, reified U> iraw(
        file: File, index: KProperty1<T, U>, module: Module = ServiceModule.std, utf8: Boolean = true) =
        Iraw<T, U>(file, index, Type.from<T>(), Type.from<U>(), module, utf8)