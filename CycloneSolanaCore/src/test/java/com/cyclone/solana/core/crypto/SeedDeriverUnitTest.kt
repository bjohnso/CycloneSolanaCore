package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.datamodel.dto.derivation.DerivationPath
import com.cyclone.solana.core.encryption.ExtendedSecretKey
import com.cyclone.solana.core.encryption.derivation.Derivation
import com.cyclone.solana.core.extensions.*
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.MnemonicEncoder
import com.cyclone.solana.core.usecase.SeedDeriver
import org.junit.Assert
import org.junit.Test

class SeedDeriverUnitTest {
    @Test
    fun `phrase to seed is correct`() {
        val phraseHex = "2e5a2fb1ecf80b8995b82a2801dc6d03"

        val phrase = MnemonicEncoder.invoke(
            phraseHex.hexToByteArray()
        )

        Assert.assertNotNull(phrase != null)

        val seed = Derivation.generateDerivationSeed(phrase!!)

        Assert.assertEquals(
            "5f0a5d2213ae6ca550494c9bb96e6711fe9dd722bb79293e3c700c309aaa11285176cedad042b80dc1db570c46e7a08bc5b378308349ae1aeb444cb610eec2fa",
            seed.toBinaryString().binaryToHex()
        )
    }

    @Test
    fun `seed to master extended secret key is correct`() {
        val seed = "5f0a5d2213ae6ca550494c9bb96e6711fe9dd722bb79293e3c700c309aaa11285176cedad042b80dc1db570c46e7a08bc5b378308349ae1aeb444cb610eec2fa".hexToByteArray()

        val masterExtendedSecretKey = Derivation.generateMasterExtendSecretKey(seed)

        Assert.assertEquals(
            "448d941059eaa2e6ea0cba639b1ae64df42757fa47777879280f0bcf24bda383",
            masterExtendedSecretKey.secretKey.toBinaryString().binaryToHex()
        )

        Assert.assertEquals(
            "1d4c20be1b0e62c2c279675f79969b6a1960d6cacd4c09a3b0b6e993124c424d",
            masterExtendedSecretKey.chainCode.toBinaryString().binaryToHex()
        )
    }

    @Test
    fun `master key to keypair is correct`() {
        val expectedPublicKeys = listOf(
            "A4j6JBMVTsB2MNNGhtym1fwM7c8TeNfn8MA9hDHvWKoB"
        )

        val masterExtendedSecretKey = ExtendedSecretKey(
            secretKey = "448d941059eaa2e6ea0cba639b1ae64df42757fa47777879280f0bcf24bda383".hexToByteArray(),
            chainCode = "1d4c20be1b0e62c2c279675f79969b6a1960d6cacd4c09a3b0b6e993124c424d".hexToByteArray(),
        )

        val path = DerivationPath.fromPath("0'")

        val derivedKey = masterExtendedSecretKey.derive(
            path.toList()
        ).getOrNull()

        Assert.assertNotNull(derivedKey!!)

        val resultPublicKeys = listOf(
            Base58Encoder.invoke(
                derivedKey.keyPair().publicKey
            )
        )

        Assert.assertArrayEquals(
            expectedPublicKeys.toTypedArray(),
            resultPublicKeys.toTypedArray()
        )
    }

