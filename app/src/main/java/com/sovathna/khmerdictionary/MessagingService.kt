package com.sovathna.khmerdictionary

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sovathna.androidmvi.Logger

class MessagingService : FirebaseMessagingService() {

  override fun onMessageReceived(remote: RemoteMessage) {
    Logger.d("Message: ${remote.data}")
  }


}