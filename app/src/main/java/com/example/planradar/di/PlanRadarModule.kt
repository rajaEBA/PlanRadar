package com.example.planradar.di

import org.koin.core.module.Module
import org.koin.dsl.module

fun PlanRadarDependencies(): List<Module> {
    return listOf(nounDigitalModule())
}

private fun nounDigitalModule() = module {

}