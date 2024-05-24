package com.trustwalletcorereactnative.wallet_core.coin

import com.trustwalletcorereactnative.wallet_core.error.CEError
import com.trustwalletcorereactnative.wallet_core.error.CError
import com.trustwalletcorereactnative.wallet_core.interfaces.ICoin
import com.trustwalletcorereactnative.wallet_core.util.toHex
import com.trustwalletcorereactnative.wallet_core.util.toHexByteArray
import org.json.JSONObject
import wallet.core.java.AnySigner
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.PrivateKey

abstract class Coin(coinType: CoinType, derivationPath: String) : ICoin {

    var coinType: CoinType? = null
    private var derivationPath: String? = null


    companion object{
        fun createCoinByteType(coinType : Int) : Coin {
            return when(coinType){
                714 -> {
                    Binance()
                }

                0 -> {
                    Bitcoin()
                }

                1815 -> {
                    Cardano()
                }
                60 -> {
                    Ethereum()
                }
                501 -> {
                    SOL()
                }
                else -> {
                    throw CError(CEError.UnSupportedCoin.message,CEError.UnSupportedCoin.code)
                }
            }
        }
    }

    init {
        this.coinType = coinType
        this.derivationPath = derivationPath
    }

    override fun getAddress(mnemonic: String, passphrase: String): String {
        val hdWallet: HDWallet = HDWallet(mnemonic, passphrase)

        val privateKey: PrivateKey = hdWallet.getKey(coinType, derivationPath)

        return coinType!!.deriveAddress(privateKey)
    }

    override fun getSeed(mnemonic: String, passphrase: String): ByteArray {
        val hdWallet: HDWallet = HDWallet(mnemonic, passphrase)

        return hdWallet.seed()
    }

    override fun getHexPrivateKey(mnemonic: String, passphrase: String): String {
        return getRawPrivateKey(mnemonic, passphrase).toHex()
    }

    override fun getRawPrivateKey(mnemonic: String, passphrase: String): ByteArray {
        return getNativePrivateKey(mnemonic, passphrase).data()
    }

    override fun getHexPublicKey(mnemonic: String, passphrase: String): String {
        return getRawPublicKey(mnemonic, passphrase).toHex()
    }

    override fun getRawPublicKey(mnemonic: String, passphrase: String): ByteArray {
        return getNativePrivateKey(mnemonic, passphrase).getPublicKeySecp256k1(true).data()
    }

    fun getNativePrivateKey(mnemonic: String, passphrase: String): PrivateKey {
        try {
            val hdWallet: HDWallet = HDWallet(mnemonic, passphrase)

            return hdWallet.getKey(coinType, derivationPath)
        }catch (e : Exception){
            throw CError(CEError.InvalidMnemonic.code,CEError.InvalidMnemonic.message)
        }
    }

    override fun sign(
        mnemonic: String,
        passphrase: String,
        message: String
    ): String {
        val privateKey: PrivateKey = getNativePrivateKey(mnemonic, passphrase)

        return privateKey.sign(message.toHexByteArray(), coinType!!.curve()).toHex()
    }

    override fun signTransaction(
        tx: Map<String, Any>,
        mnemonic: String,
        passphrase: String,
    ): String {
        val privateKey = getHexPrivateKey(mnemonic, passphrase)

        return signTransaction(tx,privateKey)
    }

    override fun signTransaction(tx: Map<String, Any>, privateKey: String): String {
        val opJson = JSONObject(tx).toString()
        return AnySigner.signJSON(opJson, privateKey.toHexByteArray(), coinType!!.value())
    }

//    override fun multiSignTransaction(
//        mnemonic: String,
//        passphrase: String,
//        tx: Map<String, Any>,
//        privateKeys: ArrayList<ByteArray>
//    ): ArrayList<ByteArray> {
//        val signatures = arrayListOf<ByteArray>()
//
//        for (privateKey in privateKeys) {
//            val signature = signTransaction(tx,mnemonic, passphrase)
//            signatures.add(signature)
//        }
//        return signatures
//    }
}