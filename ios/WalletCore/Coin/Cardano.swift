//
//  Cardano.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation
import WalletCore

class Cardano : Coin{
    init(){
        super.init(coinType: CoinType.cardano, derivationPath: "m/1852'/1815'/0'/0/0")
    }
    
    override func signTransaction(privateKey : String, tx: Dictionary<String, Any>) throws -> String {
        let utxos: [[String: Any]]? = tx["utxos"] as? [[String: Any]]
        
        guard let utxos = utxos else {
            throw CError.MissingArgumentError
        }
        
        guard let rawPrivateKey = Data(hexString: privateKey) else {
            throw CError.InvalidHexArgumentError
        }
        
        var txs : [CardanoTxInput] = []
        
        for utx in utxos {
            guard let txid = Data(hexString: utx["txid"] as! String) else {
                throw CError.InvalidHexArgumentError
            }
            
            let txInput = CardanoTxInput.with {
                $0.amount = utx["amout"] as! UInt64
                $0.outPoint.txHash = txid
                $0.outPoint.outputIndex = utx["index"] as! UInt64
                $0.address = utx["sender"] as! String
            }
            
            txs.append(txInput)
        }
        
        let input = CardanoSigningInput.with {
            $0.privateKey = [rawPrivateKey]
            $0.ttl = tx["ttl"] as! UInt64
            $0.utxos = txs
            $0.transferMessage.toAddress = tx["to"] as! String
            $0.transferMessage.changeAddress = tx["changeAddress"] as! String
            $0.transferMessage.amount = tx["amount"] as! UInt64
        }
        
        let signed : CardanoSigningOutput = AnySigner.sign(input: input, coin: coinType)
        
        return signed.encoded.hexString
    }
}
