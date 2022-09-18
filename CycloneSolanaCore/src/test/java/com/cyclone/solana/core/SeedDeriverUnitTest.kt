package com.cyclone.solana.core

import com.cyclone.solana.core.extensions.hexToByteArray
import com.cyclone.solana.core.extensions.toEd25519PublicKeyParameters
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.SeedDeriver
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class SeedDeriverUnitTest {
    @Test
    fun `derived_key_pairs_are_correct`() {
        val expected = listOf(
            "GSExUw6i46nxjHRA6zH1eNfKqxm5UCBCeRFXeiZdBR2n",
            "CZpat4LpHiBLCRohhLehncLY5wAQMM3K6NjmUgBx8u6h",
            "A2B3HgyZ4bf53uSxQihhzW5XAYNJh9bT4zfDNtycfcXu",
            "BqiL9J3938vAgGYfbKafWGSe5j1F9Kf3YZuKnPwcq3RJ",
            "8HDyYN8B9Wq9ZUG5wYvYP67pozqGCVtNYDH1Z8Nu7ZrH",
            "8ZKY7kQxuiKZDx7zT2qDs3D9NSCjdL81TMBxKSi3Bemn",
            "EWK4inYmvXfRTAaocyZoX9ieiKPY4K1EgDALaTqsShHX",
            "HadaGuqCEGq6ZgzkUkXGko8MG8UU4G1WHyKSSiv2QMSJ",
            "A9aP8i8uvDS38NjP1qVXqAjNhrkTsJVrESDUofZmXxx",
            "AitbP2ytkdSYGceCQwvpL2Jh8MWzMrsypogEMrX9UiqH",
            "EWwwH45c9auAwPv4mXZta2k7kpmu5PUCZfvPEiLoFLmH",
            "7QdLSxTwWCcnR71x1WY6pNYVRdX6T6qr88RZ8zmqUco5",
            "EgAC3HHVbZEgSGbSet9xTMaNYTQTM1i23Pgr5ykJp5BT",
            "9zUCkEhydSbX4Chxfpx8BuqpPCy6N9BBM4eujkFL1ubz",
            "i7xeT5MwACRExDE8bHARofoZxp1d5AqeRsokDcKavCV",
            "HFq25Vxx2B63MbW38ZAKPX1fSJMCFdjaY7fUSgDzrCKM",
            "A1VTu7qV93g1HYpAY8UqUcUq5MGsoKUNZMPp8Bz56cs5",
            "5Voh4CgMGkGvVKRte1pMfH8hBwhya6HffAr8NAt8E4wn",
            "F6i1SP1pEBpT3i1frDngjPH9wfnWyHwfEhFiYtiaaxji",
            "22qxKTApKs8k5A8JudLpRXMpWLLTLaF44spNret5y1L7",
            "FVS4VNenX2NuL1pfgAz16aXaZ4MEmQKk81ZAxT4nEekp",
            "59JNbA9Zz2thhGzSzEC5pde18PnXZzYwc3S6ge8KGAzR",
            "AEzLWr5BQSHqod8X5r5t6631ZGeWCzMTowiFpxoHvA5i",
            "AU9jVXwXCG1kAo6begTSNcgU3tKGKPWUjBsyvpiLddNt",
            "5XX1WNw9fGKysygP7nb2iCb8HZZAoKyTy3LiHbtLGH1c",
            "6p8yGPg1t9DgK2WvLQ1zUazwAyzGQWVVzeCvvGDSPe7c",
            "7ctzEAot3QvNbGu5RoFyQLqkUhniNjiiQgmh1Gw7io4T",
            "A7zNT7sNP7vDnKgtyj162BzsspXyyp6ybWsBQNsD3rc",
            "EAS4g6pbuGxJuahZQ6d3anEqaYbbs94rGKKvTY9Rpm43",
            "J6jtgvURFRsoZ8ZC7aeR2nWWLti59PX26xJh4ZNeQ4G1",
            "FQDKJkh5XBD8EVLyDmUCbdyweQk4aUn69BZrKxaeN4sd",
            "H8AzPBuBhqL2As3BKJ91b6hAn8rscgwz8h5vkGd1aQFU",
            "2VA4Mx1iiaHuYyEWEZWAQc9cnf1ZbGvBcomRYyASe5Pj",
            "H2wuxnDgimD3DWFkakPtoRqGjk7pwMhe8bxZ8PkaVXEs",
            "CupHfezJj3rzG9SZohwRq4i2xa4rMB7B6B1rXgdXFDZt",
            "DSAi12uqWC8TzHf2Mbctom2TunqASPjXUoZxtXh2XaSA",
            "FN688Z45gt38znZ4gqNztYoopPVvwNPE64UFgL6RBMLX",
            "dfiSnuP4Aqn2QUvXLkyT7Ka15RAoZdGoLHAE675t2ic",
            "GCZBdQ4KkRcdk6jYLYvogdcPLFAMrSnGbKiAYgJQhZ4M",
            "9FURVbbgdaDVrtgmxy2n7yHDimYT1XpkS4TuYa9prgf9",
            "3A7LD7PHcyfK9SATrzELn69FvoUmvb7jyqrRzDUfKFTu",
            "2UczBU5DV4vGxy8BU8ayg9ZS5JQsRos8itNvViLxgVQg",
            "BSnHSJwq6pqhGc7Q6JtcfcFLTNgRVKx7YWhBrj8MVXGM",
            "AKsNA87ecLSyWcxmrNLVzGmKiqzUfYHi9MQmLTWcB5DC",
            "4oeEgkSGAViyQdXDVECGXeqntcWkSEkT5H2tfRScjEyQ",
            "hJwwowYmVMuq59kNY6yLP3xSij6dbhcJG1Qfu4pbmbs",
            "ECrpGTFpzdaVF2XAAdnytdYxwBUb9dZenv2NKMLmtemP",
            "FPtdCYxz5zarkjtoQzdUF3VSnymhi3xMhUW1P9ukrccN",
            "5ywmPkd9NuS3v94ncUrVAS1yutdNNt6296GbPTVxh97u",
            "9cWYpL8awjUA6QfUzSNLJe5dCojSkxJamGr66564svqv",
            "Fsp6Db593Ey7e5XVBNAHRyMj4Xam8YGcsrk3cmbf3YWq",
            "67ynwHZ7AKU4nqSUS958WNywRVJmMskvLSSVU9L1dqxS",
            "6Mt59vaQQKL2CaffpYyewqKz6CEeckmTnirQPPEKxJRB",
            "AZ56ZmcB2XatvqRNpqLBKzv5kt6dEfT1ThaWHQ57oqPk",
            "G5eQqi8dP55HurURRNV7ij51cxhKSHMuPgVqVfKkmhLW",
            "5hUuu9JzphbgrvpL3cVcBh8LpeYBC2NNm5DmBBrgVzoE",
            "CEF1szXmncDUJg7Zz4P4H9G8qMWpqzLhgbxhZPshV9Lf",
            "ARFxPTqK2z36XobugaA3QVdrsaPED7tUw1s417oaUz1t",
            "AshGXYocisUS7nG8M9JGW81WSwCcV8CWVqRb6PCpoYYu",
            "EUWHq8RQvzE44EyjkZR2W9CQxHyXB9Fa864okdN8F2R4",
            "vx2txXc5SY9SKJbrk8Fga4pzk1U5wERih6UGDXYb6GC",
            "4s6bYJT5soUKKWyA95CNaqgPDvZoVrrenXwC3Qi8RaPE",
            "EViphtcjPLFwW1MwayUdkPWLYKSxnkHsQXohjzG38DT8",
            "6UcfwQPm1QmcHDd4jneMraRfB1X7DS46uYXUxiZ3xh9n",
        )

        val hex = "38823dd6"
        val byteArray = hex.hexToByteArray()

        val list = SeedDeriver.invoke(byteArray).map {
            Base58Encoder.invoke(it.public.toEd25519PublicKeyParameters.encoded)
        }

        assertArrayEquals(
            expected.toTypedArray(),
            list.toTypedArray()
        )
    }
}