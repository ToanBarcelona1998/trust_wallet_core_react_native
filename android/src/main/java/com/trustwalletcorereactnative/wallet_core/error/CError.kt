package com.trustwalletcorereactnative.wallet_core.error

enum class CEError(val message : String,val code :String){
    MissingArgumentError("Missing argument error", "MissingArgumentError"),
    UnSupportedCoin("Unsupported coin","UnSupportedCoin"),
    InvalidMnemonic("Invalid mnemonic","InvalidMnemonic"),
    Unknown("Unknown error","Unknown"),
    UnsupportedTransaction("UnSupported Transaction","UnsupportedTransaction")
}


final class CError : Exception{
    private var code : String

    constructor(message: String,code: String): super(message){
        this.code = code
    }

    public fun getCode() : String{
        return code
    }
}