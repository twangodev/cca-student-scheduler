package com.github.twangodev.solver

import com.github.twangodev.domain.Lesson
import com.github.twangodev.solver.ConstraintTypes.ROOM_CONFLICT
import com.github.twangodev.solver.ConstraintTypes.TEACHER_CONFLICT
import com.github.twangodev.solver.ConstraintTypes.TEACHER_PREFERRED
import com.github.twangodev.solver.ConstraintTypes.TEACHER_UNAVAILABLE
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore
import org.optaplanner.core.api.score.stream.Constraint
import org.optaplanner.core.api.score.stream.ConstraintFactory
import org.optaplanner.core.api.score.stream.ConstraintProvider
import org.optaplanner.core.api.score.stream.Joiners

class TimeTableConstraintProvider : ConstraintProvider {

    override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> {
        return arrayOf(
            roomConflict(constraintFactory),
            teacherConflict(constraintFactory),
            teacherPreferred(constraintFactory),
            teacherUnavailable(constraintFactory)
        )
    }

    private fun roomConflict(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachUniquePair(Lesson::class.java,
                Joiners.equal(Lesson::timeslot),
                Joiners.equal(Lesson::room)
            )
            .penalize(HardSoftScore.ONE_HARD).asConstraint(ROOM_CONFLICT)
    }

    private fun teacherConflict(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEachUniquePair(Lesson::class.java,
                Joiners.equal(Lesson::timeslot),
                Joiners.equal(Lesson::teacher)
            )
            .penalize(HardSoftScore.ONE_HARD).asConstraint(TEACHER_CONFLICT)
    }

    private fun teacherPreferred(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEach(Lesson::class.java)
            .filter { it.teacher?.preferredTimeslots?.contains(it.timeslot) ?: false }
            .reward(HardSoftScore.ONE_SOFT).asConstraint(TEACHER_PREFERRED)
    }

    private fun teacherUnavailable(constraintFactory: ConstraintFactory): Constraint {
        return constraintFactory
            .forEach(Lesson::class.java)
            .filter { it.teacher?.unavailableTimeslots?.contains(it.timeslot) ?: false }
            .penalize(HardSoftScore.ONE_HARD).asConstraint(TEACHER_UNAVAILABLE)
    }


}