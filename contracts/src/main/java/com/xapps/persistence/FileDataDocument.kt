package com.xapps.persistence

data class FileDataDocument(
    val fileName: String,
    val fileMime: String,

    // Room supports ByteArray natively.
    val fileBytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (this::class != other.let { it::class }) return false

        other as FileDataDocument

        if (fileName != other.fileName) return false
        if (fileMime != other.fileMime) return false
        if (!fileBytes.contentEquals(other.fileBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + fileMime.hashCode()
        result = 31 * result + fileBytes.contentHashCode()
        return result
    }
}