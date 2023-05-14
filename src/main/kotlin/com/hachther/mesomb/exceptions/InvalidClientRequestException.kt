package com.hachther.mesomb.exceptions


class InvalidClientRequestException(message: String?, val code: String) : Exception(message)