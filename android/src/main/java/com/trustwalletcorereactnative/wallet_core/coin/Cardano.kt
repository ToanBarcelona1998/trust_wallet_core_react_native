package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.util.Numeric
import com.trustwalletcorereactnative.util.toLong
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Cardano
import wallet.core.java.AnySigner

class Cardano : Coin(CoinType.CARDANO, "m/1852'/1815'/0'/0/0") {
    override fun signTransaction(
        tx: Map<String, Any>,
        mnemonic: String,
        passphrase: String
    ): ByteArray {
        val listOfAllUtxos = mutableListOf<Cardano.TxInput>();
        val utxos: List<Map<String, Any>> = tx["utxos"] as List<Map<String, Any>>

        val message = Cardano.Transfer.newBuilder().apply {
            toAddress = tx["receiverAddress"] as String
            changeAddress = tx["senderAddress"] as String
            amount = tx["amount"]!!.toLong()
        }
            .build()


        val signingInput = Cardano.SigningInput.newBuilder().apply {
            transferMessage = message
            ttl = tx["ttl"]!!.toLong()
        }

        signingInput.addPrivateKey(ByteString.copyFrom(getRawPrivateKey(mnemonic, passphrase)))


        for (utx in utxos) {
            val outpoint = Cardano.OutPoint.newBuilder().apply {
                txHash = ByteString.copyFrom(Numeric.hexStringToByteArray(utx["txid"] as String))
                outputIndex = utx["index"]!!.toLong()
            }.build()


            val utxo = Cardano.TxInput.newBuilder()
                .setOutPoint(outpoint)
                .setAddress(utx["senderAddress"] as String)
                .setAmount(utx["balance"]!!.toLong())
                .build()
            listOfAllUtxos.add(utxo)
        }

        signingInput.addAllUtxos(listOfAllUtxos)

        val output = AnySigner.sign(signingInput.build(), coinType, Cardano.SigningOutput.parser())

        return output.encoded.toByteArray()
    }
}