package com.cyclone.solana.core.encryption

import org.bouncycastle.crypto.digests.SHA512Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter

object Utils {
    fun hmacSha512(key: ByteArray, data: ByteArray): ByteArray {
        val hmac = HMac(SHA512Digest())
        hmac.init(KeyParameter(key))
        val output = ByteArray(hmac.macSize)
        hmac.update(data, 0, data.size)
        hmac.doFinal(output, 0)
        return output
    }
}