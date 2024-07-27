package com.github.dhslrl321.tryawssdk

import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class MemberEventConsumer {
    @SqsListener
    fun a() {

    }
}