package com.xapps.auth.persistence.mapper

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.model.FileData
import com.xapps.model.FileType
import com.xapps.model.mime
import com.xapps.model.toEncodedFile
import com.xapps.persistence.FileDataDocument
import org.springframework.stereotype.Component

@Component
class FileDataMapper {

    fun toDomain(doc: FileDataDocument): FileData {
        return FileData(
            fileName = doc.fileName,
            encodedFile = doc.encodedFile(doc.fileBytes)
        )
    }

    fun toDocument(domain: FileData): FileDataDocument {
        return FileDataDocument(
            fileName = domain.fileName,
            fileBytes = domain.encodedFile.bytes,
            fileMime = domain.mime()
        )
    }

    private fun FileDataDocument.encodedFile(
        bytes: ByteArray
    ): EncodedFile {

        val fileType = FileType.fileTypeOrNull(mime = fileMime)
            ?: error("Unknown file mime type: $fileMime")

        return fileType.toEncodedFile(bytes)
    }
}