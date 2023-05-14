package com.hachther.mesomb.exceptions


class ServerException(message: String?, val code: String?) : Exception(message)