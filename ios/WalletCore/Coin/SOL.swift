//
//  SOL.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation
import WalletCore

class SOL : Coin {
    init(){
        super.init(coinType: CoinType.solana, derivationPath: "m/44'/501'/0'/0/0")
    }
    
    override func getRawPublicKey(mnemonic: String, passPharse: String) throws -> Data {
        return try getNativePrivateKey(mnemonic: mnemonic, passphrase: passPharse).getPublicKeyEd25519().data
    }
    
    override func signTransaction(privateKey: String, tx: Dictionary<String, Any>) throws-> String {
        let transaction : Dictionary<String,Any>? = tx["transaction"] as? [String:Any]
        
        guard let transaction = transaction else {
            throw CError.MissingArgumentError
        }
        
        guard let rawPrivateKey = Data(hexString: privateKey) else {
            throw CError.InvalidHexArgumentError
        }
        
        guard let feePayerPrivateKey =  Data(hexString: tx["feePayerPrivateKey"] as! String) else {
            throw CError.InvalidHexArgumentError
        }
        
        var input = SolanaSigningInput.with{
            $0.privateKey = rawPrivateKey
            $0.sender = tx["sender"] as! String
            $0.nonceAccount = tx["nonce"] as! String
            $0.recentBlockhash = tx["recentBlockHash"] as! String
            $0.feePayer = tx["feePayer"] as! String
            $0.feePayerPrivateKey = feePayerPrivateKey
        }
        
        switch transaction["type"] as? String{
        case "Transfer":
            let tx = SolanaTransfer.with {
                $0.recipient = transaction["recipient"] as! String
                $0.memo = transaction["memo"] as! String
                $0.value = transaction["value"] as! UInt64
            }
            input.transferTransaction = tx
            break
        case "Delegate_Stake":
            let tx = SolanaDelegateStake.with {
                $0.value = transaction["value"] as! UInt64
                $0.validatorPubkey = transaction["pubKey"] as! String
                $0.stakeAccount = transaction["account"] as! String
            }
            
            input.delegateStakeTransaction = tx
            break
        case "Transfer_Token":
            let tx = SolanaTokenTransfer.with {
                $0.tokenMintAddress = transaction["tokenAddress"] as! String
                $0.senderTokenAddress = transaction["sender"] as! String
                $0.recipientTokenAddress = transaction["recipient"] as! String
                $0.memo = transaction["memo"] as! String
                $0.amount = transaction["amount"] as! UInt64
            }
            input.tokenTransferTransaction = tx
            break
        default:
            break
        }
        
        let signed : SolanaSigningOutput = AnySigner.sign(input: input, coin: coinType)
        
        return signed.encoded
    }
}
