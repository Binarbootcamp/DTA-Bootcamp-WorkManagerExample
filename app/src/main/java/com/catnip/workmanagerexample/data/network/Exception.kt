package com.catnip.workmanagerexample.data.network

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/

class HttpException(val code: Int, httpMessage: String) : Exception(httpMessage)