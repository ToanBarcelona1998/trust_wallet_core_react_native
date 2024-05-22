package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.wallet_core.util.Numeric
import com.trustwalletcorereactnative.wallet_core.util.toHex
import com.trustwalletcorereactnative.wallet_core.util.toHexByteArray
import com.trustwalletcorereactnative.wallet_core.util.toLong
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Cardano
import wallet.core.java.AnySigner

class Cardano : Coin(CoinType.CARDANO, "m/1852'/1815'/0'/0/0") {

    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        val listOfAllUtxos = mutableListOf<Cardano.TxInput>();
        val utxos: List<Map<String, Any>> = tx["utxos"] as List<Map<String, Any>>


        for (utx in utxos) {
            val outpoint = Cardano.OutPoint.newBuilder().apply {
                txHash = ByteString.copyFrom(Numeric.hexStringToByteArray(utx["txid"] as String))
                outputIndex = utx["index"]!!.toLong()
            }.build()


            val utxo = Cardano.TxInput.newBuilder()
                .setOutPoint(outpoint)
                .setAddress(utx["sender"] as String)
                .setAmount(utx["amount"]!!.toLong())
                .build()
            listOfAllUtxos.add(utxo)
        }

        val message = Cardano.Transfer.newBuilder().apply {
            toAddress = tx["to"] as String
            changeAddress = tx["changeAddress"] as String
            amount = tx["amount"]!!.toLong()
        }
            .build()


        val signingInput = Cardano.SigningInput.newBuilder().apply {
            transferMessage = message
            ttl = tx["ttl"]!!.toLong()
        }

        signingInput.addPrivateKey(ByteString.copyFrom(privateKey.toHexByteArray()))

        signingInput.addAllUtxos(listOfAllUtxos)

        val output = AnySigner.sign(signingInput.build(), coinType, Cardano.SigningOutput.parser())

        return output.encoded.toByteArray().toHex()
    }
}