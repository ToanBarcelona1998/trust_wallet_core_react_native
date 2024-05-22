package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.wallet_core.util.toHexByteArray
import com.trustwalletcorereactnative.wallet_core.util.toLong
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Solana

class SOL : Coin(CoinType.SOLANA , "m/44'/501'/0'/0/0") {
    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        val transaction : Map<String,Any>? = tx["transaction"] as? Map<String,Any>

        val signingInputBuilder = Solana.SigningInput.newBuilder().apply {
            this.privateKey = ByteString.copyFrom(privateKey.toHexByteArray())
            sender = tx["sender"] as String
            nonceAccount = tx["nonce"] as String
            recentBlockhash = tx["recentBlockHash"] as String
            feePayerPrivateKey = ByteString.copyFrom((tx["feePayerPrivateKey"] as String).toHexByteArray())
            feePayer = tx["feePayer"] as String
        }

        when(transaction?.get("type")){
            "Transfer" -> {
                val tx = Solana.Transfer.newBuilder().apply {
                    recipient = transaction["recipient"] as String
                    memo = transaction["memo"] as String
                    value = transaction["value"]!!.toLong()
                }.build()

                signingInputBuilder.transferTransaction = tx
            }
            "Delegate_Stake" -> {
                val tx = Solana.DelegateStake.newBuilder().apply {
                    value = transaction["value"]!!.toLong()
                    validatorPubkey = transaction["pubKey"] as String
                    stakeAccount = transaction["account"] as String
                }.build()
                signingInputBuilder.delegateStakeTransaction = tx
            }
            "Transfer_Token" -> {
                val tx = Solana.TokenTransfer.newBuilder().apply {
                    tokenMintAddress = transaction["tokenAddress"] as String
                    senderTokenAddress = transaction["sender"] as String
                    recipientTokenAddress = transaction["recipient"] as String
                    memo = transaction["memo"] as String
                    amount = transaction["amount"]!!.toLong()
                }.build()
                signingInputBuilder.tokenTransferTransaction = tx
            }
            else -> {
                throw Exception("Un support transaction type")
            }
        }

        val signingInput : Solana.SigningInput = AnySigner.sign(signingInputBuilder.build(),coinType,
            Solana.SigningInput.parser())

        val singed : Solana.SigningOutput = Solana.SigningOutput.parseFrom(signingInput.toByteArray())

        return singed.encoded
    }

    override fun getRawPublicKey( mnemonic: String, passphrase: String): ByteArray {
        return getNativePrivateKey(mnemonic,passphrase).publicKeyEd25519.data()
    }
}