package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import com.trustwalletcorereactnative.wallet_core.util.Numeric
import com.trustwalletcorereactnative.wallet_core.util.toHex
import com.trustwalletcorereactnative.wallet_core.util.toHexByteArray
import com.trustwalletcorereactnative.wallet_core.util.toLong
import wallet.core.java.AnySigner
import wallet.core.jni.BitcoinAddress
import wallet.core.jni.BitcoinScript
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Bitcoin
import wallet.core.jni.proto.Bitcoin.SigningInput

class Bitcoin : Coin(CoinType.BITCOIN, "m/44'/0'/0'/0/0") {
    override fun getAddress(mnemonic: String, passphrase: String): String {
        val privateKey = getNativePrivateKey(mnemonic,passphrase)
        val publicKey = privateKey.getPublicKeySecp256k1(true)

        val address = BitcoinAddress(publicKey, coinType!!.p2pkhPrefix())

        return address.description()
    }

    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        val utxos: List<Map<String, Any>> = tx["utxos"] as List<Map<String, Any>>

        val type = coinType!!;

        val signingInput = SigningInput.newBuilder().apply {
            amount = tx["amount"]!!.toLong()
            hashType = BitcoinScript.hashTypeForCoin(type)
            toAddress = tx["to"] as String
            changeAddress = tx["changeAddress"] as String
            byteFee = tx["fee"]!!.toLong()
        }.addPrivateKey(ByteString.copyFrom(privateKey.toHexByteArray()))

        for (utx in utxos) {
            val txHash = Numeric.hexStringToByteArray(utx["txid"] as String)
            txHash.reverse()
            val outPoint = Bitcoin.OutPoint.newBuilder()
                .setHash(ByteString.copyFrom(txHash))
                .setIndex(utx["vout"] as Int)
                .setSequence(Long.MAX_VALUE.toInt())
                .build()
            val txScript = (utx["script"] as String).toHexByteArray()

            val utxo = Bitcoin.UnspentTransaction.newBuilder()
                .setAmount(utx["value"]!!.toLong())
                .setOutPoint(outPoint)
                .setScript(ByteString.copyFrom(txScript))
                .build()
            signingInput.addUtxo(utxo)
        }

        val output = AnySigner.sign(signingInput.build(), coinType, Bitcoin.SigningOutput.parser())

        return output.encoded.toByteArray().toHex()
    }
}