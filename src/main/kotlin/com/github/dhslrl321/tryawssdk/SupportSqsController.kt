package com.github.dhslrl321.tryawssdk

import com.github.dhslrl321.tryawssdk.MemberQueues.MEMBER_DLQ_ARN
import com.github.dhslrl321.tryawssdk.MemberQueues.MEMBER_DLQ_NAME
import com.github.dhslrl321.tryawssdk.MemberQueues.MEMBER_QUEUE_ARN
import com.github.dhslrl321.tryawssdk.MemberQueues.MEMBER_QUEUE_NAME
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.web.bind.annotation.*
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.*


@RestController
class SupportSqsController(
  private val sqsTemplate: SqsTemplate,
  private val sqsClient: SqsAsyncClient,
) {
  @PostMapping("/messages")
  fun sendMessage(@RequestBody body: Map<String, String>) {
    val content = body["content"]!!
    sqsTemplate.send {
      it.queue(MEMBER_QUEUE_NAME)
        .payload(content)
    }
  }

  @GetMapping("/re-drive/messages")
  fun reDriveAll() {
    val request = StartMessageMoveTaskRequest.builder()
      .sourceArn(MEMBER_DLQ_ARN)
      .destinationArn(MEMBER_QUEUE_ARN)
      .build()

    sqsClient.startMessageMoveTask(request)
  }

  
  @GetMapping("/re-drive/messages/{id}")
  fun reDriveBy(@PathVariable id: String) {
    repeat(10) {
      val messages = receiveMessages()
      // 디버깅 로그 추가
      if (messages.isEmpty()) {
        return@repeat
      }
      val reDriveTarget = messages.firstOrNull { it.messageId() == id }
      reDriveTarget?.let {
        reDriveMessage(id, it)
        deleteMessage(it, id)
        return
      }
    }
  }

  private fun receiveMessages(): MutableList<Message> {
    val request = ReceiveMessageRequest.builder()
      .queueUrl(MEMBER_DLQ_NAME)
      .maxNumberOfMessages(2)
      .waitTimeSeconds(10)
      .maxNumberOfMessages(10)
      .visibilityTimeout(3)
      .build()
    val messageFuture = sqsClient.receiveMessage(request)

    val messages = messageFuture.get().messages()
    println("Received messages: ${messages.size}")
    return messages
  }

  private fun reDriveMessage(id: String, it: Message) {
    println("$id 에 해당하는 message 존재하여 origin queue 로 redrive")
    sqsClient.sendMessage(SendMessageRequest.builder()
      .queueUrl(MEMBER_QUEUE_NAME)
      .messageBody(it.body())
      .build())
  }

  private fun deleteMessage(it: Message, id: String) {
    val deleteMessageRequest = DeleteMessageRequest
      .builder()
      .queueUrl(MEMBER_DLQ_NAME)
      .receiptHandle(it.receiptHandle())
      .build()
    sqsClient.deleteMessage(deleteMessageRequest)
    println("DLQ 에서 message($id, ${it.receiptHandle()}) 제거됨")
  }
}
