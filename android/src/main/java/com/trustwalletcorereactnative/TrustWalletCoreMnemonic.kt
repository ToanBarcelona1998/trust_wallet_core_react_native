package com.trustwalletcorereactnative

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.trustwalletcorereactnative.wallet_core.Mnemonic

class TrustWalletCoreMnemonic (reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {


    override fun getName(): String {
        return NAME
    }


    @ReactMethod
    fun createMnemonic(length : Int,passPhrase : String , promise: Promise){
        promise.resolve(Mnemonic.random(length,passPhrase))
    }

    @ReactMethod
    fun validateMnemonic(mnemonic : String, passPhrase: String ,promise: Promise){
         promise.resolve(Mnemonic.validateMnemonic(mnemonic,passPhrase))
    }

    companion object {
        const val NAME = "TrustWalletCoreMnemonic"
    }
}