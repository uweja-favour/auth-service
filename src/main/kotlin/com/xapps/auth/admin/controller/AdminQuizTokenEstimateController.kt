//package com.xapps.auth.admin.controller
//
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/api/admin/quiz-token-estimates")
//class AdminQuizTokenEstimateController(
//    private val quizTokenEstimates: QuizTokenEstimateProvider
//) {
//    @PostMapping("/{quizType}")
//    fun updateEstimate(
//        @PathVariable quizType: String,
//        @RequestParam newValue: Int
//    ): ResponseEntity<String> {
//        val parsed = try {
//            QuizQuestionType.valueOf(quizType.uppercase())
//        } catch (e: Exception) {
//            return ResponseEntity.badRequest().body("❌ Invalid quiz type: $quizType")
//        }
//
//        quizTokenEstimates.updateEstimate(parsed, newValue)
//        return ResponseEntity.ok("✅ Updated $parsed to $newValue tokens per question.")
//    }
//
//
////    @GetMapping
////    fun getAll(): Map<QuizQuestionType, Int> = quizTokenEstimates.allEstimates()
//}
