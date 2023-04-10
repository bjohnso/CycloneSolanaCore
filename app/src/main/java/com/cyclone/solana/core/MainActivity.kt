package com.cyclone.solana.core

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cyclone.solana.core.constants.Unit
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.account_info.AccountInfo
import com.cyclone.solana.core.extensions.toBase58
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.SolanaRpcApi
import com.cyclone.solana.core.network.factory.HttpClientFactory
import com.cyclone.solana.core.repository.implementation.SolanaRpcApiRepositoryImpl
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.SolTransferTransaction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters

class MainActivity: AppCompatActivity() {

    val client = HttpClientFactory().createOkHttpClient("https://api.devnet.solana.com/")
    val api = SolanaRpcApi(client)
    val repo = SolanaRpcApiRepositoryImpl(api)

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {

            val publicKeyParameters = Ed25519PublicKeyParameters(
                Base58Decoder.invoke("DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D")
            )

            val privateKeyParameters = Ed25519PrivateKeyParameters(
                Base58Decoder.invoke("7oLVLF7Pwxdwc6BUqLGdTgg6y5hMcgW3gkwwRQsiRrRT")
            )

            val keyPair = AsymmetricCipherKeyPair(
                publicKeyParameters,
                privateKeyParameters
            )

            val fromAddress = "DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D"
            val toAddress = "GAMZ2R6wjQPpB9ydQD2LER1cMD9iuiPa9AUs7dDWmvEk"
            val blockhash = "3dzVXVEaNasfNTJHNimL2YE4piGWwCanBtnPcQ6J44z1"
            val lamports = Unit.Units.LAMPORTS_PER_SOL / 10

            val transaction = SolTransferTransaction.invoke(
                fromAddress,
                toAddress,
                blockhash,
                lamports
            )
                .apply { sign(listOf(keyPair)) }
                .serialise()
                .toBase58()

            val tx = "5JtwPkazZpmm8Q39cdY7MgWKZDsL356KTXUfcuQFfuvxonDgXscfXuhnQ5AonCzZDY3smRpZQ3H81YNyH6pBMH7H"

            repo.getAccountInfo(
                "E8qML25hDPZ1XWv9MP3pQ28dzY5oUmbbHUXVRoDuduUg"
            ).collectLatest {
                when (it) {
                    is NetworkResource.Success -> {
                        val result = when (val res = it.result.result) {
                            is Result.GetBalanceResult -> res.getBalanceResult
                            is Result.GetLatestBlockhashResult-> res.getLatestBlockhashResult
                            is Result.SendTransactionResult -> res.sendTransactionResult
                            is Result.GetTransactionResult -> res.getTransactionResult
                            is Result.GetTokenAccountsByOwnerResult -> res.getTokenAccountsByOwnerResult
                            is Result.GetAccountInfoResult -> res.getAccountInfoResult
                            else -> null
                        }
                        Log.e("TEST_ME", "Success: $result")
                    }
                    is NetworkResource.Error -> {
                        val error = it.error?.error
                        Log.e("TEST_ME", "Error: ${error?.code} ${error?.message}")
                    }
                    else -> {
                        Log.e("TEST_ME", "Loading")
                    }
                }
            }
        }
    }
}