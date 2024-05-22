//
//  Coin.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//
import Foundation
import WalletCore

class Coin : ICoin {
    
    public let coinType : CoinType
    private let derivationPath : String
    
    init(coinType : CoinType , derivationPath : String){
        self.coinType = coinType
        self.derivationPath = derivationPath
    }
    
    static func createCoinByType(coinType : Int) throws -> Coin {
        switch coinType{
        case 714:
            return Binance()
        case 0:
            return BitCoin()
        case 1815:
            return Cardano()
        case 60:
            return Ethereum()
        case 501:
            return SOL()
        default:
            throw CError.UnSupportedCoin
        }
    }
    
    func getAddress(mnemonic: String, passPharse: String) throws -> String {
        let privateKey = try getNativePrivateKey(mnemonic: mnemonic, passphrase: passPharse)
        
        return coinType.deriveAddress(privateKey: privateKey)
    }
    
    func getSeed(mnemonic: String, passPharse: String) throws -> Data {
        let hdWalelt = HDWallet(mnemonic: mnemonic, passphrase: passPharse)!
        
        return hdWalelt.seed
    }
    
    func getHexPrivateKey(mnemonic: String, passPharse: String) throws -> String {
        let rawPrivateKey = try getRawPrivateKey(mnemonic: mnemonic, passPharse: passPharse)
        
        return rawPrivateKey.hexString
    }
    
    func getRawPrivateKey(mnemonic: String, passPharse: String) throws -> Data {
        let privateKey = try getNativePrivateKey(mnemonic: mnemonic, passphrase: passPharse)
        
        return privateKey.data
    }
    
    func getHexPulicKey(mnemonic: String, passPharse: String) throws -> String {
        let rawPublicKey = try getRawPublicKey(mnemonic: mnemonic, passPharse: passPharse)
        
        return rawPublicKey.hexString
    }
    
    func getRawPublicKey(mnemonic: String, passPharse: String) throws -> Data {
        let privateKey = try getNativePrivateKey(mnemonic: mnemonic, passphrase: passPharse)
        
        return privateKey.getPublicKeySecp256k1(compressed: true).data
    }
    
    func signTransaction(mnemonic: String, passPharse: String, tx: Dictionary<String, Any>) throws -> String {
        return try signTransaction(privateKey: getHexPrivateKey(mnemonic: mnemonic, passPharse: passPharse), tx: tx)
    }
    
    func getNativePrivateKey(mnemonic: String, passphrase: String) throws -> PrivateKey {
        let hdWallet: HDWallet? = HDWallet(mnemonic : mnemonic, passphrase : passphrase)
        
        guard let hdWallet =  hdWallet else {
            throw CError.InvalidMnemonic
        }

        return hdWallet.getKey(coin: coinType, derivationPath: derivationPath)
    }
    
    
    func sign(mnemonic: String, passPharse: String, message: String) throws -> String {
        guard let messageHash = Data(hexString: message) else {
            throw CError.InvalidHexArgumentError
        }
        
        let privateKey = try getNativePrivateKey(mnemonic: mnemonic, passphrase: passPharse)
        
        let sig = privateKey.sign(digest: messageHash, curve: coinType.curve)!
        
        return sig.hexString
    }
    
    func signTransaction(privateKey: String, tx : Dictionary<String, Any>) throws-> String{
        guard let rawPrivateKey = Data(hexString: privateKey) else {
            throw CError.InvalidHexArgumentError
        }
        
        guard let json = tx.toJsonString() else{
            throw CError.MissingArgumentError
        }
        
        return AnySigner.signJSON(json, key: rawPrivateKey, coin: coinType)
    }
}
