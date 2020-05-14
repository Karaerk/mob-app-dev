package com.example.onwork.model

enum class DateFormatEnum(val format: String, val label: String) {
    MM_DD_YYYY_FORWARD_SLASH("MM/DD/YYYY", "MM/DD/YYYY (12/31/2000)"),
    DD_MM_YYYY_DASH("DD-MM-YYYY", "DD-MM-YYYY (31-12-2000)"),
    MM_DD_YYYY_DASH("MM-DD-YYYY", "MM-DD-YYYY (12-31-2000)"),
    YYYY_MM_DD_DASH("YYYY-MM-DD", "YYYY-MM-DD (2000-12-31)"),
    DD_MM_YYYY_FORWARD_SLASH("DD/MM/YYYY", "DD/MM/YYYY (31/12/2000)"),
    DD_MM_YYYY_DOT("DD.MM.YYYY", "DD.MM.YYYY (31.12.2000)"),
    DD_MMMM_YYYY_SPACE("dd MMMM yyyy", "dd MMMM yyyy (31 December 2000)")
}