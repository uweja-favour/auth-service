package com.xapps.auth.admin.audit
//
//import com.fasterxml.jackson.core.type.TypeReference
//import com.fasterxml.jackson.databind.ObjectMapper
//import jakarta.persistence.AttributeConverter
//import jakarta.persistence.Converter
//import org.springframework.beans.factory.annotation.Autowired
//
//@Converter
//class JsonMapConverter : AttributeConverter<Map<String, String>, String> {
//    @Autowired
//    private lateinit var mapper: ObjectMapper
//
//    override fun convertToDatabaseColumn(attribute: Map<String, String>): String =
//        mapper.writeValueAsString(attribute)
//
//    override fun convertToEntityAttribute(dbData: String): Map<String, String> =
//        mapper.readValue(dbData, object : TypeReference<Map<String, String>>() {})
//}
