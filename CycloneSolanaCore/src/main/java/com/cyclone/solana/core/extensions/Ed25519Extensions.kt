package com.cyclone.solana.core.extensions

import org.bouncycastle.crypto.params.AsymmetricKeyParameter
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters

val AsymmetricKeyParameter.toEd25519PublicKeyParameters get() = this as Ed25519PublicKeyParameters

val AsymmetricKeyParameter.toEd25519PrivateKeyKeyParameters get() = this as Ed25519PrivateKeyParameters