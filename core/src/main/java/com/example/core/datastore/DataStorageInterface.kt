package com.example.core.datastore

/**
 * ## Interface for local data storage using DataStore Preferences.
 *
 * Supports types: [String], [Int], [Long], [Boolean], [Float].
 *
 * ### Usage:
 * Inject [DataStorage] (the implementation) via Hilt in ViewModel or Repository:
 * ```kotlin
 * @HiltViewModel
 * class ExampleViewModel @Inject constructor(
 *     private val dataStorage: DataStorage
 * ) : ViewModel() {
 *
 *     // Write
 *     fun saveUserName(name: String) {
 *         viewModelScope.launch {
 *             dataStorage.write("USER_NAME", name)        // String
 *             dataStorage.write("USER_AGE", 25)            // Int
 *             dataStorage.write("IS_LOGGED_IN", true)      // Boolean
 *         }
 *     }
 *
 *     // Read
 *     fun loadUserName() {
 *         viewModelScope.launch {
 *             val name = dataStorage.read("USER_NAME") as? String
 *             val age = dataStorage.read("USER_AGE") as? Int
 *             val isLoggedIn = dataStorage.read("IS_LOGGED_IN") as? Boolean
 *         }
 *     }
 *
 *     // Remove a specific key
 *     fun removeUserName() {
 *         viewModelScope.launch {
 *             dataStorage.remove("USER_NAME")
 *         }
 *     }
 *
 *     // Clear all data
 *     fun clearAll() {
 *         viewModelScope.launch {
 *             dataStorage.clear()
 *         }
 *     }
 * }
 * ```
 */
interface DataStorageInterface {

    suspend fun <T : Any> write(key: String, value: T)

    suspend fun read(key: String): Any?

    suspend fun remove(key: String)

    suspend fun clear()

}