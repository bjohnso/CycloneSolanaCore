package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.extensions.*
import com.cyclone.solana.core.usecase.Base58Encoder
import com.cyclone.solana.core.usecase.SeedDeriver
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class SeedDeriverUnitTest {
    @Test
    fun derived_key_pairs_are_correct() {
        val hexSeed = "b35cd271ce77c440d5aac7fd591403e3"
        
        val expectedPublicKeys = listOf(
            "AbG3kXTcPA2efou2oK8D7h2KaSxL6JuTR5Rdxry7wMRd",
            "DMeaHFyFiJMP7VMsyWBkBfvXVHF6S4sPo6nSxa8vp4Zp",
            "5hsufXko61XrTyqZrUq8e9Fm1MyBGAuQQvzJbqzBBw8J",
            "AAfttmZQXr5eoG6H5RPrKYLoxGWD2xPps7UxUyWJsRxQ",
            "HE6Y77Zw3pCrV7mz3rJ5M94L2R5EfCca5Xp1t1NbhS8",
            "BmajEwG5MBT9o2VSD6jwm3vFvsqTrPYTZu2dEgM2LTCg",
            "GqynQ2pVanrFFChL3CwJqQE1WUZRemBuE9gAGcn41ULi",
            "A1ksfZgBeoHq335pL4YZX4Yh5dQysZV3mPogm8edQqeW",
            "EnELEjztSpTwSkdcD3ytAswttrbZVSnVu49LnvMfGGdz",
            "7LC5U9pY6UKMx5aTDc5c4RU7jmy9HPdwJk7rm6FYQ5x5",
            "F697fbPS3qN1D41yYZoH5ntJihcq1srRPfrn5oqwVU3v",
            "39ap7gnD5cfgyt6i9oPwP97drXqjACQ9QfpYYuAqz5Ge",
            "8i2tWVhUWf5QA33YB967AvxsPo5RopHg9QkvyFHyK5HS",
            "4PEJ522LvPDcYJNs1FiQwhszSP5NkFwZ3BqoxkYH7tcC",
            "AJZQDTiSRPTeyDV6PPGSbfoQqx6bvp8GJFrG97XYRBq6",
            "3odWCkXvBjm9hFW3bB2bgwhk7PUgDK8gcaDfuqZCPisr",
            "FPKsjo2sZDU3xSYBMopGQd3KQhn7rYbrBHoBwd16FLDx",
            "FEcrkLcyAwtartzjsPqV5Nvd3rFNNC3gEVUxLyzMTcMN",
            "ATSSZvgVuKRfU9ywW6PmzQSpko5XP7LinYHb7Qw7UBT2",
            "3cnq3w8Pck2WzGJknBp4fzPwNVLJAyBWADyzZXS9RoJa",
            "4H7oJUMp3UNvrdDURxfzbqPRkryp267X1ChNchLW2FxA",
            "97Vpgdhh8Nj9m9Uw452AVctUTDmHgzot362z6hSAjvGy",
            "2HdixLRnxD31GWxZXodivkaBjnTcha4o5yXHJAFbUHQv",
            "B5T2BL2EiQ5RKjrqBuMwn3WWVtyynVcNvYupfF2quxmo",
            "5PFNfF3B1cBcXzYPTmZDH4A2s8fHa1Z4uDxCEB8ZG6pv",
            "BX5yyqzQqfWNRPVPgccWxmP196M2UBP1MuXFrUGufPJE",
            "CStjaQF81M8Ko8uJbN5zwoqVSGDBsv4enmYYqLtxk2Bg",
            "96YUSamDwhF6SpVUQqYFSoZPsfpMqva58RqUQKL2E5eM",
            "ABhE7UfTGz9hpNDXP8wCFMtAvQvYdamfEDoP464WX6YB",
            "87rnUPcJbbwDK9CCUD6kxpKPJqLH48PYvfcMehk9oUnD",
            "CLf1PDywcxFhnmwccNsjp3UtLiUbETvd4stYYRWj9gGy",
            "3eYjqA1UdkRHSFsc6xFWha5rnzhm8srirUXH5oVZdsXP",
            "FaVyCUoVEXo9VLCpZETscxsmkc43DG1DJzaQdqT4W86r",
            "AS824GnpEa7jE4LbwdeaTD8swSUREbk8HpwmdJk3Pse",
            "5srksgNPJyrkRV3NhuCHTZPZzPuYGyjUkzXSpKbph2uG",
            "HJq6o46m2g2ndjusHpvBRqUXyFafUY6vuTK5z5yhBffn",
            "3t51wQZCnDvjrDt7a9WFt9WoeVxYGPwUzJM9K5urX9iW",
            "7hbmdzXsNuc6nrZMrhFxswRURh4sBCpa5QipbwVGxLPA",
            "FNfWkgK4wAogHF9tUn2mDcbSxEqtTsAVzvyf6CT7MT4o",
            "12qE1MqVhuQdMym1JGjHjjUw8qGy6a4QFeAPhkfG6Nnk",
            "GMfJVsKJCFgNpq3BBztbwwxYH2oFQP6MTLbmVARTrnE1",
            "83XvhMNkLMaZPxd85c81KHZsjJCB85GruAB6ZVCFb3iF",
            "3keWLMG46HaA9MgDRAjNFzxDzJ7qZsXvsKRBygBMGX8g",
            "2YCd13wmp6EHNaphDjNq64qqbdXYtSqaUm3gK1bPxung",
            "7JtqNiSmFPrjr2QfAZ9NXeFvKcXdNp5hrscEJydhkoHe",
            "r45eY1CsoHct1BHtvBLxXGSDpEaRpcAk3opwhbzGHwP",
            "DN23octa9vpZFMd1UmVuZRKersqFDQdFn1EMFyxiW2GQ",
            "7rd6v7DviGgKtGFPNZ6ubD9NY3ensMPLiYYSm3Vw7q4q",
            "65sEfpShyLoZ84FJ1HRWRdoCdKecNB1QbztXYvooEVZ",
            "GLqBw3FWzV2MtX5hnVWYwW9ej2z93UtWxxTfpUNS4EZG",
            "Gk3MTuSkYmVknzZwcuqQSTNHUKWZKPXScw9fGRDGqqam",
            "5PEKT1MfqVJAhFYUpJMPZ2uZqHKFA13J3hiMgQLxJH62",
            "GsQkSHieP5DLAgtruXa3joD95C2zc6AGvRSi8WHDwUig",
            "4SoQAyMNYEwEJ7tgQjqDFYhEgsJfpgRLwZrAPAFuhNRd",
            "Cxv8UQQhCQVPtp3Z1SeueBqZXtKp9nyJW9g2yHMoNQJD",
            "7gvfg5w5xFLEoiRh9t1MVZmBJAMPt9Jh1ghyZk4vLBwK",
            "4awtUzYBY5DNbzVMZ7zP3hMGa2Z4T13bF43muSMwf5Y9",
            "9vZ7uLrSWbX9MWbUZtCEyqVAdJXEfSQpE2zw9KgSPdw5",
            "tsneiquW2xnzXbWNqf4xSLtz6j4EKnMKzq1RdLyiZHm",
            "GdqJYnCwEsW2kqmyegr8kFhd15m1jEAcxpvXW88iTSLF",
            "BvbKHCXLpyamAQrfZyGuyrdvmUeAXb6KvdrLdEaWma7U",
            "DTQWEJyv3vPV7T8wGjHE41xXrZceiNxgBTAK45qwPv32",
            "35fndtNDL31om7Zr7udabBGsbEr9r7bL6bj7fBYkLDZm",
            "3JRWxWWD747SvzDqRwkZZbBSuNrDUVbThMYcw8ztq2Gv",
        )
        
        val expectedPrivateKeys = listOf(
            "tyXdkqXyMPrz62TTGfn3YnZJCjNDbwQMxSp66uRabsh",
            "YgJyp9XFrKG4QsG9TrsgHXzxV6TxFM5Bs4gBChPRVDm",
            "KQTbWpQ9UrbEKTSsMFz9e8ksXHnw7Dxd6fJ2NaZjZrm",
            "ScZ3ipDgk6JG7J69ewr7RVkMwGZoVHeBiRnfm5VDYpZ",
            "LGRhskdPLvCdAPB677bURUAhffoRzP2FbA2PqXmyL1P",
            "LgjhQrnP1iyuQWZfYcKbtoXQhpx5Ngu1Fhqmhk1Mibg",
            "vqZQLZn6gmBuSqHLDGGPSU5s9WFbkb9bVGb96SQySp4",
            "6zAmckWnDSummdZgcwnUwbAyVRhaijEv4CXULaR4igG",
            "Q372ffkjQ3RX5qCBcZb4QKz5eJgvcLei4Dv8a49uDZc",
            "DtuiSyT8bMbFA8PAWmdHXoxv7YdT4JTViLoCvHDm6c6",
            "ac7KDc6APCmPvqtXqhxYh79DVBX9ZStFt5jv77GALv8",
            "F1MbetfCtR7LperoWJVti2sgZSJPZ8EG2AtJjQ7WfGZ",
            "yHujUT44BVTCCj3AJksryzMm4rwzQtpJsYVTP9cwxuT",
            "PqkD1F61g8YrEXDuZ1JWSZiyPGYydYLHtGVCReP4kuY",
            "mPqDbZTUYyguvveq3si1S6wQTt3P8QzGixwZBUK7H4C",
            "UqmSXssLmSPRgodvEaJrtDuRxsLCw9dGXxTRwd7moTv",
            "9nyxsLHEL9hA2PPnNJsf1iTkWCumK7cUMmD7iztfqoh",
            "iqZJqmnxzSr99xqnNhCpCQ5LfHEgBLbBfvGWe5mxdJx",
            "98a9xHmgUEgk3JEcmXFWQhaDHjouXAnCwAjxTZDdX5V",
            "vajbKjYzyc59vZg16nCSjjZ5fQEpxCpSoV5jhUA6V4L",
            "r9WmDfdEhALERkA8rZa8N9x76GGLzno76dW5fUPvCSX",
            "zXmchbcP64QpQtGgDFPhJUKrhtNRiLQtkvTac8P4jBN",
            "cvLu6YLjtgFXPkzd4xM5QMeB1Rswbw6ak3QLTtCVLR1",
            "872Js5jcHMUcZSr1ANGLSaepdyt7wUhJLFpvLGxnzoD",
            "bdhVhtJpa9tD56jLDimsFb4uSpzTmyUoGwtfmKwEnmP",
            "e7PTSqi4585cvWv6y4qdJUMCkGR5JB5rfrq4pE8yEv9",
            "13x5JTZ6DrVKqkdjBYqm9SPscDBqJRBr1jEK6LSGgMyU",
            "qMDqrVyX5e5JLsWkTL8odBoCgM2Zr8k3ZMT8JphJDsE",
            "Z7XvS26j4xVGajfjVzRWEkof58RV1CQSjJAJi4LUDM5",
            "irrvERRMyNFRBskCZnt4AAgzByHoEe7RaNSvbksnsQR",
            "hVi5UVApiCL5tpojBErjTc6ahSKo27x1s4fVJfq6jG2",
            "7GzRoQJFBZ2pJHT6oxyV8Hhxv8RzMaF4CVBUSCTet2y",
            "ZFu53GV4pJNUf4vu8rQUBMN5d1us6fcxci2LmDjo779",
            "aCges4rbStCccY259GUWSwXrxJLJzr4vJcw9wMXkxje",
            "gKRVqLU5Wb6WvKU3YMZ8xDjbjPgB1p1ycGzBPmQptBA",
            "p6uB7qTtspKygZmTcS2N5S3sctQqT6kq6JGQZmbxd4m",
            "H9kmFS5vCDe76vmBfCCaKYH3kK1ENm4cbBSjxaKP6mt",
            "6X4u91HfHt5nqGmWN1qSPjKw4KstUTzg5ph2y51XvX8",
            "qkMKmRxeu6JmLEA27ZdD1CwVo4oH7dtUZ4igJXvj7f2",
            "tmfRMiWLt3vDZz9prJUfjrpT488yEHrS5P3Y1QrbSZW",
            "mexzZXz6jrwQCSiUnb3JAQjRdNuzmtCZAghdUq93aNQ",
            "zXxhX4vXRztTSk11djMSAotixpDCFoTduzS6LrnMyCU",
            "jEw6fJ9hBznUmqtzUoo9WTFLfNvAZVVHKrSfJTQrARt",
            "J4Sbnfqmec6mSdqUmYcUdcdwAdNeZdovSYia7hokmdz",
            "gGzDcXTjzKoKu6S7FeV4yJvWBpTLh9gqokcWHgEgU6u",
            "GY5ZnSNdTbMrm8NDSrYj7rgbevfEcD7x3H6uRRXh2J5",
            "ZM5a5hUbShzRMGMbLHLPSeFDMK9EawCU1n4VmEeksg9",
            "K1tSfXVV5whPShGSGvmK5rZRyXLzzJarHk8WwBZScch",
            "AEcD9bxNqNVsZLGHN59fGawRbTr2YwM6RrDKTNGczsR",
            "12uoCSpmTRJnpDjCGCW5Y3a6iC5yaBRFP4KQ8CsBjF3u",
            "RaR7HZW9Qyix3bRrnBj8iXFVmzwnn1Q9N2McYCANo2x",
            "21NNZWp5a1SUgmiwpN7N5NehELtPaQ95dkKeNYg2vJNm",
            "6ufiPr2LR625RU1Pk82BS81oPPhJGe2qU8LCMGpmjQ4",
            "NNQr38csQFcTYz2yjpx9E6cbaMjqcRN4pZWTWLdzwmV",
            "12LmfHaZJLp2y5Vpcpgvo898rrwji5MxdLZunDviXg8h",
            "24TxjpkYHEooeYVmg3YuA1Eo5kdKFPwtX4HYqD2EJhxx",
            "12wmQjXwG1poimo6w4DNhvQGxweDTKcMsQY7F7RgKo8v",
            "24tBiUy166SRVjoCRAyJNh8JVY37FR14Y9VVRV9mXrDK",
            "RpRVKzb3BFgQikw8oiBFfTfTYDEuPqoaHDDVKFW7PST",
            "6GyciEvMgxUDNR39AFPmkaYU6b8FSqvYFGSYqTmM9Er",
            "Hy2i5KNJycyvCQaUnuCJe3FQTHWKKwcsgnxsZk1qdop",
            "aSAUXsEhPPVcENMwcXkmz7xpHv4WWd2tKg9mxMNvxx3",
            "NMhU1UwJDHvKEntbPc2BtP6ZJngHvXd2TCh2k9uvrRw",
            "6vn3aboKDvZVijCycgpKgYvgon6Wuf1MGHkTYrV5iXg",
        )

        val list = SeedDeriver.invoke(
            hexSeed.hexToByteArray()
        )

        val resultPublicKeys = list.map {
            Base58Encoder.invoke(
                it.public.toEd25519PublicKeyParameters.encoded
            )
        }

        val resultPrivateKeys = list.map {
            Base58Encoder.invoke(
                it.private.toEd25519PrivateKeyKeyParameters.encoded
            )
        }

        assertArrayEquals(
            expectedPublicKeys.toTypedArray(),
            resultPublicKeys.toTypedArray()
        )

        assertArrayEquals(
            expectedPrivateKeys.toTypedArray(),
            resultPrivateKeys.toTypedArray()
        )
    }
}