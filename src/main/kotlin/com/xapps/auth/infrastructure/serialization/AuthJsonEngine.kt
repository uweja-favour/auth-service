@file: OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)

package com.xapps.auth.infrastructure.serialization

import com.xapps.platform.serialization_core.BaseJsonEngine
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlin.time.ExperimentalTime

object AuthJsonEngine {

    val module =
        SerializersModule {
            // register other contextual serializers as needed via startup registration
        }

    val json: Json =
        Json {
            serializersModule = module + BaseJsonEngine.baseModule

            // LENIENT + RESILIENT settings
            ignoreUnknownKeys = true        // extra fields won't fail decoding
            isLenient = true                // accept slightly malformed JSON (e.g., single quotes)
            coerceInputValues = true        // missing/nullable values can be coerced rather than failing
            allowSpecialFloatingPointValues = true
            encodeDefaults = false
            prettyPrint = false
            explicitNulls = false
//            classDiscriminator = "class_type"
        }


    inline fun <reified T> safeDecode(
        jsonString: String,
        deserializer: KSerializer<T>,
        context: String = "JsonEngine.decode"
    ): T? {
        return try {
            json.decodeFromString(deserializer, jsonString)
        } catch (ex: Throwable) {
            JsonLog.error(context, "safeDecode failed for ${T::class.simpleName}", "payload" to JsonLog.truncate(jsonString), cause = ex)
            null
        }
    }

    /**
     * Safe encode → logs and returns "{}" on failure.
     */
    inline fun <reified T> safeEncode(
        data: T,
        serializer: KSerializer<T>,
        context: String = "JsonEngine.encode"
    ): String {
        return try {
            json.encodeToString(serializer, data)
        } catch (ex: Exception) {
            JsonLog.error("JsonEncode", "Failed to encode ${T::class.simpleName}. Context=$context", cause = ex)
            "{}"
        }
    }
}
