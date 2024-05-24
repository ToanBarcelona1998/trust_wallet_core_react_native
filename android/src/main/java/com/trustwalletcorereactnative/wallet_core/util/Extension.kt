package com.trustwalletcorereactnative.wallet_core.util


fun ByteArray.toHex(): String {
    return Numeric.toHexString(this)
}

fun String.toHexByteArray(): ByteArray {
    return Numeric.hexStringToByteArray(this)
}

fun Any.toLong(): Long {
    return if(this is Int){
        this.toLong()
    }else{
        this as Long
    }
}