import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction.Transaction
import com.cyclone.solana.core.datamodel.dto.solanaRPC.result.transaction.TransactionMeta
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResult(
    val blockTime: Long? = null,
    val meta: TransactionMeta? = null,
    val slot: Long? = null,
    val transaction: Transaction? = null,
)