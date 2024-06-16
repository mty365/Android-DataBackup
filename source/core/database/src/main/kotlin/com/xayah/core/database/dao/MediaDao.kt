package com.xayah.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.xayah.core.model.CompressionType
import com.xayah.core.model.OpType
import com.xayah.core.model.database.MediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Upsert(entity = MediaEntity::class)
    suspend fun upsert(items: List<MediaEntity>)

    @Upsert(entity = MediaEntity::class)
    suspend fun upsert(item: MediaEntity)

    @Query(
        "SELECT * FROM MediaEntity WHERE" +
                " indexInfo_opType = :opType AND extraInfo_blocked = :blocked"
    )
    suspend fun query(opType: OpType, blocked: Boolean): List<MediaEntity>

    @Query(
        "SELECT * FROM MediaEntity" +
                " WHERE indexInfo_opType = :opType AND indexInfo_preserveId = :preserveId" +
                " AND indexInfo_cloud = :cloud AND indexInfo_backupDir = :backupDir"
    )
    suspend fun query(opType: OpType, preserveId: Long, cloud: String, backupDir: String): List<MediaEntity>

    @Query(
        "SELECT * FROM MediaEntity WHERE" +
                " indexInfo_opType = :opType AND extraInfo_existed = 1 AND indexInfo_cloud = :cloud AND indexInfo_backupDir = :backupDir"
    )
    suspend fun query(opType: OpType, cloud: String, backupDir: String): List<MediaEntity>

    @Query(
        "SELECT * FROM MediaEntity" +
                " WHERE indexInfo_opType = :opType AND indexInfo_name = :name" +
                " AND indexInfo_cloud = :cloud AND indexInfo_backupDir = :backupDir"
    )
    suspend fun query(opType: OpType, name: String, cloud: String, backupDir: String): List<MediaEntity>

    @Query(
        "SELECT * FROM MediaEntity" +
                " WHERE indexInfo_opType = :opType AND indexInfo_preserveId = :preserveId AND indexInfo_name = :name" +
                " AND indexInfo_cloud = :cloud AND indexInfo_backupDir = :backupDir" +
                " LIMIT 1"
    )
    suspend fun query(opType: OpType, preserveId: Long, name: String, cloud: String, backupDir: String): MediaEntity?

    @Query(
        "SELECT * FROM MediaEntity WHERE" +
                " indexInfo_opType = :opType AND indexInfo_preserveId = :preserveId AND indexInfo_name = :name AND indexInfo_compressionType = :ct" +
                " AND indexInfo_cloud = :cloud AND indexInfo_backupDir = :backupDir" +
                " LIMIT 1"
    )
    suspend fun query(opType: OpType, preserveId: Long, name: String, ct: CompressionType, cloud: String, backupDir: String): MediaEntity?

    @Query("SELECT * FROM MediaEntity WHERE extraInfo_activated = 1 AND indexInfo_opType = :opType")
    suspend fun queryActivated(opType: OpType): List<MediaEntity>

    @Query("SELECT * FROM MediaEntity WHERE extraInfo_activated = 1 AND indexInfo_opType = :opType AND indexInfo_cloud = :cloud AND indexInfo_backupDir = :backupDir")
    suspend fun queryActivated(opType: OpType, cloud: String, backupDir: String): List<MediaEntity>

    @Query(
        "SELECT * FROM MediaEntity WHERE" +
                " indexInfo_opType = :opType AND extraInfo_blocked = :blocked"
    )
    fun queryFlow(opType: OpType, blocked: Boolean): Flow<List<MediaEntity>>

    @Query(
        "SELECT * FROM MediaEntity WHERE" +
                " indexInfo_opType = :opType AND indexInfo_cloud = :cloud AND indexInfo_backupDir = :backupDir"
    )
    fun queryFlow(opType: OpType, cloud: String, backupDir: String): Flow<List<MediaEntity>>

    @Query("SELECT * FROM MediaEntity WHERE indexInfo_opType = :opType AND indexInfo_preserveId = :preserveId")
    fun queryFlow(opType: OpType, preserveId: Long): Flow<List<MediaEntity>>

    @Query("SELECT * FROM MediaEntity WHERE indexInfo_name = :name AND indexInfo_opType = :opType AND indexInfo_preserveId = :preserveId LIMIT 1")
    fun queryFlow(name: String, opType: OpType, preserveId: Long): Flow<MediaEntity?>

    @Query("SELECT * FROM MediaEntity WHERE indexInfo_name = :name AND indexInfo_opType = :opType")
    fun queryFlow(name: String, opType: OpType): Flow<List<MediaEntity>>

    @Query("SELECT COUNT(*) FROM MediaEntity WHERE extraInfo_activated = 1")
    fun countActivatedFlow(): Flow<Long>

    @Query("SELECT COUNT(*) FROM MediaEntity")
    suspend fun count(): Long

    @Query("UPDATE MediaEntity SET extraInfo_activated = 0")
    suspend fun clearActivated()

    @Delete(entity = MediaEntity::class)
    suspend fun delete(item: MediaEntity)
}