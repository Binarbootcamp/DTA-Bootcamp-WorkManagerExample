package com.catnip.workmanagerexample.data.model

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/

data class Event(
    val eventName: String,
    val metadata: HashMap<String, String>,
    val timestamp: Long = System.currentTimeMillis()
)
