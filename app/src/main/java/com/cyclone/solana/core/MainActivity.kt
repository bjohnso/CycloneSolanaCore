package com.cyclone.solana.core

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cyclone.solana.core.constants.Unit
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.Result
import com.cyclone.solana.core.extensions.toBase58
import com.cyclone.solana.core.network.NetworkResource
import com.cyclone.solana.core.network.api.NFTApi
import com.cyclone.solana.core.network.api.SolanaRPCApi
import com.cyclone.solana.core.network.factory.HttpClientFactoryImpl
import com.cyclone.solana.core.parser.metaplex.MetaplexParser
import com.cyclone.solana.core.repository.implementation.NFTRepositoryImpl
import com.cyclone.solana.core.repository.implementation.SolanaRpcApiRepositoryImpl
import com.cyclone.solana.core.usecase.Base58Decoder
import com.cyclone.solana.core.usecase.MetaplexPDADeriver
import com.cyclone.solana.core.usecase.SolTransferTransaction
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.bouncycastle.crypto.AsymmetricCipherKeyPair
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class MainActivity: AppCompatActivity() {

    val client = HttpClientFactoryImpl().createOkHttpClient("https://api.devnet.solana.com/")
    val clientNFT = HttpClientFactoryImpl().createOkHttpClient()
    val api = SolanaRPCApi(client)
    val nftApi = NFTApi(clientNFT)
    val repo = SolanaRpcApiRepositoryImpl(api)
    val nftRepo = NFTRepositoryImpl(nftApi)

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

            val nftOwner = "AQgMCXr8gcbjKmiB525YzcqQdVEZtAYVeBDWyDEBg9Z3"

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

            val metaplexPDA = "FWxGAqf4SRnThuio5DFpKArAD4vS75XDj1j3vxL8GJWr"

            repo.getAccountInfo(
                metaplexPDA
            ).collectLatest {
                when (it) {
                    is NetworkResource.Success -> {
                        var result: Any? = null

                        when (val res = it.result.result) {
                            is Result.GetBalanceResult -> {
                                result = res.getBalanceResult
                            }
                            is Result.GetLatestBlockhashResult-> {
                                result = res.getLatestBlockhashResult
                            }
                            is Result.SendTransactionResult -> {
                                result = res.sendTransactionResult
                            }
                            is Result.GetTransactionResult -> {
                                result = res.getTransactionResult
                            }
                            is Result.GetTokenAccountsByOwnerResult -> {
                                result = res.getTokenAccountsByOwnerResult

                                val mint = res.getTokenAccountsByOwnerResult.firstOrNull()?.account?.data?.parsed?.info?.mint

                                val (pda, bump) = MetaplexPDADeriver.invoke(mint!!)

                                val pdaAddress = pda.toBase58()

                                Log.e("TEST_ME", "PDA: $pdaAddress - $bump")
                            }
                            is Result.GetAccountInfoResult -> {
                                result = res.getAccountInfoResult

                                val parsedMetaDataAccount = MetaplexParser.unpackMetadataAccount(
                                    Base64.decode(res.getAccountInfoResult.data!!.first())
                                )

                                Log.e("TEST_ME", "Parsed: $parsedMetaDataAccount")

                                nftRepo.getNFTMetaData(parsedMetaDataAccount.data.uri).collectLatest {
                                    when (it) {
                                        is NetworkResource.Success -> {
                                            Log.e("TEST_ME", "NFT: ${it.result}")
                                        }
                                        else -> {
                                            Log.e("TEST_ME", "NFT: OOPS")
                                        }
                                    }
                                }
                            }
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