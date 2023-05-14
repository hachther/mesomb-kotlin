<h1 align="center">Welcome to java-mesomb 👋</h1>
<p>
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.1-blue.svg?cacheSeconds=2592000" />
  <a href="https://mesomb.hachther.com/en/api/v1.1/schema/" target="_blank">
    <img alt="Documentation" src="https://img.shields.io/badge/documentation-yes-brightgreen.svg" />
  </a>
  <a href="#" target="_blank">
    <img alt="License: MIT" src="https://img.shields.io/badge/License-MIT-yellow.svg" />
  </a>
  <a href="https://twitter.com/hachther" target="_blank">
    <img alt="Twitter: hachther" src="https://img.shields.io/twitter/follow/hachther.svg?style=social" />
  </a>
</p>

> JAVA client for MeSomb services.
>
> You can check the full [documentation of the api here](https://mesomb.hachther.com/en/api/v1.1/schema/)

### 🏠 [Homepage](https://mesomb.com)

## Installation

### Gradle

#### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```Gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

#### Step 2. Add the dependency

```Gradle
dependencies {
    implementation 'com.github.hachther:mesomb-kotlin:TAG'
}
```

### Maven

#### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```XML
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

#### Step 2. Add the dependency

```XML
<dependency>
    <groupId>com.github.hachther</groupId>
    <artifactId>mesomb-kotlin</artifactId>
    <version>Tag</version>
</dependency>
```

## Usage

### Collect money from an account

```kotlin
package com.hachther.mesomb.operations

import com.hachther.mesomb.util.RandomGenerator
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.models.TransactionResponse
import com.hachther.mesomb.operations.PaymentOperation

class Test {
    fun main() {
        val payment = PaymentOperation(this.applicationKey, this.accessKey, this.secretKey)
        try {
            val response = payment.makeCollect(100, "MTN", "677550203", Date(), RandomGenerator.nonce())
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        }
    }
}
```

### Depose money in an account

```kotlin
package com.hachther.mesomb.operations

import com.hachther.mesomb.util.RandomGenerator
import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.models.TransactionResponse
import com.hachther.mesomb.operations.PaymentOperation

class Test {
    fun main() {
        val payment = PaymentOperation(this.applicationKey, this.accessKey, this.secretKey)
        try {
            val response = payment.makeDeposit(100, "MTN", "677550203", Date(), RandomGenerator.nonce())
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        }
    }
}
```

### Get application status

```kotlin
package com.hachther.mesomb.operations

import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.operations.PaymentOperation

class Test {
    fun main() {
        val payment = PaymentOperation(this.applicationKey, this.accessKey, this.secretKey)
        try {
            val response = payment.getStatus()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        }
    }
}
```

### Get transactions by IDs

```kotlin
package com.hachther.mesomb.operations

import com.hachther.mesomb.exceptions.InvalidClientRequestException
import com.hachther.mesomb.exceptions.PermissionDeniedException
import com.hachther.mesomb.exceptions.ServerException
import com.hachther.mesomb.exceptions.ServiceNotFoundException
import com.hachther.mesomb.models.Application
import com.hachther.mesomb.operations.PaymentOperation

class Test {
    fun main() {
        val payment = PaymentOperation(this.applicationKey, this.accessKey, this.secretKey)
        try {
            payment.getTransactions(arrayOf("ID1", "ID2"))
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException(e)
        } catch (e: ServerException) {
            throw RuntimeException(e)
        } catch (e: ServiceNotFoundException) {
            throw RuntimeException(e)
        } catch (e: PermissionDeniedException) {
            throw RuntimeException(e)
        } catch (e: InvalidClientRequestException) {
            throw RuntimeException(e)
        }
    }
}
```


## Author

👤 **Hachther LLC <contact@hachther.com>**

* Website: https://www.hachther.com
* Twitter: [@hachther](https://twitter.com/hachther)
* Github: [@hachther](https://github.com/hachther)
* LinkedIn: [@hachther](https://linkedin.com/in/hachther)

## Show your support

Give a ⭐️ if this project helped you!
