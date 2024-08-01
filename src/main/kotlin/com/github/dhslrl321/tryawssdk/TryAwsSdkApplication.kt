package com.github.dhslrl321.tryawssdk

import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class TryAwsSdkApplication

fun main(args: Array<String>) {
  runApplication<TryAwsSdkApplication>(*args)
}
