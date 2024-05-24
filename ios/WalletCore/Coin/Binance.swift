//
//  Binance.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation
import WalletCore

class Binance : Coin{
    init() {
        super.init(coinType: CoinType.binance, derivationPath: "m/44'/714'/0'/0/0")
    }
    
    override func signTransaction(privateKey : String, tx: Dictionary<String, Any>) throws -> String {
        
        let transaction = tx["transaction"] as? Dictionary<String,Any>
        
        guard let transaction = transaction else {
            throw CError.MissingArgumentError
        }
        
        guard let pvData = Data(hexString: privateKey) else {
            throw CError.InvalidHexArgumentError
        }
        
        guard let pv = PrivateKey(data: pvData) else {
            throw CError.Unknown
        }
        
        let publicKey = pv.getPublicKeySecp256k1(compressed: true)
        
        let token = BinanceSendOrder.Token.with {
            $0.denom = transaction["denom"] as! String
            $0.amount = transaction["amount"] as! Int64
        }
        
        let orderInput = BinanceSendOrder.Input.with {
            $0.address = AnyAddress(publicKey: publicKey, coin: coinType).data
            $0.coins = [token]
        }
        
        let orderOutput = BinanceSendOrder.Output.with {
            $0.address = AnyAddress(string: transaction["to"] as! String, coin: coinType)!.data
            $0.coins = [token]
        }
        
        var input = BinanceSigningInput.with{
            $0.chainID = tx["chainId"] as! String
            $0.memo = tx["memo"] as! String
            $0.accountNumber = tx["accountNumber"] as! Int64
            $0.source = tx["source"] as! Int64
            $0.sequence = tx["sequence"] as! Int64
            $0.privateKey = pvData
            $0.sendOrder = BinanceSendOrder.with{
                $0.inputs = [orderInput]
                $0.outputs = [orderOutput]
            }
        }
        
        let signed : BinanceSigningOutput = AnySigner.sign(input: input, coin: coinType)
        
        return signed.encoded.hexString
    }
}
