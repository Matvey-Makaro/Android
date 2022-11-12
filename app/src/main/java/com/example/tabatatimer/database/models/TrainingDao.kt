package com.example.tabatatimer.database.models

import androidx.room.*

@Dao
interface TrainingDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addInterval(interval: Interval)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTraining(training: Training)

    @Update
    fun updateInterval(interval: Interval)

    @Update
    fun updateTraining(training: Training)

    @Delete
    fun deleteInterval(interval: Interval)

    @Delete
    fun deleteTraining(training: Training)

    @Query("SELECT * FROM interval")
    fun getAllIntervals()

    @Query("SELECT * FROM training")
    fun getAllTrainings() : List<Training>

    @Query("DELETE FROM interval")
    fun deleteAllIntervals()

    @Query("DELETE FROM training")
    fun deleteAllTrainings()

    @Query("SELECT MAX(interval_id) FROM interval")
    fun getMaxIntervalId() : Int

    @Query("SELECT MAX(training_id) FROM training")
    fun getMaxTrainingId() : Int

    @Query("SELECT COUNT(*) FROM training")
    fun getTrainingsNum() : Int

    @Query("SELECT * FROM interval WHERE :training_id = trainingId ORDER BY 'order'")
    fun getTrainingsIntervals(training_id: Int): MutableList<Interval>

    fun getJoined() : MutableList<TrainingWithIntervals>
    {
        val trainings = getAllTrainings()
        val list : MutableList<TrainingWithIntervals> = mutableListOf()

        for(t in trainings)
        {
            list.add(TrainingWithIntervals(t.training_id, t.color, t.repeats, t.Name, t.soundEffect, getTrainingsIntervals(t.training_id)))

        }

        return list
    }

    fun insert(item: TrainingWithIntervals)
    {

    }
}