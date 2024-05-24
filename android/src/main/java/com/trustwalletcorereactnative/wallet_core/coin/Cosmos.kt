package com.trustwalletcorereactnative.wallet_core.coin

import com.google.protobuf.ByteString
import wallet.core.jni.CoinType
import wallet.core.jni.proto.Cosmos

class Cosmos : Coin(CoinType.COSMOS, "m/44'/118'/0'/0/0") {
    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        return super.signTransaction(tx, privateKey)
    }
}