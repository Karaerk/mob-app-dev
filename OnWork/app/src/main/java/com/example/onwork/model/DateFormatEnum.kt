package com.example.onwork.model

enum class DateFormatEnum(val format: String, val label: String) {
    MM_DD_YYYY_FORWARD_SLASH("MM/dd/yyyy", "MM/DD/YYYY (12/31/2000)"),
    DD_MM_YYYY_DASH("dd-MM-yyyy", "DD-MM-YYYY (31-12-2000)"),
    MM_DD_YYYY_DASH("MM-dd-yyyy", "MM-DD-YYYY (12-31-2000)"),
    YYYY_MM_DD_DASH("yyyy-MM-dd", "YYYY-MM-DD (2000-12-31)"),
    DD_MM_YYYY_FORWARD_SLASH("dd/MM/yyyy", "DD/MM/YYYY (31/12/2000)"),
    DD_MM_YYYY_DOT("dd.MM.yyyy", "DD.MM.YYYY (31.12.2000)"),
    DD_MMMM_YYYY_SPACE("dd MMMM yyyy", "dd MMMM yyyy (31 December 2000)")
}