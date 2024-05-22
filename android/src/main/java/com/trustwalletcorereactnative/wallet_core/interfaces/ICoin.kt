package com.trustwalletcorereactnative.wallet_core.interfaces

interface ICoin {
    fun getAddress(mnemonic: String, passphrase: String): String
    fun getSeed(mnemonic: String, passphrase: String): ByteArray
    fun getHexPrivateKey(mnemonic: String, passphrase: String): String
    fun getRawPrivateKey(mnemonic: String, passphrase: String): ByteArray
    fun getHexPublicKey(mnemonic: String, passphrase: String): String
    fun getRawPublicKey(mnemonic: String, passphrase: String): ByteArray
    fun sign(mnemonic: String, passphrase: String, message: String): String
    fun signTransaction(tx: Map<String,Any>,mnemonic: String, passphrase: String): String
    fun signTransaction(tx: Map<String,Any>,privateKey: String): String
//    fun multiSignTransaction(
//        mnemonic: String, passphrase: String,
//        tx: Map<String,Any>,
//        privateKeys: ArrayList<ByteArray>
//    ): ArrayList<ByteArray>
}