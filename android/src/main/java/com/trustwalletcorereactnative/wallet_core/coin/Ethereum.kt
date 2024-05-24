package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.wallet_core.error.CEError
import com.trustwalletcorereactnative.wallet_core.error.CError
import com.trustwalletcorereactnative.wallet_core.util.toHex
import com.trustwalletcorereactnative.wallet_core.util.toHexByteArray
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Ethereum
import wallet.core.jni.proto.Ethereum.Transaction

open class Ethereum : Coin(CoinType.ETHEREUM,"m/44'/60'/0'/0/0"){
    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        val transaction: Map<String, Any> = tx["transaction"] as? Map<String,Any>
            ?: throw CError(CEError.MissingArgumentError.message,CEError.MissingArgumentError.code,)

        val signingInputBuilder = Ethereum.SigningInput.newBuilder().apply {
            this.privateKey = ByteString.copyFrom(privateKey.toHexByteArray())
            chainId = ByteString.copyFrom((tx["chainId"] as String).toHexByteArray())
            nonce = ByteString.copyFrom((tx["nonce"] as String).toHexByteArray())
            gasPrice = ByteString.copyFrom((tx["gasPrice"] as String).toHexByteArray())
            gasLimit = ByteString.copyFrom((tx["gasLimit"] as String).toHexByteArray())
        }


        when(transaction["type"]){
            "Transfer" -> {
                val tx = Transaction.newBuilder().apply {
                    transfer = Transaction.Transfer.newBuilder().apply {
                        amount = ByteString.copyFrom((transaction["amount"] as String).toHexByteArray())
                    }.build()
                }.build()

                signingInputBuilder.transaction = tx
                signingInputBuilder.toAddress = transaction["to"] as String

            }
            "ERC20_Transfer" -> {
                val tx = Transaction.newBuilder().apply {
                    erc20Transfer = Transaction.ERC20Transfer.newBuilder().apply {
                        to = transaction["to"] as String
                        amount = ByteString.copyFrom((transaction["amount"] as String).toHexByteArray())
                    }.build()
                }.build()

                signingInputBuilder.transaction = tx
                signingInputBuilder.toAddress = transaction["contractAddress"] as String
            }
            "ERC721_Transfer" -> {
                val tx = Transaction.newBuilder().apply {
                    erc721Transfer = Transaction.ERC721Transfer.newBuilder().apply {
                        to = transaction["to"] as String
                        tokenId = ByteString.copyFrom((transaction["tokenId"] as String).toHexByteArray())
                    }.build()
                }.build()

                signingInputBuilder.transaction = tx
                signingInputBuilder.toAddress = transaction["contractAddress"] as String
            }
            "ERC20_Approve" -> {
                val tx = Transaction.newBuilder().apply {
                    erc20Approve = Transaction.ERC20Approve.newBuilder().apply {
                        amount = ByteString.copyFrom((transaction["amount"] as String).toHexByteArray())
                        spender = transaction["spender"] as String
                    }.build()
                }.build()

                signingInputBuilder.transaction = tx
                signingInputBuilder.toAddress = transaction["contractAddress"] as String
            }
            else -> {
                throw CError(CEError.UnsupportedTransaction.message,CEError.UnsupportedTransaction.code)
            }

        }

        val signed = AnySigner.sign(signingInputBuilder.build(),coinType,Ethereum.SigningOutput.parser())

        return signed.encoded.toByteArray().toHex()
    }
}