//
//  Ethereum.swift
//  trust-wallet-core-react-native
//
//  Created by Nguyen Van Toan on 21/5/24.
//

import Foundation
import WalletCore

class Ethereum : Coin{
    init() {
        super.init(coinType: CoinType.ethereum, derivationPath: "m/44'/60'/0'/0/0")
    }
    
    override func signTransaction(privateKey: String, tx: Dictionary<String, Any>) throws -> String {
        
        let transaction = tx["transaction"] as? Dictionary<String,Any>
        
        guard let transaction = transaction else {
            throw CError.MissingArgumentError
        }
        
        guard let rawPrivateKey = Data(hexString: privateKey) else {
            throw CError.InvalidHexArgumentError
        }
        
        guard let chainId = Data(hexString: tx["chainId"] as! String) else {
            throw CError.InvalidHexArgumentError
        }
        
        guard let nonce = Data(hexString: tx["nonce"] as! String) else {
            throw CError.InvalidHexArgumentError
        }
        
        guard let gasPrice = Data(hexString: tx["gasPrice"] as! String) else {
            throw CError.InvalidHexArgumentError
        }
        
        guard let gasLimit = Data(hexString: tx["gasLimit"] as! String) else {
            throw CError.InvalidHexArgumentError
        }
        
        var input = EthereumSigningInput.with {
            $0.chainID = chainId
            $0.nonce = nonce
            $0.gasPrice = gasPrice
            $0.gasLimit = gasLimit
            $0.privateKey = rawPrivateKey
        }
        
        
        switch transaction["type"] as? String {
            case "Transfer":
            input.toAddress = transaction["to"] as! String
            
            guard let amount = Data(hexString: transaction["amount"] as! String) else {
                throw CError.InvalidHexArgumentError
            }
            
            let tx = EthereumTransaction.with {
                $0.transfer = EthereumTransaction.Transfer.with{
                    $0.amount = amount
                }
            }
            input.transaction = tx
                break
        case "ERC20_Transfer":
            
            guard let amount = Data(hexString: transaction["amount"] as! String) else {
                throw CError.InvalidHexArgumentError
            }
            
            let tx = EthereumTransaction.with {
                $0.erc20Transfer = EthereumTransaction.ERC20Transfer.with{
                    $0.amount = amount
                    $0.to = transaction["to"] as! String
                }
            }
            input.transaction = tx
            
            input.toAddress = transaction["contractAddress"] as! String
            break
        case "ERC721_Transfer":
            
            guard let tokenId = Data(hexString: transaction["tokenId"] as! String) else {
                throw CError.InvalidHexArgumentError
            }
            
            let tx = EthereumTransaction.with {
                $0.erc721Transfer = EthereumTransaction.ERC721Transfer.with{
                    $0.tokenID = tokenId
                    $0.to = transaction["to"] as! String
                }
            }
            input.transaction = tx
            
            input.toAddress = transaction["contractAddress"] as! String
            break
        case "ERC20_Approve":
            
            guard let amount = Data(hexString: transaction["amount"] as! String) else {
                throw CError.InvalidHexArgumentError
            }
            
            let tx = EthereumTransaction.with {
                $0.erc20Approve = EthereumTransaction.ERC20Approve.with{
                    $0.spender = transaction["spender"] as! String
                    $0.amount = amount
                }
            }
            input.transaction = tx
            
            input.toAddress = transaction["contractAddress"] as! String
            break
            default:
                throw CError.UnsupportedTransaction
        }
        
        let signed : EthereumSigningOutput = AnySigner.sign(input: input, coin: coinType)
        
        return signed.encoded.hexString
    }
}
