package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.wallet_core.error.CEError
import com.trustwalletcorereactnative.wallet_core.error.CError
import com.trustwalletcorereactnative.wallet_core.util.toHex
import com.trustwalletcorereactnative.wallet_core.util.toHexByteArray
import com.trustwalletcorereactnative.wallet_core.util.toLong
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Cosmos

class Cosmos : Coin(CoinType.COSMOS, "m/44'/118'/0'/0/0") {
    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        val transaction: Map<String, Any> = tx["transaction"] as? Map<String,Any>
            ?: throw CError(CEError.MissingArgumentError.message, CEError.MissingArgumentError.code,)

        val message = Cosmos.Message.newBuilder()


        when(transaction["type"] as? String) {
            "Send" -> {
                val messageSend = Cosmos.Message.Send.newBuilder().apply {
                    fromAddress = transaction["from"] as String
                    toAddress = transaction["to"] as String
                }


                val amount = Cosmos.Amount.newBuilder().apply {
                    denom = transaction["denom"] as String
                    amount = transaction["amount"] as String
                }

                messageSend.addAmounts(amount)

                message.sendCoinsMessage = messageSend.build()
            }
            "AuthGrant" -> {
                val author = Cosmos.Message.StakeAuthorization.newBuilder().apply {
                    maxTokens = Cosmos.Amount.newBuilder().apply {
                        amount = transaction["amount"] as String
                        denom = transaction["denom"] as String
                    }.build()
                }.build()

                val msg = Cosmos.Message.AuthGrant.newBuilder().apply {
                    granter = transaction["granter"] as String
                    grantee = transaction["grantee"] as String
                    expiration = transaction["expiration"]!!.toLong()
                    grantStake = author
                }.build()

                message.authGrant = msg
            }
            "AuthRevoke" -> {
                val msg = Cosmos.Message.AuthRevoke.newBuilder().apply {
                    grantee = transaction["grantee"] as String
                    granter = transaction["granter"] as String
                }.build()

                message.authRevoke = msg
            }
            "ExecuteContract_Send" -> {
                val msg = Cosmos.Message.WasmExecuteContractSend.newBuilder().apply {
                    msg = transaction["msg"] as String
                    senderAddress = transaction["sender"] as String
                    recipientContractAddress = transaction["recipientContractAddress"] as String
                    contractAddress = transaction["contractAddress"] as String
                    amount = ByteString.copyFrom((transaction["amount"] as String).toHexByteArray())
                }.build()

                message.wasmExecuteContractSendMessage = msg
            }
            "ExecuteContract_Generic" -> {
                val msg = Cosmos.Message.WasmExecuteContractGeneric.newBuilder().apply {
                    senderAddress = transaction["sender"] as String
                    contractAddress = transaction["contractAddress"] as String
                    executeMsg = transaction["msg"] as String
                }.build()

                message.wasmExecuteContractGeneric = msg
            }
            else ->{
                throw CError(CEError.UnsupportedTransaction.message,CEError.UnsupportedTransaction.code)
            }
        }

        val signingInput = Cosmos.SigningInput.newBuilder().apply {
            chainId = tx["chainId"] as String
            memo = tx["memo"] as String
            this.privateKey = ByteString.copyFrom(privateKey.toHexByteArray())
        }

        signingInput.addMessages(message)

        val signed : Cosmos.SigningOutput = AnySigner.sign(signingInput.build(),coinType,Cosmos.SigningOutput.parser())

        return signed.signature.toByteArray().toHex()
    }
}