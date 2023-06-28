package com.example.weatherappkotlin.domain.commands

interface Command <out T>{
    fun execute(): T
}