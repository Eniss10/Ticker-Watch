package com.example.kotlin_app.domain.repository.model

enum class Interval(val value: String) {
    ONE_MIN("1m"),
    TWO_MIN("2m"),
    FIVE_MIN("5m"),
    FIFTEEN_MIN("15m"),
    THIRTY_MIN("30m"),
    SIXTY_MIN("60m"),
    ONE_DAY("1d"),
}

enum class Range(val value: String) {
    ONE_DAY("1d"),
    FIVE_DAYS("5d"),
    ONE_MONTH("1mo"),
    THREE_MONTHS("3mo"),
    SIX_MONTHS("6mo"),
    ONE_YEAR("1y"),
    FIVE_YEARS("5y"),
    TEN_YEARS("10y"),
    MAX("max")
}