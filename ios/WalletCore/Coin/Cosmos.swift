//
//  Cosmos.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation
import WalletCore


class Cosmos : Coin {
    init(){
        super.init(coinType: CoinType.cosmos, derivationPath: "m/44'/118'/0'/0/0")
    }
    
    override func signTransaction(privateKey: String, tx: Dictionary<String, Any>) throws -> String {
        throw CError.UnSupportedCoin
    }
}
