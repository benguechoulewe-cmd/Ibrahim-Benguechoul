package com.example.data

import android.content.Context
import android.util.Log
import com.example.model.*
import com.example.simulation.SimulationEngine
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File
import java.io.FileOutputStream

object SaveManager {
    private const val TAG = "SaveManager"
    private const val PRIMARY_SAVE_FILE = "money_wars_save.json"
    private const val BACKUP_SAVE_FILE = "money_wars_save_backup.json"

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val gameStateAdapter by lazy {
        moshi.adapter(GameState::class.java)
    }

    /**
     * Atomically saves the game state to disk with automatic fallback backup.
     */
    @Synchronized
    fun saveGame(context: Context, state: GameState): Boolean {
        return try {
            val primaryFile = File(context.filesDir, PRIMARY_SAVE_FILE)
            val backupFile = File(context.filesDir, BACKUP_SAVE_FILE)
            val tempFile = File(context.filesDir, "money_wars_temp.json")

            val json = gameStateAdapter.toJson(state)

            // 1. Write to temporary file first
            FileOutputStream(tempFile).use { fos ->
                fos.write(json.toByteArray(Charsets.UTF_8))
                fos.flush()
            }

            // 2. If primary exists, copy to backup
            if (primaryFile.exists()) {
                primaryFile.copyTo(backupFile, overwrite = true)
            }

            // 3. Atomically rename temp to primary
            val renamed = tempFile.renameTo(primaryFile)
            if (!renamed) {
                tempFile.copyTo(primaryFile, overwrite = true)
                tempFile.delete()
            }

            Log.d(TAG, "Game successfully saved. Day: ${state.currentDay}, Nation: ${state.playerCountryId}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error saving game state: ${e.message}", e)
            false
        }
    }

    /**
     * Loads the saved game state, falling back to backup if corrupted.
     */
    @Synchronized
    fun loadGame(context: Context): GameState? {
        val primaryFile = File(context.filesDir, PRIMARY_SAVE_FILE)
        val backupFile = File(context.filesDir, BACKUP_SAVE_FILE)

        // Try primary
        if (primaryFile.exists()) {
            try {
                val json = primaryFile.readText(Charsets.UTF_8)
                val loadedState = gameStateAdapter.fromJson(json)
                if (loadedState != null && loadedState.isCampaignStarted) {
                    Log.d(TAG, "Loaded valid primary save. Day: ${loadedState.currentDay}")
                    return loadedState
                }
            } catch (e: Exception) {
                Log.w(TAG, "Primary save corrupted, attempting backup recovery: ${e.message}")
            }
        }

        // Fallback to backup
        if (backupFile.exists()) {
            try {
                val json = backupFile.readText(Charsets.UTF_8)
                val loadedState = gameStateAdapter.fromJson(json)
                if (loadedState != null && loadedState.isCampaignStarted) {
                    Log.d(TAG, "Restored state from backup save. Day: ${loadedState.currentDay}")
                    return loadedState
                }
            } catch (e: Exception) {
                Log.e(TAG, "Backup save also corrupted: ${e.message}")
            }
        }

        return null
    }

    /**
     * Checks if a valid campaign save exists.
     */
    fun hasSavedGame(context: Context): Boolean {
        val primaryFile = File(context.filesDir, PRIMARY_SAVE_FILE)
        return primaryFile.exists() && primaryFile.length() > 50
    }
}
