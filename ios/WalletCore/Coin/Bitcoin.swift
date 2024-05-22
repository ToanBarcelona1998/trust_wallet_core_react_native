//
//  Bitcoin.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation
import WalletCore

class BitCoin : Coin {
    init(){
        super.init(coinType: CoinType.bitcoin, derivationPath: "m/44'/0'/0'/0/0")
    }
    
    override func getAddress(mnemonic: String, passPharse: String) throws -> String {
        let privateKey = try getNativePrivateKey(mnemonic: mnemonic, passphrase: passPharse)
        
        let publicKey = privateKey.getPublicKeySecp256k1(compressed: true)
        
        let address = BitcoinAddress(publicKey: publicKey, prefix: coinType.p2pkhPrefix)!
        
        return address.description
    }
    
    override func signTransaction(privateKey: String, tx: Dictionary<String, Any>) throws -> String {
        let utxos: [[String: Any]]? = tx["utxos"] as? [[String: Any]]
        
        guard let utxos = utxos else{
            throw CError.MissingArgumentError
        }
        
        guard let rawPrivateKey = Data(hexString: privateKey) else {
            throw CError.InvalidHexArgumentError
        }
        
        var unspents : [BitcoinUnspentTransaction] = []
        
        for utx in utxos {
            guard let script = Data(hexString: utx["script"] as! String)else {
                throw CError.InvalidHexArgumentError
            }
            
            let unspent = BitcoinUnspentTransaction.with {
                $0.amount = utx["value"] as! Int64
                $0.script = script
                $0.outPoint.hash = Data.reverse(hexString: utx["txid"] as! String)
                $0.outPoint.index = utx["index"] as! UInt32
                $0.outPoint.sequence = UINT32_MAX
            }
            
            unspents.append(unspent)
        }
        
        var input = BitcoinSigningInput.with {
            $0.hashType = BitcoinScript.hashTypeForCoin(coinType: coinType)
            $0.amount = tx["amount"] as! Int64
            $0.toAddress = tx["to"] as! String
            $0.changeAddress = tx["changeAddress"] as! String
            $0.privateKey = [rawPrivateKey]
            $0.utxo = unspents
        }
        
        let transactionPlan : BitcoinTransactionPlan = AnySigner.plan(input: input, coin: coinType)
    
        input.plan = transactionPlan
        
        input.amount = transactionPlan.amount
        
        let signed : BitcoinSigningOutput = AnySigner.sign(input: input, coin: coinType)
        
        return signed.encoded.hexString
    }
}
