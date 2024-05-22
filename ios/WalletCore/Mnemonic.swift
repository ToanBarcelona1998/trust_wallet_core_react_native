//
//  Mnemonic.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation
import WalletCore


class Mnemonic{
    static func random(strength : Int32, passPharse : String) throws -> String {
        let hdWallet : HDWallet? = HDWallet(strength: strength, passphrase: passPharse)
        
        if let hdWallet = hdWallet {
            return hdWallet.mnemonic
        }
        
        throw CError.Unknown
    }
    
    static func validateMnemonic(mnemonic : String , passPharse : String)  -> Bool {
        let hdWallet : HDWallet? = HDWallet(mnemonic: mnemonic, passphrase: passPharse)
        
        return hdWallet == nil
    }
}
