package com.trustwalletcorereactnative.wallet_core

import wallet.core.jni.HDWallet


object Mnemonic{

    fun random(strength: Int, passphrase: String): String {
        val hdWallet : HDWallet = HDWallet(strength, passphrase)

        return hdWallet.mnemonic()
    }

    fun validateMnemonic(mnemonic : String, passphrase: String) : Boolean{
        return try{
            HDWallet(mnemonic, passphrase)

            true
        }catch (e : Exception){
            false
        }
    }
}