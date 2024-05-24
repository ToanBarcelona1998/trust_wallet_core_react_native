package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.wallet_core.error.CEError
import com.trustwalletcorereactnative.wallet_core.error.CError
import com.trustwalletcorereactnative.wallet_core.util.toHex
import com.trustwalletcorereactnative.wallet_core.util.toHexByteArray
import com.trustwalletcorereactnative.wallet_core.util.toLong
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import wallet.core.jni.PrivateKey
import wallet.core.jni.proto.Binance

class Binance : Coin(CoinType.BINANCE, "m/44'/714'/0'/0/0") {

    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        val transaction: Map<String,Any> = tx["transaction"] as? Map<String,Any> ?: throw CError(
            CEError.MissingArgumentError.message,
            CEError.MissingArgumentError.code,)

        val publicKey = PrivateKey(privateKey.toHexByteArray()).getPublicKeySecp256k1(true)
        val signingInput = Binance.SigningInput.newBuilder().apply {
            chainId = tx["chainId"] as String
            accountNumber = tx["accountNumber"]!!.toLong()
            sequence = tx["sequence"]!!.toLong()
            source = tx["source"]!!.toLong()
            memo = tx["memo"] as String
            this.privateKey = ByteString.copyFrom(privateKey.toHexByteArray())
        }


        val token = Binance.SendOrder.Token.newBuilder().apply {
            denom = transaction["denom"] as String
            amount = transaction["amount"]!!.toLong()
        }

        val input = Binance.SendOrder.Input.newBuilder()
        input.address = ByteString.copyFrom(AnyAddress(publicKey, coinType).data())
        input.addAllCoins(listOf(token.build()))

        val output = Binance.SendOrder.Output.newBuilder()
        output.address =
            ByteString.copyFrom(AnyAddress(transaction["to"] as String, coinType).data())
        output.addAllCoins(listOf(token.build()))

        val sendOrder = Binance.SendOrder.newBuilder()
        sendOrder.addAllInputs(listOf(input.build()))
        sendOrder.addAllOutputs(listOf(output.build()))

        signingInput.sendOrder = sendOrder.build()

        val signed: Binance.SigningOutput =
            AnySigner.sign(signingInput.build(), coinType, Binance.SigningOutput.parser())


        return signed.encoded.toByteArray().toHex()
    }

}