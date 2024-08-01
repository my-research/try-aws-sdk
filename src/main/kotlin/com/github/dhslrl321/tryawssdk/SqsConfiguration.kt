package com.github.dhslrl321.tryawssdk

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.listener.SqsContainerOptionsBuilder
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode.MANUAL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.sqs.SqsAsyncClient


@Configuration
class SqsConfiguration {
  @Bean
  fun defaultSqsListenerContainerFactory(sqsAsyncClient: SqsAsyncClient) =
    SqsMessageListenerContainerFactory
      .builder<Any>()
      .configure { options: SqsContainerOptionsBuilder ->
        options
          .acknowledgementMode(MANUAL)
      }
      .sqsAsyncClient(sqsAsyncClient)
      .build()
}
