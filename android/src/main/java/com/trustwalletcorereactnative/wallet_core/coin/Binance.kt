package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.util.toLong
import wallet.core.java.AnySigner
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Binance

class Binance : Coin(CoinType.BINANCE, "m/44'/714'/0'/0/0") {
    override fun signTransaction(
        tx: Map<String, Any>,
        mnemonic: String,
        passphrase: String
    ): ByteArray {
        val publicKey = tx["fromAddress"] as String
        val signingInput = Binance.SigningInput.newBuilder().apply {
            chainId = tx["chainID"] as String
            accountNumber = tx["accountNumber"]!!.toLong()
            sequence = tx["sequence"]!!.toLong()
            source = tx["source"]!!.toLong()
            memo = tx["memo"] as String
            privateKey = ByteString.copyFrom(getRawPrivateKey(mnemonic,passphrase))
        }


        val token = Binance.SendOrder.Token.newBuilder().apply {
            denom = tx["name"] as String
            amount = tx["amount"]!!.toLong()
        }

        val input = Binance.SendOrder.Input.newBuilder()
        input.address = ByteString.copyFrom(AnyAddress(publicKey, coinType).data())
        input.addAllCoins(listOf(token.build()))

        val output = Binance.SendOrder.Output.newBuilder()
        output.address =
            ByteString.copyFrom(AnyAddress(tx["toAddress"] as String, coinType).data())
        output.addAllCoins(listOf(token.build()))

        val sendOrder = Binance.SendOrder.newBuilder()
        sendOrder.addAllInputs(listOf(input.build()))
        sendOrder.addAllOutputs(listOf(output.build()))

        signingInput.sendOrder = sendOrder.build()

        val signed: Binance.SigningOutput =
            AnySigner.sign(signingInput.build(), coinType, Binance.SigningOutput.parser())


        return signed.encoded.toByteArray()
    }

}