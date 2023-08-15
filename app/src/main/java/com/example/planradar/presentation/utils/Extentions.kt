package com.example.planradar.presentation.utils

import java.util.HashSet

fun <T> List<T>.removeDuplicates(): List<T> {
    val set = HashSet<T>()
    val newList = mutableListOf<T>()
    for (item in this) {
        if (!set.contains(item)) {
            set.add(item)
            newList.add(item)
        }
    }
    return newList
}

fun Double.kelvinToCelsius(): Int {
    return (this - 273.15).toInt()
}