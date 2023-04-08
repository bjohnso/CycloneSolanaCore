package com.cyclone.solana.core.crypto

import com.cyclone.solana.core.constants.Address
import com.cyclone.solana.core.datamodel.dto.metaplex.MetaData
import com.cyclone.solana.core.encryption.derivation.Derivation
import com.cyclone.solana.core.extensions.base58ToByteArray
import com.cyclone.solana.core.extensions.toBase58
import com.cyclone.solana.core.parser.metaplex.Decoder
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Test
import java.util.*

class NFTTest {
    @Test
    fun `derive metaplex pda`() {
        val metaDataSeed = "metadata"
        val metadataProgramId = Address.ProgramAddresses.TOKEN_META_DATA_PROGRAM
        val mint = "4siT7XaV5xz6d1ArAy4ma8SkNRUhsrCQxjw9BpB8PfTK"

        val (pda, bump) = Derivation.findProgramAddress(
            listOf(
                metaDataSeed.toByteArray(),
                metadataProgramId.base58ToByteArray(),
                mint.base58ToByteArray()
            ),
            metadataProgramId
        )

        Assert.assertEquals("FWxGAqf4SRnThuio5DFpKArAD4vS75XDj1j3vxL8GJWr", pda.toBase58())
        Assert.assertEquals(255, bump)
    }

    @Test
    fun `parse metaplex data`() {
        val metaplexData = "BL0oT94VJre5Nqdr3m2JVbZhflagHl0DCd7aPEJuCnygOZD4GarKfuP80i1oVZ+uPPsiSLQum2MYash6e/sezy4gAAAAU29sIE90dGVycyAjNwAAAAAAAAAAAAAAAAAAAAAAAAAKAAAAT1RURVIAAAAAAMgAAABodHRwczovL2Fyd2VhdmUubmV0L2UzWjB2c3NGZG83alNTNzg3OXEzd18tUzE0a05vZnM5dDlYN2pjR3hDZzgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOgDAQIAAAC7Nfcto5p5uun0ze44YN7QnloQrhLd5txolTwKUZHZtwEAvShP3hUmt7k2p2vebYlVtmF+VqAeXQMJ3to8Qm4KfKAAZAABAf4BAAEBrlOUjkfjFn/PzBUJJh5kKhczbpRLeSV8x/oNwglrCR4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=="
        val dataRawBytes = Base64.getDecoder().decode(metaplexData)

        val metaData = Decoder.unpackMetadataAccount(dataRawBytes)

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }

        val expectedJson = "{\"updateAuthority\":\"DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D\",\"mint\":\"4siT7XaV5xz6d1ArAy4ma8SkNRUhsrCQxjw9BpB8PfTK\",\"data\":{\"name\":\"Sol Otters #7\",\"symbol\":\"OTTER\",\"uri\":\"https://arweave.net/e3Z0vssFdo7jSS7879q3w_-S14kNofs9t9X7jcGxCg8\",\"sellerFeeBasisPoints\":1000,\"creators\":[{\"address\":\"Dbny5adAUUGWcecdbRXuND8SA6XXhQKUeqWRyjJk1B3C\",\"verified\":true,\"share\":0},{\"address\":\"DjPi1LtwrXJMAh2AUvuUMajCpMJEKg8N1J8fU4L2Xr9D\",\"verified\":false,\"share\":100}]},\"primarySaleHappened\":false,\"isMutable\":true}"
        val expectedMetaData = json.decodeFromString<MetaData>(expectedJson)

        Assert.assertEquals(expectedMetaData, metaData)
    }
}