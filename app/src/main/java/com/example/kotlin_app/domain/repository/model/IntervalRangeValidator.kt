package com.example.kotlin_app.domain.repository.model

object IntervalRangeValidator {

    private val validIntervalForRange: Map<Range, List<Interval>> = mapOf(
        Range.ONE_DAY to listOf(Interval.ONE_MIN, Interval.TWO_MIN, Interval.FIVE_MIN, Interval.FIFTEEN_MIN, Interval.SIXTY_MIN),
        Range.FIVE_DAYS to listOf(Interval.FIVE_MIN, Interval.FIFTEEN_MIN, Interval.SIXTY_MIN),
        Range.ONE_MONTH to listOf(Interval.SIXTY_MIN, Interval.ONE_DAY),
        Range.THREE_MONTHS to listOf(Interval.ONE_DAY),
        Range.SIX_MONTHS to listOf(Interval.ONE_DAY),
        Range.ONE_YEAR to listOf(Interval.ONE_DAY),
        Range.FIVE_YEARS to listOf(Interval.ONE_DAY),
        Range.TEN_YEARS to listOf(Interval.ONE_DAY),
        Range.MAX to listOf(Interval.ONE_DAY)
    )

    fun getValidIntervalsFor(range: Range): List<Interval> {
        return validIntervalForRange[range].orEmpty()
    }
}