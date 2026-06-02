package com.xapps.note_summary

import com.xapps.model.NoteSummaryStyle
import com.xapps.model.QuizId
import com.xapps.question_generation.JobId
import kotlinx.serialization.Serializable

@Serializable
data class NoteSummaryGenerationSpec (
    val noteSummaryId: String,
    val userId: String,
    val jobId: JobId,

    val fileKey: String,
    val style: NoteSummaryStyle
)

fun createNoteSummaryGenerationSpec(
    noteSummaryId: String,
    userId: String,
    jobId: JobId,
    fileKey: String,
    style: NoteSummaryStyle
): NoteSummaryGenerationSpec {
    return NoteSummaryGenerationSpec(
        noteSummaryId = noteSummaryId,
        userId = userId,
        jobId = jobId,
        fileKey = fileKey,
        style = style
    )
}