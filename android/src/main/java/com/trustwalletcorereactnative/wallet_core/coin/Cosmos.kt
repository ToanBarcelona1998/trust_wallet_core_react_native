package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Cosmos

class Cosmos : Coin(CoinType.COSMOS, "m/44'/118'/0'/0/0") {
//    override fun signTransaction(
//        tx: Map<String, Any>,
//        mnemonic: String,
//        passphrase: String
//    ): ByteArray {
//        val singingInput = Cosmos.SigningInput.newBuilder().apply {
//            chainId = tx["chainId"] as String
//            memo = tx["memo"] as String
//            privateKey = ByteString.copyFrom(getRawPrivateKey(mnemonic,passphrase))
//            publicKey = ByteString.copyFrom((tx["publicKey"] as String).toByteArray())
//        }
//
//        singingInput.addAllMessages(mutableListOf())
//
//        val fee = Cosmos.Fee.newBuilder().apply {
//
//        }
//
//        return super.signTransaction(tx, mnemonic, passphrase)
//    }
}