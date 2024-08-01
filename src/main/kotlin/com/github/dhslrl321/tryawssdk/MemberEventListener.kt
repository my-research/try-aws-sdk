package com.github.dhslrl321.tryawssdk

import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class MemberEventListener {

  @SqsListener("member-event")
  fun listen(
    @Header(value = "id") messageId: String,
    message: String
  ) {

    println("received message(id:$messageId,$message)")
  }

//  @SqsListener("member-event-dlq")
  fun listenDLQ(
    @Header(value = "id") messageId: String,
    message: String,
    ack: Acknowledgement
  ) {

    println("DLQ received message(id:$messageId,$message)")
    ack.acknowledge()
  }
}
