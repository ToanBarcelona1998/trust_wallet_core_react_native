package com.trustwalletcorereactnative

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.trustwalletcorereactnative.wallet_core.coin.Coin
import com.trustwalletcorereactnative.wallet_core.error.CEError
import com.trustwalletcorereactnative.wallet_core.error.CError

class TrustWalletCoreCoin(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    private fun resolveError(e : Exception,promise: Promise){
        if(e is CError){
            promise.reject(e.getCode(),e.message , null)
        }else{
            promise.reject(CEError.Unknown.code,e.message , null)
        }
    }

    private fun convertReadableMapToMap(readableMap: ReadableMap): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val iterator = readableMap.keySetIterator()

        while (iterator.hasNextKey()) {
            val key = iterator.nextKey()

            when (readableMap.getType(key)) {
                ReadableType.Boolean -> map[key] = readableMap.getBoolean(key)
                ReadableType.Number -> map[key] = readableMap.getDouble(key)
                ReadableType.String -> map[key] = readableMap.getString(key)!!
                ReadableType.Map -> map[key] = convertReadableMapToMap(readableMap.getMap(key)!!)
                ReadableType.Array -> map[key] =
                    convertReadableArrayToList(readableMap.getArray(key)!!)

                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
        return map
    }

    private fun convertReadableArrayToList(readableArray: ReadableArray): List<Any> {
        val list: MutableList<Any> = ArrayList()
        for (i in 0 until readableArray.size()) {
            when (readableArray.getType(i)) {
                ReadableType.Boolean -> list.add(readableArray.getBoolean(i))
                ReadableType.Number -> list.add(readableArray.getDouble(i))
                ReadableType.String -> list.add(readableArray.getString(i))
                ReadableType.Map -> list.add(convertReadableMapToMap(readableArray.getMap(i)))
                ReadableType.Array -> list.add(convertReadableArrayToList(readableArray.getArray(i)))
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }
        return list
    }



    override fun getName(): String {
        return NAME
    }

    @ReactMethod
    fun getAddress(mnemonic: String, passPhrase: String, coinType: Int, promise: Promise) {
        try{
            val coin = Coin.createCoinByteType(coinType)

            val address = coin.getAddress(mnemonic, passPhrase)

            promise.resolve(address)
        }catch (e : Exception){
            resolveError(e,promise)
        }
    }

    @ReactMethod
    fun getSeed(mnemonic: String, passPhrase: String, coinType: Int, promise: Promise) {
        try{
            val coin = Coin.createCoinByteType(coinType)

            val seed = coin.getSeed(mnemonic, passPhrase)

            promise.resolve(seed)
        }catch (e : Exception){
            resolveError(e,promise)
        }
    }

    @ReactMethod
    fun getHexPrivateKey(mnemonic: String, passPhrase: String, coinType: Int, promise: Promise) {
        try{
            val coin = Coin.createCoinByteType(coinType)

            val privateKey = coin.getHexPrivateKey(mnemonic, passPhrase)

            promise.resolve(privateKey)
        }catch (e : Exception){
            resolveError(e,promise)
        }
    }

    @ReactMethod
    fun getRawPrivateKey(mnemonic: String, passPhrase: String, coinType: Int, promise: Promise) {
        try{
            val coin = Coin.createCoinByteType(coinType)

            val privateKey = coin.getRawPrivateKey(mnemonic, passPhrase)

            val array = Arguments.createArray()

            for (b in privateKey){
                array.pushInt(b.toInt())
            }

            promise.resolve(array)
        }catch (e : Exception){
            resolveError(e, promise)
        }
    }

    @ReactMethod
    fun getHexPublicKey(mnemonic: String, passPhrase: String, coinType: Int, promise: Promise) {
        try{
            val coin = Coin.createCoinByteType(coinType)

            val publicKey = coin.getHexPublicKey(mnemonic, passPhrase)

            promise.resolve(publicKey)
        }catch (e : Exception){
            resolveError(e,promise)
        }
    }

    @ReactMethod
    fun getRawPublicKey(mnemonic: String, passPhrase: String, coinType: Int, promise: Promise) {
        try {
            val coin = Coin.createCoinByteType(coinType)

            val array = Arguments.createArray()

            val publicKey = coin.getRawPublicKey(mnemonic, passPhrase)

            for (p in publicKey){
                array.pushInt(p.toInt())
            }

            promise.resolve(array)
        }catch (e: Exception){
            resolveError(e,promise)
        }
    }

    @ReactMethod
    fun sign(mnemonic: String, passPhrase: String, coinType: Int, message: String, promise: Promise){
        try {
            val coin = Coin.createCoinByteType(coinType)

            val sig = coin.sign(mnemonic,passPhrase,message)

            promise.resolve(sig)
        }catch (e: Exception){
            resolveError(e,promise)
        }
    }

    @ReactMethod
    fun signTransaction(mnemonic: String, passPhrase: String, coinType: Int, tx: ReadableMap, promise: Promise){
        try{
            val coin = Coin.createCoinByteType(coinType)

            val signed = coin.signTransaction(convertReadableMapToMap(tx),mnemonic,passPhrase)

            promise.resolve(signed)
        }catch (e : Exception){
            resolveError(e,promise)
        }
    }

    @ReactMethod
    fun signTransactionWithPrivateKey(privateKey : String,coinType: Int, tx: ReadableMap, promise: Promise){
        try {
            val coin = Coin.createCoinByteType(coinType)

            val signed = coin.signTransaction(convertReadableMapToMap(tx),privateKey)

            promise.resolve(signed)
        }catch (e : Exception){
            resolveError(e,promise)
        }
    }

//    @ReactMethod
//    fun multiSignTransaction(mnemonic: String, passphrase: String, tx: ReadableMap, privateKeys: ArrayList<ByteArray>,coinType: Int, promise: Promise){
//        val coin = Coin.createCoinByteType(coinType)
//
//        val array = Arguments.createArray()
//
//        val signed = coin.multiSignTransaction(mnemonic,passphrase,convertReadableMapToMap(tx),privateKeys)
//
//        for (s in signed){
//            val arr = Arguments.createArray()
//
//            for (raw in s){
//                arr.pushInt(raw.toInt())
//            }
//
//            array.pushArray(arr)
//        }
//
//        promise.resolve(array)
//    }

    companion object {
        const val NAME = "TrustWalletCoreCoin"
    }
}
