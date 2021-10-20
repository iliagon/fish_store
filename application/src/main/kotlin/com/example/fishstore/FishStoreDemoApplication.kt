package com.example.fishstore

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class FishStoreDemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(FishStoreDemoApplication::class.java, *args)
}