    @Test
    fun `derived key pairs are correct`() {
        val phraseHex = "2e5a2fb1ecf80b8995b82a2801dc6d03"

        val phrase = MnemonicEncoder.invoke(
            phraseHex.hexToByteArray()
        )

        Assert.assertNotNull(phrase != null)

        val list = SeedDeriver.invoke(
            phrase!!
        ).map { Base58Encoder.invoke(it.publicKey) }

        Assert.assertArrayEquals(
            arrayOf(
                "A4j6JBMVTsB2MNNGhtym1fwM7c8TeNfn8MA9hDHvWKoB",
                "FmwqXbA9WUQhPgGgZF1ecaSiCa1ih67bJ2e3arFr9hF5",
                "Dzn18rfMKFsxEwdPAgCVLExR9VFGoLXR4FVtAtSWUofy",
                "DwZtLroEoG5J1WweQC5CN5sujmeFJP2keWNauZ5u8ush",
                "GUbq8RSdvjrNhXTEKCc4fAR5EAm8f8fKSZHqniBC3VT2",
                "2yR2RmRGu4MKSKeyX93EHF7PS8BTjnKPQsh23AYTCKfG",
                "787zyG4zbi5STuDzSALXcxNX2VvAhWnUN8TEh8WpVDEk",
                "Dpe7usUnyjXEfsV9FAa8ZNbDyjFanxc9JQBWRWn9p8No",
                "EGmaNPNzZfuaPzkPctUWZZwDuuxv1euaCmvMakWq56bJ",
                "Dr26eX9Sb6v1yi82NLL7uzH7K5CxmK9ZoosfDdvjrEhT",
                "46Xj9FmZ3zkaJZNnED2jAXPgHoWaZKvtM3SCfz1TaA1Z",
                "3MXb3UAiPbAyGAMZK9LStXUXz9N2GBAEM8ANGjbXFMSB",
                "JPXH3g5cZt5A9gJmxWPcYVMKWf3DPRXWko6vXn8VAeh",
                "7wYyGT4h3WVYqNPaHve6YhGCKqxqR9cmqj2Yj4NbUzbJ",
                "DDL84CkNuDZikWX3M5Gbr6NnpvXo5J6Q47YJr7VKeUZ9",
                "E5neZ4cKHmEEmYHwbrhMPsLCi9PbZwUFoQUFKgFaevPg",
                "4ycxr7epLycDyrEVhFwxaRqXdBJNSe6wHypNbhhRro6A",
                "6LSwgtF2M6rUSMEcttFNiZtWCNApYj51QUSwQpR6z53E",
                "4dP6zewpsfxiUWAdnJquesus3rcx8hY5zoCk9J5Bz2L6",
                "9EXDQJn7TM6KXKjkNc1hir4NBEympXKZYY7EmzomT4Qr",
                "EntSB5U8wcvWzugWXMFViBcbMWSQHFBqtCrvxtYhWmpL",
                "5xEnEAjJqZqeCuBfngBK5f76HwHypawDcCZpDn78saq7",
                "29a34EAmkBR63Zcg6YLz1wZe2a8jE2BvXCeA7DGJRCVg",
                "7gzJMvydCmSXDjLvT8YUt2hkDbsQjZC9VbAdPwNZiPZ2",
                "7FA7SJ2EzKs5GNu1DxdNKdeZ1rERCmafjqc3mD18eBqr",
                "8mRmupGHF5Aa3KuEL3eRVkWAyu3sXdyZZJamnzo3xfHt",
                "GYyuwfK5BgJv7NSkxh4Rm2XM7RcvJe8humP6S4joEZes",
                "Ewfqi28MTRb4MJiGz9YxekQ7Adb9hniuZTUjGcYiLqp1",
                "gx8oEdZh52chk8A8aFC57gEVhKm2H4SuZfTUaReiC1R",
                "7PDUBWkXH3EqsAS9VPdk4UR2VK3a19y38DJLYQw6aqu1",
                "4RXcTMFJxCQfDeAqyNKTfiVMnSkNC5RgsMdJAa1FWSNG",
                "HuuGy4A1guZdi5uMNLCMKn1b6mFcDrgBpkxLExpjf524",
                "C6YxKeYPMoMuvkisKzMaCZ4kX79LkxumXUxuMPfqJBDw",
                "6is5gsfWnFdBqSXsQVV3Rk7nhWh2PFNVi6nkWbNtRbJe",
                "A4zxvLqtHMoH6ULLCRhB2r9JutuhZKQeCH2k3pYSuqNH",
                "43NMhyzzWVen9t9zvKbV55Msht8PMfiR7Dgg4pSNTweX",
                "GFmSF93Lz6Z417EEBT46d1ppyqrYd3rLzg8auHjiCKbe",
                "2MU3a8GuMpHhFVPgHGDGU4y852SKEbBSb7B5BQdgmMfX",
                "9jbnm7DPCnhhLt3zxnRBvkQTV5EATgv9pduAH1ozwFDw",
                "3ufnynvayZH3rLxMT4DxcVw7iHGS5azzjvAsGH5rrbmt",
                "YbyrFprWpVyEkU6kdxUNJSGieJLm99PEzjoWStUFnm1",
                "5C37W354yFrsWgPkfdogud4G5ukwG1cm3geofAuHitS4",
                "8YqhVxGFxGKxYwFf9xNo9dZfurfeZLRdSxmssQx3brua",
                "AV8QkM32bMx8Zqen5dMUymVyWmeqxGBaYwhu2mzPyikh",
                "6KHeH8Eok6SeRWqv3pTia2TZbLKBVbrU8xKQ3JymaUF",
                "3vW1nbcr1dzSiEbMK1jNMoZazo1dZYqt81q55i8e7si1",
                "GsBPRp1eXo2HAhWcZn1v4TWJqg3MXPMZbqCNwaGPfCec",
                "BSsBnPCzmPd93dTdFr21Dqnc35WjvvtSfMpynfsUenjS",
                "3WGgtAgSQRxyUYAmBvL7cZ79F4ZnJXB65ph3xvubSzWH",
                "7eB9p6W47PzKKZhWi6htqQ29rJZsJBYg3YoFtgYwZKyZ",
                "2T4Ujyg96TLCynKPhdUSvLifkxPijzbM8E4hMCWVbemG",
                "FiBMNmGJ5Hp7QoaTaBZuYLogQQBN9e552TJXksvQHuCK",
                "6uUgGngaE3MzjWW3HFpk34jMbiFrg948cDyiW3qZVXYP",
                "B53jwGwPFuBt8qeqmrMtdCvoEMExdbzwFBthhFiboZDU",
                "5dXuMMKscPZnhEhw9mcyW1WXsjzwBeKBtSETcruPqszf",
                "7mvtXdkfi7cyAft525NnR2vaYnMgQVt5xz5noyQ3eSWR",
                "HrvPCk6sZuB7xkZborkdeJD6EP6nxEwVomzZrxiuVDBR",
                "95rToeA9QouyRJqgnGz6XKYhPskRtnN5YnySn1yaG5Hj",
                "6pnZktnHmaZG3f3VoqhH4aXsPnZ85a5pbvCY4LwSYTJk",
                "CVsxLzUVq5d1dvv7Ut2wdUTsviNapsQfBjLScNHKCbn9",
                "8MCHQUWoWRT8HSfofJMf6j2VVgduquu13BcKw8accYRg",
                "EUUFXoLZ8kh2AjepEsTe4aCwhCJjcjH1tQVDfgWzeefb",
                "A6NuzfbbMWyrvXFtCox3Z5FFPkRLk23Td5jfdbHDbfRo",
                "BhJou2uT3DkkNKtAxU9eqXrcGsdUZVSpPQdyJgnctPmJ"
            ),
            list.toTypedArray()
        )
    }
}