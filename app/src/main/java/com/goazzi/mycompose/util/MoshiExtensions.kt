package com.goazzi.mycompose.util

import com.goazzi.mycompose.model.pixabay.HitMedia
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
Usage for extensions.
Initialize Moshi in your Converter class
 */

object MoshiExtensions {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(DefaultStringAdapter(defaultValue = "")) // Provide default value as needed
        .add(DefaultIntAdapter(defaultValue = 0)) // Provide default value as needed
        .build()

    val moshiPixabay: Moshi = Moshi.Builder()
        .add(HitMediaAdapter())  // Custom adapter
        .add(KotlinJsonAdapterFactory())
        .build()
}

/**
 * [Moshi] extension to transform a [List] to Json
 * */
inline fun <reified T> Moshi.listToJson(data: List<T>): String =
    adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
        .toJson(data)

/**
 * [Moshi] extension to transform a json object to a [List]
 * */
inline fun <reified T> Moshi.jsonToList(json: String): List<T>? =
    adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java))
        .fromJson(json)

/**
 * [Moshi] extension to transform a map to Json
 * */
inline fun <reified T, reified K> Moshi.mapToJson(data: Map<T, K>): String =
    adapter<Map<T, K>>(
        Types.newParameterizedType(
            MutableMap::class.java,
            T::class.java, K::class.java
        )
    ).toJson(data)

/**
 * [Moshi] extension to transform a json to Map
 * */
inline fun <reified T, reified K> Moshi.jsonToMap(json: String): Map<T, K>? =
    adapter<Map<T, K>>(
        Types.newParameterizedType(
            MutableMap::class.java,
            T::class.java,
            K::class.java
        )
    ).fromJson(json)


/*
* [Moshi] extension to transform an object to json
* */
inline fun <reified T> Moshi.objectToJson(data: T): String =
    adapter(T::class.java).toJson(data)

/*
* [Moshi] extension to transform json to an object
* */
inline fun <reified T> Moshi.jsonToObject(json: String): T? =
    adapter(T::class.java).fromJson(json)

/**
 * [Moshi] extension to transform multiple lists
 * */
inline fun <reified T> Moshi.multipleListsToJson(data: List<List<T>>): String =
    adapter<List<List<T>>>(
        Types.newParameterizedType(
            List::class.java,
            T::class.java
        )
    ).toJson(data)

inline fun <reified T> Moshi.jsonToMultipleLists(json: String): List<List<T>>? =
    adapter<List<List<T>>>(
        Types.newParameterizedType(
            List::class.java,
            T::class.java
        )
    ).fromJson(json)
// End transform multiple lists

/**
 * Adapter for Strings with a default value
 */
class DefaultStringAdapter(private val defaultValue: String) : JsonAdapter<String>() {
    @FromJson
    override fun fromJson(reader: JsonReader): String {
        return if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull<Unit>() // Skip null value
            defaultValue // Return the default value
        } else {
            reader.nextString()
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: String?) {
        writer.value(value)
    }
}

/**
 * Adapter for Ints with a default value
 */
class DefaultIntAdapter(private val defaultValue: Int) : JsonAdapter<Int>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Int {
        return if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull<Unit>()
            defaultValue
        } else {
            reader.nextInt()
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Int?) {
        writer.value(value)
    }
}

class HitMediaAdapter {
    @FromJson
    fun fromJson(
        reader: JsonReader,
        imageAdapter: JsonAdapter<HitMedia.Image>,
        videoAdapter: JsonAdapter<HitMedia.Video>
    ): HitMedia? {
        val jsonMap = reader.readJsonValue() as? Map<*, *> ?: return null

        return if (jsonMap["videos"] != null) {  // ✅ Check if it's a video
            videoAdapter.fromJsonValue(jsonMap)
        } else {  // ✅ Otherwise, it's an image
            imageAdapter.fromJsonValue(jsonMap)
        }
    }

    @ToJson
    fun toJson(writer: JsonWriter, value: HitMedia?) {
        throw UnsupportedOperationException("Serialization not supported")
    }
}
